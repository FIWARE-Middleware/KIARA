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

import org.fiware.kiara.client.AsyncCallback;
import org.fiware.kiara.dynamic.DynamicValue;
import org.fiware.kiara.dynamic.data.DynamicData;

/**
 * This class represents a dynamic function request. This class is used to
 * create objects whose objective is to invoke functions remotely.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public interface DynamicFunctionRequest extends DynamicValue {

    /**
     * This function returns a {@link DynamicData} object stored in the
     * parameter list depending on its name.
     *
     * @param name parameter name
     * @return parameter
     * @see DynamicData
     */
    public DynamicData getParameter(String name);

    /**
     * This function returns a {@link DynamicData} object stored in the
     * parameter list at the specified position.
     *
     * @param index parameter index
     * @return parameter
     * @see DynamicData
     */
    public DynamicData getParameterAt(int index);

    /**
     * This function executes a function remotely. It serializes all the
     * necessary information and sends the request over the wire. It returns a
     * {@link DynamicFunctionResponse} with the result.
     *
     * @return dynamic function response
     * @see DynamicFunctionResponse
     */
    public DynamicFunctionResponse execute();

    /**
     * This function behaves the same way as the function execute. The only
     * difference is that it needs a callback to be executed when the response
     * arrives from the server.
     *
     * @param callback
     */
    public void executeAsync(AsyncCallback<DynamicFunctionResponse> callback);

}
