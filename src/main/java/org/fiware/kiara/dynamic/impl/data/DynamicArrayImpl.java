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

import org.fiware.kiara.dynamic.DynamicTypeBuilderImpl;
import org.fiware.kiara.dynamic.data.DynamicArray;
import org.fiware.kiara.dynamic.data.DynamicData;
import org.fiware.kiara.exceptions.DynamicTypeException;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.typecode.data.ArrayTypeDescriptor;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*
*/
public class DynamicArrayImpl extends DynamicContainerImpl implements DynamicArray {
    
    private int m_maxSize;
    private ArrayList<Integer> m_dimensions;
    
    public DynamicArrayImpl(ArrayTypeDescriptor arrayDescriptor) {
        super(arrayDescriptor, "DynamicArrayImpl");
        this.m_members = new ArrayList<DynamicData>();
        this.m_maxSize = arrayDescriptor.getMaxSize();
        this.m_dimensions = new ArrayList<Integer>(arrayDescriptor.getDimensions());
        this.initializeArray();
    }
    
    private void initializeArray() {
        int totalSize = 1;
        for(int dim : this.m_dimensions) {
            totalSize  = totalSize * dim;
        }
        this.m_maxSize = totalSize;
        this.m_members = new ArrayList<DynamicData>(totalSize);
    }
    
    @Override
    public boolean equals(Object anotherObject) {
        if (anotherObject instanceof DynamicArray) {
            if (((DynamicArray) anotherObject).getTypeDescriptor().getKind() == this.m_typeDescriptor.getKind()) {
                boolean isEquals = true;
                for (int i=0; i < ((ArrayTypeDescriptor) ((DynamicArrayImpl) anotherObject).getTypeDescriptor()).getMaxSize(); ++i) {
                    isEquals = isEquals & ((DynamicArrayImpl) anotherObject).m_members.get(i).equals(this.m_members.get(i));
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
    public DataTypeDescriptor getContentType() {
        return this.m_contentType;
    }
    
    @Override
    public void setContentType(DataTypeDescriptor dynamicData) {
        if (dynamicData instanceof ArrayTypeDescriptor) {
            throw new DynamicTypeException(this.m_className + " - A DynamicArrayDataType object cannot be assigned as content to another DynamicArrayDataType.");
        }
        this.m_contentType = dynamicData;
    }
    
    @Override
    public DynamicData getElementAt(int... position) {
        if (this.m_dimensions.size() != position.length) {
            throw new DynamicTypeException(this.m_className + " - The specified number of dimensions is different to the number dimensions of the array.");
        }
        
        if(!checkBoundaries(position)) {
            throw new DynamicTypeException(this.m_className + " - The specified location of the data is not inside the boundaries of the array definition.");
        }
        
        int accessIndex = calculateAccessIndex(position);
        
        return this.m_members.get(accessIndex);
    }

    @Override
    public boolean setElementAt(DynamicData value, int... position) {
        if (this.m_dimensions.size() != position.length) {
            throw new DynamicTypeException(this.m_className + " - The specified number of dimensions is different to the number dimensions of the array.");
        }
        
        if(!checkBoundaries(position)) {
            throw new DynamicTypeException(this.m_className + " - The specified location of the data is not inside the boundaries of the array definition.");
        }
        
        if (value.getTypeDescriptor().getKind() == this.m_contentType.getKind()) {
            int accessIndex = calculateAccessIndex(position);
            if (this.m_members.size() != this.m_maxSize) {
                this.m_members.add(accessIndex, value);
                return true;
            } else {
                return (this.m_members.set(accessIndex, value) != null);
            }
        }
        
        return false;
    }
    
   private boolean checkBoundaries(int... position) {
        for (int i=0; i < position.length; ++i) {
            int declaredDim = this.m_dimensions.get(i);
            if (position[i] >= declaredDim) {
                return false;
            }
        }
        return true;
    }
    
    public void addElement(DynamicData dynamicData) {
        this.m_members.add(dynamicData);
    }
    
    private int calculateAccessIndex(int... coordinates) {
        int index = 0;
        int dimIndex = 1;
        for (Integer coord : coordinates) {
           index = index + coord * multiplyDimensions(dimIndex);
           dimIndex++;
        }
        return index;
    }
    
    private int multiplyDimensions(int dimIndex) {
        int ret = 1;
        for (int i = dimIndex; i < this.m_dimensions.size(); ++i) {
            ret = ret * this.m_dimensions.get(i);
        }
        return ret;
    }
    
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        for (DynamicData member : this.m_members) {
            member.serialize(impl, message, "");
        }
    }

    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        for (DynamicData member : this.m_members) {
            member.deserialize(impl, message, "");
        }
    }
   
}
