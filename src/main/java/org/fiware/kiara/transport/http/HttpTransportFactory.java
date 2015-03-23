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
import com.google.common.util.concurrent.SettableFuture;
import org.fiware.kiara.netty.NettyTransportFactory;
import org.fiware.kiara.transport.Transport;
import org.fiware.kiara.exceptions.impl.InvalidAddressException;
import org.fiware.kiara.transport.impl.TransportConnectionListener;
import org.fiware.kiara.transport.impl.TransportImpl;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpMethod;
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
public class HttpTransportFactory extends NettyTransportFactory {

    private final boolean secure;

    public HttpTransportFactory(boolean secure) {
        this.secure = secure;
    }

    @Override
    public String getName() {
        return secure ? "https" : "http";
    }

    @Override
    public int getPriority() {
        return secure ? 19 : 20;
    }

    @Override
    public boolean isSecureTransport() {
        return secure;
    }

    @Override
    public ListenableFuture<Transport> createTransport(String uri, Map<String, Object> settings) throws InvalidAddressException, IOException  {
        try {
            return openConnection(new URI(uri), settings);
        } catch (URISyntaxException ex) {
            throw new InvalidAddressException(ex);
        }
    }

    private ListenableFuture<Transport> openConnection(URI uri, Map<String, Object> settings) throws IOException {
        String scheme = uri.getScheme() == null ? "http" : uri.getScheme();
        String host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
        int port = uri.getPort();
        if (port == -1) {
            if ("http".equalsIgnoreCase(scheme)) {
                port = 80;
            } else if ("https".equalsIgnoreCase(scheme)) {
                port = 443;
            }
        }

        if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
            throw new IOException("Only HTTP(S) is supported.");
        }

        // Configure SSL context if necessary.
        final boolean ssl = "https".equalsIgnoreCase(scheme);
        final SslContext sslCtx;
        if (ssl) {
            sslCtx = SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE);
        } else {
            sslCtx = null;
        }

        // Configure the client.
        final SettableFuture<Transport> onConnectionActive = SettableFuture.create();
        final HttpHandler clientHandler = new HttpHandler(this, uri, HttpMethod.POST, null);
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
                .handler(new HttpClientInitializer(sslCtx, clientHandler));
        b.connect(host, port);

        return onConnectionActive;
    }

    @Override
    public ChannelHandler createServerChildHandler(String path, TransportConnectionListener connectionListener) {
        try {
            return new HttpServerInitializer(this, createServerSslContext(), path, connectionListener);
        } catch (CertificateException ex) {
            throw new RuntimeException(ex);
        } catch (SSLException ex) {
            throw new RuntimeException(ex);
        }
    }

}
