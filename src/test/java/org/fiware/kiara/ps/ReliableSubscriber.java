package org.fiware.kiara.ps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.fiware.kiara.Kiara;
import org.fiware.kiara.ps.Domain;
import org.fiware.kiara.ps.attributes.ParticipantAttributes;
import org.fiware.kiara.ps.attributes.SubscriberAttributes;
import org.fiware.kiara.ps.participant.Participant;
import org.fiware.kiara.ps.qos.policies.HistoryQosPolicyKind;
import org.fiware.kiara.ps.qos.policies.LivelinessQosPolicyKind;
import org.fiware.kiara.ps.qos.policies.ReliabilityQosPolicyKind;
import org.fiware.kiara.ps.rtps.common.MatchingInfo;
import org.fiware.kiara.ps.rtps.common.MatchingStatus;
import org.fiware.kiara.ps.rtps.common.TopicKind;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;
import org.fiware.kiara.ps.rtps.utils.IPFinder;
import org.fiware.kiara.ps.subscriber.SubscriberListener;
import org.fiware.kiara.ps.types.HelloWorld;
import org.fiware.kiara.ps.types.HelloWorldType;

public class ReliableSubscriber {
    
    private static final HelloWorldType hwtype = new HelloWorldType();
    
    public static void main (String [] args) {
        
        //hwtype.setGetKeyDefined(true);
        HelloWorld hw = hwtype.createData();

        hw.setInnerLongAtt(10);
        hw.setInnerStringAtt("Hello World");

        // Create participant
        ParticipantAttributes pParam = new ParticipantAttributes();
        pParam.rtps.builtinAtt.useSimplePDP = true;
        pParam.rtps.builtinAtt.useWriterLP = true;
        pParam.rtps.builtinAtt.useSimpleEDP = true;
        pParam.rtps.builtinAtt.useStaticEDP = true;

        final String ipAddr = IPFinder.getFirstIPv4Address().getHostAddress();
        System.out.println("IP Addr " + ipAddr);

        final String edpXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<staticdiscovery>"
                + "    <participant>"
                + "        <name>participant2</name>"
                + "        <writer>"
                + "            <userId>1</userId>"
                + "            <topicName>HelloWorldTopic</topicName>"
                + "            <topicDataType>HelloWorld</topicDataType>"
                + "            <topicKind>WITH_KEY</topicKind>"
                + "            <reliabilityQos>RELIABLE_RELIABILITY_QOS</reliabilityQos>"
                //+ "            <livelinessQos kind=\"AUTOMATIC_LIVELINESS_QOS\" leaseDuration_ms=\"20000\"></livelinessQos>"
                + "        </writer>"
                + "     </participant>"
                + "    </staticdiscovery>";

        pParam.rtps.builtinAtt.setStaticEndpointXML(edpXml);

        pParam.rtps.setName("participant1");

        Participant participant = Domain.createParticipant(pParam, null /*new PartListener()*/);
        if (participant == null) {
            System.out.println("Error when creating participant");
            return;
        }

        System.out.println("Subscriber participant SPDP MC Port: " + participant.getSPDPMulticastPort());
        System.out.println("Subscriber participant SPDP UC Port: " + participant.getSPDPUnicastPort());
        System.out.println("Subscriber participant User MC Port: " + participant.getUserMulticastPort());
        System.out.println("Subscriber participant User UC Port: " + participant.getUserUnicastPort());

        // Type registration
        Domain.registerType(participant, hwtype);

        /*
        SubscriberAttributes satt = new SubscriberAttributes();
        satt.topic.topicKind = TopicKind.WITH_KEY;
        satt.topic.topicDataTypeName = "HelloWorld";
        satt.topic.topicName = "HelloWorldTopic";
        satt.topic.historyQos.kind = HistoryQosPolicyKind.KEEP_LAST_HISTORY_QOS;
        satt.topic.historyQos.depth = 10;
        satt.topic.resourceLimitQos.maxSamples = 50;
        satt.topic.resourceLimitQos.maxSamplesPerInstance = 10;
        satt.topic.resourceLimitQos.allocatedSamples = 20;
        satt.qos.reliability.kind = ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS;
        satt.qos.liveliness.kind = LivelinessQosPolicyKind.AUTOMATIC_LIVELINESS_QOS;
        satt.qos.liveliness.announcementPeriod = new Timestamp(5, 0);
        satt.qos.liveliness.leaseDuration = new Timestamp(10, 0);

        satt.setUserDefinedID((short) 1);

        org.fiware.kiara.ps.subscriber.Subscriber<HelloWorld> subscriber = Domain.createSubscriber(participant, satt, new SubscriberListener() {

            private int n_matched;

            @Override
            public void onNewDataMessage(org.fiware.kiara.ps.subscriber.Subscriber<?> sub) {
                System.out.println("Message received");
                HelloWorld type = (HelloWorld) sub.takeNextData(null);
                while (type != null) {
                    System.out.println(type.getInnerStringAtt());
                    type = (HelloWorld) sub.takeNextData(null);
                    System.out.println("workDoneSignal sent");
                }
            }

            @Override
            public void onSubscriptionMatched(org.fiware.kiara.ps.subscriber.Subscriber<?> sub, MatchingInfo info) {
                if (info.status == MatchingStatus.MATCHED_MATHING) {
                    n_matched++;
                    System.out.println("Publisher Matched. Total : " + this.n_matched);
                    //workInitSignal.countDown();
                } else {
                    n_matched--;
                    System.out.println("Publisher Unmatched. Total : " + this.n_matched);
                }
            }

        });
        
        if (subscriber == null) {
            System.out.println("Error creating subscriber");
        }
        */
        
        try {
            Thread.sleep(500000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Domain.removeParticipant(participant);
        
        Kiara.shutdown();
        
        System.out.println("ReliableSubscriber finished");

        
        
    }

}
