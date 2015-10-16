/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2015 Proyectos y Sistemas de Mantenimiento S.L. (eProsima)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fiware.kiara.ps.rtps.reader;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.fiware.kiara.ps.rtps.attributes.RemoteWriterAttributes;
import org.fiware.kiara.ps.rtps.common.ChangeFromWriter;
import org.fiware.kiara.ps.rtps.common.ChangeFromWriterStatus;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.messages.elements.SequenceNumber;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;
import org.fiware.kiara.ps.rtps.reader.timedevent.HeartbeatResponseDelay;
import org.fiware.kiara.ps.rtps.reader.timedevent.WriterProxyLiveliness;
import org.fiware.kiara.ps.rtps.writer.RTPSWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents a remote {@link RTPSWriter}
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public class WriterProxy {

    /**
     * Reference to the {@link StatefulReader}
     */
    public StatefulReader statefulReader;

    /**
     * {@link RemoteWriterAttributes} of the remote {@link RTPSWriter}
     */
    public RemoteWriterAttributes att;

    /**
     * List of received {@link CacheChange}s
     */
    public List<ChangeFromWriter> changesFromWriter;

    /**
     * Counter of the ACKNACK messages
     */
    public int acknackCount;

    /**
     * Count od the last HEARTBEAT received
     */
    public int lastHeartbeatCount;

    /**
     * Indicates if the missing {@link CacheChange} list is empty or not
     */
    public boolean isMissingChangesEmpty;

    /**
     * Reference to the {@link HeartbeatResponseDelay}
     */
    public HeartbeatResponseDelay heartBeatResponse;

    /**
     * Reference to the {@link WriterProxyLiveliness}
     */
    public WriterProxyLiveliness writerProxyLiveliness;

    /**
     * Indicates if the HEARTBEAT is activated
     */
    public boolean hearbeatFinalFlag;

    /**
     * Last removed {@link SequenceNumber}
     */
    public final SequenceNumber lastRemovedSeqNum;

    /**
     * Maximum available {@link SequenceNumber}
     */
    private final SequenceNumber m_maxAvailableSeqNum;

    /**
     * minimum available {@link SequenceNumber}
     */
    private final SequenceNumber m_minAvailableSeqNum;

    /**
     * Indicates if the maximum {@link SequenceNumber} has changed
     */
    public boolean hasMaxAvailableSeqNumChanged;

    /**
     * Indicates if the minimum {@link SequenceNumber} has changed
     */
    public boolean hasMinAvailableSeqNumChanged;

    /**
     * Indicates if the {@link WriterProxy} is still alive
     */
    @SuppressWarnings("unused")
    private boolean m_isAlive;

    /**
     * Indicates if no more {@link CacheChange}s have been received after the first one
     */
    private boolean m_firstReceived;

    /**
     * Logging object
     */
    private static final Logger logger = LoggerFactory.getLogger(WriterProxy.class);

    /**
     * Liveliness multiplier
     */
    public static final int WRITERPROXY_LIVELINESS_PERIOD_MULTIPLIER = 1; // TODO Check in spec if this must be allowed to change

    /**
     * Mutex
     */
    private final Lock m_mutex = new ReentrantLock(true);

    /**
     * Destroys the {@link WriterProxy}
     */
    public void destroy() {
        this.m_mutex.lock();
        try {
            if (writerProxyLiveliness != null) {
                writerProxyLiveliness.stopTimer();
            }
            if (this.heartBeatResponse != null) {
                this.heartBeatResponse.stopTimer();
            }
        } finally {
            this.m_mutex.unlock();
        }
    }

    /**
     * {@link WriterProxy} constructor
     * 
     * @param watt The {@link RemoteWriterAttributes} for configuration
     * @param heartbeatResponse {@link Timestamp} indicating the HEARTBEAT response period
     * @param SR Reference to a {@link StatefulReader}
     */
    public WriterProxy(RemoteWriterAttributes watt, Timestamp heartbeatResponse, StatefulReader SR) {
        statefulReader = SR;
        att = new RemoteWriterAttributes();
        att.copy(watt);
        this.changesFromWriter = new ArrayList<ChangeFromWriter>();
        this.m_firstReceived = true;
        if (att.livelinessLeaseDuration.isLowerThan(new Timestamp().timeInfinite())) {
            writerProxyLiveliness = new WriterProxyLiveliness(this, att.livelinessLeaseDuration.toMilliSecondsDouble()*WRITERPROXY_LIVELINESS_PERIOD_MULTIPLIER);
        }
        lastRemovedSeqNum = new SequenceNumber();
        m_maxAvailableSeqNum = new SequenceNumber();
        m_minAvailableSeqNum = new SequenceNumber();
        logger.debug("RTPS READER: Writer Proxy created in reader: {}", statefulReader.getGuid().getEntityId());
    }

    /**
     * Get te minimum available changes
     * 
     * @return The minimum {@link SequenceNumber}
     */
    public SequenceNumber getAvailableChangesMin() {
        return getAvailableChangesMin(null);
    }

    /**
     * Get te minimum available changes starting from the provided {@link SequenceNumber}
     * 
     * @param seqNum The {@link SequenceNumber} to start with
     * @return The minimum {@link SequenceNumber}
     */
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

    /**
     * Get the maximum available changes
     * 
     * @return The maximum {@link SequenceNumber}
     */
    public SequenceNumber getAvailableChangesMax() {
        return getAvailableChangesMax(null);
    }

    /**
     * Get te maximum available changes starting from the provided {@link SequenceNumber}
     * 
     * @param seqNum The {@link SequenceNumber} to start with
     * @return The maximum {@link SequenceNumber}
     */
    public SequenceNumber getAvailableChangesMax(SequenceNumber seqNum) {
        this.m_mutex.lock();
        try {
            if (this.lastRemovedSeqNum.toLong() <= 0 && this.changesFromWriter.isEmpty()) { // Nothing received
                return null;
            }
            if (seqNum == null) {
                seqNum = new SequenceNumber();
            }
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

    /**
     * Get a list of missing {@link ChangeFromWriter}
     * 
     * @return A list of missing {@link ChangeFromWriter}
     */
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

    /**
     * Asserts the {@link WriterProxy} liveliness
     */
    public void assertLiveliness() {
        logger.debug("Liveliness asserted");
        this.m_isAlive = true;
        if (this.writerProxyLiveliness != null) {
            this.writerProxyLiveliness.stopTimer();
            this.writerProxyLiveliness.restartTimer();
        }
    }

    /**
     * Adds a {@link ChangeFromWriter} containing the provided {@link CacheChange}
     * 
     * @param change The {@link CacheChange} to add
     * @return true on success; false otherwise
     */
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

    /**
     * Adds changes from the {@link RTPSWriter} up to a specified {@link SequenceNumber}
     * 
     * @param seq The {@link SequenceNumber} to compare
     * @return true on success; false otherwise
     */
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

    /**
     * Prints changes
     */
    public void printChangesFromWriterTest2() {
        StringBuilder sb = new StringBuilder();

        sb.append(att.guid.getEntityId());
        sb.append(": ");

        for (ChangeFromWriter it : changesFromWriter) {
            sb.append(it.seqNum.toLong()).append("(").append(it.isValid()).append(",").append(it.status).append(")-");
        }

        logger.debug("RTPS READER: {}", sb.toString());
    }

    /**
     * Get the {@link CacheChange} that matches the provided {@link SequenceNumber}
     * 
     * @param seq The {@link SequenceNumber} to compare
     * @return The found {@link CacheChange}; null otherwise
     */
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

    /**
     * Updates the lost changes using the provided {@link SequenceNumber}
     * 
     * @param seqNum The {@link SequenceNumber} to compare
     * @return true on success; false otherwise
     */
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

    /**
     * Updates the missing changes using the provided {@link SequenceNumber}
     * 
     * @param seqNum The {@link SequenceNumber} to compare
     * @return true on success; false otherwise
     */
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

    /**
     * Starts the HEARTBEAT response thread
     */
    public void startHeartbeatResponse() {
        if (this.heartBeatResponse == null) {
            heartBeatResponse = new HeartbeatResponseDelay(this, statefulReader.getTimes().heartbeatResponseDelay.toMilliSecondsDouble());
        } else {
            this.heartBeatResponse.restartTimer();
        }
    }

    /**
     * Get the mutex {@link Lock} object
     * 
     * @return The {@link Lock} mutex
     */
    public Lock getMutex() {
        return this.m_mutex;
    }

    /**
     * Sets a {@link SequenceNumber} as irrelevant
     * 
     * @param seq The {@link SequenceNumber} to be set as irrelevant
     */
    public boolean irrelevantChangeSet(SequenceNumber seq) {
        this.m_mutex.lock();
        try {
            this.hasMinAvailableSeqNumChanged = true;
            this.hasMaxAvailableSeqNumChanged = true;
            this.addChangesFromWriterUpTo(seq);
            ListIterator<ChangeFromWriter> it = this.changesFromWriter.listIterator(this.changesFromWriter.size());
            while (it.hasPrevious()) {
                ChangeFromWriter current = it.previous();
                if (current.seqNum.equals(seq)) {
                    current.status = ChangeFromWriterStatus.RECEIVED;
                    current.isRelevant = false;
                    return true;
                }
            }
            logger.error("Something went wrong");
        } finally {
            this.m_mutex.unlock();
        }
        return false; 
        
    }

}
