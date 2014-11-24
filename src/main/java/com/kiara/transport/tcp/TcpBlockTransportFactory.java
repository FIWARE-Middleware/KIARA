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
import com.google.common.util.concurrent.SettableFuture;
import com.kiara.transport.Transport;
import com.kiara.transport.impl.InvalidAddressException;
import com.kiara.netty.NettyTransportFactory;
import com.kiara.transport.impl.TransportConnectionListener;
import com.kiara.transport.impl.TransportImpl;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.cert.CertificateException;
import java.util.Map;
import javax.net.ssl.SSLException;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class TcpBlockTransportFactory extends NettyTransportFactory {

    private static final int DEFAULT_TCP_PORT = 1111;
    private static final int DEFAULT_TCPS_PORT = 1112;

    private final boolean secure;

    public TcpBlockTransportFactory(boolean secure) {
        this.secure = secure;
    }

    @Override
    public String getName() {
        return secure ? "tcps" : "tcp";
    }

    @Override
    public int getPriority() {
        return secure ? 9 : 10;
    }

    @Override
    public boolean isSecureTransport() {
        return secure;
    }

    @Override
    public ListenableFuture<Transport> createTransport(String uri, Map<String, Object> settings) throws InvalidAddressException, IOException {
        if (uri == null) {
            throw new NullPointerException("uri");
        }
        try {
            return createTransport(new URI(uri), settings);
        } catch (URISyntaxException ex) {
            throw new InvalidAddressException(ex);
        }
    }

    private ListenableFuture<Transport> createTransport(URI uri, Map<String, Object> settings) throws IOException {
        if (uri == null) {
            throw new NullPointerException("uri");
        }

        final String scheme = uri.getScheme();

        if (!"tcp".equalsIgnoreCase(scheme) && !"tcps".equalsIgnoreCase(scheme)) {
            throw new IllegalArgumentException("URI has neither tcp nor tcps scheme");
        }

        final String host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
        int port = uri.getPort();
        if (port == -1) {
            if ("tcp".equalsIgnoreCase(scheme)) {
                port = DEFAULT_TCP_PORT;
            } else if ("tcps".equalsIgnoreCase(scheme)) {
                port = DEFAULT_TCPS_PORT;
            }
        }

        // Configure SSL context if necessary.
        final boolean ssl = "tcps".equalsIgnoreCase(scheme);
        final SslContext sslCtx;
        if (ssl) {
            sslCtx = SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE);
        } else {
            sslCtx = null;
        }

        // Configure the client.
        final SettableFuture<Transport> onConnectionActive = SettableFuture.create();
        final TcpHandler clientHandler = new TcpHandler(this, uri, null);
        clientHandler.setConnectionListener(new TransportConnectionListener() {

            @Override
            public void onConnectionOpened(TransportImpl connection) {
                clientHandler.setConnectionListener(null);
                onConnectionActive.set(connection);
            }

            @Override
            public void onConnectionClosed(TransportImpl connection) {

            }
        });

        Bootstrap b = new Bootstrap();
        b.group(getEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new TcpClientInitializer(sslCtx, clientHandler));
        b.connect(host, port);

        return onConnectionActive;
    }

    @Override
    public ChannelHandler createServerChildHandler(String path, TransportConnectionListener connectionHandler) {
        try {
            return new TcpServerInitializer(this, createServerSslContext(), path, connectionHandler);
        } catch (CertificateException ex) {
            throw new RuntimeException(ex);
        } catch (SSLException ex) {
            throw new RuntimeException(ex);
        }
    }

}
