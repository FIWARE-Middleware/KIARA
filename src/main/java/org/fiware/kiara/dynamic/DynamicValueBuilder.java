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
package org.fiware.kiara.dynamic;

import org.fiware.kiara.dynamic.data.DynamicData;
import org.fiware.kiara.dynamic.services.DynamicFunctionRequest;
import org.fiware.kiara.dynamic.services.DynamicFunctionResponse;
import org.fiware.kiara.dynamic.services.DynamicProxy;
import org.fiware.kiara.serialization.Serializer;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.transport.Transport;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.impl.FunctionTypeDescriptor;
import org.fiware.kiara.typecode.services.ServiceTypeDescriptor;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*
*/
public interface DynamicValueBuilder {
    
    public DynamicData createData(DataTypeDescriptor dataDescriptor);
    
    public DynamicFunctionRequest createFunctionRequest(FunctionTypeDescriptor functionDescriptor, Serializer serializer, Transport transport);
    
    public DynamicFunctionRequest createFunctionRequest(FunctionTypeDescriptor functionDescriptor);
    
    public DynamicFunctionResponse createFunctionResponse(FunctionTypeDescriptor functionDescriptor, Serializer serializer, Transport transport);
    
    public DynamicFunctionResponse createFunctionResponse(FunctionTypeDescriptor functionDescriptor);
    
    public DynamicProxy createService(ServiceTypeDescriptor serviceDescriptor, Serializer serializer, Transport transport);

}
