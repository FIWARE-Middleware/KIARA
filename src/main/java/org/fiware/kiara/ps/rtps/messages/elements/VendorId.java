package org.fiware.kiara.ps.rtps.messages.elements;

import java.io.IOException;

import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.ps.rtps.messages.RTPSSubmessageElement;

public class VendorId extends RTPSSubmessageElement {
	
	byte m_id_0;
	byte m_id_1;
	
	public VendorId() {
		m_id_0 = (byte) 0x0;
		m_id_1 = (byte) 0x0;
	}
	
	public VendorId(byte id_0, byte id_1) {
		m_id_0 = id_0;
		m_id_1 = id_1;
	}
	
	public VendorId setVendorUnknown() {
		this.m_id_0 = (byte) 0x00;
		this.m_id_1 = (byte) 0x00;
		return this;
	}
	
	public VendorId setVendoreProsima() {
		this.m_id_0 = (byte) 0x01;
		this.m_id_1 = (byte) 0x0F;
		return this;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof VendorId) {
			VendorId instance = (VendorId) other;
			boolean retVal = true;
			
			retVal &= this.m_id_0 == instance.m_id_0;
			retVal &= this.m_id_1 == instance.m_id_1;
			
			return retVal;
		}
		return false;
	}
	
	/*public void serialize(CDRSerializer ser, BinaryOutputStream bos) {
		try {
			ser.serializeByte(bos, "", this.m_id_0);
			ser.serializeByte(bos, "", this.m_id_1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

	@Override
	public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
		impl.serializeByte(message, "", this.m_id_0);
		impl.serializeByte(message, "", this.m_id_1);
	}

	@Override
	public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
		this.m_id_0 = impl.deserializeByte(message, "");
		this.m_id_1 = impl.deserializeByte(message, "");
	}

	@Override
	public short getSerializedSize() {
		return 2;
	}

}
