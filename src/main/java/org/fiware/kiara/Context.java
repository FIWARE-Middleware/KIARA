package org.fiware.kiara;

import org.fiware.kiara.server.Server;
import org.fiware.kiara.server.Service;
import org.fiware.kiara.client.Connection;

import java.io.IOException;

import org.fiware.kiara.serialization.Serializer;
import org.fiware.kiara.transport.ServerTransport;
import org.fiware.kiara.transport.Transport;
import java.io.Closeable;

public interface Context extends Closeable {
    public Connection connect(String url) throws IOException;

    public Connection connect(Transport transport, Serializer serializer) throws IOException;

    public Service createService();

    // Create server without negotiation
    public Server createServer();

    public Transport createTransport(String url) throws IOException;

    public ServerTransport createServerTransport(String url) throws IOException;

    public Serializer createSerializer(String name) throws IOException;

}
