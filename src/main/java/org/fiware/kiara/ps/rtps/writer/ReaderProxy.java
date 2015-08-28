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
import org.fiware.kiara.ps.rtps.reader.StatelessReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>} 
 */
public class ReaderProxy {
    
    public RemoteReaderAttributes att;
    
    private StatefulWriter m_SFW;
    
    private List<ChangeForReader> m_changesForReader;
    
    public boolean isRequestedChangesEmpty;
    
    //private NackResponseRelay m_nackResponse;
    
    //private NackSupressionDuration m_nackSupression;
    
    private int m_lastAckNackCount;
    
    private final Lock m_mutex = new ReentrantLock(true);
    
    private static final Logger logger = LoggerFactory.getLogger(ReaderProxy.class);
    
    public ReaderProxy(RemoteReaderAttributes rdata, WriterTimes times, StatefulWriter sw) {
        this.m_changesForReader = new ArrayList<ChangeForReader>();
        this.att = rdata;
        this.m_SFW = sw;
        this.isRequestedChangesEmpty = true;
        //this.m_nackResponse = null;
        //this.m_nackSupression = null;
        this.m_lastAckNackCount = 0;
    }
    
    public void destroy() {
        this.m_changesForReader.clear();
    }
    
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
    
    public boolean ackedChangesSet(SequenceNumber seq) {
        this.m_mutex.lock();
        try {
            for (ChangeForReader it : this.m_changesForReader) {
                if (it.getSequenceNumber().isLowerThan(seq)) {
                    it.status = ChangeForReaderStatus.ACKNOWLEDGED;
                    logger.debug("Change {} set to ACKNOWLEDGED", seq.toLong());
                }
            }
        } finally {
            this.m_mutex.unlock();
        }
        return true;
    }
    
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
    
    public List<ChangeForReader> requestedChanges() { // TODO Check this is used properly. Original code returns boolean.
        return changesList(ChangeForReaderStatus.REQUESTED);
    }
    
    public List<ChangeForReader> unsentChanges() { // TODO Check this is used properly. Original code returns boolean.
        return changesList(ChangeForReaderStatus.UNSENT);
    }
    
    public List<ChangeForReader> unackedChanges() { // TODO Check this is used properly. Original code returns boolean.
        return changesList(ChangeForReaderStatus.UNACKNOWLEDGED);
    }

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
    
    public StatefulWriter getSFW() {
        return this.m_SFW;
    }

    public Lock getMutex() {
        return this.m_mutex;
    }

}
