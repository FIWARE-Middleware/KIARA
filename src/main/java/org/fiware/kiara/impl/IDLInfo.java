/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2015 German Research Center for Artificial Intelligence (DFKI)
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
package org.fiware.kiara.impl;

import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.fiware.kiara.exceptions.IDLParseException;
import org.fiware.kiara.server.Servant;
import org.fiware.kiara.typecode.services.ServiceTypeDescriptor;

/**
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public class IDLInfo {

    public final String idlContents;
    public final Set<Servant> servants;
    public final List<ServiceTypeDescriptor> serviceTypes;

    public IDLInfo(String idlContents) throws IDLParseException {
        this.idlContents = idlContents;
        this.servants = Sets.newIdentityHashSet();
        this.serviceTypes = new ArrayList<>();

        final ParserContextImpl ctx = IDLUtils.loadIDL(idlContents, "stdin");
        serviceTypes.addAll(TypeMapper.getServiceTypes(ctx));
    }

    public Servant getServant(String serviceName) {
        if (serviceName != null) {
            for (Servant servant : servants) {
                if (serviceName.equals(servant.getServiceName())) {
                    return servant;
                }
            }
        }
        return null;
    }

    public ServiceTypeDescriptor getServiceType(String serviceName) {
        if (serviceName != null) {
            for (ServiceTypeDescriptor serviceType : serviceTypes) {
                if (serviceName.equals(serviceType.getScopedName())) {
                    return serviceType;
                }
            }
        }
        return null;
    }

}
