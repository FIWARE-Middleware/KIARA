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

import org.fiware.kiara.ps.rtps.common.LocatorList;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;

public class BuiltinAttributes {
    
    public boolean useSimplePDP; // SimpleParticipantDiscoveryProtocol
    
    public boolean useWriterLP; // WriterLivelinessProtocol
    
    public boolean useSimpleEDP; // SimpleEndpointDiscoveryProtocol
    
    public boolean useStaticEDP; // StaticEndpointDiscoveryProtocol
    
    public int domainID;
    
    public Timestamp leaseDuration;
    
    public Timestamp leaseDurationAnnouncementPeriod;
    
    public SimpleEDPAttributes simpleEDP;
    
    public LocatorList metatrafficUnicastLocatorList;
    
    public LocatorList metatrafficMulticastLocatorList;
    
    private String m_staticEndpointXMLFilename;
    
    public BuiltinAttributes() {
        this.useSimplePDP = true;
        this.useWriterLP = true;
        this.useSimpleEDP = false;
        this.m_staticEndpointXMLFilename = "";
        this.domainID = 0;
        this.leaseDuration = new Timestamp(500, 0);
        this.leaseDurationAnnouncementPeriod = new Timestamp(250, 0);
        this.simpleEDP = new SimpleEDPAttributes();
        this.useWriterLP = true;
    }
    
    public String getStaticEndpointXMLFilename() {
        return this.m_staticEndpointXMLFilename;
    }
    
    public void setStaticEndpointXMLFilename(String filename) {
        this.m_staticEndpointXMLFilename = filename;
    }

}
