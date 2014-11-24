package com.kiara.test;

import com.kiara.Context;
import com.kiara.Kiara;
import com.kiara.client.Connection;
import com.kiara.server.Server;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

abstract class TestSetup<CLIENT_INTERFACE> {
    private final int port;
    private final String transport;
    private final String protocol;
    private final String configPath;
    private volatile Server server;
    private Context clientCtx;
    private Context serverCtx;

    public TestSetup(int port, String transport, String protocol, String configPath) {
        this.port = port;
        this.transport = transport;
        this.protocol = protocol;
        this.configPath = configPath;
        this.server = null;
        this.clientCtx = null;
        this.serverCtx = null;
    }
    
    protected abstract String makeClientTransportUri(String transport, int port, String protocol);

    protected abstract String makeServerTransportUri(String transport, int port);

    protected abstract Server createServer(Context serverCtx, int port, String transport, String protocol, String configPath) throws Exception;

    protected abstract CLIENT_INTERFACE createClient(Connection connection) throws Exception;

    public boolean checkConnection(int timeout) {
        System.out.println("Checking connection...");
        boolean connected = false;
        long currentTime, startTime;
        currentTime = startTime = System.currentTimeMillis();
        while (!connected && ((currentTime-startTime) < timeout)) try {
            Socket s = new Socket(InetAddress.getLocalHost(), port);
            connected = s.isConnected();
            s.close();
        } catch (IOException ex) {
            //ex.printStackTrace();
        }
        System.out.println(connected ? "Connection detected" : "No Connection !");
        return connected;
    }

    public CLIENT_INTERFACE start(int timeout) throws Exception {
        serverCtx = Kiara.createContext();
        server = createServer(serverCtx, port, transport, protocol, configPath);
        System.out.println("Starting server...");
        server.run();

        if (!checkConnection(timeout))
            throw new IOException("Could not start server");

        clientCtx = Kiara.createContext();

        System.out.printf("Opening connection to %s with protocol %s...%n", transport, protocol);
        Connection connection = clientCtx.connect(makeClientTransportUri(transport, port, protocol));

        return createClient(connection);
    }

    void shutdown() throws Exception {
        System.out.println("Shutdown");
    }

}
