package org.fiware.kiara.dynamic.impl.data;

import java.util.ArrayList;
import java.util.List;

import org.fiware.kiara.dynamic.DynamicData;
import org.fiware.kiara.dynamic.DynamicSet;
import org.fiware.kiara.exceptions.DynamicTypeException;
import org.fiware.kiara.typecode.impl.data.DataTypeDescriptor;

public class DynamicSetImpl extends DynamicContainerImpl implements DynamicSet {

    private int m_maxSize;
    private List<Boolean> m_validData;
    
    public DynamicSetImpl(DataTypeDescriptor dataDescriptor) {
        super(dataDescriptor, "DynamicSetImpl");
        this.m_maxSize = dataDescriptor.getMaxSize();
        this.m_members = new ArrayList<DynamicData>(this.m_maxSize); 
        this.m_validData = new ArrayList<Boolean>(this.m_maxSize);
    }
    
    @Override
    public DynamicData getElementAt(int index) {
        if (index >= this.m_maxSize) {
            throw new DynamicTypeException(this.m_className + " The index specified (" + index + ") is out of the set boundaries (" + this.m_maxSize + ")."); 
        }
        
        return this.m_members.get(index);
        
    }

    @Override
    public boolean setElementAt(DynamicData value, int index) {
        if (index >= this.m_maxSize) {
            throw new DynamicTypeException(this.m_className + " The index specified (" + index + ") is out of the set boundaries (" + this.m_maxSize + ")."); 
        }
        
        if (value.getClass() == this.m_contentType.getClass()) {
            if (this.m_members.size() != this.m_maxSize) {
                this.m_members.add(index, value);
                this.m_validData.add(false);
                return true;
            } else {
                if (!existsInSet(value)) {
                    return (this.m_members.set(index, value) != null);
                }
            }
        }
        
        return false;
    }
    
    @Override
    public boolean exists(DynamicDataImpl value, Object... params) {
        value.visit(params);
        int index = getIndex(value);
        boolean exists = false;
        if (index != -1) {
            if (this.m_validData.get(index)) {
                exists = existsInSet(value, index);
            } else {
                exists = existsInSet(value);
            }
            if (!exists) {
                this.setValid(index);
                //System.out.println("Added");
                return true;
            }
            //System.out.println("NOT Added");
        }
        return false;
    }
    
    private int getIndex(DynamicDataImpl dynData) {
        for (int i=0; i < this.m_members.size(); ++i) {
            if (dynData == this.m_members.get(i)) {
                return i;
            }
        }
        return -1;
    }
    
    private void setValid(int index) {
        this.m_validData.remove(index);
        this.m_validData.add(index, true);
    }
    
    private boolean existsInSet(DynamicData value) {
        for (int i=0; i < this.m_members.size(); ++i) {
            if (this.m_validData.get(i) && this.m_members.get(i).equals(value)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean existsInSet(DynamicData value, int avoidIndex) {
        for (int i=0; i < this.m_members.size(); ++i) {
            if (i != avoidIndex) {
                if (this.m_validData.get(i) && this.m_members.get(i).equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /*public void setValidDataAt(int index, boolean value) {
        if (index >= 0 && this.m_validData.size() > index) {
            this.m_validData.remove(index);
        }
        this.m_validData.add(index, value);
    }*/
    
    /*public boolean getValidDataAt(int index) {
        if (index >= 0 && this.m_validData.size() > index) {
            return this.m_validData.get(index);
        }
        return false;
    }*/

}
