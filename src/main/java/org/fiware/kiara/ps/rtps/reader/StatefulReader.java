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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.fiware.kiara.ps.rtps.attributes.ReaderAttributes;
import org.fiware.kiara.ps.rtps.attributes.ReaderTimes;
import org.fiware.kiara.ps.rtps.attributes.RemoteWriterAttributes;

import static org.fiware.kiara.ps.rtps.common.ChangeFromWriterStatus.LOST;
import static org.fiware.kiara.ps.rtps.common.ChangeFromWriterStatus.RECEIVED;

import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.history.ReaderHistoryCache;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.SequenceNumber;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.util.ReturnParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class StatefulReader extends RTPSReader {

    /**
     * ReaderTimes of the StatefulReader.
     */
    private final ReaderTimes m_times;

    /**
     * Vector containing pointers to the matched writers.
     */
    private final List<WriterProxy> matchedWriters;

    private static final Logger logger = LoggerFactory.getLogger(StatefulReader.class);

    public StatefulReader(RTPSParticipant participant, GUID guid,
            ReaderAttributes att, ReaderHistoryCache history,
            ReaderListener listener) {
        super(participant, guid, att, history, listener);
        m_times = new ReaderTimes(att.times);
        matchedWriters = new ArrayList<>();
    }

    public void destroy() {
        logger.info("RTPS READER: StatefulReader destructor.");
        for (WriterProxy it : matchedWriters) {
            it.destroy();
        }
        matchedWriters.clear();
    }

    @Override
    public boolean matchedWriterAdd(RemoteWriterAttributes wdata) {
        m_mutex.lock();
        try {
            for (WriterProxy it : matchedWriters) {
                if (it.att.guid.equals(wdata.guid)) {
                    logger.info("RTPS READER: Attempting to add existing writer");
                    return false;
                }
            }
            WriterProxy wp = new WriterProxy(wdata, m_times.heartbeatResponseDelay, this);
            matchedWriters.add(wp);
            logger.info("RTPS READER: Writer Proxy {} added to {}", wp.att.guid, m_guid.getEntityId());
            return true;
        } finally {
            m_mutex.unlock();
        }
    }

    @Override
    public boolean matchedWriterRemove(RemoteWriterAttributes wdata) {
        m_mutex.lock();
        try {
            for (Iterator<WriterProxy> it = matchedWriters.iterator(); it.hasNext();) {
                final WriterProxy wp = it.next();
                if (wp.att.guid.equals(wdata.guid)) {
                    logger.info("RTPS READER: Writer Proxy removed: {}", wp.att.guid);
                    wp.destroy();
                    it.remove();
                    return true;
                }
            }
            logger.info("RTPS READER: Writer Proxy {} doesn't exist in reader {}", wdata.guid, this.getGuid().getEntityId());
            return false;
        } finally {
            m_mutex.unlock();
        }
    }

    @Override
    public boolean matchedWriterIsMatched(RemoteWriterAttributes wdata) {
        m_mutex.lock();
        try {

            for (WriterProxy it : matchedWriters) {
                if (it.att.guid.equals(wdata.guid)) {
                    return true;
                }
            }
            return false;
        } finally {
            m_mutex.unlock();
        }
    }

    public WriterProxy matchedWriterLookup(GUID writerGUID) {
        m_mutex.lock();
        try {
            for (WriterProxy it : matchedWriters) {
                if (it.att.guid.equals(writerGUID)) {
                    logger.info("RTPS READER: {} FINDS writerProxy {} from {}", getGuid().getEntityId(), writerGUID, matchedWriters.size());
                    return it;
                }
            }
            logger.info("RTPS READER: {} NOT FINDS writerProxy {} from {}", getGuid().getEntityId(), writerGUID, matchedWriters.size());
            return null;
        } finally {
            m_mutex.unlock();
        }
    }

    @Override
    public boolean acceptMsgFrom(GUID writerId, ReturnParam<WriterProxy> wp) {
        if (writerId.getEntityId().equals(this.m_trustedWriterEntityId)) {
            return true;
        }

        for (WriterProxy it : matchedWriters) {
            if (it.att.guid.equals(writerId)) {
                if (wp != null) {
                    wp.value = it;
                }
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean changeRemovedByHistory(CacheChange change, WriterProxy wp) {
        m_mutex.lock();
        try {
            if (wp == null) {
                wp = matchedWriterLookup(change.getWriterGUID());
            }

            if (wp != null) {
                List<Integer> toRemove = new ArrayList<>();

                boolean continuous_removal = true;
                for (int i = 0; i < wp.changesFromWriter.size(); ++i) {
                    if (change.getSequenceNumber().equals(wp.changesFromWriter.get(i).seqNum)) {
                        wp.changesFromWriter.get(i).notValid();
                        if (continuous_removal) {
                            wp.lastRemovedSeqNum.copy(wp.changesFromWriter.get(i).seqNum);
                            wp.hasMinAvailableSeqNumChanged = true;
                            toRemove.add(i);
                        }
                        break;
                    }
                    if (!wp.changesFromWriter.get(i).isValid()
                            && (wp.changesFromWriter.get(i).status == RECEIVED || wp.changesFromWriter.get(i).status == LOST)
                            && continuous_removal) {
                        wp.lastRemovedSeqNum.copy(wp.changesFromWriter.get(i).seqNum);
                        wp.hasMinAvailableSeqNumChanged = true;
                        toRemove.add(i);
                        continue;
                    }
                    continuous_removal = false;
                }

                ListIterator<Integer> it = toRemove.listIterator(toRemove.size());
                while (it.hasPrevious()) {
                    wp.changesFromWriter.remove(it.previous().intValue());
                }
                return true;
            } else {
                logger.error("RTPS READER: You should always find the WP associated with a change, something is very wrong");
            }
            return false;
        } finally {
            m_mutex.unlock();
        }
    }

    @Override
    public boolean changeReceived(CacheChange change, WriterProxy prox) {
        //First look for WriterProxy in case is not provided
        System.out.println("BLBLA");
        m_mutex.lock();
        try {
            if (prox == null) {
                prox = matchedWriterLookup(change.getWriterGUID());
                if (prox == null) {
                    {
                        logger.info("RTPS READER: Writer Proxy {} not matched to this Reader {}", change.getWriterGUID(), m_guid.getEntityId());
                        return false;
                    }
                }
            }
            //WITH THE WRITERPROXY FOUND:
            //Check if we can add it
            if (change.getSequenceNumber().isLowerOrEqualThan(prox.lastRemovedSeqNum)) {
                logger.info("RTPS READER: Change {} <= than last Removed Seq Number {}", change.getSequenceNumber(), prox.lastRemovedSeqNum);
                return false;
            }
            SequenceNumber maxSeq = prox.getAvailableChangesMax();
            if (maxSeq != null && change.getSequenceNumber().isLowerOrEqualThan(maxSeq)) {
                logger.info("RTPS READER: Change {} <= than max available Seqnum {}", change.getSequenceNumber(), maxSeq);
                return false;
            }
            if (m_history.receivedChange(change)) {
                if (prox.receivedChangeSet(change)) {
                    final SequenceNumber maxSeqNumAvailable = prox.getAvailableChangesMax();

                    if (change.getSequenceNumber().isLowerOrEqualThan(maxSeqNumAvailable)) {
                        if (getListener() != null) {
                            //cout << "CALLING NEWDATAMESSAGE "<<endl;
                            getListener().onNewCacheChangeAdded(this, change);
                            //cout << "FINISH CALLING " <<endl;
                        }
                        m_history.postChange();
                    }
                    return true;
                }
            }
            return false;
        } finally {
            m_mutex.unlock();
        }
    }

    @Override
    public boolean nextUntakenCache(ReturnParam<CacheChange> change, ReturnParam<WriterProxy> wpout) {
        m_mutex.lock();
        try {

            final SequenceNumber minSeqNum = new SequenceNumber();
            minSeqNum.setUnknown();
            final SequenceNumber auxSeqNum = new SequenceNumber();
            WriterProxy wp = null;
            boolean available = false;
            logger.info("RTPS READER: {}: looking through: {} WriterProxies", getGuid().getEntityId(), matchedWriters.size());
            for (WriterProxy it : matchedWriters) {
                //it.getAvailableChangesMax(auxSeqNum);

                if (it.getAvailableChangesMin(auxSeqNum) != null) {
                    //logUser("AVAILABLE MIN for writer: "<<(*it)->m_att.guid<< " : " << auxSeqNum);
                    if (auxSeqNum.toLong() > 0 && (minSeqNum.isGreaterThan(auxSeqNum) || minSeqNum.isUnknown())) {
                        available = true;
                        minSeqNum.copy(auxSeqNum);
                        wp = it;
                    }
                }
            }

            if (wp == null) {
                return false;
            }

            //cout << "AVAILABLE? "<< available << endl;
            CacheChange wchange = wp.getChange(minSeqNum);
            if (available && wchange != null) {

                // FIXME: This is not correct since in original code
                //        CacheChange is passed by pointer to poiner
                //        Actually first argument to this method should be
                //        ReturnParam<CacheChange>, but this require additional
                //        source code changes
                change.value = wchange;

                if (wpout != null) {
                    wpout.value = wp;
                }
                return true;
            }
            return false;
        } finally {
            m_mutex.unlock();
        }
    }

    @Override
    public boolean nextUnreadCache(ReturnParam<CacheChange> change, ReturnParam<WriterProxy> wpout) {
        m_mutex.lock();
        try {
            List<CacheChange> toremove = new ArrayList<>();

            boolean readok = false;
            for (CacheChange it : m_history.getChanges()) {
                if (it.isRead()) {
                    continue;
                }
                WriterProxy wp = matchedWriterLookup(it.getWriterGUID());
                if (wp != null) {
                    SequenceNumber seq = new SequenceNumber();
                    wp.getAvailableChangesMax(seq);
                    if (seq.isGreaterOrEqualThan(it.getSequenceNumber())) {
                        change.value = it;
                        if (wpout != null) {
                            wpout.value = wp;
                        }
                        return true;
                    }
                } else {
                    toremove.add(it);
                }
            }

            for (CacheChange it : toremove) {
                logger.warn("RTPS READER: Removing change {} from {} because is no longer paired", it.getSequenceNumber().toLong(), it.getWriterGUID());
                m_history.removeChange(it);
            }
            return readok;
        } finally {
            m_mutex.unlock();
        }
    }

    public boolean updateTimes(ReaderTimes ti) {
        if (!m_times.heartbeatResponseDelay.equals(ti.heartbeatResponseDelay)) {
            m_times.copy(ti);
            for (WriterProxy wit : matchedWriters) {
                wit.heartBeatResponse.updateInterval(m_times.heartbeatResponseDelay);
            }
        }
        return true;
    }

    @Override
    public CacheChange nextUntakenCache(WriterProxy proxy) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CacheChange nextUnreadCache(WriterProxy proxy) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     *
     * @return Reference to the ReaderTimes.
     */
    public ReaderTimes getTimes() {
        return m_times;
    }

}
