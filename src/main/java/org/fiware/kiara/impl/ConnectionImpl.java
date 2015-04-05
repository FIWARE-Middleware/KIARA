package org.fiware.kiara.impl;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.ListMultimap;
import org.fiware.kiara.client.Connection;
import org.fiware.kiara.dynamic.services.DynamicProxy;
import org.fiware.kiara.serialization.Serializer;
import org.fiware.kiara.transport.Transport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.fiware.kiara.config.EndpointInfo;
import org.fiware.kiara.exceptions.ConnectException;
import org.fiware.kiara.typecode.services.ServiceTypeDescriptor;

public class ConnectionImpl implements Connection {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionImpl.class);

    private final URI configUri;
    private final ListMultimap<String, EndpointInfo> serviceProviders;
    private final Map<String, TransportAndSerializer> activeConnections;

    private static class TransportAndSerializer {

        public final Transport transport;
        public final Serializer serializer;

        public TransportAndSerializer(Transport transport, Serializer serializer) {
            this.transport = transport;
            this.serializer = serializer;
        }
    }

    public ConnectionImpl(URI configUri, ListMultimap<String, EndpointInfo> serviceProviders) {
        this.configUri = configUri;
        this.serviceProviders = serviceProviders;
        this.activeConnections = new HashMap<>();
    }

    public ConnectionImpl(Transport transport, Serializer serializer) {
        this.configUri = null;
        this.serviceProviders = null;
        this.activeConnections = new HashMap<>();
        this.activeConnections.put("*", new TransportAndSerializer(transport, serializer));
    }

    private EndpointInfo getBestEndpoint(String serviceName) {
        EndpointInfo selectedEndpoint = null;
        if (serviceProviders != null) {
            List<EndpointInfo> esiList = serviceProviders.get(serviceName);
            if (esiList == null || esiList.isEmpty()) {
                esiList = serviceProviders.get("*");
                if (esiList == null || esiList.isEmpty()) {
                    esiList = serviceProviders.get("");
                }
            }
            if (esiList != null) {
                for (EndpointInfo esi : esiList) {
                    // we change selected endpoint only if priority is higher
                    // i.e. when priority value is less than current one
                    if (selectedEndpoint != null
                            && selectedEndpoint.transportFactory.getPriority() + selectedEndpoint.serializerFactory.getPriority()
                            < esi.transportFactory.getPriority() + esi.serializerFactory.getPriority()) {
                        continue;
                    }
                    selectedEndpoint = esi;
                }
            }
        }
        return selectedEndpoint;
    }

    @Override
    public <T> T getServiceProxy(Class<T> interfaceClass) throws Exception {
        // name of the interface class can end with 'Async' or 'Client'
        String interfaceName = interfaceClass.getName();
        if (interfaceName.endsWith("Async")) {
            interfaceName = interfaceName.substring(0, interfaceName.length() - 5);
        } else if (interfaceName.endsWith("Client")) {
            interfaceName = interfaceName.substring(0, interfaceName.length() - 6);
        }
        final String proxyClassName = interfaceName + "Proxy";
        Class<?> proxyClass = Class.forName(proxyClassName);
        if (!interfaceClass.isAssignableFrom(proxyClass)) {
            throw new RuntimeException("Proxy class " + proxyClass + " does not implement interface " + interfaceClass);
        }

        String serviceName = interfaceName; // FIXME this is a hack
        try {
            final Field field = proxyClass.getField("serviceName");
            field.setAccessible(true);
            serviceName = (String) field.get(null);
        } catch (NoSuchFieldException ex) {

        }

        Constructor<?> proxyConstr = proxyClass.getConstructor(Serializer.class, Transport.class);
        proxyConstr.setAccessible(true);

        Serializer serializer = null;
        Transport transport = null;

        final TransportAndSerializer ts;
        synchronized (activeConnections) {
            ts = activeConnections.get(serviceName);
        }
        if (ts != null) {
            serializer = ts.serializer;
            transport = ts.transport;
        } else {
            final EndpointInfo esi = getBestEndpoint(serviceName);
            if (esi == null) {
                throw new ConnectException("No endpoint found for service '" + serviceName + "'");
            }

            logger.debug("Selected transport: {}", esi.serverInfo.transport.name);
            logger.debug("Selected protocol: {}", esi.serverInfo.protocol.name);

            URI transportUri = configUri == null ? new URI(esi.serverInfo.transport.url) : configUri.resolve(esi.serverInfo.transport.url);

            serializer = esi.serializerFactory.createSerializer();
            transport = esi.transportFactory.createTransport(transportUri.toString(), null).get();
            synchronized (activeConnections) {
                activeConnections.put(serviceName, new TransportAndSerializer(transport, serializer));
            }
        }

        return interfaceClass.cast(proxyConstr.newInstance(serializer, transport));
    }

    @Override
    public DynamicProxy getDynamicProxy(String name) {
        final EndpointInfo esi = getBestEndpoint(name);
        if (esi != null) {
            try {
                for (ServiceTypeDescriptor serviceType : esi.serviceTypes) {
                    if (serviceType.getScopedName().equals(name)) {
                        URI transportUri = configUri == null ? new URI(esi.serverInfo.transport.url) : configUri.resolve(esi.serverInfo.transport.url);

                        final Serializer serializer = esi.serializerFactory.createSerializer();
                        final Transport transport = esi.transportFactory.createTransport(transportUri.toString(), null).get();

                        return TypeMapper.createDynamicProxy(serviceType, serializer, transport);
                    }
                }
            } catch (Exception ex) {
                logger.error("getDynamicProxy", ex);
            }
        }
        return null;
    }

}
