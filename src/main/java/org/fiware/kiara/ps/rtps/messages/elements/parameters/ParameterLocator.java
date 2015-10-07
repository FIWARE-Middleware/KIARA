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
import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.common.LocatorKind;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

/**
 * Locator RTPS DATA parameter
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public class ParameterLocator extends Parameter {

    /**
     * {@link Parameter} value
     */
    private final Locator m_loc;

    /**
     * Default {@link ParameterLocator} constructor
     */
    public ParameterLocator(ParameterId pid) {
        super(pid, Parameter.PARAMETER_LOCATOR_LENGTH);
        m_loc = new Locator();
    }

    /**
     * Constructor using a parameter PID and the parameter length
     * @param pid Pid of the parameter
     * @param length Its associated length
     */
    public ParameterLocator(ParameterId pid, short length) {
        super(pid, length);
        m_loc = new Locator();
    }

    /**
     * Alternative {@link ParameterLocator} constructor
     * 
     * @param pid Pid of the parameter
     * @param length Its associated length
     * @param loc {@link Locator} that will be stored in the {@link ParameterLocator}
     */
    public ParameterLocator(ParameterId pid, short length, Locator loc) {
        super(pid, length);
        m_loc = new Locator(loc);
    }

    /**
     * Serializes a {@link ParameterLocator} object and its inherited attributes
     */
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        super.serialize(impl, message, name);
        impl.serializeI32(message, name, this.m_loc.getKind().getValue());
        impl.serializeUI32(message, name, this.m_loc.getPort());
        for (int i=0; i < this.m_loc.getAddress().length; ++i) {
            impl.serializeByte(message, name, this.m_loc.getAddress()[i]);
        }
    }

    /**
     * Deserializes a {@link ParameterLocator} object and its inherited attributes
     */
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        super.deserialize(impl, message, name);
        this.m_loc.setKind(LocatorKind.values()[impl.deserializeI32(message, name)]);
        this.m_loc.setPort(impl.deserializeUI32(message, name));
        byte[] addr = new byte[16];
        for (int i=0; i < addr.length; ++i) {
            addr[i] = impl.deserializeByte(message, name);
        }
        this.m_loc.setAddress(addr);
    }
    
    /**
     * Deserializes a {@link ParameterLocator} object and not its inherited attributes
     */
    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.m_loc.setKind(LocatorKind.values()[impl.deserializeI32(message, name)]);
        this.m_loc.setPort(impl.deserializeUI32(message, name));
        byte[] addr = new byte[16];
        for (int i=0; i < addr.length; ++i) {
            addr[i] = impl.deserializeByte(message, name);
        }
        this.m_loc.setAddress(addr);
    }

    /**
     * Set the {@link Locator} attribute
     * 
     * @param loc The {@link Locator} attribute
     */
    public void setLocator(Locator loc) {
        this.m_loc.copy(loc);
    }

    /**
     * Get the {@link Locator} attribute
     * 
     * @return The {@link Locator} attribute
     */
    public Locator getLocator() {
        return this.m_loc;
    }
    
    /**
     * Transforms a {@link ParameterLocator} into its String representation
     */
    public String toString() {
        return this.m_loc.toString();
    }

}
