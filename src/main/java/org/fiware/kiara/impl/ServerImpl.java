package org.fiware.kiara.impl;

import org.fiware.kiara.Context;
import org.fiware.kiara.server.Server;
import org.fiware.kiara.server.Service;
import org.fiware.kiara.serialization.Serializer;
import org.fiware.kiara.server.Servant;
import org.fiware.kiara.transport.ServerTransport;
import org.fiware.kiara.transport.impl.TransportServer;
import org.fiware.kiara.transport.impl.TransportServerImpl;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLException;

public class ServerImpl implements Server {

    private final Context context;
    private final TransportServer transportServer;
    private final List<Service> services;
    private final List<ServantDispatcher> servantDispatchers;

    public ServerImpl(Context context) {
        this.context = context;
        try {
            this.transportServer = new TransportServerImpl();
            services = new ArrayList<Service>();
            servantDispatchers = new ArrayList<ServantDispatcher>();
        } catch (CertificateException ex) {
            throw new RuntimeException(ex);
        } catch (SSLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void addService(Service service, ServerTransport serverTransport, Serializer serializer) throws IOException {
        services.add(service);

        ServantDispatcher srv = new ServantDispatcher(serializer, serverTransport);

        for (Servant servant : service.getGeneratedServants()) {
            srv.addService(servant);
        }

        servantDispatchers.add(srv);
        transportServer.listen(serverTransport, srv);
    }

    public void addService(Service service, String path, String protocol) throws IOException {
        addService(service, context.createServerTransport(path), context.createSerializer(protocol));
    }

    public boolean removeService(Service service) {
        return services.remove(service);
    }

    public void run() {
        try {
            transportServer.run();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void close() throws IOException {
        transportServer.close();
        for (ServantDispatcher servantDispatcher : servantDispatchers) {
            servantDispatcher.close();
        }
    }

}
