package com.kiara.calculator;

import com.kiara.netty.TransportMessageDispatcher;
import com.kiara.serialization.impl.SerializerImpl;
import com.kiara.transport.*;
import com.kiara.transport.impl.TransportMessage;

import java.nio.ByteBuffer;

class CalculatorProxy implements CalculatorClient {

    public CalculatorProxy(SerializerImpl ser, Transport transport) {
        m_ser = ser;
        m_transport = (com.kiara.transport.impl.TransportImpl) transport;
    }

    public int add(int param1, int param2) {
        if (m_ser != null && m_transport != null) {
            ByteBuffer buffer = ByteBuffer.allocate(100);
            final TransportMessage trequest = m_transport.createTransportMessage(null);
            trequest.setPayload(buffer);

            final Object messageId = m_ser.getNewMessageId();
            m_ser.serializeMessageId(trequest, messageId);
            m_ser.serializeService(trequest, "Calculator");
            m_ser.serializeOperation(trequest, "add");
            m_ser.serializeI32(trequest, "", param1);
            m_ser.serializeI32(trequest, "", param2);
            buffer.flip();

            final TransportMessageDispatcher dispatcher = new TransportMessageDispatcher(messageId, m_ser, m_transport);
            m_transport.send(trequest);

            try {
                TransportMessage tresponse = dispatcher.get();
                if (tresponse != null && tresponse.getPayload() != null) {
                    int ret = m_ser.deserializeI32(tresponse, "");
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
            final TransportMessage trequest = m_transport.createTransportMessage(null);
            trequest.setPayload(buffer);

            final Object messageId = m_ser.getNewMessageId();
            m_ser.serializeMessageId(trequest, messageId);
            m_ser.serializeService(trequest, "Calculator");
            m_ser.serializeOperation(trequest, "subtract");
            m_ser.serializeI32(trequest, "", param1);
            m_ser.serializeI32(trequest, "", param2);
            buffer.flip();

            final TransportMessageDispatcher dispatcher = new TransportMessageDispatcher(messageId, m_ser, m_transport);
            m_transport.send(trequest);

            try {
                TransportMessage tresponse = dispatcher.get();
                if (tresponse != null && tresponse.getPayload() != null) {
                    int ret = m_ser.deserializeI32(tresponse, "");
                    return ret;
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        return 0;
    }

    private SerializerImpl m_ser = null;
    private com.kiara.transport.impl.TransportImpl m_transport = null;
}
