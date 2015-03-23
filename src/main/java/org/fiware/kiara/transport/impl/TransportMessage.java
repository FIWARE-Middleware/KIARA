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
package org.fiware.kiara.transport.impl;

import java.nio.ByteBuffer;

/**
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public abstract class TransportMessage {

    public static final class Names {

        public static final String MESSAGE_ID = "message-id";
        public static final String SESSION_ID = "session-id";
        public static final String CONTENT_TYPE = "content-type";
    }

    private final TransportImpl transport;
    private ByteBuffer payload;

    protected TransportMessage(TransportImpl connection, ByteBuffer payload) {
        if (connection == null) {
            throw new NullPointerException("connection");
        }
        this.transport = connection;
        this.payload = payload;
    }

    protected TransportMessage(TransportImpl connection) {
        this(connection, null);
    }

    public TransportImpl getTransport() {
        return transport;
    }

    public ByteBuffer getPayload() {
        return payload;
    }

    public TransportMessage setPayload(ByteBuffer payload) {
        this.payload = payload;
        return this;
    }

    public boolean hasPayload() {
        return getPayloadSize() > 0;
    }

    public int getPayloadSize() {
        if (payload == null) {
            return 0;
        }
        return payload.remaining();
    }

    public Object getMessageId() {
        return get(Names.MESSAGE_ID);
    }

    public TransportMessage setMessageId(Object messageId) {
        return set(Names.MESSAGE_ID, messageId);
    }

    public Object getSessionId() {
        return get(Names.SESSION_ID);
    }

    public TransportMessage setSessionId(Object sessionId) {
        return set(Names.SESSION_ID, sessionId);
    }

    public String getContentType() {
        return (String) get(Names.CONTENT_TYPE);
    }

    public TransportMessage setContentType(String value) {
        return set(Names.CONTENT_TYPE, value);
    }

    public abstract TransportMessage set(String name, Object value);

    public abstract Object get(String name);
}
