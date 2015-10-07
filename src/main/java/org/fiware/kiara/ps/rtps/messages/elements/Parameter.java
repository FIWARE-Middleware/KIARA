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
* This class represents the Parameter fields of an RTPS 
* message. Each Parameter has its own length.
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public abstract class Parameter extends RTPSSubmessageElement {
    
    /**
     * Lengh of the KIND parameter
     */
    public static short PARAMETER_KIND_LENGTH = 4;
    
    /**
     * Lengh of the TIME parameter
     */
    public static short PARAMETER_TIME_LENGTH = 8;
    
    /**
     * Lengh of the PRESENTATION parameter
     */
    public static short PARAMETER_PRESENTATION_LENGTH = 8;
    
    /**
     * Lengh of the LOCATOR parameter
     */
    public static short PARAMETER_LOCATOR_LENGTH = 24;
    
    /**
     * Lengh of the PORT parameter
     */
    public static short PARAMETER_PORT_LENGTH = 4;
    
    /**
     * Lengh of the BUILTINENDPOINTSET parameter
     */
    public static short PARAMETER_BUILTINENDPOINTSET_LENGTH = 4;
    
    /**
     * Lengh of the COUNT parameter
     */
    public static short PARAMETER_COUNT_LENGTH = 4;
    
    /**
     * Lengh of the BOOL parameter
     */
    public static short PARAMETER_BOOL_LENGTH = 4;
    
    /**
     * Lengh of the GUID parameter
     */
    public static short PARAMETER_GUID_LENGTH = 16;
    
    /**
     * Lengh of the PROTOCOL parameter
     */
    public static short PARAMETER_PROTOCOL_LENGTH = 4;
    
    /**
     * Lengh of the VENDOR parameter
     */
    public static short PARAMETER_VENDOR_LENGTH = 4;
    
    /**
     * Lengh of the IP4 parameter
     */
    public static short PARAMETER_IP4_LENGTH = 4;
    
    /**
     * Lengh of the ENTITYID parameter
     */
    public static short PARAMETER_ENTITYID_LENGTH = 4;
    
    /**
     * Lengh of the DEADLINE_QOS parameter
     */
    public static short PARAMETER_DEADLINE_QOS_LENGTH = 8;
    
    /**
     * Lengh of the LIVELINESS_WOS parameter
     */
    public static short PARAMETER_LIVELINESS_QOS_LENGTH = 12;
    
    /**
     * Lengh of the TYPE_MAX_SIZE_SERIALIZED parameter
     */
    public static short PARAMETER_TYPE_MAX_SIZE_SERIALIZED_LENGTH = 12;
    
    /**
     * Parameter identifier
     */
    protected ParameterId m_parameterId;
    
    /**
     * Parameter length
     */
    protected short m_length;

    /**
     * Default constructor
     * 
     * @param pid The parameter identifier
     * @param length The parameter length
     */
    public Parameter(ParameterId pid, short length) {
        this.m_parameterId = pid;
        this.m_length = length;
    }

    /**
     * Get the Parameter serialized size
     */
    @Override
    public short getSerializedSize() {
        return 4;
    }

    /**
     * Serializes a Parameter object
     */
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        impl.serializeI16(message, name, this.m_parameterId.getValue());
        impl.serializeI16(message, "", this.m_length);
    }

    /**
     * Deserializes a Parameter object (obly ParameterId and length)
     */
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.m_parameterId = ParameterId.createFromValue(impl.deserializeI16(message, name));
        this.m_length = impl.deserializeI16(message, "");
    }

    /**
     * Deserializes the Parameter content
     * 
     * @param impl The serializer to be used
     * @param message The BinaryInputStrem to read the data from
     * @param name The name of the serialized data
     * @throws IOException If an error should occur when deserializing
     */
    public abstract void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException;

    /**
     * Get the ParameterId
     * 
     * @return The ParameterId
     */
    public ParameterId getParameterId() {
        return this.m_parameterId;
    }
    
    /**
     * Get the Parameter length
     * 
     * @return The Parameter length
     */
    public short getParameterLength() {
        return this.m_length;
    }
    
    /**
     * Copies the content of a Parameter object
     * 
     * @param other The Parameter object to be copied
     */
    public void copy(Parameter other) {
        this.m_parameterId = other.m_parameterId;
        this.m_length = other.m_length;
    }

    

}
