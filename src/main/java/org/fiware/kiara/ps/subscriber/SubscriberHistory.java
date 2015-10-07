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
package org.fiware.kiara.ps.subscriber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
import org.fiware.kiara.ps.rtps.reader.WriterProxy;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.Serializable;
import org.fiware.kiara.util.Pair;
import org.fiware.kiara.util.ReturnParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that represents the Subscriber's HistoryCache.
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 * @param <T>
 *
 */
public class SubscriberHistory<T> extends ReaderHistoryCache {
    
    /**
     * Number of unread {@link CacheChange} objects
     */
    private long m_unreadCacheCount;
    
    /**
     * Keyes {@link CacheChange} objects
     */
    private List<Pair<InstanceHandle, List<CacheChange>>> m_keyedChanges;
    
    /**
     * {@link HistoryQosPolicy} indicating the QoS policy
     */
    private HistoryQosPolicy m_historyQos;
    
    /**
     * {@link ResourceLimitsQosPolicy} object indicating the resource limit QoS policy
     */
    private ResourceLimitsQosPolicy m_resourceLimitsQos;
    
    /**
     * {@link Subscriber} this {@link SubscriberHistory} is associated to
     */
    private Subscriber<T> m_subscriber;
    
    /**
     * Object for the KAY
     */
    private T m_getKeyObject;
    
    /**
     * Mutex
     */
    private final Lock m_mutex = new ReentrantLock(true);
    
    /**
     * Logging object
     */
    private static final Logger logger = LoggerFactory.getLogger(SubscriberHistory.class);
    
    /**
     * SubscriberHistory constructor
     *
     * @param subscriber The Subscriber to which this HistoryCache belongs
     * @param payloadMaxSize Maximum payload size
     * @param history HistoryQosPolicy for this HistoryCache
     * @param resource ResourceLimitsQosPolicy to be applied
     */
    public SubscriberHistory(Subscriber<T> subscriber, int payloadMaxSize, HistoryQosPolicy history, ResourceLimitsQosPolicy resource) {
        super(new HistoryCacheAttributes(payloadMaxSize, resource.allocatedSamples, resource.maxSamples));
        this.m_unreadCacheCount = 0;
        this.m_historyQos = history;
        this.m_resourceLimitsQos = resource;
        this.m_subscriber = subscriber;
        this.m_getKeyObject = this.m_subscriber.getType().createData();
        this.m_keyedChanges = new ArrayList<Pair<InstanceHandle,List<CacheChange>>>();
        
    }
    
    /*
     * (non-Javadoc)
     * @see org.fiware.kiara.ps.rtps.history.ReaderHistoryCache#receivedChange(org.fiware.kiara.ps.rtps.history.CacheChange)
     */
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
                        logger.debug("Change (seqNum: {}) already in ReaderHistory", change.getSequenceNumber().toLong());
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
                                this.decreaseUnreadCount();
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
                        logger.debug("Change added from {} ", change.getWriterGUID());
                        return true;
                    }
                } else {
                    return false;
                }
                
            } else if (this.m_subscriber.getAttributes().topic.topicKind == TopicKind.WITH_KEY) { // History with key
                if (!change.getInstanceHandle().isDefined() && this.m_subscriber.getType() != null) {
                    logger.debug("Getting Key of change with no Key transmitted");
                    //((T) this.m_getKeyObject)
                    try {
                        ((Serializable) this.m_getKeyObject).deserialize(change.getSerializedPayload().getSerializer(), new BinaryInputStream(change.getSerializedPayload().getBuffer()), "");
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
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
                                    this.decreaseUnreadCount();
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
                            logger.debug("Change {} added from {} with key {}", change.getSequenceNumber().toLong(), change.getWriterGUID(), change.getInstanceHandle());
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
    
    /**
     * Increaes the number of unread samples
     */
    public void increaseUnreadCount() {
        ++this.m_unreadCacheCount;
    }
    
    /**
     * Decreases the number of unread samples
     */
    public void decreaseUnreadCount() {
        if (this.m_unreadCacheCount > 0) {
            --this.m_unreadCacheCount;
        }
    }
    
    /**
     * Finds a InstanceHandle in the SubscriberHistory and returns its associated CacheChange references
     * 
     * @param change Che CacheChange to fe found
     * @return A Pair formed by the InstanceHandle and its list of CacheChange references
     */
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
    
    /**
     * Removes the CacheChange list associated to a specific InstanceHandle
     * 
     * @param change The {@link CacheChange} to be removed
     * @param vit_in The KEYED Pair for this {@link CacheChange}
     * @return true on success; false otherwise
     */
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

    /**
     * Get the number of unread changes
     * 
     * @return The number of CacheChange elements that are unread
     */
    public long getUnreadCount() {
        return this.m_unreadCacheCount;
    }
    
    /**
     * Reads next data in the SubscriberHistory
     * 
     * @param info The associated SampleInfo
     * @return The read data
     */
    public T readNextData(SampleInfo info) {
        this.m_mutex.lock();
        try {

            ReturnParam<CacheChange> change = new ReturnParam<>();
            ReturnParam<WriterProxy> proxy = new ReturnParam<>();

            if (this.m_reader.nextUnreadCache(change, proxy)) {
                change.value.setRead(true);
                this.decreaseUnreadCount();
                logger.info(this.m_reader.getGuid().getEntityId() + ": reading " + change.value.getSequenceNumber().toLong());
                if (change.value.getKind() == ChangeKind.ALIVE) {
                    try {
                        this.m_subscriber.getType().deserialize(change.value.getSerializedPayload());
                    } catch (InstantiationException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (info != null) {
                    info.sampleKind = change.value.getKind();
                    info.writerGUID = change.value.getWriterGUID();
                    info.sourceTimestamp = change.value.getSourceTimestamp();
                    if (this.m_subscriber.getAttributes().qos.ownership.kind == OwnershipQosPolicyKind.EXCLUSIVE_OWNERSHIP_QOS) {
                        info.ownershipStrength = proxy.value.att.ownershipStrength;
                    }
                    if (this.m_subscriber.getAttributes().topic.topicKind == TopicKind.WITH_KEY &&
                            change.value.getInstanceHandle().equals(new InstanceHandle()) &&
                            change.value.getKind() == ChangeKind.ALIVE) {

                    }
                    info.handle = change.value.getInstanceHandle();
                }
                //return this.m_subscriber.getType();
            }
            return null;
            
        } finally {
            this.m_mutex.unlock();
        }
    }

    /**
     * Reads and removes the next data from the SubscriberHistory
     * 
     * @param info The associated SampleInfo
     * @return The read data
     */
    public T takeNextData(SampleInfo info) {
        this.m_mutex.lock();
        try {
            T retVal = null;
            ReturnParam<CacheChange> change = new ReturnParam<CacheChange>();
            ReturnParam<WriterProxy> wp = new ReturnParam<WriterProxy>();
            if (this.m_reader.nextUntakenCache(change, wp)) {
                if (!change.value.isRead()) {
                    this.decreaseUnreadCount();
                }
                change.value.setRead(true);
                logger.debug("Taking seqNum {} from writer {}", change.value.getSequenceNumber().toLong(), change.value.getWriterGUID());
                if (change.value.getKind() == ChangeKind.ALIVE) {
                    try {
                        retVal = (T) this.m_subscriber.getType().deserialize(change.value.getSerializedPayload());
                    } catch (InstantiationException | IllegalAccessException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                 }
                if (info != null) {
                    info.sampleKind = change.value.getKind();
                    info.writerGUID = change.value.getWriterGUID();
                    info.sourceTimestamp = change.value.getSourceTimestamp();
                    if (this.m_subscriber.getAttributes().qos.ownership.kind == OwnershipQosPolicyKind.EXCLUSIVE_OWNERSHIP_QOS) {
                        info.ownershipStrength = wp.value.att.ownershipStrength;
                    }
                    if (this.m_subscriber.getAttributes().topic.topicKind == TopicKind.WITH_KEY &&
                            change.value.getInstanceHandle().equals(new InstanceHandle()) &&
                            change.value.getKind() == ChangeKind.ALIVE) {
                        this.m_subscriber.getType().getKey(retVal, change.value.getInstanceHandle()); // TODO Check this
                    }
                    info.handle = change.value.getInstanceHandle();
                }
                this.removeChangeSub(change.value, null);
                return retVal;
            }
        } finally {
            this.m_mutex.unlock();
        }
        return null;
    }
    
    }
