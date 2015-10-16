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
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.fiware.kiara.ps.rtps.messages.common.types.ChangeKind;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class CacheChangePool, used by the HistoryCache to pre-reserve 
 * a number of CacheChange_t to avoid dynamically reserving memory 
 * in the middle of execution loops.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class CacheChangePool {

    /**
     * The payload size associated with the pool.
     */
    private int m_payloadSize;

    /**
     * The initial pool size
     */
    private int m_poolSize;

    /**
     * Maximum payload size. If set to 0 the pool will keep reserving until something breaks.
     */
    private int m_maxPoolSize;

    /**
     * List of free {@link CacheChange} objects
     */
    private List<CacheChange> m_freeChanges;

    /**
     * List of {@link CacheChange} objects
     */
    private List<CacheChange> m_allChanges;

    /**
     * Mutex
     */
    private final Lock m_mutex = new ReentrantLock(true);

    /**
     * Log instance
     */
    private static final Logger logger = LoggerFactory.getLogger(CacheChangePool.class);

    /**
     * {@link CacheChangePool} constructor
     * 
     * @param poolSize Initial size of the {@link CacheChangePool}
     * @param payloadSize Maximum payload size for every {@link CacheChange}
     * @param maxPoolSize Maximum size of the {@link CacheChangePool}
     */
    public CacheChangePool(int poolSize, int payloadSize, int maxPoolSize) {
        this.m_mutex.lock();
        try {
            this.m_payloadSize = payloadSize;
            this.m_poolSize = 0;

            this.m_freeChanges = new ArrayList<CacheChange>();
            this.m_allChanges = new ArrayList<CacheChange>();

            if (maxPoolSize > 0) {
                if (poolSize > maxPoolSize) {
                    this.m_maxPoolSize = (int) Math.abs(poolSize);
                } else {
                    this.m_maxPoolSize = (int) Math.abs(maxPoolSize);
                }
            } else {
                this.m_maxPoolSize = 0;
            }
            this.allocateGroup(poolSize);
        } finally {
            this.m_mutex.unlock();
        }

    }

    /**
     * Allocates free spaces for CacheChange objects
     * 
     * @param groupSize The number of slots to be reserved
     * @return true if space can be reserved; false otherwise
     */
    private boolean allocateGroup(int groupSize) {

        boolean added = false;
        int reserved = 0;

        if (this.m_maxPoolSize == 0) {
            reserved = groupSize;
        } else {
            if (this.m_poolSize + groupSize > m_maxPoolSize) {
                reserved = this.m_maxPoolSize - this.m_poolSize;
            } else {
                reserved = groupSize;
            }
        }

        for (int i=0; i < reserved; ++i) {
            CacheChange change = new CacheChange(); // TODO Check if payloadSize is necessary
            this.m_allChanges.add(change);
            this.m_freeChanges.add(change);
            this.m_poolSize++;
            added = true;
        }

        if (!added) {
            logger.warn("Maximum number of allowed reserved caches reached");
        }

        return added;
    }

    /**
     * Allocates a new CacheChange
     * 
     * @return The reserved CacheChange object
     */
    public CacheChange reserveCache() {
        this.m_mutex.lock();
        try {
            if (this.m_freeChanges.isEmpty()) {
                if (!allocateGroup((int) Math.ceil(this.m_poolSize/10) + 10)) {
                    return null;
                }
            }
            CacheChange change = this.m_freeChanges.remove(this.m_freeChanges.size() - 1);
            return change;
        } finally {
            this.m_mutex.unlock();
        }
    }

    /**
     * Frees a used CacheChange
     * 
     * @param change CacheChange to be freed
     */
    public void releaseCache(CacheChange change) {
        this.m_mutex.lock();
        try {
            change.setKind(ChangeKind.ALIVE);
            change.getSequenceNumber().setHigh(0);
            change.getSequenceNumber().setLow(0);
            change.setWriterGUID(new GUID());
            change.setInstanceHandle(new InstanceHandle());
            change.setRead(false);
            change.getSourceTimestamp().timeZero();

            this.m_freeChanges.add(change);
        } finally {
            this.m_mutex.unlock();
        }
    }

    /**
     * Get the payload size
     * 
     * @return The size of the payload
     */
    public int getPayloadSize() {
        return m_payloadSize;
    }

    /**
     * Set the payload size
     * 
     * @param m_payloadSize The payload size to be set
     */
    public void setPayloadSize(int m_payloadSize) {
        this.m_payloadSize = m_payloadSize;
    }

}
