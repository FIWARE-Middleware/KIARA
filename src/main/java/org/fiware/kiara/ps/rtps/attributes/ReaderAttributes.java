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

import org.fiware.kiara.ps.rtps.common.DurabilityKind;
import org.fiware.kiara.ps.rtps.common.EndpointKind;
import org.fiware.kiara.ps.rtps.common.ReliabilityKind;

/**
 * Class ReaderAttributes, to define the attributes of a RTPSReader.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class ReaderAttributes {

    /**
     * Attributes of the associated endpoint.
     */
    public EndpointAttributes endpointAtt;
    /**
     * Times associated with this reader.
     */
    public ReaderTimes times;
    /**
     * Indicates if the reader expects Inline qos, default value 0.
     */
    public boolean expectsInlineQos;

    public ReaderAttributes() {
        this.endpointAtt = new EndpointAttributes();
        this.times = new ReaderTimes();
        this.endpointAtt.endpointKind = EndpointKind.READER;
        this.endpointAtt.durabilityKind = DurabilityKind.VOLATILE;
        this.endpointAtt.reliabilityKind = ReliabilityKind.BEST_EFFORT;
        this.expectsInlineQos = false;
    }

}
