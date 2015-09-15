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
package org.fiware.kiara.dynamic.services;

/**
 * This class represents a dynamic object used to hold the implementation of a
 * specific function. Its process method must be defined by the user when
 * creating the object, and it will be used to register the service?s functions
 * on the server?s side.
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public interface DynamicFunctionHandler {

    /**
     * This function is the one that will be registered to be executed when a
     * client invokes remotely a function. It must be implemented by the user.
     *
     * @param request dynamic function request
     * @param response dynamic function response
     */
    public void process(DynamicFunctionRequest request, DynamicFunctionResponse response);
}
