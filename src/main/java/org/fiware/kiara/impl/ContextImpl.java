package org.fiware.kiara.impl;

import org.fiware.kiara.client.Connection;
import org.fiware.kiara.Context;
import org.fiware.kiara.server.Server;
import org.fiware.kiara.server.Service;
import org.fiware.kiara.serialization.Serializer;
import org.fiware.kiara.serialization.impl.CDRSerializer;
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
import org.fiware.kiara.config.ServerConfiguration;
import org.fiware.kiara.exceptions.ConnectException;
import org.fiware.kiara.netty.URILoader;
import org.fiware.kiara.transport.http.HttpTransportFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextImpl implements Context {

    private static final Logger logger = LoggerFactory.getLogger(ContextImpl.class);

    private static final Map<String, TransportFactory> transportFactories = new HashMap<>();

    // FIXME this initialization is hardcoded
    static {
        registerTransportFactory(new TcpBlockTransportFactory(/*secure = */false));
        registerTransportFactory(new HttpTransportFactory(/*secure = */false));
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

    private final void loadIDL(String idlContents, String fileName) throws IOException {

    }

    public Connection connect(String url, boolean dummy) throws IOException { // TODO delete
        try {
            URI uri = new URI(url);

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
                    loadIDL(serverConfig.idlContents, configUri.toString());
                } else if (serverConfig.idlURL != null && !serverConfig.idlURL.isEmpty()) {
                    URI idlUri = configUri.resolve(serverConfig.idlURL);
                    String idlContents = URILoader.load(idlUri, "UTF-8");

                    logger.debug("IDL CONTENTS: {}", idlContents); //???DEBUG

                    loadIDL(idlContents, idlUri.toString());
                } else {
                    throw new ConnectException("No IDL specified in server configuration");
                }

                throw new UnsupportedOperationException("IDL parsing is not supported yet");
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

                // We should perform here negotation, but for now only a fixed transport/protocol combination
                final Transport transport = createTransport(url);
                final Serializer serializer = createSerializer(serializerName);

                return new ConnectionImpl(transport, serializer, dummy);
            }
        } catch (URISyntaxException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public Connection connect(Transport transport, Serializer serializer, boolean dummy) throws IOException {
        return new ConnectionImpl(transport, serializer, dummy);
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
        if (!"cdr".equals(name)) {
            throw new IOException("Unsupported serializer: " + name);
        }
        return new CDRSerializer();
    }

    @Override
    public void close() throws IOException {

    }

}
