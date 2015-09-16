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
package org.fiware.kiara.dynamic.services;

import org.fiware.kiara.dynamic.DynamicValue;

/**
 * This class represents a proxy than can be dynamically used to create an
 * instance of {@link DynamicFunctionRequest} or a
 * {@link DynamicFunctionResponse} depending if the user wants an object to
 * execute a remote call or to store the result.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public interface DynamicProxy extends DynamicValue {

    /**
     * This function returns the service name.
     *
     * @return service name
     */
    public String getServiceName();

    /**
     * This function creates a new object instance of
     * {@link DynamicFunctionRequest} according to the
     * {@link org.fiware.kiara.typecode.services.FunctionTypeDescriptor} that
     * was used to describe it.
     *
     * @param name request function name
     * @return dynamic function request
     * @see DynamicFunctionRequest
     */
    public DynamicFunctionRequest createFunctionRequest(String name);

    /**
     * This function creates a new object instance of
     * {@link DynamicFunctionResponse} according to the
     * {@link org.fiware.kiara.typecode.services.FunctionTypeDescriptor} that
     * was used to describe it.
     *
     * @param name response function name
     * @return dynamic function response
     * @see DynamicFunctionResponse
     */
    public DynamicFunctionResponse createFunctionResponse(String name);

}
