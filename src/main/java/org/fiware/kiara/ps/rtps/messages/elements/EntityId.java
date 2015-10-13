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

import org.fiware.kiara.ps.rtps.messages.RTPSSubmessageElement;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

/**
 * Class that represents the EntityId, part of GUID.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class EntityId extends RTPSSubmessageElement {

    /**
     * Enumeration representing the kind of EntityId
     * 
     * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
     *
     */
    public enum EntityIdEnum {

        /**
         * Unknown entity
         */
        ENTITYID_UNKNOWN(0x00000000),
        /**
         * Participant
         */
        ENTITYID_RTPSPARTICIPANT(0x000001C1),
        /**
         * Builtin Topic Writer (Metatraffic)
         */
        ENTITYID_SEDP_BUILTIN_TOPIC_WRITER(0x000002C2),
        /**
         * Builtin Topic Reader (Metatraffic)
         */
        ENTITYID_SEDP_BUILTIN_TOPIC_READER(0x000002C7),
        /**
         * Builtin Publications Writer (SEDP)
         */
        ENTITYID_SEDP_BUILTIN_PUBLICATIONS_WRITER(0x000003C2),
        /**
         * Builtin Publications Reader (SEDP)
         */
        ENTITYID_SEDP_BUILTIN_PUBLICATIONS_READER(0x000003C7),
        /**
         * Builtin Subscriptions Writer (SEDP)
         */
        ENTITYID_SEDP_BUILTIN_SUBSCRIPTIONS_WRITER(0x000004C2),
        /**
         * Builtin Subscriptions Reader (SEDP)
         */
        ENTITYID_SEDP_BUILTIN_SUBSCRIPTIONS_READER(0x000004C7),
        /**
         * Builtin Participant Writer (SPDP)
         */
        ENTITYID_SPDP_BUILTIN_RTPSPARTICIPANT_WRITER(0x000100C2),
        /**
         * Builtin Participant Reader (SPDP)
         */
        ENTITYID_SPDP_BUILTIN_RTPSPARTICIPANT_READER(0x000100C7),
        /**
         * Builtin Message Writer (P2P)
         */
        ENTITYID_P2P_BUILTIN_RTPSPARTICIPANT_MESSAGE_WRITER(0x000200C2),
        /**
         * Builtin Message Reader (P2P)
         */
        ENTITYID_P2P_BUILTIN_RTPSPARTICIPANT_MESSAGE_READER(0x000200C7);

        /**
         * Byte array representing the value of the EntityId
         */
        private final byte[] m_value;
        
        /**
         * Integer value of the EntityId
         */
        private final int m_intValue;

        /**
         * {@link EntityId} constructor
         * 
         * @param value The enumeration identifier
         */
        EntityIdEnum(int value) {
            this.m_value = ByteBuffer.allocate(4).putInt(value).array();
            this.m_intValue = value;
        }

        /**
         * Get the {@link EntityIdEnum} value as a byte array
         * 
         * @return The byte array value of the {@link EntityIdEnum}
         */
        public byte[] getValue() {
            return this.m_value;
        }

        /**
         * Get the {@link EntityIdEnum} as an integer value
         * 
         * @return The integer value of the {@link EntityIdEnum}
         */
        public int getIntValue() {
            return this.m_intValue;
        }
    }

    /**
     * Byte array representing the EntityId
     */
    byte m_value[];

    /**
     * Default {@link EntityId} constructor (creates an UNKNOWN {@link EntityId})
     */
    public EntityId() {
        this.m_value = new byte[4];
        this.m_value[0] = 0x00;
        this.m_value[1] = 0x00;
        this.m_value[2] = 0x00;
        this.m_value[3] = 0x00;
    }

    /**
     * Alternative {@link EntityId} constructor (creates a different {@link EntityId} depending
     * on the introduced parameter)
     * 
     * @param value Indicates the type of Entity
     */
    public EntityId(int value) {
        this.m_value = ByteBuffer.allocate(4).putInt(value).array();
    }

    /**
     * Alternative {@link EntityId} copy constructor (creates a different {@link EntityId} depending
     * on the introduced {@link EntityId})
     * 
     * @param value {@link EntityId} to be copied
     */
    public EntityId(EntityIdEnum value) {
        this.m_value = value.getValue().clone();
    }

    /**
     * Get the value of the unknown attribute
     * 
     * @return true if the EntityId value is ENTITYID_UNKNOWN; false otherwise
     */
    public boolean isUnknown() {
        return equals(new EntityId(EntityIdEnum.ENTITYID_UNKNOWN));
    }

    /**
     * Get the value of the unknown attribute
     * 
     * @return true if the EntityId value is ENTITYID_SPDP_BUILTIN_RTPSPARTICIPANT_READER; false otherwise
     */
    public boolean isSPDPReader() {
        return equals(new EntityId(EntityIdEnum.ENTITYID_SPDP_BUILTIN_RTPSPARTICIPANT_READER));
    }

    /**
     * Get the value of the unknown attribute
     * 
     * @return true if the EntityId value is ENTITYID_SPDP_BUILTIN_RTPSPARTICIPANT_WRITER; false otherwise
     */
    public boolean isSPDPWriter() {
        return equals(new EntityId(EntityIdEnum.ENTITYID_SPDP_BUILTIN_RTPSPARTICIPANT_WRITER));
    }

    /**
     * Get the value of the unknown attribute
     * 
     * @return true if the EntityId value is ENTITYID_SEDP_BUILTIN_PUBLICATIONS_WRITER; false otherwise
     */
    public boolean isSEDPPubWriter() {
        return equals(new EntityId(EntityIdEnum.ENTITYID_SEDP_BUILTIN_PUBLICATIONS_WRITER));
    }

    /**
     * Get the value of the unknown attribute
     * 
     * @return true if the EntityId value is ENTITYID_SEDP_BUILTIN_PUBLICATIONS_READER; false otherwise
     */
    public boolean isSEDPPubReader() {
        return equals(new EntityId(EntityIdEnum.ENTITYID_SEDP_BUILTIN_PUBLICATIONS_READER));
    }

    /**
     * Get the value of the unknown attribute
     * 
     * @return true if the EntityId value is ENTITYID_SEDP_BUILTIN_SUBSCRIPTIONS_WRITER; false otherwise
     */
    public boolean isSEDPSubWriter() {
        return equals(new EntityId(EntityIdEnum.ENTITYID_SEDP_BUILTIN_SUBSCRIPTIONS_WRITER));
    }

    /**
     * Get the value of the unknown attribute
     * 
     * @return true if the EntityId value is ENTITYID_SEDP_BUILTIN_SUBSCRIPTIONS_READER; false otherwise
     */
    public boolean isSEDPSubReader() {
        return equals(new EntityId(EntityIdEnum.ENTITYID_SEDP_BUILTIN_SUBSCRIPTIONS_READER));
    }

    /**
     * Get the value of the unknown attribute
     * 
     * @return true if the EntityId value is ENTITYID_RTPSPARTICIPANT; false otherwise
     */
    public boolean isRTPSParticipant() {
        return equals(new EntityId(EntityIdEnum.ENTITYID_RTPSPARTICIPANT));
    }

    /**
     * Get the value of the unknown attribute
     * 
     * @return true if the EntityId value is ENTITYID_P2P_BUILTIN_RTPSPARTICIPANT_MESSAGE_WRITER; false otherwise
     */
    public boolean isWriterLiveliness() {
        return equals(new EntityId(EntityIdEnum.ENTITYID_P2P_BUILTIN_RTPSPARTICIPANT_MESSAGE_WRITER));
    }

    /**
     * Get the value of the unknown attribute
     * 
     * @return true if the EntityId value is ENTITYID_P2P_BUILTIN_RTPSPARTICIPANT_MESSAGE_READER; false otherwise
     */
    public boolean isReaderLiveliness() {
        return equals(new EntityId(EntityIdEnum.ENTITYID_P2P_BUILTIN_RTPSPARTICIPANT_MESSAGE_READER));
    }

    /**
     * Get the a new EntityId whose value is ENTITYID_UNKNOWN
     * 
     * @return The new EntityId object
     */
    public static EntityId createUnknown() {
        return new EntityId(EntityIdEnum.ENTITYID_UNKNOWN);
    }

    /**
     * Get the a new EntityId whose value is ENTITYID_SPDP_BUILTIN_RTPSPARTICIPANT_READER
     * 
     * @return The new EntityId object
     */
    public static EntityId createSPDPReader() {
        return new EntityId(EntityIdEnum.ENTITYID_SPDP_BUILTIN_RTPSPARTICIPANT_READER);
    }

    /**
     * Get the a new EntityId whose value is ENTITYID_SPDP_BUILTIN_RTPSPARTICIPANT_WRITER
     * 
     * @return The new EntityId object
     */
    public static EntityId createSPDPWriter() {
        return new EntityId(EntityIdEnum.ENTITYID_SPDP_BUILTIN_RTPSPARTICIPANT_WRITER);
    }

    /**
     * Get the a new EntityId whose value is ENTITYID_SEDP_BUILTIN_PUBLICATIONS_WRITER
     * 
     * @return The new EntityId object
     */
    public static EntityId createSEDPPubWriter() {
        return new EntityId(EntityIdEnum.ENTITYID_SEDP_BUILTIN_PUBLICATIONS_WRITER);
    }

    /**
     * Get the a new EntityId whose value is ENTITYID_SEDP_BUILTIN_PUBLICATIONS_READER
     * 
     * @return The new EntityId object
     */
    public static EntityId createSEDPPubReader() {
        return new EntityId(EntityIdEnum.ENTITYID_SEDP_BUILTIN_PUBLICATIONS_READER);
    }

    /**
     * Get the a new EntityId whose value is ENTITYID_SEDP_BUILTIN_SUBSCRIPTIONS_WRITER
     * 
     * @return The new EntityId object
     */
    public static EntityId createSEDPSubWriter() {
        return new EntityId(EntityIdEnum.ENTITYID_SEDP_BUILTIN_SUBSCRIPTIONS_WRITER);
    }

    /**
     * Get the a new EntityId whose value is ENTITYID_SEDP_BUILTIN_SUBSCRIPTIONS_READER
     * 
     * @return The new EntityId object
     */
    public static EntityId createSEDPSubReader() {
        return new EntityId(EntityIdEnum.ENTITYID_SEDP_BUILTIN_SUBSCRIPTIONS_READER);
    }

    /**
     * Get the a new EntityId whose value is ENTITYID_RTPSPARTICIPANT
     * 
     * @return The new EntityId object
     */
    public static EntityId createRTPSParticipant() {
        return new EntityId(EntityIdEnum.ENTITYID_RTPSPARTICIPANT);
    }

    /**
     * Get the a new EntityId whose value is ENTITYID_P2P_BUILTIN_RTPSPARTICIPANT_MESSAGE_WRITER
     * 
     * @return The new EntityId object
     */
    public static EntityId createWriterLiveliness() {
        return new EntityId(EntityIdEnum.ENTITYID_P2P_BUILTIN_RTPSPARTICIPANT_MESSAGE_WRITER);
    }

    /**
     * Get the a new EntityId whose value is ENTITYID_P2P_BUILTIN_RTPSPARTICIPANT_MESSAGE_READER
     * 
     * @return The new EntityId object
     */
    public static EntityId createReaderLiveliness() {
        return new EntityId(EntityIdEnum.ENTITYID_P2P_BUILTIN_RTPSPARTICIPANT_MESSAGE_READER);
    }

    /**
     * Get the value of the {@link EntityId}
     * 
     * @param index The index
     * @param value The value to be set
     */
    public void setValue(int index, byte value) {
        this.m_value[index] = value;
    }

    /**
     * Get the byte value of the {@link EntityId}
     * 
     * @param index The index of the desired value
     * @return The byte value of the {@link EntityId}
     */
    public byte getValue(int index) {
        return this.m_value[index];
    }

    /**
     * Copies the content of an instance of {@link EntityId}
     * 
     * @param value The {@link EntityId} to be copied
     */
    public void copy(EntityId value) {
        for (int i = 0; i < 4; ++i) {
            m_value[i] = value.m_value[i];
        }
    }

    /**
     * Get the EntityId serialized size
     */
    @Override
    public short getSerializedSize() {
        return 4;
    }

    /**
     * Serializes an EntityId
     */
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        for (int i = 0; i < this.m_value.length; ++i) {
            impl.serializeByte(message, name, this.m_value[i]);
        }
    }

    /**
     * Deserializes an EntityId
     */
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        for (int i = 0; i < this.m_value.length; ++i) {
            this.m_value[i] = impl.deserializeByte(message, name);
        }
    }

    /**
     * Compares two EntityId objects
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof EntityId) {
            boolean equals = true;
            for (int i = 0; i < 4; ++i) {
                equals &= this.m_value[i] == ((EntityId) other).m_value[i];
            }
            return equals;
        }
        return false;
    }

    /**
     * Converts an EntityId to its String representation
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.m_value.length; ++i) {
            //sb.append(this.m_value[i]);
            sb.append(String.format("%02X ", this.m_value[i]));
        }
        return sb.toString();
    }
    
    /**
     * Get a hash associated with the EntityId
     */
    @Override
    public int hashCode() {
        ByteBuffer wrapped = ByteBuffer.wrap(m_value);
        return wrapped.getInt();
    }

}
