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
 * Class SubscriberAttributes, used by the user to define the attributes of a
 * Subscriber.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class SubscriberAttributes {

    /**
     * User Defined ID, used for StaticEndpointDiscovery, default value -1.
     */
    private short m_userDefinedID;

    /**
     * Entity ID, if the user want to specify the EntityID of the enpoint,
     * default value -1.
     */
    private short m_entityID;

    /**
     * Topic Attributes
     */
    public TopicAttributes topic;

    /**
     * Reader QOs.
     */
    public ReaderQos qos;

    /**
     * Times for a RELIABLE Reader
     */
    public ReaderTimes times;

    /**
     * Unicast locator list
     */
    public LocatorList unicastLocatorList;

    /**
     * Multicast locator list
     */
    public LocatorList multicastLocatorList;

    /**
     * Expects Inline QOS
     */
    public boolean expectsInlineQos;

    /**
     * Main SubscriberAttributes constructor
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

    /**
     * Get the user defined ID
     *
     * @return User defined ID
     */
    public short getUserDefinedID() {
        return m_userDefinedID;
    }

    /**
     * Set the user defined ID
     *
     * @param userDefinedID User defined ID to be set
     */
    public void setUserDefinedID(short userDefinedID) {
        this.m_userDefinedID = userDefinedID;
    }

    /**
     * Get the entity defined ID
     *
     * @return Entity ID
     */
    public short getEntityID() {
        return m_entityID;
    }

    /**
     * Set the entity ID
     *
     * @param entityID Entity ID to be set
     */
    public void setEntityID(short entityID) {
        this.m_entityID = entityID;
    }

}
