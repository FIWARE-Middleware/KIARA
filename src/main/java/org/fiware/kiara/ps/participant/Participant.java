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
package org.fiware.kiara.ps.participant;

import java.util.ArrayList;
import java.util.List;

import org.fiware.kiara.ps.attributes.ParticipantAttributes;
import org.fiware.kiara.ps.attributes.PublisherAttributes;
import org.fiware.kiara.ps.attributes.SubscriberAttributes;
import org.fiware.kiara.ps.publisher.Publisher;
import org.fiware.kiara.ps.publisher.PublisherListener;
import org.fiware.kiara.ps.qos.policies.DurabilityQosPolicyKind;
import org.fiware.kiara.ps.qos.policies.ReliabilityQosPolicyKind;
import org.fiware.kiara.ps.rtps.RTPSDomain;
import org.fiware.kiara.ps.rtps.attributes.ReaderAttributes;
import org.fiware.kiara.ps.rtps.attributes.WriterAttributes;
import org.fiware.kiara.ps.rtps.common.DurabilityKind;
import org.fiware.kiara.ps.rtps.common.EndpointKind;
import org.fiware.kiara.ps.rtps.common.ReliabilityKind;
import org.fiware.kiara.ps.rtps.common.TopicKind;
import org.fiware.kiara.ps.rtps.history.WriterHistoryCache;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.SerializedPayload;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipantDiscoveryInfo;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipantListener;
import org.fiware.kiara.ps.rtps.reader.RTPSReader;
import org.fiware.kiara.ps.rtps.writer.RTPSWriter;
import org.fiware.kiara.ps.subscriber.Subscriber;
import org.fiware.kiara.ps.subscriber.SubscriberListener;
import org.fiware.kiara.ps.topic.TopicDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class Participant used to group Publishers and Subscribers into a single
 * working unit.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class Participant {

    private static final Logger logger = LoggerFactory.getLogger(Participant.class);

    private ParticipantAttributes m_att;

    private RTPSParticipant m_rtpsParticipant;

    private ParticipantListener m_listener;

    private MyRTPSParticipantListener m_rtpsListener;

    private List<Publisher<?>> m_publishers;

    private List<Subscriber<?>> m_subscribers;

    private List<TopicDataType<?>> m_types;

    /**
     * RTPSParticipantListener implementation to be used in the Participant class
     * 
     * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
     *
     */
    public class MyRTPSParticipantListener extends RTPSParticipantListener {

        private Participant m_participant;

        /**
         * MyRTPSParticipantListener constructor
         * 
         * @param part The associated Participant 
         */
        public MyRTPSParticipantListener(Participant part) {
            this.m_participant = part;
        }

        /**
         * Method to be executed when a new RTPSParticipant is discovered
         */
        @Override
        public void onRTPSParticipantDiscovery(RTPSParticipant participant, RTPSParticipantDiscoveryInfo rtpsinfo) {
            if (this.m_participant.m_listener != null) {
                ParticipantDiscoveryInfo info = new ParticipantDiscoveryInfo();
                info.rtps = rtpsinfo;
                this.m_participant.m_rtpsParticipant = participant;
                this.m_participant.m_listener.onParticipantDiscovery(this.m_participant, info);
            }
        }

    }

    /**
     * Participant constructor
     * 
     * @param participantAttributes The ParticipantAttributes of the Participant
     * @param listener The ParticipantListener associated to the Participant
     */
    public Participant(ParticipantAttributes participantAttributes, ParticipantListener listener) {
        this.m_att = participantAttributes;
        this.m_rtpsParticipant = null;
        this.m_listener = listener;

        this.m_publishers = new ArrayList<Publisher<?>>();
        this.m_subscribers = new ArrayList<Subscriber<?>>();
        this.m_types = new ArrayList<TopicDataType<?>>();

        this.m_rtpsListener = new MyRTPSParticipantListener(this);
    }

    /**
     * Destroys the Participant and all the associated Endpoints
     */
    public void destroy() {
        this.m_rtpsParticipant.destroy();

        while (this.m_publishers.size() > 0) {
            this.removePublisher(this.m_publishers.get(0));
        }
        while (this.m_subscribers.size() > 0) {
            this.removeSubscriber(this.m_subscribers.get(0));
        }

        RTPSDomain.removeRTPSParticipant(this.m_rtpsParticipant);
    }

    /**
     * Create a Publisher in this Participant.
     *
     * @param att Attributes of the Publisher.
     * @param listener reference to the listener.
     * @return reference to the created Publisher.
     */
    @SuppressWarnings("unchecked")
    public <T> Publisher<T> createPublisher(PublisherAttributes att, PublisherListener listener) {
        //SerializableDataType<?> type = getRegisteredType(att.topic.topicDataTypeName);
        TopicDataType<T> type = null;

        try {
            type = (TopicDataType<T>) getRegisteredType(att.topic.topicDataTypeName);
        } catch (ClassCastException e) {
            logger.warn("Registered type {} cannot be casted and returned", att.topic.topicDataTypeName);
            return null;
        }

        logger.info("Creating Publisher in Topic " + att.topic.topicName);

        if (type == null) {
            logger.error("Type : " + att.topic.topicDataTypeName + " Not Registered");
            return null;
        }

        if (att.topic.topicKind == TopicKind.WITH_KEY && !type.isGetKeyDefined()) {
            logger.error("Keyed Topic needs getKey function");
            return null;
        }

        if (this.m_att.rtps.builtinAtt.useStaticEDP) {
            if (att.getUserDefinedID() <= 0) {
                logger.error("Static EDP requires user defined Id");
                return null;
            }
        }

        if (!att.unicastLocatorList.isValid()) {
            logger.error("Unicast Locator List for Publisher contains invalid Locator");
            return null;
        }

        if (!att.multicastLocatorList.isValid()) {
            logger.error("Multicast Locator List for Publisher contains invalid Locator");
            return null;
        }

        if (!att.qos.checkQos() || !att.topic.checkQos()) {
            return null;
        }

        Publisher<T> publisher = new Publisher<T>(this, type, att, listener);
        publisher.setRTPSParticipant(this.m_rtpsParticipant);

        WriterAttributes writerAtt = new WriterAttributes();
        writerAtt.endpointAtt.durabilityKind = att.qos.durability.kind == DurabilityQosPolicyKind.VOLATILE_DURABILITY_QOS ? DurabilityKind.VOLATILE : DurabilityKind.TRANSIENT_LOCAL;
        writerAtt.endpointAtt.endpointKind = EndpointKind.WRITER;
        writerAtt.endpointAtt.multicastLocatorList.copy(att.multicastLocatorList);
        writerAtt.endpointAtt.reliabilityKind = att.qos.reliability.kind == ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS ? ReliabilityKind.RELIABLE : ReliabilityKind.BEST_EFFORT;
        writerAtt.endpointAtt.topicKind = att.topic.topicKind;
        writerAtt.endpointAtt.unicastLocatorList.copy(att.unicastLocatorList);

        if (att.getEntityId() > 0) {
            writerAtt.endpointAtt.setEntityID(att.getEntityId());
        }

        if (att.getUserDefinedID() > 0) {
            writerAtt.endpointAtt.setUserDefinedID(att.getUserDefinedID());
        }

        writerAtt.times = att.times;

        //RTPSWriter writer = RTPSDomain. TODO continue impl
        RTPSWriter writer = RTPSDomain.createRTPSWriter(this.m_rtpsParticipant, writerAtt, (WriterHistoryCache) publisher.getHistory(), publisher.getWriterListener());
        if (writer == null) {
            logger.error("Problem creating associated Writer");
            return null;
        }

        publisher.setWriter(writer);

        this.m_publishers.add(publisher);

        this.m_rtpsParticipant.registerWriter(writer, att.topic, att.qos);

        logger.info("Publisher {} created in topic {}", publisher.getGuid(), att.topic.topicName);

        return publisher;
    }

    /**
     * Create a Subscriber in this Participant.
     *
     * @param att Attributes of the Subscriber
     * @param listener reference to the listener.
     * @return reference to the created Subscriber.
     */
    @SuppressWarnings("unchecked")
    public <T> Subscriber<T> createSubscriber(SubscriberAttributes att, SubscriberListener listener) {

        logger.info("Creating Subscriber in Topic: " + att.topic.topicName);

        //TopicDataType type = getRegisteredType(att.topic.topicDataTypeName);
        //SerializableDataType<?> type = getRegisteredType(att.topic.topicDataTypeName);
        TopicDataType<T> type = null;
        try {
            type = (TopicDataType<T>) getRegisteredType(att.topic.topicDataTypeName);
        } catch (ClassCastException e) {
            logger.warn("Registered type {} cannot be casted and returned", att.topic.topicDataTypeName);
            return null;
        }

        if (type == null) {
            logger.error("Type : " + att.topic.topicDataTypeName + " Not Registered");
            return null;
        }

        if (att.topic.topicKind == TopicKind.WITH_KEY && !type.isGetKeyDefined()) {
            logger.error("Keyed Topic needs getKey function");
            return null;
        }

        if (this.m_att.rtps.builtinAtt.useStaticEDP) {
            if (att.getUserDefinedID() <= 0) {
                logger.error("Static EDP requires user defined Id");
                return null;
            }
        }

        if (!att.unicastLocatorList.isValid()) {
            logger.error("Unicast Locator List for Publisher contains invalid Locator");
            return null;
        }

        if (!att.multicastLocatorList.isValid()) {
            logger.error("Multicast Locator List for Publisher contains invalid Locator");
            return null;
        }

        if (!att.qos.checkQos() || !att.topic.checkQos()) {
            return null;
        }

        Subscriber<T> subscriber = new Subscriber<T>(this, type, att, listener);
        subscriber.setRTPSParticipant(this.m_rtpsParticipant);

        ReaderAttributes ratt = new ReaderAttributes();
        ratt.endpointAtt.durabilityKind = att.qos.durability.kind == DurabilityQosPolicyKind.VOLATILE_DURABILITY_QOS ? DurabilityKind.VOLATILE : DurabilityKind.TRANSIENT_LOCAL;
        ratt.endpointAtt.endpointKind = EndpointKind.READER;
        ratt.endpointAtt.multicastLocatorList.copy(att.multicastLocatorList);
        ratt.endpointAtt.reliabilityKind = att.qos.reliability.kind == ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS ? ReliabilityKind.RELIABLE : ReliabilityKind.BEST_EFFORT;
        ratt.endpointAtt.topicKind = att.topic.topicKind;
        ratt.endpointAtt.unicastLocatorList.copy(att.unicastLocatorList);
        ratt.expectsInlineQos = att.expectsInlineQos;
        if (att.getEntityID() > 0) {
            ratt.endpointAtt.setEntityID(att.getEntityID());
        }
        if (att.getUserDefinedID() > 0) {
            ratt.endpointAtt.setUserDefinedID(att.getUserDefinedID());
        }
        ratt.times = att.times;

        RTPSReader reader = RTPSDomain.createRTPSReader(this.m_rtpsParticipant, ratt, subscriber.getHistory(), subscriber.getReaderListener());
        if (reader == null) {
            logger.error("Problem creating associated reader");
            return null;
        }

        subscriber.setReader(reader);

        this.m_subscribers.add(subscriber);

        this.m_rtpsParticipant.registerReader(reader, att.topic, att.qos);
        
        logger.info("Subscriber {} created in topic {}", subscriber.getGuid(), att.topic.topicName);

        return subscriber;
    }

    /**
     * Remove a Publisher from this participant.
     *
     * @param pub reference to the Publisher.
     * @return True if correctly removed.
     */
    public boolean removePublisher(Publisher<?> pub) {
        for (int i = 0; i < this.m_publishers.size(); ++i) {
            Publisher<?> it = this.m_publishers.get(i);
            if (it.getGuid().equals(pub.getGuid())) {
                it.destroy();
                this.m_publishers.remove(it);
                return true;
            }
        }
        return true;
    }

    /**
     * Remove a Subscriber from this participant.
     *
     * @param sub reference to the Subscriber.
     * @return True if correctly removed.
     */
    public boolean removeSubscriber(Subscriber<?> sub) {
        for (int i = 0; i < this.m_subscribers.size(); ++i) {
            Subscriber<?> it = this.m_subscribers.get(i);
            if (it.getGuid().equals(sub.getGuid())) {
                it.destroy();
                this.m_subscribers.remove(it);
                return true;
            }
        }
        return true;
    }

    /**
     * Register a type in this participant.
     *
     * @param type reference to the {@link TopicDataType}.
     * @return True if registered.
     */
    public boolean registerType(TopicDataType<?> type) {

        if (type.getTypeSize() <= 0) {
            logger.error("Registered Type must have maximum byte size > 0");
            return false;
        }

        if (type.getTypeSize() > SerializedPayload.PAYLOAD_MAX_SIZE) {
            logger.error("Current version only supports types of sizes < " + SerializedPayload.PAYLOAD_MAX_SIZE);
            return false;
        }

        if (type.getName().length() <= 0) {
            logger.error("Registered Type must have a name");
            return false;
        }

        for (TopicDataType<?> it : this.m_types) {
            if (it.getName().equals(type.getName())) {
                logger.error("Type with the same name already exists");
                return false;
            }
        }

        this.m_types.add(type);
        logger.info("Type " + type.getName() + " registered");
        return true;
    }

    /**
     * Returns type registered with specified type name.
     *
     * @param typeName type name
     * @return type or null if no type with specified name is registered
     */
    public TopicDataType<?> getRegisteredType(String typeName) {

        for (TopicDataType<?> type : this.m_types) {
            if (type.getName().equals(typeName)) {
                return type;
            }
        }

        return null;
    }

    /**
     * Get the {@link GUID} of the associated RTPSParticipant.
     *
     * @return {@link GUID}.
     */
    public GUID getGuid() {
        return this.m_rtpsParticipant.getGUID();
    }

    /**
     * Get the participant attributes
     *
     * @return Participant attributes
     */
    public ParticipantAttributes getAttributes() {
        return this.m_att;

    }

    /**
     * This method can be used when using a StaticEndpointDiscovery mechanism
     * different that the one included in FastRTPS, for example when
     * communicating with other implementations. It indicates the Participant
     * that an Endpoint from the XML has been discovered and should be
     * activated.
     *
     * @param paricipantGuid Participant {@link GUID}.
     * @param userId User defined ID as shown in the XML file.
     * @param kind EndpointKind (WRITER or READER)
     * @return True if correctly found and activated.
     */
    public boolean newRemoteEndpointDiscovered(GUID paricipantGuid, short userId, EndpointKind kind) {
        return this.m_rtpsParticipant.newRemoteEndpointDiscovered(paricipantGuid, userId, kind);
    }

    /**
     * Get the participant listener
     *
     * @return Participant listener
     */
    public RTPSParticipantListener getListener() {
        return this.m_rtpsListener;
    }

    /**
     * Set RTPSParticipant
     *
     * @param part RTPSParticipant
     */
    public void setRTPSParticipant(RTPSParticipant part) {
        this.m_rtpsParticipant = part;
    }

    /**
     * Get SPDP unicast port
     *
     * @return SPDP unicast port
     */
    public int getSPDPUnicastPort() {
        if (this.m_rtpsParticipant != null) {
            return this.m_rtpsParticipant.getSPDPUnicastPort();
        }
        return -1;
    }

    /**
     * Get SPDP multicast port
     *
     * @return SPDP multicast port
     */
    public int getSPDPMulticastPort() {
        if (this.m_rtpsParticipant != null) {
            return this.m_rtpsParticipant.getSPDPMulticastPort();
        }
        return -1;
    }

    /**
     * Get user unicast port
     *
     * @return user unicast port
     */
    public int getUserUnicastPort() {
        if (this.m_rtpsParticipant != null) {
            return this.m_rtpsParticipant.getUserUnicastPort();
        }
        return -1;
    }

    /**
     * Get user multicast port
     *
     * @return user multicast port
     */
    public int getUserMulticastPort() {
        if (this.m_rtpsParticipant != null) {
            return this.m_rtpsParticipant.getUserMulticastPort();
        }
        return -1;
    }

}
