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
package org.fiware.kiara.benchmark;

import org.fiware.kiara.Context;
import java.io.IOException;
import java.net.Socket;
import org.fiware.kiara.Kiara;
import org.fiware.kiara.server.Server;

/**
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public class BenchmarkTool {

    public static void main(String[] args) throws Exception {
        //System.setProperty("java.util.logging.config.file", "/home/rubinste/.kiara/logging.properties");
        System.setProperty("java.util.logging.config.file", "");
        final int port = 53212;

        final Context c1 = Kiara.createContext();

        Server server = BenchmarkServerTool.runServer(c1, port, "cdr");

        boolean connected = false;
        while (!connected) {
            try {
                Socket s = new Socket("localhost", port);
                connected = s.isConnected();
                s.close();
            } catch (IOException ex) {
                //ex.printStackTrace();
            }
        }

        System.out.println("Run measurements...");

        final Context c2 = Kiara.createContext();
        BenchmarkClientTool.runClient(c2, "tcp://localhost:" + port + "?serialization=cdr");

        System.out.println("Shutdown");

        server.close();
        c1.close();
        c2.close();
        Kiara.shutdown();
    }
}
