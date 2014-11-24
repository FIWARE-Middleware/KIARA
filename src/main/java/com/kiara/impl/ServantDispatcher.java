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
import com.kiara.server.Servant;
import com.kiara.transport.ServerTransport;
import com.kiara.transport.impl.*;
import com.kiara.transport.impl.TransportConnectionListener;

import java.nio.ByteBuffer;
import java.util.HashMap;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class ServantDispatcher implements TransportConnectionListener, TransportMessageListener {

    public ServantDispatcher(Serializer ser, ServerTransport transport) {
        m_ser = ser;
        ServerTransportImpl m_transport = (ServerTransportImpl) transport;
        m_servants = new HashMap<String, Servant>();
    }

    public void addService(Servant servant) {
        m_servants.put(servant.getServiceName(), servant);
    }

    public void onConnectionOpened(TransportImpl connection) {
        connection.addMessageListener(this);
    }

    public void onConnectionClosed(TransportImpl connection) {
        connection.removeMessageListener(this);
    }

    public void onMessage(TransportMessage message) {
        final ByteBuffer buffer = message.getPayload();
        final TransportImpl transport = message.getTransport();
        final Object messageId = m_ser.deserializeMessageId(buffer);
        final String service = m_ser.deserializeService(buffer);
        final Servant servant = m_servants.get(service);

        if (servant != null) {
            ByteBuffer reply = servant.process(m_ser, buffer, messageId);

            if (reply != null) {
                TransportMessage tresponse = transport.createTransportMessage(message);
                tresponse.setPayload(reply);
                transport.send(tresponse);
            } else {
                // TODO return an error to the client.
            }
        } else {
            // TODO return an error to the client.
        }
    }

    private Serializer m_ser = null;
    private HashMap<String, Servant> m_servants = null;
}
