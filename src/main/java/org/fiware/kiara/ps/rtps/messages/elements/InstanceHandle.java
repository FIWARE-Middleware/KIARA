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
 * Struct InstanceHandle, used to contain the key for WITH_KEY topics.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class InstanceHandle extends RTPSSubmessageElement {

    /**
     * Value (it's a byte array with 16 positions)
     */
    private byte[] m_value;

    /**
     * Default constructor
     */
    public InstanceHandle() {
        this.m_value = new byte[16];
        for (int i=0; i < 16; ++i) {
            this.m_value[i] = (byte) 0;
        }
    }

    public InstanceHandle(GUID guid) {
        for (int i=0; i < 16; ++i) {
            if (i < 12) {
                this.m_value[i] = guid.getGUIDPrefix().getValue(i);
            } else {
                this.m_value[i] = guid.getEntityId().getValue(i-12);
            }
        }
    }

    /**
     * Set the InstanceHandle value in a specific position
     * 
     * @param index The position in which the value will be stored
     * @param value The value to be stored
     */
    public void setValue(int index, byte value) {
        this.m_value[index] = value;
    }

    /**
     * Get the InstanceHandle value in a specific position
     * 
     * @param index The position
     * @return The value in the specified position
     */
    public byte getValue(int index) {
        return this.m_value[index];
    }

    /**
     * Get the InstanceHandle serialized size
     */
    @Override
    public short getSerializedSize() {
        return (short) 16;
    }

    /**
     * Serializes an InstanceHandle object
     */
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        for (int i=0; i < 16; ++i) {
            impl.serializeByte(message, "", this.m_value[i]);
        }
    }

    /**
     * Deserializes an InstanceHandle object
     */
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message,
            String name) throws IOException {
        for (int i=0; i < 16; ++i) {
            this.m_value[i] = impl.deserializeByte(message, "");
        }
    }

    /**
     * Compares two InstanceHanle objects
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof InstanceHandle) {
            boolean equals = true;
            if (this.m_value.length != ((InstanceHandle) other).m_value.length) {
                return false;
            }
            for (int i=0; i < this.m_value.length; ++i) {
                equals &= this.m_value[i] == ((InstanceHandle) other).m_value[i];
            }
            return equals;
        }
        return false;
    }

    /**
     * Set the GUID of the InstanceHandle (the first 12 bytes)
     * 
     * @param guid The GUID to be set
     */
    public void setGuid(GUID guid) {
        for (int i = 0; i < 16; i++) {
            if (i<12)
                m_value[i] = guid.getGUIDPrefix().getValue(i);
            else
                m_value[i] = guid.getEntityId().getValue(i-12);
        }
    }

    /**
     * Copies the content of an InstanceHandle object
     * 
     * @param ihandle The InstanceHandle to be copied
     */
    public void copy(InstanceHandle ihandle) {
        System.arraycopy(ihandle.m_value, 0, m_value, 0, 16);
    }

    /**
     * Indicates whether the InstanceHandle has been defined or not
     * 
     * @return true if the InstanceHandle has been defined; false otherwise
     */
    public boolean isDefined() {
        for (Byte b : this.m_value) {
            if (b != 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Extracts the GUID from the InstanceHandle value
     * 
     * @return The GUID contained in the InstanceHandle
     */
    public GUID toGUID() {
        GUID guid = new GUID();
        for (byte i = 0; i < 16; ++i) {
            if (i < 12) {
                guid.getGUIDPrefix().setValue(i, this.m_value[i]);
            } else {
                guid.getEntityId().setValue(i, this.m_value[i]);
            }
        }
        return guid;
    }

}
