package org.fiware.kiara.ps.rtps.common;

import org.fiware.kiara.ps.rtps.messages.elements.GUID;

public class MatchingInfo {
	
	public MatchingStatus status;
	public GUID remoteEndpointGuid;
	
	public MatchingInfo(MatchingStatus status, GUID guid) {
		this.status = status;
		this.remoteEndpointGuid = guid;
	}

}
