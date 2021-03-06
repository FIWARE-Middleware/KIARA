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
package org.fiware.kiara.ps.rtps.common;

/**
 * MatchingStatus, indicates whether the matched publication/subscription method of the PublisherListener or SubscriberListener has
 * been called for a matching or a removal of a remote endpoint.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public enum MatchingStatus {

    /**
     * Status is MATCHED_MATHING (new match)
     */
    MATCHED_MATHING,
    /**
     * Status is REMOVED_MATCHING (no longer matching)
     */
    REMOVED_MATCHING

}
