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
package org.fiware.kiara.client;

import org.fiware.kiara.dynamic.services.DynamicProxy;

/**
 * The connection interface manages the connection to the server.
 * It holds the required Transport objects and Serialization objects.
 * Also it can create these object automatically depending on the server
 * information.
 * The connection provides the service proxy interfaces, which will can
 * be used by the application to call remote functions.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public interface Connection {

    /**
     * This function provides a new proxy instance that user can use to call
     * remote procedures. This proxy uses the connection to send the requests
     * to the server.
     *
     * @param <T>
     * @param interfaceClass
     * @return new proxy instance
     * @throws Exception
     */
    public <T> T getServiceProxy(Class<T> interfaceClass) throws Exception;

    /**
     * This function provides a new dynamic proxy instance that user can use to
     * call remote procedures. This proxy uses the connection to send the
     * requests to the server.
     *
     * @param name
     * @return new dynamic proxy instance
     * @see DynamicProxy
     */
    public DynamicProxy getDynamicProxy(String name);

}
