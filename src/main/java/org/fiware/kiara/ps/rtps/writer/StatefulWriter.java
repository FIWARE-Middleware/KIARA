package org.fiware.kiara.ps.rtps.writer;

import org.fiware.kiara.ps.rtps.attributes.RemoteReaderAttributes;
import org.fiware.kiara.ps.rtps.attributes.WriterAttributes;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.history.WriterHistoryCache;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;

public class StatefulWriter extends RTPSWriter {
    
    // TODO Implement

    public StatefulWriter(RTPSParticipant participant, GUID guid,
            WriterAttributes att, WriterHistoryCache history,
            WriterListener listener) {
        super(participant, guid, att, history, listener);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean matchedReaderAdd(RemoteReaderAttributes ratt) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean matchedReaderRemove(RemoteReaderAttributes ratt) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean matchedReaderIsMatched(RemoteReaderAttributes ratt) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void updateAttributes(WriterAttributes att) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void unsentChangesNotEmpty() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void unsentChangeAddedToHistory(CacheChange change) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean changeRemovedByHistory(CacheChange change) {
        // TODO Auto-generated method stub
        return false;
    }

}
