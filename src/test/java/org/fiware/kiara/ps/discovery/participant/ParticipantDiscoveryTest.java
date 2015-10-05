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
package org.fiware.kiara.ps.discovery.participant;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.fiware.kiara.ps.Domain;
import org.fiware.kiara.ps.attributes.ParticipantAttributes;
import org.fiware.kiara.ps.participant.Participant;
import org.fiware.kiara.ps.participant.ParticipantDiscoveryInfo;
import org.fiware.kiara.ps.participant.ParticipantListener;
import org.fiware.kiara.ps.rtps.utils.IPFinder;
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
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class ParticipantDiscoveryTest {


    static {
        System.setProperty("java.util.logging.config.file", "logging.properties");
    }

    public ParticipantDiscoveryTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    private static final HelloWorldType hwtype = new HelloWorldType();

    private static class SingleDiscoveryParticipant implements Callable<Boolean> {

        private final String participantName;
        private final String remoteParticipantName;
        private final CountDownLatch initSignal;
        /*private final CountDownLatch startSignal;
        private final CountDownLatch doneSignal;*/

        public SingleDiscoveryParticipant(String participantName, String remoteParticipantName, CountDownLatch initSignal) {
            this.participantName = participantName;
            this.remoteParticipantName = remoteParticipantName;
            this.initSignal = initSignal;
            /*this.startSignal = startSignal;
            this.doneSignal = doneSignal;*/
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
                    + "        <name>" + this.remoteParticipantName + "</name>"
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

            pParam.rtps.setName(this.participantName);

            final CountDownLatch workInitSignal = new CountDownLatch(1);
            final String name = this.participantName;

            Participant participant = Domain.createParticipant(pParam, new ParticipantListener() {

                private int nMatched = 0;

                @Override
                public void onParticipantDiscovery(Participant p, ParticipantDiscoveryInfo info) {
                    workInitSignal.countDown();
                    this.nMatched++;
                    System.out.println(name + ": Discovery message received. Total: " + this.nMatched);
                }
            });

            if (participant == null) {
                System.out.println("Error when creating participant");
                return false;
            }

            System.out.println("Subscriber participant SPDP MC Port: " + participant.getSPDPMulticastPort());
            System.out.println("Subscriber participant SPDP UC Port: " + participant.getSPDPUnicastPort());
            System.out.println("Subscriber participant User MC Port: " + participant.getUserMulticastPort());
            System.out.println("Subscriber participant User UC Port: " + participant.getUserUnicastPort());

            this.initSignal.countDown();

            try {
                workInitSignal.await(15000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            assertEquals(0, workInitSignal.getCount());

            System.out.println("SingleDiscoveryParticipant finished");

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
    public void singleParticipantDiscoveryTest() throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newCachedThreadPool();
        CountDownLatch initSignal = new CountDownLatch(1);

        Future<Boolean> participant1 = es.submit(new SingleDiscoveryParticipant("Participant1", "Participant2", initSignal));
        Future<Boolean> participant2 = es.submit(new SingleDiscoveryParticipant("Participant2", "Participant1", initSignal));

        assertTrue(participant1.get());
        assertTrue(participant2.get());

    }
}


