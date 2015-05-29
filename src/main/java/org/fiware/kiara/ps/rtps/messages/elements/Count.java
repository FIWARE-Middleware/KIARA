package org.fiware.kiara.ps.rtps.messages.elements;

import java.io.IOException;

import org.fiware.kiara.ps.rtps.messages.RTPSSubmessageElement;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class Count extends RTPSSubmessageElement {
	
	private int m_value;
	
	public Count(int value) {
		this.m_value = value;
	}

	@Override
	public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
		impl.serializeI32(message, name, this.m_value);
	}

	@Override
	public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
		this.m_value = impl.deserializeI32(message, name);
	}

	@Override
	public short getSize() {
		return 4;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Count) {
			if (this.m_value == ((Count) other).m_value) {
				return true;
			}
		}
		return false;
	}

	public int getValue() {
		return m_value;
	}

	public void setValue(int m_value) {
		this.m_value = m_value;
	}

}
