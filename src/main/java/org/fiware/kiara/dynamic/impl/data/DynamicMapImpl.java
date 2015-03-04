package org.fiware.kiara.dynamic.impl.data;

import java.util.ArrayList;
import java.util.HashMap;

import org.fiware.kiara.dynamic.data.DynamicData;
import org.fiware.kiara.dynamic.data.DynamicMap;
import org.fiware.kiara.exceptions.DynamicTypeException;
import org.fiware.kiara.typecode.data.MapTypeDescriptor;
import org.fiware.kiara.typecode.impl.data.DataTypeDescriptorImpl;

public class DynamicMapImpl extends DynamicConstructedImpl implements DynamicMap {
    
    private int m_maxSize;
    //private HashMap<DynamicData, DynamicData> m_members;
    private ArrayList<Boolean> m_validData;
    private ArrayList<DynamicData> m_keyMembers;
    private ArrayList<DynamicData> m_valueMembers;
    private DynamicData m_keyContentType;
    private DynamicData m_valueContentType;

    public DynamicMapImpl(MapTypeDescriptor mapDescriptor) {
        super(mapDescriptor, "DynamicMapImpl");
        this.m_maxSize = mapDescriptor.getMaxSize();
        //this.m_members = new HashMap<DynamicData, DynamicData>(this.m_maxSize);
        this.m_validData = new ArrayList<Boolean>(this.m_maxSize);
        this.m_keyMembers = new ArrayList<DynamicData>(this.m_maxSize);
        this.m_valueMembers = new ArrayList<DynamicData>(this.m_maxSize);
    }

    @Override
    public boolean setElementAt(DynamicData key, DynamicData value, int index) {
        if (index >= this.m_maxSize) {
            throw new DynamicTypeException(this.m_className + " The index specified (" + index + ") is out of the list boundaries (" + this.m_maxSize + ")."); 
        }
        
        if (key.getTypeDescriptor().getKind() == this.m_keyContentType.getTypeDescriptor().getKind()) { // TODO
            if (value.getTypeDescriptor().getKind() == this.m_valueContentType.getTypeDescriptor().getKind()) { // TODO
                if (this.m_keyMembers.size() != this.m_maxSize) {
                    this.m_keyMembers.add(index, key);
                    this.m_valueMembers.add(index, value);
                    this.m_validData.add(false);
                    return true;
                } else {
                    if (!existsInMap(key)) {
                        boolean ret = true;
                        ret &= (this.m_keyMembers.set(index, key) != null);
                        ret &= (this.m_valueMembers.set(index, value) != null);
                        return ret;
                    }
                    return false;
                }
            } else {
                throw new DynamicTypeException(this.m_className + " The value type specified (" + key.getTypeDescriptor().getKind() + ") is not the same as the one defined in the map descriptor.");
            }
        } else {
            throw new DynamicTypeException(this.m_className + " The key type specified (" + key.getTypeDescriptor().getKind() + ") is not the same as the one defined in the map descriptor.");
        }
    }
    
    @Override
    public DynamicData getKeyAt(int index) {
        if (index >= this.m_maxSize) {
            throw new DynamicTypeException(this.m_className + " The index specified (" + index + ") is out of the list boundaries (" + this.m_maxSize + ")."); 
        }
        
        //ArrayList<DynamicData> values = (ArrayList<DynamicData>) this.m_members.values();
        
        return this.m_keyMembers.get(index);
    }
    
    @Override
    public DynamicData getValueAt(int index) {
        if (index >= this.m_maxSize) {
            throw new DynamicTypeException(this.m_className + " The index specified (" + index + ") is out of the list boundaries (" + this.m_maxSize + ")."); 
        }
        
        return this.m_valueMembers.get(index);
    }

    @Override
    public DynamicData getValue(DynamicData key) {
        if (key.getTypeDescriptor().getKind() != this.m_keyContentType.getTypeDescriptor().getKind()) {
            throw new DynamicTypeException(this.m_className + " The key type specified (" + key.getTypeDescriptor().getKind() + ") is not the same as the one defined in the map descriptor.");
        }
        
        for (int i=0; i < this.m_maxSize; ++i) {
            if (this.m_keyMembers.get(i).equals(key)) {
                return this.m_valueMembers.get(i);
            }
        }
        
        return null;
    }
    
    @Override
    public boolean notify(DynamicDataImpl value, Object... params) {
        if (this.m_visitor != null) {
            this.m_visitor.notify(this, appendParams(value, params));
        } else {
            value.visit(params);
            
        }

        int index = getIndex(value);
        boolean exists = false;
        if (index != -1) {
            if (this.m_validData.get(index)) {
                exists = existsInMap(value, index);
            } else {
                exists = existsInMap(value);
            }
            if (!exists) {
                this.setValid(index);
                System.out.println("Added");
                return true;
            }
            System.out.println("NOT Added");
        }
        return false;
    }
    
    private int getIndex(DynamicDataImpl dynData) {
        for (int i=0; i < this.m_keyMembers.size(); ++i) {
            if (dynData == this.m_keyMembers.get(i)) {
                return i;
            }
        }
        return -1;
    }
    
    private void setValid(int index) {
        this.m_validData.remove(index);
        this.m_validData.add(index, true);
    }
    
    private boolean existsInMap(DynamicData value) {
        for (int i=0; i < this.m_keyMembers.size(); ++i) {
            if (this.m_validData.get(i) && this.m_keyMembers.get(i).equals(value)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean existsInMap(DynamicData value, int avoidIndex) {
        for (int i=0; i < this.m_keyMembers.size(); ++i) {
            if (i != avoidIndex) {
                if (this.m_validData.get(i) && this.m_keyMembers.get(i).equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    public DynamicData getKeyContentType() {
        return m_keyContentType;
    }

    public void setKeyContentType(DynamicData keyContentType) {
        this.m_keyContentType = keyContentType;
    }

    public DynamicData getValueContentType() {
        return m_valueContentType;
    }

    public void setValueContentType(DynamicData valueContentType) {
        this.m_valueContentType = valueContentType;
    }

    

}
