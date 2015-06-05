package org.fiware.kiara.ps.rtps.messages.elements;

import java.io.IOException;

import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.ps.rtps.messages.RTPSSubmessageElement;

public class InstanceHandle extends RTPSSubmessageElement {
	
	private byte[] m_value;
	
	public InstanceHandle() {
		this.m_value = new byte[16];
		for (int i=0; i < 16; ++i) {
			this.m_value[i] = (byte) 0;
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
	public short getSize() {
		return (short) 16;
	}

	@Override
	public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
		for (int i=0; i < 16; ++i) {
			impl.serializeByte(message, "", this.m_value[i]);
		}
	}

	@Override
	public void deserialize(SerializerImpl impl, BinaryInputStream message,
			String name) throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean equals(Object other) {
	    if (other instanceof InstanceHandle) {
	        return this.m_value.equals(((InstanceHandle) other).m_value);
	    }
	    return false;
	}
	
	

}
