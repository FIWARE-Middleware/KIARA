package org.fiware.kiara.ps.rtps.writer;

import org.fiware.kiara.ps.rtps.Endpoint;
import org.fiware.kiara.ps.rtps.attributes.EndpointAttributes;
import org.fiware.kiara.ps.rtps.attributes.WriterAttributes;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.history.WriterHistoryCache;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;

public class RTPSWriter extends Endpoint {

	public RTPSWriter(RTPSParticipant participant, GUID guid, WriterAttributes att, WriterHistoryCache history, WriterListener listener) {
		super(participant, guid, att.endpointAtt);
		// TODO Auto-generated constructor stub
	}

	/*public GUID getGUID() {
		// TODO Auto-generated method stub
		return null;
	}*/

	public void unsentChangeAddedToHistory(CacheChange change) {
		// TODO Auto-generated method stub
		
	}

	public void changeRemovedByHistory(CacheChange change) {
		// TODO Auto-generated method stub
		
	}

}
