package org.fiware.kiara.ps.publisher;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.fiware.kiara.ps.rtps.attributes.RemoteWriterAttributes;
import org.fiware.kiara.ps.rtps.common.ChangeFromWriter;
import org.fiware.kiara.ps.rtps.common.ChangeFromWriterStatus;
import static org.fiware.kiara.ps.rtps.common.ChangeFromWriterStatus.LOST;
import static org.fiware.kiara.ps.rtps.common.ChangeFromWriterStatus.RECEIVED;
import static org.fiware.kiara.ps.rtps.common.ChangeFromWriterStatus.UNKNOWN;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.messages.elements.SequenceNumber;
import org.fiware.kiara.ps.rtps.reader.StatefulReader;
import org.fiware.kiara.ps.rtps.reader.timedevent.HeartbeatResponseDelay;
import org.fiware.kiara.ps.rtps.reader.timedevent.WriterProxyLiveliness;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;
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

    private boolean m_hasMaxAvailableSeqNumChanged;

    private boolean m_hasMinAvailableSeqNumChanged;

    private boolean m_isAlive;

    private boolean m_firstReceived;

    private static final Logger logger = LoggerFactory.getLogger(WriterProxy.class);

    private final Lock m_mutex = new ReentrantLock(true);

    public void destroy() {
        writerProxyLiveliness.stopTimer();
    }

    public WriterProxy(RemoteWriterAttributes watt, Timestamp heartbeatResponse, StatefulReader SR) {
        statefulReader = SR;
        att = new RemoteWriterAttributes();
        att.copy(watt);
        changesFromWriter.clear();
        //Create Events
        writerProxyLiveliness = new WriterProxyLiveliness(this, att.livelinessLeaseDuration.toMilliSecondsDouble());
        heartBeatResponse = new HeartbeatResponseDelay(this, statefulReader.getTimes().heartbeatResponseDelay.toMilliSecondsDouble());
        if (att.livelinessLeaseDuration.isLowerThan(new Timestamp().timeInfinite())) {
            writerProxyLiveliness.restartTimer();
        }
        logger.info("RTPS READER: Writer Proxy created in reader: {}", statefulReader.getGuid().getEntityId());
        lastRemovedSeqNum = new SequenceNumber();
        m_maxAvailableSeqNum = new SequenceNumber();
        m_minAvailableSeqNum = new SequenceNumber();
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

            if (m_hasMinAvailableSeqNumChanged) {
		//Order changesFromWriter
                //	std::sort(m_changesFromW.begin(),m_changesFromW.end(),sort_chFW);
                seqNum.setHigh(0);
                seqNum.setLow(0);
                for (ChangeFromWriter it : changesFromWriter) {
                    if (it.status == RECEIVED) {
                        seqNum.copy(it.seqNum);
                        m_minAvailableSeqNum.copy(it.seqNum);
                        m_hasMinAvailableSeqNumChanged = false;
                        return seqNum;
                    } else if (it.status == LOST) {
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
                m_hasMinAvailableSeqNumChanged = false;
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
            if (this.m_hasMaxAvailableSeqNumChanged) {
                seqNum.setHigh(0);
                seqNum.setLow(0);

                for (ChangeFromWriter it : this.changesFromWriter) {
                    if (it.status == ChangeFromWriterStatus.RECEIVED || it.status == ChangeFromWriterStatus.LOST) {
                        seqNum.copy(it.seqNum);
                        this.m_maxAvailableSeqNum.copy(it.seqNum);
                        this.m_hasMaxAvailableSeqNumChanged = false;
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
                List<ChangeFromWriter> missing = new ArrayList<>();
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

    public boolean receivedChangeSet(CacheChange change) {
        logger.info("RTPS READER: {}: seqNum: {}", att.guid.getEntityId(), change.getSequenceNumber().toLong());
        m_mutex.lock();
        try {
            m_hasMaxAvailableSeqNumChanged = true;
            m_hasMinAvailableSeqNumChanged = true;
            if (!m_firstReceived || changesFromWriter.size() > 0) {
                addChangesFromWriterUpTo(change.getSequenceNumber());
                m_firstReceived = false;
            } else {
                ChangeFromWriter chfw = new ChangeFromWriter();
                chfw.setChange(change);
                chfw.status = RECEIVED;
                chfw.isRelevant = true;
                changesFromWriter.add(chfw);
                m_firstReceived = false;
                printChangesFromWriterTest2();
                return true;
            }

            for (ListIterator<ChangeFromWriter> cit = changesFromWriter.listIterator(); cit.hasPrevious();) {
                final ChangeFromWriter chfw = cit.previous();
                if (chfw.seqNum.equals(change.getSequenceNumber())) {
                    chfw.setChange(change);
                    chfw.status = RECEIVED;
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
        while (true) {
            firstSN.increment();
            if (firstSN.isGreaterThan(seq)) {
                break;
            }
            ChangeFromWriter chw = new ChangeFromWriter();
            chw.seqNum.copy(firstSN);
            chw.status = UNKNOWN;
            chw.isRelevant = true;
            logger.info("RTPS READER: WP {} adding unknown changes up to: {}", att.guid, chw.seqNum.toLong());
            changesFromWriter.add(chw);
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

        logger.info("RTPS READER: {}", sb.toString());
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

}
