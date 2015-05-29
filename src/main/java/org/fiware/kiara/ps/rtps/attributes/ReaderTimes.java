package org.fiware.kiara.ps.rtps.attributes;

import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;

public class ReaderTimes {
	
	public Timestamp heartbeatResponseDelay;
	
	public ReaderTimes() {
		this.heartbeatResponseDelay = new Timestamp(0, 500*1000*1000);
	}

}
