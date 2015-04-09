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
package org.fiware.kiara.transport.impl;

import org.fiware.kiara.RunningService;
import org.fiware.kiara.netty.NettyTransportFactory;
import org.fiware.kiara.transport.ServerTransport;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SSLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public class TransportServerImpl implements TransportServer, RunningService {


    private static class ServerEntry {

        public final ServerTransportImpl serverTransport;
        public final TransportConnectionListener listener;

        public ServerEntry(ServerTransportImpl serverTransport, TransportConnectionListener listener) {
            this.serverTransport = serverTransport;
            this.listener = listener;
        }

        public boolean isServerRunning() {
            return serverTransport.isRunning();
        }

        public void startServer() throws InterruptedException {
            serverTransport.startServer(listener);
        }

        public void stopServer() throws InterruptedException {
            serverTransport.stopServer();
        }

    }

    private static final Logger logger = LoggerFactory.getLogger(TransportServerImpl.class);

    private final List<ServerEntry> serverEntries = new ArrayList<>();
    private int numRunning = 0;

    public TransportServerImpl() throws CertificateException, SSLException {
        // bossGroup and workerGroup need to be always shutdown, so we are always running service
        // Kiara.addRunningService(this);
    }

    @Override
    public void listen(ServerTransport serverTransport, TransportConnectionListener listener) {
        if (!(serverTransport instanceof ServerTransportImpl)) {
            throw new IllegalArgumentException("transport factory is not an instance of " + ServerTransportImpl.class.getName() + " class");
        }
        final ServerTransportImpl st = (ServerTransportImpl) serverTransport;
        if (!(st.getTransportFactory() instanceof NettyTransportFactory)) {
            throw new IllegalArgumentException("transport factory is not an instance of " + NettyTransportFactory.class.getName() + " class");
        }
        synchronized (serverEntries) {
            serverEntries.add(new ServerEntry(st, listener));
        }
    }

    @Override
    public void run() throws IOException {
        synchronized (serverEntries) {
            try {
                for (ServerEntry serverEntry : serverEntries) {
                    if (!serverEntry.isServerRunning()) {
                        serverEntry.startServer();
                        ++numRunning;
                    }
                }
            } catch (InterruptedException ex) {
                throw new IOException(ex);
            }
        }
    }

    @Override
    public void stop() throws IOException {
        synchronized (serverEntries) {
            try {
                for (ServerEntry serverEntry : serverEntries) {
                    serverEntry.stopServer();
                    --numRunning;
                }
            } catch (InterruptedException ex) {
                throw new IOException(ex);
            }
        }
    }

    @Override
    public boolean isRunning() {
        synchronized (serverEntries) {
            return numRunning > 0;
        }
    }

    @Override
    public void close() throws IOException {
        synchronized (serverEntries) {
            stop();
            serverEntries.clear();
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
