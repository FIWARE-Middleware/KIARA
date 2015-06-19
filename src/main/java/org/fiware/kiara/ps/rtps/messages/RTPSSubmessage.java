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
package org.fiware.kiara.ps.rtps.messages;

import java.io.IOException;
import java.util.ArrayList;

import org.fiware.kiara.ps.rtps.messages.common.types.RTPSEndian;
import org.fiware.kiara.ps.rtps.messages.elements.ExtraFlags;
import org.fiware.kiara.ps.rtps.messages.elements.OctectsToInlineQos;
import org.fiware.kiara.ps.rtps.messages.elements.SerializedPayload;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.CDRSerializer;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public class RTPSSubmessage {
	
	protected RTPSSubmessageHeader m_submessageHeader;
	protected CDRSerializer m_serializer;
	protected ArrayList<RTPSSubmessageElement> m_submessageElements;
	private RTPSEndian m_endian = RTPSEndian.BIG_ENDIAN; // false BE, true LE. DEFAULT VALUE = BIG_ENDIAN
	
	public RTPSSubmessage() {
		this.m_submessageElements = new ArrayList<RTPSSubmessageElement>();
		/*this.m_serializer = new CDRSerializer();*/
	}
	
	public void setSubmessageEndian(RTPSEndian endian) {
		this.m_endian = endian;
	}
	
	public RTPSEndian getSubmessageEndian() {
		return this.m_endian;
	}
	
	public void initSerializer() {
		this.m_serializer = new CDRSerializer(this.m_endian == RTPSEndian.BIG_ENDIAN ? false : true);
	}
	
	public void setSubmessageHeader(RTPSSubmessageHeader submessageHeader) {
		this.m_submessageHeader = submessageHeader;
	}
	
	public RTPSSubmessageHeader getSubmessageHeader() {
		return this.m_submessageHeader;
	}
	
	public void addSubmessageElement(RTPSSubmessageElement element) {
		/*if (element instanceof SerializedPayload) {
			switch (((SerializedPayload) element).getEncapsulation()) {
			case CDR_BE:
			case PL_CDR_BE:
				this.m_endian = RTPSEndian.BIG_ENDIAN;
				break;
			case CDR_LE:
			case PL_CDR_LE:
				this.m_endian = RTPSEndian.LITTLE_ENDIAN;
				break;
			}
		}*/
		this.m_submessageElements.add(element);
	}
	
	public void serialize(CDRSerializer ser, BinaryOutputStream bos, boolean isLast) {
		if (this.m_serializer == null) {
			this.initSerializer();
		}
		try {
			this.m_submessageHeader.serialize(ser, bos, "");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// NEW
		
		int sizePos = bos.getPosition() - 2 /* Size of the field in Bytes */;
		int currentPos = bos.getPosition();
		
		// END NEW
		
		try {
			for (RTPSSubmessageElement elm : this.m_submessageElements) {
				elm.serialize(this.m_serializer, bos, "");
			}
			
			// NEW
			int finalPos = bos.getPosition();
			short size = (short) (finalPos - currentPos);
			
			if (!isLast && (size % 4 != 0)) {
				short diff = (short) (4 - (size % 4));
				size = (short) (size + diff);
				this.m_serializer.addPadding(bos, diff);
			}
			
			byte newBuffer[] = new byte[2];
			BinaryOutputStream newBos = new BinaryOutputStream();
			newBos.setBuffer(newBuffer);
			
			ser.serializeI16(newBos, "", size);
			System.arraycopy(newBuffer, 0, bos.getBuffer(), sizePos, 2);
			this.m_submessageHeader.setOctectsToNextHeader(size);
			
			
			// END NEW
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof RTPSSubmessage) {
			RTPSSubmessage instance = (RTPSSubmessage) other;
			boolean retVal = true;
			
			retVal &= this.m_submessageHeader.equals(instance.m_submessageHeader);
			retVal &= this.m_submessageElements.equals(instance.m_submessageElements);
			
			return retVal;
		}
		return false;
	}
	
	public short getLength() {
		short retVal = 0;
		
		for (RTPSSubmessageElement elm : this.m_submessageElements) {
			retVal = (short) (retVal + elm.getSerializedSize());
		}
		
		return retVal;
	}

}
