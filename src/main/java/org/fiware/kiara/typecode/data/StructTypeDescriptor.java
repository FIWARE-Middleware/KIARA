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

import org.fiware.kiara.typecode.TypeDescriptor;

/**
 * Interface that represents a struct data type. Structs can have multiple
 * different DataTypeDescriptor objects inside stored as members. Every struct
 * member is identified by a unique name.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public interface StructTypeDescriptor extends MemberedTypeDescriptor {

    /**
     * This function adds a new TypeDescriptor object as a member using a
     * specific name.
     *
     * @param member member type descriptor
     * @param name name of the member
     */
    public void addMember(TypeDescriptor member, String name);

    /**
     * This function returns a DataTypeDescriptor object identified by the name
     * introduced as a parameter.
     *
     * @param name member name
     * @return member type descriptor
     * @see DataTypeDescriptor
     */
    public DataTypeDescriptor getMember(String name);

}
