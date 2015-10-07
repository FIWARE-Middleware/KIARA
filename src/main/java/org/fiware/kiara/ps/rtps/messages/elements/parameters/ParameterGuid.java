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
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

/**
 * GUID RTPS DATA parameter
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public class ParameterGuid extends Parameter {

    /**
     * {@link Parameter} value
     */
    private final GUID m_guid;

    /**
     * Default {@link ParameterGuid} constructor
     */
    public ParameterGuid(ParameterId pid) {
        super(pid, Parameter.PARAMETER_GUID_LENGTH);
        m_guid = new GUID();
    }

    /**
     * Constructor using a parameter PID and the parameter length
     * @param pid Pid of the parameter
     * @param length Its associated length
     */
    public ParameterGuid(ParameterId pid, short length) {
        super(pid, length);
        m_guid = new GUID();
    }

    /**
     * Alternative {@link ParameterGuid} constructor
     * @param pid Pid of the parameter
     * @param length Its associated length
     * @param guid The Entity {@link GUID}
     */
    public ParameterGuid(ParameterId pid, short length, GUID guid) {
        super(pid, length);
        m_guid = new GUID();
        m_guid.copy(guid);
    }

    /**
     * Alternative {@link ParameterGuid} constructor
     * @param pid Pid of the parameter
     * @param length Its associated length
     * @param iH The {@link InstanceHandle} representing the KEY
     */
    public ParameterGuid(ParameterId pid, short length, InstanceHandle iH) {
        super(pid, length);
        m_guid = new GUID();
        for(int i =0; i<16; ++i) {
            if(i<12)
                m_guid.getGUIDPrefix().setValue(i, iH.getValue(i));
            else
                m_guid.getEntityId().setValue(i-12, iH.getValue(i));
        }
    };

    /**
     * Serializes a {@link ParameterGuid} object and its inherited attributes
     */
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        super.serialize(impl, message, name);
        this.m_guid.serialize(impl, message, name);
    }

    /**
     * Deserializes a {@link ParameterGuid} object and its inherited attributes
     */
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        super.deserialize(impl, message, name);
        this.m_guid.deserialize(impl, message, name);
    }

    /**
     * Deserializes a {@link ParameterGuid} object and not its inherited attributes
     */
    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.m_guid.deserialize(impl, message, name);
    }

    /**
     * Get the {@link GUID}
     * 
     * @return The {@link GUID}
     */
    public GUID getGUID() {
        return this.m_guid;
    }

    /**
     * Set the {@link GUID}
     * 
     * @param guid The {@link GUID} to be set
     */
    public void setGUID(GUID guid) {
        this.m_guid.copy(guid);
    }



}
