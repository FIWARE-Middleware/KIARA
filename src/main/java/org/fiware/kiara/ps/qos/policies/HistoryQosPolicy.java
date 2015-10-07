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
import org.fiware.kiara.ps.rtps.history.HistoryCache;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

/**
 * Class HistoryQosPolicy, defines the HistoryQos of the topic in the Writer or
 * Reader side. kind: Default value KEEP_LAST_HISTORY_QOS. depth: Default value
 * 1000.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class HistoryQosPolicy extends Parameter {

    /**
     * {@link QosPolicy} acting as a parent class
     */
    public QosPolicy parent;

    /**
     * {@link HistoryQosPolicyKind} indicating the type of {@link HistoryCache} QoS policy
     */
    public HistoryQosPolicyKind kind;

    /**
     * Number of samples that can be stored in a {@link HistoryCache}
     */
    public int depth;

    /**
     * Default {@link HistoryQosPolicy} constructor
     */
    public HistoryQosPolicy() {
        super(ParameterId.PID_HISTORY, (short) (Parameter.PARAMETER_KIND_LENGTH + 4));
        this.parent = new QosPolicy(true);
        this.kind = HistoryQosPolicyKind.KEEP_LAST_HISTORY_QOS;
        this.depth = 1000;
    }

    /**
     * Serializes a {@link DestinationOrderQosPolicy}
     */
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        super.serialize(impl, message, name);
        impl.serializeByte(message, name, this.kind.getValue());
        impl.serializeByte(message, name, (byte) 0);
        impl.serializeByte(message, name, (byte) 0);
        impl.serializeByte(message, name, (byte) 0);
        impl.serializeI32(message, name, this.depth);
    }

    /**
     * Deserializes a {@link DestinationOrderQosPolicy} and its parent {@link QosPolicy}
     */
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        super.deserialize(impl, message, name);
        this.kind = HistoryQosPolicyKind.values()[impl.deserializeByte(message, name)];
        message.skipBytes(3);
        this.depth = impl.deserializeI32(message, name);
    }

    /**
     * Deserializes only the contents of a {@link DestinationOrderQosPolicy} (not the {@link QosPolicy} contents)
     */
    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.kind = HistoryQosPolicyKind.values()[impl.deserializeByte(message, name)];
        message.skipBytes(3);
        this.depth = impl.deserializeI32(message, name);
    }

}
