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
 * Interface that represents a set data type. Sets can have non repeated objects
 * of the same data type inside.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public interface SetTypeDescriptor extends ContainerTypeDescriptor {

    /**
     * This function returns the DataTypeDescriptor object describing the
     * content type of the set.
     *
     * @return element type descriptor
     * @see DataTypeDescriptor
     */
    public DataTypeDescriptor getElementType();

    /**
     * This function sets the DataTypeDescriptor object describing the content
     * type of the set.
     *
     * @param contentType element type descriptor
     * @return true if operation was successful
     * @see DataTypeDescriptor
     */
    public boolean setElementType(DataTypeDescriptor contentType);

}
