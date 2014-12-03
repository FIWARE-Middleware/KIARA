package com.kiara.calculator;

import com.kiara.serialization.Serializer;
import com.kiara.serialization.impl.SerializerImpl;
import com.kiara.server.Servant;
import com.kiara.transport.Transport;
import com.kiara.transport.impl.TransportImpl;
import com.kiara.transport.impl.TransportMessage;

import java.nio.ByteBuffer;

public abstract class CalculatorServant implements Servant
{
    public String getServiceName()
    {
        return "Calculator";
    }

    @Override
    public TransportMessage process(Serializer ser, Transport transport, TransportMessage message, Object messageId)
    {
        SerializerImpl serImpl;
        TransportImpl transportImpl;

        if (ser instanceof SerializerImpl && transport instanceof TransportImpl) {
            serImpl = (SerializerImpl) ser;
            transportImpl = (TransportImpl) transport;
        } else {
            return null; // TODO Raise error
        }


        final String operation = serImpl.deserializeOperation(message);

        if(operation.equals("add"))
        {
            int param1 = 0;
            int param2 = 0;

            param1 = serImpl.deserializeI32(message, "");
            param2 = serImpl.deserializeI32(message, "");
            int ret = add(param1, param2);
            ByteBuffer retBuffer = ByteBuffer.allocate(100);
            TransportMessage retMsg = transportImpl.createTransportMessage(null);
            retMsg.setPayload(retBuffer);
            serImpl.serializeMessageId(retMsg, messageId);
            serImpl.serializeI32(retMsg, "", ret);
            retMsg.getPayload().flip();
            return retMsg;
        }
        else if(operation.equals("subtract"))
        {
            int param1 = 0;
            int param2 = 0;

            param1 = serImpl.deserializeI32(message, "");
            param2 = serImpl.deserializeI32(message, "");
            int ret = add(param1, param2);
            ByteBuffer retBuffer = ByteBuffer.allocate(100);
            TransportMessage retMsg = transportImpl.createTransportMessage(null);
            retMsg.setPayload(retBuffer);
            serImpl.serializeMessageId(retMsg, messageId);
            serImpl.serializeI32(retMsg, "", ret);
            retMsg.getPayload().flip();
            return retMsg;
        }

        return null;
    }

    public abstract int add(int param1, int param2);

    public abstract int subtract(int param1, int param2);
}