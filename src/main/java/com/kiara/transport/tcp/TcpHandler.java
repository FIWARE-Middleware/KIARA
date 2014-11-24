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
package com.kiara.transport.tcp;

import com.google.common.util.concurrent.ListenableFuture;
import com.kiara.netty.BaseHandler;
import com.kiara.netty.ListenableConstantFutureAdapter;
import com.kiara.transport.impl.TransportConnectionListener;
import com.kiara.transport.impl.TransportMessage;
import com.kiara.util.Buffers;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import java.net.URI;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class TcpHandler extends BaseHandler<Object, TcpBlockTransportFactory> {

    private static final Logger logger = LoggerFactory.getLogger(TcpHandler.class);

    private final URI uri;
    private volatile String sessionId = null;
    private final boolean SEND_SESSION_ID = false;

    public TcpHandler(TcpBlockTransportFactory transportFactory, URI uri, TransportConnectionListener connectionListener) {
        super(Mode.CLIENT, State.UNINITIALIZED, transportFactory, connectionListener);
        if (transportFactory == null) {
            throw new NullPointerException("transportFactory");
        }
        if (uri == null) {
            throw new NullPointerException("uri");
        }
        this.uri = uri;
    }

    public TcpHandler(TcpBlockTransportFactory transportFactory, String path, TransportConnectionListener connectionListener) {
        super(Mode.SERVER, State.UNINITIALIZED, transportFactory, connectionListener);
        if (transportFactory == null) {
            throw new NullPointerException("transportFactory");
        }
        if (connectionListener == null) {
            throw new NullPointerException("connectionListener");
        }
        this.uri = null;
    }

    private static final byte[] EMPTY_ARRAY = new byte[0];
    private static final ByteBuffer EMPTY_BUFFER = ByteBuffer.wrap(EMPTY_ARRAY);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if ((state == State.UNINITIALIZED || state == State.WAIT_CONNECT) && mode == Mode.CLIENT && SEND_SESSION_ID) {
            // FIXME send sessionID
            ctx.writeAndFlush(EMPTY_BUFFER);
        }
        super.channelActive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        // ctx.flush();
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.debug("Handler: {} / Mode: {} / Channel: {} / Message class {}", this, mode, ctx.channel(), msg.getClass());

        final TcpBlockMessage transportMessage = new TcpBlockMessage(this, (ByteBuffer) msg);

        if (logger.isDebugEnabled()) {
            logger.debug("RECEIVED CONTENT {}", new String(transportMessage.getPayload().array(), transportMessage.getPayload().arrayOffset(), transportMessage.getPayload().remaining()));
        }

        if (mode == Mode.SERVER && sessionId == null && SEND_SESSION_ID) {
            if (logger.isDebugEnabled()) {
                logger.debug("Set session ID to '{}'", Buffers.bufferToString(transportMessage.getPayload()));
            }
            sessionId = Buffers.bufferToString(transportMessage.getPayload(), "UTF-8");
        } else {
            notifyListeners(transportMessage);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        logger.error("Tcp Error", cause);
    }

    @Override
    public TransportMessage createTransportMessage(TransportMessage transportMessage) {
        return new TcpBlockMessage(this, null);
    }

    @Override
    public ListenableFuture<Void> send(TransportMessage message) {
        if (message == null) {
            throw new NullPointerException("message");
        }
        if (state != State.CONNECTED || channel == null) {
            throw new IllegalStateException("state=" + state.toString() + " channel=" + channel);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("SEND CONTENT: {}", Buffers.bufferToString(message.getPayload()));
        }

        ChannelFuture result = channel.writeAndFlush(message.getPayload());
        return new ListenableConstantFutureAdapter<Void>(result, null);
    }

}
