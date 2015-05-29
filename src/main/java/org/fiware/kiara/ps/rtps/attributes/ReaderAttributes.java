package org.fiware.kiara.ps.rtps.attributes;

import org.fiware.kiara.ps.rtps.common.DurabilityKind;
import org.fiware.kiara.ps.rtps.common.EndpointKind;
import org.fiware.kiara.ps.rtps.common.ReliabilityKind;

public class ReaderAttributes {
	
	public EndpointAttributes endpointAtt;
	public ReaderTimes times;
	public boolean expectsInlineQos;
	
	public ReaderAttributes() {
		this.endpointAtt.endpointKind = EndpointKind.READER;
		this.endpointAtt.durabilityKind = DurabilityKind.VOLATILE;
		this.endpointAtt.reliabilityKind = ReliabilityKind.BEST_EFFORT;
		this.expectsInlineQos = false;
	}

}
