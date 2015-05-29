package org.fiware.kiara.ps.rtps.messages.elements;

import java.io.IOException;

import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.ps.rtps.messages.RTPSSubmessageElement;

public class GUIDPrefix extends RTPSSubmessageElement {
	
	byte[] m_value;
	
	public GUIDPrefix() {
		m_value = new byte[12];
		for (int i=0; i < 12; ++i) {
			m_value[i] = (byte) 0;
		}
	}
	
	public GUIDPrefix(byte[] value) throws Exception {
		if (value.length != 12) {
			throw new Exception();
		}
		
		for (int i=0; i < 12; ++i) {
			m_value[i] = value[i];
		}
	}
	
	@Override
	public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
		for (int i=0; i < 12; ++i) {
			impl.serializeByte(message, "", this.m_value[i]);
		}
	}

	@Override
	public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
		for (int i=0; i < 12; ++i) {
			this.m_value[i] = impl.deserializeByte(message, "");
		}
	}

	@Override
	public short getSize() {
		return 12;
	}
	
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

}
