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
package org.fiware.kiara.netty;

import com.google.common.util.concurrent.AbstractFuture;
import java.io.IOException;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.transport.impl.TransportImpl;
import org.fiware.kiara.transport.impl.TransportMessage;
import org.fiware.kiara.transport.impl.TransportMessageListener;

import org.fiware.kiara.serialization.impl.BinaryInputStream;

/**
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public class TransportMessageDispatcher extends AbstractFuture<TransportMessage> implements TransportMessageListener {

    private final Object messageId;
    private final SerializerImpl ser;
    private final TransportImpl transport;

    public TransportMessageDispatcher(Object messageId, SerializerImpl ser, TransportImpl transport) {
        this.messageId = messageId;
        this.ser = ser;
        this.transport = transport;
        this.transport.addMessageListener(this);
    }

    public Object getMessageId() {
        return messageId;
    }

    @Override
    public boolean onMessage(TransportMessage message) {
        final Object responseId;
        try {
            responseId = ser.deserializeMessageId(BinaryInputStream.fromByteBuffer(message.getPayload()));
        } catch (IOException ex) {
            return false;
        }

        if (!ser.equalMessageIds(messageId, responseId)) {
            return false;
        }

        transport.removeMessageListener(this);
        set(message);
        return true;
    }

}
