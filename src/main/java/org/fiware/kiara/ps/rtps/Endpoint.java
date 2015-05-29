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

}
