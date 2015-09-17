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
package org.fiware.kiara.ps.rtps.common;

/**
* Enumeraton representing the kind of encapsulation that is going to
* be used within an RTPS messgae.
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public enum EncapsulationKind {
	
	CDR_BE((byte) 0x00),
	CDR_LE((byte) 0x01),
	PL_CDR_BE((byte) 0x02),
	PL_CDR_LE((byte) 0x03);
	
	private final byte m_value;
	
	private EncapsulationKind(byte value) {
		this.m_value = value;
	}
	
	/**
	 * Get the EncapsulationKind enumeration value
	 * 
	 * @return The EncapsulationKind enumeration value
	 */
	public byte getValue() {
		return this.m_value;
	}
	
	/**
	 * Creates a new EncapsulationKind object from its ordinal value
	 * 
	 * @param value A byte integer indicating the enumeration value
	 * @return A new EncapsulationKind object according to the received value
	 */
	public static EncapsulationKind createFromValue(byte value) {
		switch (value) {
		case 0x00:
			return EncapsulationKind.CDR_BE;
		case 0x01:
			return EncapsulationKind.CDR_LE;
		case 0x02:
			return EncapsulationKind.PL_CDR_BE;
		case 0x03:
			return EncapsulationKind.PL_CDR_LE;
		default:
			return EncapsulationKind.CDR_BE;	
		}
	}

}
