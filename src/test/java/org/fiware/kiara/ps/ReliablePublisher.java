package org.fiware.kiara.ps;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.fiware.kiara.Kiara;
import org.fiware.kiara.ps.Domain;
import org.fiware.kiara.ps.attributes.ParticipantAttributes;
import org.fiware.kiara.ps.attributes.PublisherAttributes;
import org.fiware.kiara.ps.common.PubListener;
import org.fiware.kiara.ps.participant.Participant;
import org.fiware.kiara.ps.qos.policies.HistoryQosPolicyKind;
import org.fiware.kiara.ps.qos.policies.LivelinessQosPolicyKind;
import org.fiware.kiara.ps.qos.policies.ReliabilityQosPolicyKind;
import org.fiware.kiara.ps.rtps.common.TopicKind;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;
import org.fiware.kiara.ps.rtps.utils.IPFinder;
import org.fiware.kiara.ps.topic.KeyedType;
import org.fiware.kiara.ps.types.HelloWorld;
import org.fiware.kiara.ps.types.HelloWorldType;
import org.fiware.kiara.serialization.impl.Serializable;

public class ReliablePublisher {

    private static final HelloWorldType hwtype = new HelloWorldType();

    public static void main (String [] args) {
        
        HelloWorld hw = hwtype.createData();
        
        hw.setInnerLongAtt(10);
        hw.setInnerStringAtt("Hello World");

        ParticipantAttributes pAtt = new ParticipantAttributes();
        //pAtt.rtps.defaultSendPort = 11511;
        pAtt.rtps.useIPv4ToSend = true;
        pAtt.rtps.builtinAtt.useSimplePDP = true;
        pAtt.rtps.builtinAtt.useWriterLP = true;
        pAtt.rtps.builtinAtt.useSimpleEDP = true;
        pAtt.rtps.builtinAtt.useStaticEDP = true;
        //pAtt.rtps.builtinAtt.

        final String ipAddr = IPFinder.getFirstIPv4Address().getHostAddress();
        System.out.println("IP Addr " + ipAddr);
        final String edpXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<staticdiscovery>"
                + "    <participant>"
                + "        <name>participant1</name>"
                + "        <reader>"
                + "            <userId>1</userId>"
                + "            <topic name=\"HelloWorldTopic\" dataType=\"HelloWorld\" kind=\"WITH_KEY\"></topic>"
                + "            <expectsInlineQos>false</expectsInlineQos>"
                //+ "                 <topicKind>WITH_KEY</topicKind>"
                + "            <reliabilityQos>RELIABLE_RELIABILITY_QOS</reliabilityQos>"
                + "        </reader>"
                + "    </participant>"
                + "</staticdiscovery>";

        pAtt.rtps.builtinAtt.setStaticEndpointXML(edpXml);

        pAtt.rtps.setName("participant2");

        Participant participant = Domain.createParticipant(pAtt, null /*new PartListener()*/);

        System.out.println("Publisher participant SPDP MC Port: " + participant.getSPDPMulticastPort());
        System.out.println("Publisher participant SPDP UC Port: " + participant.getSPDPUnicastPort());
        System.out.println("Publisher participant User MC Port: " + participant.getUserMulticastPort());
        System.out.println("Publisher participant User UC Port: " + participant.getUserUnicastPort());

        assertNotNull("Error when creating participant", participant);

        boolean registered = Domain.registerType(participant, hwtype);
        assertTrue("Error registering type", registered);

        // Create publisher
        PublisherAttributes pubAtt = new PublisherAttributes();
        pubAtt.setUserDefinedID((short) 1);
        //pubAtt.topic.topicKind = TopicKind.NO_KEY;
        pubAtt.topic.topicKind = TopicKind.WITH_KEY;
        pubAtt.topic.topicDataTypeName = "HelloWorld";
        pubAtt.topic.topicName = "HelloWorldTopic";
        pubAtt.topic.historyQos.kind = HistoryQosPolicyKind.KEEP_LAST_HISTORY_QOS;
        pubAtt.topic.historyQos.depth = 5;
        pubAtt.topic.resourceLimitQos.maxSamples = 50;
        pubAtt.topic.resourceLimitQos.maxSamplesPerInstance = 5;
        pubAtt.topic.resourceLimitQos.allocatedSamples = 20;
        pubAtt.times.heartBeatPeriod = new Timestamp(2, 200 * 1000 * 1000);
        pubAtt.qos.reliability.kind = ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS;
        pubAtt.qos.liveliness.kind = LivelinessQosPolicyKind.AUTOMATIC_LIVELINESS_QOS;
        //pubAtt.qos.liveliness.kind = LivelinessQosPolicyKind.MANUAL_BY_PARTICIPANT_LIVELINESS_QOS;
        pubAtt.qos.liveliness.announcementPeriod = new Timestamp(5, 0);
        //pubAtt.qos.liveliness.leaseDuration = new Timestamp(20, 1);
        //pubAtt.qos.liveliness.announcementPeriod = new Timestamp(5, 0);


        org.fiware.kiara.ps.publisher.Publisher<HelloWorld> publisher = null;
        publisher = Domain.createPublisher(participant, pubAtt, new PubListener());

        if (publisher == null) {
            Domain.removeParticipant(participant);
        }

        assertNotNull("Error creating publisher", publisher);
        
        try {
            Thread.sleep(3000);
            for (int i=0; i < 7; ++i) {
                System.out.println("Send message " + i);
                hw.setInnerStringAtt("Hello World - " + "0");
                publisher.write(hw);
                Thread.sleep(15000);
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //publisher.write(hw);




        //Domain.removeParticipant(participant);

        try {
            Thread.sleep(300000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("Publisher finished");

    }

}
