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
 * Class DurabilityServiceQosPolicy, to indicate the Durability Service. This
 * QosPolicy can be defined and is transmitted to the rest of the network but is
 * not implemented in this version. service_cleanup_delay: Default value
 * c_TimeZero. history_kind: Default value KEEP_LAST_HISTORY_QOS. history_depth:
 * Default value 1. max_samples: Default value -1. max_instances: Default value
 * -1. max_samples_per_instance: Default value -1.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class DurabilityServiceQosPolicy extends Parameter {

    /**
     * {@link QosPolicy} acting as a parent class
     */
    public QosPolicy parent;

    /**
     * {@link Timestamp} indicating the service cleanup delay
     */
    public Timestamp serviceCleanupDelay;

    /**
     * {@link HistoryQosPolicyKind} indicating the type of QoS policy
     */
    public HistoryQosPolicyKind kind;

    /**
     * Integer value which indicated ow mamy objects can be stored in the HistoryCache
     */
    public int historyDepth;

    /**
     * Maximum number of samples of any instance
     */
    public int maxSamples;

    /**
     * Maximum number of samples of a different instance (different KEY)
     */
    public int maxInstances;

    /**
     * Maximum number of samples of the same instance (same KEY)
     */
    public int maxSamplesPerInstance;

    /**
     * Default {@link DurabilityServiceQosPolicy} constructor
     */
    public DurabilityServiceQosPolicy() {
        super(ParameterId.PID_DURABILITY_SERVICE, (short) (Parameter.PARAMETER_KIND_LENGTH + Parameter.PARAMETER_KIND_LENGTH + 4 + 4 + 4 + 4));
        this.parent = new QosPolicy(false);
        this.serviceCleanupDelay = new Timestamp().timeZero();
        this.kind = HistoryQosPolicyKind.KEEP_LAST_HISTORY_QOS;
        this.historyDepth = 1;
        this.maxSamples = -1;
        this.maxInstances = -1;
        this.maxSamplesPerInstance = -1;
    }

    /**
     * This method copies two instnces of {@link DurabilityServiceQosPolicy}
     * @param value The {@link DurabilityServiceQosPolicy} to be copied
     */
    public void copy(DurabilityServiceQosPolicy value) {
        parent.copy(value.parent);
        serviceCleanupDelay.copy(value.serviceCleanupDelay);
        kind = value.kind;
        historyDepth = value.historyDepth;
        maxSamples = value.maxSamples;
        maxInstances = value.maxInstances;
        maxSamplesPerInstance = value.maxSamplesPerInstance;
    }

    /**
     * Serializes a {@link DestinationOrderQosPolicy}
     */
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        super.serialize(impl, message, name);

        this.serviceCleanupDelay.serialize(impl, message, name);

        impl.serializeByte(message, name, this.kind.getValue());
        impl.serializeByte(message, name, (byte) 0);
        impl.serializeByte(message, name, (byte) 0);
        impl.serializeByte(message, name, (byte) 0);

        impl.serializeI32(message, name, this.historyDepth);
        impl.serializeI32(message, name, this.maxSamples);
        impl.serializeI32(message, name, this.maxInstances);
        impl.serializeI32(message, name, this.maxSamplesPerInstance);

    }

    /**
     * Deserializes a {@link DestinationOrderQosPolicy} and its parent {@link QosPolicy}
     */
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        super.deserialize(impl, message, name);

        this.serviceCleanupDelay.deserialize(impl, message, name);

        this.kind = HistoryQosPolicyKind.values()[impl.deserializeByte(message, name)];
        message.skipBytes(3);

        this.historyDepth = impl.deserializeI32(message, name);
        this.maxSamples = impl.deserializeI32(message, name);
        this.maxInstances = impl.deserializeI32(message, name);
        this.maxSamplesPerInstance = impl.deserializeI32(message, name);
    }

    /**
     * Deserializes only the contents of a {@link DestinationOrderQosPolicy} (not the {@link QosPolicy} contents)
     */
    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.serviceCleanupDelay.deserialize(impl, message, name);

        this.kind = HistoryQosPolicyKind.values()[impl.deserializeByte(message, name)];
        message.skipBytes(3);

        this.historyDepth = impl.deserializeI32(message, name);
        this.maxSamples = impl.deserializeI32(message, name);
        this.maxInstances = impl.deserializeI32(message, name);
        this.maxSamplesPerInstance = impl.deserializeI32(message, name);
    }

}
