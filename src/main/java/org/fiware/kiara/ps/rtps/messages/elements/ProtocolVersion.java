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
 * Class representing the RTPS protocol version. It's a 
 * Submessage element composed by a major and a minor value.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class ProtocolVersion extends RTPSSubmessageElement {

    /**
     * Major value
     */
    public byte m_major;
    
    /**
     * Minor value
     */
    public byte m_minor;

    /**
     * Default constructor
     */
    public ProtocolVersion() {
        m_major = (byte) 2;
        m_minor = (byte) 1;
    }

    /**
     * Alternative {@link ProtocolVersion} constructor
     * 
     * @param major Mayor byte value
     * @param minor Minor byte value
     */
    public ProtocolVersion(byte major, byte minor) {
        m_major = major;
        m_minor = minor;
    }

    /**
     * Serializes a ProtocolVersion object
     */
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        impl.serializeByte(message, "", this.m_major);
        impl.serializeByte(message, "", this.m_minor);
    }

    /**
     * Deserializes a ProtocolVersion object
     */
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.m_major = impl.deserializeByte(message, "");
        this.m_minor = impl.deserializeByte(message, "");
    }

    /**
     * Get the ProtocolVersion serialized size
     */
    @Override
    public short getSerializedSize() {
        return 2;
    }

    /**
     * Get the major value
     * 
     * @return The major value
     */
    public byte getMajor() {
        return this.m_major;
    }

    /**
     * Get the minor value
     * 
     * @return The minor value
     */
    public byte getMinor() {
        return this.m_minor;
    }

    /**
     * Compares two ProtocolVersion values
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof ProtocolVersion) {
            return this.m_major == ((ProtocolVersion) other).m_major && this.m_minor == ((ProtocolVersion) other).m_minor;
        } 
        return false;
    }

    /**
     * Checks if a ProtocolVersion object is lower than other
     * 
     * @param other he other ProtocolVersion to compare
     * @return true if the current ProtocolVersion object is lower than the 
     * other; false otherwise
     */
    public boolean isLowerThan(ProtocolVersion other) {
        if (this.m_major < other.m_major) {
            return true;
        } else if (this.m_major > other.m_major) {
            return false;
        } else {
            if (this.m_minor < other.m_minor) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Checks if a ProtocolVersion object is lower than or equal to other
     * 
     * @param other he other ProtocolVersion to compare
     * @return true if the current ProtocolVersion object is lower than or 
     * equal to the other; false otherwise
     */
    public boolean isLowerOrEqualThan(ProtocolVersion other) {
        if (this.m_major < other.m_major) {
            return true;
        } else if (this.m_major > other.m_major) {
            return false;
        } else {
            if (this.m_minor <= other.m_minor) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Checks if a ProtocolVersion object is greater than other
     * 
     * @param other he other ProtocolVersion to compare
     * @return true if the current ProtocolVersion object is greater than 
     * the other; false otherwise
     */
    public boolean isGreaterThan(ProtocolVersion other) {
        if (this.m_major > other.m_major) {
            return true;
        } else if (this.m_major < other.m_major) {
            return false;
        } else {
            if (this.m_minor > other.m_minor) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Checks if a ProtocolVersion object is greater than or equal to other
     * 
     * @param other he other ProtocolVersion to compare
     * @return true if the current ProtocolVersion object is greater then or 
     * equal to the other; false otherwise
     */
    public boolean isGreaterOrEqualThan(ProtocolVersion other) {
        if (this.m_major > other.m_major) {
            return true;
        } else if (this.m_major < other.m_major) {
            return false;
        } else {
            if (this.m_minor >= other.m_minor) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Copies the content of a ProtocolVersion object
     * 
     * @param protocolVersion The ProtocolVersion to be copied
     */
    public void copy(ProtocolVersion protocolVersion) {
        this.m_minor = protocolVersion.m_minor;
        this.m_major = protocolVersion.m_major;
    }

}
