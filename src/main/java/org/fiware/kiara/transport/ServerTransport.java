package org.fiware.kiara.transport;

import java.util.concurrent.ExecutorService;

public interface ServerTransport {
    public void setDispatchingExecutor(ExecutorService executor);
    public ExecutorService getDispatchingExecutor();
}
