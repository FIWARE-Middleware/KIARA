package org.fiware.kiara.impl;

import org.fiware.kiara.client.Connection;
import org.fiware.kiara.exceptions.ConnectException;
import org.fiware.kiara.exceptions.impl.InvalidAddressException;
import org.fiware.kiara.serialization.Serializer;
import org.fiware.kiara.transport.Transport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

public class ConnectionImpl implements Connection {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionImpl.class);
    private final Serializer serializer;
    private final Transport transport;
    public ConnectionImpl(Transport transport, Serializer serializer) {
        super();
        this.transport = transport;
        this.serializer = serializer;
    }
    
    public Transport getTransport() {
        return transport;
    }

    public Serializer getSerializer() {
        return serializer;
    }

    public <T> T getServiceProxy(Class<T> interfaceClass) throws Exception {
        // name of the interface class can end with 'Async' or 'Client'
        String interfaceName = interfaceClass.getName();
        if (interfaceName.endsWith("Async"))
            interfaceName = interfaceName.substring(0, interfaceName.length()-5);
        else if (interfaceName.endsWith("Client"))
            interfaceName = interfaceName.substring(0, interfaceName.length()-6);
        final String proxyClassName = interfaceName+"Proxy";
        Class<?> proxyClass = Class.forName(proxyClassName);
        if (!interfaceClass.isAssignableFrom(proxyClass))
            throw new RuntimeException("Proxy class "+proxyClass+" does not implement interface "+interfaceClass);
        Constructor<?> proxyConstr = proxyClass.getConstructor(Serializer.class, Transport.class);
        proxyConstr.setAccessible(true);
        return interfaceClass.cast(proxyConstr.newInstance(serializer, transport));
    }

}
