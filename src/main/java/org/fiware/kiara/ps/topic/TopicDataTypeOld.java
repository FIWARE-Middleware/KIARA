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
package org.fiware.kiara.ps.topic;

import java.io.IOException;

import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.Serializable;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public abstract class TopicDataTypeOld implements Serializable {
    
    public int typeSize;
    
    public boolean isKeyDefined;
    
    private String m_topicDataTypeName;
    
    public TopicDataTypeOld() {
        this.typeSize = 0;
        this.isKeyDefined = false;
    }
    
    public abstract void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException;

    public abstract void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException;
    
    public abstract Object createData();
    
    public abstract void deleteData();
    
    public abstract InstanceHandle getKey();
    
    public void setName(String name) {
        this.m_topicDataTypeName = name;
    }
    
    public String getName() {
        return this.m_topicDataTypeName;
    }

}
