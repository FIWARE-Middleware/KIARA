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
import org.fiware.kiara.serialization.impl.SerializerImpl;

/**
 * Class TimeBasedFilterQosPolicy, to indicate the Time Based Filter Qos. This
 * QosPolicy can be defined and is transmitted to the rest of the network but is
 * not implemented in this version. minimum_separation: Default value c_TimeZero
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class TimeBasedFilterQosPolicy extends Parameter {

    /**
     * {@link QosPolicy} acting as a parent class
     */
    public QosPolicy parent;

    /**
     * {@link Timestamp} indicating the minimum separation in time for the time based filter
     */
    public Timestamp minimumSeparation;

    /**
     * Default {@link TimeBasedFilterQosPolicy} constructor
     */
    public TimeBasedFilterQosPolicy() {
        super(ParameterId.PID_TIME_BASED_FILTER, Parameter.PARAMETER_KIND_LENGTH);
        this.parent = new QosPolicy(false);
        this.minimumSeparation = new Timestamp().timeZero();
    }

    /**
     * This method copies two instnces of {@link TimeBasedFilterQosPolicy}
     * @param value The {@link TimeBasedFilterQosPolicy} to be copied
     */
    public void copy(TimeBasedFilterQosPolicy value) {
        parent.copy(value.parent);
        minimumSeparation.copy(value.minimumSeparation);
    }

    /**
     * Deserializes only the contents of a {@link TimeBasedFilterQosPolicy} (not the {@link QosPolicy} contents)
     */
    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.minimumSeparation.deserialize(impl, message, name);
    }

}
