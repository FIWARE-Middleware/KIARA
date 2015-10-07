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
import org.fiware.kiara.ps.rtps.messages.elements.ProtocolVersion;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

/**
 * Protocol Version RTPS DATA parameter
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public class ParameterProtocolVersion extends Parameter {

    /**
     * {@link Parameter} value
     */
    private final ProtocolVersion m_protocolVersion;

    /**
     * Default {@link ParameterProtocolVersion} constructor
     */
    public ParameterProtocolVersion() {
        super(ParameterId.PID_PROTOCOL_VERSION, Parameter.PARAMETER_PROTOCOL_LENGTH);
        m_protocolVersion = new ProtocolVersion();
    }

    /**
     * Constructor using a parameter PID and the parameter length
     * @param pid Pid of the parameter
     * @param length Its associated length
     */
    public ParameterProtocolVersion(ParameterId pid, short length) {
        super(pid, length);
        m_protocolVersion = new ProtocolVersion();
    }

    /**
     * Serializes a {@link ParameterProtocolVersion} object and its inherited attributes
     */
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        super.serialize(impl, message, name);
        this.m_protocolVersion.serialize(impl, message, name);
    }

    /**
     * Deserializes a {@link ParameterProtocolVersion} object and its inherited attributes
     */
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        super.deserialize(impl, message, name);
        this.m_protocolVersion.deserialize(impl, message, name);
    }

    /**
     * Deserializes a {@link ParameterProtocolVersion} object and not its inherited attributes
     */
    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.m_protocolVersion.deserialize(impl, message, name);
    }

    /**
     * Set the {@link ProtocolVersion} attribute
     * 
     * @param protocolVersion The {@link ProtocolVersion} to be set
     */
    public void setProtocolVersion(ProtocolVersion protocolVersion) {
        this.m_protocolVersion.copy(protocolVersion);
    }
    
    /**
     * Get the {@link ProtocolVersion} attribute
     * 
     * @return The {@link ProtocolVersion} attribute
     */
    public ProtocolVersion getProtocolVersion() {
        return this.m_protocolVersion;
    }

    

}
