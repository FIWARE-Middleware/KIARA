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
package org.fiware.kiara.dynamic.data;

/**
 * This class is used to dynamically manipulate enumerations described by a
 * specific {@link org.fiware.kiara.typecode.data.EnumTypeDescriptor} object.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public interface DynamicEnum extends DynamicMembered {

    /**
     * This function sets the actual value of the DynamicEnum object to the one
     * specified as a parameter.
     *
     * @param value
     */
    public void set(String value);

    /**
     * This function returns the actual value of the {@link DynamicEnum} object.
     *
     * @return value of this enum
     */
    public String get();

}
