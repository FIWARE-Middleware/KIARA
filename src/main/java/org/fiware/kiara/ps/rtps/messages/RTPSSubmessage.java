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
import java.util.ArrayList;

import org.fiware.kiara.ps.rtps.messages.common.types.RTPSEndian;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.CDRSerializer;

/**
 * This class represents an RTPS submessage. It is fored by a submessage 
 * header and a list of submessage elements
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class RTPSSubmessage {

    /**
     * {@link RTPSSubmessageHeader} containing information about the submessage
     */
    protected RTPSSubmessageHeader m_submessageHeader;
    
    /**
     * {@link CDRSerializer} used to serialize and deserialize the submessage contents
     */
    protected CDRSerializer m_serializer;
    
    /**
     * List of {@link RTPSSubmessageElement} objects
     */
    protected ArrayList<RTPSSubmessageElement> m_submessageElements;
    
    /**
     * Endiannes of the submessage
     */
    private RTPSEndian m_endian = RTPSEndian.BIG_ENDIAN; // false BE, true LE. DEFAULT VALUE = BIG_ENDIAN

    /**
     * Default {@link RTPSSubmessage} constructor
     */
    public RTPSSubmessage() {
        this.m_submessageElements = new ArrayList<RTPSSubmessageElement>();
    }

    /**
     * Serializes the {@link RTPSSubmessage}
     * 
     * @param ser {@link CDRSerializer} reference
     * @param bos {@link BinaryOutputStream} to serialize the data into
     */
    public void serialize(CDRSerializer ser, BinaryOutputStream bos) {
        if (this.m_serializer == null) {
            this.initSerializer();
        }
        try {
            this.m_submessageHeader.serialize(ser, bos, "");
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try {
            for (RTPSSubmessageElement elm : this.m_submessageElements) {
                elm.serialize(this.m_serializer, bos, "");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Compares two instances of {@link RTPSSubmessage}
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof RTPSSubmessage) {
            RTPSSubmessage instance = (RTPSSubmessage) other;
            boolean retVal = true;

            retVal &= this.m_submessageHeader.equals(instance.m_submessageHeader);
            retVal &= this.m_submessageElements.equals(instance.m_submessageElements);

            return retVal;
        }
        return false;
    }

    /**
     * Get the Submessage length
     * 
     * @return The sub,essage length
     */
    public short getLength() {
        short retVal = 0;

        for (RTPSSubmessageElement elm : this.m_submessageElements) {
            retVal = (short) (retVal + elm.getSerializedSize());
        }

        return retVal;
    }

    /**
     * Get the submessage serialized size
     * 
     * @return The submessage serialized size
     */
    public int getSerializedSize() {
        int totalSize = 0;
        totalSize += this.m_submessageHeader.getSerializedSize();
        for (RTPSSubmessageElement element : this.m_submessageElements) {
            totalSize += element.getSerializedSize();
        }
        return totalSize;
    }
    
    /**
     * Set the submessage {@link RTPSEndian}
     * 
     * @param endian The {@link RTPSEndian}
     */
    public void setSubmessageEndian(RTPSEndian endian) {
        this.m_endian = endian;
    }

    /**
     * Get the submessage {@link RTPSEndian}
     * 
     * @return The submessage {@link RTPSEndian}
     */
    public RTPSEndian getSubmessageEndian() {
        return this.m_endian;
    }

    /**
     * Initializes the serializer
     */
    public void initSerializer() {
        this.m_serializer = new CDRSerializer(this.m_endian == RTPSEndian.BIG_ENDIAN ? false : true);
    }

    /**
     * Set the {@link RTPSSubmessageHeader}
     * 
     * @param submessageHeader The {@link RTPSSubmessageHeader} to be set
     */
    public void setSubmessageHeader(RTPSSubmessageHeader submessageHeader) {
        this.m_submessageHeader = submessageHeader;
    }

    /**
     * Get the {@link RTPSSubmessageHeader}
     * 
     * @return The {@link RTPSSubmessageHeader}
     */
    public RTPSSubmessageHeader getSubmessageHeader() {
        return this.m_submessageHeader;
    }

    /**
     * Adds a new {@link RTPSSubmessageElement} to the {@link RTPSSubmessage}
     * 
     * @param element The {@link RTPSSubmessageElement} to be added
     */
    public void addSubmessageElement(RTPSSubmessageElement element) {
        this.m_submessageElements.add(element);
    }

    

}
