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
package org.fiware.kiara.dynamic.impl.services;

import java.io.IOException;
import java.util.ArrayList;

import org.fiware.kiara.dynamic.services.DynamicFunctionRequest;
import org.fiware.kiara.dynamic.data.DynamicData;
import org.fiware.kiara.dynamic.data.DynamicException;
import org.fiware.kiara.dynamic.data.DynamicMember;
import org.fiware.kiara.dynamic.impl.DynamicTypeImpl;
import org.fiware.kiara.dynamic.impl.data.DynamicMemberImpl;
import org.fiware.kiara.exceptions.DynamicTypeException;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.typecode.TypeDescriptor;
import org.fiware.kiara.typecode.services.FunctionTypeDescriptor;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*
*/
public class DynamicFunctionRequestImpl extends DynamicTypeImpl implements DynamicFunctionRequest {
    
    private ArrayList<DynamicMember> m_parameters;
    
    public DynamicFunctionRequestImpl(FunctionTypeDescriptor typeDescriptor) {
        super(typeDescriptor, "DynamicFunctionImpl");
        this.m_parameters = new ArrayList<DynamicMember>();
    }
    
    public void addParameter(DynamicData parameter, String name) {
        this.m_parameters.add(new DynamicMemberImpl(parameter, name));
    }
    
    /*public ArrayList<DynamicData> getParameter() {
        return this.m_parameters;
    }*/
    
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        impl.serializeOperation(message, ((FunctionTypeDescriptor) this.m_typeDescriptor).getName());
        
        for (DynamicMember parameter : this.m_parameters) {
            parameter.getDynamicData().serialize(impl, message, name);
        }
    }

    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        String function_name = impl.deserializeOperation(message);
        
        if (((FunctionTypeDescriptor) this.m_typeDescriptor).getName().equals(function_name)) {
            for (DynamicMember parameter : this.m_parameters) {
                parameter.getDynamicData().deserialize(impl, message, name);
            }
        } else {
            throw new DynamicTypeException(this.m_className + " - The deserialized function name does not match the one described in the type descriptor.");
        }
        
    }

    @Override
    public DynamicData getParameter(String name) {
        for (DynamicMember param : this.m_parameters) {
            if (param.getName().equals(name)) {
                return param.getDynamicData();
            }
        }
        return null;
    }
    
    @Override
    public boolean equals(Object anotherObject) {
        if (anotherObject instanceof DynamicFunctionRequestImpl) {
            return this.m_parameters.equals(((DynamicFunctionRequestImpl)anotherObject).m_parameters);
        } 
        return false;
    }
    
}
