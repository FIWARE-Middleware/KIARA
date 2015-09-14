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
     * @param host
     * @param port
     * @param configPath
     * @throws URISyntaxException
     */
    public void enableNegotiationService(String host, int port, String configPath) throws URISyntaxException;

    /**
     * Disables negotiation service.
     */
    public void disableNegotiationService();

    /**
     * This function registers service on specified URL and with specified serialization protocol.
     * @param service
     * @param path
     * @param protocol
     * @throws IOException
     */
    public void addService(Service service, String path, String protocol) throws IOException;

    /**
     * This function registers service on specified URL and with specified serialization protocol.
     * @param service
     * @param serverTransport
     * @param serializer
     * @throws IOException
     */
    public void addService(Service service, ServerTransport serverTransport, Serializer serializer) throws IOException;

    /**
     * Removes previously registered service.
     *
     * @param service
     * @return true if service was removed, false if service was not registered
     * with this server.
     */
    public boolean removeService(Service service);

    /**
     * Starts server.
     */
    public void run();
}
