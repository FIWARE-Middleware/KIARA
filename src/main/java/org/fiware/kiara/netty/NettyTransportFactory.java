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

import org.fiware.kiara.Kiara;
import org.fiware.kiara.RunningService;
import org.fiware.kiara.transport.ServerTransport;
import org.fiware.kiara.transport.impl.Global;
import org.fiware.kiara.transport.impl.TransportConnectionListener;
import org.fiware.kiara.transport.TransportFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Slf4JLoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.cert.CertificateException;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.SSLException;

/**
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public abstract class NettyTransportFactory implements TransportFactory {

    private static boolean SSL = System.getProperty("ssl") != null;
    private static final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup();

    static {
        InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());
        Kiara.addRunningService(new RunningService() {

            public void shutdownService() {
                try {
                    if (!workerGroup.isShutdown()) {
                        workerGroup.shutdownGracefully().get();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                try {
                    if (!bossGroup.isShutdown()) {
                        bossGroup.shutdownGracefully().get();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public NettyTransportFactory() {
    }

    protected final EventLoopGroup getEventLoopGroup() {
        return Global.transportGroup;
    }

    protected SslContext createServerSslContext() throws CertificateException, SSLException {
        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            return SslContext.newServerContext(ssc.certificate(), ssc.privateKey());
        } else {
            return null;
        }
    }

    @Override
    public ServerTransport createServerTransport(String url) throws IOException {
        try {
            final URI uri = new URI(url);
            return new NettyServerTransport(new InetSocketAddress(uri.getHost(), uri.getPort()), uri.getPath(), this);
        } catch (URISyntaxException ex) {
            throw new IOException(ex);
        }
    }

    public void startServer(NettyServerTransport serverTransport, TransportConnectionListener listener) throws InterruptedException {
        if (serverTransport == null) {
            throw new NullPointerException("serverTransport");
        }
        
        // Check if secure transport has been enabled
        if (serverTransport.getTransportFactory().isSecureTransport()) {
            SSL = true;
        } else {
            SSL = false;
        }
        
        if (listener == null) {
            throw new NullPointerException("listener");
        }

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(createServerChildHandler(serverTransport.getPath(), listener));

        final Channel channel = b.bind(serverTransport.getLocalSocketAddress()).sync().channel();
        serverTransport.setChannel(channel);
        serverTransport.setListener(listener);
    }

    protected abstract ChannelHandler createServerChildHandler(String path, TransportConnectionListener connectionListener);

}
