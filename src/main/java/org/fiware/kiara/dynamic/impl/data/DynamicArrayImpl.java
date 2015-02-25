package org.fiware.kiara.dynamic.impl.data;

import java.util.ArrayList;

import org.fiware.kiara.dynamic.DynamicArray;
import org.fiware.kiara.dynamic.DynamicData;
import org.fiware.kiara.dynamic.impl.DynamicTypeImpl;
import org.fiware.kiara.exceptions.DynamicTypeException;
import org.fiware.kiara.typecode.TypeDescriptor;
import org.fiware.kiara.typecode.impl.data.ArrayTypeDescriptor;

public class DynamicArrayImpl extends /*DynamicArray*/ DynamicContainerImpl implements DynamicArray {
    
    private int m_maxSize;
    private ArrayList<Integer> m_dimensions;
    //private DynamicData m_contentType

    public DynamicArrayImpl(ArrayTypeDescriptor typeDescriptor) {
        super(typeDescriptor, "DynamicArrayImpl");
        this.m_members = new ArrayList<DynamicData>();
        this.m_maxSize = typeDescriptor.getMaxSize();
        this.m_dimensions = new ArrayList<Integer>(typeDescriptor.getDimensions());
        this.initializeAray();
    }
    
    private void initializeAray() {
        int totalSize = 1;
        for(int dim : this.m_dimensions) {
            totalSize  = totalSize * dim;
        }
        this.m_maxSize = totalSize;
        this.m_members = new ArrayList<DynamicData>(totalSize);
    }
    
    @Override
    public DynamicTypeImpl getContentType() {
        return this.m_contentType;
    }
    
    @Override
    public void setContentType(DynamicDataImpl contentType) {
        if (contentType instanceof DynamicArrayImpl) {
            throw new DynamicTypeException(this.m_className + " - A DynamicArrayDataType object cannot be assigned as content to another DynamicArrayDataType.");
        }
        this.m_contentType = contentType;
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
        
        if (value.getClass() == this.m_contentType.getClass()) {
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
    
    public void addElement(DynamicDataImpl value) {
        this.m_members.add(value);
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
            ret = ret * this.m_dimensions.get(dimIndex);
        }
        return ret;
    }

}
