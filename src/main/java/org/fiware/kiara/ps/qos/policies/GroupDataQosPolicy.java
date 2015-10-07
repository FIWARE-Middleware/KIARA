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
 * Class GroupDataQosPolicy, to indicate the Group Data.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class GroupDataQosPolicy extends Parameter {

    /**
     * {@link QosPolicy} acting as a parent class
     */
    public QosPolicy parent;

    /**
     * {@link GroupDataQosPolicy} value
     */
    private List<Byte> m_value;

    /**
     * Default {@link GroupDataQosPolicy} constructor
     */
    public GroupDataQosPolicy() {
        super(ParameterId.PID_GROUP_DATA, (short) 0);
        this.parent = new QosPolicy(false);
        this.m_value = new ArrayList<Byte>();
    }

    /**
     * Adds a new boolean value to the {@link HistoryQosPolicy} value
     * 
     * @param value The value to be added
     */
    public void pushBack(byte value) {
        this.m_value.add(value);
    }

    /**
     * Clears the {@link GroupDataQosPolicy} value
     */
    public void clear() {
        this.m_value.clear();
    }

    /**
     * Set the {@link GroupDataQosPolicy} value
     * 
     * @param value The new value to be set
     */
    public void setValue(List<Byte> value) {
        this.m_value.clear();
        this.m_value.addAll(value);
    }

    /**
     * Get the {@link GroupDataQosPolicy} value
     * 
     * @return The {@link GroupDataQosPolicy} value
     */
    public List<Byte> getValue() {
        return this.m_value;
    }

    /**
     * This method copies two instnces of {@link GroupDataQosPolicy}
     * @param value The {@link GroupDataQosPolicy} to be copied
     */
    public void copy(GroupDataQosPolicy value) {
        parent.copy(value.parent);
        setValue(value.m_value);
    }

    /**
     * Deserializes the {@link GroupDataQosPolicy} value
     */
    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        // Not implemented
    }

}
