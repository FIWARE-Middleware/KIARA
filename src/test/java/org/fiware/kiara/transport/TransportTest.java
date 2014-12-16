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
package org.fiware.kiara.transport;

import com.google.common.collect.Sets;
import org.fiware.kiara.Context;
import org.fiware.kiara.Kiara;
import org.fiware.kiara.test.TestUtils;
import org.fiware.kiara.transport.impl.ServerTransportImpl;
import org.fiware.kiara.transport.impl.TransportConnectionListener;
import org.fiware.kiara.transport.impl.TransportImpl;
import org.fiware.kiara.transport.impl.TransportMessage;
import org.fiware.kiara.transport.impl.TransportMessageListener;
import org.fiware.kiara.transport.impl.TransportServer;
import org.fiware.kiara.transport.impl.TransportServerImpl;
import org.fiware.kiara.util.Buffers;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.security.cert.CertificateException;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.SynchronousQueue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public class TransportTest {

    static {
        //System.setProperty("java.util.logging.config.file", "/home/rubinste/.kiara/logging.properties");
    }

    public TransportTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    private static class EchoServer implements Runnable, Closeable, TransportMessageListener {

        private final ServerTransportImpl serverTransport;
        private final TransportServer transportServer;
        private final Set<TransportImpl> connections;
        private final int port;

        public EchoServer(Context context, String transportName, int port, String path) throws IOException, CertificateException {
            this.port = port;
            String url = transportName + "://0.0.0.0:" + port;
            if (path != null) {
                url += path;
            }
            this.serverTransport = (ServerTransportImpl) context.createServerTransport(url);
            this.transportServer = new TransportServerImpl();
            this.connections = Sets.newIdentityHashSet();

            transportServer.listen(serverTransport, new TransportConnectionListener() {

                public void onConnectionOpened(TransportImpl connection) {
                    System.err.println("New connection opened: " + connection);
                    synchronized (connections) {
                        connections.add(connection);
                    }
                    connection.addMessageListener(EchoServer.this);
                }

                public void onConnectionClosed(TransportImpl connection) {
                    System.err.println("Connection closed: " + connection);
                    synchronized (connections) {
                        connections.remove(connection);
                    }
                    connection.removeMessageListener(EchoServer.this);
                }
            });

        }

        public int getPort() {
            return port;
        }

        @Override
        public boolean onMessage(TransportMessage message) {
            final TransportImpl transport = message.getTransport();
            final TransportMessage tresponse = transport.createTransportMessage(message);
            tresponse.setPayload(message.getPayload().duplicate());
            transport.send(tresponse);
            return true;
        }

        public void run() {
            try {
                transportServer.run();
            } catch (IOException ex) {
                System.err.println("Exception: " + ex);
            }
        }

        public void close() throws IOException {
            transportServer.close();
        }

    }

    private static final String transportName = "tcp";
    private static final int transportPort = 5555;

    private Context context;
    private EchoServer echoServer;

    @Before
    public void setUp() throws Exception {
        context = Kiara.createContext();
        echoServer = new EchoServer(context, transportName, transportPort, null);
        echoServer.run();
        assertTrue(TestUtils.checkConnection(echoServer.getPort(), 100));
    }

    @After
    public void tearDown() throws Exception {
        echoServer.close();
        context.close();
    }

    public ByteBuffer sendAndReceive(TransportImpl transport, ByteBuffer data) throws Exception {
        final SynchronousQueue<Object> sync = new SynchronousQueue<Object>();
        TransportMessage trequest = transport.createTransportMessage(null);
        trequest.setPayload(data);

        final TransportMessageListener messageListener = new TransportMessageListener() {

            @Override
            public boolean onMessage(TransportMessage message) {
                message.getTransport().removeMessageListener(this);
                try {
                    sync.put(message.getPayload());
                } catch (InterruptedException ex) {
                    try {
                        sync.put(ex);
                    } catch (InterruptedException ex1) {
                        System.err.println("Exception: " + ex1);
                    }
                }
                return true;
            }
        };

        transport.addMessageListener(messageListener);

        transport.send(trequest);
        Object result = sync.take();

        assertFalse(transport.removeMessageListener(messageListener));

        if (result instanceof ByteBuffer) {
            return (ByteBuffer) result;
        }
        throw (Exception) result;
    }

    @Test
    public void testSendReceiveText() throws Exception {
        TransportImpl transport = (TransportImpl) context.createTransport(transportName + "://" + InetAddress.getLocalHost().getHostName() + ":" + transportPort);
        assertNotNull(transport);
        assertTrue(transport.isOpen());

        {
            final ByteBuffer data = Buffers.stringToBuffer("DATA TEST", "UTF-8");
            final ByteBuffer test = sendAndReceive(transport, data);
            assertEquals(test, data);
        }

        {
            final ByteBuffer data = Buffers.stringToBuffer("TEST123", "UTF-8");
            final ByteBuffer test = sendAndReceive(transport, data);
            assertEquals(test, data);
        }

        transport.close();
        assertFalse(transport.isOpen());
    }

    @Test
    public void testSendReceiveBinary() throws Exception {
        TransportImpl transport = (TransportImpl) context.createTransport(transportName + "://" + InetAddress.getLocalHost().getHostName() + ":" + transportPort);
        assertNotNull(transport);
        assertTrue(transport.isOpen());

        Random rnd = new Random();
        final byte[] array = new byte[100];
        for (int i = 0; i < 10; ++i) {
            rnd.nextBytes(array);
            final ByteBuffer data = ByteBuffer.wrap(array);
            final ByteBuffer test = sendAndReceive(transport, data);
            assertEquals(test, data);
        }

        transport.close();
        assertFalse(transport.isOpen());
    }

}
