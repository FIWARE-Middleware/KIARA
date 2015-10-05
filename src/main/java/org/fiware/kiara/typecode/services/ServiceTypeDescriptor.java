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
package org.fiware.kiara.typecode.services;

import java.util.List;

import org.fiware.kiara.typecode.TypeDescriptor;

/**
 * This interface represents a service, providing methods to add the
 * FunctionTypeDescriptor objects representing every function defined in a
 * specific service.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public interface ServiceTypeDescriptor extends TypeDescriptor {

    /**
     * This function returns the service name.
     *
     * @return service name
     */
    public String getName();

    /**
     * This function returns the service scoped name.
     *
     * @return service scoped name
     */
    public String getScopedName();

    /**
     * This function returns the list of {@link FunctionTypeDescriptor} objects
     * stored inside the {@link ServiceTypeDescriptor}.
     *
     * @return service function list
     * @see List
     * @see FunctionTypeDescriptor
     */
    public List<FunctionTypeDescriptor> getFunctions();

    /**
     * This function adds a {@link FunctionTypeDescriptor} to the list of
     * functions defined inside the service.
     *
     * @param functionTypeDescriptor function type
     */
    public void addFunction(FunctionTypeDescriptor functionTypeDescriptor);

}
