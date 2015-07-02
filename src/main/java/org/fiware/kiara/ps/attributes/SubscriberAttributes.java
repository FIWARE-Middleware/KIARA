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

import org.fiware.kiara.ps.qos.ReaderQos;
import org.fiware.kiara.ps.rtps.attributes.ReaderTimes;
import org.fiware.kiara.ps.rtps.common.LocatorList;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public class SubscriberAttributes {
    
    private short m_userDefinedID;
    
    private short m_entityID;
    
    public TopicAttributes topic;
    
    public ReaderQos qos;
    
    public ReaderTimes times;
    
    public LocatorList unicastLocatorList;
    
    public LocatorList multicastLocatorList;
    
    public boolean expectsInlineQos;
    
    /**
     * Main Constructor
     */
    public SubscriberAttributes() {
        this.m_userDefinedID = -1;
        this.m_entityID = -1;
        this.expectsInlineQos = false;
        
        this.topic = new TopicAttributes();
        this.qos = new ReaderQos();
        this.times = new ReaderTimes();
        this.unicastLocatorList = new LocatorList();
        this.multicastLocatorList = new LocatorList();
    }

    public short getUserDefinedID() {
        return m_userDefinedID;
    }

    public void setUserDefinedID(short m_userDefinedID) {
        this.m_userDefinedID = m_userDefinedID;
    }

    public short getEntityID() {
        return m_entityID;
    }

    public void setEntityID(short m_entityID) {
        this.m_entityID = m_entityID;
    }

}
