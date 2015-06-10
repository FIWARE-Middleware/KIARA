package org.fiware.kiara.ps.rtps.reader;

import org.fiware.kiara.ps.rtps.attributes.ReaderAttributes;
import org.fiware.kiara.ps.rtps.history.ReaderHistoryCache;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;

public class StatefulReader extends RTPSReader {
    
    // TODO Implement

    public StatefulReader(RTPSParticipant participant, GUID guid,
            ReaderAttributes att, ReaderHistoryCache history,
            ReaderListener listener) {
        super(participant, guid, att, history, listener);
        // TODO Auto-generated constructor stub
    }

}
