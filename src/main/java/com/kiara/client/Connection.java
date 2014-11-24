package com.kiara.client;

import com.kiara.serialization.Serializer;
import com.kiara.transport.Transport;

public interface Connection {
    public Transport getTransport();

    public Serializer getSerializer();

    public <T> T getServiceProxy(Class<T> interfaceClass) throws Exception;

}
