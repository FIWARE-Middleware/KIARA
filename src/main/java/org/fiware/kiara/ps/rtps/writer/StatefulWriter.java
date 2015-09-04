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

import org.fiware.kiara.ps.rtps.attributes.RemoteReaderAttributes;
import org.fiware.kiara.ps.rtps.attributes.WriterAttributes;
import org.fiware.kiara.ps.rtps.attributes.WriterTimes;
import org.fiware.kiara.ps.rtps.common.ChangeForReader;
import org.fiware.kiara.ps.rtps.common.ChangeForReaderStatus;
import org.fiware.kiara.ps.rtps.common.DurabilityKind;
import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.common.LocatorList;
import org.fiware.kiara.ps.rtps.common.ReliabilityKind;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.history.WriterHistoryCache;
import org.fiware.kiara.ps.rtps.messages.RTPSMessage;
import org.fiware.kiara.ps.rtps.messages.RTPSMessageBuilder;
import org.fiware.kiara.ps.rtps.messages.RTPSMessageGroup;
import org.fiware.kiara.ps.rtps.messages.elements.Count;
import org.fiware.kiara.ps.rtps.messages.elements.EntityId;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.SequenceNumber;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.writer.timedevent.PeriodicHeartbeat;
import org.fiware.kiara.ps.rtps.writer.timedevent.UnsentChangesNotEmptyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class StatefulWriter extends RTPSWriter {

    private static final Logger logger = LoggerFactory.getLogger(RTPSWriter.class);

    /**
     * Count of the sent heartbeats.
     */
    private final Count m_heartbeatCount;

    /**
     * Timed Event to manage the periodic HB to the Reader.
     */
    private PeriodicHeartbeat m_periodicHB;

    private final WriterTimes m_times;

    /**
     * Vector containing all the associated ReaderProxies.
     */
    private final List<ReaderProxy> m_matchedReaders;

    /**
     * EntityId used to send the HB.(only for builtin types performance)
     */
    private final EntityId m_HBReaderEntityId;

    public StatefulWriter(RTPSParticipant participant, GUID guid,
            WriterAttributes att, WriterHistoryCache history,
            WriterListener listener) {
        super(participant, guid, att, history, listener);
        m_heartbeatCount = new Count(0);
        m_periodicHB = null;
        m_times = new WriterTimes(att.times);
        m_matchedReaders = new ArrayList<>();
        if (guid.getEntityId().isSEDPPubWriter()) {
            m_HBReaderEntityId = EntityId.createSEDPPubReader();
        } else if (guid.getEntityId().isSEDPSubWriter()) {
            m_HBReaderEntityId = EntityId.createSEDPSubReader();
        } else if (guid.getEntityId().isWriterLiveliness()) {
            m_HBReaderEntityId = EntityId.createReaderLiveliness();
        } else {
            m_HBReaderEntityId = EntityId.createUnknown();
        }
    }

    public void destroy() {
        logger.info("RTPS WRITER: StatefulWriter destructor");
        if (m_periodicHB != null)
            m_periodicHB.destroy();
	for (ReaderProxy it : m_matchedReaders) {
            it.destroy();
	}
        m_matchedReaders.clear();
    }

    public List<ReaderProxy> getMatchedReaders() {
        return m_matchedReaders;
    }
    
    /*
     * CHANGE-RELATED METHODS
     */
    
    @Override
    public void unsentChangeAddedToHistory(CacheChange change) {

        this.m_mutex.lock();
        try {
            LocatorList uniLocList = new LocatorList();
            LocatorList multiLocList = new LocatorList();
            
            List<CacheChange> changeV = new ArrayList<CacheChange>();
            changeV.add(change);
            
            boolean expectsInlineQos = false;
            this.setLivelinessAsserted(true);
            if (!this.m_matchedReaders.isEmpty()) {
                for (ReaderProxy it : this.m_matchedReaders) {
                    ChangeForReader changeForReader = new ChangeForReader();
                    changeForReader.setChange(change);
                    if (this.m_pushMode) {
                        changeForReader.status = ChangeForReaderStatus.UNDERWAY;
                    } else {
                        changeForReader.status = ChangeForReaderStatus.UNACKNOWLEDGED;
                    }
                    changeForReader.isRelevant = it.rtpsChangeIsRelevant(change);
                    it.getChangesForReader().add(changeForReader);
                    uniLocList.pushBack(it.att.endpoint.unicastLocatorList);
                    multiLocList.pushBack(it.att.endpoint.multicastLocatorList);
                    //it.getNackSupression().restartTimer();
                    it.startNackSupression();
                }
                RTPSMessageGroup.sendChangesAsData(this, changeV, uniLocList, multiLocList, expectsInlineQos, EntityId.createUnknown());
                if (this.m_periodicHB == null) { // TODO Review this
                    this.m_periodicHB = new PeriodicHeartbeat(this, this.m_times.heartBeatPeriod.toMilliSecondsDouble());
                } else {
                    this.m_periodicHB.restartTimer();
                }
            } else {
                logger.info("No reader proxy to add change.");
            }
        } finally {
            this.m_mutex.unlock();
        }
        
    }

    @Override
    public boolean changeRemovedByHistory(CacheChange change) {
        this.m_mutex.lock();
        try {
            logger.info("Change {} to be removed", change.getSequenceNumber().toLong());
            for (ReaderProxy it : this.m_matchedReaders) {
                for (ChangeForReader chit : it.getChangesForReader()) {
                    if (chit.getSequenceNumber().equals(change.getSequenceNumber())) {
                        chit.notValid();
                        break;
                    }
                }
            }
        } finally {
            this.m_mutex.unlock();
        }
        return true;
    }
    
    @Override
    public void unsentChangesNotEmpty() {
        this.m_mutex.lock();
        try {
            for (ReaderProxy rit : this.m_matchedReaders) {
                Lock guard = rit.getMutex();
                guard.lock();
                try {
                    List<ChangeForReader> chVec = rit.unsentChanges();
                    if (!chVec.isEmpty()) {
                        List<CacheChange> relevantChanges = new ArrayList<CacheChange>();
                        List<SequenceNumber> notRelevantChanges = new ArrayList<SequenceNumber>();
                        for (ChangeForReader cit : chVec) {
                            cit.status = ChangeForReaderStatus.UNDERWAY;
                            if (cit.isRelevant && cit.isValid()) {
                                relevantChanges.add(cit.getChange());
                            } else {
                                notRelevantChanges.add(cit.getSequenceNumber());
                            }
                        }
                        if (this.m_pushMode) {
                            if (!relevantChanges.isEmpty()) {
                                RTPSMessageGroup.sendChangesAsData(
                                        this, 
                                        relevantChanges, 
                                        rit.att.endpoint.unicastLocatorList,
                                        rit.att.endpoint.multicastLocatorList, 
                                        rit.att.expectsInlineQos, 
                                        rit.att.guid.getEntityId());
                                
                            }
                            if (!notRelevantChanges.isEmpty()) {
                                RTPSMessageGroup.sendChangesAsGap(
                                        this, 
                                        notRelevantChanges, 
                                        rit.att.guid.getEntityId(), 
                                        rit.att.endpoint.unicastLocatorList, 
                                        rit.att.endpoint.multicastLocatorList);
                            }
                            if (rit.att.endpoint.reliabilityKind == ReliabilityKind.RELIABLE) {
                                //this.m_periodicHB.restartTimer();
                                if (this.m_periodicHB == null) { // TODO Review this
                                    this.m_periodicHB = new PeriodicHeartbeat(this, this.m_times.heartBeatPeriod.toMilliSecondsDouble());
                                } else {
                                    this.m_periodicHB.restartTimer();
                                }
                            }
                            rit.getNackSupression().restartTimer();
                        } else {
                            CacheChange first = this.m_history.getMinChange();
                            CacheChange last = this.m_history.getMaxChange();
                            if (first != null && last != null) {
                                if (first.getSequenceNumber().toLong() > 0 && last.getSequenceNumber().toLong() > first.getSequenceNumber().toLong()) {
                                    this.incrementHBCount();
                                    RTPSMessage msg = RTPSMessageBuilder.createMessage();
                                    RTPSMessageBuilder.addSubmessageHeartbeat(
                                            msg, 
                                            EntityId.createUnknown(), 
                                            this.m_guid.getEntityId(), 
                                            first.getSequenceNumber(), 
                                            last.getSequenceNumber(), 
                                            this.m_heartbeatCount, 
                                            true, 
                                            false);
                                    for (Locator lit : this.m_att.unicastLocatorList) {
                                        this.m_participant.sendSync(msg, lit);
                                    }
                                    for (Locator lit : this.m_att.multicastLocatorList) {
                                        this.m_participant.sendSync(msg, lit);
                                    }
                                }
                            }
                        }
                    }
                } finally {
                    guard.unlock();
                }
            }
        } finally {
            this.m_mutex.unlock();
        }
        logger.info("Finished sending unsent changes");
    }
    
    /*
     * MATCHED_READER-RELATED METHODS
     */

    @Override
    public boolean matchedReaderAdd(RemoteReaderAttributes ratt) {
        this.m_mutex.lock();
        try {
            if (ratt.guid.equals(new GUID())) {
                logger.error("Reliable Writer need GUID of matched readers");
                return false;
            }
            for (ReaderProxy it : this.m_matchedReaders) {
                if (it.att.guid.equals(ratt.guid)) {
                    logger.info("Attempting to add existing reader");
                    return false;
                }
            }
            ReaderProxy rp = new ReaderProxy(ratt, this.m_times, this);
            /*if (this.m_periodicHB == null) {
                this.m_periodicHB = new PeriodicHeartbeat(this, this.m_times.heartBeatPeriod.toMilliSecondsDouble());
            }*/
            if (rp.att.endpoint.durabilityKind == DurabilityKind.TRANSIENT_LOCAL) {
                for (CacheChange cit : this.m_history.getChanges()) {
                    ChangeForReader changeForReader = new ChangeForReader();
                    changeForReader.setChange(cit);
                    changeForReader.isRelevant = rp.rtpsChangeIsRelevant(cit);
                    if (this.m_pushMode) {
                        changeForReader.status = ChangeForReaderStatus.UNSENT;
                    } else {
                        changeForReader.status = ChangeForReaderStatus.UNACKNOWLEDGED;
                    }
                    rp.getChangesForReader().add(changeForReader);
                }
            }
            this.m_matchedReaders.add(rp);
            logger.info(
                    "Reader Proxy {} added to {} with {}(u) - {}(m) locators", 
                    rp.att.guid, 
                    this.m_guid, 
                    rp.att.endpoint.unicastLocatorList.getLocators().size(),
                    rp.att.endpoint.multicastLocatorList.getLocators().size());
            if (rp.getChangesForReader().size() > 0) {
                this.m_unsentChangesNotEmpty = new UnsentChangesNotEmptyEvent(this, 1000);
                //this.m_unsentChangesNotEmpty.restartTimer();
                //this.m_unsentChangesNotEmpty = null;
            }
        } finally {
            this.m_mutex.unlock();
        }
        return true;
    }

    @Override
    public boolean matchedReaderRemove(RemoteReaderAttributes ratt) {
        this.m_mutex.lock();
        try {
            for (int i=0; i < this.m_matchedReaders.size(); ++i) {
                ReaderProxy it = this.m_matchedReaders.get(i);
                if (it.att.guid.equals(ratt.guid)) {
                    this.m_matchedReaders.remove(it);
                    i--;
                    if (this.m_matchedReaders.size() == 0) {
                        this.m_periodicHB.stopTimer();
                    }
                    return true;
                }
            }
        } finally {
            this.m_mutex.unlock();
        }
        return false;
    }

    @Override
    public boolean matchedReaderIsMatched(RemoteReaderAttributes ratt) {
        this.m_mutex.lock();
        try {
            for (ReaderProxy it : this.m_matchedReaders) {
                if (it.att.guid.equals(ratt.guid)) {
                    return true;
                }
            }
        } finally {
            this.m_mutex.unlock();
        }
        return false;
    }

    public ReaderProxy matchedReaderLookup(GUID readerGUID) {
        this.m_mutex.lock();
        try {
            for (ReaderProxy it : this.m_matchedReaders) {
                if (it.att.guid.equals(readerGUID)) {
                    return it; // TODO Check if this should be a copy
                }
            }
        } finally {
            this.m_mutex.unlock();
        }
        return null;
    }
    
    public boolean isAckedByAll(CacheChange change) {
        if (!change.getWriterGUID().equals(this.m_guid)) {
            logger.warn("The given change is not from this writer");
            return false;
        }
        for (ReaderProxy it : this.m_matchedReaders) {
            ChangeForReader changeForReader = it.getChangeForReader(change);
            if (changeForReader != null) {
                if (changeForReader.isRelevant) {
                    if (changeForReader.status == ChangeForReaderStatus.ACKNOWLEDGED) {
                        logger.info("Change not acked. Relevant: {}; status: ", changeForReader.isRelevant, changeForReader.status);
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    /*
     * PARAMETER-RELATED METHODS
     */
    
    @Override
    public void updateAttributes(WriterAttributes att) {
        this.updateTimes(att.times);
    }
    
    public void updateTimes(WriterTimes times) {
        if (!this.m_times.heartBeatPeriod.equals(times.heartBeatPeriod)) {
            this.m_periodicHB.updateInterval(times.heartBeatPeriod);
        }
        if (!this.m_times.nackResponseDelay.equals(times.nackResponseDelay)) {
            for (ReaderProxy it : this.m_matchedReaders) {
                it.getNackResponseDelay().updateInterval(times.nackResponseDelay);
            }
        }
        if (!this.m_times.nackSupressionDuration.equals(times.nackSupressionDuration)) {
            for (ReaderProxy it : this.m_matchedReaders) {
                it.getNackSupression().updateInterval(times.nackSupressionDuration);
            }
        }
        this.m_times.copy(times);
    }
    
    /**
     * Get heartbeat reader entity id
     *
     * @return heartbeat reader entity id
     */
    public EntityId getHBReaderEntityId() {
        return this.m_HBReaderEntityId;
    }

    /**
     * Get count of heartbeats
     *
     * @return count of heartbeats
     */
    public Count getHeartbeatCount() {
        return this.m_heartbeatCount;
    }

    /**
     * Increment the HB count.
     */
    public void incrementHBCount() {
        this.m_heartbeatCount.increase(); // TODO Name all increase/increment methods the same way
    }

    /**
     * Get the number of matched readers
     *
     * @return Number of the matched readers
     */
    public int getMatchedReadersSize() {
        return this.m_matchedReaders.size();
    }

}
