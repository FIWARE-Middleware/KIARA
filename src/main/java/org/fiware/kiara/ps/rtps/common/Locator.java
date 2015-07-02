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

import java.util.Arrays;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public class Locator {
	
	private LocatorKind m_kind;
	private int m_port;
	private byte[] m_address;
	
	private void initAddr() {
		this.m_address = new byte[16];
		for (int i=0; i < 16; ++i) {
			this.m_address[i] = (byte) 0;
		}
	}
	
	public Locator() {
		this.m_kind = LocatorKind.LOCATOR_KIND_UDPv4;
		this.m_port = 0;
		this.initAddr();
	}
	
	public Locator(int port) {
		this.m_kind = LocatorKind.LOCATOR_KIND_UDPv4;
		this.m_port = port;
		this.initAddr();
	}
	
	public Locator(Locator other) {
		this.m_kind = other.m_kind;
		this.m_port = other.m_port;
		this.m_address = new byte[other.m_address.length];
		System.arraycopy(other.m_address, 0, this.m_address, 0, other.m_address.length);
	}
	
	public boolean isValid() {
		return true;
	}

	public LocatorKind getKind() {
		return m_kind;
	}

	public void setKind(LocatorKind m_kind) {
		this.m_kind = m_kind;
	}

	public int getPort() {
		return m_port;
	}

	public void setPort(int m_port) {
		this.m_port = m_port;
	}
	
	public void increasePort() {
	    ++this.m_port;
	}

	public byte[] getAddress() {
		return m_address;
	}

	public void setAddress(byte[] m_address) {
		this.m_address = m_address;
	}
	
	public void setIPv4Address(String m_address) {
	    String[] splitted = m_address.split("\\.");
	    if (splitted.length != 4) {
	        return;
	    }
            this.m_address[12] = (byte) Integer.parseInt(splitted[0]);
            this.m_address[13] = (byte) Integer.parseInt(splitted[1]);
            this.m_address[14] = (byte) Integer.parseInt(splitted[2]);
            this.m_address[15] = (byte) Integer.parseInt(splitted[3]);
        }
	
	@Override
	public boolean equals(Object other) {
	    if (other instanceof Locator) {
	        Locator loc = (Locator) other;
	        return Arrays.equals(this.m_address, loc.m_address) && this.m_kind == loc.m_kind && this.m_port == loc.m_port;
	    }
	    return false;
	}

	public boolean isAddressDefined() {
		if (this.m_address.length == 16) {
			if (this.m_kind == LocatorKind.LOCATOR_KIND_UDPv4) {
				for (byte i = 12; i < 16; i++) {
					if (this.m_address[i] != 0) {
						return true;
					}
				}
			} else if (this.m_kind == LocatorKind.LOCATOR_KIND_UDPv6) {
				for (byte i = 0; i < 16; i++) {
					if (this.m_address[i] != 0) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public String toIPv4String() {
		/*int[] parsedAddr = new int[4];
		parsedAddr[0] = this.m_address[12];
		parsedAddr[1] = this.m_address[13];
		parsedAddr[2] = this.m_address[14];
		parsedAddr[3] = this.m_address[15];
		for (int i = 0; i < 4; ++i) {
			parsedAddr[i] = this.m_address[12+i] & 0xFF;
		}*/
		return new String((this.m_address[12] & 0xFF) + "." + (this.m_address[13] & 0xFF) + "." + (this.m_address[14] & 0xFF) + "." + (this.m_address[15] & 0xFF));
	}
	
	

}
