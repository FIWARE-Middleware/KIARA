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
	public short getSerializedSize() {
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
