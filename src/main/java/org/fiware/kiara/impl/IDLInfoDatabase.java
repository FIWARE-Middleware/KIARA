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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.fiware.kiara.dynamic.impl.services.DynamicServant;
import org.fiware.kiara.exceptions.IDLParseException;
import org.fiware.kiara.server.Servant;
import org.fiware.kiara.typecode.services.ServiceTypeDescriptor;

/**
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public class IDLInfoDatabase {

    private final Map<Class<?>, IDLInfo> idlInfoMap;
    private final List<IDLInfo> idlInfos;

    public IDLInfoDatabase() {
        this.idlInfoMap = new HashMap<>();
        this.idlInfos = new ArrayList<>();
    }

    public ServiceTypeDescriptor getServiceType(String serviceName) {
        for (IDLInfo idlInfo : idlInfos) {
            final ServiceTypeDescriptor type = idlInfo.getServiceType(serviceName);
            if (type != null) {
                return type;
            }
        }
        return null;
    }

    public IDLInfo getIDLInfoByServiceName(String serviceName) {
        for (IDLInfo idlInfo : idlInfos) {
            final ServiceTypeDescriptor type = idlInfo.getServiceType(serviceName);
            if (type != null) {
                return idlInfo;
            }
        }
        return null;
    }

    public boolean addIDLInfo(IDLInfo idlInfo) {
        return idlInfos.add(idlInfo);
    }

    public boolean addServant(Servant servant) {
        try {
            IDLInfo idlInfo = null;

            if (servant instanceof DynamicServant) {
                for (IDLInfo i : idlInfos) {
                    final ServiceTypeDescriptor type = i.getServiceType(servant.getServiceName());
                    if (type != null) {
                        if (i.getServant(servant.getServiceName()) == null) {
                            idlInfo = i;
                            break;
                        }
                    }
                }
            } else {
                final Class<?> servantCls = servant.getClass();
                final Class<?> idlInfoClass = Class.forName(servantCls.getPackage().getName() + ".IDLText");

                idlInfo = idlInfoMap.get(idlInfoClass);
                if (idlInfo == null) {

                    final Field field = idlInfoClass.getField("contents");
                    final String idlContents = (String) field.get(null);

                    idlInfo = new IDLInfo(idlContents);

                    idlInfoMap.put(idlInfoClass, idlInfo);
                    idlInfos.add(idlInfo);
                }
            }
            if (idlInfo != null) {
                idlInfo.servants.add(servant);
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace(); //???DEBUG
        }
        return false;
    }

    public List<IDLInfo> getIDLInfos() {
        return idlInfos;
    }

    public void loadServiceIDLFromString(String idlContents) throws IDLParseException {
        final IDLInfo idlInfo = new IDLInfo(idlContents);
        idlInfos.add(idlInfo);
    }

}
