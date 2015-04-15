package org.fiware.kiara.transport;

import java.io.Closeable;
import java.util.concurrent.ExecutorService;
import org.fiware.kiara.transport.impl.TransportConnectionListener;

public interface ServerTransport extends Closeable {

    public TransportFactory getTransportFactory();

    public void setDispatchingExecutor(ExecutorService executor);

    public ExecutorService getDispatchingExecutor();

    public boolean isRunning();

    public void startServer(TransportConnectionListener listener) throws InterruptedException;

    public void stopServer() throws InterruptedException;

    public String getLocalTransportAddress();

}
