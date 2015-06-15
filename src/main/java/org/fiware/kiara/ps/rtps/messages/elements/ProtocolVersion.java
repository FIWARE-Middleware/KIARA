package org.fiware.kiara.ps.rtps.messages.elements;

import java.io.IOException;

import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.ps.rtps.messages.RTPSSubmessageElement;

public class ProtocolVersion extends RTPSSubmessageElement {
	
	public byte m_major;
	public byte m_minor;
	
	public ProtocolVersion() {
		m_major = (byte) 2;
		m_minor = (byte) 1;
	}
	
	public ProtocolVersion(byte major, byte minor) {
		m_major = major;
		m_minor = minor;
	}
	
	/*public void serialize(CDRSerializer ser, BinaryOutputStream bos) {
		try {
			ser.serializeByte(bos, "", this.m_major);
			ser.serializeByte(bos, "", this.m_minor);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

	@Override
	public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
		impl.serializeByte(message, "", this.m_major);
		impl.serializeByte(message, "", this.m_minor);
	}

	@Override
	public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
		this.m_major = impl.deserializeByte(message, "");
		this.m_minor = impl.deserializeByte(message, "");
	}

	@Override
	public short getSerializedSize() {
		return 2;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof ProtocolVersion) {
			return this.m_major == ((ProtocolVersion) other).m_major && this.m_minor == ((ProtocolVersion) other).m_minor;
		} 
		return false;
	}
	
	public boolean isLowerThan(ProtocolVersion other) {
		if (this.m_major < other.m_major) {
			return true;
		} else if (this.m_major > other.m_major) {
			return false;
		} else {
			if (this.m_minor < other.m_minor) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean isLowerOrEqualThan(ProtocolVersion other) {
		if (this.m_major < other.m_major) {
			return true;
		} else if (this.m_major > other.m_major) {
			return false;
		} else {
			if (this.m_minor <= other.m_minor) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean isGreaterThan(ProtocolVersion other) {
		if (this.m_major > other.m_major) {
			return true;
		} else if (this.m_major < other.m_major) {
			return false;
		} else {
			if (this.m_minor > other.m_minor) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean isGreaterOrEqualThan(ProtocolVersion other) {
		if (this.m_major > other.m_major) {
			return true;
		} else if (this.m_major < other.m_major) {
			return false;
		} else {
			if (this.m_minor >= other.m_minor) {
				return true;
			} else {
				return false;
			}
		}
	}

}
