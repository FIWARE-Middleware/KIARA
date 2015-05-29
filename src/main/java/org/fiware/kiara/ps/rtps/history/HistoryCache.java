package org.fiware.kiara.ps.rtps.history;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.fiware.kiara.ps.rtps.attributes.HistoryCacheAttributes;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.SequenceNumber;

public abstract class HistoryCache {
	
	/* Attributes */
	
	protected HistoryCacheAttributes m_attributes;
	
	protected ArrayList<CacheChange> m_changes;
	
	protected boolean m_isHistoryFull;
	
	protected CacheChange m_invalidChange;
	
	protected CacheChange m_minSeqCacheChange;
	
	protected CacheChange m_maxSeqCacheChange;
	
	protected CacheChangePool m_changePool;
	
	public final Lock m_mutex;
	
	/* Methods */
	
	protected HistoryCache(HistoryCacheAttributes att) {
		this.m_attributes = att;
		this.m_isHistoryFull = false;
		//this.m_invalidChange = null;
		this.m_changePool = new CacheChangePool(att.initialeservedCaches, att.payloadMaxSize, att.maximumReservedCaches);
		//this.m_minSeqCacheChange = null;
		//this.m_maxSeqCacheChange = null;
		this.m_mutex = new ReentrantLock(true);
		
		this.m_changes = new ArrayList<CacheChange>(att.initialeservedCaches);
		this.m_invalidChange = this.m_changePool.reserveCache();
		this.m_invalidChange.setWriterGUID(new GUID());
		this.m_invalidChange.setSequenceNumber(new SequenceNumber());
		this.m_minSeqCacheChange = this.m_invalidChange;
		this.m_maxSeqCacheChange = this.m_invalidChange;
	}
	
	public boolean removeAllChanges() {
		this.m_mutex.lock();
		if (!this.m_changes.isEmpty()) {
			Iterator<CacheChange> it = this.m_changes.iterator();
			while(it.hasNext()) {
				this.removeChange(it.next());
			}
			this.m_changes.clear();
			this.m_isHistoryFull = false;
			this.updateMaxMinSeqNum();
			this.m_mutex.unlock();
			return true;
		}
		this.m_mutex.unlock();
		return false;
	}
	
	public boolean getMinChange(CacheChange minChange) {
		if (!this.m_minSeqCacheChange.getSequenceNumber().equals(this.m_invalidChange.getSequenceNumber())) {
			minChange = this.m_minSeqCacheChange;
			return true;
		}
		return false;
	}
	
	public boolean getMaxChange(CacheChange maxChange) {
		if (this.m_maxSeqCacheChange.getSequenceNumber().equals(this.m_invalidChange.getSequenceNumber())) {
			maxChange = this.m_maxSeqCacheChange;
			return true;
		}
		return false;
	}
	
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
	
	public CacheChange reserveCache() {
		return this.m_changePool.reserveCache();
	}
	
	public void releaseCache(CacheChange change) {
		this.m_changePool.releaseCache(change);
	}
	
	public boolean isFull() {
		return this.m_isHistoryFull;
	}
	
	public int getHistorySize() {
		return this.m_changes.size();
	}
	
	public void updateMaxMinSeqNum() {
		if (this.m_changes.size() == 0) {
			this.m_minSeqCacheChange = m_invalidChange;
			this.m_maxSeqCacheChange = m_invalidChange;
		} else {
			this.m_minSeqCacheChange = this.m_changes.get(0);
			this.m_maxSeqCacheChange = this.m_changes.get(this.m_changes.size()-1);
		}
	}
	
	public abstract boolean removeChange(CacheChange change);
	
	public Iterator<CacheChange> changesIterator() {
		return this.m_changes.iterator();
	}
	
	public int getTypeMaxSerialized() {
		return this.m_changePool.getPayloadSize();
	}
	
	public Lock getMutex() {
		return this.m_mutex;
	}
	
	
}
