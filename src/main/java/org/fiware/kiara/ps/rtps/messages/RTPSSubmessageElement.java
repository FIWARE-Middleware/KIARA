package org.fiware.kiara.ps.rtps.messages;

import org.fiware.kiara.serialization.impl.Serializable;


public abstract class RTPSSubmessageElement implements Serializable {
	
	public abstract short getSerializedSize();
	
}
