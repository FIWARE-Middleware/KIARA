/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2014 German Research Center for Artificial Intelligence (DFKI)
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
package com.kiara.impl;

import com.kiara.serialization.Serializer;
import com.kiara.serialization.impl.SerializerImpl;
import com.kiara.server.Servant;
import com.kiara.transport.ServerTransport;
import com.kiara.transport.impl.*;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class ServantDispatcher implements TransportConnectionListener, TransportMessageListener {

    private final SerializerImpl serializer;
    private final HashMap<String, Servant> servants;
    private final ExecutorService executor;

    public ServantDispatcher(Serializer serializer, ServerTransport transport) {
        if (serializer == null) {
            throw new NullPointerException("serializer");
        }
        if (!(serializer instanceof SerializerImpl)) {
            throw new IllegalArgumentException("serializer argument is not of type "
                    + SerializerImpl.class.getName() + ", but " + serializer.getClass().getName());
        }
        if (transport == null) {
            throw new NullPointerException("transport");
        }
        if (!(transport instanceof ServerTransportImpl)) {
            throw new IllegalArgumentException("transport argument is not of type "
                    + ServerTransportImpl.class.getName() + ", but " + transport.getClass().getName());
        }
        this.serializer = (SerializerImpl) serializer;
        //TODO Send error.
        ServerTransportImpl serverTransport = (ServerTransportImpl) transport;
        executor = serverTransport.getDispatchingExecutor();
        servants = new HashMap<String, Servant>();
    }

    public void addService(Servant servant) {
        servants.put(servant.getServiceName(), servant);
    }

    public void onConnectionOpened(TransportImpl connection) {
        connection.addMessageListener(this);
    }

    public void onConnectionClosed(TransportImpl connection) {
        connection.removeMessageListener(this);
    }

    public void onMessage(final TransportMessage message) {
        final ByteBuffer buffer = message.getPayload();
        final TransportImpl transport = message.getTransport();
        final Object messageId = serializer.deserializeMessageId(message);
        final String service = serializer.deserializeService(message);
        final Servant servant = servants.get(service);

        if (servant != null) {
            if (executor == null) {
                TransportMessage tpmreply = servant.process(serializer, transport, message, messageId);
                if (tpmreply != null) {
                    //TransportMessage tresponse = transport.createTransportMessage(message);
                    //tresponse.setPayload(reply);
                    transport.send(tpmreply);
                } else {
                    // TODO return an error to the client.
                }
            } else {
                executor.submit(new Runnable() {

                    public void run() {
                        TransportMessage tpmreply = servant.process(serializer, transport, message, messageId);
                        if (tpmreply != null) {
                            //TransportMessage tresponse = transport.createTransportMessage(message);
                            //tresponse.setPayload(reply);
                            transport.send(tpmreply);
                        } else {
                            // TODO return an error to the client.
                        }
                    }
                });
            }
        }
    }

}
