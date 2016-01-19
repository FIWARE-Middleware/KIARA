package org.fiware.kiara.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.fiware.kiara.client.Connection;
import org.fiware.kiara.Context;
import org.fiware.kiara.server.Server;
import org.fiware.kiara.server.Service;
import org.fiware.kiara.serialization.Serializer;
import org.fiware.kiara.transport.ServerTransport;
import org.fiware.kiara.transport.Transport;
import org.fiware.kiara.transport.TransportFactory;
import org.fiware.kiara.transport.tcp.TcpBlockTransportFactory;

import io.netty.handler.codec.http.QueryStringDecoder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.fiware.kiara.config.EndpointInfo;
import org.fiware.kiara.config.ServerConfiguration;
import org.fiware.kiara.config.ServerInfo;
import org.fiware.kiara.exceptions.ConnectException;
import org.fiware.kiara.netty.URILoader;
import org.fiware.kiara.serialization.SerializerFactory;
import org.fiware.kiara.serialization.impl.CDRSerializerFactory;
import org.fiware.kiara.transport.http.HttpTransportFactory;
import org.fiware.kiara.typecode.services.ServiceTypeDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextImpl implements Context {

    private static final Logger logger = LoggerFactory.getLogger(ContextImpl.class);

    private static final Map<String, TransportFactory> transportFactories = new HashMap<>();
    private static final Map<String, SerializerFactory> serializerFactories = new HashMap<>();

    // FIXME this initialization is hardcoded
    static {
        registerTransportFactory(new TcpBlockTransportFactory(/*secure = */false));
        registerTransportFactory(new TcpBlockTransportFactory(true));
        registerTransportFactory(new HttpTransportFactory(/*secure = */false));
        registerTransportFactory(new HttpTransportFactory(true));
        registerSerializerFactory(new CDRSerializerFactory());
    }

    private static SerializerFactory getSerializerFactoryByName(String serializerName) {
        synchronized (serializerFactories) {
            return serializerFactories.get(serializerName);
        }
    }

    private static TransportFactory getTransportFactoryByName(String transportName) {
        synchronized (transportFactories) {
            return transportFactories.get(transportName);
        }
    }

    public static TransportFactory getTransportFactoryByURI(String uri) throws URISyntaxException {
        return getTransportFactoryByURI(new URI(uri));
    }

    public static TransportFactory getTransportFactoryByURI(URI uri) {
        final String scheme = uri.getScheme();
        if (scheme == null) {
            return null;
        }
        return getTransportFactoryByName(scheme);
    }

    public static void registerTransportFactory(String transportName, TransportFactory transportFactory) {
        if (transportName == null) {
            throw new NullPointerException("transportName");
        }
        if (transportFactory == null) {
            throw new NullPointerException("transportFactory");
        }
        synchronized (transportFactories) {
            transportFactories.put(transportName, transportFactory);
        }
    }

    private static void registerTransportFactory(TransportFactory transportFactory) {
        if (transportFactory == null) {
            throw new NullPointerException("transportFactory");
        }
        final String transportName = transportFactory.getName();
        if (transportName == null) {
            throw new NullPointerException("transportName");
        }
        synchronized (transportFactories) {
            transportFactories.put(transportName, transportFactory);
        }
    }

    public static void registerSerializerFactory(String serializerName, SerializerFactory serializerFactory) {
        if (serializerName == null) {
            throw new NullPointerException("serializerName");
        }
        if (serializerFactory == null) {
            throw new NullPointerException("serializerFactory");
        }
        synchronized (serializerFactories) {
            serializerFactories.put(serializerName, serializerFactory);
        }
    }

    private static void registerSerializerFactory(SerializerFactory serializerFactory) {
        if (serializerFactory == null) {
            throw new NullPointerException("serializerFactory");
        }
        final String serializerName = serializerFactory.getName();
        if (serializerName == null) {
            throw new NullPointerException("serializerName");
        }
        synchronized (serializerFactories) {
            serializerFactories.put(serializerName, serializerFactory);
        }
    }

    @Override
    public Connection connect(String url) throws IOException {
        try {
            Transport transport = null;
            Serializer serializer = null;
            ParserContextImpl ctx = null;

            final URI uri = new URI(url);
            
            ListMultimap<String, EndpointInfo> serviceProviders = ArrayListMultimap.create();

            if (uri.getScheme().equals("kiara")) {
                final URI configUri = new URI("http",
                        uri.getUserInfo(), uri.getHost(), uri.getPort(),
                        uri.getPath(), uri.getQuery(),
                        uri.getFragment());

                // 1. load server configuration
                String configText;
                try {
                    configText = URILoader.load(configUri, "UTF-8");
                } catch (IOException ex) {
                    throw new ConnectException("Could not load server configuration", ex);
                }

                logger.debug("Config text: {}", configText);

                ServerConfiguration serverConfig;
                try {
                    serverConfig = ServerConfiguration.fromJson(configText);
                } catch (IOException ex) {
                    throw new ConnectException("Could not parse server configuration", ex);
                }

                //???DEBUG BEGIN
                if (logger.isDebugEnabled()) {
                    try {
                        logger.debug(serverConfig.toJson());
                        //System.err.println(serverConfig.toJson());
                    } catch (IOException ex) {
                        throw new ConnectException("Could not convert to JSON", ex);
                    }
                }
                //???DEBUG END

                // load IDL
                if (serverConfig.idlContents != null && !serverConfig.idlContents.isEmpty()) {
                    ctx = IDLUtils.loadIDL(serverConfig.idlContents, configUri.toString());
                } else if (serverConfig.idlURL != null && !serverConfig.idlURL.isEmpty()) {
                    URI idlUri = configUri.resolve(serverConfig.idlURL);
                    String idlContents = URILoader.load(idlUri, "UTF-8");

                    logger.debug("IDL CONTENTS: {}", idlContents); //???DEBUG

                    ctx = IDLUtils.loadIDL(idlContents, idlUri.toString());
                } else {
                    throw new ConnectException("No IDL specified in server configuration");
                }

                List<ServiceTypeDescriptor> allServiceTypes = TypeMapper.getServiceTypes(ctx);
                // build service map
                Map<String, ServiceTypeDescriptor> serviceNameMap = new HashMap<>();
                for (ServiceTypeDescriptor serviceType : allServiceTypes) {
                    serviceNameMap.put(serviceType.getScopedName(), serviceType);
                }

                // 2. perform negotation
                // find matching endpoint

                for (ServerInfo si : serverConfig.servers) {
                    final TransportFactory t = ContextImpl.getTransportFactoryByName(si.transport.name);
                    if (t != null) {
                        final SerializerFactory s = ContextImpl.getSerializerFactoryByName(si.protocol.name);
                        if (s != null) {
                            final EndpointInfo esi = new EndpointInfo(si, t, s);

                            // 1. pass: determine supported services
                            for (String serviceName : si.services) {
                                if (serviceName.isEmpty() || "*".equals(serviceName)) {
                                    esi.serviceTypes.addAll(allServiceTypes);
                                } else {
                                    final ServiceTypeDescriptor serviceType = serviceNameMap.get(serviceName);
                                    if (serviceType != null) {
                                        esi.serviceTypes.add(serviceType);
                                    }
                                }
                            }

                            // 2. pass: register endpoint for each service name
                            for (ServiceTypeDescriptor serviceType : esi.serviceTypes) {
                                serviceProviders.put(serviceType.getScopedName(), esi);
                            }
                        }
                    }
                }

                if (serviceProviders.isEmpty()) { // TODO Check if user's configuration in connection chain matches the server
                    throw new ConnectException("No matching endpoint found");
                }

                return new ConnectionImpl(configUri, serviceProviders);
            } else {
                QueryStringDecoder decoder = new QueryStringDecoder(uri);
                
                String serializerName = null;
                
                List<String> parameters = decoder.parameters().get("serialization");
                if (parameters != null && !parameters.isEmpty()) {
                    serializerName = parameters.get(0);
                }
                
                if (serializerName == null) {
                    throw new IllegalArgumentException("No serializer is specified as a part of the URI");
                }
                
                final TransportFactory transportFactory = getTransportFactoryByURI(uri);
                
                final SerializerFactory serializerFactory = getSerializerFactoryByName(serializerName);

                // create pseudo endpoint
                final ServerInfo si = new ServerInfo();
                si.protocol.name = serializerName;
                si.transport.name = transportFactory.getName();
                si.transport.url = uri.toString();
                final EndpointInfo esi = new EndpointInfo(si, transportFactory, serializerFactory);
                
                // register endpoint for all services
                serviceProviders.put("*", esi);
                
                return new ConnectionImpl(null, serviceProviders);
            }
        } catch (URISyntaxException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public Connection connect(Transport transport, Serializer serializer) throws IOException {
        return new ConnectionImpl(transport, serializer);
    }

    @Override
    public Service createService() {
        return new ServiceImpl();
    }

    // Create server without negotiation
    @Override
    public Server createServer() {
        return new ServerImpl(this);
    }

    @Override
    public Transport createTransport(String url) throws IOException {
        if (url == null) {
            throw new NullPointerException("url");
        }

        try {
            URI uri = new URI(url);

            final TransportFactory factory = getTransportFactoryByURI(uri);
            if (factory == null) {
                throw new IOException("Unsupported transport URI " + url);
            }
            return factory.createTransport(url, null).get();
        } catch (Exception ex) {
            throw new IOException(ex);
        }
    }

    public Transport createTransport(URI uri) throws IOException {
        if (uri == null) {
            throw new NullPointerException("uri");
        }

        try {
            final TransportFactory factory = getTransportFactoryByURI(uri);
            if (factory == null) {
                throw new IOException("Unsupported transport URI " + uri);
            }
            return factory.createTransport(uri.toString(), null).get();
        } catch (Exception ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public ServerTransport createServerTransport(String url) throws IOException {
        try {
            URI uri = new URI(url);

            final TransportFactory factory = getTransportFactoryByURI(uri);
            if (factory == null) {
                throw new IOException("Unsupported transport URI " + url);
            }
            return factory.createServerTransport(url);

        } catch (Exception ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public Serializer createSerializer(String name) throws IOException {
        try {
            final SerializerFactory factory = getSerializerFactoryByName(name);
            if (factory == null) {
                throw new IOException("Unsupported serializer: " + name);
            }
            return factory.createSerializer();
        } catch (Exception ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public void close() throws IOException {

    }

}
