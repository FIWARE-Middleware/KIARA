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

public class GroupDataQosPolicy extends Parameter {
    
    // TODO
    
public QosPolicy parent;
    
    private List<Byte> m_value;
    
    public GroupDataQosPolicy() {
        super(ParameterId.PID_GROUP_DATA, (short) 0);
        this.parent = new QosPolicy(false);
        this.m_value = new ArrayList<Byte>();
    }
    
    public void pushBack(byte b) {
        this.m_value.add(b);
    }
    
    public void clear() {
        this.m_value.clear();
    }
    
    public void setValue(List<Byte> value) {
        this.m_value = value;
    }
    
    public List<Byte> getValue() {
        return this.m_value;
    }

    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        // Do nothing
        
    }

}
