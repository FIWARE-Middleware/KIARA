package org.fiware.kiara.ps.subscriber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.fiware.kiara.ps.publisher.WriterProxy;
import org.fiware.kiara.ps.qos.policies.HistoryQosPolicy;
import org.fiware.kiara.ps.qos.policies.HistoryQosPolicyKind;
import org.fiware.kiara.ps.qos.policies.OwnershipQosPolicyKind;
import org.fiware.kiara.ps.qos.policies.ResourceLimitsQosPolicy;
import org.fiware.kiara.ps.rtps.attributes.HistoryCacheAttributes;
import org.fiware.kiara.ps.rtps.common.TopicKind;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.history.ReaderHistoryCache;
import org.fiware.kiara.ps.rtps.messages.common.types.ChangeKind;
import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.fiware.kiara.ps.topic.SerializableDataType;
import org.fiware.kiara.ps.topic.TopicDataType;
import org.fiware.kiara.serialization.impl.Serializable;
import org.fiware.kiara.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubscriberHistory extends ReaderHistoryCache {
    
    private long m_unreadCacheCount;
    
    private List<Pair<InstanceHandle, List<CacheChange>>> m_keyedChanges;
    
    private HistoryQosPolicy m_historyQos;
    
    private ResourceLimitsQosPolicy m_resourceLimitsQos;
    
    private Subscriber m_subscriber;
    
    private Object m_getKeyObject;
    
    private final Lock m_mutex = new ReentrantLock(true);
    
    private static final Logger logger = LoggerFactory.getLogger(SubscriberHistory.class);

    public SubscriberHistory(Subscriber subscriber, int payloadMaxSize, HistoryQosPolicy history, ResourceLimitsQosPolicy resource) {
        super(new HistoryCacheAttributes(payloadMaxSize, resource.allocatedSamples, resource.maxSamples));
        this.m_unreadCacheCount = 0;
        this.m_historyQos = history;
        this.m_resourceLimitsQos = resource;
        this.m_subscriber = subscriber;
        this.m_getKeyObject = this.m_subscriber.getType().createData();
        
    }
    
    @SuppressWarnings("unchecked")
    public boolean receivedChange(CacheChange change) {
        this.m_mutex.lock();
        try {
            
            if (this.m_isHistoryFull) {
                logger.warn("Attempting to add Data to full ReaderHistory");
                return false;
            }
            
            if (change.getSequenceNumber().isLowerThan(this.m_maxSeqCacheChange.getSequenceNumber())) {
                for (CacheChange it : this.m_changes) {
                    if (it.getSequenceNumber().equals(change.getSequenceNumber()) && it.getWriterGUID().equals(change.getWriterGUID())) {
                        logger.info("Change (seqNum: " + change.getSequenceNumber().toLong() + ") already in ReaderHistory");
                        return false;
                    }
                    if (it.getWriterGUID().equals(change.getWriterGUID()) && it.getSequenceNumber().isLowerThan(change.getSequenceNumber())) {
                        // All elemens should be lower than the one we are looking for
                        break;
                    }
                }
            }
            
            if (this.m_subscriber.getAttributes().topic.topicKind == TopicKind.NO_KEY) { // No key history
                
                boolean add = false;
                if (this.m_historyQos.kind == HistoryQosPolicyKind.KEEP_ALL_HISTORY_QOS) {
                    add = true;
                } else if (this.m_historyQos.kind == HistoryQosPolicyKind.KEEP_LAST_HISTORY_QOS) {
                    if (this.m_changes.size() < this.m_historyQos.depth) {
                        add = true;
                    } else {
                        boolean read = false;
                        if (this.m_minSeqCacheChange.isRead()) {
                            read = true;
                        }
                        if (this.removeChangeSub(this.m_minSeqCacheChange, null)) {
                            if (!read) {
                                this.decreadeUnreadCount();
                            }
                            add = true;
                        }
                    }
                }
                if (add) {
                    if (this.addChange(change)) {
                        this.increaseUnreadCount();
                        if (change.getSequenceNumber().isLowerThan(this.m_maxSeqCacheChange.getSequenceNumber())) {
                            this.sortCacheChanges();
                        }
                        this.updateMaxMinSeqNum();
                        if (this.m_changes.size() == this.m_resourceLimitsQos.maxSamples) {
                            this.m_isHistoryFull = true;
                        }
                        logger.info("Change added from: " + change.getWriterGUID());
                        return true;
                    }
                } else {
                    return false;
                }
                
            } else if (this.m_subscriber.getAttributes().topic.topicKind == TopicKind.WITH_KEY) { // History with key
                if (change.getInstanceHandle().isDefined() && this.m_subscriber.getType() != null) {
                    logger.info("Getting Key of change with no Key transmitted");
                    if (!this.m_subscriber.getType().getKey(this.m_getKeyObject, change.getInstanceHandle())) {
                        return false;
                    }
                } else if (!change.getInstanceHandle().isDefined()) {
                    logger.warn("NO KEY in topic " + this.m_subscriber.getAttributes().topic.topicName + " and no method to obtain it");
                    return false;
                }
                
                Pair<InstanceHandle, List<CacheChange>> vit = this.findKey(change);
                if (vit != null) {
                    boolean add = false;
                    if (this.m_historyQos.kind == HistoryQosPolicyKind.KEEP_ALL_HISTORY_QOS) {
                        if (vit.getSecond().size() < this.m_resourceLimitsQos.maxSamplesPerInstance) {
                            add = true;
                        } else {
                            logger.warn("Change not added due to maximum number of samples per instance");
                            return false;
                        }
                    } else if (this.m_historyQos.kind == HistoryQosPolicyKind.KEEP_LAST_HISTORY_QOS) {
                        if (vit.getSecond().size() < this.m_historyQos.depth) {
                            add = true;
                        } else {
                            boolean read = false;
                            if (vit.getSecond().get(0).isRead()) {
                                read = true;
                            }
                            if (this.removeChangeSub(vit.getSecond().get(0), vit)) {
                                if (!read) {
                                    this.decreadeUnreadCount();
                                }
                                add = true;
                            }
                        }
                    }
                    if (add) {
                        if (this.addChange(change)) {
                            this.increaseUnreadCount();
                            if (change.getSequenceNumber().isLowerThan(this.m_maxSeqCacheChange.getSequenceNumber())) {
                                this.sortCacheChanges();
                            }
                            this.updateMaxMinSeqNum();
                            if (this.m_changes.size() == this.m_resourceLimitsQos.maxSamples) {
                                this.m_isHistoryFull = true;
                            }
                            
                            if (vit.getSecond().size() == 0) {
                                vit.getSecond().add(change);
                            } else if (vit.getSecond().get(vit.getSecond().size()-1).getSequenceNumber().isLowerThan(change.getSequenceNumber())) {
                                vit.getSecond().add(change);
                            } else {
                                vit.getSecond().add(change);
                                Collections.sort(vit.getSecond());
                            }
                            logger.info("Change " + change.getSequenceNumber().toLong() + " added from: " + change.getWriterGUID() + "with kEY: " + change.getInstanceHandle());
                            return true;
                        }
                    } else {
                        return false;
                    }
                }
            }
            return false;
            
        } finally {
            this.m_mutex.unlock();
        }
    }
    
    public void increaseUnreadCount() {
        ++this.m_unreadCacheCount;
    }
    
    public void decreadeUnreadCount() {
        if (this.m_unreadCacheCount > 0) {
            --this.m_unreadCacheCount;
        }
    }
    
    private Pair<InstanceHandle, List<CacheChange>> findKey(CacheChange change) {
        for (Pair<InstanceHandle, List<CacheChange>> entry : this.m_keyedChanges) {
            if (change.getInstanceHandle().equals(entry.getFirst())) {
                return entry;
            }
        }
        
        if (this.m_keyedChanges.size() < this.m_resourceLimitsQos.maxInstances) {
            Pair<InstanceHandle, List<CacheChange>> newPair;
            newPair = new Pair<InstanceHandle, List<CacheChange>>(change.getInstanceHandle(), new ArrayList<CacheChange>());
            this.m_keyedChanges.add(newPair);
            return newPair;
        } else {
            for (int i=0; i < this.m_keyedChanges.size(); ++i) {
                Pair<InstanceHandle, List<CacheChange>> pair = this.m_keyedChanges.get(i);
                if (pair.getSecond().size() == 0) {
                    this.m_keyedChanges.remove(pair);
                    i--;
                    Pair<InstanceHandle, List<CacheChange>> newPair;
                    newPair = new Pair<InstanceHandle, List<CacheChange>>(change.getInstanceHandle(), new ArrayList<CacheChange>());
                    this.m_keyedChanges.add(newPair);
                    return newPair;
                }
            }
        }
        
        return null;
    }
    
    public boolean removeChangeSub(CacheChange change, Pair<InstanceHandle, List<CacheChange>> vit_in) {
        this.m_mutex.lock();
        try {
            
            if (this.m_subscriber.getAttributes().topic.topicKind == TopicKind.NO_KEY) {
                return this.removeChange(change);
            } else {
                Pair<InstanceHandle, List<CacheChange>> vit;
                if (vit_in != null) {
                    vit = vit_in;
                } else {
                    vit = this.findKey(change);
                    if (vit == null) {
                        return false;
                    }
                } 
                
                for (int i=0; i < vit.getSecond().size(); ++i) {
                    CacheChange changeit = vit.getSecond().get(i);
                    if (changeit.getSequenceNumber().equals(change.getSequenceNumber()) && changeit.getWriterGUID().equals(change.getWriterGUID())) {
                        if (this.removeChange(change)) {
                            vit.getSecond().remove(changeit);
                            i--;
                            return true;
                        }
                    }
                }
                logger.error("Change not found, something went wrong");
            }
            
            return false;
            
        } finally {
            this.m_mutex.unlock();
        }
    }

    public long getUnreadCount() {
        return this.m_unreadCacheCount;
    }

    public <T extends Serializable> SerializableDataType<T> readNextData(SampleInfo info) {
        this.m_mutex.lock();
        try {
            
            CacheChange change = new CacheChange();
            WriterProxy proxy = new WriterProxy();
            
            if (this.m_reader.nextUnreadCache(change, proxy)) {
                change.setRead(true);
                this.decreadeUnreadCount();
                logger.info(this.m_reader.getGuid().getEntityId() + ": reading " + change.getSequenceNumber().toLong());
                if (change.getKind() == ChangeKind.ALIVE) {
                    try {
                        this.m_subscriber.getType().deserialize(change.getSerializedPayload());
                    } catch (InstantiationException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (info != null) {
                    info.sampleKind = change.getKind();
                    info.writerGUID = change.getWriterGUID();
                    info.sourceTimestamp = change.getSourceTimestamp();
                    if (this.m_subscriber.getAttributes().qos.ownership.kind == OwnershipQosPolicyKind.EXCLUSIVE_OWNERSHIP_QOS) {
                        info.ownershipStrength = proxy.att.ownershipStrength;
                    }
                    if (this.m_subscriber.getAttributes().topic.topicKind == TopicKind.WITH_KEY &&
                            change.getInstanceHandle().equals(new InstanceHandle()) &&
                            change.getKind() == ChangeKind.ALIVE) {
                        
                    }
                    info.handle = change.getInstanceHandle();
                }
                //return this.m_subscriber.getType();
            }
            return null;
            
        } finally {
            this.m_mutex.unlock();
        }
    }
    
    public Serializable takeNextData(SampleInfo info) {
        this.m_mutex.lock();
        try {
            Serializable retVal = null;
            CacheChange change = new CacheChange();
            WriterProxy wp = new WriterProxy();
            if (this.m_reader.nextUntakenCache(change, wp)) {
                if (!change.isRead()) {
                    this.decreadeUnreadCount();
                }
                change.setRead(true);
                logger.info("Taking seqNum {} from writer {}", change.getSequenceNumber().toLong(), change.getWriterGUID());
                if (change.getKind() == ChangeKind.ALIVE) {
                    try {
                        retVal = this.m_subscriber.getType().deserialize(change.getSerializedPayload());
                    } catch (InstantiationException | IllegalAccessException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                   /* change.getSerializedPayload().setData(retVal);
                    try {
                        change.getSerializedPayload().deserializeData();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }*/
                }
                if (info != null) {
                    info.sampleKind = change.getKind();
                    info.writerGUID = change.getWriterGUID();
                    info.sourceTimestamp = change.getSourceTimestamp();
                    if (this.m_subscriber.getAttributes().qos.ownership.kind == OwnershipQosPolicyKind.EXCLUSIVE_OWNERSHIP_QOS) {
                        info.ownershipStrength = wp.att.ownershipStrength;
                    }
                    if (this.m_subscriber.getAttributes().topic.topicKind == TopicKind.WITH_KEY &&
                            change.getInstanceHandle().equals(new InstanceHandle()) && 
                            change.getKind() == ChangeKind.ALIVE) {
                        this.m_subscriber.getType().getKey(this.m_subscriber.getType(), change.getInstanceHandle()); // TODO Check this
                    }
                    info.handle = change.getInstanceHandle();
                }
                this.removeChangeSub(change, null);
                return retVal;
            }
        } finally {
            this.m_mutex.unlock();
        }
        return null;
    }

    public <T> TopicDataType<T> takeNextData_old(SampleInfo info) {
        this.m_mutex.lock();
        try {
            TopicDataType<T> retVal = null;
            CacheChange change = new CacheChange();
            WriterProxy wp = new WriterProxy();
            if (this.m_reader.nextUntakenCache(change, wp)) {
                if (!change.isRead()) {
                    this.decreadeUnreadCount();
                }
                change.setRead(true);
                logger.info("Taking seqNum {} from writer {}", change.getSequenceNumber().toLong(), change.getWriterGUID());
                if (change.getKind() == ChangeKind.ALIVE) {
                    /*Serializable instance = (Serializable) this.m_subscriber.getType().createData();
                    change.getSerializedPayload().setData(instance);
                    try {
                        change.getSerializedPayload().deserializeData();
                        retVal = (T) instance;
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }*/
                    
                    //retVal = change.getSerializedPayload().get
                    
                }
                if (info != null) {
                    info.sampleKind = change.getKind();
                    info.writerGUID = change.getWriterGUID();
                    info.sourceTimestamp = change.getSourceTimestamp();
                    if (this.m_subscriber.getAttributes().qos.ownership.kind == OwnershipQosPolicyKind.EXCLUSIVE_OWNERSHIP_QOS) {
                        info.ownershipStrength = wp.att.ownershipStrength;
                    }
                    if (this.m_subscriber.getAttributes().topic.topicKind == TopicKind.WITH_KEY &&
                            change.getInstanceHandle().equals(new InstanceHandle()) && 
                            change.getKind() == ChangeKind.ALIVE) {
                        this.m_subscriber.getType().getKey(this.m_subscriber.getType(), change.getInstanceHandle()); // TODO Check this
                    }
                    info.handle = change.getInstanceHandle();
                }
                this.removeChangeSub(change, null);
                return retVal;
            }
        } finally {
            this.m_mutex.unlock();
        }
        return null;
    }

    
    

}
