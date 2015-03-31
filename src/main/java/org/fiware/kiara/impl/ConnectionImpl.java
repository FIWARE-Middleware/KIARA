package org.fiware.kiara.impl;

import org.fiware.kiara.client.Connection;
import org.fiware.kiara.dynamic.services.DynamicProxy;
import org.fiware.kiara.serialization.Serializer;
import org.fiware.kiara.transport.Transport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class ConnectionImpl implements Connection {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionImpl.class);
    private final Serializer serializer;
    private final Transport transport;

    private final List<DynamicProxy> m_dynamicServices;

    public ConnectionImpl(Transport transport, Serializer serializer, ParserContextImpl ctx) {
        super();
        this.transport = transport;
        this.serializer = serializer;
        this.m_dynamicServices = (ctx != null ? TypeMapper.processTree(ctx, this.serializer, this.transport) : new ArrayList<DynamicProxy>());
    }

    @Override
    public Transport getTransport() {
        return transport;
    }

    @Override
    public Serializer getSerializer() {
        return serializer;
    }

    @Override
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

    @Override
    public DynamicProxy getDynamicProxy(String name) {
        for (DynamicProxy service : this.m_dynamicServices) {
            if (service.getServiceName().equals(name)) {
                return service;
            }
        }
        return null;
    }

}
