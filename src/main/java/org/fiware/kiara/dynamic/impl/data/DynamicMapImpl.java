/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2014 Proyectos y Sistemas de Mantenimiento S.L. (eProsima)
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
package org.fiware.kiara.dynamic.impl.data;

import java.io.IOException;
import java.util.ArrayList;

import org.fiware.kiara.Kiara;
import org.fiware.kiara.dynamic.data.DynamicData;
import org.fiware.kiara.dynamic.data.DynamicMap;
import org.fiware.kiara.dynamic.impl.DynamicValueBuilderImpl;
import org.fiware.kiara.exceptions.DynamicTypeException;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.data.MapTypeDescriptor;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*
*/
public class DynamicMapImpl extends DynamicContainerImpl implements DynamicMap {
    
    private int m_maxSize;
    private ArrayList<DynamicData> m_keyMembers;
    private DataTypeDescriptor m_keyContentType;
    
    public DynamicMapImpl(MapTypeDescriptor mapDescriptor) {
        super(mapDescriptor, "DynamicMapImpl");
        this.m_maxSize = mapDescriptor.getMaxSize();
        this.m_keyMembers = new ArrayList<DynamicData>(this.m_maxSize);
        this.m_members = new ArrayList<DynamicData>(this.m_maxSize);
    }
    
    @Override
    public boolean put(DynamicData key, DynamicData value) {
        if (key.getTypeDescriptor().getKind() == this.m_keyContentType.getKind()) {
            if (value.getTypeDescriptor().getKind() == this.m_contentType.getKind()) {
                if (this.m_members.size() != this.m_maxSize) {
                    if (!this.existsInMap(key)) {
                        this.m_keyMembers.add(key);
                        this.m_members.add(value);
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    throw new DynamicTypeException(this.m_className + " Element cannot be added. The maximum size specified for this map has been reached.");
                }
            } else {
                throw new DynamicTypeException(this.m_className + " Element cannot be added. The value type is not the same specified in the value content type.");
            }
        } else {
            throw new DynamicTypeException(this.m_className + " Element cannot be added. The key type is not the same specified in the key content type.");
        }
    }

    @Override
    public boolean containsKey(DynamicData key) {
        return this.existsInMap(key);
    }

    @Override
    public boolean containsValue(DynamicData value) {
        for (int i=0; i < this.m_members.size(); ++i) {
            if (this.m_members.get(i).equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public DynamicData get(DynamicData key) {
        if (key.getTypeDescriptor().getKind() != this.m_keyContentType.getKind()) {
            throw new DynamicTypeException(this.m_className + " The key type specified (" + key.getTypeDescriptor().getKind() + ") is not the same as the one defined in the map descriptor.");
        }
        
        for (int i=0; i < this.m_members.size(); ++i) {
            if (this.m_keyMembers.get(i).equals(key)) {
                return this.m_members.get(i);
            }
        }
        
        return null;
    }
    
    @Override
    public boolean equals(Object anotherObject) {
        if (anotherObject instanceof DynamicMap) {
            if (((DynamicMap) anotherObject).getTypeDescriptor().getKind() == this.m_typeDescriptor.getKind()) {
                boolean isEquals = true;
                for (int i=0; i < ((MapTypeDescriptor) ((DynamicMapImpl) anotherObject).getTypeDescriptor()).getMaxSize(); ++i) {
                    isEquals = isEquals & ((DynamicMapImpl) anotherObject).m_keyMembers.get(i).equals(this.m_keyMembers.get(i));
                    isEquals = isEquals & ((DynamicMapImpl) anotherObject).m_members.get(i).equals(this.m_members.get(i));
                    if (!isEquals) {
                        return isEquals;
                    }
                }
                return isEquals;
            }
        }
        return false;
    }
    
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        impl.serializeUI32(message, "", this.m_members.size());
        
        for (int i=0; i < this.m_members.size(); ++i) {
            this.m_keyMembers.get(i).serialize(impl, message, "");
            this.m_members.get(i).serialize(impl, message, "");
        }
    }

    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        int size = impl.deserializeUI32(message, "");
        
        for (int i=0; i < size; ++i) {
            DynamicData key = Kiara.getDynamicValueBuilder().createData(this.m_keyContentType);
            DynamicData value = Kiara.getDynamicValueBuilder().createData(this.m_contentType);
            key.deserialize(impl, message, "");
            value.deserialize(impl, message, "");
            this.m_keyMembers.add(key);
            this.m_members.add(value);
        }
    }

    private boolean existsInMap(DynamicData value) {
        for (int i=0; i < this.m_keyMembers.size(); ++i) {
            if (this.m_keyMembers.get(i).equals(value)) {
                return true;
            }
        }
        return false;
    }
    
    public DataTypeDescriptor getKeyContentType() {
        return m_keyContentType;
    }

    public void setKeyContentType(DataTypeDescriptor keyContentType) {
        this.m_keyContentType = keyContentType;
    }

    public DataTypeDescriptor getValueContentType() {
        return m_contentType;
    }

    public void setValueContentType(DataTypeDescriptor valueContentType) {
        this.m_contentType = valueContentType;
    }

    

    

}
