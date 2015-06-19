/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2015 Proyectos y Sistemas de Mantenimiento S.L. (eProsima)
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
package org.fiware.kiara.ps.rtps.messages;

import java.util.List;

import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.common.LocatorList;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.messages.common.types.RTPSEndian;
import org.fiware.kiara.ps.rtps.messages.elements.EntityId;
import org.fiware.kiara.ps.rtps.messages.elements.ParameterList;
import org.fiware.kiara.ps.rtps.writer.RTPSWriter;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public class RTPSMessageGroup {
    
    // TODO Implement

    public static void sendChangesAsData(RTPSWriter rtpsWriter, List<CacheChange> changes, LocatorList unicastLocatorList, LocatorList multicastLocatorList, boolean expectsInlineQos, EntityId entityId) {
        
        RTPSMessage msg = RTPSMessageBuilder.createMessage(RTPSEndian.LITTLE_ENDIAN);
        RTPSMessageBuilder.addHeader(msg, rtpsWriter.getGuid().getGUIDPrefix());
        boolean added = false;
        
        for (CacheChange cit : changes) {
            
            RTPSMessageBuilder.addSubmessageInfoTSNow(msg, false);
            
            RTPSMessageGroup.prepareSubmessageData(msg, rtpsWriter, cit, expectsInlineQos, entityId);
            
            added = true;
             
        }
        
        if (added) {
            for (Locator unicastLoc : unicastLocatorList.getLocators()) {
                rtpsWriter.getRTPSParticipant().sendSync(msg, unicastLoc);
            }
            for (Locator multicastLoc : multicastLocatorList.getLocators()) {
                rtpsWriter.getRTPSParticipant().sendSync(msg, multicastLoc);
            }
        }
        
        
    }
    
    public static void prepareSubmessageData(RTPSMessage msg, RTPSWriter rtpsWriter, CacheChange change, boolean expectsInlineQos, EntityId entityId) {
        ParameterList inlineQos = null;
        if (expectsInlineQos) {
            // TODO Prepare inline QOS (Not supported yet)
        }
        
        RTPSMessageBuilder.addSubmessageData(msg, change, rtpsWriter.getAttributes().topicKind, entityId, expectsInlineQos, inlineQos); 
        
    }
    
    public static void sendChangesAsData(RTPSWriter rtpsWriter, List<CacheChange> changes, Locator locator, boolean expectsInlineQos, EntityId entityId) {
        //short dataMsgSize = 0;
        //short changeN = 1;
        
        RTPSMessage msg = RTPSMessageBuilder.createMessage(RTPSEndian.LITTLE_ENDIAN);
        RTPSMessageBuilder.addHeader(msg, rtpsWriter.getGuid().getGUIDPrefix());
        boolean added = false;
        
        for (CacheChange cit : changes) {
            
            RTPSMessageBuilder.addSubmessageInfoTSNow(msg, false);
            
            RTPSMessageGroup.prepareSubmessageData(msg, rtpsWriter, cit, expectsInlineQos, entityId);
            
            added = true;
             
        }
        
        if (added) {
            rtpsWriter.getRTPSParticipant().sendSync(msg, locator);
        }
        
    }

}
