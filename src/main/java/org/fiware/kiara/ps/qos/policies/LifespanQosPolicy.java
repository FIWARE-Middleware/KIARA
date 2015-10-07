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
 * Class LifespanQosPolicy. This QosPolicy can be defined and is transmitted to
 * the rest of the network but is not implemented in this version. duration:
 * Default value c_TimeInfinite.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class LifespanQosPolicy extends Parameter {

    /**
     * {@link QosPolicy} acting as a parent class
     */
    public QosPolicy parent;

    /**
     * {@link Timestamp} indicating the duration of the Lifespan
     */
    public final Timestamp duration;

    /**
     * Default {@link LifespanQosPolicy} constructor
     */
    public LifespanQosPolicy() {
        super(ParameterId.PID_LIFESPAN, Parameter.PARAMETER_TIME_LENGTH);
        this.parent = new QosPolicy(true);
        this.duration = new Timestamp().timeInfinite();
    }

    /**
     * This method copies two instnces of {@link LifespanQosPolicy}
     * @param value The {@link LifespanQosPolicy} to be copied
     */
    public void copy(LifespanQosPolicy value) {
        parent.copy(value.parent);
        duration.copy(value.duration);
    }

    /**
     * Serializes a {@link LifespanQosPolicy}
     */
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        super.serialize(impl, message, name);
        this.duration.serialize(impl, message, name);
    }

    /**
     * Deserializes a {@link LifespanQosPolicy} and its parent {@link QosPolicy}
     */
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        super.deserialize(impl, message, name);
        this.duration.deserialize(impl, message, name);
    }

    /**
     * Deserializes only the contents of a {@link LifespanQosPolicy} (not the {@link QosPolicy} contents)
     */
    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.duration.deserialize(impl, message, name);
    }

}
