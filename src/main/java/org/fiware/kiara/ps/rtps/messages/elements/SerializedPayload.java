package org.fiware.kiara.ps.rtps.messages.elements;

import java.io.IOException;
import java.util.ArrayList;

import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.CDRSerializer;
import org.fiware.kiara.serialization.impl.Serializable;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.ps.rtps.common.EncapsulationKind;
import org.fiware.kiara.ps.rtps.messages.RTPSMessage;
import org.fiware.kiara.ps.rtps.messages.RTPSSubmessageElement;

public class SerializedPayload extends RTPSSubmessageElement {
	
	private short m_length;
	private ParameterList m_parameterList;
	private Serializable m_appData;
	private EncapsulationKind m_encapsulation;
	private CDRSerializer m_ownSerializer;
	private boolean m_dataFlag;
	private boolean m_keyFlag;
	
	public static final int PAYLOAD_MAX_SIZE = 64000;
	
	private byte[] m_buffer;
	
	public SerializedPayload() {
		this.m_length = 0;
		this.m_encapsulation = EncapsulationKind.CDR_BE;
		this.m_parameterList = null;
		this.m_appData = null;
		this.m_ownSerializer = new CDRSerializer(false);
		
	}

        public CDRSerializer getSerializer() {
            return m_ownSerializer;
        }

	public short getLength() {
		return this.m_length;
	}

	public void setLength(short length) {
		this.m_length = length;
	}

        public byte[] getBuffer() {
            return m_buffer;
        }

        public void setBuffer(byte[] buffer) {
            m_buffer = buffer;
            m_length = (short) buffer.length;
        }

	public Serializable getData() {
		return this.m_appData;
	}
	
	public void setData(Serializable data) {
		this.m_appData = data;
	}
	
	public void setDataFlag(boolean dataFlag) {
		this.m_dataFlag = dataFlag;
	}
	
	public void setKeyFlag(boolean keyFlag) {
		this.m_keyFlag = keyFlag;
	}
	
	/*public void addData(RTPSSubmessageElement data) {
		this.m_data.add(data);
		this.m_length = (short) (this.m_length + data.getSerializedSize());
	}*/
	
	public void addParameter(Parameter param) {
		if (this.m_encapsulation == EncapsulationKind.PL_CDR_BE || this.m_encapsulation == EncapsulationKind.PL_CDR_LE) {
			if (this.m_parameterList == null) {
				this.m_parameterList = new ParameterList();
			}
			this.m_parameterList.addParameter(param);
			this.m_length = (short) (this.m_length + param.getSerializedSize());
		}
	}
	
	public void setData(Serializable data, short length) {
		if (this.m_encapsulation == EncapsulationKind.CDR_BE || this.m_encapsulation == EncapsulationKind.CDR_LE) {
			this.m_appData = data;
			this.m_length = (short) (this.m_length + length);
		}
	}
	
	public EncapsulationKind getEncapsulation() {
		return this.m_encapsulation;
	}
	
	public void setEncapsulationKind(EncapsulationKind encapsulation) {
		this.m_encapsulation = encapsulation;
	}

	@Override
	public short getSerializedSize() {
		return (short) (2 + 2 + this.m_length);
	}
	
	private boolean checkEndianness() {
		if (this.m_encapsulation == EncapsulationKind.PL_CDR_BE || this.m_encapsulation == EncapsulationKind.CDR_BE) {
			return false;
		} else {
			return true;
		}
	}

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

	@Override
	public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {

		this.m_ownSerializer.setEndianness(checkEndianness()); // true = LE, false = BE
		
		message.skipBytes(1); // Encapsulation empty octet
		this.m_encapsulation = EncapsulationKind.createFromValue(impl.deserializeByte(message, "")); // Encapsulation octet
		message.skipBytes(2); // Encapsulation options
		
		this.m_buffer = new byte[this.m_length];
                message.readFully(m_buffer);
	}
	
	public void deserializeData(/*SerializerImpl impl, String name*/) throws IOException {
		if (this.m_appData == null) {
			System.out.println("Type not specified in SerializedPayload object."); // TODO Log this
			return;
		}
		
		CDRSerializer impl = new CDRSerializer(checkEndianness());
		BinaryInputStream message = new BinaryInputStream(this.m_buffer);
		this.m_appData.deserialize(impl, message, "");
	}
	
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

    

}
