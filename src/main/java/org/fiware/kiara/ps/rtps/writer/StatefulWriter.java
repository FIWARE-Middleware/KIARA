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
import org.fiware.kiara.ps.rtps.attributes.RemoteReaderAttributes;
import org.fiware.kiara.ps.rtps.attributes.WriterAttributes;
import org.fiware.kiara.ps.rtps.attributes.WriterTimes;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.history.WriterHistoryCache;
import org.fiware.kiara.ps.rtps.messages.elements.Count;
import org.fiware.kiara.ps.rtps.messages.elements.EntityId;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.writer.timedevent.PeriodicHeartbeat;
import org.fiware.kiara.ps.subscriber.ReaderProxy;
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

    @Override
    public boolean matchedReaderAdd(RemoteReaderAttributes ratt) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean matchedReaderRemove(RemoteReaderAttributes ratt) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean matchedReaderIsMatched(RemoteReaderAttributes ratt) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void updateAttributes(WriterAttributes att) {
        // TODO Auto-generated method stub

    }

    @Override
    public void unsentChangesNotEmpty() {
        // TODO Auto-generated method stub

    }

    /**
     * Get heartbeat reader entity id
     *
     * @return heartbeat reader entity id
     */
    public EntityId getHBReaderEntityId() {
        throw new UnsupportedOperationException();
    }

    /**
     * Get count of heartbeats
     *
     * @return count of heartbeats
     */
    public Count getHeartbeatCount() {
        throw new UnsupportedOperationException();
    }

    /**
     * Increment the HB count.
     */
    public void incrementHBCount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unsentChangeAddedToHistory(CacheChange change) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean changeRemovedByHistory(CacheChange change) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Get the number of matched readers
     *
     * @return Number of the matched readers
     */
    public int getMatchedReadersSize() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
