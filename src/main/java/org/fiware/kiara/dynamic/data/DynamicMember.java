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
 * This class represents a dynamic member of any {@link DynamicMembered} object.
 * It is used to store the {@link DynamicData} objects inside structures,
 * unions, enumerations and exceptions.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public interface DynamicMember {

    /**
     * This function returns the member?s name.
     *
     * @return member name
     */
    public String getName();

    /**
     * This function returns the {@link DynamicData} stored inside a
     * DynamicMember object.
     *
     * @return dynamic data
     * @see DynamicData
     */
    public DynamicData getDynamicData();

    /**
     * It returns true if two {@link DynamicMember} objects are equal.
     *
     * @param anotherObject
     * @return true if passed object is equal to this
     */
    @Override
    public boolean equals(Object anotherObject);

}
