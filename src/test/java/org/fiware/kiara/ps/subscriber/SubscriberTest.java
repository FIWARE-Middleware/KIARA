package org.fiware.kiara.ps.subscriber;

import org.fiware.kiara.ps.Domain;
import org.fiware.kiara.ps.attributes.ParticipantAttributes;
import org.fiware.kiara.ps.attributes.SubscriberAttributes;
import org.fiware.kiara.ps.common.SubListener;
import org.fiware.kiara.ps.participant.Participant;
import org.fiware.kiara.ps.qos.policies.HistoryQosPolicyKind;
import org.fiware.kiara.ps.qos.policies.ReliabilityQosPolicyKind;
import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.common.TopicKind;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;
import org.fiware.kiara.ps.types.HelloWorld;
import org.fiware.kiara.ps.types.HelloWorldType;

public class SubscriberTest {
    
    public static void main (String[] args) {
        
        HelloWorldType type = new HelloWorldType();
        HelloWorld hw = type.createData();
        
        hw.setInnerLongAtt(10);
        hw.setInnerStringAtt("Hello World");
        
        // Create participant
        ParticipantAttributes pParam = new ParticipantAttributes();
        pParam.rtps.builtinAtt.useSimplePDP = false;
        pParam.rtps.builtinAtt.useWriterLP = false;
        pParam.rtps.defaultSendPort = 10043;
        pParam.rtps.builtinAtt.useSimpleEDP = true;
        pParam.rtps.builtinAtt.simpleEDP.usePulicationReaderAndSubscriptionWriter = true;
        pParam.rtps.builtinAtt.simpleEDP.usePulicationWriterAndSubscriptionReader = true;
        pParam.rtps.builtinAtt.domainID = 80;
        pParam.rtps.builtinAtt.leaseDuration = new Timestamp().timeInfinite();
        pParam.rtps.sendSocketBufferSize = 8712;
        pParam.rtps.listenSocketBufferSize = 17424;
        pParam.rtps.setName("ParticipantSub");
        
        Participant participant = Domain.createParticipant(pParam, null);
        if (participant == null) {
            System.out.println("Error when creating participant");
            return;
        }
        
        // Type registration
        Domain.registerType(participant, type);
        
        SubscriberAttributes satt = new SubscriberAttributes();
        satt.topic.topicKind = TopicKind.NO_KEY;
        satt.topic.topicDataTypeName = "HelloWorld";
        satt.topic.topicName = "HelloWorldTopic";
        satt.topic.historyQos.kind = HistoryQosPolicyKind.KEEP_LAST_HISTORY_QOS;
        satt.topic.historyQos.depth = 30;
        satt.topic.resourceLimitQos.maxSamples = 50;
        satt.topic.resourceLimitQos.allocatedSamples = 20;
        satt.qos.reliability.kind = ReliabilityQosPolicyKind.BEST_EFFORT_RELIABILITY_QOS;
        //satt.
        
        Subscriber subscriber = Domain.createSubscriber(participant, satt, new SubListener());
        
        if (subscriber == null) {
            System.out.println("Error creating subscriber");
            return;
        }
        
        
        
    }

}
