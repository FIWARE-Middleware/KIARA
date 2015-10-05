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
package org.fiware.kiara.typecode.data;

/**
 * Interface that represents a member of a MemberedTypeDescriptor object. Each
 * member is identified by its name and the TypeDescriptor object that it holds.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public interface Member {

    /**
     * This function returns the member?s name.
     *
     * @return member name
     */
    public String getName();

    /**
     * This function returns a DataTypeDescriptor object stored inside the
     * member.
     *
     * @return member type descriptor
     * @see DataTypeDescriptor
     */
    public DataTypeDescriptor getTypeDescriptor();

}
