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
import org.fiware.kiara.dynamic.data.DynamicSet;
import org.fiware.kiara.exceptions.DynamicTypeException;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.typecode.data.ContainerTypeDescriptor;
import org.fiware.kiara.typecode.data.SetTypeDescriptor;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*
*/
public class DynamicSetImpl extends DynamicContainerImpl implements DynamicSet {

    private int m_maxSize;
    
    public DynamicSetImpl(SetTypeDescriptor dataDescriptor) {
        super(dataDescriptor, "DynamicSetImpl");
        this.m_maxSize = dataDescriptor.getMaxSize();
        this.m_members = new ArrayList<DynamicData>(this.m_maxSize); 
    }
    
    @Override
    public boolean add(DynamicData element) {
        if (element.getTypeDescriptor().getKind() == this.m_contentType.getKind()) {
            if (this.m_members.size() != this.m_maxSize) {
                if (!existsInSet(element)) {
                    this.m_members.add(element);
                    return true;
                } else {
                    return false;
                }
            } else {
                throw new DynamicTypeException(this.m_className + " Element cannot be added. The maximum size specified for this array has been reached.");
            }
        } else {
            throw new DynamicTypeException(this.m_className + " Element cannot be added. The element's type does not fit with the specified content Type for this set.");
        }
    }

    @Override
    public void add(int index, DynamicData element) {
        if (index >= this.m_maxSize && this.m_maxSize != ContainerTypeDescriptor.UNBOUNDED) {
            throw new DynamicTypeException(this.m_className + " The index specified (" + index + ") is out of the set boundaries (" + this.m_maxSize + ")."); 
        }
        
        if (element.getClass().equals(this.m_contentType.getClass())) {
            if (this.m_members.size() != this.m_maxSize) {
                if (!existsInSet(element)) {
                    this.m_members.add(index, element);
                }
            } else {
                throw new DynamicTypeException(this.m_className + " Element cannot be added. The maximum size specified for this set has been reached.");
            }
        }
    }

    @Override
    public DynamicData get(int index) {
        if (index >= this.m_maxSize && this.m_maxSize != ContainerTypeDescriptor.UNBOUNDED) {
            throw new DynamicTypeException(this.m_className + " The index specified (" + index + ") is greater than the maximum size of this set (" + this.m_maxSize + ")."); 
        } else if (index >= this.m_members.size()){
            throw new DynamicTypeException(this.m_className + " The index specified (" + index + ") is out of the set boundaries (" + this.m_maxSize + ").");
        }
        
        return this.m_members.get(index);
    }

    @Override
    public boolean isEmpty() {
        return this.m_members.isEmpty();
    }
    
    @Override
    public boolean equals(Object anotherObject) {
        if (anotherObject instanceof DynamicSet) {
            if (((DynamicSet) anotherObject).getTypeDescriptor().getKind() == this.m_typeDescriptor.getKind()) {
                boolean isEquals = true;
                for (int i=0; i < ((SetTypeDescriptor) ((DynamicSetImpl) anotherObject).getTypeDescriptor()).getMaxSize(); ++i) {
                    isEquals = isEquals & ((DynamicSetImpl) anotherObject).m_members.get(i).equals(this.m_members.get(i));
                    if (!isEquals) {
                        return isEquals;
                    }
                }
                return isEquals;
            }
        }
        return false;
    }
    
    
    
    private boolean existsInSet(DynamicData value) {
        for (int i=0; i < this.m_members.size(); ++i) {
            if (this.m_members.get(i).equals(value)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        impl.serializeUI32(message, "", this.m_members.size());
        
        for (DynamicData member : this.m_members) {
            member.serialize(impl, message, "");
        }
    }

    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        int size = impl.deserializeUI32(message, "");
        
        for (int i=0; i < size; ++i) {
            DynamicData member = Kiara.getDynamicValueBuilder().createData(this.m_contentType);
            member.deserialize(impl, message, "");
            this.m_members.add(member);
        }
    }
    
    
}
