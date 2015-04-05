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
import org.fiware.kiara.Kiara;

import org.fiware.kiara.dynamic.DynamicValueBuilderImpl;
import org.fiware.kiara.dynamic.impl.DynamicTypeImpl;
import org.fiware.kiara.dynamic.services.DynamicFunctionRequest;
import org.fiware.kiara.dynamic.services.DynamicFunctionResponse;
import org.fiware.kiara.dynamic.services.DynamicProxy;
import org.fiware.kiara.serialization.Serializer;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.transport.Transport;
import org.fiware.kiara.typecode.services.FunctionTypeDescriptor;
import org.fiware.kiara.typecode.services.ServiceTypeDescriptor;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*
*/
public class DynamicProxyImpl extends DynamicTypeImpl implements DynamicProxy {
    
    SerializerImpl m_serializer;
    Transport m_transport;
    
    public DynamicProxyImpl(ServiceTypeDescriptor typeDescriptor, Serializer serializer, Transport transport) {
        super(typeDescriptor, "DynamicServiceImpl");
        this.m_serializer = (SerializerImpl) serializer;
        this.m_transport = transport;
    }
    
    @Override
    public String getServiceName() {
        return ((ServiceTypeDescriptor) this.m_typeDescriptor).getScopedName();
    }

    @Override
    public DynamicFunctionRequest createFunctionRequest(String name) {

        for (FunctionTypeDescriptor func : ((ServiceTypeDescriptor) this.m_typeDescriptor).getFunctions()) {
            if (func.getName().equals(name)) {
                return Kiara.getDynamicValueBuilder().createFunctionRequest(func, this.m_serializer, this.m_transport);
            }
        }
        
        return null;
    }
    
    @Override
    public DynamicFunctionResponse createFunctionResponse(String name) {
        for (FunctionTypeDescriptor func : ((ServiceTypeDescriptor) this.m_typeDescriptor).getFunctions()) {
            if (func.getName().equals(name)) {
                return Kiara.getDynamicValueBuilder().createFunctionResponse(func);
            }
        }
        
        return null;
    }

    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        throw new UnsupportedOperationException(this.m_className + " - A service cannot be serialized.");
    }

    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        throw new UnsupportedOperationException(this.m_className + " - A service cannot be deserialized.");
    }

}
