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
import java.util.Iterator;
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
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class ReaderHistoryCache extends HistoryCache {

    protected RTPSReader m_reader;

    protected final Semaphore m_semaphore = new Semaphore(0); 

    protected Map<GUID,Set<SequenceNumber>> m_historyRecord; // TODO Comparison functions in GUID

    private static final Logger logger = LoggerFactory.getLogger(ReaderHistoryCache.class);

    public ReaderHistoryCache(HistoryCacheAttributes att) {
        super(att);
        this.m_reader = null;
        this.m_historyRecord = new HashMap<GUID,Set<SequenceNumber>>();
    }

    public boolean receivedChange(CacheChange change) {
        return this.addChange(change);
    }

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
                logger.error("The Payload length is larger than the maximum payload size"); 
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
    
            if (this.m_historyRecord.get(change.getWriterGUID()).add(change.getSequenceNumber())) {
                this.m_changes.add(change);
                this.updateMaxMinSeqNum();
                logger.info("Change " + change.getSequenceNumber().toLong() + " added with " + change.getSerializedPayload().getSerializedSize() + " bytes"); // TODO Log this
                //this.m_mutex.unlock();
                return true;
            }
        } finally {
            this.m_mutex.unlock();
        }
        return false;
    }

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
                    logger.info("Removing change " + change.getSequenceNumber()); 
                    this.m_reader.changeRemovedByHistory(change, null);
                    this.m_changePool.releaseCache(change);
                    this.m_changes.remove(it);
                    i--;
                    updateMaxMinSeqNum();
                    //this.m_mutex.unlock();
                    return true;
                }
    
            }
        } finally {
            this.m_mutex.unlock();
        }
        return false;
    }

    public void sortCacheChanges() {
        Collections.sort(this.m_changes);
    }

    public void postChange() {
        this.m_semaphore.release();
    }

    public void waitChange() {
        try {
            this.m_semaphore.wait();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            // TODO Log this
            e.printStackTrace();
        }
    }

    public void setReader(RTPSReader rtpsReader) {
        this.m_reader = rtpsReader;
    }



}
