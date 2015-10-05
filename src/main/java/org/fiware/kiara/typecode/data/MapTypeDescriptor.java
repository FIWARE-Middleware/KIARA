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
 * Interface that represents a map data type. Maps can hold multiple key-object
 * pairs inside if and only if the key objects are unique.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public interface MapTypeDescriptor extends ContainerTypeDescriptor {

    /**
     * This function sets the {@link DataTypeDescriptor} object describing the
     * key type of the map.
     *
     * @param keyDescriptor key type descriptor
     * @see DataTypeDescriptor
     * @return true if operation was successful
     */
    public boolean setKeyTypeDescriptor(DataTypeDescriptor keyDescriptor);

    /**
     * This function returns the {@link DataTypeDescriptor} object describing
     * the key type of the map.
     *
     * @return key type descriptor
     * @see DataTypeDescriptor
     */
    public DataTypeDescriptor getKeyTypeDescriptor();

    /**
     * This function sets the DataTypeDescriptor object describing the value
     * type of the map.
     *
     * @param valueDescriptor value type descriptor
     * @return true if operation was successful
     * @see DataTypeDescriptor
     */
    public boolean setValueTypeDescriptor(DataTypeDescriptor valueDescriptor);

    /**
     * This function returns the {@link DataTypeDescriptor} object describing
     * the value type of the map.
     *
     * @return data type descriptor
     * @see DataTypeDescriptor
     */
    public DataTypeDescriptor getValueTypeDescriptor();

}
