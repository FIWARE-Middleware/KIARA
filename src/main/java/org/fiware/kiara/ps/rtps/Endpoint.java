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
package org.fiware.kiara.ps.rtps;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.fiware.kiara.ps.rtps.attributes.EndpointAttributes;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;

/**
 * Class Endpoint, all entities of the RTPS network are a specification of this
 * class. Although the RTPSParticipant is also defined as an endpoint in the
 * RTPS specification in this implementation the RTPSParticipant class DOESN'T
 * inherit from this class. The elements needed where added directly to the
 * RTPSParticipant class. This way each instance of our class (Endpoint) has a
 * reference to the RTPSParticipant they belong to.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class Endpoint {

    /**
     * Reference to the RTPSParticipant containing this endpoints
     */
    protected RTPSParticipant m_participant;

    /**
     * Guid of the Endpoint
     */
    protected GUID m_guid;

    /**
     * Endpoint Attributes
     */
    protected EndpointAttributes m_att;

    /**
     * Mutex of the object
     */
    protected final Lock m_mutex = new ReentrantLock(true);

    public Endpoint(RTPSParticipant participant, GUID guid, EndpointAttributes att) {
        this.m_participant = participant;
        this.m_guid = guid;
        this.m_att = att;
    }

    /**
     * Get associated attributes
     *
     * @return Endpoint attributes
     */
    public EndpointAttributes getAttributes() {
        return this.m_att;
    }

    /**
     * Get associated GUID
     *
     * @return Associated GUID
     */
    public GUID getGuid() {
        return this.m_guid;
    }

    /**
     * Get associated RTPS participant
     *
     * @return RTPS participant
     * @see RTPSParticipant
     */
    public RTPSParticipant getRTPSParticipant() {
        return this.m_participant;
    }

    /**
     * Get mutex
     *
     * @return Associated Mutex
     */
    public Lock getMutex() {
        return m_mutex;
    }

}
