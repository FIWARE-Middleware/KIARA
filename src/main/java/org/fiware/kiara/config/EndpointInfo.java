/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2014 German Research Center for Artificial Intelligence (DFKI)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fiware.kiara.config;

import java.util.ArrayList;
import java.util.List;
import org.fiware.kiara.serialization.SerializerFactory;
import org.fiware.kiara.transport.TransportFactory;
import org.fiware.kiara.typecode.services.ServiceTypeDescriptor;

/**
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public class EndpointInfo {

    public final ServerInfo serverInfo;
    public TransportFactory transportFactory;
    public SerializerFactory serializerFactory;
    public final List<ServiceTypeDescriptor> serviceTypes;

    public EndpointInfo(ServerInfo serverInfo, TransportFactory transportFactory, SerializerFactory serializerFactory) {
        this.serverInfo = serverInfo;
        this.transportFactory = transportFactory;
        this.serializerFactory = serializerFactory;
        this.serviceTypes = new ArrayList<>();
    }

    public EndpointInfo(EndpointInfo other) {
        this.serverInfo = other.serverInfo;
        this.transportFactory = other.transportFactory;
        this.serializerFactory = other.serializerFactory;
        this.serviceTypes = new ArrayList<>();
    }

}
