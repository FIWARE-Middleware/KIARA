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
import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.RTPSSubmessageElement;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public abstract class Parameter extends RTPSSubmessageElement {
    
    public static short PARAMETER_KIND_LENGTH = 4;
    public static short PARAMETER_TIME_LENGTH = 8;
    public static short PARAMETER_PRESENTATION_LENGTH = 8;
    public static short PARAMETER_LOCATOR_LENGTH = 24;
    public static short PARAMETER_PORT_LENGTH = 4;
    public static short PARAMETER_BUILTINENDPOINTSET_LENGTH = 4;
    public static short PARAMETER_COUNT_LENGTH = 4;
    public static short PARAMETER_BOOL_LENGTH = 4;
    public static short PARAMETER_GUID_LENGTH = 16;
    public static short PARAMETER_PROTOCOL_LENGTH = 4;
    public static short PARAMETER_VENDOR_LENGTH = 4;
    public static short PARAMETER_IP4_LENGTH = 4;
    
    protected ParameterId m_parameterId;
    protected short m_length;

    public Parameter(ParameterId pid, short length) {
        this.m_parameterId = pid;
        this.m_length = length;
    }

    @Override
    public short getSerializedSize() {
        return 4;
    }

    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        impl.serializeI16(message, name, this.m_parameterId.getValue());
        impl.serializeI16(message, "", this.m_length);
    }

    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.m_parameterId = ParameterId.createFromValue(impl.deserializeI16(message, name));
        this.m_length = impl.deserializeI16(message, "");
    }

    public abstract void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException;

    public ParameterId getParameterId() {
        return this.m_parameterId;
    }
    
    public void copy(Parameter other) {
        this.m_parameterId = other.m_parameterId;
        this.m_length = other.m_length;
    }

}
