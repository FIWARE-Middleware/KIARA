package org.fiware.kiara.ps.rtps.writer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.fiware.kiara.ps.rtps.attributes.RemoteReaderAttributes;
import org.fiware.kiara.ps.rtps.attributes.WriterAttributes;
import org.fiware.kiara.ps.rtps.common.LocatorList;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.history.WriterHistoryCache;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatelessWriter extends RTPSWriter {
    
    // TODO Implement
    
    private List<ReaderLocator> m_readerLocator;
    
    private List<RemoteReaderAttributes> m_matchedReaders;
    
    private final Lock m_mutex;
    
    private static final Logger logger = LoggerFactory.getLogger(StatelessWriter.class);

    public StatelessWriter(RTPSParticipant participant, GUID guid, WriterAttributes att, WriterHistoryCache history, WriterListener listener) {
        super(participant, guid, att, history, listener);
        this.m_mutex = new ReentrantLock(true);
        
    }
    
    public void unsentChangeAddedToHistory(CacheChange change) {
        List<CacheChange> changes = new ArrayList<CacheChange>();
        changes.add(change);
        
        LocatorList locList = new LocatorList();;
        LocatorList locList2 = new LocatorList();
        
        //this.setLivelinessAsserted(true);
        
    }

}
