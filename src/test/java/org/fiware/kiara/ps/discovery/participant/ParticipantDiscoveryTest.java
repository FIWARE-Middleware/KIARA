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
import org.fiware.kiara.ps.rtps.participant.DiscoveryStatus;
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
    
    CountDownLatch participantCt;


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
    
    @Before
    public void prepare() throws InterruptedException {
        if (this.participantCt != null) {
            this.participantCt.await(10000, TimeUnit.MILLISECONDS);
        }
    }

    private static final HelloWorldType hwtype = new HelloWorldType();

    private static class SingleDiscoveryParticipant implements Callable<Boolean> {

        private final String participantName;
        private final String[] remoteParticipantNames;
        private final CountDownLatch myCt;
        private int participantID;
        private int totalDisc;

        public SingleDiscoveryParticipant(CountDownLatch myCt, int id, String participantName, String[] remoteParticipantNames, int totalDisc) {
            this.participantName = participantName;
            this.remoteParticipantNames = remoteParticipantNames;
            this.myCt = myCt;
            this.participantID = id;
            this.totalDisc = totalDisc;
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
            
            boolean staticDiscovery = false;

            if (this.remoteParticipantNames != null) { // Static
                pParam.rtps.builtinAtt.useSimpleEDP = false;
                pParam.rtps.builtinAtt.useStaticEDP = true;
                staticDiscovery = true;
            } else { // Dynamic
                pParam.rtps.builtinAtt.useSimpleEDP = true;
                pParam.rtps.builtinAtt.useStaticEDP = false;
            }
            
            if (staticDiscovery) {
                final String edpXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                        + "<staticdiscovery>";
                        
                String participants = "";
                for (int i=0; i < remoteParticipantNames.length; ++i) {
                    participants = participants + "+<participant><name>" + remoteParticipantNames[i] + "</name></participant>";
                }
                final String edpXmlEnd = "</staticdiscovery>";
                pParam.rtps.builtinAtt.setStaticEndpointXML(edpXml + participants + edpXmlEnd);
            }

            pParam.rtps.setName(this.participantName);
            //pParam.rtps.participantID = this.participantID;

            final CountDownLatch workInitSignal = new CountDownLatch(this.totalDisc);
            
            Participant participant = Domain.createParticipant(pParam, new ParticipantListener() {
                @Override
                public void onParticipantDiscovery(Participant p, ParticipantDiscoveryInfo info) {
                    if (info.rtps.status == DiscoveryStatus.DISCOVERED_PARTICIPANT) {
                        workInitSignal.countDown();
                        //System.out.println(participantName + " - DISCOVERED: " + info.rtps.guid);
                    }
                }
            });

            if (participant == null) {
                System.out.println("Error when creating participant");
                return false;
            }

            try {
                workInitSignal.await(15000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            //Domain.removeParticipant(participant);
            
            assertEquals(0, workInitSignal.getCount());

            //assertTrue(true);
            
            this.myCt.countDown();
            
            return true;
        }

    }
/*
    @Test
    public void staticSingleParticipantDiscoveryTest() throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newCachedThreadPool();
        participantCt = new CountDownLatch(2);

        Future<Boolean> participant1 = es.submit(new SingleDiscoveryParticipant(participantCt, 1, "Participant1", new String[] {"Participant2"}, 1));
        Future<Boolean> participant2 = es.submit(new SingleDiscoveryParticipant(participantCt, 2, "Participant2", new String[] {"Participant1"}, 1));

        assertTrue(participant1.get());
        assertTrue(participant2.get());

    }
    
    @Test
    public void dynamicSingleParticipantDiscoveryTest() throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newCachedThreadPool();
        CountDownLatch ct = new CountDownLatch(2);

        Future<Boolean> participant1 = es.submit(new SingleDiscoveryParticipant(ct, 3, "Participant3", null, 1));
        Future<Boolean> participant2 = es.submit(new SingleDiscoveryParticipant(ct, 4, "Participant4", null, 1));

        assertTrue(participant1.get());
        assertTrue(participant2.get());
        assertEquals(0, ct.getCount());

    }
    
    @Test
    public void staticMultipleParticipantDiscoveryTest() throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newCachedThreadPool();
        participantCt = new CountDownLatch(3);

        Future<Boolean> participant1 = es.submit(new SingleDiscoveryParticipant(participantCt, 5, "Participant5", new String[] {"Participant6", "Participant7"}, 2));
        Future<Boolean> participant2 = es.submit(new SingleDiscoveryParticipant(participantCt, 6, "Participant6", new String[] {"Participant5", "Participant7"}, 2));
        Future<Boolean> participant3 = es.submit(new SingleDiscoveryParticipant(participantCt, 7, "Participant7", new String[] {"Participant5", "Participant6"}, 2));

        assertTrue(participant1.get());
        assertTrue(participant2.get());
        assertTrue(participant3.get());

    }
    
    @Test
    public void dynamicMultipleParticipantDiscoveryTest() throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newCachedThreadPool();
        participantCt = new CountDownLatch(3);

        Future<Boolean> participant1 = es.submit(new SingleDiscoveryParticipant(participantCt, 8, "Participant8", null, 2));
        Future<Boolean> participant2 = es.submit(new SingleDiscoveryParticipant(participantCt, 9, "Participant9", null, 2));
        Future<Boolean> participant3 = es.submit(new SingleDiscoveryParticipant(participantCt, 10, "Participant10", null, 2));

        assertTrue(participant1.get());
        assertTrue(participant2.get());
        assertTrue(participant3.get());

    }
  */  
    
}


