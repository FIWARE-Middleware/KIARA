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

import com.google.common.util.concurrent.ListenableFuture;
import com.kiara.transport.Transport;
import java.io.Closeable;
import java.net.SocketAddress;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public interface TransportImpl extends Transport, Closeable {

    public SocketAddress getLocalAddress();

    public SocketAddress getRemoteAddress();

    /**
     * Create response message when passed message is request message, and
     * request message otherwise.
     *
     * @param message
     * @return
     */
    public TransportMessage createTransportMessage(TransportMessage message);

    public ListenableFuture<Void> send(TransportMessage message);

    public void addMessageListener(TransportMessageListener listener);

    public boolean removeMessageListener(TransportMessageListener listener);

    public boolean isOpen();

}
