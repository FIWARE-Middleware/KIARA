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
import java.net.URI;
import java.net.URISyntaxException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLException;

import org.fiware.kiara.config.ProtocolInfo;
import org.fiware.kiara.config.ServerConfiguration;
import org.fiware.kiara.config.ServerInfo;
import org.fiware.kiara.dynamic.impl.services.DynamicServant;
import org.fiware.kiara.dynamic.services.DynamicFunctionHandler;
import org.fiware.kiara.transport.TransportFactory;
import org.fiware.kiara.typecode.services.FunctionTypeDescriptor;
import org.fiware.kiara.typecode.services.ServiceTypeDescriptor;

public class ServerImpl implements Server {

    private final Context context;
    private final TransportServer transportServer;
    private final List<ServiceInstanceInfo> serviceInstanceInfos;
    private final List<ServantDispatcher> servantDispatchers;
    private final Map<String, DynamicServant> dynamicServants;
    private NegotiationHandler negotiationHandler;

    private String configHost;
    private int configPort;
    private String configPath;
    private URI configUri;

    private static class ServiceInstanceInfo {

        public final Service service;
        public final ServerTransport serverTransport;
        public final Serializer serializer;
        public final ProtocolInfo protocolInfo;

        public ServiceInstanceInfo(Service service, ServerTransport serverTransport, Serializer serializer) {
            this.service = service;
            this.serverTransport = serverTransport;
            this.serializer = serializer;
            this.protocolInfo = new ProtocolInfo();
            this.protocolInfo.name = serializer.getName();
        }
    }

    public ServerImpl(Context context) {
        this.context = context;
        try {
            this.transportServer = new TransportServerImpl();
            serviceInstanceInfos = new ArrayList<>();
            servantDispatchers = new ArrayList<>();
            dynamicServants = new HashMap<>();
            negotiationHandler = null;
        } catch (CertificateException | SSLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public final ServerConfiguration generateServerConfiguration(String localHostName, String remoteHostName) {
        final ServerConfiguration serverConfiguration = new ServerConfiguration();

        final Set<String> allServiceNames = new HashSet<>();
        final Set<IDLInfo> allIdlInfos = Sets.newIdentityHashSet();

        synchronized (serviceInstanceInfos) {
            for (ServiceInstanceInfo element : serviceInstanceInfos) {
                final ServerInfo serverInfo = new ServerInfo();
                serverInfo.protocol.set(element.protocolInfo);

                // create a list of all available services
                final ServiceImpl serviceImpl = (ServiceImpl) element.service;

                final Set<String> serviceNames = new HashSet<>();

                for (IDLInfo idlInfo : serviceImpl.getIDLInfoDatabase().getIDLInfos()) {
                    for (ServiceTypeDescriptor serviceTypeDescr : idlInfo.getServiceTypes()) {
                        if (allServiceNames.add(serviceTypeDescr.getScopedName())) {
                            allIdlInfos.add(idlInfo);
                        }
                    }
                    for (Servant servant : idlInfo.servants) {
                        serviceNames.add(servant.getServiceName());
                    }
                }

                final Map<FunctionTypeDescriptor, DynamicFunctionHandler> dynamicHandlers = serviceImpl.getDynamicHandlers();
                for (Map.Entry<FunctionTypeDescriptor, DynamicFunctionHandler> entry : dynamicHandlers.entrySet()) {
                    serviceNames.add(entry.getKey().getServiceName());
                }

                for (String serviceName : serviceNames) {
                    serverInfo.services.add(serviceName);
                }

                // get transport parameters
                serverInfo.transport.name = element.serverTransport.getTransportFactory().getName();
                try {
                    URI uri = new URI(element.serverTransport.getLocalTransportAddress());
                    if ("0.0.0.0".equals(uri.getHost())) {
                        uri = new URI(uri.getScheme(),
                                uri.getUserInfo(), localHostName, uri.getPort(),
                                uri.getPath(), uri.getQuery(),
                                uri.getFragment());
                    }
                    serverInfo.transport.url = uri.toString();
                    serverConfiguration.servers.add(serverInfo);
                } catch (URISyntaxException ex) {
                }
            }
        }

        final StringBuilder builder = new StringBuilder();
        for (IDLInfo idlInfo : allIdlInfos) {
            builder.append(idlInfo.idlContents);
        }
        serverConfiguration.idlContents = builder.toString();
        return serverConfiguration;
    }

    @Override
    public void addService(Service service, ServerTransport serverTransport, Serializer serializer) throws IOException {
        ServantDispatcher dispatcher = new ServantDispatcher(serializer, serverTransport);

        ServiceInstanceInfo serviceInstanceInfo = new ServiceInstanceInfo(service, serverTransport, serializer);
        synchronized (serviceInstanceInfos) {
            serviceInstanceInfos.add(serviceInstanceInfo);
        }

        ServiceImpl serviceImpl = (ServiceImpl) service;

        for (IDLInfo idlInfo : serviceImpl.getIDLInfoDatabase().getIDLInfos()) {

            for (Servant servant : idlInfo.servants) {
                dispatcher.addServant(servant);
            }
        }

        Map<FunctionTypeDescriptor, DynamicFunctionHandler> dynamicHandlers = serviceImpl.getDynamicHandlers();

        for (Map.Entry<FunctionTypeDescriptor, DynamicFunctionHandler> entry : dynamicHandlers.entrySet()) {
            final String serviceName = entry.getKey().getServiceName();
            DynamicServant servant = dynamicServants.get(serviceName);
            if (servant == null) {
                servant = new DynamicServant(serviceName);
                dynamicServants.put(serviceName, servant);
            }
            servant.addFunctionHandler(entry.getKey(), entry.getValue());
            dispatcher.addServant(servant);
        }

        servantDispatchers.add(dispatcher);
        transportServer.listen(serverTransport, dispatcher);
    }

    @Override
    public void addService(Service service, String path, String protocol) throws IOException {
        addService(service, context.createServerTransport(path), context.createSerializer(protocol));
    }

    @Override
    public boolean removeService(Service service) {
        boolean removed = false;
        synchronized (serviceInstanceInfos) {
            for (Iterator<ServiceInstanceInfo> iter = serviceInstanceInfos.iterator(); iter.hasNext();) {
                final ServiceInstanceInfo element = iter.next();
                if (element.service != null) {
                    if (element.service.equals(service)) {
                        iter.remove();
                        removed = true;
                    }
                }
            }
        }
        return removed;
    }

    @Override
    public void enableNegotiationService(String host, int port, String configPath) throws URISyntaxException {
        if (transportServer.isRunning()) {
            throw new IllegalStateException("Transport server is already running");
        }
        this.configHost = host;
        this.configPort = port;
        this.configPath = configPath;
        this.configUri = new URI("http://" + configHost + ":" + Integer.toString(configPort) + "/" + configPath).normalize();
        if (this.negotiationHandler == null) {
            this.negotiationHandler = new NegotiationHandler(this);
        }

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
        if (transportServer.isRunning()) {
            throw new IllegalStateException("Transport server is already running");
        }
        this.configHost = null;
        this.configPort = -1;
        this.configPath = null;
        this.configUri = null;
    }

    public final URI getConfigUri() {
        return configUri;
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
