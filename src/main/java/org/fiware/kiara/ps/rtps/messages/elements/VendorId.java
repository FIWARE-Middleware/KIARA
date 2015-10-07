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
package org.fiware.kiara.ps.rtps.messages.elements;

import java.io.IOException;

import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.ps.rtps.messages.RTPSSubmessageElement;

/**
 * Class that represents the RTPS VendorId submessage element.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class VendorId extends RTPSSubmessageElement {

    /**
     * First byte of the ID
     */
    byte m_id_0;
    
    /**
     * Second byte of the ID
     */
    byte m_id_1;

    /**
     * Default {@link VendorId} constructor
     */
    public VendorId() {
        m_id_0 = (byte) 0x0;
        m_id_1 = (byte) 0x0;
    }

    /**
     * Alternative {@link VendorId} constructor
     * 
     * @param id_0 First byte of the {@link VendorId}
     * @param id_1 Second byte of the {@link VendorId}
     */
    public VendorId(byte id_0, byte id_1) {
        m_id_0 = id_0;
        m_id_1 = id_1;
    }

    /**
     * Set the VendorId to unknown
     * 
     * @return The object in which the method has been invoked
     */
    public VendorId setVendorUnknown() {
        this.m_id_0 = (byte) 0x00;
        this.m_id_1 = (byte) 0x00;
        return this;
    }

    /**
     * Set the VendorId to eProsima
     * 
     * @return The object in which the method has been invoked
     */
    public VendorId setVendoreProsima() {
        this.m_id_0 = (byte) 0x01;
        this.m_id_1 = (byte) 0x0F;
        return this;
    }

    /**
     * Compares two VendorId objects
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof VendorId) {
            VendorId instance = (VendorId) other;
            boolean retVal = true;

            retVal &= this.m_id_0 == instance.m_id_0;
            retVal &= this.m_id_1 == instance.m_id_1;

            return retVal;
        }
        return false;
    }

    /**
     * Serializes a VendorId object
     */
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        impl.serializeByte(message, "", this.m_id_0);
        impl.serializeByte(message, "", this.m_id_1);
    }

    /**
     * Deserializes a VendorId object
     */
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.m_id_0 = impl.deserializeByte(message, "");
        this.m_id_1 = impl.deserializeByte(message, "");
    }

    /**
     * Get the VendorId serialized size
     */
    @Override
    public short getSerializedSize() {
        return 2;
    }

    /**
     * Copies the content of a VendorId object
     * 
     * @param vendorId The VendorId to be copied
     */
    public void copy(VendorId vendorId) {
        this.m_id_0 = vendorId.m_id_0;
        this.m_id_1 = vendorId.m_id_1;
    }

}
