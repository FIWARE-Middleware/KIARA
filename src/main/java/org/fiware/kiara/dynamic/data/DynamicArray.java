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
 * This class holds the data values of a DynamicData object created from an
 * {@link org.fiware.kiara.typecode.data.ArrayTypeDescriptor}. A {@link DynamicArray} contains a group of
 * {@link DynamicData} objects (all must be the same type) stored in single or
 * multi dimensional matrixes.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public interface DynamicArray extends DynamicContainer {

    /**
     * This function returns {@link DynamicData} object stored in a certain
     * position or coordinate.
     *
     * @param position
     * @return dynamic data
     * @see DynamicData
     */
    public DynamicData getElementAt(int... position);

    /**
     * This function sets a {@link DynamicData} object in a specific position
     * inside the array. If the array has multiple dimensions, the object will
     * be set in a specific coordinate.
     *
     * @param value element value
     * @param position array with positions
     * @return true if operation was successful
     */
    public boolean setElementAt(DynamicData value, int... position);

}
