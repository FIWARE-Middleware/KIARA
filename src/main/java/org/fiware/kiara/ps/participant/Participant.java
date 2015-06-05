package org.fiware.kiara.ps.participant;

import java.util.ArrayList;
import java.util.List;

import org.fiware.kiara.ps.attributes.ParticipantAttributes;
import org.fiware.kiara.ps.attributes.PublisherAttributes;
import org.fiware.kiara.ps.publisher.Publisher;
import org.fiware.kiara.ps.publisher.PublisherListener;
import org.fiware.kiara.ps.rtps.common.EndpointKind;
import org.fiware.kiara.ps.rtps.common.TopicKind;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.subscriber.Subscriber;
import org.fiware.kiara.ps.topic.TopicDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Participant {
    
    private static final Logger logger = LoggerFactory.getLogger(Participant.class);
    
    private ParticipantAttributes m_att;
    
    private RTPSParticipant m_rtpsParticipant;
    
    private ParticipantListener m_listener;
    
    private List<Publisher> m_publishers;
    
    private List<Subscriber> m_subscribers;
    
    private List<TopicDataType> m_types;
    
    private Participant(ParticipantAttributes participantAttributes, ParticipantListener listener) {
        this.m_att = participantAttributes;
        this.m_rtpsParticipant = null;
        this.m_listener = listener;
        
        //rtpsPartListener TODO Finish this
        
        this.m_publishers = new ArrayList<Publisher>();
        this.m_subscribers = new ArrayList<Subscriber>();
        this.m_types = new ArrayList<TopicDataType>();
    }
    
    private void deleteParticipant() {
        this.m_publishers.clear();
        this.m_subscribers.clear();
        
        // TODO Remove participant -> Domain::removeRTPSParticipant(this);
    }
    
    public Publisher createPublisher(PublisherAttributes att, PublisherListener listener) {
        TopicDataType type = getRegisteredType(att.topic.topicDataType);
        
        if (type == null) {
            logger.error("Type : " + att.topic.topicDataType + " Not Registered");
            return null;
        }
        
        if (att.topic.topicKind == TopicKind.WITH_KEY && !type.isKeyDefined) {
            logger.error("Keyed Topic needs getKey function");
            return null;
        }
        
        if (this.m_att.rtps.builtinAtt.useStaticEDP) {
            if (att.getUserDefinedId() <= 0) {
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
        
        //Publisher publisher = new Publisher();
        
        return null;
    }
    
    private TopicDataType getRegisteredType(String typeName) {
        
        for (TopicDataType type : this.m_types) {
            if (type.getName().equals(typeName)) {
                return type;
            }
        }
        
        return null;
        
    }
    
    public GUID getGuid() {
        return null;
        
    }
    
    public ParticipantAttributes getAttributes() {
        return this.m_att;
        
    }
    
    public boolean newRemoteEndpointDiscovered(GUID paricipantGuid, short userId, EndpointKind kind) {
        return false;
        
    }

}
