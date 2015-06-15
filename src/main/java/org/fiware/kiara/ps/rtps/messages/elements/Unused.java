package org.fiware.kiara.ps.rtps.messages.elements;

import java.io.IOException;

import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.ps.rtps.messages.RTPSSubmessageElement;

public class Unused  extends RTPSSubmessageElement {
	
	private byte[] m_gap;
	
	public Unused(int nBytes) {
		this.m_gap = new byte[nBytes];
	}

	@Override
	public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
		for (int i=0; i < m_gap.length; ++i) {
			impl.serializeByte(message, name, (byte) 0); 
		}
	}

	@Override
	public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
		for (int i=0; i < m_gap.length; ++i) {
			impl.deserializeByte(message, name); 
		}
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Unused) {
			if (m_gap.length == ((Unused) other).m_gap.length) {
				return true;
			}
		}
		return false;
	}

	@Override
	public short getSerializedSize() {
		return (short) this.m_gap.length;
	}

}
