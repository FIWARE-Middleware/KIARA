package org.fiware.kiara.transport;

import java.io.Closeable;
import java.util.concurrent.ExecutorService;
import org.fiware.kiara.transport.impl.TransportConnectionListener;

/**
 * This interface provides an abstraction for server-side connection endpoint
 * waiting for incoming connections.
 */
public interface ServerTransport extends Closeable {

    /**
     * This function returns an instance of a factory class which was used to
     * create this server transport instance.
     *
     * @return factory instance that was used to create this server transport
     * @see TransportFactory
     */
    public TransportFactory getTransportFactory();

    /**
     * This function sets executor service used for dispatching incoming messages.
     * @param executor
     * @see ExecutorService
     */
    public void setDispatchingExecutor(ExecutorService executor);

    /**
     * Returns executor service previously set.
     * @return currently used dispatching executor service
     * @see ExecutorService
     */
    public ExecutorService getDispatchingExecutor();

    /**
     * Returns true if server is up and waiting for incoming connections.
     * @return running status
     */
    public boolean isRunning();

    /**
     * Starts server.
     * @param listener instance of the {@link TransportConnectionListener} that will be notified on incoming connections.
     * @throws InterruptedException
     */
    public void startServer(TransportConnectionListener listener) throws InterruptedException;

    /**
     * Stops server.
     * @throws InterruptedException
     */
    public void stopServer() throws InterruptedException;

    /**
     * Returns transport address to which this server is bound.
     * @return transport address
     */
    public String getLocalTransportAddress();

}
