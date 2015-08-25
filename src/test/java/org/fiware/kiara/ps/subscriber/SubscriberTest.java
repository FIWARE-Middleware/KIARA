package org.fiware.kiara.ps.subscriber;

import org.fiware.kiara.Kiara;
import org.fiware.kiara.ps.Domain;
import org.fiware.kiara.ps.attributes.ParticipantAttributes;
import org.fiware.kiara.ps.attributes.SubscriberAttributes;
import org.fiware.kiara.ps.common.PartListener;
import org.fiware.kiara.ps.common.SubListener;
import org.fiware.kiara.ps.participant.Participant;
import org.fiware.kiara.ps.qos.policies.HistoryQosPolicyKind;
import org.fiware.kiara.ps.qos.policies.ReliabilityQosPolicyKind;
import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.common.TopicKind;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;
import org.fiware.kiara.ps.types.HelloWorld;
import org.fiware.kiara.ps.types.HelloWorldType;
import org.fiware.kiara.ps.types.TopicHelloWorldType;

public class SubscriberTest {
    
    static {
        System.setProperty("java.util.logging.config.file", "logging.properties");
    }
    
    public static void main (String[] args) {
        
        //newTypesTest();
        discoveryTest();
    }
    
    /*public static void singleParticipantTest() {
        HelloWorldType type = new HelloWorldType();
        HelloWorld hw = type.createData();
        
        hw.setInnerLongAtt(10);
        hw.setInnerStringAtt("Hello World");
        
        // Create participant
        ParticipantAttributes pParam = new ParticipantAttributes();
        pParam.rtps.builtinAtt.useSimplePDP = true;
        pParam.rtps.builtinAtt.useWriterLP = false;
        pParam.rtps.builtinAtt.useSimpleEDP = true;
        pParam.rtps.builtinAtt.useStaticEDP = true;
        pParam.rtps.builtinAtt.setStaticEndpointXMLFilename("READER_ENDPOINTS.xml");
        
        pParam.rtps.setName("participant1");
        
        Participant participant = Domain.createParticipant(pParam, new PartListener());
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
        
        satt.setUserDefinedID((short) 1);
        Subscriber subscriber = Domain.createSubscriber(participant, satt, new SubListener());
        if (subscriber == null) {
            System.out.println("Error creating subscriber");
            return;
        }
        System.out.println("Subscriber created");
        
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        System.out.println("");
        
    }
    
    public static void multipleParticipantTest() {
        HelloWorldType type = new HelloWorldType();
        HelloWorld hw = type.createData();
        
        hw.setInnerLongAtt(10);
        hw.setInnerStringAtt("Hello World");
        
        // Create participant
        ParticipantAttributes pParam = new ParticipantAttributes();
        pParam.rtps.builtinAtt.useSimplePDP = true;
        pParam.rtps.builtinAtt.useWriterLP = false;
        pParam.rtps.builtinAtt.useSimpleEDP = true;
        pParam.rtps.builtinAtt.useStaticEDP = true;
        pParam.rtps.builtinAtt.setStaticEndpointXMLFilename("READER_ENDPOINTS.xml");
        
        pParam.rtps.setName("participant1");
        
        Participant participant = Domain.createParticipant(pParam, null);
        if (participant == null) {
            System.out.println("Error when creating participant");
            return;
        }
        
        pParam.rtps.setName("participant2");
        
        Participant participant2 = Domain.createParticipant(pParam, null);
        if (participant2 == null) {
            System.out.println("Error when creating participant2");
            return;
        }
        
        // Type registration
        Domain.registerType(participant, type);
        Domain.registerType(participant2, type);
        
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
        
        satt.setUserDefinedID((short) 1);
        Subscriber subscriber = Domain.createSubscriber(participant, satt, new SubListener());
        if (subscriber == null) {
            System.out.println("Error creating subscriber");
            return;
        }
        System.out.println("Subscriber created");
        
        satt.setUserDefinedID((short) 2);
        Subscriber subscriber2 = Domain.createSubscriber(participant2, satt, new SubListener());
        if (subscriber2 == null) {
            System.out.println("Error creating subscriber");
            return;
        }
        System.out.println("Subscriber2 created");
        
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        Domain.removeParticipant(participant);
        Domain.removeParticipant(participant2);
        
        Kiara.shutdown();
    }

    public static void singleSubscriberRemovalTest() {
        HelloWorldType type = new HelloWorldType();
        HelloWorld hw = type.createData();
        
        hw.setInnerLongAtt(10);
        hw.setInnerStringAtt("Hello World");
        
        // Create participant
        ParticipantAttributes pParam = new ParticipantAttributes();
        pParam.rtps.builtinAtt.useSimplePDP = true;
        pParam.rtps.builtinAtt.useWriterLP = false;
        pParam.rtps.builtinAtt.useSimpleEDP = true;
        pParam.rtps.builtinAtt.useStaticEDP = true;
        pParam.rtps.builtinAtt.setStaticEndpointXMLFilename("READER_ENDPOINTS.xml");
        
        pParam.rtps.setName("participant1");
        
        Participant participant = Domain.createParticipant(pParam, new PartListener());
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
        
        satt.setUserDefinedID((short) 1);
        Subscriber subscriber = Domain.createSubscriber(participant, satt, new SubListener());
        if (subscriber == null) {
            System.out.println("Error creating subscriber");
            return;
        }
        System.out.println("Subscriber created");
        
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        Domain.removeSubscriber(subscriber);
        
    }
    
    public static void singleSubscriberAndParticipantRemovalTest() {
        HelloWorldType type = new HelloWorldType();
        HelloWorld hw = type.createData();
        
        hw.setInnerLongAtt(10);
        hw.setInnerStringAtt("Hello World");
        
        // Create participant
        ParticipantAttributes pParam = new ParticipantAttributes();
        pParam.rtps.builtinAtt.useSimplePDP = true;
        pParam.rtps.builtinAtt.useWriterLP = false;
        pParam.rtps.builtinAtt.useSimpleEDP = true;
        pParam.rtps.builtinAtt.useStaticEDP = true;
        pParam.rtps.builtinAtt.setStaticEndpointXMLFilename("READER_ENDPOINTS.xml");
        
        pParam.rtps.setName("participant1");
        
        Participant participant = Domain.createParticipant(pParam, new PartListener());
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
        
        satt.setUserDefinedID((short) 1);
        Subscriber subscriber = Domain.createSubscriber(participant, satt, new SubListener());
        if (subscriber == null) {
            System.out.println("Error creating subscriber");
            return;
        }
        System.out.println("Subscriber created");
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        Domain.removeSubscriber(subscriber);
        
        Domain.removeParticipant(participant);
        
        Kiara.shutdown();
        
        System.out.println("Finished Subscriber");
        
    }
    
    public static void multipleSubscriberAndParticipantRemovalTest() {
        
        HelloWorldType type = new HelloWorldType();
        HelloWorld hw = type.createData();
        
        hw.setInnerLongAtt(10);
        hw.setInnerStringAtt("Hello World");
        
        // Create participant
        ParticipantAttributes pParam = new ParticipantAttributes();
        pParam.rtps.builtinAtt.useSimplePDP = true;
        pParam.rtps.builtinAtt.useWriterLP = false;
        pParam.rtps.builtinAtt.useSimpleEDP = true;
        pParam.rtps.builtinAtt.useStaticEDP = true;
        pParam.rtps.builtinAtt.setStaticEndpointXMLFilename("READER_ENDPOINTS.xml");
        
        pParam.rtps.setName("participant1");
        
        Participant participant = Domain.createParticipant(pParam, new PartListener());
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
        
        satt.setUserDefinedID((short) 1);
        Subscriber subscriber = Domain.createSubscriber(participant, satt, new SubListener());
        if (subscriber == null) {
            System.out.println("Error creating subscriber");
            Kiara.shutdown();
            return;
        }
        System.out.println("Subscriber created");
        
        satt.setUserDefinedID((short) 2);
        Subscriber subscriber2 = Domain.createSubscriber(participant, satt, new SubListener());
        if (subscriber2 == null) {
            System.out.println("Error creating subscriber2");
            Kiara.shutdown();
            return;
        }
        System.out.println("Subscriber2 created");
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        Domain.removeSubscriber(subscriber);
        
        Domain.removeSubscriber(subscriber2);
        
        Domain.removeParticipant(participant);
        
        Kiara.shutdown();
        
        System.out.println("Finished Subscribers");
        
    }
    
    public static void subscriptionTest() {
        
        HelloWorldType type = new HelloWorldType();
        HelloWorld hw = type.createData();
        
        hw.setInnerLongAtt(10);
        hw.setInnerStringAtt("Hello World");
        
        // Create participant
        ParticipantAttributes pParam = new ParticipantAttributes();
        pParam.rtps.builtinAtt.useSimplePDP = true;
        pParam.rtps.builtinAtt.useWriterLP = false;
        pParam.rtps.builtinAtt.useSimpleEDP = true;
        pParam.rtps.builtinAtt.useStaticEDP = true;
        pParam.rtps.builtinAtt.setStaticEndpointXMLFilename("LOCAL_READER_ENDPOINTS.xml");
        
        pParam.rtps.setName("participant1");
        
        Participant participant = Domain.createParticipant(pParam, null );
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
        
        satt.setUserDefinedID((short) 1);
        Subscriber subscriber = Domain.createSubscriber(participant, satt, new SubListener());
        if (subscriber == null) {
            System.out.println("Error creating subscriber");
            return;
        }
        
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        //Domain.removeParticipant(participant);
        
        //Kiara.shutdown();
        
        System.out.println("Subscriber finished");
        
    }
    */
    
    public static void newTypesTest() {
        
        TopicHelloWorldType type = new TopicHelloWorldType();
        HelloWorld hw = type.createData();
        
        hw.setInnerLongAtt(10);
        hw.setInnerStringAtt("Hello World");
        
        // Create participant
        ParticipantAttributes pParam = new ParticipantAttributes();
        pParam.rtps.builtinAtt.useSimplePDP = true;
        pParam.rtps.builtinAtt.useWriterLP = false;
        pParam.rtps.builtinAtt.useSimpleEDP = true;
        pParam.rtps.builtinAtt.useStaticEDP = true;
        pParam.rtps.builtinAtt.setStaticEndpointXMLFilename("LOCAL_READER_ENDPOINTS.xml");
        
        pParam.rtps.setName("participant1");
        
        Participant participant = Domain.createParticipant(pParam, null );
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
        
        satt.setUserDefinedID((short) 1);
        Subscriber<HelloWorld> subscriber = Domain.createSubscriber(participant, satt, new SubListener());
        if (subscriber == null) {
            System.out.println("Error creating subscriber");
            return;
        }
        
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        Domain.removeParticipant(participant);
        
        Kiara.shutdown();
        
        System.out.println("Subscriber finished");
        
    }
    
    public static void discoveryTest() {
        
        TopicHelloWorldType type = new TopicHelloWorldType();
        HelloWorld hw = type.createData();
        
        hw.setInnerLongAtt(10);
        hw.setInnerStringAtt("Hello World");
        
        // Create participant
        ParticipantAttributes pParam = new ParticipantAttributes();
        pParam.rtps.builtinAtt.useSimplePDP = true;
        pParam.rtps.builtinAtt.useWriterLP = false;
        pParam.rtps.builtinAtt.useSimpleEDP = true;
        pParam.rtps.builtinAtt.useStaticEDP = true;
        pParam.rtps.builtinAtt.setStaticEndpointXMLFilename("LOCAL_READER_ENDPOINTS.xml");
        
        pParam.rtps.setName("participant1");
        
        Participant participant = Domain.createParticipant(pParam, null );
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
        
        satt.setUserDefinedID((short) 1);
        Subscriber<HelloWorld> subscriber = Domain.createSubscriber(participant, satt, new SubListener());
        if (subscriber == null) {
            System.out.println("Error creating subscriber");
            return;
        }
        
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        Domain.removeParticipant(participant);
        
        Kiara.shutdown();
        
        System.out.println("Subscriber finished");
        
    }
    
}
