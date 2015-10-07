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
 * Port RTPS DATA parameter
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public class ParameterPort extends Parameter {
    
    /**
     * {@link Parameter} value
     */
    private int m_port;

    /**
     * Default {@link ParameterPort} constructor
     * 
     * @param pid The type {@link ParameterPort}
     */
    public ParameterPort(ParameterId pid) {
        super(pid, Parameter.PARAMETER_PORT_LENGTH);
    }

    /**
     * Constructor using a parameter PID and the parameter length
     * @param pid Pid of the parameter
     * @param length Its associated length
     */
    public ParameterPort(ParameterId pid, short length) {
        super(pid, length);
        m_port = 0;
    }

    /**
     * Alternative {@link ParameterPort} constructor
     * 
     * @param pid Pid of the parameter
     * @param length Its associated length
     * @param port The port number
     */
    public ParameterPort(ParameterId pid, short length, int port) {
        super(pid, length);
        m_port = port;
    }

    /**
     * Serializes a {@link ParameterPort} object and its inherited attributes
     */
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        super.serialize(impl, message, name);
        impl.serializeUI32(message, name, this.m_port);
    }

    /**
     * Deserializes a {@link ParameterPort} object and its inherited attributes
     */
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        super.deserialize(impl, message, name);
        this.m_port = impl.deserializeUI32(message, name);
    }

    /**
     * Deserializes a {@link ParameterPort} object and not its inherited attributes
     */
    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.m_port = impl.deserializeUI32(message, name);
    }
    
    /**
     * Set the {@link ParameterPort} port number
     * 
     * @param port The por number
     */
    public void setPort(int port) {
        this.m_port = port;
    }

}
