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
import org.fiware.kiara.transport.Transport;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.services.FunctionTypeDescriptor;
import org.fiware.kiara.typecode.services.ServiceTypeDescriptor;

/**
 * This class allows the users to create new data types based on their TypeCode
 * descriptions.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public interface DynamicValueBuilder {

    /**
     * This function allows the user to create new DynamicData objects by using
     * their {@link TypeDescriptor}.
     *
     * @param dataDescriptor type descriptor
     * @return dynamic data object
     * @see DynamicData
     */
    public DynamicData createData(DataTypeDescriptor dataDescriptor);

    /**
     * This function receives a {@link FunctionTypeDescriptor} object describing
     * a function, and it generates a new {@link DynamicFunctionRequest} (which
     * inherits from {@link DynamicData}) object representing it.
     *
     * @param functionDescriptor
     * @param serializer
     * @param transport
     * @return
     */
    public DynamicFunctionRequest createFunctionRequest(FunctionTypeDescriptor functionDescriptor, Serializer serializer, Transport transport);

    /**
     * This function receives a {@link FunctionTypeDescriptor} object describing
     * a function, and it generates a new {@link DynamicFunctionResponse} (which
     * inherits from {@link DynamicData}) object representing it.
     *
     * @param functionDescriptor function type descriptor
     * @return dynamic function request object
     * @see DynamicFunctionRequest
     */
    public DynamicFunctionRequest createFunctionRequest(FunctionTypeDescriptor functionDescriptor);

    /**
     * This function receives a {@link FunctionTypeDescriptor} object describing
     * a function, and it generates a new {@link DynamicFunctionResponse} (which
     * inherits from {@link DynamicData}) object representing it.
     *
     * @param functionDescriptor function type descriptor
     * @param serializer serializer
     * @param transport transport
     * @return dynamic function response object
     * @see DynamicFunctionResponse
     */
    public DynamicFunctionResponse createFunctionResponse(FunctionTypeDescriptor functionDescriptor, Serializer serializer, Transport transport);

    /**
     * This function receives a {@link FunctionTypeDescriptor} object describing
     * a function, and it generates a new {@link DynamicFunctionResponse} (which
     * inherits from {@link DynamicData}) object representing it.
     *
     * @param functionDescriptor function type descriptor
     * @return dynamic function response object
     * @see DynamicFunctionResponse
     */
    public DynamicFunctionResponse createFunctionResponse(FunctionTypeDescriptor functionDescriptor);

    /**
     * This function receives a {@link ServiceTypeDescriptor} object describing
     * a function, and it creates a new {@link DynamicService} object
     * representing it.
     *
     * @param serviceDescriptor service type descriptor
     * @param serializer serializer
     * @param transport transport
     * @return dynamic proxy object
     */
    public DynamicProxy createService(ServiceTypeDescriptor serviceDescriptor, Serializer serializer, Transport transport);

}
