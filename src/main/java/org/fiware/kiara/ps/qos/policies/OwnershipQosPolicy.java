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
 * Class OwnershipQosPolicy, to indicate the ownership kind of the endpoints.
 * kind: Default value SHARED_OWNERSHIP_QOS.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class OwnershipQosPolicy extends Parameter {

    /**
     * {@link QosPolicy} acting as a parent class
     */
    public QosPolicy parent;

    /**
     * {@link OwnershipQosPolicyKind} idnicating the type of Ownership
     */
    public OwnershipQosPolicyKind kind;

    /**
     * Default {@link OwnershipQosPolicy} constructor
     */
    public OwnershipQosPolicy() {
        super(ParameterId.PID_OWNERSHIP, Parameter.PARAMETER_KIND_LENGTH);
        this.parent = new QosPolicy(true);
        this.kind = OwnershipQosPolicyKind.SHARED_OWNERSHIP_QOS;
    }

    /**
     * This method copies two instnces of {@link OwnershipQosPolicy}
     * @param value The {@link OwnershipQosPolicy} to be copied
     */
    public void copy(OwnershipQosPolicy value) {
        parent.copy(value.parent);
        kind = value.kind;
    }

    /**
     * Serializes a {@link OwnershipQosPolicy}
     */
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        super.serialize(impl, message, name);
        impl.serializeUI32(message, name, this.kind.getValue());
    }

    /**
     * Deserializes a {@link OwnershipQosPolicy} and its parent {@link QosPolicy}
     */
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        super.deserialize(impl, message, name);
        this.kind = OwnershipQosPolicyKind.getFromValue((byte) impl.deserializeUI32(message, name)); 
    }

    /**
     * Deserializes only the contents of a {@link OwnershipQosPolicy} (not the {@link QosPolicy} contents)
     */
    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.kind = OwnershipQosPolicyKind.getFromValue((byte) impl.deserializeUI32(message, name));
    }

}
