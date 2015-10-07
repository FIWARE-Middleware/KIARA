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
 * IPv4 RTPS DATA parameter
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public class ParameterIPv4Address extends Parameter {
    
    /**
     * {@link Parameter} value
     */
    private byte[] m_address;

    /**
     * Default {@link ParameterIPv4Address} constructor
     */
    public ParameterIPv4Address(ParameterId pid) {
        super(pid, Parameter.PARAMETER_IP4_LENGTH);
        this.m_address = new byte[4];
        this.setIpV4Address((byte) 0, (byte) 0, (byte) 0, (byte) 0);
    }
    
    /**
     * Serializes a {@link ParameterIPv4Address} object and its inherited attributes
     */
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        super.serialize(impl, message, name);
        impl.serializeByte(message, name, this.m_address[0]);
        impl.serializeByte(message, name, this.m_address[1]);
        impl.serializeByte(message, name, this.m_address[2]);
        impl.serializeByte(message, name, this.m_address[3]);
    }

    /**
     * Deserializes a {@link ParameterIPv4Address} object and its inherited attributes
     */
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        super.deserialize(impl, message, name);
        this.m_address[0] = impl.deserializeByte(message, name);
        this.m_address[1] = impl.deserializeByte(message, name);
        this.m_address[2] = impl.deserializeByte(message, name);
        this.m_address[3] = impl.deserializeByte(message, name);
    }

    /**
     * Deserializes a {@link ParameterIPv4Address} object and not its inherited attributes
     */
    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.m_address[0] = impl.deserializeByte(message, name);
        this.m_address[1] = impl.deserializeByte(message, name);
        this.m_address[2] = impl.deserializeByte(message, name);
        this.m_address[3] = impl.deserializeByte(message, name);
    }
    
    /**
     * Set the IPv4 address (structure: 01.02.03.04)
     * 
     * @param o1 First address number
     * @param o2 Second address number
     * @param o3 Third address number
     * @param o4 Fourth address number
     */
    public void setIpV4Address(byte o1, byte o2, byte o3, byte o4) {
        this.m_address[0] = o1;
        this.m_address[1] = o2;
        this.m_address[2] = o3;
        this.m_address[3] = o4;
    }

    

}
