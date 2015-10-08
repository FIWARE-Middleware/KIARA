/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
*
* Copyright (C) 2015 German Research Center for Artificial Intelligence (DFKI)
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
package org.fiware.kiara.ps.initialization;


import static org.junit.Assert.*;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.fiware.kiara.ps.Domain;
import org.fiware.kiara.ps.attributes.ParticipantAttributes;
import org.fiware.kiara.ps.attributes.PublisherAttributes;
import org.fiware.kiara.ps.attributes.SubscriberAttributes;
import org.fiware.kiara.ps.common.PubListener;
import org.fiware.kiara.ps.common.SubListener;
import org.fiware.kiara.ps.initialization.types.Initialization;
import org.fiware.kiara.ps.initialization.types.InitializationType;
import org.fiware.kiara.ps.participant.Participant;
import org.fiware.kiara.ps.participant.ParticipantDiscoveryInfo;
import org.fiware.kiara.ps.participant.ParticipantListener;
import org.fiware.kiara.ps.qos.policies.HistoryQosPolicyKind;
import org.fiware.kiara.ps.qos.policies.LivelinessQosPolicyKind;
import org.fiware.kiara.ps.qos.policies.ReliabilityQosPolicyKind;
import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.common.TopicKind;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;
import org.fiware.kiara.ps.rtps.utils.IPFinder;
import org.fiware.kiara.ps.subscriber.Subscriber;
import org.fiware.kiara.ps.types.HelloWorld;
import org.fiware.kiara.ps.types.HelloWorldType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public class EndpointInitializationTest {

   static {
       System.setProperty("java.util.logging.config.file", "logging.properties");
   }

   public EndpointInitializationTest() {
   }

   @BeforeClass
   public static void setUpClass() {
   }

   @AfterClass
   public static void tearDownClass() {
   }

   private static final InitializationType type = new InitializationType();

   @Before
   public void setUp() {
       //Domain.stopAll();
   }

   @After
   public void tearDown() {
   }
   
   @Test
   public void publisherNoType() throws InterruptedException, ExecutionException {

       Initialization hw = type.createData();

       hw.setInnerLongAtt(10);
       hw.setInnerStringAtt("Hello World");

       // Create participant
       ParticipantAttributes pParam = new ParticipantAttributes();
       pParam.rtps.builtinAtt.useSimplePDP = true;
       pParam.rtps.builtinAtt.useWriterLP = false;
       pParam.rtps.builtinAtt.useSimpleEDP = true;
       pParam.rtps.builtinAtt.useStaticEDP = false;
       
       Participant participant = Domain.createParticipant(pParam, null);
       assertNotNull("Error creating participant", participant);
       
       // Create publisher
       PublisherAttributes pubAtt = new PublisherAttributes();
       pubAtt.setUserDefinedID((short) 1);
       pubAtt.topic.topicKind = TopicKind.NO_KEY;
       pubAtt.qos.reliability.kind = ReliabilityQosPolicyKind.BEST_EFFORT_RELIABILITY_QOS;
       pubAtt.topic.topicDataTypeName = "WrongName";
       pubAtt.topic.topicName = "InitializationTopic";

       org.fiware.kiara.ps.publisher.Publisher<HelloWorld> publisher = null;
       publisher = Domain.createPublisher(participant, pubAtt, new PubListener());

       if (publisher == null) {
           Domain.removeParticipant(participant);
       }

       assertNull(publisher);

   }
   
   @Test
   public void subscriberNoType() throws InterruptedException, ExecutionException {

       Initialization hw = type.createData();

       hw.setInnerLongAtt(10);
       hw.setInnerStringAtt("Hello World");

       // Create participant
       ParticipantAttributes pParam = new ParticipantAttributes();
       pParam.rtps.builtinAtt.useSimplePDP = true;
       pParam.rtps.builtinAtt.useWriterLP = false;
       pParam.rtps.builtinAtt.useSimpleEDP = true;
       pParam.rtps.builtinAtt.useStaticEDP = false;
       
       Participant participant = Domain.createParticipant(pParam, null);
       assertNotNull("Error creating participant", participant);
       
       // Create publisher
       SubscriberAttributes subAtt = new SubscriberAttributes();
       subAtt.setUserDefinedID((short) 1);
       subAtt.topic.topicKind = TopicKind.NO_KEY;
       subAtt.qos.reliability.kind = ReliabilityQosPolicyKind.BEST_EFFORT_RELIABILITY_QOS;
       subAtt.topic.topicDataTypeName = "WrongName";
       subAtt.topic.topicName = "InitializationTopic";

       Subscriber<HelloWorld> subscriber = null;
       subscriber = Domain.createSubscriber(participant, subAtt, null);

       if (subscriber == null) {
           Domain.removeParticipant(participant);
       }

       assertNull(subscriber);

   }
   
   @Test
   public void publisherWrongType() throws InterruptedException, ExecutionException {

       Initialization hw = type.createData();

       hw.setInnerLongAtt(10);
       hw.setInnerStringAtt("Hello World");

       // Create participant
       ParticipantAttributes pParam = new ParticipantAttributes();
       pParam.rtps.builtinAtt.useSimplePDP = true;
       pParam.rtps.builtinAtt.useWriterLP = false;
       pParam.rtps.builtinAtt.useSimpleEDP = true;
       pParam.rtps.builtinAtt.useStaticEDP = false;
       
       Participant participant = Domain.createParticipant(pParam, null);
       assertNotNull("Error creating participant", participant);
       
       boolean registered = Domain.registerType(participant, type);
       assertTrue("Error registering type", registered);
       
       // Create publisher
       PublisherAttributes pubAtt = new PublisherAttributes();
       pubAtt.setUserDefinedID((short) 1);
       pubAtt.topic.topicKind = TopicKind.NO_KEY;
       pubAtt.qos.reliability.kind = ReliabilityQosPolicyKind.BEST_EFFORT_RELIABILITY_QOS;
       pubAtt.topic.topicDataTypeName = "WrongName";
       pubAtt.topic.topicName = "InitializationTopic";

       org.fiware.kiara.ps.publisher.Publisher<HelloWorld> publisher = null;
       publisher = Domain.createPublisher(participant, pubAtt, new PubListener());

       if (publisher == null) {
           Domain.removeParticipant(participant);
       }

       assertNull(publisher);

   }
   
   @Test
   public void subscriberWrongType() throws InterruptedException, ExecutionException {

       Initialization hw = type.createData();

       hw.setInnerLongAtt(10);
       hw.setInnerStringAtt("Hello World");

       // Create participant
       ParticipantAttributes pParam = new ParticipantAttributes();
       pParam.rtps.builtinAtt.useSimplePDP = true;
       pParam.rtps.builtinAtt.useWriterLP = false;
       pParam.rtps.builtinAtt.useSimpleEDP = true;
       pParam.rtps.builtinAtt.useStaticEDP = false;
       
       Participant participant = Domain.createParticipant(pParam, null);
       assertNotNull("Error creating participant", participant);
       
       boolean registered = Domain.registerType(participant, type);
       assertTrue("Error registering type", registered);
       
       // Create publisher
       SubscriberAttributes subAtt = new SubscriberAttributes();
       subAtt.setUserDefinedID((short) 1);
       subAtt.topic.topicKind = TopicKind.NO_KEY;
       subAtt.qos.reliability.kind = ReliabilityQosPolicyKind.BEST_EFFORT_RELIABILITY_QOS;
       subAtt.topic.topicDataTypeName = "WrongName";
       subAtt.topic.topicName = "InitializationTopic";

       Subscriber<HelloWorld> subscriber = null;
       subscriber = Domain.createSubscriber(participant, subAtt, null);

       if (subscriber == null) {
           Domain.removeParticipant(participant);
       }

       assertNull(subscriber);

   }
   
   @Test
   public void publisherInvalidLocator() throws InterruptedException, ExecutionException {

       Initialization hw = type.createData();

       hw.setInnerLongAtt(10);
       hw.setInnerStringAtt("Hello World");

       // Create participant
       ParticipantAttributes pParam = new ParticipantAttributes();
       pParam.rtps.builtinAtt.useSimplePDP = true;
       pParam.rtps.builtinAtt.useWriterLP = false;
       pParam.rtps.builtinAtt.useSimpleEDP = true;
       pParam.rtps.builtinAtt.useStaticEDP = false;
       
       Participant participant = Domain.createParticipant(pParam, null);
       assertNotNull("Error creating participant", participant);
       
       boolean registered = Domain.registerType(participant, type);
       assertTrue("Error registering type", registered);
       
       // Create publisher
       PublisherAttributes pubAtt = new PublisherAttributes();
       pubAtt.setUserDefinedID((short) 1);
       pubAtt.topic.topicKind = TopicKind.NO_KEY;
       pubAtt.qos.reliability.kind = ReliabilityQosPolicyKind.BEST_EFFORT_RELIABILITY_QOS;
       pubAtt.topic.topicDataTypeName = "Initialization";
       pubAtt.topic.topicName = "InitializationTopic";
       pubAtt.unicastLocatorList.pushBack(new Locator(-1));

       org.fiware.kiara.ps.publisher.Publisher<HelloWorld> publisher = null;
       publisher = Domain.createPublisher(participant, pubAtt, new PubListener());

       if (publisher == null) {
           Domain.removeParticipant(participant);
       }

       assertNull(publisher);

   }
   
   @Test
   public void subscriberInvalidLocator() throws InterruptedException, ExecutionException {

       Initialization hw = type.createData();

       hw.setInnerLongAtt(10);
       hw.setInnerStringAtt("Hello World");

       // Create participant
       ParticipantAttributes pParam = new ParticipantAttributes();
       pParam.rtps.builtinAtt.useSimplePDP = true;
       pParam.rtps.builtinAtt.useWriterLP = false;
       pParam.rtps.builtinAtt.useSimpleEDP = true;
       pParam.rtps.builtinAtt.useStaticEDP = false;
       
       Participant participant = Domain.createParticipant(pParam, null);
       assertNotNull("Error creating participant", participant);
       
       boolean registered = Domain.registerType(participant, type);
       assertTrue("Error registering type", registered);
       
       // Create publisher
       SubscriberAttributes subAtt = new SubscriberAttributes();
       subAtt.setUserDefinedID((short) 1);
       subAtt.topic.topicKind = TopicKind.NO_KEY;
       subAtt.qos.reliability.kind = ReliabilityQosPolicyKind.BEST_EFFORT_RELIABILITY_QOS;
       subAtt.topic.topicDataTypeName = "Initialization";
       subAtt.topic.topicName = "InitializationTopic";
       subAtt.unicastLocatorList.pushBack(new Locator(-1));

       Subscriber<HelloWorld> subscriber = null;
       subscriber = Domain.createSubscriber(participant, subAtt, null);

       if (subscriber == null) {
           Domain.removeParticipant(participant);
       }

       assertNull(subscriber);

   }
   
   @Test
   public void publisherWrongDepth() throws InterruptedException, ExecutionException {

       Initialization hw = type.createData();

       hw.setInnerLongAtt(10);
       hw.setInnerStringAtt("Hello World");

       // Create participant
       ParticipantAttributes pParam = new ParticipantAttributes();
       pParam.rtps.builtinAtt.useSimplePDP = true;
       pParam.rtps.builtinAtt.useWriterLP = false;
       pParam.rtps.builtinAtt.useSimpleEDP = true;
       pParam.rtps.builtinAtt.useStaticEDP = false;
       
       Participant participant = Domain.createParticipant(pParam, null);
       assertNotNull("Error creating participant", participant);
       
       boolean registered = Domain.registerType(participant, type);
       assertTrue("Error registering type", registered);
       
       // Create publisher
       PublisherAttributes pubAtt = new PublisherAttributes();
       pubAtt.setUserDefinedID((short) 1);
       pubAtt.topic.topicKind = TopicKind.WITH_KEY;
       pubAtt.qos.reliability.kind = ReliabilityQosPolicyKind.BEST_EFFORT_RELIABILITY_QOS;
       pubAtt.topic.topicDataTypeName = "Initialization";
       pubAtt.topic.topicName = "InitializationTopic";
       
       org.fiware.kiara.ps.publisher.Publisher<HelloWorld> publisher = null;
       publisher = Domain.createPublisher(participant, pubAtt, new PubListener());

       if (publisher == null) {
           Domain.removeParticipant(participant);
       }

       assertNull(publisher);

   }
   
   @Test
   public void subscriberWrongDepth() throws InterruptedException, ExecutionException {

       Initialization hw = type.createData();

       hw.setInnerLongAtt(10);
       hw.setInnerStringAtt("Hello World");

       // Create participant
       ParticipantAttributes pParam = new ParticipantAttributes();
       pParam.rtps.builtinAtt.useSimplePDP = true;
       pParam.rtps.builtinAtt.useWriterLP = false;
       pParam.rtps.builtinAtt.useSimpleEDP = true;
       pParam.rtps.builtinAtt.useStaticEDP = false;
       
       Participant participant = Domain.createParticipant(pParam, null);
       assertNotNull("Error creating participant", participant);
       
       boolean registered = Domain.registerType(participant, type);
       assertTrue("Error registering type", registered);
       
       // Create publisher
       SubscriberAttributes subAtt = new SubscriberAttributes();
       subAtt.setUserDefinedID((short) 1);
       subAtt.topic.topicKind = TopicKind.WITH_KEY;
       subAtt.qos.reliability.kind = ReliabilityQosPolicyKind.BEST_EFFORT_RELIABILITY_QOS;
       subAtt.topic.topicDataTypeName = "Initialization";
       subAtt.topic.topicName = "InitializationTopic";
       
       Subscriber<HelloWorld> subscriber = null;
       subscriber = Domain.createSubscriber(participant, subAtt, null);

       if (subscriber == null) {
           Domain.removeParticipant(participant);
       }

       assertNull(subscriber);

   }
   
   @Test
   public void publisherNotDefinedUserID() throws InterruptedException, ExecutionException {

       Initialization hw = type.createData();
       type.setGetKeyDefined(false);

       hw.setInnerLongAtt(10);
       hw.setInnerStringAtt("Hello World");

       // Create participant
       ParticipantAttributes pParam = new ParticipantAttributes();
       pParam.rtps.builtinAtt.useSimplePDP = true;
       pParam.rtps.builtinAtt.useWriterLP = false;
       pParam.rtps.builtinAtt.useSimpleEDP = true;
       pParam.rtps.builtinAtt.useStaticEDP = false;
       
       Participant participant = Domain.createParticipant(pParam, null);
       assertNotNull("Error creating participant", participant);
       
       boolean registered = Domain.registerType(participant, type);
       assertTrue("Error registering type", registered);
       
       // Create publisher
       PublisherAttributes pubAtt = new PublisherAttributes();
       pubAtt.setUserDefinedID((short) 1);
       pubAtt.topic.topicKind = TopicKind.WITH_KEY;
       pubAtt.qos.reliability.kind = ReliabilityQosPolicyKind.BEST_EFFORT_RELIABILITY_QOS;
       pubAtt.topic.topicDataTypeName = "Initialization";
       pubAtt.topic.topicName = "InitializationTopic";
       pubAtt.topic.historyQos.depth = 5;
       pubAtt.topic.resourceLimitQos.maxSamplesPerInstance = 10;
       
       org.fiware.kiara.ps.publisher.Publisher<HelloWorld> publisher = null;
       publisher = Domain.createPublisher(participant, pubAtt, new PubListener());

       if (publisher == null) {
           Domain.removeParticipant(participant);
       }

       assertNull(publisher);

   }
   
   @Test
   public void subscriberNotDefinedUserID() throws InterruptedException, ExecutionException {

       Initialization hw = type.createData();
       type.setGetKeyDefined(false);

       hw.setInnerLongAtt(10);
       hw.setInnerStringAtt("Hello World");

       // Create participant
       ParticipantAttributes pParam = new ParticipantAttributes();
       pParam.rtps.builtinAtt.useSimplePDP = true;
       pParam.rtps.builtinAtt.useWriterLP = false;
       pParam.rtps.builtinAtt.useSimpleEDP = true;
       pParam.rtps.builtinAtt.useStaticEDP = false;
       
       Participant participant = Domain.createParticipant(pParam, null);
       assertNotNull("Error creating participant", participant);
       
       boolean registered = Domain.registerType(participant, type);
       assertTrue("Error registering type", registered);
       
       // Create publisher
       SubscriberAttributes subAtt = new SubscriberAttributes();
       subAtt.setUserDefinedID((short) 1);
       subAtt.topic.topicKind = TopicKind.WITH_KEY;
       subAtt.qos.reliability.kind = ReliabilityQosPolicyKind.BEST_EFFORT_RELIABILITY_QOS;
       subAtt.topic.topicDataTypeName = "Initialization";
       subAtt.topic.topicName = "InitializationTopic";
       subAtt.topic.historyQos.depth = 5;
       subAtt.topic.resourceLimitQos.maxSamplesPerInstance = 10;
       
       Subscriber<HelloWorld> subscriber = null;
       subscriber = Domain.createSubscriber(participant, subAtt, null);

       if (subscriber == null) {
           Domain.removeParticipant(participant);
       }

       assertNull(subscriber);

   }

   @Test
   public void publisherBestEffort() throws InterruptedException, ExecutionException {

       Initialization hw = type.createData();

       hw.setInnerLongAtt(10);
       hw.setInnerStringAtt("Hello World");

       // Create participant
       ParticipantAttributes pParam = new ParticipantAttributes();
       pParam.rtps.builtinAtt.useSimplePDP = true;
       pParam.rtps.builtinAtt.useWriterLP = false;
       pParam.rtps.builtinAtt.useSimpleEDP = true;
       pParam.rtps.builtinAtt.useStaticEDP = false;
       
       Participant participant = Domain.createParticipant(pParam, null);
       assertNotNull("Error creating participant", participant);
       
       boolean registered = Domain.registerType(participant, type);
       assertTrue("Error registering type", registered);
       
       // Create publisher
       PublisherAttributes pubAtt = new PublisherAttributes();
       pubAtt.setUserDefinedID((short) 1);
       pubAtt.topic.topicKind = TopicKind.NO_KEY;
       pubAtt.qos.reliability.kind = ReliabilityQosPolicyKind.BEST_EFFORT_RELIABILITY_QOS;
       pubAtt.topic.topicDataTypeName = "Initialization";
       pubAtt.topic.topicName = "InitializationTopic";

       org.fiware.kiara.ps.publisher.Publisher<HelloWorld> publisher = null;
       publisher = Domain.createPublisher(participant, pubAtt, new PubListener());

       if (publisher == null) {
           Domain.removeParticipant(participant);
       }
       
       Domain.removePublisher(publisher);
       
       assertNotNull("Error creating publisher", publisher);

   }
   
   @Test
   public void subscriberBestEffort() throws InterruptedException, ExecutionException {

       Initialization hw = type.createData();

       hw.setInnerLongAtt(10);
       hw.setInnerStringAtt("Hello World");

       // Create participant
       ParticipantAttributes pParam = new ParticipantAttributes();
       pParam.rtps.builtinAtt.useSimplePDP = true;
       pParam.rtps.builtinAtt.useWriterLP = false;
       pParam.rtps.builtinAtt.useSimpleEDP = true;
       pParam.rtps.builtinAtt.useStaticEDP = false;
       
       Participant participant = Domain.createParticipant(pParam, null);
       assertNotNull("Error creating participant", participant);
       
       boolean registered = Domain.registerType(participant, type);
       assertTrue("Error registering type", registered);
       
       // Create publisher
       SubscriberAttributes subAtt = new SubscriberAttributes();
       subAtt.setUserDefinedID((short) 1);
       subAtt.topic.topicKind = TopicKind.NO_KEY;
       subAtt.qos.reliability.kind = ReliabilityQosPolicyKind.BEST_EFFORT_RELIABILITY_QOS;
       subAtt.topic.topicDataTypeName = "Initialization";
       subAtt.topic.topicName = "InitializationTopic";

       Subscriber<HelloWorld> subscriber = null;
       subscriber = Domain.createSubscriber(participant, subAtt, null);

       if (subscriber == null) {
           Domain.removeParticipant(participant);
       }
       
       Domain.removeSubscriber(subscriber);

       assertNotNull("Error creating publisher", subscriber);

   }
   
   @Test
   public void publisherReliable() throws InterruptedException, ExecutionException {

       Initialization hw = type.createData();

       hw.setInnerLongAtt(10);
       hw.setInnerStringAtt("Hello World");

       // Create participant
       ParticipantAttributes pParam = new ParticipantAttributes();
       pParam.rtps.builtinAtt.useSimplePDP = true;
       pParam.rtps.builtinAtt.useWriterLP = false;
       pParam.rtps.builtinAtt.useSimpleEDP = true;
       pParam.rtps.builtinAtt.useStaticEDP = false;
       
       Participant participant = Domain.createParticipant(pParam, null);
       assertNotNull("Error creating participant", participant);
       
       boolean registered = Domain.registerType(participant, type);
       assertTrue("Error registering type", registered);
       
       // Create publisher
       PublisherAttributes pubAtt = new PublisherAttributes();
       pubAtt.setUserDefinedID((short) 1);
       pubAtt.topic.topicKind = TopicKind.NO_KEY;
       pubAtt.qos.reliability.kind = ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS;
       pubAtt.topic.topicDataTypeName = "Initialization";
       pubAtt.topic.topicName = "InitializationTopic";

       org.fiware.kiara.ps.publisher.Publisher<HelloWorld> publisher = null;
       publisher = Domain.createPublisher(participant, pubAtt, new PubListener());

       if (publisher == null) {
           Domain.removeParticipant(participant);
       }
       
       Domain.removePublisher(publisher);

       assertNotNull("Error creating publisher", publisher);

   }
   
   @Test
   public void subscriberReliable() throws InterruptedException, ExecutionException {

       Initialization hw = type.createData();

       hw.setInnerLongAtt(10);
       hw.setInnerStringAtt("Hello World");

       // Create participant
       ParticipantAttributes pParam = new ParticipantAttributes();
       pParam.rtps.builtinAtt.useSimplePDP = true;
       pParam.rtps.builtinAtt.useWriterLP = false;
       pParam.rtps.builtinAtt.useSimpleEDP = true;
       pParam.rtps.builtinAtt.useStaticEDP = false;
       
       Participant participant = Domain.createParticipant(pParam, null);
       assertNotNull("Error creating participant", participant);
       
       boolean registered = Domain.registerType(participant, type);
       assertTrue("Error registering type", registered);
       
       // Create publisher
       SubscriberAttributes subAtt = new SubscriberAttributes();
       subAtt.setUserDefinedID((short) 1);
       subAtt.topic.topicKind = TopicKind.NO_KEY;
       subAtt.qos.reliability.kind = ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS;
       subAtt.topic.topicDataTypeName = "Initialization";
       subAtt.topic.topicName = "InitializationTopic";

       Subscriber<HelloWorld> subscriber = null;
       subscriber = Domain.createSubscriber(participant, subAtt, null);

       if (subscriber == null) {
           Domain.removeParticipant(participant);
       }
       
       Domain.removeSubscriber(subscriber);

       assertNotNull("Error creating publisher", subscriber);

   }
   
   @Test
   public void subscriberReliableMultiple() throws InterruptedException, ExecutionException {

       Initialization hw = type.createData();

       hw.setInnerLongAtt(10);
       hw.setInnerStringAtt("Hello World");

       // Create participant
       ParticipantAttributes pParam = new ParticipantAttributes();
       pParam.rtps.builtinAtt.useSimplePDP = true;
       pParam.rtps.builtinAtt.useWriterLP = false;
       pParam.rtps.builtinAtt.useSimpleEDP = true;
       pParam.rtps.builtinAtt.useStaticEDP = false;
       
       Participant participant = Domain.createParticipant(pParam, null);
       assertNotNull("Error creating participant", participant);
       
       boolean registered = Domain.registerType(participant, type);
       assertTrue("Error registering type", registered);
       
       // Create publisher
       SubscriberAttributes subAtt = new SubscriberAttributes();
       subAtt.setUserDefinedID((short) 1);
       subAtt.setEntityID((short) 1);
       subAtt.topic.topicKind = TopicKind.NO_KEY;
       subAtt.qos.reliability.kind = ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS;
       subAtt.topic.topicDataTypeName = "Initialization";
       subAtt.topic.topicName = "InitializationTopic";
       
       SubscriberAttributes subAtt2 = new SubscriberAttributes();
       subAtt2.setUserDefinedID((short) 1);
       subAtt2.setEntityID((short) 1); 
       subAtt2.topic.topicKind = TopicKind.NO_KEY;
       subAtt2.qos.reliability.kind = ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS;
       subAtt2.topic.topicDataTypeName = "Initialization";
       subAtt2.topic.topicName = "InitializationTopic";

       Subscriber<HelloWorld> subscriber = null;
       subscriber = Domain.createSubscriber(participant, subAtt, null);

       if (subscriber == null) {
           Domain.removeParticipant(participant);
       }

       assertNotNull("Error creating publisher", subscriber);
       
       Subscriber<HelloWorld> subscriber2 = null;
       subscriber2 = Domain.createSubscriber(participant, subAtt2, null);

       if (subscriber2 == null) {
           Domain.removeParticipant(participant);
       }

       assertNull(subscriber2);
       
       Domain.removeSubscriber(subscriber);
       Domain.removeSubscriber(subscriber2);

   }
   
   
   
   

}
