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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.fiware.kiara.ps.rtps.attributes.HistoryCacheAttributes;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.SequenceNumber;
import org.fiware.kiara.util.ReturnParam;

/**
 * Class HistoryCache, container of the different CacheChanges 
 * and the methods to access them.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public abstract class HistoryCache {

    /**
     * The {@link HistoryCacheAttributes} associated to the HistoryCache
     */
    protected HistoryCacheAttributes m_attributes;

    /**
     * List of references to the {@link CacheChange}.
     */
    protected List<CacheChange> m_changes;

    /**
     * Variable to know if the history is full without 
     * needing to block the History mutex.
     */
    protected boolean m_isHistoryFull;

    /**
     * Pointer to and invalid {@link CacheChange} used to return the 
     * maximum and minimum when no changes are stored in the history.
     */
    protected CacheChange m_invalidChange;

    /**
     * Pointer to the minimum sequeceNumber {@link CacheChange}.
     */
    protected CacheChange m_minSeqCacheChange;

    /**
     * Pointer to the maximum sequeceNumber {@link CacheChange}.
     */
    protected CacheChange m_maxSeqCacheChange;

    /**
     * Pool of cache changes reserved when the History is created.
     */
    protected CacheChangePool m_changePool;

    /**
     * Mutex
     */
    public final Lock m_mutex;

    /* Methods */

    protected HistoryCache(HistoryCacheAttributes att) {
        this.m_attributes = att;
        this.m_isHistoryFull = false;
        this.m_changePool = new CacheChangePool(att.initialReservedCaches, att.payloadMaxSize, att.maximumReservedCaches);
        this.m_mutex = new ReentrantLock(true);
        this.m_changes = new ArrayList<CacheChange>(att.initialReservedCaches);
        this.m_invalidChange = this.m_changePool.reserveCache();
        this.m_invalidChange.setWriterGUID(new GUID());
        this.m_invalidChange.setSequenceNumber(new SequenceNumber());
        this.m_minSeqCacheChange = this.m_invalidChange;
        this.m_maxSeqCacheChange = this.m_invalidChange;
    }

    public boolean removeAllChanges() {
        this.m_mutex.lock();
        try {
            if (!this.m_changes.isEmpty()) {
                Iterator<CacheChange> it = this.m_changes.iterator();
                while(it.hasNext()) {
                    this.removeChange(it.next());
                }
                this.m_changes.clear();
                this.m_isHistoryFull = false;
                this.updateMaxMinSeqNum();
                return true;
            }
        } finally {
            this.m_mutex.unlock();
        }
        return false;
    }

    /**
     * Get the minimum CacheChange in the HistoryCache
     * 
     * @return The minimum CacheChange
     */
    public CacheChange getMinChange() {
        if (!this.m_minSeqCacheChange.getSequenceNumber().equals(this.m_invalidChange.getSequenceNumber())) {
            return this.m_minSeqCacheChange;
        }
        return null;
    }

    /**
     * Get the minimum CacheChange inside a ReturnParam object
     * 
     * @param change The ReturnParam to store CacheChange
     * @return The ReturnParam with the stored CacheChange
     */
    public boolean getMinChange(ReturnParam<CacheChange> change) {
        if (!this.m_minSeqCacheChange.getSequenceNumber().equals(this.m_invalidChange.getSequenceNumber())) {
            change.value = this.m_minSeqCacheChange;
            return true;
        }
        return false;
    }

    /**
     * Get the maximum CacheChange in the HistoryCache
     * 
     * @return The maximum CacheChange
     */
    public CacheChange getMaxChange() {
        if (!this.m_maxSeqCacheChange.getSequenceNumber().equals(this.m_invalidChange.getSequenceNumber())) {
            return this.m_maxSeqCacheChange;
        }
        return null;
    }

    /**
     * Get the Change that matches with the specified objects
     * 
     * @param seq The SequenceNumber to compare with the one in the CacheChange
     * @param guid The GUID to compare with the one in the CacheChange
     * @param change The CacheChange reference
     * @return true if the CacheChange has been found; false otherwise
     */
    public boolean getChange(SequenceNumber seq, GUID guid, CacheChange change) {
        this.m_mutex.lock();
        Iterator<CacheChange> it = this.m_changes.iterator();
        while(it.hasNext()) {
            CacheChange current = it.next();
            if (current.getSequenceNumber() == seq && current.getWriterGUID() == guid) {
                change = current;
                this.m_mutex.unlock();
                return true;
            } else if (current.getSequenceNumber().isGreaterThan(seq)) {
                break;
            }
        }
        this.m_mutex.unlock();
        return false;
    }

    /**
     * Get the HistoryCacheAttributes
     * 
     * @return The HistoryCacheAttributes
     */
    public HistoryCacheAttributes getAttributes() {
        return this.m_attributes;
    }

    /**
     * Reserves a new CacheChange object
     * 
     * @return the new reserved CacheChange
     */
    public CacheChange reserveCache() {
        return this.m_changePool.reserveCache();
    }

    /**
     * Frees the information inside a CacheChange reference
     * 
     * @param change The CacheChange referente to be freed
     */
    public void releaseCache(CacheChange change) {
        this.m_changePool.releaseCache(change);
    }

    /**
     * Returns a boolean value indicating if the HistoryCache is full
     * 
     * @return true if the HistoryCache is full; false otherwise
     */
    public boolean isFull() {
        return this.m_isHistoryFull;
    }

    /**
     * Get the HistoryCache size
     * 
     * @return The HistoryCache size
     */
    public int getHistorySize() {
        return this.m_changes.size();
    }

    /**
     * Removes a CacheChange from the HistoryCache
     * 
     * @param change The CacheChange to be removed
     * @return true if the CacheChange has been removed; false otherwise
     */
    public abstract boolean removeChange(CacheChange change);

    /**
     * Get an iterator over the CacheChange objects in the HistoryCache 
     * 
     * @return A new CacheChange iterator
     */
    public Iterator<CacheChange> changesIterator() {
        return this.m_changes.iterator();
    }

    /**
     * Get the CacheChange objects in the HistoryCache
     * 
     * @return The list of CacheChange objects
     */
    public List<CacheChange> getChanges() {
        return this.m_changes;
    }

    /**
     * Get the maximum serialized size
     * 
     * @return The maximum serialized size
     */
    public int getTypeMaxSerialized() {
        return this.m_changePool.getPayloadSize();
    }

    /**
     * Get the mutex
     * 
     * @return The mutex
     */
    public Lock getMutex() {
        return this.m_mutex;
    }

    /**
     * Updates the maximum sequence number of the HistoryCache
     */
    public void updateMaxMinSeqNum() {
        if (this.m_changes.size() == 0) {
            this.m_minSeqCacheChange = m_invalidChange;
            this.m_maxSeqCacheChange = m_invalidChange;
        } else {
            this.m_minSeqCacheChange = this.m_changes.get(0);
            this.m_maxSeqCacheChange = this.m_changes.get(this.m_changes.size()-1);
        }
    }

    

}
