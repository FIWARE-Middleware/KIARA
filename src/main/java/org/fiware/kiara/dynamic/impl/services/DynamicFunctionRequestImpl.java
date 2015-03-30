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
 */
package org.fiware.kiara.dynamic.impl.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.fiware.kiara.dynamic.DynamicValueBuilderImpl;
import org.fiware.kiara.dynamic.services.DynamicFunctionRequest;
import org.fiware.kiara.dynamic.services.DynamicFunctionResponse;
import org.fiware.kiara.dynamic.data.DynamicData;
import org.fiware.kiara.dynamic.data.DynamicMember;
import org.fiware.kiara.dynamic.impl.DynamicTypeImpl;
import org.fiware.kiara.dynamic.impl.data.DynamicMemberImpl;
import org.fiware.kiara.exceptions.DynamicTypeException;
import org.fiware.kiara.netty.TransportMessageDispatcher;
import org.fiware.kiara.serialization.Serializer;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.transport.Transport;
import org.fiware.kiara.transport.impl.TransportImpl;
import org.fiware.kiara.transport.impl.TransportMessage;
import org.fiware.kiara.typecode.services.FunctionTypeDescriptor;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import org.fiware.kiara.client.AsyncCallback;

/**
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
*/
public class DynamicFunctionRequestImpl extends DynamicTypeImpl implements DynamicFunctionRequest {

    private final ArrayList<DynamicMember> m_parameters;
    private SerializerImpl m_serializer;
    private TransportImpl m_transport;

    public DynamicFunctionRequestImpl(FunctionTypeDescriptor typeDescriptor) {
        this(typeDescriptor, null, null);
    }

    public DynamicFunctionRequestImpl(FunctionTypeDescriptor typeDescriptor, Serializer serializer, Transport transport) {
        super(typeDescriptor, "DynamicFunctionImpl");
        this.m_parameters = new ArrayList<>();
        this.m_serializer = (SerializerImpl) serializer;
        this.m_transport = (TransportImpl) transport;
    }

    public void addParameter(DynamicData parameter, String name) {
        this.m_parameters.add(new DynamicMemberImpl(parameter, name));
    }

    /*public ArrayList<DynamicData> getParameter() {
     return this.m_parameters;
     }*/
    @Override
    public DynamicFunctionResponse execute() {
        if (this.m_serializer != null && this.m_transport != null) {
            final BinaryOutputStream bos = new BinaryOutputStream();
            final TransportMessage trequest = this.m_transport.createTransportMessage(null);
            final Object messageId = this.m_serializer.getNewMessageId();

            try {
                this.m_serializer.serializeMessageId(bos, messageId);
                this.serialize(this.m_serializer, bos, "");

                /*this.m_serializer.serializeService(bos, "Calculator");
                 this.m_serializer.serializeOperation(bos, "add");
                 this.m_serializer.serializeFloat32(bos, "", n1);
                 this.m_serializer.serializeFloat32(bos, "", n2);*/
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            trequest.setPayload(bos.getByteBuffer());

            final TransportMessageDispatcher dispatcher = new TransportMessageDispatcher(messageId, this.m_serializer, this.m_transport);

            this.m_transport.send(trequest);

            try {
                TransportMessage tresponse;
                tresponse = dispatcher.get();
                if (tresponse != null && tresponse.getPayload() != null) {
                    final BinaryInputStream bis = BinaryInputStream.fromByteBuffer(tresponse.getPayload());

                    DynamicFunctionResponse ret = DynamicValueBuilderImpl.getInstance().createFunctionResponse((FunctionTypeDescriptor) this.m_typeDescriptor);

                    // Deserialize response message ID
                    final Object responseMessageId = this.m_serializer.deserializeMessageId(bis);

                    ret.deserialize(this.m_serializer, bis, "");

                    return ret;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    @Override
    public void executeAsync(final AsyncCallback<DynamicFunctionResponse> callback) {
        if (this.m_serializer != null && this.m_transport != null) {
            final BinaryOutputStream bos = new BinaryOutputStream();
            final TransportMessage trequest = this.m_transport.createTransportMessage(null);
            final Object messageId = this.m_serializer.getNewMessageId();

            try {
                this.m_serializer.serializeMessageId(bos, messageId);
                this.serialize(this.m_serializer, bos, "");

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            trequest.setPayload(bos.getByteBuffer());

            final TransportMessageDispatcher dispatcher = new TransportMessageDispatcher(messageId, this.m_serializer, this.m_transport);

            Futures.addCallback(dispatcher, new FutureCallback<TransportMessage>() {

                @Override
                public void onSuccess(TransportMessage result) {
                    if (result != null && result.getPayload() != null) {
                        try {
                            final BinaryInputStream bis = BinaryInputStream.fromByteBuffer(result.getPayload());

                            DynamicFunctionResponse ret = DynamicValueBuilderImpl.getInstance().createFunctionResponse((FunctionTypeDescriptor) m_typeDescriptor);

                            // Deserialize response message ID
                            final Object responseMessageId = m_serializer.deserializeMessageId(bis);

                            ret.deserialize(m_serializer, bis, "");

                            callback.onSuccess(ret);
                        } catch (IOException ex) {
                            onFailure(ex);
                        }
                    }

                }

                @Override
                public void onFailure(Throwable t) {
                    callback.onFailure(t);
                }

            });

            this.m_transport.send(trequest);

        }

    }

    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        if (((FunctionTypeDescriptor) this.m_typeDescriptor).getServiceName() != null) {
            impl.serializeService(message, ((FunctionTypeDescriptor) this.m_typeDescriptor).getServiceName());
        }
        impl.serializeOperation(message, ((FunctionTypeDescriptor) this.m_typeDescriptor).getName());

        for (DynamicMember parameter : this.m_parameters) {
            parameter.getDynamicData().serialize(impl, message, name);
        }
    }

    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        String function_name = impl.deserializeOperation(message);

        if (((FunctionTypeDescriptor) this.m_typeDescriptor).getName().equals(function_name)) {
            for (DynamicMember parameter : this.m_parameters) {
                parameter.getDynamicData().deserialize(impl, message, name);
            }
        } else {
            throw new DynamicTypeException(this.m_className + " - The deserialized function name does not match the one described in the type descriptor.");
        }

    }

    @Override
    public DynamicData getParameter(String name) {
        for (DynamicMember param : this.m_parameters) {
            if (param.getName().equals(name)) {
                return param.getDynamicData();
            }
        }
        return null;
    }

    @Override
    public DynamicData getParameterAt(int index) {
        return this.m_parameters.get(index).getDynamicData();
    }

    @Override
    public boolean equals(Object anotherObject) {
        if (anotherObject instanceof DynamicFunctionRequestImpl) {
            return this.m_parameters.equals(((DynamicFunctionRequestImpl) anotherObject).m_parameters);
        }
        return false;
    }

}
