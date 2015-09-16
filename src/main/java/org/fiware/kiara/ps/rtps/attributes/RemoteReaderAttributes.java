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

/**
 * Class RemoteReaderAttributes, to define the attributes of a Remote Reader.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class RemoteReaderAttributes {

    /**
     * Attributes of the associated endpoint.
     */
    public EndpointAttributes endpoint;

    /**
     * GUID_t of the reader.
     */
    public GUID guid;

    /**
     * Expects inline QOS.
     */
    public boolean expectsInlineQos;

    public RemoteReaderAttributes() {
        this.endpoint = new EndpointAttributes();
        this.guid = new GUID();
        this.endpoint.endpointKind = EndpointKind.READER;
        this.expectsInlineQos = false;
    }

    public void setGUID(GUID other) {
        this.guid.copy(other);
    }

    public void copy(RemoteReaderAttributes value) {
        endpoint.copy(value.endpoint);
        guid.copy(value.guid);
        expectsInlineQos = value.expectsInlineQos;
    }

}
