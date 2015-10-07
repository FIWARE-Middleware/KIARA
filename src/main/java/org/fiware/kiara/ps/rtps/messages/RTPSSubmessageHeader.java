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
package org.fiware.kiara.ps.rtps.messages;

import java.io.IOException;

import org.fiware.kiara.ps.rtps.messages.common.types.SubmessageFlags;
import org.fiware.kiara.ps.rtps.messages.common.types.SubmessageId;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.CDRSerializer;
import org.fiware.kiara.serialization.impl.Serializable;
import org.fiware.kiara.serialization.impl.SerializerImpl;

/**
 * This class represnets the submessahe header 
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class RTPSSubmessageHeader implements Serializable {

    /**
     * {@link SubmessageId} of the {@link RTPSSubmessage}
     */
    SubmessageId m_submessageId;
    
    /**
     * Submessage flags
     */
    SubmessageFlags m_flags;
    
    /**
     * Submessage total length
     */
    short m_submessageLength;
    
    /**
     * Larger submessage length
     */
    int m_submessageLengthLarger;
    
    /**
     * Bytes to next {@link RTPSSubmessageHeader}
     */
    short m_octectsToNextHeader;

    /**
     * Default {@link RTPSSubmessageHeader} constructor
     */
    public RTPSSubmessageHeader() {
        this.m_flags = new SubmessageFlags();
    }

    /**
     * Set the {@link SubmessageId}
     * 
     * @param submessageId The {@link SubmessageId} to be set
     */
    public void setSubmessageId(SubmessageId submessageId) {
        this.m_submessageId = submessageId;
    }

    /**
     * Get the {@link SubmessageId}
     * 
     * @return The {@link SubmessageId}
     */
    public SubmessageId getSubmessageId() {
        return this.m_submessageId;
    }

    /**
     * Set the {@link SubmessageFlags}
     * 
     * @param flags The {@link SubmessageFlags} to be set
     */
    public void setFlags(SubmessageFlags flags) {
        this.m_flags = flags;
    }

    /**
     * Get the {@link SubmessageFlags}
     * 
     * @return The {@link SubmessageFlags}
     */
    public SubmessageFlags getFlags() {
        return this.m_flags;
    }

    /**
     * Set the number of bytes until the next {@link RTPSSubmessageHeader}
     * 
     * @param octectsToNextHeader The number of bytes
     */
    public void setOctectsToNextHeader(short octectsToNextHeader) {
        this.m_octectsToNextHeader = octectsToNextHeader;
    }

    /**
     * Get the number of bytes until the next {@link RTPSSubmessageHeader}
     * 
     * @return The number of bytes
     */
    public short getOctectsToNextHeader() {
        return this.m_octectsToNextHeader;
    }

    /**
     * Serializes an {@link RTPSSubmessageHeader}
     */
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        impl.serializeByte(message, "", (byte) this.m_submessageId.getValue());
        impl.serializeByte(message, "", this.m_flags.getByteValue());
        impl.serializeUI16(message, "", this.m_octectsToNextHeader);
    }

    /**
     * Deserializes an {@link RTPSSubmessageHeader}
     */
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.m_submessageId = SubmessageId.createFromValue(impl.deserializeByte(message, ""));
        this.m_flags.setFlagValue(impl.deserializeByte(message, ""));
        ((CDRSerializer) impl).setEndianness(this.m_flags.getFlagValue(0));
        this.m_octectsToNextHeader = impl.deserializeUI16(message, "");
    }

    /**
     * This function compares two instances of {@link RTPSSubmessageHeader}
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof RTPSSubmessageHeader) {
            RTPSSubmessageHeader instance = (RTPSSubmessageHeader) other;
            boolean retVal = true;

            retVal &= this.m_submessageId == instance.m_submessageId;
            retVal &= this.m_flags.equals(instance.m_flags);
            retVal &= this.m_octectsToNextHeader == instance.m_octectsToNextHeader;
            retVal &= this.m_submessageLength == instance.m_submessageLength;
            retVal &= this.m_submessageLengthLarger == instance.m_submessageLengthLarger;

            return retVal;
        }
        return false;
    }

    /**
     * Get the serialized size
     * 
     * @return The serialized size
     */
    public int getSerializedSize() {
        return 32;
    }

}
