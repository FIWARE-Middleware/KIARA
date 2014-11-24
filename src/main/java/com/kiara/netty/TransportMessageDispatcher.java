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
package com.kiara.netty;

import com.google.common.util.concurrent.AbstractFuture;
import com.kiara.serialization.Serializer;
import com.kiara.transport.Transport;
import com.kiara.transport.impl.TransportImpl;
import com.kiara.transport.impl.TransportMessage;
import com.kiara.transport.impl.TransportMessageListener;
import java.nio.ByteBuffer;

/**
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class TransportMessageDispatcher extends AbstractFuture<TransportMessage> implements TransportMessageListener {

    private final Object messageId;
    private final Serializer ser;
    private final TransportImpl transport;

    public TransportMessageDispatcher(Object messageId, Serializer ser, TransportImpl transport) {
        this.messageId = messageId;
        this.ser = ser;
        this.transport = transport;
        this.transport.addMessageListener(this);
    }

    public void onMessage(TransportMessage message) {
        final ByteBuffer buffer = message.getPayload();
        buffer.rewind();
        final Object responseId = ser.deserializeMessageId(buffer);

        if (!ser.equalMessageIds(messageId, responseId)) {
            return;
        }

        set(message);
        transport.removeMessageListener(this);
    }

}
