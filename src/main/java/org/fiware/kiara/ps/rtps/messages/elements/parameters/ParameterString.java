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
 * String RTPS DATA parameter
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public class ParameterString extends Parameter {
    
    /**
     * {@link Parameter} value
     */
    private String m_content; 

    /**
     * Default {@link ParameterString} constructor
     * @param pid
     */
    public ParameterString(ParameterId pid) {
        super(pid, (short) 4);
    }

    /**
     * Constructor using a parameter PID and the parameter length
     * @param pid Pid of the parameter
     * @param length Its associated length
     */
    public ParameterString(ParameterId pid, short length) {
        super(pid, length);
    }

    /**
     * Alternative {@link ParameterString} constructor
     * 
     * @param pid Pid of the parameter
     * @param length Its associated length
     * @param str String value
     */
    public ParameterString(ParameterId pid, short length, String str) {
        super(pid, length);
        setContent(str);
    }

    /**
     * Serializes a {@link ParameterString} object and its inherited attributes
     */
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        super.serialize(impl, message, name);
        if (this.m_content != null) {
            impl.serializeString(message, name, this.m_content);
        }
    }
    
    /**
     * Deserializes a {@link ParameterString} object and its inherited attributes
     */
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        super.deserialize(impl, message, name);
        this.m_content = impl.deserializeString(message, name);
    }
    
    /**
     * Deserializes a {@link ParameterString} object and not its inherited attributes
     */
    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.m_content = impl.deserializeString(message, name);
    }

    /**
     * Get the String value of the {@link ParameterString}
     * 
     * @return The {@link ParameterString} value
     */
    public String getString() {
        return this.m_content;
    }
    
    /**
     * Set the String value of the {@link ParameterString} and modifies its internal length
     * 
     * @param content The String content
     */
    public void setContent(String content) {
        this.m_content = content;
        int size = (4 + this.m_content.length());
        int pad = (size % 4 == 0) ? 0 : (4 - (size % 4));
        super.m_length = (short) (size + pad);
    }

    

}
