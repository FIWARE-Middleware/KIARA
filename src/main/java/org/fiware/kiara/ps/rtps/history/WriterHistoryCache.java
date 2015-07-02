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
package org.fiware.kiara.ps.rtps.history;

import org.fiware.kiara.ps.rtps.attributes.HistoryCacheAttributes;
import org.fiware.kiara.ps.rtps.messages.elements.SequenceNumber;
import org.fiware.kiara.ps.rtps.writer.RTPSWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class WriterHistoryCache extends HistoryCache {

    public SequenceNumber m_lastCacheChangeSeqNum;

    public RTPSWriter m_writer;
    
    private static final Logger logger = LoggerFactory.getLogger(WriterHistoryCache.class);

    public WriterHistoryCache(HistoryCacheAttributes att) {
        super(att);
        this.m_writer = null;
        this.m_lastCacheChangeSeqNum = new SequenceNumber();
    }

    public boolean addChange(CacheChange change) {
        this.m_mutex.lock();
        try {
            if (this.m_writer == null) {
                logger.error("You need to create a Writer with this History before adding any changes");
                this.m_mutex.unlock();
                return false;
            }
    
            if (!change.getWriterGUID().equals(this.m_writer.getGuid())) {
                logger.error("Change writerGUID " + change.getWriterGUID() + " different than Writer GUID " + this.m_writer.getGuid());
                this.m_mutex.unlock();
                return false;
            }
    
            if (change.getSerializedPayload().getSerializedSize() > this.m_attributes.payloadMaxSize) {
                logger.error("The Payload length is larger than the maximum payload size");
                this.m_mutex.unlock();
                return false;
            }
    
            this.m_lastCacheChangeSeqNum.increment();
            change.setSequenceNumber(this.m_lastCacheChangeSeqNum);
            this.m_changes.add(change);
            logger.info("Change " + change.getSequenceNumber().toLong() + " added with " + change.getSerializedPayload().getSerializedSize() + " bytes");
            updateMaxMinSeqNum();
            this.m_writer.unsentChangeAddedToHistory(change);
        } finally {
            this.m_mutex.unlock();
        }
        return true;
    }

    @Override
    public boolean removeChange(CacheChange change) {
        this.m_mutex.lock();
        try {
            if (change == null) {
                logger.error("CacheChange object is null"); 
                this.m_mutex.unlock();
                return false;
            }
    
            if (!change.getWriterGUID().equals(this.m_writer.getGuid())) {
                logger.error("Change writerGUID " + change.getWriterGUID() + " different than Writer GUID " + this.m_writer.getGuid()); 
                this.m_mutex.unlock();
                return false;
            }
    
            for (CacheChange current : this.m_changes) {
                if (current.getSequenceNumber().equals(change.getSequenceNumber())) {
                    this.m_writer.changeRemovedByHistory(change);
                    this.m_changePool.releaseCache(change);
                    this.m_changes.remove(current);
                    this.m_mutex.unlock();
                    return true;
                }
            }
        } finally {
            this.m_mutex.unlock();
        }
        logger.warn("SequenceNumber " + change.getSequenceNumber().toLong() + " not found");
        return false;
    }

    public boolean removeMinChange() {
        this.m_mutex.lock();
        try {
            if (this.m_changes.size() > 0 && removeChange(this.m_minSeqCacheChange)) {
                updateMaxMinSeqNum();
                this.m_mutex.unlock(); 
                return true;
            }
        } finally {
            this.m_mutex.unlock();
        }
        return false;
    }

}
