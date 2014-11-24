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

import com.kiara.RunningService;
import com.kiara.netty.NettyTransportFactory;
import com.kiara.transport.ServerTransport;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SSLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class TransportServerImpl implements TransportServer, RunningService {

    private static class ServerEntry {

        public final ServerTransportImpl serverTransport;
        public final TransportConnectionListener listener;

        public ServerEntry(ServerTransportImpl serverTransport, TransportConnectionListener listener) {
            this.serverTransport = serverTransport;
            this.listener = listener;
        }

        public void startServer() throws InterruptedException {
            serverTransport.getTransportFactory().startServer(serverTransport, listener);
        }

    }

    private static final Logger logger = LoggerFactory.getLogger(TransportServerImpl.class);

    private final List<ServerEntry> serverEntries = new ArrayList<ServerEntry>();

    public TransportServerImpl() throws CertificateException, SSLException {
        // bossGroup and workerGroup need to be always shutdown, so we are always running service
        // Kiara.addRunningService(this);
    }

    @Override
    public void listen(ServerTransport serverTransport, TransportConnectionListener listener) {
        if (!(serverTransport instanceof ServerTransportImpl))
            throw new IllegalArgumentException("transport factory is not an instance of " + ServerTransportImpl.class.getName() + " class");
        final ServerTransportImpl st = (ServerTransportImpl)serverTransport;
        if (!(st.getTransportFactory() instanceof NettyTransportFactory)) {
            throw new IllegalArgumentException("transport factory is not an instance of " + NettyTransportFactory.class.getName() + " class");
        }
        synchronized (serverEntries) {
            serverEntries.add(new ServerEntry(st, listener));
        }
    }

    @Override
    public void run() throws IOException {
        int numServers = 0;
        try {
            synchronized (serverEntries) {
                for (ServerEntry serverEntry : serverEntries) {
                    serverEntry.startServer();
                    ++numServers;
                }
            }
        } catch (InterruptedException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public void close() throws IOException {
        ServerEntry[] tmp;
        synchronized (serverEntries) {
            tmp = serverEntries.toArray(new ServerEntry[serverEntries.size()]);
            serverEntries.clear();
        }
        for (final ServerEntry serverEntry : tmp) {
            serverEntry.serverTransport.close();
        }
    }

    @Override
    public void shutdownService() {
        try {
            close();
        } catch (IOException ex) {
            logger.error("Error on shutdown: {}", ex);
        }
    }

}
