package org.fiware.kiara.ps.rtps.messages.elements;

import java.io.IOException;

import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.ps.rtps.messages.RTPSSubmessageElement;

public class Pad extends RTPSSubmessageElement {
	
	private short m_length;
	
	public Pad(short length) {
		this.m_length = length;
	}

	@Override
	public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
		for (int i=0; i < this.m_length; ++i) {
			impl.serializeByte(message, name, (byte) 0);
		}
	}

	@Override
	public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
		for (int i=0; i < this.m_length; ++i) {
			impl.deserializeByte(message, name);
		}
	}

	@Override
	public short getSerializedSize() {
		return this.m_length;
	}
	
	@Override
	public boolean equals(Object other) {
		return true;
	}
	
	

}
