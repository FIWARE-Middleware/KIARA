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

import org.fiware.kiara.ps.rtps.messages.RTPSSubmessageElement;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

/**
 * The Count class represents an integer count
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class Count extends RTPSSubmessageElement {

    /**
     * Count value
     */
    private int m_value;

    /**
     * Default constructor
     * 
     * @param value The Count value
     */
    public Count(int value) {
        this.m_value = value;
    }

    /**
     * Serializes the Count object
     */
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        impl.serializeI32(message, name, this.m_value);
    }

    /**
     * Deserializes the Count object
     */
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.m_value = impl.deserializeI32(message, name);
    }

    /**
     * Get the Count serialized size
     */
    @Override
    public short getSerializedSize() {
        return 4;
    }

    /**
     * Compares two Count objects
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Count) {
            if (this.m_value == ((Count) other).m_value) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the value of the Count class
     * 
     * @return
     */
    public int getValue() {
        return m_value;
    }

    /**
     * Set the Count value
     * 
     * @param m_value
     */
    public void setValue(int m_value) {
        this.m_value = m_value;
    }

    /**
     * Increases the Count value
     */
    public void increase() {
        this.m_value++;
    }

    /**
     * Decreases the Count value
     */
    public void decrease() {
        this.m_value--;
    }

}
