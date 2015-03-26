package org.fiware.kiara.client;

import org.fiware.kiara.dynamic.services.DynamicProxy;
import org.fiware.kiara.serialization.Serializer;
import org.fiware.kiara.transport.Transport;

public interface Connection {
    public Transport getTransport();

    public Serializer getSerializer();

    public <T> T getServiceProxy(Class<T> interfaceClass) throws Exception;
    
    public DynamicProxy getDynamicProxy(String name);

}
