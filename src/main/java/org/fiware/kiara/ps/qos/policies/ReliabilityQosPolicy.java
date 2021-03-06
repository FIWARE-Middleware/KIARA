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
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

/**
 * Class ReliabilityQosPolicy, to indicate the reliability of the endpoints.
 * kind: Default value BEST_EFFORT_RELIABILITY_QOS for ReaderQos and
 * RELIABLE_RELIABILITY_QOS for WriterQos. max_blocking_time: Not Used in this
 * version.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class ReliabilityQosPolicy extends Parameter {

    /**
     * {@link QosPolicy} acting as a parent class
     */
    public QosPolicy parent;

    /**
     * {@link ReliabilityQosPolicyKind} indicating the type of Reliability QoS
     */
    public ReliabilityQosPolicyKind kind;

    /**
     * {@link Timestamp} indicating how much is the blocking time
     */
    public Timestamp maxBlockingTime;

    /**
     * Default {@link ReliabilityQosPolicy} constructor
     */
    public ReliabilityQosPolicy() {
        super(ParameterId.PID_RELIABILITY, (short) (Parameter.PARAMETER_KIND_LENGTH + Parameter.PARAMETER_KIND_LENGTH));
        this.parent = new QosPolicy(true); // indicate send always
        this.kind = ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS;
        this.maxBlockingTime = new Timestamp();
    }

    /**
     * This method copies two instnces of {@link ReliabilityQosPolicy}
     * @param value The {@link ReliabilityQosPolicy} to be copied
     */
    public void copy(ReliabilityQosPolicy value) {
        parent.copy(value.parent);
        kind = value.kind;
        maxBlockingTime.copy(value.maxBlockingTime);
    }

    /**
     * Serializes a {@link ReliabilityQosPolicy}
     */
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        super.serialize(impl, message, name);
        impl.serializeUI32(message, name, this.kind.getValue());
        this.maxBlockingTime.serialize(impl, message, name);
    }

    /**
     * Deserializes a {@link ReliabilityQosPolicy} and its parent {@link QosPolicy}
     */
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        super.deserialize(impl, message, name);
        this.kind = ReliabilityQosPolicyKind.getFromValue((byte) impl.deserializeUI32(message, name));
        this.maxBlockingTime.deserialize(impl, message, name);
    }

    /**
     * Deserializes only the contents of a {@link ReliabilityQosPolicy} (not the {@link QosPolicy} contents)
     */
    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.kind = ReliabilityQosPolicyKind.getFromValue((byte) impl.deserializeUI32(message, name));
        this.maxBlockingTime.deserialize(impl, message, name);
    }
}
