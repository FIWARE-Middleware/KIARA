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
 * This class holds the data values of a DynamicData object created from a
 * ListTypeDescriptor. A list can only have one dimension and it has a maximum
 * length. All the DynamicData objects stored inside a DynamicList must have
 * been created from the same TypeDescriptor definition.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public interface DynamicList extends DynamicContainer {

    /**
     * This function adds a DynamicData object into the list in the last
     * position or in the position specified via parameter.
     *
     * @param element
     * @return true if operation was successful
     */
    public boolean add(DynamicData element);

    /**
     * This function adds a DynamicData object into the list at the specified
     * via parameter.
     *
     * @param index
     * @param element
     * @see DynamicData
     */
    public void add(int index, DynamicData element);

    /**
     * This function returns a DynamicData object stored is a specific position
     * in the list.
     *
     * @param index
     * @return dynamic data
     * @see DynamicData
     */
    public DynamicData get(int index);

    /**
     * This function returns true if the {@link DynamicList} is empty.
     *
     * @return true if list is empty
     */
    public boolean isEmpty();

}
