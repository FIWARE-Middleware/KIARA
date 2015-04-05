 /* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2014 Proyectos y Sistemas de Mantenimiento S.L. (eProsima)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * @file StructServiceProxy.java
 * This file contains the proxy implementation.
 *
 * This file was generated by using the tool Kiaragen.
 *
 */
package org.fiware.kiara.struct;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import java.io.IOException;

import org.fiware.kiara.netty.TransportMessageDispatcher;
import org.fiware.kiara.serialization.Serializer;
import org.fiware.kiara.transport.Transport;
import org.fiware.kiara.transport.impl.TransportMessage;

import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.client.AsyncCallback;

/**
 * Class containing the proxy implementation for all the services.
 *
 * @author Kiaragen tool.
 *
 */
class StructServiceProxy implements StructServiceClient {

    public static final String serviceName = "StructService";

    public StructServiceProxy(Serializer ser, Transport transport) {
        m_ser = (org.fiware.kiara.serialization.impl.SerializerImpl) ser;
        m_transport = (org.fiware.kiara.transport.impl.TransportImpl) transport;
    }


    @Override
    public PrimitiveTypesStruct sendReceivePrimitives(/*in*/PrimitiveTypesStruct value) {
        if (m_ser != null && m_transport != null) {
			final BinaryOutputStream bos = new BinaryOutputStream();
            final TransportMessage trequest = m_transport.createTransportMessage(null);

            final Object messageId = m_ser.getNewMessageId();
            try {
                m_ser.serializeMessageId(bos, messageId);
                m_ser.serializeService(bos, "StructService");
                m_ser.serializeOperation(bos, "sendReceivePrimitives");
                m_ser.serialize(bos, "", value);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            trequest.setPayload(bos.getByteBuffer());

            final TransportMessageDispatcher dispatcher = new TransportMessageDispatcher(messageId, m_ser, m_transport);
            m_transport.send(trequest);

            try {
                TransportMessage tresponse = dispatcher.get();
                if (tresponse != null && tresponse.getPayload() != null) {
                    final BinaryInputStream bis = BinaryInputStream.fromByteBuffer(tresponse.getPayload());

                    // Deserialize response message ID
                    final Object responseMessageId = m_ser.deserializeMessageId(bis);

                    // Deserialize return code (0 = OK, anything else = WRONG)
                    int retCode = m_ser.deserializeUI32(bis, "");
                    if (retCode == 0) { // Function execution was OK.
                        PrimitiveTypesStruct ret = m_ser.deserialize(bis, "", PrimitiveTypesStruct.class);
                        return ret;
                    }

                }
	        }
	        catch (Exception ex) {
                throw new RuntimeException(ex);
            }

        }

        return null;
    }

    @Override
    public OuterStruct sendReceiveStruct(/*in*/OuterStruct value) {
        if (m_ser != null && m_transport != null) {
			final BinaryOutputStream bos = new BinaryOutputStream();
            final TransportMessage trequest = m_transport.createTransportMessage(null);

            final Object messageId = m_ser.getNewMessageId();
            try {
                m_ser.serializeMessageId(bos, messageId);
                m_ser.serializeService(bos, "StructService");
                m_ser.serializeOperation(bos, "sendReceiveStruct");
                m_ser.serialize(bos, "", value);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            trequest.setPayload(bos.getByteBuffer());

            final TransportMessageDispatcher dispatcher = new TransportMessageDispatcher(messageId, m_ser, m_transport);
            m_transport.send(trequest);

            try {
                TransportMessage tresponse = dispatcher.get();
                if (tresponse != null && tresponse.getPayload() != null) {
                    final BinaryInputStream bis = BinaryInputStream.fromByteBuffer(tresponse.getPayload());

                    // Deserialize response message ID
                    final Object responseMessageId = m_ser.deserializeMessageId(bis);

                    // Deserialize return code (0 = OK, anything else = WRONG)
                    int retCode = m_ser.deserializeUI32(bis, "");
                    if (retCode == 0) { // Function execution was OK.
                        OuterStruct ret = m_ser.deserialize(bis, "", OuterStruct.class);
                        return ret;
                    }

                }
	        }
	        catch (Exception ex) {
                throw new RuntimeException(ex);
            }

        }

        return null;
    }

    @Override
	public void sendReceivePrimitives(/*in*/ PrimitiveTypesStruct value, final AsyncCallback<PrimitiveTypesStruct> callback) {

        if (m_ser != null && m_transport != null) {
			final BinaryOutputStream bos = new BinaryOutputStream();
            final TransportMessage trequest = m_transport.createTransportMessage(null);

            final Object messageId = m_ser.getNewMessageId();
            try {
                m_ser.serializeMessageId(bos, messageId);
                m_ser.serializeService(bos, "StructService");
                m_ser.serializeOperation(bos, "sendReceivePrimitives");
                m_ser.serialize(bos, "", value);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            trequest.setPayload(bos.getByteBuffer());

            final TransportMessageDispatcher dispatcher = new TransportMessageDispatcher(messageId, m_ser, m_transport);

            Futures.addCallback(dispatcher, new FutureCallback<TransportMessage>() {

                @Override
                public void onSuccess(TransportMessage result) {
					StructServiceProcess.sendReceivePrimitives_processAsync(result, m_ser, callback);
                }

                @Override
                public void onFailure(Throwable t) {
                    callback.onFailure(t);
                }

            });

            m_transport.send(trequest);

        }

        return;
    }

    @Override
	public void sendReceiveStruct(/*in*/ OuterStruct value, final AsyncCallback<OuterStruct> callback) {

        if (m_ser != null && m_transport != null) {
			final BinaryOutputStream bos = new BinaryOutputStream();
            final TransportMessage trequest = m_transport.createTransportMessage(null);

            final Object messageId = m_ser.getNewMessageId();
            try {
                m_ser.serializeMessageId(bos, messageId);
                m_ser.serializeService(bos, "StructService");
                m_ser.serializeOperation(bos, "sendReceiveStruct");
                m_ser.serialize(bos, "", value);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            trequest.setPayload(bos.getByteBuffer());

            final TransportMessageDispatcher dispatcher = new TransportMessageDispatcher(messageId, m_ser, m_transport);

            Futures.addCallback(dispatcher, new FutureCallback<TransportMessage>() {

                @Override
                public void onSuccess(TransportMessage result) {
					StructServiceProcess.sendReceiveStruct_processAsync(result, m_ser, callback);
                }

                @Override
                public void onFailure(Throwable t) {
                    callback.onFailure(t);
                }

            });

            m_transport.send(trequest);

        }

        return;
    }

    private org.fiware.kiara.serialization.impl.SerializerImpl m_ser = null;
    private org.fiware.kiara.transport.impl.TransportImpl m_transport = null;

}
