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

/**
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
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

    private String m_staticEndpointXML;

    public BuiltinAttributes() {
        this.useSimplePDP = true;
        this.useWriterLP = true;
        this.useSimpleEDP = false;
        this.m_staticEndpointXMLFilename = "";
        this.m_staticEndpointXML = "";
        this.domainID = 0;
        this.leaseDuration = new Timestamp(500, 0);
        this.leaseDurationAnnouncementPeriod = new Timestamp(10, 0);
        this.simpleEDP = new SimpleEDPAttributes();
        this.useWriterLP = true;
        this.metatrafficMulticastLocatorList = new LocatorList();
        this.metatrafficUnicastLocatorList = new LocatorList();
    }

    public String getStaticEndpointXMLFilename() {
        return this.m_staticEndpointXMLFilename;
    }

    public void setStaticEndpointXMLFilename(String filename) {
        this.m_staticEndpointXMLFilename = filename;
    }

    public String getStaticEndpointXML() {
        return this.m_staticEndpointXML;
    }

    public void setStaticEndpointXML(String edpXml) {
        this.m_staticEndpointXML = edpXml;
    }

    public void copy(BuiltinAttributes other) {
        useSimplePDP = other.useSimplePDP;
        useWriterLP = other.useWriterLP;
        useSimpleEDP = other.useSimpleEDP;
        useStaticEDP = other.useStaticEDP;
        domainID = other.domainID;
        leaseDuration.copy(other.leaseDuration);
        leaseDurationAnnouncementPeriod.copy(other.leaseDurationAnnouncementPeriod);
        simpleEDP.copy(other.simpleEDP);
        metatrafficUnicastLocatorList.copy(metatrafficUnicastLocatorList);
        metatrafficMulticastLocatorList.copy(metatrafficMulticastLocatorList);
        m_staticEndpointXMLFilename = other.m_staticEndpointXMLFilename;
    }

}
