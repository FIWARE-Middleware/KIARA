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
package org.fiware.kiara.ps;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.fiware.kiara.Kiara;
import org.fiware.kiara.ps.Domain;
import org.fiware.kiara.ps.attributes.ParticipantAttributes;
import org.fiware.kiara.ps.attributes.PublisherAttributes;
import org.fiware.kiara.ps.attributes.SubscriberAttributes;
import org.fiware.kiara.ps.common.PubListener;
import org.fiware.kiara.ps.participant.Participant;
import org.fiware.kiara.ps.qos.policies.HistoryQosPolicyKind;
import org.fiware.kiara.ps.qos.policies.LivelinessQosPolicyKind;
import org.fiware.kiara.ps.qos.policies.ReliabilityQosPolicyKind;
import org.fiware.kiara.ps.rtps.common.MatchingInfo;
import org.fiware.kiara.ps.rtps.common.MatchingStatus;
import org.fiware.kiara.ps.rtps.common.TopicKind;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;
import org.fiware.kiara.ps.rtps.utils.IPFinder;
import org.fiware.kiara.ps.subscriber.SampleInfo;
import org.fiware.kiara.ps.subscriber.SubscriberListener;
import org.fiware.kiara.ps.topic.TopicDataType;
import org.fiware.kiara.ps.types.HelloWorld;
import org.fiware.kiara.ps.types.HelloWorldType;
import org.fiware.kiara.serialization.impl.Serializable;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public class SubscriptionTest {

    static {
        //System.setProperty("java.util.logging.config.file", "/home/rubinste/.kiara/logging.properties");
        //System.setProperty("java.util.logging.config.file", "/home/rubinste/.kiara/nologging.properties");
        System.setProperty("java.util.logging.config.file", "logging.properties");
    }

    public SubscriptionTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    private static final HelloWorldType hwtype = new HelloWorldType();

    private static class Subscriber implements Callable<Boolean> {

        private final CountDownLatch initSignal;
        private final CountDownLatch startSignal;
        private final CountDownLatch doneSignal;

        public Subscriber(CountDownLatch initSignal, CountDownLatch startSignal, CountDownLatch doneSignal) {
            this.initSignal = initSignal;
            this.startSignal = startSignal;
            this.doneSignal = doneSignal;
        }

        @Override
        public Boolean call() throws Exception {
            HelloWorld hw = hwtype.createData();

            hw.setInnerLongAtt(10);
            hw.setInnerStringAtt("Hello World");

            // Create participant
            ParticipantAttributes pParam = new ParticipantAttributes();
            pParam.rtps.builtinAtt.useSimplePDP = true;
            pParam.rtps.builtinAtt.useWriterLP = false;
            pParam.rtps.builtinAtt.useSimpleEDP = true;
            pParam.rtps.builtinAtt.useStaticEDP = true;

            final String ipAddr = IPFinder.getFirstIPv4Address().getHostAddress();
            System.out.println("IP Addr " + ipAddr);

            final String edpXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                    + "<staticdiscovery>"
                    + "    <participant>"
                    + "        <name>participant1</name>"
                    + "        <writer>"
                    + "            <userId>1</userId>"
                    + "            <topicName>HelloWorldTopic</topicName>"
                    + "            <topicDataType>HelloWorld</topicDataType>"
                    + "            <topicKind>NO_KEY</topicKind>"
                    + "            <reliabilityQos>BEST_EFFORT_RELIABILITY_QOS</reliabilityQos>"
                    + "            <livelinessQos kind=\"AUTOMATIC_LIVELINESS_QOS\" leaseDuration_ms=\"100\"></livelinessQos>"
                    + "        </writer>"
                    + "     </participant>"
                    + "    </staticdiscovery>";

            pParam.rtps.builtinAtt.setStaticEndpointXML(edpXml);

            pParam.rtps.setName("participant1");

            Participant participant = Domain.createParticipant(pParam, null /*new PartListener()*/);
            if (participant == null) {
                System.out.println("Error when creating participant");
                return false;
            }

            System.out.println("Subscriber participant SPDP MC Port: " + participant.getSPDPMulticastPort());
            System.out.println("Subscriber participant SPDP UC Port: " + participant.getSPDPUnicastPort());
            System.out.println("Subscriber participant User MC Port: " + participant.getUserMulticastPort());
            System.out.println("Subscriber participant User UC Port: " + participant.getUserUnicastPort());

            initSignal.countDown();

            // Type registration
            Domain.registerType(participant, hwtype);

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

            final CountDownLatch workInitSignal = new CountDownLatch(1);
            final CountDownLatch workDoneSignal = new CountDownLatch(1);

            org.fiware.kiara.ps.subscriber.Subscriber<HelloWorld> subscriber = Domain.createSubscriber(participant, satt, new SubscriberListener() {

                private int n_matched;

                @Override
                public void onNewDataMessage(org.fiware.kiara.ps.subscriber.Subscriber<?> sub) {
                    System.out.println("Message received");
                    //SampleInfo info = new SampleInfo();
                    HelloWorld type = (HelloWorld) sub.takeNextData(null);
                    while (type != null) {
                        //HelloWorld instance = (HelloWorld) type;
                        System.out.println(type.getInnerStringAtt());
                        type = (HelloWorld) sub.takeNextData(null);
                        workDoneSignal.countDown();
                        System.out.println("workDoneSignal sent");
                    }
                }

                @Override
                public void onSubscriptionMatched(org.fiware.kiara.ps.subscriber.Subscriber<?> sub, MatchingInfo info) {
                    if (info.status == MatchingStatus.MATCHED_MATHING) {
                        n_matched++;
                        System.out.println("Publisher Matched. Total : " + this.n_matched);
                        workInitSignal.countDown();
                    } else {
                        n_matched--;
                        System.out.println("Publisher Unmatched. Total : " + this.n_matched);
                    }
                }

            });
            assertNotNull("Error creating subscriber", subscriber);

            try {
                workInitSignal.await(15000, TimeUnit.MILLISECONDS);
                startSignal.countDown();
                workDoneSignal.await(15000, TimeUnit.MILLISECONDS);
                doneSignal.countDown();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            assertEquals(0, workInitSignal.getCount());
            assertEquals(0, workDoneSignal.getCount());

            //Domain.removeParticipant(participant);
            //Kiara.shutdown();
            System.out.println("Subscriber finished");

            return true;
        }

    }

    private static class Publisher implements Callable<Boolean> {

        private final CountDownLatch initSignal;
        private final CountDownLatch startSignal;
        private final CountDownLatch doneSignal;

        public Publisher(CountDownLatch initSignal, CountDownLatch startSignal, CountDownLatch doneSignal) {
            this.initSignal = initSignal;
            this.startSignal = startSignal;
            this.doneSignal = doneSignal;
        }

        @Override
        public Boolean call() throws Exception {
            initSignal.await();

            HelloWorld hw = hwtype.createData();

            hw.setInnerLongAtt(10);
            hw.setInnerStringAtt("Hello World");

            ParticipantAttributes pAtt = new ParticipantAttributes();
            //pAtt.rtps.defaultSendPort = 11511;
            pAtt.rtps.useIPv4ToSend = true;
            pAtt.rtps.builtinAtt.useSimplePDP = true;
            pAtt.rtps.builtinAtt.useWriterLP = false;
            pAtt.rtps.builtinAtt.useSimpleEDP = true;
            pAtt.rtps.builtinAtt.useStaticEDP = true;

            final String ipAddr = IPFinder.getFirstIPv4Address().getHostAddress();
            System.out.println("IP Addr " + ipAddr);
            final String edpXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                    + "<staticdiscovery>"
                    + "    <participant>"
                    + "        <name>participant1</name>"
                    + "        <reader>"
                    + "            <userId>1</userId>"
                    + "            <topic name=\"HelloWorldTopic\" dataType=\"HelloWorld\" kind=\"NO_KEY\"></topic>"
                    + "            <expectsInlineQos>false</expectsInlineQos>"
                    + "			<topicKind>NO_KEY</topicKind>"
                    + "            <reliabilityQos>BEST_EFFORT_RELIABILITY_QOS</reliabilityQos>"
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
            pubAtt.topic.topicKind = TopicKind.NO_KEY;
            pubAtt.topic.topicDataTypeName = "HelloWorld";
            pubAtt.topic.topicName = "HelloWorldTopic";
            pubAtt.topic.historyQos.kind = HistoryQosPolicyKind.KEEP_LAST_HISTORY_QOS;
            pubAtt.topic.historyQos.depth = 30;
            pubAtt.topic.resourceLimitQos.maxSamples = 50;
            pubAtt.topic.resourceLimitQos.allocatedSamples = 20;
            pubAtt.times.heartBeatPeriod = new Timestamp(2, 200 * 1000 * 1000);
            pubAtt.qos.reliability.kind = ReliabilityQosPolicyKind.BEST_EFFORT_RELIABILITY_QOS;
            pubAtt.qos.liveliness.kind = LivelinessQosPolicyKind.AUTOMATIC_LIVELINESS_QOS;
            pubAtt.qos.liveliness.leaseDuration = new Timestamp(5, 1);
            pubAtt.qos.liveliness.announcementPeriod = new Timestamp(5, 0);

            org.fiware.kiara.ps.publisher.Publisher<HelloWorld> publisher = Domain.createPublisher(participant, pubAtt, new PubListener());

            if (publisher == null) {
                Domain.removeParticipant(participant);
            }
            assertNotNull("Error creating publisher", publisher);

            try {
                startSignal.await();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            int i = 1;
            while (true) {
                System.out.println("Send message " + i);
                ++i;
                publisher.write(hw);
                try {
                    if (doneSignal.await(10, TimeUnit.MILLISECONDS)) {
                        System.out.println("Work done !!!");
                        break;
                    }
                } catch (InterruptedException e) {
                    System.out.println("Interrupted !!!");
                }
            }

            //Domain.removeParticipant(participant);

            System.out.println("Publisher finished");

            return true;
        }

    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void testSubscribePublish() throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newCachedThreadPool();
        CountDownLatch initSignal = new CountDownLatch(1);
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(1);
        Future<Boolean> subscriber = es.submit(new Subscriber(initSignal, startSignal, doneSignal));
        Future<Boolean> publisher = es.submit(new Publisher(initSignal, startSignal, doneSignal));

        assertTrue(subscriber.get());
        assertTrue(publisher.get());

    }
}
