package com.kiara.impl;

import com.kiara.client.Connection;
import com.kiara.serialization.Serializer;
import com.kiara.transport.Transport;
import java.lang.reflect.Constructor;

public class ConnectionImpl implements Connection {
    private final Transport transport;
    private final Serializer serializer;

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
