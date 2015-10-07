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
import org.fiware.kiara.ps.rtps.messages.elements.Count;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

/**
 * Count RTPS DATA parameter
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public class ParameterCount extends Parameter {

    /**
     * {@link Parameter} value
     */
    private Count m_count;

    /**
     * Default {@link ParameterCount} constructor
     */
    public ParameterCount() {
        super(ParameterId.PID_PARTICIPANT_MANUAL_LIVELINESS_COUNT, Parameter.PARAMETER_COUNT_LENGTH);
    }

    /**
     * Serializes a {@link ParameterCount} object and its inherited attributes
     */
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        if (this.m_count != null) {
            super.serialize(impl, message, name);
            this.m_count.serialize(impl, message, name);
        }
    }

    /**
     * Deserializes a {@link ParameterCount} object and its inherited attributes
     */
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        if (this.m_count == null) {
            this.m_count = new Count(0);
        }
        super.deserialize(impl, message, name);
        this.m_count.deserialize(impl, message, name);
    }

    /**
     * Deserializes a {@link ParameterCount} object and not its inherited attributes
     */
    @Override
    public void deserializeContent(SerializerImpl impl,BinaryInputStream message, String name) throws IOException {
        if (this.m_count == null) {
            this.m_count = new Count(0);
        }
        this.m_count.deserialize(impl, message, name);

    }

    /**
     * Get the {@link Count} attribute
     * 
     * @param count The {@link Count} attribute
     */
    public void setCount(Count count) {
        this.m_count = count;
    }

    

}
