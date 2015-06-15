package org.fiware.kiara.ps;

import java.util.ArrayList;
import java.util.List;

import org.fiware.kiara.ps.attributes.ParticipantAttributes;
import org.fiware.kiara.ps.attributes.PublisherAttributes;
import org.fiware.kiara.ps.attributes.SubscriberAttributes;
import org.fiware.kiara.ps.participant.Participant;
import org.fiware.kiara.ps.participant.ParticipantListener;
import org.fiware.kiara.ps.publisher.Publisher;
import org.fiware.kiara.ps.publisher.PublisherListener;
import org.fiware.kiara.ps.rtps.RTPSDomain;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.subscriber.Subscriber;
import org.fiware.kiara.ps.subscriber.SubscriberListener;
import org.fiware.kiara.ps.topic.TopicDataTypeOld;
import org.fiware.kiara.ps.topic.TopicDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Domain {
    
    private static List<Participant> m_participants = new ArrayList<Participant>();
    
    private static final Logger logger = LoggerFactory.getLogger(Domain.class);
    
    public static void stopAll() {
        while (m_participants.size() > 0) {
            Domain.removeParticipant(m_participants.get(0));
        }
    }
    
    public static boolean removeParticipant(Participant part) {
        if (part != null) {
            for (Participant current : m_participants) {
                if (current.getGuid().equals(part.getGuid())) {
                    // Found
                    m_participants.remove(current);
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean removePublisher(Publisher pub) {
        if (pub != null) {
            for (Participant it : m_participants) {
                if (it.getGuid().getGUIDPrefix().equals(pub.getGuid().getGUIDPrefix())) {
                    // Found
                    return it.removePublisher(pub);
                }
            }
        }
        return false;
    }
    
    public static boolean removeSubscriber(Subscriber sub) {
        if (sub != null) {
            for (Participant it : m_participants) {
                if (it.getGuid().getGUIDPrefix().equals(sub.getGuid().getGUIDPrefix())) {
                    // Found
                    return it.removeSubscriber(sub);
                }
            }
        }
        return false;
    }
    
    public static Participant createParticipant(ParticipantAttributes att, ParticipantListener listener) {
        Participant pubSubParticipant = new Participant(att, listener);
        
        RTPSParticipant part = RTPSDomain.createParticipant(att.rtps, pubSubParticipant.getListener());
        
        if (part == null) {
            logger.error("Problem creating RTPSParticipant");
            return null;
        }
        
        pubSubParticipant.setRTPSParticipant(part);
        m_participants.add(pubSubParticipant);
        return pubSubParticipant;
    }
    
    public static Publisher createPublisher(Participant part, PublisherAttributes att, PublisherListener listener) {
        for (Participant it : m_participants) {
            if (it.getGuid().equals(part.getGuid())) {
                return part.createPublisher(att, listener);
            }
        }
        return null;
    }
    
    public static Subscriber createSubscriber(Participant part, SubscriberAttributes att, SubscriberListener listener) {
        for (Participant it : m_participants) {
            if (it.getGuid().equals(part.getGuid())) {
                return part.createSubscriber(att, listener);
            }
        }
        return null;
    }
    
    public static boolean registerType(Participant part, TopicDataType type) {
        for (Participant it : m_participants) {
            if (it.getGuid().equals(part.getGuid())) {
                return part.registerType(type);
            }
        }
        return false;
    }
    
    public static boolean registerTypeAlt(Participant part, TopicDataType type) {
        for (Participant it : m_participants) {
            if (it.getGuid().equals(part.getGuid())) {
                return part.registerType(type);
            }
        }
        return false;
    }

}
