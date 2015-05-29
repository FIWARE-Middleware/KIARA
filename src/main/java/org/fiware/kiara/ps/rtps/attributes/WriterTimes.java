package org.fiware.kiara.ps.rtps.attributes;

import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;

public class WriterTimes {
	
	public Timestamp heartBeatPeriod;
	public Timestamp nackResponseDelay;
	public Timestamp nackSupressionDuration;
	
	public WriterTimes() {
		this.heartBeatPeriod = new Timestamp(3, 0);
		this.nackResponseDelay = new Timestamp(0, 200*1000*1000);
		this.nackSupressionDuration = new Timestamp().timeZero();
	}

}
