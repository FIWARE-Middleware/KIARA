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
package org.fiware.kiara.ps.rtps.messages.elements.parameters;

import java.io.IOException;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.EntityId;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

/**
 * Entity Identifier RTPS DATA parameter
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public class ParameterEntityId extends Parameter {
    
    /**
     * {@link Parameter} value
     */
    private EntityId m_entityId;

    /**
     * Default {@link ParameterEntityId} constructor
     */
    public ParameterEntityId(ParameterId pid) {
        super(pid, Parameter.PARAMETER_ENTITYID_LENGTH);
        this.m_entityId = new EntityId();
    }
    
    /**
     * Serializes a {@link ParameterEntityId} object and its inherited attributes
     */
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        super.serialize(impl, message, name);
        this.m_entityId.serialize(impl, message, name);
    }

    /**
     * Deserializes a {@link ParameterEntityId} object and its inherited attributes
     */
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        super.deserialize(impl, message, name);
        this.m_entityId.deserialize(impl, message, name);
    }

    /**
     * Deserializes a {@link ParameterEntityId} object and not its inherited attributes
     */
    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.m_entityId.deserialize(impl, message, name);
    }
    
    /**
     * Set the {@link EntityId} attribute
     * 
     * @param entityId The {@link EntityId} attribute to be set
     */
    public void setEntityId(EntityId entityId) {
        this.m_entityId = entityId;
    }

    

}
