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
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

/**
 * Max Serialized Size RTPS DATA parameter
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public class ParameterMaxSerializedSize extends Parameter {
    
    /**
     * {@link Parameter} value
     */
    private int maxSerializedSize;

    /**
     * Default {@link ParameterMaxSerializedSize} constructor
     */
    public ParameterMaxSerializedSize() {
        super(ParameterId.PID_TYPE_MAX_SIZE_SERIALIZED, Parameter.PARAMETER_TYPE_MAX_SIZE_SERIALIZED_LENGTH);
    }

    /**
     * Serializes a {@link ParameterMaxSerializedSize} object and its inherited attributes
     */
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        super.serialize(impl, message, name);
        impl.serializeUI32(message, name, this.maxSerializedSize);
    }

    /**
     * Deserializes a {@link ParameterMaxSerializedSize} object and its inherited attributes
     */
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        super.deserialize(impl, message, name);
        this.maxSerializedSize = impl.deserializeUI32(message, name);
    }

    /**
     * Deserializes a {@link ParameterMaxSerializedSize} object and not its inherited attributes
     */
    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.maxSerializedSize = impl.deserializeUI32(message, name);
    }

    /**
     * Get the maximum serialized size
     * 
     * @return The maximum serialized size
     */
    public int getMaxSerializedSize() {
        return maxSerializedSize;
    }

    /**
     * Set the maximum serialized size
     * 
     * @param maxSerializedSize The maximum serialized size
     */
    public void setMaxSerializedSize(int maxSerializedSize) {
        this.maxSerializedSize = maxSerializedSize;
    }

}
