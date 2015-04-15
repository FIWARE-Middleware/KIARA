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
package org.fiware.kiara.transport.http;

import org.fiware.kiara.transport.impl.TransportImpl;
import org.fiware.kiara.transport.impl.TransportMessage;
import java.nio.ByteBuffer;

/**
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public abstract class HttpMessage extends TransportMessage {

    public static final class Names {
        public static final String REQUEST_URI = "request-uri";
        public static final String HTTP_METHOD = "http-method";
        public static final String STATUS_CODE = "status-code";
    }

    protected HttpMessage(TransportImpl connection, ByteBuffer payload) {
        super(connection, payload);
    }

    protected HttpMessage(TransportImpl connection) {
        super(connection, null);
    }

    public final String getRequestUri() {
        return (String)get(Names.REQUEST_URI);
    }

    public final TransportMessage setRequestUri(String uri) {
        return set(Names.REQUEST_URI, uri);
    }

    public final String getHttpMethod() {
        return (String)get(Names.HTTP_METHOD);
    }

    public final TransportMessage setHttpMethod(String method) {
        return set(Names.HTTP_METHOD, method);
    }

    @Override
    public TransportMessage setPayload(ByteBuffer payload) {
        if (getPayload() != null)
            throw new IllegalStateException("Cannot replace payload");
        return super.setPayload(payload);
    }

}
