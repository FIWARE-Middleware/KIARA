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
import org.fiware.kiara.Kiara;
import org.fiware.kiara.server.Server;
import org.fiware.kiara.server.Service;

/**
 *
 * @author Shahzad
 */
public class BenchmarkServerTool {

    public static class BenchmarkImpl extends benchmarkServant {

        int messageCounter = 0;

        public MarketData sendMarketData(MarketData marketData) {
            int counter = messageCounter;
            ++messageCounter;
            MarketData returnData = marketData;
            returnData.setIsEcho(true);
            return returnData;
        }

        public QuoteRequest sendQuoteRequest(QuoteRequest quoteRequest) {
            int counter = messageCounter;
            ++messageCounter;

            QuoteRequest returnRequest = quoteRequest;
            returnRequest.setIsEcho(true);
            return returnRequest;
        }
    }

    public static Server runServer(Context context, int port, String protocol) throws Exception {

        /* Create a new service */
        Service service = context.createService();

        System.out.printf("Register benchmark.sendMarketData ...%n");

        /* Create new instance of the implementation class */
        BenchmarkImpl benchmarkImpl = new BenchmarkImpl();

        service.register(benchmarkImpl);

        /*
         * Create new server and register service
         */
        Server server = context.createServer();

        server.addService(service, "tcp://0.0.0.0:" + port, protocol);

        System.out.printf("Starting server...%n");

        /* Run server */
        server.run();
        return server;
    }

    public static void main(String args[]) throws Exception {
        int port;
        String protocol;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        if (args.length > 1) {
            protocol = args[1];
        } else {
            protocol = "cdr";
        }
        System.out.printf("Server port: %d%n", port);
        System.out.printf("Protocol: %s%n", protocol);

        /* Create new context,
         * it will be automatically closed at the end of
         * the try block
         */
        try (Context context = Kiara.createContext()) {
            runServer(context, port, protocol);
        } catch (Exception e) {
            System.out.printf("Error: %s", e.getMessage());
            System.exit(1);
        } finally {
            // Kiara.shutdownGracefully();
        }
    }

}
