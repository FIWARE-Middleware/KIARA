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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

import org.fiware.kiara.ps.rtps.attributes.HistoryCacheAttributes;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.SequenceNumber;
import org.fiware.kiara.ps.rtps.reader.RTPSReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class ReaderHistoryCache, container of the different CacheChanges 
 * of a reader
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class ReaderHistoryCache extends HistoryCache {

    /**
     * Pointer to the {@link RTPSReader}
     */
    protected RTPSReader m_reader;

    /**
     * Pointer to the {@link Semaphore}, used to halt execution until new 
     * message arrives.
     */
    protected final Semaphore m_semaphore = new Semaphore(0); 

    /**
     * Information about changes already in History
     */
    protected Map<GUID,Set<SequenceNumber>> m_historyRecord; // TODO Comparison functions in GUID

    /**
     * Logging object
     */
    private static final Logger logger = LoggerFactory.getLogger(ReaderHistoryCache.class);

    /**
     * Main {@link ReaderHistoryCache} constructor
     * 
     * @param att {@link HistoryCacheAttributes} related to this {@link ReaderHistoryCache}
     */
    public ReaderHistoryCache(HistoryCacheAttributes att) {
        super(att);
        this.m_reader = null;
        this.m_historyRecord = new HashMap<GUID,Set<SequenceNumber>>();
    }

    /**
     * Add the received CacheChange to the HistoryCache
     * 
     * @param change The received CacheChange
     * @return true is the change can be added; false otherwise
     */
    public boolean receivedChange(CacheChange change) {
        return this.addChange(change);
    }

    /**
     * Adds a new CacheChange to the HistoryCache
     * 
     * @param change The CacheChange to be added
     * @return true if the change can be added; false otherwise
     */
    public boolean addChange(CacheChange change) {
        this.m_mutex.lock();
        try {
            if (this.m_reader == null) {
                //this.m_mutex.unlock();
                logger.error("You need to create a Reader with this History before adding any changes"); 
                return false;
            }
    
            if (change.getSerializedPayload().getSerializedSize() > this.m_attributes.payloadMaxSize) {
                //this.m_mutex.unlock();
                logger.error("The Payload length {} is larger than the maximum payload size", change.getSerializedPayload().getSerializedSize()); 
                return false;
            }
    
            if (change.getWriterGUID().equals(new GUID())) {
                //this.m_mutex.unlock();
                logger.error("The Writer GUID_t must be defined"); 
                return false;
            }
            
            if (this.m_historyRecord.get(change.getWriterGUID()) == null) {
                this.m_historyRecord.put(change.getWriterGUID(), new HashSet<SequenceNumber>());
            }
    
            if (this.m_historyRecord.get(change.getWriterGUID()).add(new SequenceNumber(change.getSequenceNumber()))) {
                this.m_changes.add(change);
                this.updateMaxMinSeqNum();
                logger.debug("Change {} added with {} bytes", change.getSequenceNumber().toLong(), change.getSerializedPayload().getSerializedSize());
                //this.m_mutex.unlock();
                return true;
            }
        } finally {
            this.m_mutex.unlock();
        }
        return false;
    }

    /**
     * Removes a CacheChange from the HistoryCache
     */
    @Override
    public boolean removeChange(CacheChange change) {
        this.m_mutex.lock();
        try {
            if (change == null) {
                logger.error("CacheChange is null.");
                return false;
            }
    
            for (int i=0; i < this.m_changes.size(); ++i) {
                CacheChange it = this.m_changes.get(i);
                if (it.getSequenceNumber().equals(change.getSequenceNumber()) && it.getWriterGUID().equals(change.getWriterGUID())) {
                    logger.debug("Removing change " + change.getSequenceNumber()); 
                    this.m_reader.changeRemovedByHistory(change, null);
                    this.m_changePool.releaseCache(change);
                    this.m_changes.remove(it);
                    i--;
                    updateMaxMinSeqNum();
                    return true;
                }
    
            }
        } finally {
            this.m_mutex.unlock();
        }
        return false;
    }

    /**
     * Sorts all the allocated CacheChange objects
     */
    public void sortCacheChanges() {
        Collections.sort(this.m_changes);
    }

    /**
     * Releases the Semaphore
     */
    public void postChange() {
        this.m_semaphore.release();
    }

    /**
     * Waits for the Semaphore to be released
     */
    public void waitChange() {
        try {
            this.m_semaphore.wait();
        } catch (InterruptedException e) {
            // TODO Log this
            e.printStackTrace();
        }
    }

    /**
     * Set the {@link RTPSReader} of the {@link ReaderHistoryCache}
     * 
     * @param rtpsReader The {@link RTPSReader} to be set
     */
    public void setReader(RTPSReader rtpsReader) {
        this.m_reader = rtpsReader;
    }



}
