package org.fiware.kiara.ps.publisher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.fiware.kiara.ps.rtps.attributes.RemoteWriterAttributes;
import org.fiware.kiara.ps.rtps.common.ChangeFromWriter;
import org.fiware.kiara.ps.rtps.common.ChangeFromWriterStatus;
import org.fiware.kiara.ps.rtps.messages.elements.SequenceNumber;
import org.fiware.kiara.ps.rtps.reader.StatefulReader;
import org.fiware.kiara.ps.rtps.reader.timedevent.HeartbeatResponseDelay;
import org.fiware.kiara.ps.rtps.reader.timedevent.WriterProxyLiveliness;

public class WriterProxy {
    
    // TODO Implement
    
    public StatefulReader statefulReader;
    
    public RemoteWriterAttributes att;
    
    public List<ChangeFromWriter> changesFromWriter; 
    
    public int acknackCount;
    
    public int lastHeartbeatCount;
    
    public boolean isMissingChangesEmpty;
    
    public HeartbeatResponseDelay heartBeatResponse;
    
    public WriterProxyLiveliness writerProxyLiveliness;
    
    public boolean hearbeatFinalFlag;
    
    public SequenceNumber lastRemovedSeqNum;
    
    private SequenceNumber m_maxAvailableSeqNum;
    
    private SequenceNumber m_minAvailableSeqNum;
    
    private boolean m_hasMaxAvailableSeqNumChanged;
    
    private boolean m_hasMinAvailableSeqNumChanged;
    
    private boolean m_isAlive;
    
    private boolean m_firstReceived;
    
    
    private final Lock m_mutex = new ReentrantLock(true);
    
    public SequenceNumber getAvailableChangesMax() {
        this.m_mutex.lock();
        SequenceNumber seqNum = new SequenceNumber();
        try {
            if (this.lastRemovedSeqNum.toLong() <= 0 && this.changesFromWriter.size() == 0)  { // Nothing received
                return null;
            }
            if (this.m_hasMaxAvailableSeqNumChanged) {
                seqNum.setHigh(0);
                seqNum.setLow(0);
                
                for (ChangeFromWriter it : this.changesFromWriter) {
                    if (it.status == ChangeFromWriterStatus.RECEIVED || it.status == ChangeFromWriterStatus.LOST) {
                        seqNum = it.seqNum;
                        this.m_maxAvailableSeqNum = it.seqNum;
                        this.m_hasMaxAvailableSeqNumChanged = false;
                    } else {
                        break;
                    }
                }
            } else {
                seqNum = this.m_maxAvailableSeqNum;
            }
            
            if (seqNum.isLowerThan(this.m_maxAvailableSeqNum)) {
                seqNum = this.lastRemovedSeqNum;
                this.m_maxAvailableSeqNum = this.lastRemovedSeqNum;
                this.m_hasMaxAvailableSeqNumChanged = false;
            }
            
            return seqNum;
        } finally {
            this.m_mutex.unlock();
        }
    }
    
    public List<ChangeFromWriter> getMissingChanges() {
        if (!this.changesFromWriter.isEmpty()) {
            this.m_mutex.lock();
            try {
                List<ChangeFromWriter> missing = new ArrayList<ChangeFromWriter>();
                for (ChangeFromWriter it : this.changesFromWriter) {
                    if (it.status == ChangeFromWriterStatus.MISSING && it.isRelevant) {
                        missing.add(it);
                    }
                }
                if (missing.isEmpty()) {
                    this.isMissingChangesEmpty = true;
                    //printChangesFromWriterTest(); Debugging purposes (not implemented in Java version)
                }
                return missing;
            } finally {
                this.m_mutex.unlock();
            }
        }
        return null;
    }

    public void assertLiveliness() {
        // TODO Auto-generated method stub
        
    }

}
