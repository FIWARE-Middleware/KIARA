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

import org.fiware.kiara.ps.rtps.common.EndpointKind;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;

/**
*
* Class RemoteWriterAttributes, to define the attributes of a Remote Writer.
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public class RemoteWriterAttributes {

    /**
     * Attributes of the associated endpoint.
     */
    public EndpointAttributes endpoint;

    /**
     * {@link GUID} of the writer, can be unknown if the reader is best effort.
     */
    public GUID guid;

    /**
     * Liveliness lease duration, default value is infinite time.
     */
    public Timestamp livelinessLeaseDuration;

    /**
     * Ownership Strength of the associated writer.
     */
    public short ownershipStrength;

    public RemoteWriterAttributes() {
        this.endpoint = new EndpointAttributes();
        this.guid = new GUID();

        endpoint.endpointKind = EndpointKind.WRITER;
        livelinessLeaseDuration = new Timestamp().timeInfinite();
        this.ownershipStrength = 0;
    }

    /**
     * Set the RemoteWriter GUID. 
     *
     * @param other GUID to be set.
     */
    public void setGUID(GUID other) {
        this.guid.copy(other);
    }

    /**
     * Copies the content of the RemoteWriterAttributes reference
     * 
     * @param value A reference to other RemoteWriterAttributes
     */
    public void copy(RemoteWriterAttributes value) {
        endpoint.copy(value.endpoint);
        guid.copy(value.guid);
        livelinessLeaseDuration.copy(value.livelinessLeaseDuration);
        ownershipStrength = value.ownershipStrength;
    }

}
