package org.fiware.kiara.server;

import org.fiware.kiara.serialization.Serializer;
import org.fiware.kiara.transport.ServerTransport;
import java.io.Closeable;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Through this interface, users can start up multiple services on different
 * ports. The implementation uses serialization mechanisms and network
 * transports to listen for clients? requests and executes the proper servant
 * implementation. Optional negotiation protocol provides automatic discovery
 * of all available services via HTTP protocol.
 *
 */
public interface Server extends Closeable {

    /**
     * Enables negotiation service on specified port and configuration path.
     *
     * @param host The host to create the Endpoint into
     * @param port The port in which the server will be listening
     * @param configPath Configuration path
     * @throws URISyntaxException If the URI is not well formatted
     */
    public void enableNegotiationService(String host, int port, String configPath) throws URISyntaxException;

    /**
     * Disables negotiation service.
     */
    public void disableNegotiationService();

    /**
     * This function registersa  {@link Service} on a specified URL and with specified serialization protocol.
     * @param service The {@link Service} to be registered
     * @param path The path on the server
     * @param protocol The protocol to be used
     * @throws IOException If anything goes wrong
     */
    public void addService(Service service, String path, String protocol) throws IOException;

    /**
     * This function registers {@link Service} on specified URL and with specified serialization protocol.
     * @param service The {@link Service} to be registered
     * @param serverTransport The transport to be used
     * @param serializer The serialization mechanism
     * @throws IOException If anything goes wrong
     */
    public void addService(Service service, ServerTransport serverTransport, Serializer serializer) throws IOException;

    /**
     * Removes previously registered service.
     *
     * @param service The {@link Service} to be removed
     * @return true if service was removed, false if service was not registered
     * with this server.
     */
    public boolean removeService(Service service);

    /**
     * Starts server.
     */
    public void run();
}
