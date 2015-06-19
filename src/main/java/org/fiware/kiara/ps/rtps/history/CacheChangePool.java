package org.fiware.kiara.ps.rtps.history;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.fiware.kiara.ps.rtps.messages.common.types.ChangeKind;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;

public class CacheChangePool {

    private int m_payloadSize;
    private int m_poolSize;
    private int m_maxPoolSize;

    private List<CacheChange> m_freeChanges;
    private List<CacheChange> m_allChanges;

    private final Lock m_mutex = new ReentrantLock(true);

    public CacheChangePool(int poolSize, int payloadSize, int maxPoolSize) {
        this.m_mutex.lock();
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
        this.m_mutex.unlock();

    }

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
            // TODO Log here
            System.out.println("Maximum number of allowed reserved caches reached");
        }

        return added;
    }

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

    public void releaseCache(CacheChange change) {
        //synchronized(this) {
        this.m_mutex.lock();
        change.setKind(ChangeKind.ALIVE);
        change.getSequenceNumber().setHigh(0);
        change.getSequenceNumber().setLow(0);
        change.setWriterGUID(new GUID());
        // TODO Check serializedPayload length
        change.setInstanceHandle(new InstanceHandle());
        change.setRead(false);
        change.getSourceTimestamp().timeZero();

        this.m_freeChanges.add(change);
        //}
        this.m_mutex.unlock();
    }

    public int getPayloadSize() {
        return m_payloadSize;
    }

    public void setPayloadSize(int m_payloadSize) {
        this.m_payloadSize = m_payloadSize;
    }

}
