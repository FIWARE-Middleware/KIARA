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
package org.fiware.kiara.ps.rtps.messages.elements;

import java.io.IOException;

import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.ps.rtps.messages.RTPSSubmessageElement;

/**
 * Class representing the Pad submessage of an RTPS message. It
 * contains the lenght of the padding to be added.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class Pad extends RTPSSubmessageElement {

    /**
     * Length of the padding
     */
    private short m_length;

    /**
     * Default constructor
     * 
     * @param length The lenght of the padding
     */
    public Pad(short length) {
        this.m_length = length;
    }

    /**
     * Serializes a Pad object
     */
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        for (int i=0; i < this.m_length; ++i) {
            impl.serializeByte(message, name, (byte) 0);
        }
    }

    /**
     * Deserializes a Pad object
     */
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        for (int i=0; i < this.m_length; ++i) {
            impl.deserializeByte(message, name);
        }
    }

    /**
     * Get the Pad serialized size
     */
    @Override
    public short getSerializedSize() {
        return this.m_length;
    }

    /**
     * Compares two Pad objects
     */
    @Override
    public boolean equals(Object other) {
        return true;
    }



}
