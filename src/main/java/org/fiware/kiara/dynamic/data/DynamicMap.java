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
 * This class holds a list of pairs key-value instances of {@link DynamicData}.
 * In a {@link DynamicMap}, the key values cannot be duplicated.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public interface DynamicMap extends DynamicContainer {

    /**
     * This function adds a new key-value pair using the DynamicData objets
     * introduces as parameters. It will return false if the key value already
     * exists in the map.
     *
     * @param key
     * @param value
     * @return true if operation was successful
     */
    public boolean put(DynamicData key, DynamicData value);

    /**
     * This function returns true if the DynamicMap contains at least one
     * key-value pair in which the key DynamicData object is equal to the one
     * introduced as a parameter.
     *
     * @param key
     * @return true if this map contains a mapping for the specified key
     */
    public boolean containsKey(DynamicData key);

    /**
     * This function returns true if the DynamicMap contains at least one
     * key-value pair in which the value DynamicData object is equal to the one
     * introduced as a parameter.
     *
     * @param value
     * @return true if this map maps one or more keys to the specified value
     */
    public boolean containsValue(DynamicData value);

    /**
     * This function returns a DynamicData object from a key-value pair whose
     * key is equal to the one introduced as a parameter.
     *
     * @param key
     * @return the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key
     * @see DynamicData
     */
    public DynamicData get(DynamicData key);

}
