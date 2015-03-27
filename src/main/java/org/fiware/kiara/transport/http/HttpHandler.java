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

import com.google.common.util.concurrent.ListenableFuture;
import org.fiware.kiara.netty.BaseHandler;
import org.fiware.kiara.netty.ListenableConstantFutureAdapter;
import org.fiware.kiara.transport.impl.TransportConnectionListener;
import org.fiware.kiara.transport.impl.TransportMessage;
import org.fiware.kiara.util.HexDump;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponse;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import io.netty.handler.codec.http.HttpVersion;
import static io.netty.handler.codec.http.HttpVersion.*;
import io.netty.handler.codec.http.LastHttpContent;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public class HttpHandler extends BaseHandler<Object, HttpTransportFactory> {

    private static final Logger logger = LoggerFactory.getLogger(HttpHandler.class);

    private HttpHeaders headers = null;
    private final ByteArrayOutputStream bout;

    private final URI uri;
    private final HttpMethod method;

    public HttpHandler(HttpTransportFactory transportFactory, URI uri, HttpMethod method, TransportConnectionListener connectionListener) {
        super(Mode.CLIENT, State.UNINITIALIZED, transportFactory, connectionListener);
        if (transportFactory == null) {
            throw new NullPointerException("transportFactory");
        }
        if (uri == null) {
            throw new NullPointerException("uri");
        }
        if (method == null) {
            throw new NullPointerException("method");
        }
        this.uri = uri;
        this.method = method;
        this.bout = new ByteArrayOutputStream(1024);
    }

    public HttpHandler(HttpTransportFactory transportFactory, String path, TransportConnectionListener connectionListener) {
        super(Mode.SERVER, State.UNINITIALIZED, transportFactory, connectionListener);
        if (transportFactory == null) {
            throw new NullPointerException("transportFactory");
        }
        if (connectionListener == null) {
            throw new NullPointerException("connectionListener");
        }
        URI tmp = null;
        try {
            tmp = path != null ? new URI(path) : null;
        } catch (URISyntaxException ex) {
        }
        this.uri = tmp;
        this.method = null;
        this.bout = null;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.debug("Handler: {} / Channel: {}", this, ctx.channel());
        if (mode == Mode.SERVER) {
            if (msg instanceof FullHttpRequest) {
                final FullHttpRequest request = (FullHttpRequest) msg;

                HttpRequestMessage transportMessage = new HttpRequestMessage(this, request);
                transportMessage.setPayload(request.content().copy().nioBuffer());

                if (logger.isDebugEnabled()) {
                    logger.debug("RECEIVED CONTENT {}", HexDump.dumpHexString(transportMessage.getPayload()));
                    //logger.debug("RECEIVED REQUEST WITH CONTENT {}", Util.bufferToString(transportMessage.getPayload()));
                }

                notifyListeners(transportMessage);

                boolean keepAlive = HttpHeaders.isKeepAlive(request);
            }
        } else {
            // CLIENT
            if (msg instanceof HttpResponse) {
                HttpResponse response = (HttpResponse) msg;
                headers = response.headers();
                //if (!response.headers().isEmpty()) {
                //    contentType = response.headers().get("Content-Type");
                //}
            }
            if (msg instanceof HttpContent) {
                HttpContent content = (HttpContent) msg;
                ByteBuf buf = content.content();
                if (buf.isReadable()) {
                    if (buf.hasArray()) {
                        bout.write(buf.array(), buf.readerIndex(), buf.readableBytes());
                    } else {
                        byte[] bytes = new byte[buf.readableBytes()];
                        buf.getBytes(buf.readerIndex(), bytes);
                        bout.write(bytes);
                    }
                }
                if (content instanceof LastHttpContent) {
                    //ctx.close();
                    bout.flush();
                    HttpResponseMessage response = new HttpResponseMessage(this, headers);
                    response.setPayload(ByteBuffer.wrap(bout.toByteArray(), 0, bout.size()));
                    onResponse(response);
                    bout.reset();
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();

        logger.error("Http error", cause);
    }

    private void onResponse(HttpResponseMessage response) {
        if (logger.isDebugEnabled()) {
            logger.debug("RECEIVED RESPONSE WITH CONTENT {}", HexDump.dumpHexString(response.getPayload()));
        }

        notifyListeners(response);
    }

    private TransportMessage createRequest() {
        if (mode == Mode.SERVER) {
            throw new IllegalStateException("Requests from server are not supported");
        }
        // Prepare the HTTP request.
        String host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
        FullHttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, method, uri.getRawPath());

        request.headers().set(HttpHeaders.Names.HOST, host);
        request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        request.headers().set(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP);

        return new HttpRequestMessage(this, request);
    }

    @Override
    public TransportMessage createTransportMessage(TransportMessage transportMessage) {
        if (transportMessage instanceof HttpRequestMessage) {
            return createResponse(transportMessage);
        } else {
            return createRequest();
        }
    }

    private TransportMessage createResponse(TransportMessage transportMessage) {
        if (!(transportMessage instanceof HttpRequestMessage)) {
            throw new IllegalArgumentException("request is not of type HttpRequestMessage");
        }
        HttpRequestMessage request = (HttpRequestMessage) transportMessage;

        // Decide whether to close the connection or not.
        boolean keepAlive = HttpHeaders.isKeepAlive(request.getRequest());
        // Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, request.getRequest().getDecoderResult().isSuccess() ? OK : BAD_REQUEST);

        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

        if (keepAlive) {
            // Add 'Content-Length' header only for a keep-alive connection.
            response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
            // Add keep alive header as per:
            // - http://www.w3.org/Protocols/HTTP/1.1/draft-ietf-http-v11-spec-01.html#Connection
            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }

        return new HttpResponseMessage(this, response);
    }

    @Override
    public ListenableFuture<Void> send(TransportMessage message) {
        if (message == null) {
            throw new NullPointerException("message");
        }
        if (state != State.CONNECTED || channel == null) {
            throw new IllegalStateException("state=" + state.toString() + " channel=" + channel);
        }

        HttpMessage httpMsg;

        boolean keepAlive = true;

        if (message instanceof HttpRequestMessage) {
            HttpRequestMessage msg = (HttpRequestMessage) message;

            httpMsg = msg.finalizeRequest();

            if (logger.isDebugEnabled()) {
                logger.debug("SEND CONTENT: {}", HexDump.dumpHexString(msg.getPayload()));
            }
        } else if (message instanceof HttpResponseMessage) {
            HttpResponseMessage msg = (HttpResponseMessage) message;

            httpMsg = msg.finalizeResponse();

            keepAlive = HttpHeaders.isKeepAlive(httpMsg);

            if (logger.isDebugEnabled()) {
                logger.debug("SEND CONTENT: {}", HexDump.dumpHexString(msg.getPayload()));
            }
        } else {
            throw new IllegalArgumentException("msg is neither of type HttpRequestMessage nor HttpResponseMessage");
        }

        ChannelFuture result = channel.writeAndFlush(httpMsg);

        if (!keepAlive) {
            // If keep-alive is off, close the connection once the content is fully written.
            channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }

        return new ListenableConstantFutureAdapter<Void>(result, null);
    }


}
