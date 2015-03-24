package org.fiware.kiara.impl;

import com.google.common.collect.Sets;
import org.fiware.kiara.Context;
import org.fiware.kiara.server.Server;
import org.fiware.kiara.server.Service;
import org.fiware.kiara.serialization.Serializer;
import org.fiware.kiara.server.Servant;
import org.fiware.kiara.transport.ServerTransport;
import org.fiware.kiara.transport.impl.TransportServer;
import org.fiware.kiara.transport.impl.TransportServerImpl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLException;
import org.fiware.kiara.transport.impl.TransportFactory;

public class ServerImpl implements Server {

    private final Context context;
    private final TransportServer transportServer;
    private final List<Service> services;
    private final List<ServantDispatcher> servantDispatchers;
    private final Map<Class<?>, IDLInfo> idlInfoMap;

    private String configHost;
    private int configPort;
    private String configPath;
    private URI configUri;
    private NegotiationHandler negotiationHandler;

    private static class IDLInfo {

        public final String idlContents;
        public final Set<Servant> servants;

        public IDLInfo(String idlContents) {
            this.idlContents = idlContents;
            this.servants = Sets.newIdentityHashSet();
        }
    }

    public ServerImpl(Context context) {
        this.context = context;
        try {
            this.transportServer = new TransportServerImpl();
            services = new ArrayList<Service>();
            servantDispatchers = new ArrayList<ServantDispatcher>();
            idlInfoMap = new HashMap<>();
        } catch (CertificateException ex) {
            throw new RuntimeException(ex);
        } catch (SSLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private IDLInfo addServantToIDLInfo(Servant servant) {
        try {

            final Class<?> servantCls = servant.getClass();
            final Class<?> idlInfoClass = Class.forName(servantCls.getPackage().getName() + ".IDLText");

            IDLInfo idlInfo = idlInfoMap.get(idlInfoClass);
            if (idlInfo == null) {

                final Field field = idlInfoClass.getField("contents");
                final String idlContents = (String) field.get(null);

                idlInfo = new IDLInfo(idlContents);

                idlInfoMap.put(idlInfoClass, idlInfo);

                System.err.println("IDL contents " + idlContents); //???DEBUG
            }

            idlInfo.servants.add(servant);

            return idlInfo;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void addService(Service service, ServerTransport serverTransport, Serializer serializer) throws IOException {
        services.add(service);

        ServantDispatcher srv = new ServantDispatcher(serializer, serverTransport);

        for (Servant servant : service.getGeneratedServants()) {
            addServantToIDLInfo(servant);
            srv.addService(servant);
        }

        servantDispatchers.add(srv);
        transportServer.listen(serverTransport, srv);
    }

    @Override
    public void addService(Service service, String path, String protocol) throws IOException {
        addService(service, context.createServerTransport(path), context.createSerializer(protocol));
    }

    @Override
    public boolean removeService(Service service) {
        return services.remove(service);
    }

    @Override
    public void enableNegotiationService(String host, int port, String configPath) throws URISyntaxException {
        this.configHost = host;
        this.configPort = port;
        this.configPath = configPath;
        this.configUri = new URI("http://" + configHost + ":" + Integer.toString(configPort) + "/" + configPath).normalize();
        this.negotiationHandler = new NegotiationHandler();

        final TransportFactory transportFactory = ContextImpl.getTransportFactoryByURI(this.configUri);
        final ServerTransport serverTransport;
        try {
            serverTransport = transportFactory.createServerTransport(this.configUri.toString());
            transportServer.listen(serverTransport, negotiationHandler);
        } catch (IOException ex) {
            //???TODO
        }
    }

    @Override
    public void disableNegotiationService() {
        this.configHost = null;
        this.configPort = -1;
        this.configPath = null;
        this.configUri = null;
    }

    @Override
    public void run() {
        try {
            transportServer.run();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void close() throws IOException {
        transportServer.close();
        for (ServantDispatcher servantDispatcher : servantDispatchers) {
            servantDispatcher.close();
        }
    }

}
