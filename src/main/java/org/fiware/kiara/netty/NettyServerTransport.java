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

import org.fiware.kiara.transport.impl.ServerTransportImpl;
import org.fiware.kiara.transport.impl.TransportConnectionListener;
import org.fiware.kiara.transport.impl.TransportFactory;
import io.netty.channel.Channel;
import java.io.IOException;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;

/**
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public class NettyServerTransport implements ServerTransportImpl {
    private final String path;
    private final SocketAddress localSocketAddress;
    private final NettyTransportFactory transportFactory;
    private Channel channel;
    private TransportConnectionListener listener;
    private ExecutorService dispatchingExecutor;

    public NettyServerTransport(SocketAddress endpoint, String path, NettyTransportFactory transportFactory) {
        this.localSocketAddress = endpoint;
        this.path = path;
        this.transportFactory = transportFactory;
        this.channel = null;
        this.listener = null;
    }

    public String getPath() {
        return path;
    }

    public SocketAddress getLocalSocketAddress() {
        return localSocketAddress;
    }

    public TransportFactory getTransportFactory() {
        return transportFactory;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setListener(TransportConnectionListener listener) {
        this.listener = listener;
    }

    public TransportConnectionListener getListener() {
        return listener;
    }

    public void close() throws IOException {
        if (channel != null && channel.isOpen()) {
            try {
                channel.close().sync();
            } catch (InterruptedException ex) {
                throw new IOException(ex);
            }
        }
    }

    public void setDispatchingExecutor(ExecutorService executor) {
        this.dispatchingExecutor = executor;
    }

    public ExecutorService getDispatchingExecutor() {
        return dispatchingExecutor;
    }
}
