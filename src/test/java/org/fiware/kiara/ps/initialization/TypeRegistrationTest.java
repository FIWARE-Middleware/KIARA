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

import java.util.concurrent.ExecutionException;
import org.fiware.kiara.ps.Domain;
import org.fiware.kiara.ps.attributes.ParticipantAttributes;
import org.fiware.kiara.ps.initialization.types.Initialization;
import org.fiware.kiara.ps.initialization.types.InitializationType;
import org.fiware.kiara.ps.participant.Participant;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public class TypeRegistrationTest {

   static {
       System.setProperty("java.util.logging.config.file", "logging.properties");
   }

   public TypeRegistrationTest() {
   }

   @BeforeClass
   public static void setUpClass() {
   }

   @AfterClass
   public static void tearDownClass() {
   }

  @Before
   public void setUp() {
       Domain.stopAll();
   }

   @After
   public void tearDown() {
   }

   @Test
   public void minSizeError() throws InterruptedException, ExecutionException {

       InitializationType hwtype = new InitializationType();
       hwtype.setTypeSizeToZero();
       
       Initialization hw = hwtype.createData();

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
       
       boolean registered = Domain.registerType(participant, hwtype);
       assertFalse(registered);

   }
   
   @Test
   public void maxSizeError() throws InterruptedException, ExecutionException {

       InitializationType hwtype = new InitializationType();
       hwtype.setTypeSizeToMax();
       
       Initialization hw = hwtype.createData();

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
       
       boolean registered = Domain.registerType(participant, hwtype);
       assertFalse(registered);

   }
   
   @Test
   public void nameError() throws InterruptedException, ExecutionException {

       InitializationType hwtype = new InitializationType();
       hwtype.setName("");
       
       Initialization hw = hwtype.createData();

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
       
       boolean registered = Domain.registerType(participant, hwtype);
       assertFalse(registered);

   }
   
   @Test
   public void doubleRegistrationError() throws InterruptedException, ExecutionException {

       InitializationType hwtype = new InitializationType();
       
       Initialization hw = hwtype.createData();

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
       
       boolean registered = false;
       registered = Domain.registerType(participant, hwtype);
       assertTrue(registered);
       registered = Domain.registerType(participant, hwtype);
       assertFalse(registered);
   
   }
   
   @Test
   public void typeRegistration() throws InterruptedException, ExecutionException {

       InitializationType hwtype = new InitializationType();
       
       Initialization hw = hwtype.createData();

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
       
       boolean registered = false;
       registered = Domain.registerType(participant, hwtype);
       assertTrue(registered);
   
   }
   
   @Test
   public void multipleTypeRegistration() throws InterruptedException, ExecutionException {

       InitializationType hwtype = new InitializationType();
       InitializationType hwtype2 = new InitializationType();
       hwtype2.setName("NewName");
       
       Initialization hw = hwtype.createData();

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
       
       boolean registered = false;
       registered = Domain.registerType(participant, hwtype);
       assertTrue(registered);
       registered = Domain.registerType(participant, hwtype2);
       assertTrue(registered);
   
   }
   

}
