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
 * Boolean RTPS DATA parameter
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public class ParameterBool extends Parameter {

    /**
     * {@link Parameter} value
     */
    private boolean m_bool;

    /**
     * Default {@link ParameterBool} constructor
     */
    public ParameterBool() {
        super(ParameterId.PID_EXPECTS_INLINE_QOS, Parameter.PARAMETER_BOOL_LENGTH);
    }

    /**
     * Constructor using a parameter PID and the parameter length
     * @param pid Pid of the parameter
     * @param length Its associated length
     */
    public ParameterBool(ParameterId pid, short length) {
        super(pid, length);
        m_bool = false;
    }

    /**
     * Alternative {@link ParameterBool} constructor
     * @param pid Pid of the parameter
     * @param length Its associated length
     * @param inbool Is an inline parameter
     */
    public ParameterBool(ParameterId pid, short length, boolean inbool) {
        super(pid,length);
        m_bool = inbool;
    }

    /**
     * Serializes a {@link ParameterBool} object and its inherited attributes
     */
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        super.serialize(impl, message, name);
        impl.serializeBoolean(message, name, this.m_bool);
    }

    /**
     * Deserializes a {@link ParameterBool} object and its inherited attributes
     */
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        super.deserialize(impl, message, name);
        this.m_bool = impl.deserializeBoolean(message, name);
    }

    /**
     * Deserializes a {@link ParameterBool} object and not its inherited attributes
     */
    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.m_bool = impl.deserializeBoolean(message, name);
    }

    /**
     * Set the bool attribute
     * 
     * @param bool The boolean value to be set
     */
    public void setBool(boolean bool) {
        this.m_bool = bool;
    }

    /**
     * Get the bool attribute
     * 
     * @return The bool attribute
     */
    public boolean getBool() {
        return this.m_bool;
    }

}
