package org.fiware.kiara.dynamic.impl.data;

import java.util.ArrayList;

import org.fiware.kiara.dynamic.data.DynamicArray;
import org.fiware.kiara.dynamic.data.DynamicData;
import org.fiware.kiara.dynamic.data.DynamicPrimitive;
import org.fiware.kiara.dynamic.impl.DynamicTypeImpl;
import org.fiware.kiara.dynamic.impl.data.visitor.Visitor;
import org.fiware.kiara.exceptions.DynamicTypeException;
import org.fiware.kiara.typecode.TypeDescriptor;
import org.fiware.kiara.typecode.data.ArrayTypeDescriptor;
import org.fiware.kiara.typecode.impl.data.ArrayTypeDescriptorImpl;

public class DynamicArrayImpl extends DynamicContainerImpl implements DynamicArray {
    
    private int m_maxSize;
    private ArrayList<Integer> m_dimensions;
    private int temporalAccessIndex = -1;
    
    public DynamicArrayImpl(ArrayTypeDescriptor arrayDescriptor) {
        super(arrayDescriptor, "DynamicArrayImpl");
        this.m_members = new ArrayList<DynamicData>();
        this.m_maxSize = arrayDescriptor.getMaxSize();
        this.m_dimensions = new ArrayList<Integer>(arrayDescriptor.getDimensions());
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
    public boolean equals(Object anotherObject) {
        if (anotherObject instanceof DynamicArray) {
            if (((DynamicArray) anotherObject).getTypeDescriptor().getKind() == this.m_typeDescriptor.getKind()) {
                boolean isEquals = true;
                for (int i=0; i < ((ArrayTypeDescriptor) ((DynamicArrayImpl) anotherObject).getTypeDescriptor()).getMaxSize(); ++i) {
                    isEquals = isEquals & ((DynamicArrayImpl) anotherObject).m_members.get(i).equals(this.m_members.get(i));
                }
                return isEquals;
            }
        }
        return false;
    }
    
    @Override
    public DynamicData getContentType() {
        return this.m_contentType;
    }
    
    @Override
    public void setContentType(DynamicData dynamicData) {
        if (dynamicData instanceof DynamicArrayImpl) {
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
        
        this.temporalAccessIndex = accessIndex;
        
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
                if (this.m_visitor != null) {
                    (this.m_visitor).notify(this, value);
                } else {
                    return (this.m_members.set(accessIndex, value) != null);
                }
            }
        }
        
        return false;
    }
    
    @Override
    public void visit(Object... params) {
        DynamicDataImpl value = (DynamicDataImpl) params[0];
        
        value.visit(trimParams(params));
        
        //int accessIndex = (int) params[1];
        
        //this.m_members.set(accessIndex, value);
    }
    
    @Override
    public boolean notify(DynamicDataImpl value, Object... params) { // value = value that changes (this); params: the parameters to be used later in visit function
        if (this.m_visitor != null) {
            if (temporalAccessIndex != -1) {
                this.m_visitor.notify(this, appendParams(value, params));
                temporalAccessIndex = -1;
            }
        } else {
            value.visit(params);
        }
        
        return true;
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
        //System.out.println("LinearIndex:  " + index);
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
    public void registerVisitor(Visitor visitor) {
        super.registerVisitor(visitor);
        for (int i=0; i < this.m_members.size(); ++i) {
            ((DynamicDataImpl) this.m_members.get(i)).registerVisitor(this);
        }
    }

}
