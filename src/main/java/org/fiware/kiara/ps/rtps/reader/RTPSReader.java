package org.fiware.kiara.ps.rtps.reader;

import org.fiware.kiara.ps.rtps.Endpoint;
import org.fiware.kiara.ps.rtps.attributes.EndpointAttributes;
import org.fiware.kiara.ps.rtps.attributes.ReaderAttributes;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.history.ReaderHistoryCache;
import org.fiware.kiara.ps.rtps.messages.elements.EntityId;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;

public class RTPSReader extends Endpoint {

	public RTPSReader(RTPSParticipant participant, GUID guid, ReaderAttributes att, ReaderHistoryCache history, ReaderListener listener) {
		super(participant, guid, att.endpointAtt);
		// TODO Auto-generated constructor stub
	}

	public void changeRemovedByHistory(CacheChange change) {
		// TODO Auto-generated method stub
		
	}

	public boolean acceptMsgDirectedTo(EntityId readerId) {
		// TODO Auto-generated method stub
		return false;
	}

}
