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
package org.fiware.kiara.ps.rtps.writer.timedevent;

import org.fiware.kiara.ps.rtps.resources.TimedEvent;
import org.fiware.kiara.ps.rtps.writer.RTPSWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public class UnsentChangesNotEmptyEvent extends TimedEvent {
    
    private RTPSWriter m_writer;
    
    private static final Logger logger = LoggerFactory.getLogger(UnsentChangesNotEmptyEvent.class);
    
    public UnsentChangesNotEmptyEvent(RTPSWriter writer, double milliseconds) {
        super(milliseconds);
        this.m_writer = writer;
    }

    @Override
    public void event(EventCode code, String msg) {
        if (code == EventCode.EVENT_SUCCESS) {
            logger.debug("Sending unsent changes");
            System.out.println("UnsentChangesNotEmptyEvent - Sending unsent changes");
            this.m_writer.unsentChangesNotEmpty();
        } else if (code == EventCode.EVENT_ABORT) {
            logger.debug("Aborting automatic change sending");
            System.out.println("UnsentChangesNotEmptyEvent - Aborting automatic change sending");
            this.stopSemaphorePost();
        } else {
            logger.debug("MSG: {}", msg);
        }
        this.stopTimer();
    }

}
