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

package com.kiara.transport.impl;

import java.nio.ByteBuffer;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public abstract class TransportMessage {

    private final TransportImpl transport;
    private ByteBuffer payload;

    protected TransportMessage(TransportImpl connection, ByteBuffer payload) {
        if (connection == null)
            throw new NullPointerException("connection");
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
        if (payload == null)
            return 0;
        return payload.remaining();
    }

    public abstract TransportMessage set(String name, Object value);

    public abstract Object get(String name);
}
