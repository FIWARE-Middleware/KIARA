package org.fiware.kiara.transport;

import java.util.concurrent.ExecutorService;
import org.fiware.kiara.transport.impl.TransportConnectionListener;

public interface ServerTransport {

    public void setDispatchingExecutor(ExecutorService executor);

    public ExecutorService getDispatchingExecutor();

    public void startServer(TransportConnectionListener listener) throws InterruptedException;
}
