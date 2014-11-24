package com.kiara.test;

import com.kiara.netty.TransportMessageDispatcher;
import com.kiara.serialization.Serializer;
import com.kiara.transport.*;
import com.kiara.transport.impl.TransportMessage;
import java.nio.ByteBuffer;

class CalculatorProxy implements CalculatorClient {

    public CalculatorProxy(Serializer ser, Transport transport) {
        m_ser = ser;
        m_transport = (com.kiara.transport.impl.TransportImpl) transport;
    }

    public int add(int param1, int param2) {
        if (m_ser != null && m_transport != null) {
            ByteBuffer buffer = ByteBuffer.allocate(100);
            final Object messageId = m_ser.getNewMessageId();
            m_ser.serializeMessageId(buffer, messageId);
            m_ser.serializeService(buffer, "Calculator");
            m_ser.serializeOperation(buffer, "add");
            m_ser.serializeInteger(buffer, param1);
            m_ser.serializeInteger(buffer, param2);
            buffer.flip();

            final TransportMessage trequest = m_transport.createTransportMessage(null);
            trequest.setPayload(buffer);

            final TransportMessageDispatcher dispatcher = new TransportMessageDispatcher(messageId, m_ser, m_transport);
            m_transport.send(trequest);

            try {
                TransportMessage tresponse = dispatcher.get();
                if (tresponse != null && tresponse.getPayload() != null) {
                    int ret = m_ser.deserializeInteger(tresponse.getPayload());
                    return ret;
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

        }

        return 0;
    }

    public int subtract(int param1, int param2) {
        if (m_ser != null && m_transport != null) {
            ByteBuffer buffer = ByteBuffer.allocate(100);
            final Object messageId = m_ser.getNewMessageId();
            m_ser.serializeMessageId(buffer, messageId);
            m_ser.serializeService(buffer, "Calculator");
            m_ser.serializeOperation(buffer, "subtract");
            m_ser.serializeInteger(buffer, param1);
            m_ser.serializeInteger(buffer, param2);
            buffer.flip();

            final TransportMessage trequest = m_transport.createTransportMessage(null);
            trequest.setPayload(buffer);

            final TransportMessageDispatcher dispatcher = new TransportMessageDispatcher(messageId, m_ser, m_transport);
            m_transport.send(trequest);

            try {
                TransportMessage tresponse = dispatcher.get();
                if (tresponse != null && tresponse.getPayload() != null) {
                    int ret = m_ser.deserializeInteger(tresponse.getPayload());
                    return ret;
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        return 0;
    }

    private Serializer m_ser = null;
    private com.kiara.transport.impl.TransportImpl m_transport = null;
}
