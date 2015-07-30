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

import org.fiware.kiara.ps.qos.WriterQos;
import org.fiware.kiara.ps.rtps.attributes.WriterTimes;
import org.fiware.kiara.ps.rtps.common.LocatorList;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public class PublisherAttributes {
    
    public TopicAttributes topic;
    
    public WriterQos qos;
    
    public WriterTimes times;
    
    public LocatorList unicastLocatorList;
    
    public LocatorList multicastLocatorList;
    
    private short m_userDefinedId;
    
    private short m_entityId;
    
    /**
     * Main Constructor
     */
    public PublisherAttributes() {
        this.m_userDefinedId = -1;
        this.m_entityId = -1;
        
        this.topic = new TopicAttributes();
        this.qos = new WriterQos();
        this.times = new WriterTimes();
        this.unicastLocatorList = new LocatorList();
        this.multicastLocatorList = new LocatorList();
        
    }
    
    public short getUserDefinedID() {
        return this.m_userDefinedId;
    }
    
    public void setUserDefinedID(short userDefinedId) {
        this.m_userDefinedId = userDefinedId;
    }
    
    public short getEntityId() {
        return this.m_entityId;
    }
    
    public void setEntityId(short entityId) {
        this.m_entityId = entityId;
    }

}
