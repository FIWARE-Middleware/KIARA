package org.fiware.kiara.ps.rtps.messages;

import java.io.IOException;

import org.fiware.kiara.ps.rtps.messages.common.types.SubmessageFlags;
import org.fiware.kiara.ps.rtps.messages.common.types.SubmessageId;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.CDRSerializer;
import org.fiware.kiara.serialization.impl.Serializable;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class RTPSSubmessageHeader implements Serializable {
	
	SubmessageId m_submessageId;
	SubmessageFlags m_flags;
	short m_submessageLength;
	int m_submessageLengthLarger;
	short m_octectsToNextHeader;
	
	public RTPSSubmessageHeader() {
		this.m_flags = new SubmessageFlags();
	}
	
	public void setSubmessageId(SubmessageId submessageId) {
		this.m_submessageId = submessageId;
	}
	
	public SubmessageId getSubmessageId() {
		return this.m_submessageId;
	}
	
	public void setFlags(SubmessageFlags flags) {
		this.m_flags = flags;
	}
	
	public SubmessageFlags getFlags() {
		return this.m_flags;
	}
	
	public void setOctectsToNextHeader(short octectsToNextHeader) {
		this.m_octectsToNextHeader = octectsToNextHeader;
	}
	
	public short getOctectsToNextHeader() {
		return this.m_octectsToNextHeader;
	}
	
	@Override
	public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
		impl.serializeByte(message, "", (byte) this.m_submessageId.getValue());
		impl.serializeByte(message, "", this.m_flags.getByteValue());
		impl.serializeUI16(message, "", this.m_octectsToNextHeader);
	}

	@Override
	public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
		this.m_submessageId = SubmessageId.createFromValue(impl.deserializeByte(message, ""));
		this.m_flags.setFlagValue(impl.deserializeByte(message, ""));
		((CDRSerializer) impl).setEndianness(this.m_flags.getFlagValue(0));
		this.m_octectsToNextHeader = impl.deserializeUI16(message, "");
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof RTPSSubmessageHeader) {
			RTPSSubmessageHeader instance = (RTPSSubmessageHeader) other;
			boolean retVal = true;
			
			retVal &= this.m_submessageId == instance.m_submessageId;
			retVal &= this.m_flags.equals(instance.m_flags);
			retVal &= this.m_octectsToNextHeader == instance.m_octectsToNextHeader;
			retVal &= this.m_submessageLength == instance.m_submessageLength;
			retVal &= this.m_submessageLengthLarger == instance.m_submessageLengthLarger;
			
			return retVal;
		}
		return false;
	}
	
}
