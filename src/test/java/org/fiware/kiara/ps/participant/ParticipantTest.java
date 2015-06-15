package org.fiware.kiara.ps.participant;

import org.fiware.kiara.ps.Domain;
import org.fiware.kiara.ps.attributes.ParticipantAttributes;
import org.fiware.kiara.ps.attributes.PublisherAttributes;
import org.fiware.kiara.ps.publisher.Publisher;
import org.fiware.kiara.ps.publisher.PublisherListener;
import org.fiware.kiara.ps.qos.policies.HistoryQosPolicyKind;
import org.fiware.kiara.ps.qos.policies.LivelinessQosPolicyKind;
import org.fiware.kiara.ps.qos.policies.ReliabilityQosPolicyKind;
import org.fiware.kiara.ps.rtps.common.MatchingInfo;
import org.fiware.kiara.ps.rtps.common.MatchingStatus;
import org.fiware.kiara.ps.rtps.common.TopicKind;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;

public class ParticipantTest {
    
    public static void main(String[] args) {
        
        HelloWorldType type = new HelloWorldType();
        HelloWorld hw = type.createData();
        
        ParticipantAttributes pAtt = new ParticipantAttributes();
        pAtt.rtps.defaultSendPort = 11511;
        pAtt.rtps.useIPv4ToSend = true;
        pAtt.rtps.builtinAtt.useSimplePDP = true;
        pAtt.rtps.builtinAtt.useSimpleEDP = true;
        pAtt.rtps.builtinAtt.simpleEDP.usePulicationReaderAndSubscriptionWriter = true;
        pAtt.rtps.builtinAtt.simpleEDP.usePulicationWriterAndSubscriptionReader = true;
        pAtt.rtps.builtinAtt.domainID = 80;
        pAtt.rtps.builtinAtt.leaseDuration = new Timestamp().timeInfinite();
        pAtt.rtps.sendSocketBufferSize = 8712;
        pAtt.rtps.listenSocketBufferSize = 17424;
        pAtt.rtps.setName("Participant_pub");
        
        Participant participant = Domain.createParticipant(pAtt, null);
        
        if (participant == null) {
            System.out.println("Error when creating participant");
            return;
        }
        
        if (!Domain.registerType(participant, type)) {
            System.out.println("Error registering type");
            return;
        }
        
        // Create publisher
        PublisherAttributes pubAtt = new PublisherAttributes();
        pubAtt.topic.topicKind = TopicKind.NO_KEY;
        pubAtt.topic.topicDataType = "HelloWorld";
        pubAtt.topic.topicName = "HelloWorldTopic";
        pubAtt.topic.historyQos.kind = HistoryQosPolicyKind.KEEP_LAST_HISTORY_QOS;
        pubAtt.topic.historyQos.depth = 30;
        pubAtt.topic.resourceLimitQos.maxSamples = 50;
        pubAtt.topic.resourceLimitQos.allocatedSamples = 20;
        pubAtt.times.heartBeatPeriod = new Timestamp(2, 200*1000*1000);
        pubAtt.qos.reliability.kind = ReliabilityQosPolicyKind.BEST_EFFORT_RELIABILITY_QOS;
        pubAtt.qos.liveliness.kind = LivelinessQosPolicyKind.AUTOMATIC_LIVELINESS_QOS;
        pubAtt.qos.liveliness.leaseDuration = new Timestamp(5, 1);
        pubAtt.qos.liveliness.announcementPeriod = new Timestamp(5, 0);
        
        Publisher publisher = Domain.createPublisher(participant, pubAtt, new PubListener()); 
        
        if (publisher == null) {
            System.out.println("Error creating publisher");
            return;
        }
        
        
    }

}
