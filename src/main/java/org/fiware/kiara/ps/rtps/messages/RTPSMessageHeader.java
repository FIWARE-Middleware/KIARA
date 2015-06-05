package org.fiware.kiara.ps.rtps.messages;

import java.io.IOException;
import java.util.Arrays;

import org.fiware.kiara.ps.rtps.messages.elements.GUIDPrefix;
import org.fiware.kiara.ps.rtps.messages.elements.ProtocolVersion;
import org.fiware.kiara.ps.rtps.messages.elements.VendorId;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.Serializable;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class RTPSMessageHeader implements Serializable {
	
	public static final int RTPS_MESSAGE_HEADER_SIZE = 20;
	
	char m_rtps[] = new char[4];
	
	ProtocolVersion m_protocolVersion;
	VendorId m_vendorId;
	GUIDPrefix m_guidPrefix;
	
	public RTPSMessageHeader() {
		this.m_rtps = new String("RTPS").toCharArray();
		this.m_protocolVersion = new ProtocolVersion((byte) 2, (byte) 1);
		this.m_vendorId = new VendorId().setVendoreProsima();
		this.m_guidPrefix = new GUIDPrefix();
	}
	
	public String getProtocolName() {
		return new String(this.m_rtps);
	}
	
	public ProtocolVersion getProtocolVersion() {
		return this.m_protocolVersion;
	}
	
	public VendorId getVendorId() {
		return this.m_vendorId;
	}
	
	public GUIDPrefix getGUIDPrefix() {
		return this.m_guidPrefix;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof RTPSMessageHeader) {
			RTPSMessageHeader instance = (RTPSMessageHeader) other;
			boolean retVal = true;
			
			retVal &= Arrays.equals(this.m_rtps, instance.m_rtps);
			retVal &= this.m_protocolVersion.equals(instance.m_protocolVersion);
			retVal &= this.m_vendorId.equals(instance.m_vendorId);
			retVal &= this.m_guidPrefix.equals(instance.m_guidPrefix);
			
			return retVal;
		}
		return false;
	}

	@Override
	public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
		impl.serializeChar(message, "", 'R');
		impl.serializeChar(message, "", 'T');
		impl.serializeChar(message, "", 'P');
		impl.serializeChar(message, "", 'S');
		
		this.m_protocolVersion.serialize(impl, message, "");
		
		this.m_vendorId.serialize(impl, message, "");
		
		this.m_guidPrefix.serialize(impl, message, "");
	}

	@Override
	public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
		this.m_rtps[0] = impl.deserializeChar(message, name);
		this.m_rtps[1] = impl.deserializeChar(message, name);
		this.m_rtps[2] = impl.deserializeChar(message, name);
		this.m_rtps[3] = impl.deserializeChar(message, name);
		
		this.m_protocolVersion.deserialize(impl, message, name);
		this.m_vendorId.deserialize(impl, message, name);
		this.m_guidPrefix.deserialize(impl, message, name);
	}
	
	

}