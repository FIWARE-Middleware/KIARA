package com.kiara.server;

import com.kiara.serialization.Serializer;
import com.kiara.transport.Transport;
import com.kiara.transport.impl.TransportMessage;

public interface Servant
{
    public String getServiceName();

    public TransportMessage process(Serializer ser, Transport transport, TransportMessage message, Object messageId);
}
