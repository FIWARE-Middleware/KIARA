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

import com.kiara.transport.impl.*;
import com.kiara.transport.impl.TransportConnectionListener;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.io.IOException;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 * @param <I>
 * @param <T>
 */
public abstract class BaseHandler<I, T extends TransportFactory> extends SimpleChannelInboundHandler<I> implements TransportImpl {

    private static final Logger logger = LoggerFactory.getLogger(BaseHandler.class);

    private final T transportFactory;
    protected volatile Channel channel = null;
    private TransportConnectionListener connectionListener;
    private final List<TransportMessageListener> listeners = new ArrayList<TransportMessageListener>();

    public static enum Mode {

        CLIENT,
        SERVER
    }
    protected final Mode mode;

    public static enum State {

        UNINITIALIZED,
        WAIT_CONNECT,
        CONNECTED,
        WAIT_CLOSE,
        CLOSED
    }
    protected State state;

    protected BaseHandler(Mode mode, State state, T transportFactory, TransportConnectionListener connectionListener) {
        this.mode = mode;
        this.state = state;
        this.connectionListener = connectionListener;
        this.transportFactory = transportFactory;
    }

    public T getTransportFactory() {
        return transportFactory;
    }

    @Override
    public void addMessageListener(TransportMessageListener listener) {
        if (listener == null) {
            throw new NullPointerException("listener");
        }
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    @Override
    public boolean removeMessageListener(TransportMessageListener listener) {
        if (listener == null) {
            return false;
        }
        synchronized (listeners) {
            return listeners.remove(listener);
        }
    }

    public TransportConnectionListener getConnectionListener() {
        return connectionListener;
    }

    public void setConnectionListener(TransportConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }

    protected final void notifyListeners(final TransportMessage message) {
        TransportMessageListener currentListeners[] = null;
        synchronized (listeners) {
            if (!listeners.isEmpty()) {
                currentListeners = listeners.toArray(new TransportMessageListener[listeners.size()]);
            }
        }
        if (currentListeners != null) {
            for (TransportMessageListener listener : currentListeners) {
                listener.onMessage(message);
            }
        }
    }

    @Override
    public SocketAddress getLocalAddress() {
        if (channel == null) {
            throw new IllegalStateException();
        }
        return channel.localAddress();
    }

    @Override
    public SocketAddress getRemoteAddress() {
        if (channel == null) {
            throw new IllegalStateException();
        }
        return channel.remoteAddress();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channel = ctx.channel();
        switch (state) {
            case UNINITIALIZED:
            case WAIT_CONNECT:
                state = State.CONNECTED;
                if (connectionListener != null) {
                    connectionListener.onConnectionOpened(this);
                }
                break;
            case WAIT_CLOSE:
                closeChannel();
                break;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Class: "+getClass().getName()+", channel closed {}", ctx);
        }
        state = State.CLOSED;
        channel = null;
        if (connectionListener != null) {
            connectionListener.onConnectionClosed(this);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    protected final void closeChannel() {
        if (channel != null) {
            channel.closeFuture().addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    future.removeListener(this);
                    state = State.CLOSED;
                    channel = null;
                }

            });
        }
    }

    @Override
    public void close() throws IOException {
        if (state == State.WAIT_CLOSE || state == State.CLOSED) {
            return;
        }

        state = State.WAIT_CLOSE;
        closeChannel();
    }

    @Override
    public boolean isOpen() {
        return state == State.CONNECTED && channel != null;
    }

}
