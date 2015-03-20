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

import org.fiware.kiara.dynamic.services.DynamicFunction;
import org.fiware.kiara.dynamic.data.DynamicData;
import org.fiware.kiara.dynamic.data.DynamicException;
import org.fiware.kiara.dynamic.impl.DynamicTypeImpl;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.typecode.TypeDescriptor;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*
*/
public class DynamicFunctionImpl extends DynamicTypeImpl implements DynamicFunction {
    
    private DynamicData m_returnType;
    private ArrayList<DynamicData> m_parameters;
    private ArrayList<DynamicException> m_exceptions;

    public DynamicFunctionImpl(TypeDescriptor typeDescriptor) {
        super(typeDescriptor, "DynamicFunctionImpl");
        this.m_parameters = new ArrayList<DynamicData>();
        this.m_exceptions = new ArrayList<DynamicException>();
    }
    
    public void setReturnType(DynamicData returnType) {
        this.m_returnType = returnType;
    }
    
    public DynamicData getReturnType() {
        return this.m_returnType;
    }
    
    public void addParameter(DynamicData parameter) {
        this.m_parameters.add(parameter);
    }
    
    public ArrayList<DynamicData> getParameters() {
        return this.m_parameters;
    }
    
    public void addException(DynamicException exception) {
        this.m_exceptions.add(exception);
    }
    
    public ArrayList<DynamicException> getExceptions() {
        return this.m_exceptions;
    }

    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message,
            String name) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        // TODO Auto-generated method stub
        
    }
    
}
