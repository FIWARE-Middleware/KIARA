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
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class EntityId extends RTPSSubmessageElement {

    public enum EntityIdEnum {

        ENTITYID_UNKNOWN (0x00000000),
        ENTITYID_RTPSPARTICIPANT (0x000001C1),
        ENTITYID_SEDP_BUILTIN_TOPIC_WRITER (0x000002C2),
        ENTITYID_SEDP_BUILTIN_TOPIC_READER (0x000002C7),
        ENTITYID_SEDP_BUILTIN_PUBLICATIONS_WRITER (0x000003C2),
        ENTITYID_SEDP_BUILTIN_PUBLICATIONS_READER (0x000003C7),
        ENTITYID_SEDP_BUILTIN_SUBSCRIPTIONS_WRITER (0x000004C2),
        ENTITYID_SEDP_BUILTIN_SUBSCRIPTIONS_READER (0x000004C7),
        ENTITYID_SPDP_BUILTIN_RTPSPARTICIPANT_WRITER (0x000100C2),
        ENTITYID_SPDP_BUILTIN_RTPSPARTICIPANT_READER (0x000100C7),
        ENTITYID_P2P_BUILTIN_RTPSPARTICIPANT_MESSAGE_WRITER (0x000200C2),
        ENTITYID_P2P_BUILTIN_RTPSPARTICIPANT_MESSAGE_READER (0x000200C7);

        private final byte[] m_value;
        private final int m_intValue;

        EntityIdEnum(int value) {
            this.m_value = ByteBuffer.allocate(4).putInt(value).array();
            this.m_intValue = value;
        }

        public byte[] getValue() {
            return this.m_value;
        }

        public int getIntValue() {
            return this.m_intValue;
        }
    }

    byte m_value[];

    public EntityId() {
        this.m_value = new byte[4];
        this.m_value[0] = 0x00;
        this.m_value[1] = 0x00;
        this.m_value[2] = 0x00;
        this.m_value[3] = 0x00;
    }

    public EntityId(int value) {
        this.m_value = ByteBuffer.allocate(4).putInt(value).array();
    }

    public EntityId(EntityIdEnum value) {
        this.m_value = value.getValue().clone();
    }

    public boolean isSPDPReader() {
        return equals(new EntityId(EntityIdEnum.ENTITYID_SPDP_BUILTIN_RTPSPARTICIPANT_READER));
    }

    public boolean isSPDPWriter() {
        return equals(new EntityId(EntityIdEnum.ENTITYID_SPDP_BUILTIN_RTPSPARTICIPANT_WRITER));
    }

    public boolean isSEDPPubWriter() {
        return equals(new EntityId(EntityIdEnum.ENTITYID_SEDP_BUILTIN_PUBLICATIONS_WRITER));
    }

    public boolean isSEDPPubReader() {
        return equals(new EntityId(EntityIdEnum.ENTITYID_SEDP_BUILTIN_PUBLICATIONS_READER));
    }

    public boolean isSEDPSubWriter() {
        return equals(new EntityId(EntityIdEnum.ENTITYID_SEDP_BUILTIN_SUBSCRIPTIONS_WRITER));
    }

    public boolean isSEDPSubReader() {
        return equals(new EntityId(EntityIdEnum.ENTITYID_SEDP_BUILTIN_SUBSCRIPTIONS_READER));
    }

    public boolean isRTPSParticipant() {
        return equals(new EntityId(EntityIdEnum.ENTITYID_RTPSPARTICIPANT));
    }

    public boolean isWriterLiveliness() {
        return equals(new EntityId(EntityIdEnum.ENTITYID_P2P_BUILTIN_RTPSPARTICIPANT_MESSAGE_WRITER));
    }
    public boolean isReaderLiveliness() {
        return equals(new EntityId(EntityIdEnum.ENTITYID_P2P_BUILTIN_RTPSPARTICIPANT_MESSAGE_READER));
    }

    public void setValue(int index, byte value) {
        this.m_value[index] = value;
    }
    
    public byte getValue(int index) {
        return this.m_value[index];
    }

    public void copy(EntityId value) {
        for (int i = 0; i < 4; ++i) {
            m_value[i] = value.m_value[i];
        }
    }

    /*@Override
	public void serialize(CDRSerializer ser, BinaryOutputStream bos) {
		try {

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

    @Override
    public short getSerializedSize() {
        return 4;
    }

    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        for (int i=0; i < this.m_value.length; ++i) {
            impl.serializeByte(message, name, this.m_value[i]);
        }
    }

    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        for (int i=0; i < this.m_value.length; ++i) {
            this.m_value[i] = impl.deserializeByte(message, name);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof EntityId) {
            boolean equals = true;
            for (int i=0; i < 4; ++i) {
                equals &= this.m_value[i] == ((EntityId) other).m_value[i];
            }
            return equals;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("");
        for (int i=0; i < this.m_value.length; ++i) {
            sb.append(this.m_value[i]);
        }
        return sb.toString();
    }

    

}
