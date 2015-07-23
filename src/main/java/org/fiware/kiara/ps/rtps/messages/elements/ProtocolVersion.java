/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2015 Proyectos y Sistemas de Mantenimiento S.L. (eProsima)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fiware.kiara.ps.rtps.messages.elements;

import java.io.IOException;

import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.ps.rtps.messages.RTPSSubmessageElement;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
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
	
	public byte getMajor() {
	    return this.m_major;
	}
	
	public byte getMinor() {
	    return this.m_minor;
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

    public void copy(ProtocolVersion protocolVersion) {
        this.m_minor = protocolVersion.m_minor;
        this.m_major = protocolVersion.m_major;
    }

}
