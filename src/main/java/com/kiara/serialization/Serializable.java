package com.kiara.serialization;

import com.kiara.serialization.impl.SerializerImpl;
import com.kiara.transport.impl.TransportMessage;

public interface Serializable
{
    public abstract void serialize(SerializerImpl impl, TransportMessage message, String name);

    public abstract void deserialize(SerializerImpl impl, TransportMessage message, String name);

    public String getClassName();
}
