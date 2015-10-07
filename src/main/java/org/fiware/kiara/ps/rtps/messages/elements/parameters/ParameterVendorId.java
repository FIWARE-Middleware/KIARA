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
import org.fiware.kiara.ps.rtps.messages.elements.VendorId;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

/**
 * Vendor Identifier RTPS DATA parameter
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public class ParameterVendorId extends Parameter {

    /**
     * {@link Parameter} value
     */
    private final VendorId m_vendorId;

    /**
     * Default {@link ParameterVendorId} constructor
     */
    public ParameterVendorId() {
        super(ParameterId.PID_VENDORID, Parameter.PARAMETER_VENDOR_LENGTH);
        m_vendorId = new VendorId();
        m_vendorId.setVendoreProsima();
    }

    /**
     * Constructor using a parameter PID and the parameter length
     * @param pid Pid of the parameter
     * @param length Its associated length
     */
    public ParameterVendorId(ParameterId pid, short length) {
        super(pid, length);
        m_vendorId = new VendorId();
        m_vendorId.setVendoreProsima();
    }

    /**
     * Serializes a {@link ParameterVendorId} object and its inherited attributes
     */
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        super.serialize(impl, message, name);
        this.m_vendorId.serialize(impl, message, name);
    }

    /**
     * Deserializes a {@link ParameterVendorId} object and its inherited attributes
     */
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        super.deserialize(impl, message, name);
        this.m_vendorId.deserialize(impl, message, name);
    }

    /**
     * Deserializes a {@link ParameterVendorId} object and not its inherited attributes
     */
    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.m_vendorId.deserialize(impl, message, name);
    }
    
    /**
     * Set the {@link VendorId}
     * 
     * @param vendorId The {@link VendorId} to be set
     */
    public void setVendorId(VendorId vendorId) {
        this.m_vendorId.copy(vendorId);
    }

    /**
     * Get the {@link VendorId}
     * 
     * @return The {@link VendorId}
     */
    public VendorId getVendorId() {
        return this.m_vendorId;
    }

    

}
