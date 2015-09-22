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
 * Class WriterHistoryCache, container of the different CacheChange 
 * objects of a writer
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class WriterHistoryCache extends HistoryCache {

    /**
     * Last CacheChange SequenceNumber added to the History.
     */
    public SequenceNumber m_lastCacheChangeSeqNum;

    /**
     * Reference to the associated RTPSWriter;
     */
    public RTPSWriter m_writer;
    
    /**
     * Log object
     */
    private static final Logger logger = LoggerFactory.getLogger(WriterHistoryCache.class);

    public WriterHistoryCache(HistoryCacheAttributes att) {
        super(att);
        this.m_writer = null;
        this.m_lastCacheChangeSeqNum = new SequenceNumber();
    }

    /**
     * CacheChange to be added to the HistoryCache
     * @param change CacheChange to be added
     * @return true if the CacheChange can be added; false otherwise
     */
    public boolean addChange(CacheChange change) {
        this.m_mutex.lock();
        try {
            if (this.m_writer == null) {
                logger.error("You need to create a Writer with this History before adding any changes");
                this.m_mutex.unlock();
                return false;
            }
    
            if (!change.getWriterGUID().equals(this.m_writer.getGuid())) {
                logger.error("Change writerGUID {} different from Writer GUID {}", change.getWriterGUID(), this.m_writer.getGuid());
                this.m_mutex.unlock();
                return false;
            }
    
            if (change.getSerializedPayload().getSerializedSize() > this.m_attributes.payloadMaxSize) {
                logger.error("The Payload length is larger than the maximum payload size");
                this.m_mutex.unlock();
                return false;
            }
    
            this.m_lastCacheChangeSeqNum.increment();
            
            //change.setSequenceNumber(this.m_lastCacheChangeSeqNum);
            
            SequenceNumber changeSeqNum = new SequenceNumber(this.m_lastCacheChangeSeqNum);
            change.setSequenceNumber(changeSeqNum);
            
            this.m_changes.add(change);
            logger.debug("Change {} added with {} bytes", change.getSequenceNumber().toLong(), change.getSerializedPayload().getSerializedSize());
            updateMaxMinSeqNum();
            this.m_writer.unsentChangeAddedToHistory(change);
        } finally {
            this.m_mutex.unlock();
        }
        return true;
    }

    /**
     * Removes the CacheChange in thie HistoryCache
     */
    @Override
    public boolean removeChange(CacheChange change) {
        this.m_mutex.lock();
        try {
            if (change == null) {
                logger.error("CacheChange object is null"); 
                return false;
            }
    
            if (!change.getWriterGUID().equals(this.m_writer.getGuid())) {
                logger.error("Change writerGUID " + change.getWriterGUID() + " different than Writer GUID " + this.m_writer.getGuid()); 
                return false;
            }
    
            for (CacheChange current : this.m_changes) {
                if (current.getSequenceNumber().equals(change.getSequenceNumber())) {
                    this.m_writer.changeRemovedByHistory(change);
                    this.m_changePool.releaseCache(change);
                    this.m_changes.remove(current);
                    return true;
                }
            }
        } finally {
            this.m_mutex.unlock();
        }
        logger.warn("SequenceNumber " + change.getSequenceNumber().toLong() + " not found");
        return false;
    }

    /**
     * Removed the CacheChange with the minimum SequenceNumber
     * @return true if the change can be removed; false otherwise
     */
    public boolean removeMinChange() {
        this.m_mutex.lock();
        try {
            if (this.m_changes.size() > 0 && removeChange(this.m_minSeqCacheChange)) {
                updateMaxMinSeqNum();
                //this.m_mutex.unlock(); 
                return true;
            }
        } finally {
            this.m_mutex.unlock();
        }
        return false;
    }

}
