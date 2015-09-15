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
 * This class allows the users to manipulate {@link DynamicData} objects made
 * from {@link PrimitiveTypeDescriptor} objects.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public interface DynamicPrimitive extends DynamicData {

    /**
     * This function sets the inner value of a {@link DynamicPrimitive} object
     * according to the {@link TypeDescriptor} specified when creating it.
     *
     * @param value
     * @return
     */
    public boolean set(Object value);

    /**
     * This function returns the value of a {@link DynamicPrimitive} object.
     *
     * @return value of dynamic primitive
     */
    public Object get();

    /**
     * This function sets the inner value of a {@link DynamicPrimitive} object
     * according to the {@link TypeDescriptor} specified when creating it.
     *
     * @param value dynamic data
     * @return true if operation was successful
     */
    public boolean set(DynamicData value);

}
