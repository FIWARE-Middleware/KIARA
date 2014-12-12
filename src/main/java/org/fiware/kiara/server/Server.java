package org.fiware.kiara.server;

import org.fiware.kiara.serialization.Serializer;
import org.fiware.kiara.transport.ServerTransport;
import java.io.Closeable;

import java.io.IOException;

public interface Server extends Closeable {

    public void addService(Service service, String path, String protocol) throws IOException;

    public void addService(Service service, ServerTransport serverTransport, Serializer serializer) throws IOException;

    public boolean removeService(Service service);

    public void run();
}
