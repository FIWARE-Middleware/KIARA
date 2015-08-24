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

import org.fiware.kiara.ps.attributes.SubscriberAttributes;
import org.fiware.kiara.ps.participant.Participant;
import org.fiware.kiara.ps.qos.policies.ReliabilityQosPolicyKind;
import org.fiware.kiara.ps.rtps.RTPSDomain;
import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.common.MatchingInfo;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.reader.RTPSReader;
import org.fiware.kiara.ps.rtps.reader.ReaderListener;
import org.fiware.kiara.ps.rtps.reader.StatefulReader;
import org.fiware.kiara.ps.topic.SerializableDataType;
import org.fiware.kiara.ps.topic.TopicDataType;
import org.fiware.kiara.serialization.impl.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eprosima.log.Log;

/**
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 * @param <T>
 */
public class Subscriber<T> {

    private class SubscriberReaderListener extends ReaderListener {

        private Subscriber<T> m_subscriber;

        public SubscriberReaderListener(Subscriber<T> s) {
            this.m_subscriber = s;
        }

        @Override
        public void onReaderMatched(RTPSReader reader, MatchingInfo info) {
            if (this.m_subscriber.m_listener != null) {
                this.m_subscriber.m_listener.onSubscriptionMatched(this.m_subscriber, info);
            }

        }

        @Override
        public void onNewCacheChangeAdded(RTPSReader reader, CacheChange change) {
            if (this.m_subscriber.m_listener != null) {
                this.m_subscriber.m_listener.onNewDataMessage(this.m_subscriber);
            }
        }

    }

    private Participant m_participant;

    private TopicDataType<T> m_type_old;
    
    //private SerializableDataType<T> m_type;
    
    private TopicDataType<T> m_type;

    private SubscriberAttributes m_att;

    private SubscriberHistory m_history;

    private SubscriberListener m_listener;

    private SubscriberReaderListener m_readerListener;

    private RTPSReader m_reader;

    private RTPSParticipant m_rtpsParticipant;
    
    private static final Logger logger = LoggerFactory.getLogger(Subscriber.class);

    //private Subscriber m_userSubscriber;

    public Subscriber(Participant p, TopicDataType<T> type, SubscriberAttributes att, SubscriberListener listener) {
        this.m_participant = p;
        this.m_readerListener = null;
        this.m_type = type;
        this.m_att = att;

        this.m_history = new SubscriberHistory(this, type.getTypeSize(), att.topic.historyQos, att.topic.resourceLimitQos);
        this.m_listener = listener;

        this.m_readerListener = new SubscriberReaderListener(this);

    }

    public void waitForUnreadMessage() {
        if (this.m_history.getUnreadCount() == 0) {
            while (true) {
                this.m_history.waitChange();
                if (this.m_history.getUnreadCount() > 0) {
                    break;
                }
            }
        }
    }

    public T readNextData(SampleInfo info) {
        return this.m_history.readNextData(info);
    }

    public T takeNextData(SampleInfo info) {
        return this.m_history.takeNextData(info);
    }
    
    /*public <T extends Serializable> SerializableDataType<T> readNextData(SampleInfo info) {
        return this.m_history.readNextData(info);
    }

    public Serializable takeNextData(SampleInfo info) {
        return this.m_history.takeNextData(info);
    }*/
    
    public boolean updateAttributes(SubscriberAttributes att) {
        boolean updated = true;
        boolean missing = true;
        
        if (att.unicastLocatorList.getLocators().size() != this.m_att.unicastLocatorList.getLocators().size() ||
                att.multicastLocatorList.getLocators().size() != this.m_att.multicastLocatorList.getLocators().size()) {
            logger.warn("Locator Lists cannot be changed or updated in this version");
            updated &= false;
        } else {
            for (Locator locit : this.m_att.unicastLocatorList.getLocators()) {
                missing = true;
                for (Locator locit2 : att.unicastLocatorList.getLocators()) {
                    if (locit.equals(locit2)) {
                        missing = false;
                        break;
                    }
                }
                if (missing) {
                    logger.warn("Locator: " + locit + " not present in new list");
                    logger.warn("Locator Lists cannot be changed or updated in this version");
                }
            }
            for (Locator locit : this.m_att.multicastLocatorList.getLocators()) {
                missing = true;
                for (Locator locit2 : att.multicastLocatorList.getLocators()) {
                    if (locit.equals(locit2)) {
                        missing = false;
                        break;
                    }
                }
                if (missing) {
                    logger.warn("Locator: " + locit + " not present in new list");
                    logger.warn("Locator Lists cannot be changed or updated in this version");
                }
            }
        }
        
        // Topic Attributes
        
        if (!this.m_att.topic.equals(att.topic)) {
            logger.warn("Topic Attributes cannot be updated");
            updated &= false;
        }
        
        if (updated) {
            this.m_att.expectsInlineQos = att.expectsInlineQos;
            if (this.m_att.qos.reliability.kind == ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS) {
                StatefulReader sfr = (StatefulReader) this.m_reader;
                sfr.updateTimes(att.times);
            }
            this.m_att.qos.setQos(att.qos, false);
            this.m_rtpsParticipant.updateReader(this.m_reader, this.m_att.qos);
        }
        
        return updated;
    }

    public GUID getGuid() {
        return this.m_reader.getGuid();
    }

    public TopicDataType<T> getType() {
        return this.m_type;
    }
    
    /*public SerializableDataType<T> getType_old() {
        return this.m_type;
    }*/
    
    public SubscriberAttributes getAttributes() {
        return this.m_att;
    }
    
    public void setRTPSParticipant(RTPSParticipant participant) {
        this.m_rtpsParticipant = participant;
    }
    
    public SubscriberHistory getHistory() {
        return this.m_history;
    }

    public ReaderListener getReaderListener() {
        return this.m_readerListener;
    }

    public void setReader(RTPSReader reader) {
        this.m_reader = reader;
    }
    
    public RTPSReader getReader() {
        return this.m_reader;
    }

    public void destroy() {
        logger.info("Destroying Subscriber (Reader GUID: {})", this.getGuid());
        RTPSDomain.removeRTPSReader(this.m_reader);
    }

}
