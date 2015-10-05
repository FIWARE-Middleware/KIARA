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
package org.fiware.kiara.ps.rtps.resources;

import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public class EventResource {
    
    private EventThread m_thread;
    
    private RTPSParticipant m_participant;
    
    public EventResource() {
        
    }

    public void initThread(RTPSParticipant participant) {
        // TODO Implement
        
        /*this.m_participant = participant;
        this.m_thread = new EventThread(this);
        this.m_thread.run();
        this.m_participant.resourceSemaphoreWait();*/
    }
    
    public void announceThread() {
        this.m_participant.resourceSemaphorePost();
    }
    
    public void startDatagramChannel() {
        // TODO Implement. Equivalent to start_io_service
    }

    public void destroy() {
        // TODO Auto-generated method stub
        
    }
    
    

}
