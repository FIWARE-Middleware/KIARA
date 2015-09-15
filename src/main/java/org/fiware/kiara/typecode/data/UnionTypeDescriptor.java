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
 * Interface that represents a union data type. Unions are formed by a group of
 * members identified by their names and the labels of the discriminator to
 * which they are assigned.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public interface UnionTypeDescriptor extends MemberedTypeDescriptor {

    /**
     * This function adds a new TypeDescriptor object as a member using a
     * specific name and the labels of the discriminator.
     *
     * @param typeDescriptor member type descriptor
     * @param name member type name
     * @param isDefault true for default member
     * @param labels discriminator labels
     * @return union type descriptor
     * @see UnionTypeDescriptor
     */
    public UnionTypeDescriptor addMember(DataTypeDescriptor typeDescriptor, String name, boolean isDefault, Object... labels);

}
