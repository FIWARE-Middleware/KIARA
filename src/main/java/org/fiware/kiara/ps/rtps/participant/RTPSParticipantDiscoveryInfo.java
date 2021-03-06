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
package org.fiware.kiara.ps.rtps.participant;

import java.util.List;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.util.Pair;

/**
* Discovery information in a Participant discovery
* 
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public class RTPSParticipantDiscoveryInfo {
  
    /**
     * Status of the discovery
     */
    public DiscoveryStatus status;
    
    /**
     * Discovered {@link GUID}
     */
    public GUID guid;
    
    /**
     * List of properties
     */
    public List<Pair<String, String>> propertyList;
    
    /**
     * User's data
     */
    public List<Byte> userData;
    
    /**
     * Name of the {@link RTPSParticipant}
     */
    public String RTPSParticipantName;
    

}
