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
package org.fiware.kiara.ps.discovery.endpoint.dyn;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.fiware.kiara.ps.Domain;
import org.fiware.kiara.ps.attributes.ParticipantAttributes;
import org.fiware.kiara.ps.attributes.PublisherAttributes;
import org.fiware.kiara.ps.attributes.SubscriberAttributes;
import org.fiware.kiara.ps.participant.Participant;
import org.fiware.kiara.ps.participant.ParticipantDiscoveryInfo;
import org.fiware.kiara.ps.participant.ParticipantListener;
import org.fiware.kiara.ps.publisher.PublisherListener;
import org.fiware.kiara.ps.qos.policies.HistoryQosPolicyKind;
import org.fiware.kiara.ps.qos.policies.LivelinessQosPolicyKind;
import org.fiware.kiara.ps.qos.policies.ReliabilityQosPolicyKind;
import org.fiware.kiara.ps.rtps.common.MatchingInfo;
import org.fiware.kiara.ps.rtps.common.MatchingStatus;
import org.fiware.kiara.ps.rtps.common.TopicKind;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;
import org.fiware.kiara.ps.rtps.participant.DiscoveryStatus;
import org.fiware.kiara.ps.subscriber.SubscriberListener;
import org.fiware.kiara.ps.types.HelloWorld;
import org.fiware.kiara.ps.types.HelloWorldType;
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
public class DynamicEndpointDiscoveryTest {

    //    CountDownLatch subEndCt;
    //    CountDownLatch pubEndCt;

    static {
        System.setProperty("java.util.logging.config.file", "logging.properties");
    }

    public DynamicEndpointDiscoveryTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void prepare() throws InterruptedException {
        //Thread.sleep(2000);
        System.out.println("---------------------------------------------------------------------------------------");
    }

    private static final HelloWorldType hwtype = new HelloWorldType();

    private static class SubscriberEntity implements Callable<Boolean> { 

        private boolean isReliable;
        private boolean isStatic;
        private boolean isKeyed;
        private int matchedEntities;
        private boolean waitForUnmatching;
        private boolean useWLP;
        private CyclicBarrier myCt;
        private final CountDownLatch remoteCt;
        private CyclicBarrier barrier;
        private CyclicBarrier subscriberBarrier;

        public SubscriberEntity(CyclicBarrier barrier, CyclicBarrier myCt, CyclicBarrier subscriberBarrier, CountDownLatch remoteCt, boolean isReliable, boolean isStatic, boolean isKeyed, boolean waitForUnmatching, boolean useWLP, int nEntities) {
            this.isReliable = isReliable;
            this.isStatic = isStatic;
            this.isKeyed = isKeyed;
            this.matchedEntities = nEntities;
            this.waitForUnmatching = waitForUnmatching;
            this.useWLP = useWLP;
            this.myCt = myCt;
            this.remoteCt = remoteCt;
            this.barrier = barrier;
            this.subscriberBarrier = subscriberBarrier;
        }

        @Override
        public Boolean call() throws Exception {
            HelloWorld hw = hwtype.createData();

            hw.setInnerLongAtt(10);
            hw.setInnerStringAtt("Hello World");

            // Create participant
            ParticipantAttributes pParam = new ParticipantAttributes();
            pParam.rtps.builtinAtt.useSimplePDP = true;
            pParam.rtps.builtinAtt.useWriterLP = this.useWLP;

            pParam.rtps.builtinAtt.useSimpleEDP = true;
            pParam.rtps.builtinAtt.useStaticEDP = false;

            pParam.rtps.setName("participant1");

            final CountDownLatch discoveryCt = new CountDownLatch(1);

            Participant participant = Domain.createParticipant(pParam, new ParticipantListener() {

                @Override
                public void onParticipantDiscovery(Participant p, ParticipantDiscoveryInfo info) {
                    if (info.rtps.status == DiscoveryStatus.DISCOVERED_PARTICIPANT) {
                        //System.out.println("Participant Discovered by SubscriberEntity");
                        discoveryCt.countDown();
                    }
                }
            });

            if (participant == null) {
                System.out.println("Error when creating participant");
                return false;
            }

            discoveryCt.await(5000, TimeUnit.MILLISECONDS);

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

            satt.qos.liveliness.kind = LivelinessQosPolicyKind.AUTOMATIC_LIVELINESS_QOS;
            satt.qos.liveliness.leaseDuration = new Timestamp(5, 0);
            satt.qos.liveliness.announcementPeriod = new Timestamp(3, 0);

            if (this.isReliable) {
                satt.qos.reliability.kind = ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS;
            } else {
                satt.qos.reliability.kind = ReliabilityQosPolicyKind.BEST_EFFORT_RELIABILITY_QOS;
            }

            //satt.setUserDefinedID((short) 1);

            final CountDownLatch matchedSignal = new CountDownLatch(matchedEntities);
            final CountDownLatch unmatchedSignal = new CountDownLatch(matchedEntities);

            org.fiware.kiara.ps.subscriber.Subscriber<HelloWorld> subscriber = Domain.createSubscriber(participant, satt, new SubscriberListener() {

                private int n_matched;

                @Override
                public void onNewDataMessage(org.fiware.kiara.ps.subscriber.Subscriber<?> sub) { }

                @Override
                public void onSubscriptionMatched(org.fiware.kiara.ps.subscriber.Subscriber<?> sub, MatchingInfo info) {
                    //System.out.println("Publisher recv: " + n_matched);
                    if (info.status == MatchingStatus.MATCHED_MATHING) {
                        n_matched++;
                        matchedSignal.countDown();
                    } else {
                        n_matched--;
                        unmatchedSignal.countDown();
                    }
                }

            });
            assertNotNull("Error creating subscriber", subscriber);

            this.myCt.await();
            
            try {
                matchedSignal.await(5000, TimeUnit.MILLISECONDS);
                if (this.waitForUnmatching) {
                    unmatchedSignal.await(10000, TimeUnit.MILLISECONDS);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            assertEquals(0, matchedSignal.getCount());
            if (this.waitForUnmatching) {
                assertEquals(0, unmatchedSignal.getCount());
            }
            
            /*System.out.println("Sleeping...");
            Thread.sleep(500);*/
            
            this.subscriberBarrier.await();

            Domain.removeSubscriber(subscriber);
            
            Domain.removeParticipant(participant);
            
            this.barrier.await();

            return true;
        }

    }

    private static class PublisherEntity implements Callable<Boolean> {

        private boolean isReliable;
        private boolean isStatic;
        private boolean isKeyed;
        private int matchedEntities;
        private boolean waitForUnmatching;
        private boolean useWLP;
        private final CountDownLatch myCt;
        private final CountDownLatch remoteCt;
        private CyclicBarrier barrier;
        private CyclicBarrier subscriberBarrier;

        public PublisherEntity(CyclicBarrier barrier, CyclicBarrier subscriberBarrier, CountDownLatch myCt, CountDownLatch remoteCt, boolean isReliable, boolean isStatic, boolean isKeyed, boolean waitForUnmatching, boolean useWLP, int nEntities) {
            this.isReliable = isReliable;
            this.isStatic = isStatic;
            this.isKeyed = isKeyed;
            this.matchedEntities = nEntities;
            this.waitForUnmatching = waitForUnmatching;
            this.useWLP = useWLP;
            this.myCt = myCt;
            this.remoteCt = remoteCt;
            this.barrier = barrier;
            this.subscriberBarrier = subscriberBarrier;
        }

        @Override
        public Boolean call() throws Exception {

            HelloWorld hw = hwtype.createData();

            hw.setInnerLongAtt(10);
            hw.setInnerStringAtt("Hello World");

            ParticipantAttributes pAtt = new ParticipantAttributes();
            pAtt.rtps.useIPv4ToSend = true;
            pAtt.rtps.builtinAtt.useSimplePDP = true;
            pAtt.rtps.builtinAtt.useWriterLP = this.useWLP;

            pAtt.rtps.builtinAtt.useSimpleEDP = true;
            pAtt.rtps.builtinAtt.useStaticEDP = false;

            pAtt.rtps.setName("participant2");

            final CountDownLatch discoveryCt = new CountDownLatch(1);

            Participant participant = Domain.createParticipant(pAtt, new ParticipantListener() {

                @Override
                public void onParticipantDiscovery(Participant p, ParticipantDiscoveryInfo info) {
                    if (info.rtps.status == DiscoveryStatus.DISCOVERED_PARTICIPANT) {
                        //System.out.println("Participant Discovered by PublisherEntity");
                        discoveryCt.countDown();
                    }
                }
            });

            assertNotNull("Error when creating participant", participant);

            discoveryCt.await(5000, TimeUnit.MILLISECONDS);

            boolean registered = Domain.registerType(participant, hwtype);
            assertTrue("Error registering type", registered);

            // Create publisher
            PublisherAttributes pubAtt = new PublisherAttributes();
            //pubAtt.setUserDefinedID((short) 1);
            pubAtt.topic.topicKind = TopicKind.NO_KEY;
            pubAtt.topic.topicDataTypeName = "HelloWorld";
            pubAtt.topic.topicName = "HelloWorldTopic";
            pubAtt.topic.historyQos.kind = HistoryQosPolicyKind.KEEP_LAST_HISTORY_QOS;
            pubAtt.topic.historyQos.depth = 30;
            pubAtt.topic.resourceLimitQos.maxSamples = 50;
            pubAtt.topic.resourceLimitQos.allocatedSamples = 20;
            pubAtt.times.heartBeatPeriod = new Timestamp(2, 200 * 1000 * 1000);

            pubAtt.qos.liveliness.kind = LivelinessQosPolicyKind.AUTOMATIC_LIVELINESS_QOS;
            pubAtt.qos.liveliness.leaseDuration = new Timestamp(5, 0);
            pubAtt.qos.liveliness.announcementPeriod = new Timestamp(3, 0);

            if (isReliable) {
                pubAtt.qos.reliability.kind = ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS;
            } else {
                pubAtt.qos.reliability.kind = ReliabilityQosPolicyKind.BEST_EFFORT_RELIABILITY_QOS;
            }


            final CountDownLatch matchedSignal = new CountDownLatch(matchedEntities);
            final CountDownLatch unmatchedSignal = new CountDownLatch(matchedEntities);

            org.fiware.kiara.ps.publisher.Publisher<HelloWorld> publisher = Domain.createPublisher(participant, pubAtt, new PublisherListener() {

                private int n_matched = 0;

                @Override
                public void onPublicationMatched(org.fiware.kiara.ps.publisher.Publisher<?> pub, MatchingInfo info) {
                    if (info.status == MatchingStatus.MATCHED_MATHING) {
                        n_matched++;
                        System.out.println("Subscriber matched: " + n_matched);
                        matchedSignal.countDown();
                    } else {
                        n_matched--;
                        System.out.println("Subscriber unmatched: " + n_matched);
                        unmatchedSignal.countDown();
                    }
                }
            });

            if (publisher == null) {
                Domain.removeParticipant(participant);
            }
            assertNotNull("Error creating publisher", publisher);
            
            try {
                matchedSignal.await(5000, TimeUnit.MILLISECONDS);
                
                this.subscriberBarrier.await();
                
                if (this.waitForUnmatching) {
                    unmatchedSignal.await(10000, TimeUnit.MILLISECONDS);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            assertEquals(0, matchedSignal.getCount());
            
            if (this.waitForUnmatching) {
                assertEquals(0, unmatchedSignal.getCount());
            }
            
            Domain.removePublisher(publisher);
            
            Domain.removeParticipant(participant);

            this.barrier.await();

            return true;
        }

    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void dynamicDiscovery() {

        try {
            ExecutorService es = Executors.newCachedThreadPool();

            CyclicBarrier barrier = new CyclicBarrier(3);
            CyclicBarrier subscriberBarrier = new CyclicBarrier(2);
            CyclicBarrier subCt1 = new CyclicBarrier(2);

            //Future<Boolean> subscriber = es.submit(new SubscriberEntity(isReliable, isStatic, isKeyed, waitForUnmatching, useWLP, nEntities));
            Future<Boolean> subscriber = es.submit(new SubscriberEntity(barrier, subCt1, subscriberBarrier, null, false, false, false, false, false, 1));
            subCt1.await();
            Future<Boolean> publisher = es.submit(new PublisherEntity(barrier, subscriberBarrier, null, null, false, false, false, false, false, 1));

            barrier.await(30000, TimeUnit.MILLISECONDS);

            assertTrue(subscriber.get());
            assertTrue(publisher.get());
            
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            assertTrue(false);
        } catch (BrokenBarrierException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            assertTrue(false);
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            assertTrue(false);
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            assertTrue(false);
        }
    }

    /*@Test
    public void dynamicSubscriberUndiscovery() {

        try {
            ExecutorService es = Executors.newCachedThreadPool();

            CyclicBarrier barrier = new CyclicBarrier(3);

            CyclicBarrier subCt1 = new CyclicBarrier(2);

            //Future<Boolean> subscriber = es.submit(new SubscriberEntity(isReliable, isStatic, isKeyed, waitForUnmatching, useWLP, nEntities));
            Future<Boolean> subscriber = es.submit(new SubscriberEntity(barrier, subCt1, null, false, false, false, false, false, 1));
            subCt1.await();
            Future<Boolean> publisher = es.submit(new PublisherEntity(barrier, null, null, false, false, false, true, false, 1));

            barrier.await(30000, TimeUnit.MILLISECONDS);


            assertTrue(subscriber.get());
            assertTrue(publisher.get());

        } catch (BrokenBarrierException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            assertTrue(false);
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            assertTrue(false);
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            assertTrue(false);
        }

    }*/

    /*@Test
    public void dynamicPublisherUndiscovery() {
        try {

            ExecutorService es = Executors.newCachedThreadPool();

            CyclicBarrier barrier = new CyclicBarrier(3);

            CyclicBarrier subCt1 = new CyclicBarrier(2);

            Future<Boolean> subscriber = es.submit(new SubscriberEntity(barrier, subCt1, null, false, false, false, true, false, 1));
            subCt1.await();

            Future<Boolean> publisher = es.submit(new PublisherEntity(barrier, null, null, false, false, false, false, false, 1));

            barrier.await(30000, TimeUnit.MILLISECONDS);

            assertTrue(subscriber.get());
            assertTrue(publisher.get());

        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            assertTrue(false);
        } catch (BrokenBarrierException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            assertTrue(false);
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            assertTrue(false);
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            assertTrue(false);
        } 

    }*/

    @Test
    public void dynamicMultipleSubscriberDiscovery() {
        try {

            ExecutorService es = Executors.newCachedThreadPool();

            CyclicBarrier barrier = new CyclicBarrier(4);
            CyclicBarrier subscriberBarrier = new CyclicBarrier(3);
            CyclicBarrier subCt1 = new CyclicBarrier(2);
            CyclicBarrier subCt2 = new CyclicBarrier(2);

            //Future<Boolean> subscriber = es.submit(new SubscriberEntity(isReliable, isStatic, isKeyed, waitForUnmatching, useWLP, nEntities));
            Future<Boolean> subscriber1 = es.submit(new SubscriberEntity(barrier, subCt1, subscriberBarrier, null, false, false, false, false, false, 1));
            subCt1.await();

            Future<Boolean> subscriber2 = es.submit(new SubscriberEntity(barrier, subCt2, subscriberBarrier, null, false, false, false, false, false, 1));
            subCt2.await();
            Future<Boolean> publisher1 = es.submit(new PublisherEntity(barrier, subscriberBarrier, null, null, false, false, false, false, false, 2));

            barrier.await(30000, TimeUnit.MILLISECONDS);

            assertTrue(subscriber1.get());
            assertTrue(subscriber2.get());
            assertTrue(publisher1.get());

        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            assertTrue(false);
        } catch (BrokenBarrierException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            assertTrue(false);
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            assertTrue(false);
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            assertTrue(false);
        }

    }

    @Test
    public void dynamicMultipleSubscriberUndiscovery() {

        try {
            ExecutorService es = Executors.newCachedThreadPool();

            CyclicBarrier barrier = new CyclicBarrier(4);
            CyclicBarrier subscriberBarrier = new CyclicBarrier(3);
            CyclicBarrier subCt1 = new CyclicBarrier(2);
            CyclicBarrier subCt2 = new CyclicBarrier(2);

            //Future<Boolean> subscriber = es.submit(new SubscriberEntity(isReliable, isStatic, isKeyed, waitForUnmatching, useWLP, nEntities));
            Future<Boolean> subscriber = es.submit(new SubscriberEntity(barrier, subCt1, subscriberBarrier, null, false, false, false, false, false, 1));
            subCt1.await();

            Future<Boolean> subscriber2 = es.submit(new SubscriberEntity(barrier, subCt2, subscriberBarrier, null, false, false, false, false, false, 1));
            subCt2.await();

            Future<Boolean> publisher = es.submit(new PublisherEntity(barrier, subscriberBarrier, null, null, false, false, false, true, false, 2));

            barrier.await(30000, TimeUnit.MILLISECONDS);

            assertTrue(subscriber.get());
            assertTrue(subscriber2.get());
            assertTrue(publisher.get());

        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            assertTrue(false);
        } catch (BrokenBarrierException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            assertTrue(false);
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            assertTrue(false);
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            assertTrue(false);
        } 

    }

   /* @Test
    public void dynamicMultiplePublisherDiscovery() {
        try {

            ExecutorService es = Executors.newCachedThreadPool();

            CyclicBarrier barrier = new CyclicBarrier(4);

            CyclicBarrier subCt1 = new CyclicBarrier(2);

            //Future<Boolean> subscriber = es.submit(new SubscriberEntity(isReliable, isStatic, isKeyed, waitForUnmatching, useWLP, nEntities));
            Future<Boolean> subscriber = es.submit(new SubscriberEntity(barrier, subCt1, null, false, false, false, false, false, 2));
            subCt1.await();

            Future<Boolean> publisher1 = es.submit(new PublisherEntity(barrier, null, null, false, false, false, false, false, 1));
            Future<Boolean> publisher2 = es.submit(new PublisherEntity(barrier, null, null, false, false, false, false, false, 1));

            barrier.await(30000, TimeUnit.MILLISECONDS);

            assertTrue(subscriber.get());
            assertTrue(publisher1.get());
            assertTrue(publisher2.get());

        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            assertTrue(false);
        } catch (BrokenBarrierException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            assertTrue(false);
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            assertTrue(false);
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            assertTrue(false);
        }

    }*/
    
   /* @Test
    public void dynamicMultiplePublisherUndiscovery() {
        try {

            ExecutorService es = Executors.newCachedThreadPool();

            CyclicBarrier barrier = new CyclicBarrier(4);

            CyclicBarrier pubCt1 = new CyclicBarrier(2);

            //Future<Boolean> subscriber = es.submit(new SubscriberEntity(isReliable, isStatic, isKeyed, waitForUnmatching, useWLP, nEntities));
            Future<Boolean> subscriber = es.submit(new SubscriberEntity(barrier, subCt1, null, false, false, false, true, false, 2));
            subCt1.await();

            Future<Boolean> publisher1 = es.submit(new PublisherEntity(barrier, null, null, false, false, false, false, false, 1));
            Future<Boolean> publisher2 = es.submit(new PublisherEntity(barrier, null, null, false, false, false, false, false, 1));

            barrier.await(30000, TimeUnit.MILLISECONDS);

            assertTrue(subscriber.get());
            assertTrue(publisher1.get());
            assertTrue(publisher2.get());

        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            assertTrue(false);
        } catch (BrokenBarrierException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            assertTrue(false);
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            assertTrue(false);
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            assertTrue(false);
        }

    }*/


}

