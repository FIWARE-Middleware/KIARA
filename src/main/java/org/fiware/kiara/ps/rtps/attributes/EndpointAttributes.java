package org.fiware.kiara.ps.rtps.attributes;

import org.fiware.kiara.ps.rtps.common.DurabilityKind;
import org.fiware.kiara.ps.rtps.common.EndpointKind;
import org.fiware.kiara.ps.rtps.common.LocatorList;
import org.fiware.kiara.ps.rtps.common.ReliabilityKind;
import org.fiware.kiara.ps.rtps.common.TopicKind;

public class EndpointAttributes {
	
	public EndpointKind endpointKind;
	public TopicKind topicKind;
	public ReliabilityKind reliabilityKind;
	public DurabilityKind durabilityKind;
	public LocatorList unicastLocatorList;
	public LocatorList multicastLocatorList;
	private short m_userDefinedId;
	private short m_endityId;
	
	public EndpointAttributes() {
		this.topicKind = TopicKind.NO_KEY;
		this.reliabilityKind = ReliabilityKind.BEST_EFFORT;
		this.durabilityKind = DurabilityKind.VOLATILE;
		this.m_userDefinedId = -1;
		this.m_endityId = -1;
		this.endpointKind = EndpointKind.WRITER;
	}
	
	public short getUserDefinedID() {
		return this.m_userDefinedId;
	}
	
	public void setUserDefinedID(short id) {
		this.m_userDefinedId = id;
	}
	
	public short getEntityID() {
		return this.m_endityId;
	}
	
	public void setEntityID(short id) {
		this.m_endityId = id;
	}

}
