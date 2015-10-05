/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2014 Proyectos y Sistemas de Mantenimiento S.L. (eProsima)
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
package org.fiware.kiara.typecode.impl.services;

import java.util.ArrayList;

import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.services.FunctionTypeDescriptor;
import org.fiware.kiara.typecode.impl.TypeDescriptorImpl;
import org.fiware.kiara.typecode.services.ServiceTypeDescriptor;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*
*/
public class ServiceTypeDescriptorImpl extends TypeDescriptorImpl implements ServiceTypeDescriptor {

    private final ArrayList<FunctionTypeDescriptor> m_functionsDescriptors;
    private final String m_name;
    private final String m_scopedName;

    public ServiceTypeDescriptorImpl(String name, String scopedName) {
        super(TypeKind.SERVICE_TYPE);
        this.m_name = name;
        this.m_scopedName = scopedName;
        this.m_functionsDescriptors = new ArrayList<>();
    }

    /*public ServiceTypeDescriptorImpl(ArrayList<FunctionTypeDescriptor> functionsDescriptors) {
        super(TypeKind.SERVICE_TYPE);
        this.m_functionsDescriptors = functionsDescriptors;
    }*/
    
    @Override
    public boolean isService() {
        return false;
    }
    
    @Override
    public ArrayList<FunctionTypeDescriptor> getFunctions() {
        return (ArrayList<FunctionTypeDescriptor>) this.m_functionsDescriptors;
    }

    @Override
    public void addFunction(FunctionTypeDescriptor functionTypeDescriptor) {
        this.m_functionsDescriptors.add(functionTypeDescriptor.setServiceName(this.m_name));
    }

    @Override
    public String getName() {
        return this.m_name;
    }

    @Override
    public String getScopedName() {
        return this.m_scopedName;
    }

}
