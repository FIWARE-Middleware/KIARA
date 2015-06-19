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
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public enum LocatorKind {
	
	LOCATOR_KIND_RESERVED(0),
	LOCATOR_KIND_UDPv4(1),
	LOCATOR_KIND_UDPv6(2),
	LOCATOR_KIND_INVALID(-1);
	
	private int m_value;
	
	private LocatorKind(int value) {
		this.m_value = value;
	}
	
	public int getValue() {
		return this.m_value;
	}

}
