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

import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;

/**
 * Class ReaderTimes, defining the times associated with the Reliable Readers
 * events.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class ReaderTimes {

    /**
     * Delay to be applied when a hearbeat message is received, default value
     * ~116ms.
     */
    public Timestamp heartbeatResponseDelay;

    /**
     * Default {@link ReaderTimes} constructor
     */
    public ReaderTimes() {
        this.heartbeatResponseDelay = new Timestamp(0, 500 * 1000 * 1000);
    }

    /**
     * Alternative {@link ReaderAttributes} copy constructor
     * 
     * @param value The {@link ReaderTimes} object to copy data from
     */
    public ReaderTimes(ReaderTimes value) {
        this.heartbeatResponseDelay = new Timestamp(value.heartbeatResponseDelay);
    }

    /**
     * This method copies an instance of {@link ReaderTimes} into another
     * 
     * @param value The {@link ReaderTimes} to be copied
     */
    public void copy(ReaderTimes value) {
        heartbeatResponseDelay.copy(value.heartbeatResponseDelay);
    }

}
