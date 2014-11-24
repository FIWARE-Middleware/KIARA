package com.kiara.test;

import com.kiara.serialization.Serializer;
import com.kiara.server.Servant;
import java.nio.ByteBuffer;

public abstract class CalculatorServant implements Servant
{
    public String getServiceName()
    {
        return "Calculator";
    }

    public ByteBuffer process(Serializer ser, ByteBuffer buffer, Object messageId)
    {
        final String operation = ser.deserializeOperation(buffer);

        if(operation.equals("add"))
        {
            int param1 = 0;
            int param2 = 0;

            param1 = ser.deserializeInteger(buffer);
            param2 = ser.deserializeInteger(buffer);
            int ret = add(param1, param2);
            ByteBuffer retBuffer = ByteBuffer.allocate(100);
            ser.serializeMessageId(retBuffer, messageId);
            ser.serializeInteger(retBuffer, ret);
            retBuffer.flip();
            return retBuffer;
        }
        else if(operation.equals("subtract"))
        {
            int param1 = 0;
            int param2 = 0;

            param1 = ser.deserializeInteger(buffer);
            param2 = ser.deserializeInteger(buffer);
            int ret = subtract(param1, param2);
            ByteBuffer retBuffer = ByteBuffer.allocate(100);
            ser.serializeMessageId(retBuffer, messageId);
            ser.serializeInteger(retBuffer, ret);
            retBuffer.flip();
            return retBuffer;
        }

        return null;
    }

    public abstract int add(int param1, int param2);

    public abstract int subtract(int param1, int param2);
}
