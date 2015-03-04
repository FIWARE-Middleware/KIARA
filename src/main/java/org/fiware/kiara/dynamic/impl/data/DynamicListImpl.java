package org.fiware.kiara.dynamic.impl.data;

import java.util.ArrayList;

import org.fiware.kiara.dynamic.data.DynamicArray;
import org.fiware.kiara.dynamic.data.DynamicData;
import org.fiware.kiara.dynamic.data.DynamicList;
import org.fiware.kiara.dynamic.impl.DynamicTypeImpl;
import org.fiware.kiara.dynamic.impl.data.visitor.Visitor;
import org.fiware.kiara.exceptions.DynamicTypeException;
import org.fiware.kiara.typecode.data.ArrayTypeDescriptor;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.data.ListTypeDescriptor;
import org.fiware.kiara.typecode.impl.data.DataTypeDescriptorImpl;

public class DynamicListImpl extends DynamicContainerImpl implements DynamicList {
    
    private int m_maxSize;

    public DynamicListImpl(ListTypeDescriptor dataDescriptor) {
        super(dataDescriptor, "DynamicListImpl");
        this.m_maxSize = dataDescriptor.getMaxSize();
        this.m_members = new ArrayList<DynamicData>(this.m_maxSize);
    }
    
    @Override
    public DynamicData getElementAt(int index) {
        if (index >= this.m_maxSize) {
            throw new DynamicTypeException(this.m_className + " The index specified (" + index + ") is out of the list boundaries (" + this.m_maxSize + ")."); 
        }
        
        return this.m_members.get(index);
    }

    @Override
    public boolean setElementAt(DynamicData value, int index) {
        if (index >= this.m_maxSize) {
            throw new DynamicTypeException(this.m_className + " The index specified (" + index + ") is out of the list boundaries (" + this.m_maxSize + ")."); 
        }
        
        if (value.getClass() == this.m_contentType.getClass()) {
            if (this.m_members.size() != this.m_maxSize) {
                this.m_members.add(index, value);
                return true;
            } else {
                return (this.m_members.set(index, value) != null);
            }
        }
        
        return false;
    }
    
    @Override
    public boolean notify(DynamicDataImpl value, Object... params) {
        if (this.m_visitor != null) {
            this.m_visitor.notify(this, appendParams(value, params));
        } else {
            value.visit(params);
            
        }
        
        return true;
    }
    
    @Override
    public void visit(Object... params) {
        DynamicDataImpl value = (DynamicDataImpl) params[0];
        
        value.visit(trimParams(params));
        
    }
    
    @Override
    public void registerVisitor(Visitor visitor) {
        super.registerVisitor(visitor);
        for (int i=0; i < this.m_members.size(); ++i) {
            ((DynamicDataImpl) this.m_members.get(i)).registerVisitor(this);
        }
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
