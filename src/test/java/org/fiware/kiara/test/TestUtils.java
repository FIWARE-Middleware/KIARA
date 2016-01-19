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
package org.fiware.kiara.test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public class TestUtils {

    public static boolean checkConnection(InetAddress address, int port, int timeout) {
        System.out.println("Checking connection...");
        boolean connected = false;
        long currentTime, startTime;
        currentTime = startTime = System.currentTimeMillis();
        while (!connected && ((currentTime - startTime) < timeout)) {
            try {
                Socket s = new Socket(address, port);
                connected = s.isConnected();
                s.close();
            } catch (IOException ex) {
                //ex.printStackTrace();
            }
        }
        System.out.println(connected ? "Connection detected" : "No Connection !");
        return connected;
    }

    public static boolean checkConnection(int port, int timeout) {
        try {
            return checkConnection(InetAddress.getLocalHost(), port, timeout);
        } catch (UnknownHostException ex) {
            return false;
        }
    }

    public static TypeFactory[] createExecutorFactories() {
        final TypeFactory[] executorFactories = {
            null,
            new TypeFactory<ExecutorService>() {
                @Override
                public ExecutorService create() {
                    return Executors.newSingleThreadExecutor();
                }
            },
            new TypeFactory<ExecutorService>() {

                @Override
                public ExecutorService create() {
                    return Executors.newFixedThreadPool(2);
                }
            },
            new TypeFactory<ExecutorService>() {
                @Override
                public ExecutorService create() {
                    return Executors.newCachedThreadPool();
                }
            }
        };
        return executorFactories;
    }

    public static Collection<?> createDefaultTestConfig() {
        Collection<Object[]> params = new ArrayList<>();
        final String[] transports = {"tcp", "tcps"};
        final String[] protocols = {"cdr"};
        final TypeFactory[] executorFactories = TestUtils.createExecutorFactories();

        for (String transport : transports) {
            for (String protocol : protocols) {
                for (TypeFactory executorFactory : executorFactories) {
                    Object[] config = new Object[]{transport, protocol, executorFactory};
                    params.add(config);
                }
            }
        }
        return params;
    }
    
    public static Collection<?> createHttpTestConfig() {
        Collection<Object[]> params = new ArrayList<>();
        final String[] transports = {"http"};
        final String[] protocols = {"cdr"};
        final TypeFactory[] executorFactories = TestUtils.createExecutorFactories();

        for (String transport : transports) {
            for (String protocol : protocols) {
                for (TypeFactory executorFactory : executorFactories) {
                    Object[] config = new Object[]{transport, protocol, executorFactory};
                    params.add(config);
                }
            }
        }
        return params;
    }


}
