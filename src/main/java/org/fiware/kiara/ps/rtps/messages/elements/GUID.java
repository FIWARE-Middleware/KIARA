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

import java.io.IOException;

import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.Serializable;
import org.fiware.kiara.serialization.impl.SerializerImpl;


/**
 * Structure GUID, entity identifier, unique in DDS-RTPS Domain.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class GUID implements Serializable {

    /**
     * {@link GUIDPrefix} of the GUID
     */
    private final GUIDPrefix m_guidPrefix;

    /**
     * {@link EntityId} of the GUID
     */
    private final EntityId m_entityId;

    /**
     * Default constructor
     */
    public GUID() {
        this.m_guidPrefix = new GUIDPrefix();
        this.m_entityId = new EntityId();
    }

    /**
     * Alternative {@link GUID} constructor (using the {@link GUIDPrefix} and 
     * the {@link EntityId})
     * 
     * @param guidPrefix The {@link GUIDPrefix} of the {@link GUID}
     * @param entityId The {@link EntityId} of the {@link GUID}
     */
    public GUID(GUIDPrefix guidPrefix, EntityId entityId) {
        this.m_guidPrefix = guidPrefix;
        this.m_entityId = entityId;
    }

    /**
     * Get the {@link GUIDPrefix} part of the {@link GUID}
     * 
     * @return The {@link GUIDPrefix}
     */
    public GUIDPrefix getGUIDPrefix() {
        return this.m_guidPrefix;
    }

    /**
     * Set the {@link GUIDPrefix} part of the {@link GUID}
     * 
     * @param guidPrefix The {@link GUIDPrefix} to be set
     */
    public void setGUIDPrefix(GUIDPrefix guidPrefix) {
        this.m_guidPrefix.copy(guidPrefix);
    }

    /**
     * Get the {@link EntityId} of the GUID
     * 
     * @return The {@link EntityId}
     */
    public EntityId getEntityId() {
        return this.m_entityId;
    }

    /**
     * Set the {@link EntityId} of the GUID
     * @param id The {@link EntityId} to be set
     */
    public void setEntityId(EntityId id) {
        this.m_entityId.copy(id);
    }

    /**
     * Copies the content of a {@link GUID} object
     * 
     * @param value The {@link GUID} to be copied
     */
    public void copy(GUID value) {
        m_guidPrefix.copy(value.m_guidPrefix);
        m_entityId.copy(value.m_entityId);
    }

    /**
     * Compares two {@link GUID} objects
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof GUID) {
            boolean result = this.m_guidPrefix.equals(((GUID) other).m_guidPrefix) && this.m_entityId.equals(((GUID) other).m_entityId); 
            return result;
        }
        return false;
    }
    
    /**
     * Converts a {@link GUID} object to its String representation
     */
    @Override
    public String toString() {
        return this.m_guidPrefix.toString() + "-" + this.m_entityId.toString();
    }
    
    /**
     * Serializes a {@link GUID} object
     */
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        this.m_guidPrefix.serialize(impl, message, name);
        this.m_entityId.serialize(impl, message, name);
    }
    
    /**
     * Deserializes a {@link GUID} object
     */
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.m_guidPrefix.deserialize(impl, message, name);
        this.m_entityId.deserialize(impl, message, name);
    }
    
    /**
     * Creates an {@link InstanceHandle} object whose GUID is the object on which this method is invoked
     * 
     * @return The created {@link InstanceHandle}
     */
    public InstanceHandle toInstanceHandle() {
        InstanceHandle retVal = new InstanceHandle();
        retVal.setGuid(this);
        return retVal;
    }

    /**
     * Returns a different value for every different {@link GUID}
     */
    @Override
    public int hashCode() {
        return this.m_guidPrefix.hashCode() + this.m_entityId.hashCode();
    }

   
}
