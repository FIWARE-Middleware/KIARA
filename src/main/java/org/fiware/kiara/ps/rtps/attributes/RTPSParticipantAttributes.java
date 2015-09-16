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

/**
 * Class RTPSParticipantAttributes used to define different aspects of a
 * RTPSParticipant.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class RTPSParticipantAttributes {

    /**
     * Default list of Unicast Locators to be used for any Endpoint defined
     * inside this RTPSParticipant in the case that it was defined with NO
     * UnicastLocators. At least ONE locator should be included in this list.
     */
    public LocatorList defaultUnicastLocatorList;

    /**
     * Default list of Multicast Locators to be used for any Endpoint defined
     * inside this RTPSParticipant in the case that it was defined with NO
     * UnicastLocators. This is usually left empty.
     */
    public LocatorList defaultMulticastLocatorList;

    /**
     * Default send port that all Endpoints in the RTPSParticipant would use to
     * send messages, default value 10040. In this release all Endpoints use the
     * same resource (socket) to send messages.
     */
    public int defaultSendPort;

    /**
     * Send socket buffer size for the send resource, default value 65536.
     */
    public int sendSocketBufferSize;

    /**
     * Listen socket buffer for all listen resources, default value 65536.
     */
    public int listenSocketBufferSize;

    /**
     * Builtin parameters.
     */
    public BuiltinAttributes builtinAtt;

    /**
     * Port Parameters
     */
    public PortParameters portParameters;

    /**
     * User Data of the participant
     */
    public List<Byte> userData;

    /**
     * Participant ID
     */
    public int participantID;

    /**
     * Use IP4 to send messages.
     */
    public boolean useIPv4ToSend;

    /**
     * Use IP6 to send messages.
     */
    public boolean useIPv6ToSend;

    /**
     * Name of the participant.
     */
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

    public void resetParticipantID() {
        this.participantID = -1;
    }

    /**
     * Set the name of the participant.
     *
     * @param name name of the participant
     */
    public void setName(String name) {
        this.m_name = name;
    }

    /**
     * Get the name of the participant.
     *
     * @return name of the participant
     */
    public String getName() {
        return this.m_name;
    }

}
