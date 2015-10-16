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
import org.fiware.kiara.ps.participant.Participant;
import org.fiware.kiara.ps.participant.ParticipantDiscoveryInfo;
import org.fiware.kiara.ps.participant.ParticipantListener;
import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.utils.IPFinder;
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
public class ParticipantInitializationTest {

    static {
        System.setProperty("java.util.logging.config.file", "logging.properties");
    }

    public ParticipantInitializationTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    private static final HelloWorldType hwtype = new HelloWorldType();

    @Before
    public void setUp() {
        //Domain.stopAll();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void errorLeaseDuration() throws InterruptedException, ExecutionException {

        HelloWorld hw = hwtype.createData();

        hw.setInnerLongAtt(10);
        hw.setInnerStringAtt("Hello World");

        // Create participant
        ParticipantAttributes pParam = new ParticipantAttributes();
        pParam.rtps.builtinAtt.useSimplePDP = true;
        pParam.rtps.builtinAtt.useWriterLP = false;
        pParam.rtps.builtinAtt.useSimpleEDP = true;
        pParam.rtps.builtinAtt.useStaticEDP = false;

        pParam.rtps.builtinAtt.leaseDuration.setMilliSecondsDouble(5000);

        Participant participant = Domain.createParticipant(pParam, null);
        
        assertNull(participant);

    }

    @Test
    public void errorNotIpv4orIPv6() throws InterruptedException, ExecutionException {

        HelloWorld hw = hwtype.createData();

        hw.setInnerLongAtt(10);
        hw.setInnerStringAtt("Hello World");

        // Create participant
        ParticipantAttributes pParam = new ParticipantAttributes();
        pParam.rtps.builtinAtt.useSimplePDP = true;
        pParam.rtps.builtinAtt.useWriterLP = false;
        pParam.rtps.builtinAtt.useSimpleEDP = true;
        pParam.rtps.builtinAtt.useStaticEDP = false;

        pParam.rtps.useIPv4ToSend = false;
        pParam.rtps.useIPv6ToSend = false;

        Participant participant = Domain.createParticipant(pParam, null);
        
        assertNull(participant);

    }

    @Test
    public void repeatedParticipantID() throws InterruptedException, ExecutionException {

        HelloWorld hw = hwtype.createData();

        hw.setInnerLongAtt(10);
        hw.setInnerStringAtt("Hello World");

        // Create participant
        ParticipantAttributes pParam = new ParticipantAttributes();
        pParam.rtps.builtinAtt.useSimplePDP = true;
        pParam.rtps.builtinAtt.useWriterLP = false;
        pParam.rtps.builtinAtt.useSimpleEDP = true;
        pParam.rtps.builtinAtt.useStaticEDP = false;

        pParam.rtps.participantID = 1;
        
        Domain.createParticipant(pParam, null);
        
        pParam.rtps.participantID = 1;

        Participant participant2 = Domain.createParticipant(pParam, null);

        assertNull(participant2);


    }

    @Test
    public void wrongDefaultUnicastLocatorList() throws InterruptedException, ExecutionException {

        HelloWorld hw = hwtype.createData();

        hw.setInnerLongAtt(10);
        hw.setInnerStringAtt("Hello World");

        // Create participant
        ParticipantAttributes pParam = new ParticipantAttributes();
        pParam.rtps.builtinAtt.useSimplePDP = true;
        pParam.rtps.builtinAtt.useWriterLP = false;
        pParam.rtps.builtinAtt.useSimpleEDP = true;
        pParam.rtps.builtinAtt.useStaticEDP = false;

        Locator loc = new Locator();
        loc.setPort(-1);
        pParam.rtps.defaultUnicastLocatorList.pushBack(loc);

        Participant participant = Domain.createParticipant(pParam, null);

        assertNull(participant);

    }

    @Test
    public void wrongDefaultMulticastLocatorList() throws InterruptedException, ExecutionException {

        HelloWorld hw = hwtype.createData();

        hw.setInnerLongAtt(10);
        hw.setInnerStringAtt("Hello World");

        // Create participant
        ParticipantAttributes pParam = new ParticipantAttributes();
        pParam.rtps.builtinAtt.useSimplePDP = true;
        pParam.rtps.builtinAtt.useWriterLP = false;
        pParam.rtps.builtinAtt.useSimpleEDP = true;
        pParam.rtps.builtinAtt.useStaticEDP = false;

        Locator loc = new Locator();
        loc.setPort(-1);
        pParam.rtps.defaultMulticastLocatorList.pushBack(loc);

        Participant participant = Domain.createParticipant(pParam, null);
        
        assertNull(participant);

    }
    
    @Test
    public void notSimpleEDPorStaticEDPPort() throws InterruptedException, ExecutionException {

        HelloWorld hw = hwtype.createData();

        hw.setInnerLongAtt(10);
        hw.setInnerStringAtt("Hello World");

        // Create participant
        ParticipantAttributes pParam = new ParticipantAttributes();
        pParam.rtps.builtinAtt.useSimplePDP = true;
        pParam.rtps.builtinAtt.useWriterLP = false;
        pParam.rtps.builtinAtt.useSimpleEDP = false;
        pParam.rtps.builtinAtt.useStaticEDP = false;

        Participant participant = Domain.createParticipant(pParam, null);
        
        assertNull(participant);
    
    }
    
    @Test
    public void simpleEDPnotWLP() throws InterruptedException, ExecutionException {

        HelloWorld hw = hwtype.createData();

        hw.setInnerLongAtt(10);
        hw.setInnerStringAtt("Hello World");

        // Create participant
        ParticipantAttributes pParam = new ParticipantAttributes();
        pParam.rtps.builtinAtt.useSimplePDP = true;
        pParam.rtps.builtinAtt.useWriterLP = false;
        pParam.rtps.builtinAtt.useSimpleEDP = true;
        pParam.rtps.builtinAtt.useStaticEDP = false;

        Participant participant = Domain.createParticipant(pParam, null);
        
        assertNotNull(participant);
        
        //Domain.removeParticipant(participant);

    }
    
    @Test
    public void staticEDPFileNotFoundnotWLP() throws InterruptedException, ExecutionException {

        HelloWorld hw = hwtype.createData();

        hw.setInnerLongAtt(10);
        hw.setInnerStringAtt("Hello World");

        // Create participant
        ParticipantAttributes pParam = new ParticipantAttributes();
        pParam.rtps.builtinAtt.useSimplePDP = true;
        pParam.rtps.builtinAtt.useWriterLP = false;
        pParam.rtps.builtinAtt.useSimpleEDP = false;
        pParam.rtps.builtinAtt.useStaticEDP = true;

        Participant participant = Domain.createParticipant(pParam, null);
        
        assertNull(participant);

    }
    
    @Test
    public void staticEDPnotWLP() throws InterruptedException, ExecutionException {

        HelloWorld hw = hwtype.createData();

        hw.setInnerLongAtt(10);
        hw.setInnerStringAtt("Hello World");

        // Create participant
        ParticipantAttributes pParam = new ParticipantAttributes();
        pParam.rtps.builtinAtt.useSimplePDP = true;
        pParam.rtps.builtinAtt.useWriterLP = false;
        pParam.rtps.builtinAtt.useSimpleEDP = false;
        pParam.rtps.builtinAtt.useStaticEDP = true;
        
        final String edpXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<staticdiscovery>"
                + "    <participant>"
                + "        <name>" + "ParticipantName" + "</name>"
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

        Participant participant = Domain.createParticipant(pParam, null);
        
        assertNotNull(participant);
        
        //Domain.removeParticipant(participant);

    }
    
 

}
