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

import java.util.List;

/**
 * Interface that represents a membered data type. Membered data types are
 * structs, enumerations, unions and exceptions.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public interface MemberedTypeDescriptor extends DataTypeDescriptor {

    /**
     * This function returns the list of member objects stored in a
     * {@link ContainerTypeDescriptor} object.
     *
     * @return list of members
     * @see java.util.List
     * @see Member
     */
    public List<Member> getMembers();

    /**
     * This function returns the name of the {@link ContainerTypeDescriptor}
     * object.
     *
     * @return name of the container type descriptor
     */
    public String getName();

}
