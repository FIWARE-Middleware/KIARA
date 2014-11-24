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

import com.kiara.netty.ByteBufferDecoder;
import com.kiara.netty.ByteBufferEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import java.nio.ByteOrder;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
class TcpClientInitializer extends ChannelInitializer<SocketChannel> {
    private final SslContext sslCtx;
    private final TcpHandler handler;

    public TcpClientInitializer(SslContext sslCtx, TcpHandler handler) {
        this.sslCtx = sslCtx;
        this.handler = handler;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        // Enable TCPS if necessary.
        if (sslCtx != null) {
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }
        p.addLast("logger", new LoggingHandler(LogLevel.DEBUG));

        p.addLast(new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, Integer.MAX_VALUE, 0, 4, 0, 4, true));
        p.addLast(new ByteBufferDecoder());

        p.addLast(new LengthFieldPrepender(4, 0, false) {
            @Override
            protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
                ByteBuf outWithLittleEndian = out.order(ByteOrder.LITTLE_ENDIAN);
                super.encode(ctx, msg, outWithLittleEndian);
            }
        });
        p.addLast(new ByteBufferEncoder());
        p.addLast(handler);
    }

}
