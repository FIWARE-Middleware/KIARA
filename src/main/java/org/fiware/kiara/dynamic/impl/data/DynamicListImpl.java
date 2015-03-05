package org.fiware.kiara.dynamic.impl.data;

import java.util.ArrayList;

import org.fiware.kiara.dynamic.data.DynamicData;
import org.fiware.kiara.dynamic.data.DynamicList;
import org.fiware.kiara.exceptions.DynamicTypeException;
import org.fiware.kiara.typecode.data.ListTypeDescriptor;

public class DynamicListImpl extends DynamicContainerImpl implements DynamicList {
    
    private int m_maxSize;

    public DynamicListImpl(ListTypeDescriptor dataDescriptor) {
        super(dataDescriptor, "DynamicListImpl");
        this.m_maxSize = dataDescriptor.getMaxSize();
        this.m_members = new ArrayList<DynamicData>(this.m_maxSize);
    }
    
    @Override
    public boolean add(DynamicData element) {
        if (element.getClass() == this.m_contentType.getClass()) {
            if (this.m_members.size() != this.m_maxSize) {
                this.m_members.add(element);
                return true;
            } else {
                throw new DynamicTypeException(this.m_className + " Element cannot be added. The maximum size specified for this array has been reached.");
            }
        } else {
            throw new DynamicTypeException(this.m_className + " Element cannot be added. The element's type does not fit with the specified content Type for this set.");
        }
    }

    @Override
    public void add(int index, DynamicData element) {
        if (index >= this.m_maxSize) {
            throw new DynamicTypeException(this.m_className + " The index specified (" + index + ") is out of the list boundaries (" + this.m_maxSize + ")."); 
        }
        
        if (element.getClass() == this.m_contentType.getClass()) {
            if (this.m_members.size() != this.m_maxSize) {
                this.m_members.add(index, element);
            } else {
                throw new DynamicTypeException(this.m_className + " Element cannot be added. The maximum size specified for this list has been reached.");
            }
        }
        
    }

    @Override
    public DynamicData get(int index) {
        if (index >= this.m_maxSize) {
            throw new DynamicTypeException(this.m_className + " The index specified (" + index + ") is greater than the maximum size of this list (" + this.m_maxSize + ")."); 
        } else if (index >= this.m_members.size()){
            throw new DynamicTypeException(this.m_className + " The index specified (" + index + ") is out of the list boundaries (" + this.m_maxSize + ").");
        }
        
        return this.m_members.get(index);
    }

    @Override
    public boolean isEmpty() {
        return this.m_members.isEmpty();
    }
    
    @Override
    public boolean equals(Object anotherObject) {
        if (anotherObject instanceof DynamicList) {
            if (((DynamicList) anotherObject).getTypeDescriptor().getKind() == this.m_typeDescriptor.getKind()) {
                boolean isEquals = true;
                for (int i=0; i < ((ListTypeDescriptor) ((DynamicListImpl) anotherObject).getTypeDescriptor()).getMaxSize(); ++i) {
                    isEquals = isEquals & ((DynamicListImpl) anotherObject).m_members.get(i).equals(this.m_members.get(i));
                }
                return isEquals;
            }
        }
        return false;
    }

    
    
}
