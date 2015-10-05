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
package org.fiware.kiara.ps.subscriber;

import org.fiware.kiara.ps.rtps.common.MatchingInfo;

/**
* Class representing the listener that will be invoked when a certain
* event should occur.
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public abstract class SubscriberListener {
    
    /**
     * Method to be called when a new data message has arrived
     * 
     * @param sub The Subscriber entity that received the sample
     */
    public abstract void onNewDataMessage(Subscriber<?> sub);
    
    /**
     * Method to be called when a new Publisher matches (or unmatches) the Subscriber
     * 
     * @param sub The Subscriber to which the Publishes has matched (or unmatched)
     * @param info The associated MatchingInfo
     */
    public abstract void onSubscriptionMatched(Subscriber<?> sub, MatchingInfo info);

}
