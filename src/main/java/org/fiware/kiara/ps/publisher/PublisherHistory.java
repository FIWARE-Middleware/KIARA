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
package org.fiware.kiara.ps.publisher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.fiware.kiara.ps.qos.policies.HistoryQosPolicy;
import org.fiware.kiara.ps.qos.policies.HistoryQosPolicyKind;
import org.fiware.kiara.ps.qos.policies.ResourceLimitsQosPolicy;
import org.fiware.kiara.ps.rtps.attributes.HistoryCacheAttributes;
import org.fiware.kiara.ps.rtps.common.TopicKind;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.history.WriterHistoryCache;
import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.fiware.kiara.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public class PublisherHistory extends WriterHistoryCache {
    
    private static final Logger logger = LoggerFactory.getLogger(PublisherHistory.class);
    
    private List<Pair<InstanceHandle, List<CacheChange>>> m_keyedChanges;
    
    private HistoryQosPolicy m_historyQos;
    
    private ResourceLimitsQosPolicy m_resourceLimitsQos;
    
    private Publisher m_publisher;
    
    private final Lock m_mutex = new ReentrantLock(true);
    
    public PublisherHistory(Publisher publisher, int payloadMaxSize, HistoryQosPolicy history, ResourceLimitsQosPolicy resource) {
        super(new HistoryCacheAttributes(payloadMaxSize, resource.allocatedSamples, resource.maxSamples));
        this.m_historyQos = history;
        this.m_resourceLimitsQos = resource;
        this.m_publisher = publisher;
    }
    
    public boolean addPubChange(CacheChange change) {
        
        this.m_mutex.lock();
            
        try {
            
            if (this.m_isHistoryFull && this.m_historyQos.kind == HistoryQosPolicyKind.KEEP_ALL_HISTORY_QOS) {
                logger.warn("Attempting to add Data to Full WriterCache: {} with KEEP ALL History ", this.m_publisher.getGuid().getEntityId());
                return false;
            }
            
            if (this.m_publisher.getAttributes().topic.topicKind == TopicKind.NO_KEY) { // No key history
                boolean add = false;
                if (this.m_historyQos.kind == HistoryQosPolicyKind.KEEP_ALL_HISTORY_QOS) {
                    add = true;
                } else if (this.m_historyQos.kind == HistoryQosPolicyKind.KEEP_LAST_HISTORY_QOS) {
                    if (this.m_changes.size() < this.m_historyQos.depth) {
                        add = true;
                    } else {
                        if (this.removeChangePub(this.m_minSeqCacheChange, null)) {
                            add = true;
                        }
                    }
                }
                if (add) {
                    if (this.addChange(change)) {
                        if (this.m_historyQos.kind == HistoryQosPolicyKind.KEEP_ALL_HISTORY_QOS) {
                            if (this.m_changes.size() == this.m_resourceLimitsQos.maxSamples) {
                                this.m_isHistoryFull = true;
                            }
                        } else {
                            if (this.m_changes.size() == this.m_historyQos.depth) {
                                this.m_isHistoryFull = true;
                            }
                        }
                        return true;
                    }
                }
            } else if (this.m_publisher.getAttributes().topic.topicKind == TopicKind.WITH_KEY) { // History with key
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
                    } else if(this.m_historyQos.kind == HistoryQosPolicyKind.KEEP_LAST_HISTORY_QOS) {
                        if (vit.getSecond().size() < this.m_historyQos.depth) {
                            add = true;
                        } else {
                            if (removeChangePub(vit.getSecond().get(0), vit)) {
                                add = true;
                            }
                        }
                    }
                    
                    if (add) {
                        if (this.addChange(change)) {
                            logger.info("Change added");
                            vit.getSecond().add(change);
                            if (this.m_historyQos.kind == HistoryQosPolicyKind.KEEP_ALL_HISTORY_QOS) {
                                if (this.m_changes.size() == this.m_resourceLimitsQos.maxSamples) {
                                    this.m_isHistoryFull = true;
                                }
                            } else {
                                if (this.m_changes.size() == this.m_historyQos.depth) {
                                    this.m_isHistoryFull = true;
                                }
                            }
                            return true;
                        }
                    }
                }
            }
            
            return false;
            
        } finally {
            this.m_mutex.unlock();
        }
        
    }
    
    public int removeAllChangesNum() {
        int rem = 0;
        while(this.m_changes.size() > 0) {
            if (this.removeChangePub(this.m_changes.get(0), null)) {
                ++rem;
            } else {
                break;
            }
        }
        
        return rem;
    }
    
    /*private int findKeyIndex(CacheChange change) {
        for (Pair<InstanceHandle, List<CacheChange>> entry : this.m_keyedChanges) {
            if (change.getInstanceHandle().equals(entry.getFirst())) {
                return this.m_keyedChanges.indexOf(change);
            }
        }
        
        if (this.m_keyedChanges.size() < this.m_resourceLimitsQos.maxInstances) {
            Pair<InstanceHandle, List<CacheChange>> newPair;
            newPair = new Pair<InstanceHandle, List<CacheChange>>(change.getInstanceHandle(), new ArrayList<CacheChange>());
            this.m_keyedChanges.add(newPair);
            return this.m_keyedChanges.size()-1;
        } else {
            for (Pair<InstanceHandle, List<CacheChange>> pair : this.m_keyedChanges) {
                if (pair.getSecond().size() == 0) {
                    this.m_keyedChanges.remove(pair);
                    Pair<InstanceHandle, List<CacheChange>> newPair;
                    newPair = new Pair<InstanceHandle, List<CacheChange>>(change.getInstanceHandle(), new ArrayList<CacheChange>());
                    this.m_keyedChanges.add(newPair);
                    return this.m_keyedChanges.size()-1;
                }
            }
        }
        
        return -1;
    }*/
    
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
    
    private boolean removeChangePub(CacheChange change, Pair<InstanceHandle, List<CacheChange>> v_it) {
        
        this.m_mutex.lock();
        try {
            
            if (this.m_publisher.getAttributes().topic.topicKind == TopicKind.NO_KEY) {
                return this.removeChange(change);
            } else {
                Pair<InstanceHandle, List<CacheChange>> it;
                if (v_it != null) {
                    it = v_it;
                } else {
                    it = findKey(change);
                }
                
                if (it != null) {
                    //Pair<InstanceHandle, List<CacheChange>> entry = it.next();
                    Iterator<CacheChange> innerIterator = it.getSecond().iterator();
                    while(innerIterator.hasNext()) {
                        CacheChange current = innerIterator.next();
                        if (current.getSequenceNumber().equals(change.getSequenceNumber())) {
                            if (removeChange(change)) {
                                it.getSecond().remove(current);
                                return true;
                            }
                        }
                    }
                    logger.error("Change not found, something went wrong");
                }
            }
            
        } finally {
            this.m_mutex.unlock();
        }
        
        return false;
    }
    
}
