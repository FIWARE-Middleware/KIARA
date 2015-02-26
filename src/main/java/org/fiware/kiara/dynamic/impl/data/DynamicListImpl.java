package org.fiware.kiara.dynamic.impl.data;

import java.util.ArrayList;

import org.fiware.kiara.dynamic.DynamicData;
import org.fiware.kiara.dynamic.DynamicList;
import org.fiware.kiara.dynamic.impl.DynamicTypeImpl;
import org.fiware.kiara.exceptions.DynamicTypeException;
import org.fiware.kiara.typecode.impl.data.DataTypeDescriptor;

public class DynamicListImpl extends DynamicContainerImpl implements DynamicList {
    
    private int m_maxSize;

    public DynamicListImpl(DataTypeDescriptor dataDescriptor) {
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
    
}
