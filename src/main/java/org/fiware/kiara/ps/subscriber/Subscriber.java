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
import org.fiware.kiara.ps.rtps.history.HistoryCache;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.reader.RTPSReader;
import org.fiware.kiara.ps.rtps.reader.ReaderListener;
import org.fiware.kiara.ps.rtps.reader.StatefulReader;
import org.fiware.kiara.ps.topic.TopicDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class Subscriber, contains the public API that allows the user to control the reception of messages.
 * This class should not be instantiated directly. DomainRTPSParticipant class should be used to correctly create this element.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 * @param <T> The type the {@link Subscriber} will subscribe to
 */
public class Subscriber<T> {

    /**
     * Reader listener for the Subscriber
     * 
     * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
     *
     */
    private class SubscriberReaderListener extends ReaderListener {

        /**
         * {@link Subscriber} that will create and use the {@link SubscriberReaderListener}
         */
        private Subscriber<T> m_subscriber;

        /**
         * Default {@link SubscriberReaderListener} constructor
         * @param subscriber The {@link Subscriber} thet creates this {@link SubscriberReaderListener}
         */
        public SubscriberReaderListener(Subscriber<T> subscriber) {
            this.m_subscriber = subscriber;
        }

        /**
         * Method to be executed when a new Reader matches with the Writer
         */
        @Override
        public void onReaderMatched(RTPSReader reader, MatchingInfo info) {
            if (this.m_subscriber.m_listener != null) {
                this.m_subscriber.m_listener.onSubscriptionMatched(this.m_subscriber, info);
            }

        }

        /**
         * Method to be executed when a new CacheChange has been added
         */
        @Override
        public void onNewCacheChangeAdded(RTPSReader reader, CacheChange change) {
            if (this.m_subscriber.m_listener != null) {
                this.m_subscriber.m_listener.onNewDataMessage(this.m_subscriber);
            }
        }

    }

    /**
     * {@link Participant} that creates the {@link Subscriber}
     */
    private Participant m_participant;

    /**
     * {@link TopicDataType} the {@link Subscriber} is able to subscribe to
     */
    private TopicDataType<T> m_type;

    /**
     * Attributes of the {@link Subscriber}
     */
    private SubscriberAttributes m_att;

    /**
     * {@link HistoryCache} of the {@link Subscriber}
     */
    private SubscriberHistory<T> m_history;

    /**
     * Listener of the {@link Subscriber}
     */
    private SubscriberListener m_listener;

    /**
     * Internal Reader listener implementing the {@link ReaderListener} interface
     */
    private SubscriberReaderListener m_readerListener;

    /**
     * {@link RTPSReader} associated to this {@link Subscriber}
     */
    private RTPSReader m_reader;

    /**
     * {@link RTPSParticipant} in which the {@link Subscriber} is registered
     */
    private RTPSParticipant m_rtpsParticipant;

    /**
     * Logging object
     */
    private static final Logger logger = LoggerFactory.getLogger(Subscriber.class);

    /**
     * Publisher constructor
     * 
     * @param participant {@link Participant} that creates the Subscriber 
     * @param type {@link TopicDataType} associated to the Subscriber
     * @param att {@link SubscriberAttributes} of the Subscriber
     * @param listener {@link SubscriberListener} reference to be called when an event occurs
     */
    public Subscriber(Participant participant, TopicDataType<T> type, SubscriberAttributes att, SubscriberListener listener) {
        this.m_participant = participant;
        this.m_readerListener = null;
        this.m_type = type;
        this.m_att = att;

        this.m_history = new SubscriberHistory<T>(this, type.getTypeSize(), att.topic.historyQos, att.topic.resourceLimitQos);
        this.m_listener = listener;

        this.m_readerListener = new SubscriberReaderListener(this);

    }

    /**
     * Commands the Subscriber to wait for unsead messages
     */
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

    /**
     * Reads and returns next data from the HistoryCache
     * 
     * @param info The SampleInfo
     * @return The sample data type
     */
    public T readNextData(SampleInfo info) {
        return (T) this.m_history.readNextData(info);
    }

    /**
     * Takes and removes the next data from the HistoryCache
     * 
     * @param info The SampleInfo
     * @return The sample data type
     */
    public T takeNextData(SampleInfo info) {
        return (T) this.m_history.takeNextData(info);
    }

    /**
     * Updated the SubscriberAttributes reference
     * 
     * @param att The SubscriberAttributes with the new changes
     * @return true if the SubscriberAttributes can be updated; false otherwise
     */
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

    /**
     * Get the GUID
     * 
     * @return The GUID
     */
    public GUID getGuid() {
        return this.m_reader.getGuid();
    }

    /**
     * Get the registered TopicDataType
     * 
     * @return The registered TopicDataType 
     */
    public TopicDataType<T> getType() {
        return this.m_type;
    }

    /**
     * Get the SubscriberAttributes of the Subscriber
     * 
     * @return The SubscriberAttributes
     */
    public SubscriberAttributes getAttributes() {
        return this.m_att;
    }

    /**
     * Set the RTPSParticipant reference
     * 
     * @param participant The RTPSParticipant to be set
     */
    public void setRTPSParticipant(RTPSParticipant participant) {
        this.m_rtpsParticipant = participant;
    }

    /**
     * Get the SubscriberHistory of the Subscriber
     * 
     * @return The SubscriberHistory
     */
    public SubscriberHistory<T> getHistory() {
        return this.m_history;
    }

    /**
     * Get the ReaderListener reference
     * 
     * @return The ReaderListener
     */
    public ReaderListener getReaderListener() {
        return this.m_readerListener;
    }

    /**
     * Set the RTPSReader reference
     * 
     * @param reader The RTPSReader to be set
     */
    public void setReader(RTPSReader reader) {
        this.m_reader = reader;
    }

    /**
     * Get the RTPSReader reference
     * 
     * @return The RTPSReader
     */
    public RTPSReader getReader() {
        return this.m_reader;
    }

    /**
     * Destroy the information in the Subscriber
     */
    public void destroy() {
        RTPSDomain.removeRTPSReader(this.m_reader);
        logger.debug("Subscriber destroyed (Reader GUID: {})", this.getGuid());
    }

    /**
     * Get the {@link Participant} who created the {@link Subscriber}
     * 
     * @return The {@link Participant} who created the {@link Subscriber}
     */
    public Participant getParticipant() {
        return m_participant;
    }

}
