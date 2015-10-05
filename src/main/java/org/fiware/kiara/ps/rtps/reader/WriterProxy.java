package org.fiware.kiara.ps.rtps.reader;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.fiware.kiara.ps.rtps.attributes.RemoteWriterAttributes;
import org.fiware.kiara.ps.rtps.common.ChangeForReaderStatus;
import org.fiware.kiara.ps.rtps.common.ChangeFromWriter;
import org.fiware.kiara.ps.rtps.common.ChangeFromWriterStatus;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.messages.elements.SequenceNumber;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;
import org.fiware.kiara.ps.rtps.reader.timedevent.HeartbeatResponseDelay;
import org.fiware.kiara.ps.rtps.reader.timedevent.WriterProxyLiveliness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public final SequenceNumber lastRemovedSeqNum;

    private final SequenceNumber m_maxAvailableSeqNum;

    private final SequenceNumber m_minAvailableSeqNum;

    public boolean hasMaxAvailableSeqNumChanged;

    public boolean hasMinAvailableSeqNumChanged;

    private boolean m_isAlive;

    private boolean m_firstReceived;

    private static final Logger logger = LoggerFactory.getLogger(WriterProxy.class);
    
    public static final int WRITERPROXY_LIVELINESS_PERIOD_MULTIPLIER = 1; // TODO Check in spec if this must be allowed to change

    private final Lock m_mutex = new ReentrantLock(true);

    public void destroy() {
        if (writerProxyLiveliness != null) {
            writerProxyLiveliness.stopTimer();
        }
    }

    public WriterProxy(RemoteWriterAttributes watt, Timestamp heartbeatResponse, StatefulReader SR) {
        statefulReader = SR;
        att = new RemoteWriterAttributes();
        att.copy(watt);
        this.changesFromWriter = new ArrayList<ChangeFromWriter>();
        this.m_firstReceived = true;
        //changesFromWriter.clear();
        //Create Events
        //heartBeatResponse = new HeartbeatResponseDelay(this, statefulReader.getTimes().heartbeatResponseDelay.toMilliSecondsDouble()*WRITERPROXY_LIVELINESS_PERIOD_MULTIPLIER);
        if (att.livelinessLeaseDuration.isLowerThan(new Timestamp().timeInfinite())) {
            writerProxyLiveliness = new WriterProxyLiveliness(this, att.livelinessLeaseDuration.toMilliSecondsDouble()*WRITERPROXY_LIVELINESS_PERIOD_MULTIPLIER);
        }
        lastRemovedSeqNum = new SequenceNumber();
        m_maxAvailableSeqNum = new SequenceNumber();
        m_minAvailableSeqNum = new SequenceNumber();
        logger.debug("RTPS READER: Writer Proxy created in reader: {}", statefulReader.getGuid().getEntityId());
    }

    public SequenceNumber getAvailableChangesMin() {
        return getAvailableChangesMin(null);
    }

    public SequenceNumber getAvailableChangesMin(SequenceNumber seqNum) {
        m_mutex.lock();
        try {
            if (lastRemovedSeqNum.toLong() <= 0 && changesFromWriter.isEmpty()) // NOT RECEIVED ANYTHING
            {
                return null;
            }

            if (seqNum == null) {
                seqNum = new SequenceNumber();
            }

            if (hasMinAvailableSeqNumChanged) {
		//Order changesFromWriter
                //	std::sort(m_changesFromW.begin(),m_changesFromW.end(),sort_chFW);
                seqNum.setHigh(0);
                seqNum.setLow(0);
                for (ChangeFromWriter it : changesFromWriter) {
                    if (it.status == ChangeFromWriterStatus.RECEIVED) {
                        if (it.isValid()) {
                            seqNum.copy(it.seqNum);
                            m_minAvailableSeqNum.copy(it.seqNum);
                            hasMinAvailableSeqNumChanged = false;
                            return seqNum;
                        } else {
                            continue;
                        }
                    } else if (it.status == ChangeFromWriterStatus.LOST) {
                        continue;
                    } else {
                        return null;
                    }
                }
            } else {
                seqNum.copy(this.m_minAvailableSeqNum);
            }
            if (seqNum.isLowerOrEqualThan(this.lastRemovedSeqNum)) {
                seqNum.copy(lastRemovedSeqNum);
                m_minAvailableSeqNum.copy(this.lastRemovedSeqNum);
                hasMinAvailableSeqNumChanged = false;
                return null;
            }
            return seqNum;
        } finally {
            m_mutex.unlock();
        }
    }

    public SequenceNumber getAvailableChangesMax() {
        return getAvailableChangesMax(null);
    }

    public SequenceNumber getAvailableChangesMax(SequenceNumber seqNum) {
        this.m_mutex.lock();
        try {
            if (this.lastRemovedSeqNum.toLong() <= 0 && this.changesFromWriter.isEmpty()) { // Nothing received
                return null;
            }
            if (seqNum == null)
                seqNum = new SequenceNumber();
            if (this.hasMaxAvailableSeqNumChanged) {
                seqNum.setHigh(0);
                seqNum.setLow(0);

                for (ChangeFromWriter it : this.changesFromWriter) {
                    if (it.status == ChangeFromWriterStatus.RECEIVED || it.status == ChangeFromWriterStatus.LOST) {
                        seqNum.copy(it.seqNum);
                        this.m_maxAvailableSeqNum.copy(it.seqNum);
                        this.hasMaxAvailableSeqNumChanged = false;
                    } else {
                        break;
                    }
                }
            } else {
                seqNum.copy(this.m_maxAvailableSeqNum);
            }

            if (seqNum.isLowerThan(this.lastRemovedSeqNum)) {
                seqNum.copy(this.lastRemovedSeqNum);
                this.m_maxAvailableSeqNum.copy(this.lastRemovedSeqNum);
                this.hasMaxAvailableSeqNumChanged = false;
            }

            return seqNum;
        } finally {
            this.m_mutex.unlock();
        }
    }

    public List<ChangeFromWriter> getMissingChanges() {
        List<ChangeFromWriter> missing = new ArrayList<>();
        if (!this.changesFromWriter.isEmpty()) {
            this.m_mutex.lock();
            try {
                
                for (ChangeFromWriter it : this.changesFromWriter) {
                    if (it.status == ChangeFromWriterStatus.MISSING && it.isRelevant) {
                        missing.add(it);
                    }
                }
                if (missing.isEmpty()) {
                    this.isMissingChangesEmpty = true;
                    //printChangesFromWriterTest(); Debugging purposes (not implemented in Java version)
                }
                //return missing;
            } finally {
                this.m_mutex.unlock();
            }
        }
        return missing;
    }

    public void assertLiveliness() { // TODO Review this (whole liveliness behaviour)
        logger.debug("Liveliness asserted");
        this.m_isAlive = true;
        if (this.writerProxyLiveliness != null) {
//            if (this.writerProxyLiveliness.isWaiting()) {
//                this.writerProxyLiveliness.stopTimer();
//            }
            this.writerProxyLiveliness.restartTimer();
        }
    }

    public boolean receivedChangeSet(CacheChange change) {
        logger.debug("RTPS READER: {}: seqNum: {}", att.guid.getEntityId(), change.getSequenceNumber().toLong());
        m_mutex.lock();
        try {
            hasMaxAvailableSeqNumChanged = true;
            hasMinAvailableSeqNumChanged = true;
            if (!m_firstReceived || changesFromWriter.size() > 0) {
                addChangesFromWriterUpTo(change.getSequenceNumber());
                m_firstReceived = false;
            } else {
                ChangeFromWriter chfw = new ChangeFromWriter();
                chfw.setChange(change);
                chfw.status = ChangeFromWriterStatus.RECEIVED;
                chfw.isRelevant = true;
                changesFromWriter.add(chfw);
                m_firstReceived = false;
                printChangesFromWriterTest2();
                return true;
            }

            for (ListIterator<ChangeFromWriter> cit = changesFromWriter.listIterator(changesFromWriter.size()); cit.hasPrevious();) {
                final ChangeFromWriter chfw = cit.previous();
                if (chfw.seqNum.equals(change.getSequenceNumber())) {
                    chfw.setChange(change);
                    chfw.status = ChangeFromWriterStatus.RECEIVED;
                    printChangesFromWriterTest2();
                    return true;
                }
            }
            logger.error("RTPS READER: Something has gone wrong");
            return false;
        } finally {
            m_mutex.unlock();
        }
    }

    public boolean addChangesFromWriterUpTo(SequenceNumber seq) {
        final SequenceNumber firstSN = new SequenceNumber();
        if (changesFromWriter.isEmpty()) {
            if (lastRemovedSeqNum.toLong() > 0) {
                firstSN.copy(lastRemovedSeqNum);
            } else {
                firstSN.copy(seq.subtract(1));
            }
        } else {
            firstSN.copy(changesFromWriter.get(changesFromWriter.size() - 1).seqNum);
        }
        
        firstSN.increment();
        while (firstSN.isLowerOrEqualThan(seq)) {
            /*firstSN.increment();
            if (firstSN.isGreaterThan(seq)) {
                break;
            }*/
            ChangeFromWriter chw = new ChangeFromWriter();
            chw.seqNum.copy(firstSN);
            chw.status = ChangeFromWriterStatus.UNKNOWN;
            chw.isRelevant = true;
            logger.debug("RTPS READER: WP {} adding unknown changes up to: {}", att.guid, chw.seqNum.toLong());
            changesFromWriter.add(chw);
            firstSN.increment();
        }
        return true;
    }

    public void printChangesFromWriterTest2() {
        StringBuilder sb = new StringBuilder();

        sb.append(att.guid.getEntityId());
        sb.append(": ");

        for (ChangeFromWriter it : changesFromWriter) {
            sb.append(it.seqNum.toLong()).append("(").append(it.isValid()).append(",").append(it.status).append(")-");
        }

        logger.debug("RTPS READER: {}", sb.toString());
    }

    public CacheChange getChange(final SequenceNumber seq) {
        m_mutex.lock();
        try {
            for (ChangeFromWriter it : changesFromWriter) {
                if (it.seqNum.equals(seq) && it.isValid()) {
                    return it.getChange();
                }
            }
            return null;
        } finally {
            m_mutex.unlock();
        }
    }

    public boolean lostChangesUpdate(SequenceNumber seqNum) {
        logger.debug("{} up to seqNum {}", this.att.guid.getEntityId(), seqNum.toLong());
        this.m_mutex.lock();
        try {
            this.addChangesFromWriterUpTo(seqNum);
            for (ChangeFromWriter cit : this.changesFromWriter) {
                if (cit.status == ChangeFromWriterStatus.UNKNOWN || cit.status == ChangeFromWriterStatus.MISSING) {
                    if (cit.seqNum.isLowerThan(seqNum)) {
                        cit.status = ChangeFromWriterStatus.LOST;
                    }
                }
            }
            this.hasMaxAvailableSeqNumChanged = true;
            this.hasMinAvailableSeqNumChanged = true;
            printChangesFromWriterTest2();
        } finally {
            this.m_mutex.unlock();
        }
        return true;
    }

    public boolean missingChangesUpdate(SequenceNumber seqNum) {
        logger.debug("{} changes up to seqNum {}", this.att.guid.getEntityId(), seqNum.toLong());
        this.m_mutex.lock();
        try {
            this.addChangesFromWriterUpTo(seqNum);
            for (ChangeFromWriter cit : this.changesFromWriter) {
                if (cit.status == ChangeFromWriterStatus.MISSING) {
                    this.isMissingChangesEmpty = false;
                }
                if (cit.status == ChangeFromWriterStatus.UNKNOWN) {
                    if (cit.seqNum.isLowerOrEqualThan(seqNum)) {
                        cit.status = ChangeFromWriterStatus.MISSING;
                        this.isMissingChangesEmpty = false;
                    }
                }
            }
            this.hasMaxAvailableSeqNumChanged = true;
            this.hasMinAvailableSeqNumChanged = true;
            printChangesFromWriterTest2();
        } finally {
            this.m_mutex.unlock();
        }
        return true;
    }
    
    public void startHeartbeatResponse() {
        if (this.heartBeatResponse == null) {
            heartBeatResponse = new HeartbeatResponseDelay(this, statefulReader.getTimes().heartbeatResponseDelay.toMilliSecondsDouble());
        } else {
            this.heartBeatResponse.restartTimer();
        }
    }

}
