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
package org.fiware.kiara.ps.rtps.resources;

import org.fiware.kiara.ps.rtps.messages.RTPSMessage;
import org.fiware.kiara.ps.rtps.messages.RTPSMessageBuilder;
import org.fiware.kiara.ps.rtps.messages.common.types.RTPSEndian;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import java.net.InetSocketAddress;
import org.fiware.kiara.netty.Buffers;

/**
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public class ReceptionHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private Channel m_channel;

    private final ListenResource m_listenResource;

    public ReceptionHandler(ListenResource listenResource) {
        this.m_listenResource = listenResource;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        m_channel = ctx.channel();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {

        System.out.println(((InetSocketAddress) ctx.channel().localAddress()).getPort());
        System.out.println("Received");

        RTPSMessage msg = RTPSMessageBuilder.createMessage(RTPSEndian.BIG_ENDIAN);

        System.out.println("Message created");

        msg.setBuffer(Buffers.toByteArray(packet.content()));
        System.out.println("Buffer set");

        newRTPSMessage(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        // We don't close the channel because we can keep serving requests.
    }

    private void newRTPSMessage(RTPSMessage msg) {
        synchronized (this.m_listenResource) {
            if (msg.getSize() == 0) {
                return;
            }

            System.out.println(""); // TODO Log this

            this.m_listenResource.getSenderLocator().setPort(this.m_listenResource.getSenderEndpoint().port);

        }
    }

}
