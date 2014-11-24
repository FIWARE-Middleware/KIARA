package com.kiara;

import com.kiara.server.Server;
import com.kiara.server.Service;
import com.kiara.client.Connection;
import java.io.IOException;
import com.kiara.serialization.Serializer;
import com.kiara.transport.ServerTransport;
import com.kiara.transport.Transport;

public interface Context {
    public Connection connect(String url) throws IOException;

    public Connection connect(Transport transport, Serializer serializer) throws IOException;
    
    public Service createService();

    // Create server without negotiation
    public Server createServer();

    public Transport createTransport(String url) throws IOException;

    public ServerTransport createServerTransport(String url) throws IOException;

    public Serializer createSerializer(String name) throws IOException;

}
