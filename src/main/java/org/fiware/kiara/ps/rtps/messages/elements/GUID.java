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
package org.fiware.kiara.ps.rtps.messages.elements;


/**
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class GUID {

    private GUIDPrefix m_guidPrefix;

    private EntityId m_entityId;

    public GUID() {
        this.m_guidPrefix = new GUIDPrefix();
        this.m_entityId = new EntityId();
    }

    public GUID(GUIDPrefix guidPrefix, EntityId entityId) {
        this.m_guidPrefix = guidPrefix;
        this.m_entityId = entityId;
    }

    public GUIDPrefix getGUIDPrefix() {
        return this.m_guidPrefix;
    }

    public void setGUIDPrefix(GUIDPrefix guidPrefix) {
        this.m_guidPrefix = guidPrefix;
    }

    public EntityId getEntityId() {
        return this.m_entityId;
    }

    public void setEntityId(EntityId id) {
        this.m_entityId = id;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof GUID) {
            return this.m_guidPrefix.equals(((GUID) other).m_guidPrefix) && this.m_entityId.equals(((GUID) other).m_entityId);
        }
        return false;
    }
    
    @Override
    public String toString() {
        return this.m_guidPrefix.toString() + "-" + this.m_entityId.toString();
    }

}
