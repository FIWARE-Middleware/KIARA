/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2015 German Research Center for Artificial Intelligence (DFKI)
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

import io.netty.bootstrap.ChannelFactory;
import io.netty.channel.ChannelException;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public class NioDatagramChannelFactory implements ChannelFactory<NioDatagramChannel> {

    private final InternetProtocolFamily ipFamily;

    public NioDatagramChannelFactory(InternetProtocolFamily ipFamily) {
        this.ipFamily = ipFamily;
    }

    @Override
    public NioDatagramChannel newChannel() {
        try {
            return new NioDatagramChannel(ipFamily);
        } catch (Throwable t) {
            throw new ChannelException("Unable to create Channel from class NioDatagramChannel", t);
        }
    }

    @Override
    public String toString() {
        return "NioDatagramChannelFactory";
    }

}
