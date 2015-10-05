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

/**
 * Class PortParameters, to define the port parameters and gains related with
 * the RTPS protocol.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class PortParameters {

    /**
     * PortBase, default value 7400.
     */
    public short portBase;

    /**
     * DomainID gain, default value 250.
     */
    public short domainIDGain;

    /**
     * ParticipantID gain, default value 2.
     */
    public short participantIDGain;

    /**
     * Offset d0, default value 0.
     */
    public short offsetd0;
    /**
     * Offset d1, default value 10.
     */
    public short offsetd1;
    /**
     * Offset d2, default value 1.
     */
    public short offsetd2;
    /**
     * Offset d3, default value 11.
     */
    public short offsetd3;

    public PortParameters() {
        this.portBase = 7400;
        this.participantIDGain = 2;
        this.domainIDGain = 250;
        this.offsetd0 = 0;
        this.offsetd1 = 10;
        this.offsetd2 = 1;
        this.offsetd3 = 11;
    }

    /**
     * Get a multicast port based on the domain ID.
     *
     * @param domainID Domain ID.
     * @return Multicast port
     */
    public int getMulticastPort(int domainID) {
        return portBase + (domainIDGain * domainID) + offsetd0;
    }

    /**
     * Get a unicast port base on the domain ID and the participant ID.
     *
     * @param domainID Domain ID.
     * @param participantID Participant ID.
     * @return Unicast port
     */
    public int getUnicastPort(int domainID, int participantID) {
        return portBase + (domainIDGain * domainID) + offsetd1 + (participantIDGain * participantID);
    }

    /**
     * Get user multicast port.
     *
     * @param domainID Domain ID.
     * @return User multicast port
     */
    public int getUserMulticastPort(int domainID) {
        return this.portBase + this.domainIDGain * domainID + this.offsetd2;
    }

    /**
     * Get user unicast port
     *
     * @param domainID Domain ID
     * @param participantID Participant ID
     * @return user unicast port
     */
    public int getUserUnicastPort(int domainID, int participantID) {
        return this.portBase + this.domainIDGain * domainID + this.offsetd3 + this.participantIDGain * participantID;
    }

}
