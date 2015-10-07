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
 * Class HistoryCacheAttributes, to specify the attributes of a WriterHistory or
 * a ReaderHistory. This class is only intended to be used with the RTPS API.
 * The Publsiher-Subscriber API has other fields to define this values
 * (HistoryQosPolicy and ResourceLimitsQosPolicy).
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class HistoryCacheAttributes {

    /**
     * Maximum payload size of the history, default value 500.
     */
    public int payloadMaxSize;
    /**
     * Number of the initial Reserved Caches, default value 500.
     */
    public int initialReservedCaches;
    /**
     * Maximum number of reserved caches. Default value is 0 that indicates to
     * keep reserving until something breaks.
     */
    public int maximumReservedCaches;

    /**
     * Default {@link HistoryCacheAttributes} constructor
     */
    public HistoryCacheAttributes() {
        this.payloadMaxSize = 500;
        this.initialReservedCaches = 500;
        this.maximumReservedCaches = 0;
    }

    /**
     * Alternative {@link EndpointAttributes} constructor
     *
     * @param payloadMaxSize Maximum payload size.
     * @param initialeservedCaches Initial reserved caches.
     * @param maximumReservedCaches Maximum reserved caches.
     */
    public HistoryCacheAttributes(int payloadMaxSize, int initialeservedCaches, int maximumReservedCaches) {
        super();
        this.payloadMaxSize = payloadMaxSize;
        this.initialReservedCaches = initialeservedCaches;
        this.maximumReservedCaches = maximumReservedCaches;
    }

}
