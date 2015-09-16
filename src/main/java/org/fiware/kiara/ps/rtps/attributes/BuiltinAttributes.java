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
 * Class BuiltinAttributes, to define the behavior of the RTPSParticipant
 * builtin protocols.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class BuiltinAttributes {

    /**
     * If set to false, NO discovery whatsoever would be used. Publisher and
     * Subscriber defined with the same topic name would NOT be linked. All
     * matching must be done manually through the addReaderLocator,
     * addReaderProxy, addWriterProxy methods.
     */
    public boolean useSimplePDP; // SimpleParticipantDiscoveryProtocol

    /**
     * Indicates to use the WriterLiveliness protocol.
     */
    public boolean useWriterLP; // WriterLivelinessProtocol

    /**
     * If set to true, SimpleEDP would be used.
     */
    public boolean useSimpleEDP; // SimpleEndpointDiscoveryProtocol

    /**
     * If set to true, StaticEDP based on an XML file would be implemented. The
     * XML filename must be provided.
     */
    public boolean useStaticEDP; // StaticEndpointDiscoveryProtocol

    /**
     * DomainId to be used by the RTPSParticipant (80 by default).
     */
    public int domainID;

    /**
     * Lease Duration of the RTPSParticipant, indicating how much time remote
     * RTPSParticipants should consider this RTPSParticipant alive.
     */
    public Timestamp leaseDuration;

    /**
     * The period for the RTPSParticipant to send its Discovery Message to all
     * other discovered RTPSParticipants as well as to all Multicast ports.
     */
    public Timestamp leaseDurationAnnouncementPeriod;

    /**
     * Attributes of the SimpleEDP protocol
     */
    public SimpleEDPAttributes simpleEDP;

    /**
     * Metatraffic Unicast Locator List
     */
    public LocatorList metatrafficUnicastLocatorList;

    /**
     * Metatraffic Multicast Locator List.
     */
    public LocatorList metatrafficMulticastLocatorList;

    /**
     * StaticEDP XML filename, only necessary if
     * use_STATIC_EndpointDiscoveryProtocol=true
     */
    private String m_staticEndpointXMLFilename;

    /**
     * StaticEDP XML configuration text, only necessary if
     * use_STATIC_EndpointDiscoveryProtocol=true, only used for tests
     */
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

    /**
     * Get the static endpoint XML filename
     *
     * @return Static endpoint XML filename
     */
    public String getStaticEndpointXMLFilename() {
        return this.m_staticEndpointXMLFilename;
    }

    /**
     * Set the static endpoint XML filename
     *
     * @param filename Static endpoint XML filename
     */
    public void setStaticEndpointXMLFilename(String filename) {
        this.m_staticEndpointXMLFilename = filename;
    }

    /**
     * Get the static endpoint XML configuration text
     *
     * @return Static endpoint XML configuration texxt
     */
    public String getStaticEndpointXML() {
        return this.m_staticEndpointXML;
    }

    /**
     * Set the static endpoint XML configuration text
     *
     * @param edpXml Static endpoint XML configuration text
     */
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
