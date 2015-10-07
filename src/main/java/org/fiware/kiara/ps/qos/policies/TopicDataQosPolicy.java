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
import java.util.ArrayList;
import java.util.List;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

/**
 * Class TopicDataQosPolicy, to indicate the Topic Data.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class TopicDataQosPolicy extends Parameter {

    /**
     * {@link QosPolicy} acting as a parent class
     */
    public QosPolicy parent;

    /**
     * {@link TopicDataQosPolicy} byte array value
     */
    private final List<Byte> m_value;

    /**
     * Default {@link TopicDataQosPolicy} constructor
     */
    public TopicDataQosPolicy() {
        super(ParameterId.PID_TOPIC_DATA, (short) 0);
        this.parent = new QosPolicy(false);
        this.m_value = new ArrayList<>();
    }

    /**
     * Adds a new value at the end of the byte array
     * @param value The new value to be added
     */
    public void pushBack(byte value) {
        this.m_value.add(value);
    }

    /**
     * Clears the content of the byte array
     */
    public void clear() {
        this.m_value.clear();
    }

    /**
     * This method copies two instnces of {@link TopicDataQosPolicy}
     * @param value The {@link TopicDataQosPolicy} to be copied
     */
    public void copy(TopicDataQosPolicy value) {
        parent.copy(value.parent);
        m_value.clear();
    }

    /**
     * Set the byte array value
     * 
     * @param value The value to be set
     */
    public void setValue(List<Byte> value) {
        this.m_value.clear();
        this.m_value.addAll(value);
    }

    /**
     * Get the byte array value
     * 
     * @return The byte array value
     */
    public List<Byte> getValue() {
        return this.m_value;
    }

    /**
     * Deserializes only the contents of a {@link TopicDataQosPolicy} (not the {@link QosPolicy} contents)
     */
    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        // Do nothing

    }

}
