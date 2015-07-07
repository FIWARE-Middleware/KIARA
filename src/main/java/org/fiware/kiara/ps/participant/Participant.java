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
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public class Participant {
    
    private static final Logger logger = LoggerFactory.getLogger(Participant.class);
    
    private ParticipantAttributes m_att;
    
    private RTPSParticipant m_rtpsParticipant;
    
    private ParticipantListener m_listener;
    
    private MyRTPSParticipantListener m_rtpsListener;
    
    private List<Publisher> m_publishers;
    
    private List<Subscriber> m_subscribers;
    
    private List<TopicDataType> m_types;
    
    public class MyRTPSParticipantListener extends RTPSParticipantListener {
        
        private Participant m_participant;
        
        public MyRTPSParticipantListener(Participant part) {
            this.m_participant = part;
        }

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
    
    public Participant(ParticipantAttributes participantAttributes, ParticipantListener listener) {
        this.m_att = participantAttributes;
        this.m_rtpsParticipant = null;
        this.m_listener = listener;
        
        this.m_publishers = new ArrayList<Publisher>();
        this.m_subscribers = new ArrayList<Subscriber>();
        this.m_types = new ArrayList<TopicDataType>();
        
        this.m_rtpsListener = new MyRTPSParticipantListener(this);
    }
    
    public void destroy() {
        while (this.m_publishers.size() > 0) {
            this.removePublisher(this.m_publishers.get(0));
        }
        while (this.m_subscribers.size() > 0) {
            this.removeSubscriber(this.m_subscribers.get(0));
        }
   
        RTPSDomain.removeRTPSParticipant(this.m_rtpsParticipant);
    }
    
    public Publisher createPublisher(PublisherAttributes att, PublisherListener listener) {
        TopicDataType<?> type = getRegisteredType(att.topic.topicDataTypeName);
        
        logger.info("Creating Publisher in Topic: " + att.topic.topicName);
        
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
        
        Publisher publisher = new Publisher(this, type, att, listener);
        publisher.setRTPSParticipant(this.m_rtpsParticipant);
        
        WriterAttributes writerAtt = new WriterAttributes();
        writerAtt.endpointAtt.durabilityKind = att.qos.durability.kind == DurabilityQosPolicyKind.VOLATILE_DURABILITY_QOS ? DurabilityKind.VOLATILE : DurabilityKind.TRANSIENT_LOCAL;
        writerAtt.endpointAtt.endpointKind = EndpointKind.WRITER;
        writerAtt.endpointAtt.multicastLocatorList = att.multicastLocatorList;
        writerAtt.endpointAtt.reliabilityKind = att.qos.reliability.kind == ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS ? ReliabilityKind.RELIABLE : ReliabilityKind.BEST_EFFORT;
        writerAtt.endpointAtt.topicKind = att.topic.topicKind;
        writerAtt.endpointAtt.unicastLocatorList = att.unicastLocatorList;
        
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
        
        return publisher;
    }
    
    public Subscriber createSubscriber(SubscriberAttributes att, SubscriberListener listener) {
        
        logger.info("Creating Subscriber in Topic: " + att.topic.topicName);
        
        TopicDataType type = getRegisteredType(att.topic.topicDataTypeName);
        
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
        
        Subscriber subscriber = new Subscriber(this, type, att, listener);
        subscriber.setRTPSParticipant(this.m_rtpsParticipant);
        
        ReaderAttributes ratt = new ReaderAttributes();
        ratt.endpointAtt.durabilityKind = att.qos.durability.kind == DurabilityQosPolicyKind.VOLATILE_DURABILITY_QOS ? DurabilityKind.VOLATILE : DurabilityKind.TRANSIENT_LOCAL;
        ratt.endpointAtt.endpointKind = EndpointKind.READER;
        ratt.endpointAtt.multicastLocatorList = att.multicastLocatorList;
        ratt.endpointAtt.reliabilityKind = att.qos.reliability.kind == ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS ? ReliabilityKind.RELIABLE : ReliabilityKind.BEST_EFFORT;
        ratt.endpointAtt.topicKind = att.topic.topicKind;
        ratt.endpointAtt.unicastLocatorList = att.unicastLocatorList;
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
        
        return subscriber;
    }
    
    public boolean removePublisher(Publisher pub) {
        for (int i=0; i < this.m_publishers.size(); ++i) {
            Publisher it = this.m_publishers.get(i);
            if (it.getGuid().equals(pub.getGuid())) {
                it.destroy();
                this.m_publishers.remove(it);
                return true;
            }
        }
        return true;
    }
    
    public boolean removeSubscriber(Subscriber sub) {
        // TODO Auto-generated method stub
        return true;
    }
    
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
    
    public TopicDataType getRegisteredType(String typeName) {
        
        for (TopicDataType type : this.m_types) {
            if (type.getName().equals(typeName)) {
                return type;
            }
        }
        
        return null;
    }
    
    public GUID getGuid() {
         return this.m_rtpsParticipant.getGUID();
    }
    
    public ParticipantAttributes getAttributes() {
        return this.m_att;
        
    }
    
    public boolean newRemoteEndpointDiscovered(GUID paricipantGuid, short userId, EndpointKind kind) {
        return false;
        
    }

    public RTPSParticipantListener getListener() {
        return this.m_rtpsListener;
    }

    public void setRTPSParticipant(RTPSParticipant part) {
        this.m_rtpsParticipant = part;
    }

    

   

    
}
