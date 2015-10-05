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
 * Class PublisherAttributes, used by the user to define the attributes of a
 * Publisher.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class PublisherAttributes {

    /**
     * Topic Attributes for the Publisher
     */
    public TopicAttributes topic;

    /**
     * QOS for the Publisher
     */
    public WriterQos qos;

    /**
     * Writer Attributes
     */
    public WriterTimes times;

    /**
     * Unicast locator list
     */
    public LocatorList unicastLocatorList;

    /**
     * Multicast locator list
     */
    public LocatorList multicastLocatorList;

    /**
     * User Defined ID, used for StaticEndpointDiscovery, default value -1.
     */
    private short m_userDefinedId;

    /**
     * Entity ID, if the user want to specify the EntityID of the endpoint,
     * default value -1.
     */
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

    /**
     * Get the user defined ID
     *
     * @return User defined ID
     */
    public short getUserDefinedID() {
        return this.m_userDefinedId;
    }

    /**
     * Set the user defined ID
     *
     * @param userDefinedId User defined ID to be set
     */
    public void setUserDefinedID(short userDefinedId) {
        this.m_userDefinedId = userDefinedId;
    }

    /**
     * Get the entity defined ID
     *
     * @return Entity ID
     */
    public short getEntityId() {
        return this.m_entityId;
    }

    /**
     * Set the entity ID
     *
     * @param entityId Entity ID to be set
     */
    public void setEntityId(short entityId) {
        this.m_entityId = entityId;
    }

}
