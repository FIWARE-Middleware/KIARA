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
import org.fiware.kiara.serialization.impl.CDRSerializer;
import org.fiware.kiara.serialization.impl.Serializable;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.ps.rtps.common.EncapsulationKind;
import org.fiware.kiara.ps.rtps.messages.RTPSSubmessageElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that represents the serialized payload in an RTPS DATA submessage. 
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class SerializedPayload extends RTPSSubmessageElement {

    /**
     * SerializedPayload length
     */
    private short m_length;
    
    /**
     * {@link ParameterList} reference representing a list of {@link Parameter} objects
     */
    private ParameterList m_parameterList;
    
    /**
     * {@link Serializable} user data
     */
    private Serializable m_appData;
    
    /**
     * {@link EncapsulationKind} of the SerializedPayload
     */
    private EncapsulationKind m_encapsulation;
    
    /**
     * {@link CDRSerializer} object to serialize specifically the user's data type
     */
    private CDRSerializer m_ownSerializer;
    
    /**
     * boolean value indicating whether this SerializedPayload contains user data or not
     */
    private boolean m_dataFlag;
    
    //private boolean m_keyFlag; // TODO Uncomment this for future releases

    /**
     * Maximum SerializedPayload size
     */
    public static final int PAYLOAD_MAX_SIZE = 64000;

    /**
     * Byte array to serialize data into
     */
    private byte[] m_buffer;

    /**
     * Log object
     */
    private static final Logger logger = LoggerFactory.getLogger(SerializedPayload.class);

    /**
     * Default constructor
     */
    public SerializedPayload() {
        this.m_length = 0;
        this.m_encapsulation = EncapsulationKind.CDR_BE;
        this.m_parameterList = null;
        this.m_appData = null;
        this.m_ownSerializer = new CDRSerializer(false);

    }

    /**
     * Get the CDRSerializer object
     * 
     * @return The CDRSerializer object
     */
    public CDRSerializer getSerializer() {
        return m_ownSerializer;
    }

    /**
     * Get the lengh of the serialized data type
     * 
     * @return The length of the serialized data type
     */
    public short getLength() {
        return this.m_length;
    }

    /**
     * Get the number of elements in the ParameterList object
     * 
     * @return The number of elements in the ParameterList
     */
    public int getParameterListLength() {
        if (this.m_parameterList != null) {
            return this.m_parameterList.getListLength();
        }
        return 0;
    }

    /**
     * Get the ParameterList object
     * 
     * @return The ParameterList object
     */
    public ParameterList getParameterList() {
        return this.m_parameterList;
    }

    /**
     * Set the SerializedPayload length
     * 
     * @param length The length to be set
     */
    public void setLength(short length) {
        this.m_length = length;
    }

    /**
     * Get the buffer
     * 
     * @return The buffer
     */
    public byte[] getBuffer() {
        return m_buffer;
    }

    /**
     * Set the buffer
     * 
     * @param buffer The buffer to be set
     */
    public void setBuffer(byte[] buffer) {
        m_buffer = buffer;
        m_length = (short) buffer.length;
    }

    /**
     * Get the Seriazable user data type
     * 
     * @return The user data type
     */
    public Serializable getData() {
        return this.m_appData;
    }

    /**
     * Set the Serializable user data type
     * 
     * @param data The Serializable data
     */
    public void setData(Serializable data) {
        this.m_appData = data;
    }

    /**
     * Set the data flag
     * 
     * @param dataFlag The data flag value to be set
     */
    public void setDataFlag(boolean dataFlag) {
        this.m_dataFlag = dataFlag;
    }

    /**
     * Adds a new Parameter in the ParameterList
     * 
     * @param param The new Parameter to be added
     */
    public void addParameter(Parameter param) {
        if (this.m_encapsulation == EncapsulationKind.PL_CDR_BE || this.m_encapsulation == EncapsulationKind.PL_CDR_LE) {
            if (this.m_parameterList == null) {
                this.m_parameterList = new ParameterList();
            }
            this.m_parameterList.addParameter(param);
            this.m_length = (short) (this.m_length + param.getSerializedSize());
        }
    }

    /**
     * Adds a ParameterList at the end of the current ParameterList
     * 
     * @param paramList The ParameterList to be added
     */
    public void addParameters(ParameterList paramList) {
        if (this.m_parameterList == null) {
            this.m_parameterList = new ParameterList();
        }
        for (Parameter param : paramList.getParameters()) {
            this.m_parameterList.addParameter(param);
        }
    }

    /**
     * Set the Serializable data
     * 
     * @param data The Serializable data to be ser
     * @param length The length
     */
    public void setData(Serializable data, short length) {
        if (this.m_encapsulation == EncapsulationKind.CDR_BE || this.m_encapsulation == EncapsulationKind.CDR_LE) {
            this.m_appData = data;
            this.m_length = (short) (this.m_length + length);
        }
    }

    /**
     * Get the EncapsulationKind
     * 
     * @return The EncapsulationKind
     */
    public EncapsulationKind getEncapsulation() {
        return this.m_encapsulation;
    }

    /**
     * Set the EncpsulationKind
     * 
     * @param encapsulation The EncapsulationKind to be set
     */
    public void setEncapsulationKind(EncapsulationKind encapsulation) {
        this.m_encapsulation = encapsulation;
    }

    /**
     * Get the serialize size of the SerializedPayload object
     */
    @Override
    public short getSerializedSize() {
        return (short) (2 + 2 + this.m_length);
    }

    /**
     * Checks the endianness
     * 
     * @return true is endiannes is BE; false otherwise
     */
    private boolean checkEndianness() {
        if (this.m_encapsulation == EncapsulationKind.PL_CDR_BE || this.m_encapsulation == EncapsulationKind.CDR_BE) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Serializes a SerializedPayload object
     */
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {

        this.m_ownSerializer.setEndianness(checkEndianness()); // true = LE, false = BE

        impl.serializeByte(message, "", (byte) 0); // Encapsulation empty octet
        impl.serializeByte(message, "", (byte) this.m_encapsulation.getValue()); // Encapsulation octet
        impl.serializeI16(message, "", (short) 0); // Encapsulation options

        if (this.m_parameterList != null) {
            this.m_parameterList.serialize(impl, message, name);
        } else if (this.m_appData != null) {
            this.m_appData.serialize(this.m_ownSerializer, message, name);
        } else if (this.m_buffer != null) {
            message.write(m_buffer, 0, getLength());
        }
        this.m_length = (short) message.getBufferLength();

    }

    /**
     * Deserializes a SerializedPayload object
     */
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {

        this.m_ownSerializer.setEndianness(checkEndianness()); // true = LE, false = BE

        message.skipBytes(1); // Encapsulation empty octet
        this.m_encapsulation = EncapsulationKind.createFromValue(impl.deserializeByte(message, "")); // Encapsulation octet
        message.skipBytes(2); // Encapsulation options

        this.m_buffer = new byte[this.m_length];
        message.readFully(this.m_buffer);
    }

    /**
     * Deserializes a SerializedPayload object
     * 
     * @throws IOException If the deserializion was wrong
     */
    public void deserializeData() throws IOException {
        if (this.m_appData == null) {
            logger.error("Type not specified in SerializedPayload object.");
            return;
        }

        CDRSerializer impl = new CDRSerializer(checkEndianness());
        BinaryInputStream message = new BinaryInputStream(this.m_buffer);
        this.m_appData.deserialize(impl, message, "");
    }
    
    /**
     * Deserializes a ParameterList object
     * 
     * @throws IOException If the deserializion was wrong
     */
    public void deserializeParameterList() throws IOException {
        if (this.m_parameterList == null) {
            this.m_parameterList = new ParameterList();
        }

        CDRSerializer impl = new CDRSerializer(checkEndianness());
        BinaryInputStream message = new BinaryInputStream(this.m_buffer);
        this.m_parameterList.deserialize(impl, message, "");
    }

    /**
     * Compares two SerializedPayload objects
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof SerializedPayload) {
            SerializedPayload obj = (SerializedPayload) other;
            boolean ret = true;
            ret &= (this.m_length == obj.m_length);
            ret &= (this.m_dataFlag == obj.m_dataFlag);
            // TODO More comparisons
            return ret;
        }
        return false;
    }

    /**
     * Copies the content of a SerializedPayloa object
     * 
     * @param serializedPayload The SerializedPayload to be copioed
     * @return true if copy process was OK; false otherwise
     */
    public boolean copy(SerializedPayload serializedPayload) {
        this.m_length = serializedPayload.getLength();
        this.m_encapsulation = serializedPayload.m_encapsulation;
        if (this.m_appData == null) {
            this.m_appData = serializedPayload.m_appData;
        }
        if (serializedPayload.m_buffer != null) {
            this.m_buffer = new byte[serializedPayload.m_buffer.length]; // NEW
            System.arraycopy(serializedPayload.m_buffer, 0, this.m_buffer, 0, serializedPayload.m_buffer.length);
        }
        if (serializedPayload.m_parameterList != null) {
            this.m_parameterList = new ParameterList();
            for (Parameter pit : serializedPayload.m_parameterList.getParameters()) {
                this.m_parameterList.addParameter(pit);
            }
        }
        return true;
    }

    /**
     * Self-sets the endianness
     */
    public void updateSerializer() {
        this.m_ownSerializer.setEndianness(checkEndianness()); // true = LE, false = BE
    }

    /**
     * Deletes all Parameter objects in the ParameterList
     */
    public void deleteParameters() {
        if (this.m_parameterList != null) {
            this.m_parameterList.deleteParams();
        }
    }



}
