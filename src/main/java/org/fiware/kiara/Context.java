package org.fiware.kiara;

import org.fiware.kiara.server.Server;
import org.fiware.kiara.server.Service;
import org.fiware.kiara.client.Connection;

import java.io.IOException;

import org.fiware.kiara.serialization.Serializer;
import org.fiware.kiara.transport.ServerTransport;
import org.fiware.kiara.transport.Transport;
import java.io.Closeable;

/**
 * This interface is the starting point to use Advanced Middleware middleware.
 * It holds the configuration of the middleware and hides the process of negotiation, selection,
 * and configuration of the correct implementation classes. Also it provides users a way to
 * instantiate Advanced Middleware components.
 */
public interface Context extends Closeable {

    /**
     * This function creates a new connection to the server.
     * This connection might be used by proxies to send requests to the server.
     *
     * @param url
     * @return new connection instance
     * @throws IOException
     * @see Connection
     */
    public Connection connect(String url) throws IOException;

    /**
     * This function provides a direct way to instantiate a network transport
     * when the user wants later to configure it.
     *
     * @param transport
     * @param serializer
     * @return new connection instance
     * @throws IOException
     * @see Connection
     */
    public Connection connect(Transport transport, Serializer serializer) throws IOException;

    /**
     * This function creates a new service that user can use to register his servants.
     * @return new service instance
     * @see Service
     */
    public Service createService();

    /**
     * This function creates a new server that user can use to include new services.
     * @return new server instance
     * @see Server
     */
    public Server createServer();

    /**
     * This function provides a direct way to instantiate a network transport
     * when the user wants later to configure it.
     *
     * @param url
     * @return new transport instance
     * @throws IOException
     * @see Transport
     */
    public Transport createTransport(String url) throws IOException;

    /**
     * Create transport instance for server-side connection endpoint
     * waiting for incoming connections.
     *
     * @param url
     * @return new server transport instance
     * @throws IOException
     * @see ServerTransport
     */
    public ServerTransport createServerTransport(String url) throws IOException;

    /**
     * This function provides a direct way to instantiate a serializer when the
     * user wants later to configure it.
     *
     * @param name name of the serializer
     * @return new serializer instance
     * @throws IOException
     * @see Serializer
     */
    public Serializer createSerializer(String name) throws IOException;

}
