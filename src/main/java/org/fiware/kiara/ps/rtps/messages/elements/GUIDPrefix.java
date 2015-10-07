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
import java.nio.ByteBuffer;

import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.ps.rtps.messages.RTPSSubmessageElement;

/**
 * Structure GuidPrefix, Guid Prefix of the GUID.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class GUIDPrefix extends RTPSSubmessageElement {

    /**
     * GUIDPrefix value (it's a byte array with 12 positions)
     */
    byte[] m_value;

    /**
     * Default constructor
     */
    public GUIDPrefix() {
        m_value = new byte[12];
        for (int i=0; i < 12; ++i) {
            m_value[i] = (byte) 0;
        }
    }

    /**
     * Set the GUIDPrefix value
     * 
     * @param index The index in which the value will de stored
     * @param value The value to be stored
     */
    public void setValue(int index, byte value) {
        this.m_value[index] = value;
    }

    /**
     * Get the GUIDPrefix value
     * 
     * @return The GuidPrefix value
     */
    public byte[] getValue() {
        return m_value;
    }

    /**
     * Get the byte value in a certain position of the GUIDPrefix value
     * 
     * @param index The position of the value to be returned
     * @return The value of the GUIDPrefix in that position
     */
    public byte getValue(int index) {
        return this.m_value[index];
    }

    /**
     * Alternative {@link GUIDPrefix} constructor
     * 
     * @param value Byte array containing the value of the {@link GUIDPrefix}
     * @throws Exception If the introduced Byte array does not have a length of 12
     */
    public GUIDPrefix(byte[] value) throws Exception {
        if (value.length != 12) {
            throw new Exception();
        }

        for (int i=0; i < 12; ++i) {
            m_value[i] = value[i];
        }
    }

    /**
     * Copies the content of a GUIDPrefix object
     * 
     * @param value The GUIDPrefix to be copied
     */
    public void copy(GUIDPrefix value) {
        for (int i = 0; i < 12; ++i) {
            m_value[i] = value.m_value[i];
        }
    }

    /**
     * Serializes a GUIDPrefix object
     */
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        for (int i=0; i < 12; ++i) {
            impl.serializeByte(message, "", this.m_value[i]);
        }
    }

    /**
     * Deserializes a GUIDPrefix object
     */
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        for (int i=0; i < 12; ++i) {
            this.m_value[i] = impl.deserializeByte(message, "");
        }
    }

    /**
     * Get the GUIDPrefix serialized size
     */
    @Override
    public short getSerializedSize() {
        return 12;
    }

    /**
     * Compares two GUIDPrefix objects
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof GUIDPrefix) {
            boolean equals = true;
            for (int i=0; i < 12; ++i) {
                equals &= this.m_value[i] == ((GUIDPrefix) other).m_value[i];
            }
            return equals;
        }
        return false;
    }

    /**
     * Converts a GUIDPrefix to its String representation
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("");
        for (Byte b : this.m_value) {
            sb.append(b);
        }
        return sb.toString();
    }
    
    /**
     * Get a hash associated with the GUIDPrefix
     */
    @Override
    public int hashCode() {
        ByteBuffer wrapped = ByteBuffer.wrap(m_value);
        return wrapped.getInt();
    }

}
