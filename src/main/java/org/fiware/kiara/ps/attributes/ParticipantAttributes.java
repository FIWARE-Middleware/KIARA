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
package org.fiware.kiara.ps.attributes;

import org.fiware.kiara.ps.rtps.attributes.RTPSParticipantAttributes;

/**
 * Class ParticipantAttributes, used by the user to define the attributes of a
 * Participant. The Participants in the Publisher-Subscriber layer are only a
 * container to a RTPSParticipant, so their attributes are the same. Still to
 * maintain the equivalence this class is used to define them.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class ParticipantAttributes {

    /**
     * Attributes of the associated RTPSParticipant.
     */
    public RTPSParticipantAttributes rtps;

    /**
     * Main Constructor
     */
    public ParticipantAttributes() {
        this.rtps = new RTPSParticipantAttributes();
    }

}
