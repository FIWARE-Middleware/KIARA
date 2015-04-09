/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2015 German Research Center for Artificial Intelligence (DFKI)
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
import java.util.HashMap;
import java.util.Map;

import org.fiware.kiara.Kiara;
import org.fiware.kiara.dynamic.DynamicValueBuilder;
import org.fiware.kiara.dynamic.services.DynamicFunctionHandler;
import org.fiware.kiara.dynamic.services.DynamicFunctionRequest;
import org.fiware.kiara.dynamic.services.DynamicFunctionResponse;
import org.fiware.kiara.serialization.Serializer;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.server.Servant;
import org.fiware.kiara.transport.Transport;
import org.fiware.kiara.transport.impl.TransportImpl;
import org.fiware.kiara.transport.impl.TransportMessage;
import org.fiware.kiara.typecode.impl.FunctionTypeDescriptor;

/**
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public class DynamicServant implements Servant {

    private final String serviceName;
    private final Map<String, FunctionTypeAndHandler> handlers;

    public static class FunctionTypeAndHandler {

        public final FunctionTypeDescriptor typeDesc;
        public final DynamicFunctionHandler handler;

        public FunctionTypeAndHandler(FunctionTypeDescriptor typeDesc, DynamicFunctionHandler handler) {
            this.typeDesc = typeDesc;
            this.handler = handler;
        }
    }

    public DynamicServant(String serviceName) {
        this(serviceName, null);
    }

    public DynamicServant(String serviceName, Map<String, FunctionTypeAndHandler> handlers) {
        this.serviceName = serviceName;
        this.handlers = handlers == null ? new HashMap<String, FunctionTypeAndHandler>() : handlers;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    public void addFunctionHandler(FunctionTypeDescriptor typeDesc, DynamicFunctionHandler handler) {
        handlers.put(typeDesc.getName(), new FunctionTypeAndHandler(typeDesc, handler));
    }

    public FunctionTypeAndHandler removeFunctionHandler(FunctionTypeDescriptor typeDesc) {
        return handlers.remove(typeDesc.getName());
    }

    @Override
    public TransportMessage process(Serializer ser, TransportMessage message, Transport transport, Object messageId, BinaryInputStream bis) {
        final SerializerImpl serImpl = (SerializerImpl) ser;
        final TransportImpl transportImpl = (TransportImpl) transport;

        try {
            // FIXME: We need to save and restore current position since DynamicFunctionRequest will deserialize operation name as well.
            //        THIS IS POSSIBLY A WRONG APPROACH !!!
            final int savedPos = bis.getPosition();
            final String operation = serImpl.deserializeOperation(bis);
            bis.setPosition(savedPos);

            final FunctionTypeAndHandler typeAndHandler = handlers.get(operation);
            if (typeAndHandler != null) {
                final DynamicValueBuilder valueBuilder = Kiara.getDynamicValueBuilder();
                final DynamicFunctionRequest req = valueBuilder.createFunctionRequest(typeAndHandler.typeDesc, serImpl, transportImpl);
                final DynamicFunctionResponse res = valueBuilder.createFunctionResponse(typeAndHandler.typeDesc, serImpl, transportImpl);

                req.deserialize(serImpl, bis, operation);

                typeAndHandler.handler.process(req, res);

                final BinaryOutputStream retBuffer = new BinaryOutputStream();
                final TransportMessage retMsg = transportImpl.createTransportMessage(message);
                serImpl.serializeMessageId(retBuffer, messageId);

                res.serialize(serImpl, retBuffer, operation);
                retMsg.setPayload(retBuffer.getByteBuffer());
                return retMsg;
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return null;
    }

}
