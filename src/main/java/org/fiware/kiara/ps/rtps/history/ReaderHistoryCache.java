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

public class ReaderHistoryCache extends HistoryCache {
	
	protected RTPSReader m_reader;
	
	protected final Semaphore m_semaphore = new Semaphore(0); 
	
	protected Map<GUID,Set<SequenceNumber>> m_historyRecord; // TODO Comparison functions in GUID

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
		
		if (this.m_reader == null) {
			this.m_mutex.unlock();
			System.out.println("You need to create a Reader with this History before adding any changes"); // TODO Log this
			return false;
		}
		
		if (change.getSerializedPayload().getSerializedSize() > this.m_attributes.payloadMaxSize) {
			this.m_mutex.unlock();
			System.out.println("The Payload length is larger than the maximum payload size"); // TODO Log this
			return false;
		}
		
		if (change.getWriterGUID().equals(new GUID())) {
			this.m_mutex.unlock();
			System.out.println("The Writer GUID_t must be defined"); // TODO Log this
			return false;
		}
		
		if (this.m_historyRecord.get(change.getWriterGUID()) == null) {
			this.m_historyRecord.put(change.getWriterGUID(), new HashSet<SequenceNumber>());
		}
		
		if (this.m_historyRecord.get(change.getWriterGUID()).add(change.getSequenceNumber())) {
			this.m_changes.add(change);
			this.updateMaxMinSeqNum();
			System.out.println("Change " + change.getSequenceNumber().toLong() + " added with " + change.getSerializedPayload().getSerializedSize() + " bytes"); // TODO Log this
			this.m_mutex.unlock();
			return true;
		}
		this.m_mutex.unlock();
		return false;
	}
	
	@Override
	public boolean removeChange(CacheChange change) {
		this.m_mutex.lock();
		if (change == null) {
			System.out.println("CacheChange is null."); // TODO Log this
		}
		
		Iterator<CacheChange> it = this.m_changes.iterator();
		while(it.hasNext()) {
			CacheChange current = it.next();
			if (current.getSequenceNumber().equals(change.getSequenceNumber()) && current.getWriterGUID().equals(change.getWriterGUID())) {
				System.out.println("Removing change " + change.getSequenceNumber()); // TODO Log this
				this.m_reader.changeRemovedByHistory(change);
				this.m_changePool.releaseCache(change);
				this.m_changes.remove(current);
				updateMaxMinSeqNum();
				this.m_mutex.unlock();
				return true;
			}
		}
		
		this.m_mutex.unlock();
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public void sortCacheChanges() {
		Collections.sort(this.m_changes);
	}

	/*@Override
	public void updateMaxMinSeqNum() {
		if (this.m_changes.size() == 0) {
			this.m_minSeqCacheChange = m_invalidChange;
			this.m_maxSeqCacheChange = m_invalidChange;
		} else {
			this.m_minSeqCacheChange = this.m_changes.get(0);
			this.m_maxSeqCacheChange = this.m_changes.get(this.m_changes.size()-1);
		}
	}*/
	
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

	

}
