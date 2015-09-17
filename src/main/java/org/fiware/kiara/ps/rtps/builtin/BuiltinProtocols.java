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
package org.fiware.kiara.ps.rtps.builtin;

import org.fiware.kiara.ps.attributes.TopicAttributes;
import org.fiware.kiara.ps.qos.ReaderQos;
import org.fiware.kiara.ps.qos.WriterQos;
import org.fiware.kiara.ps.rtps.attributes.BuiltinAttributes;
import org.fiware.kiara.ps.rtps.builtin.discovery.participant.PDPSimple;
import org.fiware.kiara.ps.rtps.builtin.liveliness.WLP;
import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.common.LocatorKind;
import org.fiware.kiara.ps.rtps.common.LocatorList;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.reader.RTPSReader;
import org.fiware.kiara.ps.rtps.writer.RTPSWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class BuiltinProtocols that contains builtin endpoints implementing the
 * discovery and liveliness protocols.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class BuiltinProtocols {

    /**
     * BuiltinAttributes of the builtin protocols.
     */
    private BuiltinAttributes m_att;

    /**
     * Reference to the RTPSParticipant
     */
    private RTPSParticipant m_participant;

    /**
     * Reference to the PDPSimple.
     */
    private PDPSimple m_PDP;

    /**
     * Reference to the WLP
     */
    private WLP m_WLP;

    /**
     * Port number for the multicast locators.
     */
    private int m_SPDPWellKnownMulticastPort;

    /**
     * Port number for the unicast locators.
     */
    private int m_SPDPWellKnownUnicastPort;

    /**
     * Locator list for metatraffic
     */
    private final LocatorList m_metatrafficMulticastLocatorList;

    /**
     * Locator List for metatraffic unicast
     */
    private LocatorList m_metatrafficUnicastLocatorList;

    /**
     * Mandatory Multicast Locator (239.255.0.1)
     */
    private Locator m_mandatoryMulticastLocator;

    /**
     * Use the mandatory locator.
     */
    private boolean m_useMandatory;

    private static final Logger logger = LoggerFactory.getLogger(BuiltinProtocols.class);

    /**
     * Main Constructor
     */
    public BuiltinProtocols() {
        this.m_SPDPWellKnownMulticastPort = 7400;
        this.m_SPDPWellKnownUnicastPort = 7410;
        this.m_useMandatory = false;
        this.m_metatrafficMulticastLocatorList = new LocatorList();
        this.m_metatrafficUnicastLocatorList = new LocatorList();
    }

    public void destroy() {
        if (this.m_WLP != null) {
            this.m_WLP.destroy();
        }
        if (this.m_PDP != null) {
            this.m_PDP.destroy();
        }
    }

    /**
     * Initialize the builtin protocols.
     *
     * @param rtpsParticipant RTPS participant.
     * @param builtinAtt builtin attributes
     * @return True if correct.
     */
    public boolean initBuiltinProtocols(RTPSParticipant rtpsParticipant, BuiltinAttributes builtinAtt) {
        logger.debug("Starting builtin endpoints");
        this.m_participant = rtpsParticipant;
        this.m_att = builtinAtt;

        this.m_SPDPWellKnownMulticastPort = this.m_participant.getAttributes().portParameters.getMulticastPort(
                this.m_att.domainID);

        this.m_SPDPWellKnownUnicastPort = this.m_participant.getAttributes().portParameters.getUnicastPort(
                this.m_att.domainID,
                this.m_participant.getAttributes().participantID);

        this.m_mandatoryMulticastLocator = new Locator();
        this.m_mandatoryMulticastLocator.setKind(LocatorKind.LOCATOR_KIND_UDPv4);
        this.m_mandatoryMulticastLocator.setPort(this.m_SPDPWellKnownMulticastPort);
        this.m_mandatoryMulticastLocator.setIPv4Address("239.255.0.1");

        if (this.m_att.metatrafficMulticastLocatorList.isEmpty()) {
            this.m_metatrafficMulticastLocatorList.pushBack(this.m_mandatoryMulticastLocator);
        } else {
            this.m_useMandatory = false;
            for (Locator it : this.m_att.metatrafficMulticastLocatorList.getLocators()) {
                this.m_metatrafficMulticastLocatorList.pushBack(it);
            }
        }

        if (this.m_att.metatrafficUnicastLocatorList.isEmpty()) {
            Locator loc = new Locator();
            loc.setPort(this.m_SPDPWellKnownUnicastPort);
            loc.setKind(LocatorKind.LOCATOR_KIND_UDPv4);
            this.m_metatrafficUnicastLocatorList.pushBack(loc);
        } else {
            for (Locator it : this.m_att.metatrafficUnicastLocatorList.getLocators()) {
                this.m_metatrafficUnicastLocatorList.pushBack(it);
            }
        }

        if (this.m_att.useSimplePDP) {
            this.m_PDP = new PDPSimple(this);
            if (!this.m_PDP.initPDP(this.m_participant)) {
                return false;
            }
            /*if (this.m_att.useWriterLP) { // TODO Implement statefull readers and writers
             this.m_WLP = new WLP(this);
             this.m_WLP.initWL(this.m_participant);
             }*/
            this.m_PDP.announceParticipantState(true);
            this.m_PDP.resetParticipantAnnouncement();
        }

        logger.debug("Builtin protocols have been initialized");

        return true;
    }

    /**
     * Update the metatraffic locatorlist after it was created. Because when you
     * create the EDP readers you are not sure the selected endpoints can be
     * used.
     *
     * @param loclist LocatorList to update
     * @return True on success
     */
    public boolean updateMetatrafficLocators(LocatorList loclist) {
        this.m_metatrafficUnicastLocatorList = loclist;
        return true;
    }

    /**
     * Add a local Writer to the BuiltinProtocols.
     *
     * @param writer Pointer to the RTPSWriter.
     * @param topicAtt topic attributes
     * @param wqos writer QoS
     * @return True if correct.
     */
    public boolean addLocalWriter(RTPSWriter writer, TopicAttributes topicAtt, WriterQos wqos) {
        boolean ok = false;

        if (this.m_PDP != null) {
            ok |= this.m_PDP.getEDP().newLocalWriterProxyData(writer, topicAtt, wqos);
        } else {
            logger.warn("EDP is not used in this Participant, register a Writer is impossible");
        }

        if (this.m_WLP != null) {
            ok |= this.m_WLP.addLocalWriter(writer, wqos);
        } else {
            logger.warn("LIVELINESS is not used in this Participant, register a Writer is impossible");
        }

        return ok;
    }

    /**
     * Add a local Reader to the BuiltinProtocols.
     *
     * @param reader Pointer to the RTPSReader.
     * @param topicAtt topic attributes
     * @param rqos reader QoS
     * @return True if correct.
     */
    public boolean addLocalReader(RTPSReader reader, TopicAttributes topicAtt, ReaderQos rqos) {
        boolean ok = false;

        if (this.m_PDP != null) {
            ok |= this.m_PDP.getEDP().newLocalReaderProxyData(reader, topicAtt, rqos);
        } else {
            logger.warn("EDP is not used in this Participant, register a Reader is impossible");
        }

        return ok;
    }

    /**
     * Update a local Writer QOS
     *
     * @param writer Writer to update
     * @param wqos New Writer QoS
     * @return true if operation was successful
     */
    public boolean updateLocalWriter(RTPSWriter writer, WriterQos wqos) {
        boolean ok = false;

        if (this.m_PDP != null && this.m_PDP.getEDP() != null) {
            ok |= this.m_PDP.getEDP().updatedLocalWriter(writer, wqos);
        }

        if (this.m_WLP != null) {
            ok |= this.m_WLP.updateLocalWriter(writer, wqos);
        }

        return ok;
    }

    /**
     * Update a local Reader QOS
     *
     * @param reader Reader to update
     * @param rqos New Reader QoS
     * @return true if operation was successful
     */
    public boolean updateLocalReader(RTPSReader reader, ReaderQos rqos) {
        boolean ok = false;

        if (this.m_PDP != null && this.m_PDP.getEDP() != null) {
            ok |= this.m_PDP.getEDP().updatedLocalReader(reader, rqos);
        }

        return ok;
    }

    /**
     * Remove a local Writer from the builtinProtocols.
     *
     * @param writer Pointer to the writer.
     * @return True if correctly removed.
     */
    public boolean removeLocalWriter(RTPSWriter writer) {
        boolean ok = false;

        logger.debug("Removing Writer {} from builtin protocols", writer.getGuid());

        if (this.m_WLP != null) {
            ok |= this.m_WLP.removeLocalWriter(writer);
        }

        if (this.m_PDP != null && this.m_PDP.getEDP() != null) {
            ok |= this.m_PDP.getEDP().removeLocalWriter(writer);
        }

        logger.debug("Writer {} successfully removed from builtin protocols", writer.getGuid());

        return ok;
    }

    /**
     * Remove a local Reader from the builtinProtocols.
     *
     * @param reader Pointer to the reader.
     * @return True if correctly removed.
     */
    public boolean removeLocalReader(RTPSReader reader) {
        boolean ok = false;

        logger.debug("Removing Writer {} from builtin protocol", reader.getGuid());

        if (this.m_PDP != null && this.m_PDP.getEDP() != null) {
            ok |= this.m_PDP.getEDP().removeLocalReader(reader);
        }

        logger.debug("Reader {} successfully removed from builtin protocols", reader.getGuid());

        return ok;
    }

    /**
     * Announce RTPSParticipantState (force the sending of a DPD message.)
     */
    public void announceRTPSParticipantState() {
        this.m_PDP.announceParticipantState(false);
    }

    /**
     * Stop the RTPSParticipant Announcement (used in tests to avoid multiple
     * packets being send)
     */
    public void stopRTPSParticipantAnnouncement() {
        this.m_PDP.stopParticipantAnnouncement();
    }

    /**
     * Reset to timer to make periodic RTPSParticipant Announcements.
     */
    public void resetRTPSParticipantAnnouncement() {
        this.m_PDP.resetParticipantAnnouncement();
    }

    /**
     * Get PDP object
     *
     * @return PDP object
     * @see PDPSimple
     */
    public PDPSimple getPDP() {
        return this.m_PDP;
    }

    /**
     * Set PDP object
     *
     * @param PDP object
     * @see PDPSimple
     */
    public void setPDP(PDPSimple PDP) {
        this.m_PDP = PDP;
    }

    /**
     * Get metatraffic multicast locator list
     *
     * @return metatraffic multicast locator list
     * @see LocatorList
     */
    public LocatorList getMetatrafficMulticastLocatorList() {
        return this.m_metatrafficMulticastLocatorList;
    }

    /**
     * Get metatraffic unicast locator list
     *
     * @return metatraffic unicast locator list
     * @see LocatorList
     */
    public LocatorList getMetatrafficUnicastLocatorList() {
        return this.m_metatrafficUnicastLocatorList;
    }

    /**
     * Get use mandatory flag
     *
     * @return use mandatory flag
     */
    public boolean getUseMandatory() {
        return this.m_useMandatory;
    }

    /**
     * Get mandatory multicast locator
     *
     * @return mandatory multicast locator
     * @see Locator
     */
    public Locator getMandatoryMulticastLocator() {
        return this.m_mandatoryMulticastLocator;
    }

    /**
     * Get WLP object
     *
     * @return WLP object
     */
    public WLP getWLP() {
        return this.m_WLP;
    }

    /**
     * Get SPDP unicast port
     *
     * @return SPDP unicast port
     */
    public int getSPDPUnicastPort() {
        return this.m_SPDPWellKnownUnicastPort;
    }

    /**
     * Get SPDP multicast port
     *
     * @return SPDP multicast port
     */
    public int getSPDPMulticastPort() {
        return this.m_SPDPWellKnownMulticastPort;
    }

}
