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
package org.fiware.kiara.ps.rtps.writer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.fiware.kiara.ps.rtps.attributes.RemoteReaderAttributes;
import org.fiware.kiara.ps.rtps.attributes.WriterTimes;
import org.fiware.kiara.ps.rtps.common.ChangeForReader;
import org.fiware.kiara.ps.rtps.common.ChangeForReaderStatus;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.messages.elements.SequenceNumber;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;
import org.fiware.kiara.ps.rtps.reader.RTPSReader;
import org.fiware.kiara.ps.rtps.writer.timedevent.NackResponseDelay;
import org.fiware.kiara.ps.rtps.writer.timedevent.NackSupressionDuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class represents an {@link RTPSReader} proxy
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>} 
 */
public class ReaderProxy {
    
    /**
     * Attributes of the {@link ReaderProxy}
     */
    public RemoteReaderAttributes att;
    
    /**
     * Associated {@link StatefulWriter}
     */
    private StatefulWriter m_SFW;
    
    /**
     * List of {@link CacheChange} references for the reader
     */
    private List<ChangeForReader> m_changesForReader;
    
    /**
     * Indicates if there are requested {@link CacheChange}s
     */
    public boolean isRequestedChangesEmpty;
    
    /**
     * {@link NackResponseDelay} reference
     */
    private NackResponseDelay m_nackResponse;
    
    /**
     * {@link NackSupressionDuration} reference
     */
    private NackSupressionDuration m_nackSupression;
    
    /**
     * {@link Timestamp} for the {@link NackSupressionDuration}
     */
    private Timestamp m_nackSupressionTimestamp;
    
    /**
     * {@link Timestamp} for the {@link NackResponseDelay}
     */
    private Timestamp m_nackResponseTimestamp;
    
    /**
     * Sent ACKNACK count
     */
    private int m_lastAckNackCount;
    
    /**
     * Mutex
     */
    private final Lock m_mutex = new ReentrantLock(true);
    
    /**
     * Logging object
     */
    private static final Logger logger = LoggerFactory.getLogger(ReaderProxy.class);
    
    /**
     * {@link ReaderProxy} constructor
     * 
     * @param rdata Attributes of the remore {@link RTPSReader}
     * @param times Objec containing the {@link Timestamp} information
     * @param sw Reference to the {@link StatefulWriter}
     */
    public ReaderProxy(RemoteReaderAttributes rdata, WriterTimes times, StatefulWriter sw) {
        this.m_changesForReader = new ArrayList<ChangeForReader>();
        this.att = rdata;
        this.m_SFW = sw;
        this.isRequestedChangesEmpty = true;
        this.m_nackSupressionTimestamp = new Timestamp(times.nackSupressionDuration);
        this.m_nackResponseTimestamp = new Timestamp(times.nackResponseDelay);
        this.m_lastAckNackCount = 0;
    }
    
    /**
     * Destroys the {@link ReaderProxy}
     */
    public void destroy() {
        this.m_changesForReader.clear();
    }
    
    /**
     * Creates a {@link ChangeForReader} from a {@link CacheChange} object
     * 
     * @param change The {@link CacheChange} reference
     * @return The new {@link ChangeForReader}
     */
    public ChangeForReader getChangeForReader(CacheChange change) {
        ChangeForReader retVal = null;
        this.m_mutex.lock();
        try {
            for (ChangeForReader it : this.m_changesForReader) {
                if (it.getSequenceNumber().equals(change.getSequenceNumber())) {
                    retVal = new ChangeForReader();
                    retVal.copy(it);
                    logger.debug("Change {} found in Reader Proxy", change.getSequenceNumber().toLong());
                    break;
                }
            }
        } finally {
            this.m_mutex.unlock();
        }
        return retVal;
    }
    
    /**
     * Get a new {@link ChangeForReader} associated to a {@link SequenceNumber}
     * 
     * @param seq The {@link SequenceNumber} to search
     * @return The associated {@link ChangeForReader}
     */
    public ChangeForReader getChangeForReader(SequenceNumber seq) {
        ChangeForReader retVal = null;
        this.m_mutex.lock();
        try {
            for (ChangeForReader it : this.m_changesForReader) {
                if (it.getSequenceNumber().equals(seq)) {
                    retVal = new ChangeForReader();
                    retVal.copy(it);
                    logger.debug("Change {} found in Reader Proxy", seq.toLong());
                    break;
                }
            }
        } finally {
            this.m_mutex.unlock();
        }
        return retVal;
    }
    
    /**
     * Sets the changes below the provided {@link SequenceNumber} to ACKNOWLEDGED
     * 
     * @param seq The reference {@link SequenceNumber}
     * @return true on success; false otherwise
     */
    public boolean ackedChangesSet(SequenceNumber seq) {
        this.m_mutex.lock();
        try {
            for (ChangeForReader it : this.m_changesForReader) {
                if (it.getSequenceNumber().isLowerThan(seq)) {
                    it.status = ChangeForReaderStatus.ACKNOWLEDGED;
                    logger.debug("Change {} set to ACKNOWLEDGED", it.seqNum);
                }
            }
        } finally {
            this.m_mutex.unlock();
        }
        return true;
    }
    
    /**
     * Sets the changes below the provided {@link SequenceNumber} to REQUESTED
     * 
     * @param seqNumSet The reference {@link SequenceNumber}
     * @return true on success; false otherwise
     */
    public boolean requestedChangesSet(List<SequenceNumber> seqNumSet) {
        this.m_mutex.lock();
        try {
            for (SequenceNumber sit : seqNumSet) {
                for (ChangeForReader it : this.m_changesForReader) {
                    if (it.getSequenceNumber().equals(sit)) {
                        it.status = ChangeForReaderStatus.REQUESTED;
                        this.isRequestedChangesEmpty = false;
                        break;
                    }
                }
            }
            if (!this.isRequestedChangesEmpty) {
                logger.debug("Requested Changes: {}", seqNumSet);
            }
        } finally {
            this.m_mutex.unlock();
        }
        return true;
    }
    
    /**
     * Get the list of requested {@link ChangeForReader} references
     * 
     * @return The list of requested {@link ChangeForReader} references
     */
    public List<ChangeForReader> requestedChanges() {
        return changesList(ChangeForReaderStatus.REQUESTED);
    }
    
    /**
     * Get the list of unsent {@link ChangeForReader} references
     * 
     * @return The list of unsent {@link ChangeForReader} references
     */
    public List<ChangeForReader> unsentChanges() {
        return changesList(ChangeForReaderStatus.UNSENT);
    }
    
    /**
     * Get the list of unacked {@link ChangeForReader} references
     * 
     * @return The list of unacked {@link ChangeForReader} references
     */
    public List<ChangeForReader> unackedChanges() {
        return changesList(ChangeForReaderStatus.UNACKNOWLEDGED);
    }

    /**
     * Get a list of {@link ChangeForReader} objects whose status matches
     * the provided {@link ChangeForReaderStatus}
     * 
     * @param status The {@link ChangeForReaderStatus} to compare
     * @return The List of matched {@link ChangeForReader} references
     */
    private List<ChangeForReader> changesList(ChangeForReaderStatus status) {
        ArrayList<ChangeForReader> changes = new ArrayList<ChangeForReader>();
        this.m_mutex.lock();
        try {
            for (ChangeForReader it : this.m_changesForReader) {
                if (it.status == status) {
                    ChangeForReader change = new ChangeForReader();
                    change.copy(it);
                    changes.add(change);
                }
            }
        } finally {
            this.m_mutex.unlock();
        }
        return changes;
    }
    
    /**
     * Get the next requested {@link ChangeForReader}
     * 
     * @return The next requested {@link ChangeForReader}
     */
    public ChangeForReader nextRequestedChange() {
        ChangeForReader retVal = null;
        this.m_mutex.lock();
        try {
            List<ChangeForReader> changes = requestedChanges();
            if (changes.size() != 0) {
                return minChange(changes);
            }
        } finally {
            this.m_mutex.unlock();
        }
        return retVal;
    }
    
    /**
     * Get the next unsent {@link ChangeForReader}
     * 
     * @return The next unsent {@link ChangeForReader}
     */
    public ChangeForReader nextUnsentChange() {
        ChangeForReader retVal = null;
        this.m_mutex.lock();
        try {
            List<ChangeForReader> changes = unsentChanges();
            if (changes.size() != 0) {
                return minChange(changes);
            }
        } finally {
            this.m_mutex.unlock();
        }
        return retVal;
    }
    
    /**
     * Get the maximum acked {@link SequenceNumber}
     * 
     * @return The next acked {@link SequenceNumber}
     */
    public SequenceNumber maxAckedChange() {
        SequenceNumber sn = null;
        if (!this.m_changesForReader.isEmpty()) {
            sn = new SequenceNumber();
            for (ChangeForReader it : this.m_changesForReader) {
                if (it.status != ChangeForReaderStatus.ACKNOWLEDGED) {
                    sn.copy(it.getSequenceNumber());
                    sn.decrement();
                    return sn;
                }
            }
            sn.copy(this.m_changesForReader.get(this.m_changesForReader.size()-1).getSequenceNumber());
            sn.decrement();
        }
        return sn;
    }
    
    /**
     * Get the mimimum {@link ChangeForReader} from a specified list
     * 
     * @param changes The {@link ChangeForReader} list to check
     * @return The mimimum {@link ChangeForReader}
     */
    private ChangeForReader minChange(List<ChangeForReader> changes) {
        this.m_mutex.lock();
        try {
            int index = -1;
            if (changes.size() != 0) {
                for (int i=0; i < changes.size()-1; ++i) {
                    if (changes.get(i).getSequenceNumber().isLowerThan(changes.get(i+1).getSequenceNumber())) {
                        index = i;
                    }
                }
            }
            
            if (index != -1) {
                ChangeForReader retVal = new ChangeForReader();
                retVal.copy(changes.get(index));
                return retVal;
            }
            
        } finally {
            this.m_mutex.unlock();
        }
        
        return null;
    }
    
    /**
     * Get the {@link StatefulWriter}
     * 
     * @return The {@link StatefulWriter}
     */
    public StatefulWriter getSFW() {
        return this.m_SFW;
    }

    /**
     * Get the {@link Lock} mutex
     * 
     * @return The {@link Lock} mutex
     */
    public Lock getMutex() {
        return this.m_mutex;
    }
    
    /**
     * Get the list of {@link ChangeForReader} references
     * 
     * @return The list of {@link ChangeForReader} references
     */
    public List<ChangeForReader> getChangesForReader() {
        return this.m_changesForReader;
    }
    
    /**
     * Checks if the {@link CacheChange} is relevant
     * 
     * @param change The {@link CacheChange} to check
     * @return true if relevant; false otherwise
     */
    public boolean rtpsChangeIsRelevant(CacheChange change) {
        return true; // In this version, always returns true
    }
    
    /**
     * Get the {@link NackResponseDelay}
     * 
     * @return The {@link NackResponseDelay}
     */
    public NackResponseDelay getNackResponseDelay() {
        return this.m_nackResponse;
    }
    
    /**
     * Get the {@link NackSupressionDuration}
     * 
     * @return The {@link NackSupressionDuration}
     */
    public NackSupressionDuration getNackSupression() {
        return this.m_nackSupression;
    }

    /**
     * Starts the {@link NackSupressionDuration} thread
     */
    public void startNackSupression() {
        if (this.m_nackSupression == null) {
            this.m_nackSupression = new NackSupressionDuration(this, this.m_nackSupressionTimestamp.toMilliSecondsDouble());
        }
    }
    
    /**
     * Starts the {@link NackResponseDelay} thread
     */
    public void startNackResponseDelay() {
        if (this.m_nackResponse == null) {
            this.m_nackResponse = new NackResponseDelay(this, this.m_nackResponseTimestamp.toMilliSecondsDouble());
        } else {
            this.m_nackResponse.restartTimer();
        }
    }
    
    /**
     * Get the last ACKNACK count registered
     * 
     * @return The last ACKNACK count registered
     */
    public int getLastAcknackCount() {
        return this.m_lastAckNackCount;
    }
    
    /**
     * Set the last ACKNACK count registered
     * 
     * @param count The last ACKNACK count to be set
     * 
     */
    public void setLastAcknackCount(int count) {
        this.m_lastAckNackCount = count;
    }

}
