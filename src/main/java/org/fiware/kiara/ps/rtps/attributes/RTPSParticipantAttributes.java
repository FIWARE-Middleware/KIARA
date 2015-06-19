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
package org.fiware.kiara.ps.rtps.attributes;

import java.util.List;

import org.fiware.kiara.ps.rtps.common.LocatorList;

public class RTPSParticipantAttributes {
    
    public LocatorList defaultUnicastLocatorList;
    
    public LocatorList defaultMulticastLocatorList;
    
    public int defaultSendPort;
    
    public int sendSocketBufferSize;
    
    public int listenSocketBufferSize;
    
    public BuiltinAttributes builtinAtt;
    
    public PortParameters portParameters;
    
    public List<Byte> userData;
    
    public int participantID;
    
    public boolean useIPv4ToSend;
    
    public boolean useIPv6ToSend;
    
    private String m_name;
    
    public RTPSParticipantAttributes() {
        this.defaultUnicastLocatorList = new LocatorList();
        this.defaultMulticastLocatorList = new LocatorList();
        this.defaultSendPort = 10040;
        this.setName("RTPSParticipant");
        this.sendSocketBufferSize = 65536;
        this.listenSocketBufferSize = 65536;
        this.builtinAtt = new BuiltinAttributes();
        this.useIPv4ToSend = true;
        this.useIPv6ToSend = false;
        this.participantID = -1;
        this.portParameters = new PortParameters();
    }
    
    public void setName(String name) {
        this.m_name = name;
    }
    
    public String getName() {
        return this.m_name;
    }

}
