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
import org.fiware.kiara.ps.rtps.builtin.discovery.endpoint.config.LivelinessQos;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

/**
 * Class LivelinessQosPolicy, to indicate the Liveliness of the Writers. This
 * QosPolicy can be defined for the Subscribers and is transmitted but only the
 * Writer Liveliness protocol is implemented in this version. The user should
 * set the lease_duration and the announcement_period with values that differ in
 * at least 30%. Values too close to each other may cause the failure of the
 * writer liveliness assertion in networks with high latency or with lots of
 * communication errors. kind: Default value AUTOMATIC_LIVELINESS_QOS
 * lease_duration: Default value c_TimeInfinite. announcement_period: Default
 * value c_TimeInfinite (must be lower than lease_duration).
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class LivelinessQosPolicy extends Parameter {

    /**
     * {@link QosPolicy} acting as a parent class
     */
    public QosPolicy parent;

    /**
     * {@link LivelinessQosPolicyKind} indicating the type of {@link LivelinessQos}
     */
    public LivelinessQosPolicyKind kind;

    /**
     * {@link Timestamp} indicating the lease duration of the writers
     */
    public Timestamp leaseDuration;

    /**
     * {@link Timestamp} indicating the liveliness announcement period 
     */
    public Timestamp announcementPeriod;

    /**
     * Default {@link LivelinessQosPolicy} constructor
     */
    public LivelinessQosPolicy() {
        super(ParameterId.PID_LIVELINESS, (short) (Parameter.PARAMETER_KIND_LENGTH + Parameter.PARAMETER_LIVELINESS_QOS_LENGTH));
        this.parent = new QosPolicy(true);
        this.kind = LivelinessQosPolicyKind.AUTOMATIC_LIVELINESS_QOS;
        this.leaseDuration = new Timestamp().timeInfinite();
        this.announcementPeriod = new Timestamp().timeInfinite();
    }

    /**
     * This method copies two instnces of {@link LivelinessQosPolicy}
     * @param value The {@link LivelinessQosPolicy} to be copied
     */
    public void copy(LivelinessQosPolicy value) {
        parent.copy(value.parent);
        kind = value.kind;
        leaseDuration.copy(value.leaseDuration);
        announcementPeriod.copy(value.announcementPeriod);
    }

    /**
     * Serializes a {@link LivelinessQosPolicy}
     */
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        super.serialize(impl, message, name);
        impl.serializeUI32(message, name, this.kind.getValue());
        this.leaseDuration.serialize(impl, message, name);
    }

    /**
     * Deserializes a {@link LivelinessQosPolicy} and its parent {@link QosPolicy}
     */
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        super.deserialize(impl, message, name);
        this.kind = LivelinessQosPolicyKind.fromValue((byte) impl.deserializeUI32(message, name));
        this.leaseDuration.deserialize(impl, message, name);
    }

    /**
     * Deserializes only the contents of a {@link LivelinessQosPolicy} (not the {@link QosPolicy} contents)
     */
    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.kind = LivelinessQosPolicyKind.fromValue((byte) impl.deserializeUI32(message, name));
        this.leaseDuration.deserialize(impl, message, name);
    }
    
    
}
