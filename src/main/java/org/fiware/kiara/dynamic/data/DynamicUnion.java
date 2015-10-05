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
 * This class is used to dynamically manipulate unions described by a specific
 * {@link org.fiware.kiara.typecode.data.UnionTypeDescriptor} object. A union is
 * formed by some {@link DynamicData} objects, and the valid one is selected by
 * using a discriminator.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public interface DynamicUnion extends DynamicMembered {

    /**
     * This function sets a new discriminator.
     *
     * @param value new discriminator
     */
    public void _d(Object value);

    /**
     * This function returns the discriminator.
     *
     * @return discriminator object
     */
    public Object _d();

    /**
     * This function returns valid {@link DynamicData} value depending on the
     * selected discriminator.
     *
     * @param name member name
     * @return dynamic data
     */
    public DynamicData getMember(String name);

    /**
     * This function sets the {@link DynamicData} object received as a parameter
     * in the member whose name is the same as the one introduced (if and only
     * if the discriminator value is correct).
     *
     * @param name member name
     * @param data
     */
    public void setMember(String name, DynamicData data);

}
