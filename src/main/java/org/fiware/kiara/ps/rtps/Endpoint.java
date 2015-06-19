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
package org.fiware.kiara.ps.rtps;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.fiware.kiara.ps.rtps.attributes.EndpointAttributes;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;

public class Endpoint {
	
	protected RTPSParticipant m_participant;
	
	protected GUID m_guid;
	
	protected EndpointAttributes m_att;
	
	protected final Lock m_mutex = new ReentrantLock(true);
	
	public Endpoint(RTPSParticipant participant, GUID guid, EndpointAttributes att) {
		this.m_participant = participant;
		this.m_guid = guid;
		this.m_att = att;
	}

	public EndpointAttributes getAttributes() {
		return this.m_att;
	}
	
	public GUID getGuid() {
		return this.m_guid;
	}
	
	public RTPSParticipant getRTPSParticipant() {
	    return this.m_participant;
	}

}
