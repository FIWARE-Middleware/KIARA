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
package org.fiware.kiara.ps.qos.policies;

import java.io.IOException;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

/**
 * Class OwnershipStrengthQosPolicy, to indicate the strength of the ownership.
 * value: Default value 0.
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public class OwnershipStrengthQosPolicy extends Parameter {

    /**
     * {@link QosPolicy} acting as a parent class
     */
    public QosPolicy parent;
    
    /**
     * {@link OwnershipStrengthQosPolicy} value
     */
    public int value;

    /**
     * Default {@link OwnershipStrengthQosPolicy} constructor
     */
    public OwnershipStrengthQosPolicy() {
        super(ParameterId.PID_OWNERSHIP_STRENGTH, (short) 4);
        this.parent = new QosPolicy(false);
        this.value = 0;
    }

    /**
     * This method copies two instnces of {@link OwnershipStrengthQosPolicy}
     * @param other The {@link OwnershipStrengthQosPolicy} to be copied
     */
    public void copy(OwnershipStrengthQosPolicy other) {
        parent.copy(other.parent);
        value = other.value;
    }

    /**
     * Serializes a {@link OwnershipStrengthQosPolicy}
     */
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        super.serialize(impl, message, name);
        impl.serializeUI32(message, name, this.value);
    }

    /**
     * Deserializes a {@link OwnershipStrengthQosPolicy} and its parent {@link QosPolicy}
     */
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        super.deserialize(impl, message, name);
        this.value = impl.deserializeUI32(message, name);
    }

    /**
     * Deserializes only the contents of a {@link OwnershipStrengthQosPolicy} (not the {@link QosPolicy} contents)
     */
    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.value = impl.deserializeUI32(message, name);
    }
    
    
}
