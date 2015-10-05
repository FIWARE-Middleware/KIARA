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

import org.fiware.kiara.ps.rtps.common.DurabilityKind;
import org.fiware.kiara.ps.rtps.common.EndpointKind;
import org.fiware.kiara.ps.rtps.common.LocatorList;
import org.fiware.kiara.ps.rtps.common.ReliabilityKind;
import org.fiware.kiara.ps.rtps.common.TopicKind;

/**
 * Structure EndpointAttributes, describing the attributes associated with an
 * RTPS Endpoint.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class EndpointAttributes {

    /**
     * Endpoint kind, default value WRITER
     */
    public EndpointKind endpointKind;
    /**
     * Topic kind, default value NO_KEY
     */
    public TopicKind topicKind;
    /**
     * Reliability kind, default value BEST_EFFORT
     */
    public ReliabilityKind reliabilityKind;
    /**
     * Durability kind, default value VOLATILE
     */
    public DurabilityKind durabilityKind;
    /**
     * Unicast locator list
     */
    public final LocatorList unicastLocatorList;
    /**
     * Multicast locator list
     */
    public final LocatorList multicastLocatorList;
    /**
     * User Defined ID, used for StaticEndpointDiscovery, default value -1.
     */
    private short m_userDefinedId;

    /**
     * Entity ID, if the user want to specify the EntityID of the enpoint,
     * default value -1.
     */
    private short m_entityId;

    public EndpointAttributes() {
        this.topicKind = TopicKind.NO_KEY;
        this.reliabilityKind = ReliabilityKind.BEST_EFFORT;
        this.durabilityKind = DurabilityKind.VOLATILE;
        this.m_userDefinedId = -1;
        this.m_entityId = -1;
        this.endpointKind = EndpointKind.WRITER;
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
     * @param id User defined ID to be set
     */
    public void setUserDefinedID(short id) {
        this.m_userDefinedId = id;
    }

    /**
     * Get the entity defined ID
     *
     * @return Entity ID
     */
    public short getEntityID() {
        return this.m_entityId;
    }

    /**
     * Set the entity ID
     *
     * @param id Entity ID to be set
     */
    public void setEntityID(short id) {
        this.m_entityId = id;
    }

    public LocatorList getUnicastLocatorList() {
        return unicastLocatorList;
    }

    public LocatorList getMulticastLocatorList() {
        return multicastLocatorList;
    }

    public void copy(EndpointAttributes value) {
        endpointKind = value.endpointKind;
        topicKind = value.topicKind;
        reliabilityKind = value.reliabilityKind;
        durabilityKind = value.durabilityKind;
        unicastLocatorList.copy(value.unicastLocatorList);
        multicastLocatorList.copy(value.multicastLocatorList);
        m_userDefinedId = value.m_userDefinedId;
        m_entityId = value.m_entityId;
    }

}
