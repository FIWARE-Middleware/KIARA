package org.fiware.kiara.ps.rtps.writer;

import java.util.List;

import org.fiware.kiara.ps.rtps.attributes.WriterAttributes;
import org.fiware.kiara.ps.rtps.history.WriterHistoryCache;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;

public class StatelessWriter extends RTPSWriter {
    
    // TODO Implement
    
    private List<ReaderLocator> m_readerLocator;

    public StatelessWriter(RTPSParticipant participant, GUID guid,
            WriterAttributes att, WriterHistoryCache history,
            WriterListener listener) {
        super(participant, guid, att, history, listener);
        // TODO Auto-generated constructor stub
    }

}
