/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2015 Proyectos y Sistemas de Mantenimiento S.L. (eProsima)
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
package org.fiware.kiara.ps.topic;

import java.io.IOException;

import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.Serializable;
import org.fiware.kiara.serialization.impl.SerializerImpl;

/**
 * Interface that provides a function to be overwriten by the KEYED types
 * so that the KEY can be serialized 
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public interface KeyedType {
    
    /**
     * Serializes the attributes marked as KEY values
     * 
     * @param impl {@link SerializerImpl} object implementing the interface {@link Serializable}
     * @param message The {@link BinaryOutputStream} to serialize the key into
     * @param name The name of the data
     * @throws IOException If something goes wrong while serializing the key
     */
    public void serializeKey(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException;

}
