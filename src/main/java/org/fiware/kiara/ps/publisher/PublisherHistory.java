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
import java.util.List;
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
 * Class PublisherHistory, implementing a WriterHistory with support for keyed
 * topics and HistoryQOS. This class is created by the PublisherImpl and should
 * not be used by the user directly.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class PublisherHistory extends WriterHistoryCache {

    private static final Logger logger = LoggerFactory.getLogger(PublisherHistory.class);

    private List<Pair<InstanceHandle, List<CacheChange>>> m_keyedChanges;

    private final HistoryQosPolicy m_historyQos;

    private final ResourceLimitsQosPolicy m_resourceLimitsQos;

    private final Publisher m_publisher;

    private final Lock m_mutex = new ReentrantLock(true);

    /**
     * Constructor of the PublisherHistory.
     *
     * @param publisher reference to the Publisher.
     * @param payloadMaxSize Maximum payload size.
     * @param history QOS of the associated History.
     * @param resource ResourceLimits for the History.
     */
    public PublisherHistory(Publisher publisher, int payloadMaxSize, HistoryQosPolicy history, ResourceLimitsQosPolicy resource) {
        super(new HistoryCacheAttributes(payloadMaxSize, resource.allocatedSamples, resource.maxSamples));
        this.m_historyQos = history;
        this.m_resourceLimitsQos = resource;
        this.m_publisher = publisher;
    }

    /**
     * Add a change coming from the Publisher.
     *
     * @param change reference to the change
     * @return True if added.
     */
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
                    } else if (this.m_historyQos.kind == HistoryQosPolicyKind.KEEP_LAST_HISTORY_QOS) {
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

    /**
     * Remove all change from the associated history.
     *
     * @return Number of elements removed.
     */
    public int removeAllChangesNum() {
        int rem = 0;
        while (this.m_changes.size() > 0) {
            if (this.removeChangePub(this.m_changes.get(0), null)) {
                ++rem;
            } else {
                break;
            }
        }

        return rem;
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
            for (int i = 0; i < this.m_keyedChanges.size(); ++i) {
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
     * Remove a change by the publisher History.
     *
     * @param change Pointer to the CacheChange_t.
     * @param vit Pointer to the iterator of the Keyed history vector.
     * @return True if removed.
     */
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
                    for (int i = 0; i < it.getSecond().size(); ++i) {
                        CacheChange current = it.getSecond().get(i);
                        if (current.getSequenceNumber().equals(change.getSequenceNumber())) {
                            if (removeChange(change)) {
                                it.getSecond().remove(current);
                                i--;
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
