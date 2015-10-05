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
 *
 * Interface that represents a primitive data type. Primitive types include
 * boolean, byte, i16, ui16, i32, ui32, i64, ui64, float32, float64, char and
 * string.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public interface PrimitiveTypeDescriptor extends DataTypeDescriptor {

    /**
     * This function returns true if and only if the
     * {@link PrimitiveTypeDescriptor} object represents a string data type.
     *
     * @return true if this type is string
     */
    public boolean isString();

    /**
     * This function can only be used with string types. It sets the maximum
     * length value for a specific string represented by the
     * {@link PrimitiveTypeDescriptor} object.
     *
     * @param length
     * @return primitive type descriptor
     * @see PrimitiveTypeDescriptor
     */
    public PrimitiveTypeDescriptor setMaxFixedLength(int length);

    /**
     * This function returns the maximum length specified when creating the
     * {@link PrimitiveTypeDescriptor} object if it represents a string data
     * type.
     *
     * @return maximal fixed length of the string data type.
     */
    public int getMaxFixedLength();

}
