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
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class HttpRequestMessage extends HttpMessage {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HttpRequestMessage.class);

    private final HttpRequest request;
    private final HttpContent content;

    /**
     *
     * @param connection
     * @param request
     */
    public HttpRequestMessage(TransportImpl connection, FullHttpRequest request) {
        super(connection);
        if (request == null) {
            throw new NullPointerException("request");
        }
        this.request = request;
        this.content = request;
    }

    public HttpRequestMessage(TransportImpl connection, HttpRequest request, HttpContent content) {
        super(connection);
        if (request == null)
            throw new NullPointerException("request");
        if (content == null)
            throw new NullPointerException("content");
        this.request = request;
        this.content = content;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public HttpContent getContent() {
        return content;
    }

    public HttpRequest finalizeRequest() {
        request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, getPayload().remaining());
        ByteBuf bbuf = Unpooled.wrappedBuffer(getPayload());
        ByteBuf byteContent = content.content().clear();
        byteContent.writeBytes(bbuf);
        bbuf.release();
        return request;
    }

    @Override
    public TransportMessage set(String name, Object value) {
        if (TransportMessage.Names.CONTENT_TYPE.equals(name)) {
            request.headers().set(HttpHeaders.Names.CONTENT_TYPE, value);
        } else if (TransportMessage.Names.SESSION_ID.equals(name)) {
            request.headers().set("x-kiara-session", value);
        } else if (HttpMessage.Names.REQUEST_URI.equals(name)) {
            request.setUri((String)value);
        } else if (HttpMessage.Names.HTTP_METHOD.equals(name)) {
            request.setMethod(HttpMethod.valueOf(name));
        }
        return this;
    }

    @Override
    public Object get(String name) {
        if (TransportMessage.Names.CONTENT_TYPE.equals(name)) {
            return request.headers().get(HttpHeaders.Names.CONTENT_TYPE);
        } else if (TransportMessage.Names.SESSION_ID.equals(name)) {
            return request.headers().get("x-kiara-session");
        } else if (HttpMessage.Names.REQUEST_URI.equals(name)) {
            return request.getUri();
        } else if (HttpMessage.Names.HTTP_METHOD.equals(name)) {
            return request.getMethod().name();
        }
        return null;
    }

}
