package org.fiware.kiara.ps.publisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.fiware.kiara.Kiara;
import org.fiware.kiara.ps.Domain;
import org.fiware.kiara.ps.attributes.ParticipantAttributes;
import org.fiware.kiara.ps.attributes.PublisherAttributes;
import org.fiware.kiara.ps.common.PartListener;
import org.fiware.kiara.ps.common.RTPSPartListener;
import org.fiware.kiara.ps.common.PubListener;
import org.fiware.kiara.ps.participant.Participant;
import org.fiware.kiara.ps.publisher.Publisher;
import org.fiware.kiara.ps.publisher.PublisherListener;
import org.fiware.kiara.ps.qos.policies.HistoryQosPolicyKind;
import org.fiware.kiara.ps.qos.policies.LivelinessQosPolicyKind;
import org.fiware.kiara.ps.qos.policies.ReliabilityQosPolicyKind;
import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.common.MatchingInfo;
import org.fiware.kiara.ps.rtps.common.MatchingStatus;
import org.fiware.kiara.ps.rtps.common.TopicKind;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;
import org.fiware.kiara.ps.types.HelloWorld;
import org.fiware.kiara.ps.types.HelloWorldType;

public class PublisherTest {
    
    static {
        System.setProperty("java.util.logging.config.file", "logging.properties");
    }
    
    public static void main(String[] args) {
        
        //publicationTest();
        //discoveryTest();
        shutdownPublisherTest();
        
    }
    
    public static void singleParticipantTest() {
        HelloWorldType type = new HelloWorldType();
        HelloWorld hw = type.createData();
        
        hw.setInnerLongAtt(10);
        hw.setInnerStringAtt("Hello World");
        
        ParticipantAttributes pAtt = new ParticipantAttributes();
        pAtt.rtps.defaultSendPort = 11511;
        pAtt.rtps.useIPv4ToSend = true;
        pAtt.rtps.builtinAtt.useSimplePDP = true;
        pAtt.rtps.builtinAtt.useWriterLP = false;
        pAtt.rtps.builtinAtt.useSimpleEDP = true;
        pAtt.rtps.builtinAtt.useStaticEDP = true;
        pAtt.rtps.builtinAtt.setStaticEndpointXMLFilename("LOCAL_WRITER_ENDPOINTS.xml");
        /*pAtt.rtps.builtinAtt.simpleEDP.usePulicationReaderAndSubscriptionWriter = true;
        pAtt.rtps.builtinAtt.simpleEDP.usePulicationWriterAndSubscriptionReader = true;
        pAtt.rtps.builtinAtt.domainID = 80;
        pAtt.rtps.builtinAtt.leaseDuration = new Timestamp().timeInfinite();
        pAtt.rtps.sendSocketBufferSize = 8712;
        pAtt.rtps.listenSocketBufferSize = 17424;
        pAtt.rtps.setName("ParticipantPub");*/
        
        pAtt.rtps.setName("participant1");
        
        Participant participant = Domain.createParticipant(pAtt, new PartListener());
        
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
        pubAtt.setUserDefinedID((short) 1);
        pubAtt.topic.topicKind = TopicKind.NO_KEY;
        pubAtt.topic.topicDataTypeName = "HelloWorld";
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
        
        Publisher<HelloWorld> publisher = Domain.createPublisher(participant, pubAtt, new PubListener()); 
        
        if (publisher == null) {
            System.out.println("Error creating publisher");
            Domain.removeParticipant(participant);
            return;
        }
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        Domain.removeParticipant(participant);
        
        System.out.println("Publisher finished");
    }
    
    public static void multipleParticipantTest() {
        HelloWorldType type = new HelloWorldType();
        HelloWorld hw = type.createData();
        
        hw.setInnerLongAtt(10);
        hw.setInnerStringAtt("Hello World");
        
        ParticipantAttributes pAtt = new ParticipantAttributes();
        pAtt.rtps.defaultSendPort = 11511;
        pAtt.rtps.useIPv4ToSend = true;
        pAtt.rtps.builtinAtt.useSimplePDP = true;
        pAtt.rtps.builtinAtt.useWriterLP = false;
        pAtt.rtps.builtinAtt.useSimpleEDP = true;
        pAtt.rtps.builtinAtt.useStaticEDP = true;
        pAtt.rtps.builtinAtt.setStaticEndpointXMLFilename("WRITER_ENDPOINTS.xml");
        
        pAtt.rtps.setName("participant1");
        Participant participant = Domain.createParticipant(pAtt, null /*new PartListener()*/);
        
        if (participant == null) {
            System.out.println("Error when creating participant");
            return;
        }
        
        if (!Domain.registerType(participant, type)) {
            System.out.println("Error registering type in participant1");
            Kiara.shutdown();
            return;
        }
        
        pAtt.rtps.setName("participant2");
        Participant participant2 = Domain.createParticipant(pAtt, null /*new PartListener()*/);
        
        if (participant2 == null) {
            System.out.println("Error when creating participant2");
            Kiara.shutdown();
            return;
        }
        
        if (!Domain.registerType(participant2, type)) {
            System.out.println("Error registering type in participant2");
            return;
        }
        
        // Create publisher
        PublisherAttributes pubAtt = new PublisherAttributes();
        pubAtt.setUserDefinedID((short) 1);
        pubAtt.topic.topicKind = TopicKind.NO_KEY;
        pubAtt.topic.topicDataTypeName = "HelloWorld";
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
            System.out.println("Error creating publisher1");
            Domain.removeParticipant(participant);
            Kiara.shutdown();
            return;
        }
        
        System.out.println("Publisher created");
        
        Publisher publisher2 = Domain.createPublisher(participant2, pubAtt, new PubListener()); 
        
        if (publisher2 == null) {
            System.out.println("Error creating publishe2r");
            Domain.removeParticipant(participant2);
            Kiara.shutdown();
            return;
        }
        
        System.out.println("Publisher created");
        
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        Domain.removeParticipant(participant);
        
        Domain.removeParticipant(participant2);
        
        System.out.println("Publisher finished");
        
        Kiara.shutdown();
    }
    
    public static void singlePublisherRemovalTest() {
        HelloWorldType type = new HelloWorldType();
        HelloWorld hw = type.createData();
        
        hw.setInnerLongAtt(10);
        hw.setInnerStringAtt("Hello World");
        
        ParticipantAttributes pAtt = new ParticipantAttributes();
        pAtt.rtps.defaultSendPort = 11511;
        pAtt.rtps.useIPv4ToSend = true;
        pAtt.rtps.builtinAtt.useSimplePDP = true;
        pAtt.rtps.builtinAtt.useWriterLP = false;
        pAtt.rtps.builtinAtt.useSimpleEDP = true;
        pAtt.rtps.builtinAtt.useStaticEDP = true;
        pAtt.rtps.builtinAtt.setStaticEndpointXMLFilename("WRITER_ENDPOINTS.xml");
        pAtt.rtps.setName("participant1");
        
        Participant participant = Domain.createParticipant(pAtt, new PartListener());
        
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
        pubAtt.setUserDefinedID((short) 1);
        pubAtt.topic.topicKind = TopicKind.NO_KEY;
        pubAtt.topic.topicDataTypeName = "HelloWorld";
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
            Domain.removeParticipant(participant);
            return;
        }
        
        try {
            Thread.sleep(9000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        Domain.removePublisher(publisher);
        
        //Domain.removeParticipant(participant);
        
        System.out.println("Publisher finished");
    }
    
    public static void singlePublisherAndParticipantRemovalTest() {
        HelloWorldType type = new HelloWorldType();
        HelloWorld hw = type.createData();
        
        hw.setInnerLongAtt(10);
        hw.setInnerStringAtt("Hello World");
        
        ParticipantAttributes pAtt = new ParticipantAttributes();
        pAtt.rtps.defaultSendPort = 11511;
        pAtt.rtps.useIPv4ToSend = true;
        pAtt.rtps.builtinAtt.useSimplePDP = true;
        pAtt.rtps.builtinAtt.useWriterLP = false;
        pAtt.rtps.builtinAtt.useSimpleEDP = true;
        pAtt.rtps.builtinAtt.useStaticEDP = true;
        pAtt.rtps.builtinAtt.setStaticEndpointXMLFilename("WRITER_ENDPOINTS.xml");
        pAtt.rtps.setName("participant1");
        
        Participant participant = Domain.createParticipant(pAtt, new PartListener());
        
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
        pubAtt.setUserDefinedID((short) 1);
        pubAtt.topic.topicKind = TopicKind.NO_KEY;
        pubAtt.topic.topicDataTypeName = "HelloWorld";
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
            Domain.removeParticipant(participant);
            return;
        }
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        Domain.removePublisher(publisher);
        
        Domain.removeParticipant(participant);
        
        Kiara.shutdown();
        
        System.out.println("Publisher finished");
        
    }
    
    public static void multiplePublisherAndParticipantRemovalTest() {
        HelloWorldType type = new HelloWorldType();
        HelloWorld hw = type.createData();
        
        hw.setInnerLongAtt(10);
        hw.setInnerStringAtt("Hello World");
        
        ParticipantAttributes pAtt = new ParticipantAttributes();
        pAtt.rtps.defaultSendPort = 11511;
        pAtt.rtps.useIPv4ToSend = true;
        pAtt.rtps.builtinAtt.useSimplePDP = true;
        pAtt.rtps.builtinAtt.useWriterLP = false;
        pAtt.rtps.builtinAtt.useSimpleEDP = true;
        pAtt.rtps.builtinAtt.useStaticEDP = true;
        pAtt.rtps.builtinAtt.setStaticEndpointXMLFilename("WRITER_ENDPOINTS.xml");
        pAtt.rtps.setName("participant1");
        
        Participant participant = Domain.createParticipant(pAtt, new PartListener());
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
        pubAtt.topic.topicDataTypeName = "HelloWorld";
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
        
        pubAtt.setUserDefinedID((short) 1);
        Publisher publisher = Domain.createPublisher(participant, pubAtt, new PubListener()); 
        
        if (publisher == null) {
            System.out.println("Error creating publisher");
            Domain.removeParticipant(participant);
            Kiara.shutdown();
            return;
        }
        
        pubAtt.setUserDefinedID((short) 2);
        Publisher publisher2 = Domain.createPublisher(participant, pubAtt, new PubListener()); 
        
        if (publisher2 == null) {
            System.out.println("Error creating publisher");
            Domain.removeParticipant(participant);
            Kiara.shutdown();
            return;
        }
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        Domain.removePublisher(publisher);
        
        Domain.removePublisher(publisher2);
        
        Domain.removeParticipant(participant);
        
        Kiara.shutdown();
        
        System.out.println("Publisher finished");
        
    }

    public static void publicationTest() {
        HelloWorldType type = new HelloWorldType();
        HelloWorld hw = type.createData();
        
        hw.setInnerLongAtt(10);
        hw.setInnerStringAtt("Hello World");
        
        ParticipantAttributes pAtt = new ParticipantAttributes();
        //pAtt.rtps.defaultSendPort = 11511;
        pAtt.rtps.useIPv4ToSend = true;
        pAtt.rtps.builtinAtt.useSimplePDP = true;
        pAtt.rtps.builtinAtt.useWriterLP = false;
        pAtt.rtps.builtinAtt.useSimpleEDP = true;
        pAtt.rtps.builtinAtt.useStaticEDP = true;
        pAtt.rtps.builtinAtt.setStaticEndpointXMLFilename("WRITER_ENDPOINTS.xml");
        
        pAtt.rtps.setName("participant1");
        
        Participant participant = Domain.createParticipant(pAtt, null /*new PartListener()*/);
        
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
        pubAtt.setUserDefinedID((short) 1);
        pubAtt.topic.topicKind = TopicKind.NO_KEY;
        pubAtt.topic.topicDataTypeName = "HelloWorld";
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
            Domain.removeParticipant(participant);
            return;
        }
        
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        publisher.write(hw);
        
        Domain.removeParticipant(participant);
        
        System.out.println("Publisher finished");
        
        Kiara.shutdown();
    }
    
    public static void discoveryTest() {
        HelloWorldType type = new HelloWorldType();
        HelloWorld hw = type.createData();
        
        hw.setInnerLongAtt(10);
        hw.setInnerStringAtt("Hello World");
        
        ParticipantAttributes pAtt = new ParticipantAttributes();
        //pAtt.rtps.defaultSendPort = 11511;
        pAtt.rtps.useIPv4ToSend = true;
        pAtt.rtps.builtinAtt.useSimplePDP = true;
        pAtt.rtps.builtinAtt.useWriterLP = false;
        pAtt.rtps.builtinAtt.useSimpleEDP = true;
        pAtt.rtps.builtinAtt.useStaticEDP = true;
        pAtt.rtps.builtinAtt.setStaticEndpointXMLFilename("WRITER_ENDPOINTS.xml");
        
        pAtt.rtps.setName("participant1");
        
        Participant participant = Domain.createParticipant(pAtt, null /*new PartListener()*/);
        
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
        pubAtt.setUserDefinedID((short) 1);
        pubAtt.topic.topicKind = TopicKind.NO_KEY;
        pubAtt.topic.topicDataTypeName = "HelloWorld";
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
        
        Publisher<HelloWorld> publisher = Domain.createPublisher(participant, pubAtt, new PubListener()); 
        
        if (publisher == null) {
            System.out.println("Error creating publisher");
            Domain.removeParticipant(participant);
            return;
        }
        
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        publisher.write(hw);
        
        Domain.removeParticipant(participant);
        
        System.out.println("Publisher finished");
        
        Kiara.shutdown();
    }
    
    public static void shutdownPublisherTest() {
        HelloWorldType type = new HelloWorldType();
        HelloWorld hw = type.createData();
        
        hw.setInnerLongAtt(10);
        hw.setInnerStringAtt("Hello World");
        
        ParticipantAttributes pAtt = new ParticipantAttributes();
        //pAtt.rtps.defaultSendPort = 11511;
        pAtt.rtps.useIPv4ToSend = true;
        pAtt.rtps.builtinAtt.useSimplePDP = true;
        pAtt.rtps.builtinAtt.useWriterLP = false;
        pAtt.rtps.builtinAtt.useSimpleEDP = true;
        pAtt.rtps.builtinAtt.useStaticEDP = true;
        pAtt.rtps.builtinAtt.setStaticEndpointXMLFilename("WRITER_ENDPOINTS.xml");
        
        pAtt.rtps.setName("participant1");
        
        Participant participant = Domain.createParticipant(pAtt, null /*new PartListener()*/);
        
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
        pubAtt.setUserDefinedID((short) 1);
        pubAtt.topic.topicKind = TopicKind.NO_KEY;
        pubAtt.topic.topicDataTypeName = "HelloWorld";
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
        
        Publisher<HelloWorld> publisher = Domain.createPublisher(participant, pubAtt, new PubListener()); 
        
        if (publisher == null) {
            System.out.println("Error creating publisher");
            Domain.removeParticipant(participant);
            return;
        }
        
        Scanner scan = new Scanner(System.in);
        
        String line = null;
        while (line == null) {
            System.out.println("Press any key to send sample...");
            line = scan.nextLine();
        }
        
        publisher.write(hw);
        
        System.out.println("Sample sent");
        
        line = null;
        while (line == null) {
            System.out.println("Press any key to remove publisher...");
            line = scan.nextLine();
        }
        
        Domain.removePublisher(publisher);
        
        System.out.println("Publisher removed");
        
        line = null;
        while (line == null) {
            System.out.println("Press any key to remove participant...");
            line = scan.nextLine();
        }
        
        scan.close();
        
        Domain.removeParticipant(participant);
        
        System.out.println("Participant removed");
        
        Kiara.shutdown();
        
        System.out.println("PublisherTest finished");
    }
    
}
