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
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.CDRSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that represents an RTPS Message. It is formed by an {@link RTPSMessageHeader}
 * and a list of {@link RTPSSubmessage} objects.
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class RTPSMessage {

    /**
     * Header size
     */
    public static final short RTPS_MESSAGE_HEADER_SIZE = 20;
    /**
     * Default message size
     */
    public static final short RTPSMESSAGE_DEFAULT_SIZE = 10500;
    /**
     * Default payload size
     */
    public static final short RTPSMESSAGE_COMMON_RTPS_PAYLOAD_SIZE = 500;
    /**
     * Number of bytes of the InlineQoS field
     */
    public static final short OCTETSTOINLINEQOS_DATASUBMSG = 16;
    /**
     * Number of bytes of the OctectsToInlineQos field
     */
    public static final short DATA_EXTRA_INLINEQOS_SIZE = 4;
    /**
     * Number of bytes of the data encoding field
     */
    public static final short DATA_EXTRA_ENCODING_SIZE = 4;

    /**
     * RTPS Message Header
     */
    private RTPSMessageHeader m_header;

    /**
     * Liste of RTPS Submessages
     */
    private ArrayList<RTPSSubmessage> m_submessages;

    /**
     * Buffer to serialize the RTPS message into
     */
    private byte[] m_buffer;
    
    /**
     * Writing output object
     */
    private BinaryOutputStream m_bos;
    
    /**
     * Reading input object
     */
    private BinaryInputStream m_bis;
    
    /**
     * Serializer to be used when serializing and deserializing the {@link RTPSMessage}
     */
    private CDRSerializer m_ser;
    
    /**
     * Endiannes of the message
     */
    private RTPSEndian m_endian;
    
    /**
     * Maximum message size
     */
    private int m_maxSize;
    
    /**
     * Initial {@link RTPSSubmessage} position in the buffer
     */
    private int initSubMsgPosition = -1;
    
    /**
     * Logging object
     */
    private static final Logger logger = LoggerFactory.getLogger(RTPSMessage.class);

    /**
     * {@link RTPSMessage} constructor
     * 
     * @param payloadSize Initial payload size
     * @param endian {@link RTPSEndian} to be used
     */
    public RTPSMessage(int payloadSize, RTPSEndian endian) { // TODO Endian should be chosen from the user's side
        this.m_submessages = new ArrayList<RTPSSubmessage>();
        this.m_endian = endian; 
        this.m_buffer = new byte[payloadSize];
        this.m_bos = new BinaryOutputStream(payloadSize);
        this.m_bis = new BinaryInputStream(m_buffer);
        this.m_maxSize = payloadSize;
        this.m_ser = new CDRSerializer((this.m_endian == RTPSEndian.LITTLE_ENDIAN) ? true : false);
    }

    /**
     * Set the {@link RTPSMessageHeader}
     * @param header The {@link RTPSMessageHeader}
     * @return true on success; false otherwise
     */
    public boolean setHeader(RTPSMessageHeader header) {
        if (this.m_header != null) {
            return false;
        }
        this.m_header = header;
        return true;
    }

    /**
     * Serializes an {@link RTPSMessage} into its contained buffer
     */
    public void serialize() {
        this.m_buffer = this.m_bos.toByteArray();
        this.m_bis.setBuffer(this.m_buffer);
    }
    
    /**
     * Checks if some padding has to be added at the end of an {@link RTPSSubmessage}
     * 
     * @param isData Indicates whether the {@link RTPSSubmessage} is of type DATA
     */
    public void checkPadding(boolean isData) {
        try {
            int finalPos = this.m_bos.getPosition();
            short size = (short) (finalPos - (initSubMsgPosition + 4));

            if ((size % 4 != 0)) {
                short diff = (short) (4 - (size % 4));
                size = (short) (size + diff);
                this.m_ser.addPadding(this.m_bos, diff);
            } else if (isData && size < 24) {
                short diff = (short) (24 - size);
                size = 24;
                this.m_ser.addPadding(this.m_bos, diff); 
            }

            byte newBuffer[] = new byte[2];
            BinaryOutputStream newBos = new BinaryOutputStream();
            newBos.setBuffer(newBuffer);

            this.m_ser.serializeI16(newBos, "", size);
            System.arraycopy(newBuffer, 0, this.m_bos.getBuffer(), initSubMsgPosition + 2, 2);
            this.m_submessages.get(this.m_submessages.size()-1).getSubmessageHeader().setOctectsToNextHeader(size);

        } catch (IOException e) {
            logger.error(e.getStackTrace().toString());
        }
    }
    
    /**
     * Compares two {@link RTPSMessage} objects
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof RTPSMessage) {
            RTPSMessage instance = (RTPSMessage) other;
            boolean retVal = true;
            if (this.m_header != null) {
                retVal &= this.m_header.equals(instance.m_header);
            }
            retVal &= this.m_submessages.equals(instance.m_submessages);

            return retVal;
        }
        return false;
    }

    /**
     * Get the {@link CDRSerializer} attribute
     * 
     * @return The {@link CDRSerializer} attribute
     */
    public CDRSerializer getSerializer() {
        return this.m_ser;
    }

    /**
     * Get the {@link BinaryInputStream} attribute
     * 
     * @return The {@link BinaryInputStream} attribute
     */
    public BinaryInputStream getBinaryInputStream() {
        return this.m_bis;
    }
    
    /**
     * Get the {@link BinaryOutputStream} attribute
     * 
     * @return The {@link BinaryOutputStream} attribute
     */
    public BinaryOutputStream getBinaryOutputStream() {
        return this.m_bos;
    }

    /**
     * Initializes the {@link BinaryOutputStream}
     */
    public void initBinaryOutputStream() {
        this.m_bis.setBuffer(this.m_buffer);
    }

    /**
     * Get the buffer
     * 
     * @return The byte buffer
     */
    public byte[] getBuffer() {
        return this.m_buffer;
    }

    /**
     * Set the byte buffer
     * 
     * @param buffer The buffer to be set
     */
    public void setBuffer(byte[] buffer) {
        this.m_buffer = buffer;
    }

    /**
     * Set the byte buffer
     * 
     * @param buffer The buffer to be set
     * @param payloadSize The buffer payload size to be set
     */
    public void setBuffer(byte[] buffer, int payloadSize) {
        this.m_buffer = new byte[payloadSize];
        System.arraycopy(buffer, 0, this.m_buffer, 0, payloadSize);
    }

    /**
     * Get the buffer length
     * 
     * @return The buffer length
     */
    public int getSize() {
        if (this.m_buffer != null) {
            return this.m_buffer.length;
        }
        return -1;
    }

    /**
     * Get the selected {@link RTPSEndian}
     * 
     * @return The selected {@link RTPSEndian}
     */
    public RTPSEndian getEndiannes() {
        return this.m_endian;
    }

    /**
     * Set the {@link RTPSEndian}
     * 
     * @param endian The {@link RTPSEndian} to be set
     */
    public void setEndiannes(RTPSEndian endian) {
        if (this.m_endian != endian) {
            this.m_endian = endian;
            this.m_ser = new CDRSerializer((this.m_endian == RTPSEndian.LITTLE_ENDIAN) ? true : false);
        }
    }

    /**
     * Adds a new {@link RTPSSubmessage} at the end of the {@link RTPSSubmessage} list
     * 
     * @param submessage The {@link RTPSSubmessage} to be added
     */
    public void addSubmessage(RTPSSubmessage submessage) {
        this.m_submessages.add(submessage);
        initSubMsgPosition = this.m_bos.getPosition();
    }

    /**
     * Clears the {@link RTPSSubmessage} list
     */
    public void clearSubmessages() {
        this.m_submessages.clear();
    }
    
    /**
     * Get the maximum size of the {@link RTPSMessage}
     * 
     * @return The {@link RTPSMessage} maximum size
     */
    public int getMaxSize() {
        return this.m_maxSize;
    }

}
