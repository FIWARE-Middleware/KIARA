package org.fiware.kiara.server;

import org.fiware.kiara.serialization.Serializer;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.transport.Transport;
import org.fiware.kiara.transport.impl.TransportMessage;

public interface Servant
{
    public String getServiceName();

    public TransportMessage process(Serializer ser, Transport transport, Object messageId, BinaryInputStream bis);
}
