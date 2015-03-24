package org.fiware.kiara.server;

import org.fiware.kiara.serialization.Serializer;
import org.fiware.kiara.transport.ServerTransport;
import java.io.Closeable;

import java.io.IOException;
import java.net.URISyntaxException;

public interface Server extends Closeable {

    public void enableNegotiationService(String host, int port, String configPath) throws URISyntaxException;

    public void disableNegotiationService();

    public void addService(Service service, String path, String protocol) throws IOException;

    public void addService(Service service, ServerTransport serverTransport, Serializer serializer) throws IOException;

    public boolean removeService(Service service);

    public void run();
}
