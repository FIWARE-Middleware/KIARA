package org.fiware.kiara.test;

import org.fiware.kiara.Context;
import org.fiware.kiara.Kiara;
import org.fiware.kiara.client.Connection;
import org.fiware.kiara.server.Server;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class TestSetup<CLIENT_INTERFACE> {

    private final ExecutorService serverDispatchingExecutor;
    private final int port;
    private final String transport;
    private final String protocol;
    private final String configPath;
    private volatile Server server;
    private Context clientCtx;
    private Context serverCtx;

    public TestSetup(int port, String transport, String protocol, String configPath, TypeFactory<ExecutorService> serverDispatchingExecutorFactory) {
        this.port = port;
        this.transport = transport;
        this.protocol = protocol;
        this.configPath = configPath;
        this.server = null;
        this.clientCtx = null;
        this.serverCtx = null;
        this.serverDispatchingExecutor = serverDispatchingExecutorFactory != null ? serverDispatchingExecutorFactory.create() : null;
        System.out.printf("Testing port=%d transport=%s protocol=%s configPath=%s serverDispatchingExecutor=%s%n", port, transport, protocol, configPath, serverDispatchingExecutor);
    }

    public ExecutorService getServerDispatchingExecutor() {
        return serverDispatchingExecutor;
    }

    protected String makeClientTransportUri(String transport, int port, String protocol) {
        if ("tcp".equals(transport)) {
            return "tcp://0.0.0.0:" + port + "/?serialization=" + protocol;
        }

        throw new IllegalArgumentException("Unknown transport " + transport);
    }

    protected String makeServerTransportUri(String transport, int port) {
        if ("tcp".equals(transport)) {
            return "tcp://0.0.0.0:" + port;
        }
        throw new IllegalArgumentException("Unknown transport " + transport);
    }

    protected abstract Server createServer(Context serverCtx, int port, String transport, String protocol, String configPath) throws Exception;

    protected abstract CLIENT_INTERFACE createClient(Connection connection) throws Exception;

    public boolean checkConnection(int timeout) {
        System.out.println("Checking connection...");
        boolean connected = false;
        long currentTime, startTime;
        currentTime = startTime = System.currentTimeMillis();
        while (!connected && ((currentTime - startTime) < timeout)) {
            try {
                Socket s = new Socket(InetAddress.getLocalHost(), port);
                connected = s.isConnected();
                s.close();
            } catch (IOException ex) {
                //ex.printStackTrace();
            }
        }
        System.out.println(connected ? "Connection detected" : "No Connection !");
        return connected;
    }

    public CLIENT_INTERFACE start(int timeout) throws Exception {
        serverCtx = Kiara.createContext();
        server = createServer(serverCtx, port, transport, protocol, configPath);
        System.out.println("Starting server...");
        server.run();

        if (!checkConnection(timeout)) {
            throw new IOException("Could not start server");
        }

        clientCtx = Kiara.createContext();

        System.out.printf("Opening connection to %s with protocol %s...%n", transport, protocol);
        Connection connection = clientCtx.connect(makeClientTransportUri(transport, port, protocol)); // TODO delete

        return createClient(connection);
    }

    public void shutdown() throws Exception {
        System.out.println("Shutdown");
        if (server != null) {
            server.close();
        }
        if (clientCtx != null) {
            clientCtx.close();
        }
        if (serverCtx != null) {
            serverCtx.close();
        }
        if (serverDispatchingExecutor != null) {
            serverDispatchingExecutor.shutdown();
            serverDispatchingExecutor.awaitTermination(10, TimeUnit.MINUTES);
        }
    }

}
