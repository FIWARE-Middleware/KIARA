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
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class EndpointAttributes {

    public EndpointKind endpointKind;
    public TopicKind topicKind;
    public ReliabilityKind reliabilityKind;
    public DurabilityKind durabilityKind;
    public final LocatorList unicastLocatorList;
    public final LocatorList multicastLocatorList;
    private short m_userDefinedId;
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

    public short getUserDefinedID() {
        return this.m_userDefinedId;
    }

    public void setUserDefinedID(short id) {
        this.m_userDefinedId = id;
    }

    public short getEntityID() {
        return this.m_entityId;
    }

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
