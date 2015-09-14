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
package org.fiware.kiara.serialization;

/**
 * This interface provides an abstraction for serialization mechanism
 * implementations.
 * The user can creates directly a serializer using the interface
 * {@link org.fiware.kiara.Context}.
 * This {@link org.fiware.kiara.Context} interface will return the serialization mechanism
 * implementation encapsulated in this interface.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public interface Serializer {

    /**
     * Returns name of the serializer.
     *
     * @return name of the serializer.
     */
    public String getName();
}
