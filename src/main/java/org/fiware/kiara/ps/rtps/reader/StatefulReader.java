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
import org.fiware.kiara.ps.publisher.WriterProxy;
import org.fiware.kiara.ps.rtps.attributes.ReaderAttributes;
import org.fiware.kiara.ps.rtps.attributes.ReaderTimes;
import org.fiware.kiara.ps.rtps.attributes.RemoteWriterAttributes;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.history.ReaderHistoryCache;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
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
        m_times = new ReaderTimes();
        m_times.copy(att.times);
        matchedWriters = new ArrayList<>();
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

    public void updateTimes(ReaderTimes times) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean acceptMsgFrom(GUID rntityGUID, WriterProxy proxy) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean matchedWriterRemove(RemoteWriterAttributes wdata) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean matchedWriterIsMatched(RemoteWriterAttributes wdata) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean changeReceived(CacheChange change, WriterProxy proxy) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean changeRemovedByHistory(CacheChange change, WriterProxy proxy) {
        // TODO Auto-generated method stub
        return false;
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

    @Override
    public boolean nextUntakenCache(CacheChange change, WriterProxy proxy) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean nextUnreadCache(CacheChange change, WriterProxy proxy) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean matchedWriterLookup(GUID guid, WriterProxy wp) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     *
     * @return Reference to the ReaderTimes.
     */
    public ReaderTimes getTimes() {
        return m_times;
    }

}
