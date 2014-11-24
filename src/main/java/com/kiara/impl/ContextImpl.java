package com.kiara.impl;

import com.kiara.client.Connection;
import com.kiara.Context;
import com.kiara.server.Server;
import com.kiara.server.Service;
import com.kiara.serialization.Cdr;
import com.kiara.serialization.Serializer;
import com.kiara.transport.ServerTransport;
import com.kiara.transport.Transport;
import com.kiara.transport.impl.TransportFactory;
import com.kiara.transport.tcp.TcpBlockTransportFactory;
import io.netty.handler.codec.http.QueryStringDecoder;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContextImpl implements Context {

    private static final Map<String, TransportFactory> transportFactories = new HashMap<String, TransportFactory>();

    // FIXME this initialization is hardcoded
    static {
        registerTransportFactory(new TcpBlockTransportFactory(/*secure = */false));
    }

    private static TransportFactory getTransportFactoryByName(String transportName) {
        synchronized (transportFactories) {
            return transportFactories.get(transportName);
        }
    }

    public static TransportFactory getTransportFactoryByURI(String uri) throws URISyntaxException {
        return getTransportFactoryByURI(new URI(uri));
    }

    private static TransportFactory getTransportFactoryByURI(URI uri) {
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

    private static synchronized void registerTransportFactory(TransportFactory transportFactory) {
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

    public Connection connect(String url) throws IOException {
        try {
            URI uri = new URI(url);
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

            return new ConnectionImpl(transport, serializer);
        } catch (URISyntaxException ex) {
            throw new IOException(ex);
        }
    }

    public Connection connect(Transport transport, Serializer serializer) throws IOException {
        return new ConnectionImpl(transport, serializer);
    }

    public Service createService() {
        return new ServiceImpl();
    }

    // Create server without negotiation
    public Server createServer() {
        return new ServerImpl(this);
    }

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

    public Serializer createSerializer(String name) throws IOException {
        if (!"cdr".equals(name)) {
            throw new IOException("Unsupported serializer: " + name);
        }
        return new Cdr();
    }

}
