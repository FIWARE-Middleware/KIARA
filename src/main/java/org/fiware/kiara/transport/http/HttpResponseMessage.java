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
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class HttpResponseMessage extends HttpMessage {

    private final HttpHeaders headers;
    private final HttpResponse response;
    private final HttpContent content;

    HttpResponseMessage(TransportImpl connection, HttpHeaders headers) {
        super(connection);
        if (headers == null) {
            throw new NullPointerException("headers");
        }

        this.headers = headers;
        this.response = null;
        this.content = null;
    }

    HttpResponseMessage(TransportImpl connection, HttpResponse response, HttpContent content) {
        super(connection);
        if (response == null) {
            throw new NullPointerException("response");
        }
        if (content == null) {
            throw new NullPointerException("content");
        }

        this.response = response;
        this.headers = response.headers();
        this.content = content;
    }

    HttpResponseMessage(TransportImpl connection, FullHttpResponse response) {
        super(connection);
        if (response == null) {
            throw new NullPointerException("response");
        }

        this.response = response;
        this.headers = response.headers();
        this.content = response;
    }

    public HttpResponse getResponse() {
        return response;
    }

    public HttpContent getContent() {
        return content;
    }

    public HttpResponse finalizeResponse() {
        HttpResponse httpResponse = getResponse();
        getContent().content().clear();

        if (getPayload() != null) {
            ByteBuf bbuf = Unpooled.wrappedBuffer(getPayload().duplicate());
            getContent().content().writeBytes(bbuf);
        }

        httpResponse.headers().set(CONTENT_LENGTH, getContent().content().readableBytes());

        return httpResponse;
    }

    @Override
    public TransportMessage set(String name, Object value) {
        if (TransportMessage.Names.CONTENT_TYPE.equals(name)) {
            headers.set(HttpHeaders.Names.CONTENT_TYPE, value);
        } else if (TransportMessage.Names.SESSION_ID.equals(name)) {
            headers.set("x-kiara-session", value);
        } else if (Names.STATUS_CODE.equals(name)) {
            getResponse().setStatus(HttpResponseStatus.valueOf((Integer)value));
        }
        return this;
    }

    @Override
    public Object get(String name) {
        if (TransportMessage.Names.CONTENT_TYPE.equals(name)) {
            return headers.get(HttpHeaders.Names.CONTENT_TYPE);
        } else if (TransportMessage.Names.SESSION_ID.equals(name)) {
            return headers.get("x-kiara-session");
        } else if (Names.STATUS_CODE.equals(name)) {
            return getResponse().getStatus().code();
        }
        return null;
    }

}
