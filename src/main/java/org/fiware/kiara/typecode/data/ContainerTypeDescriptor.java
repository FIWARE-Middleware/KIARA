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
 * Interface that represents a container data type. Container data types are
 * arrays, lists, maps and sets.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public interface ContainerTypeDescriptor extends DataTypeDescriptor {

    public static final int UNBOUNDED = -1;

    /**
     * This function sets the maximum size of a container data type.
     *
     * @param length maximum size of this container
     */
    public void setMaxSize(int length);

    /**
     * This function returns the maximum size of a container data type.
     *
     * @return maximum size of this container
     */
    public int getMaxSize();

}
