package org.fiware.kiara.dynamic.impl.data;

import java.util.ArrayList;
import java.util.HashMap;

import org.fiware.kiara.dynamic.data.DynamicData;
import org.fiware.kiara.dynamic.data.DynamicMap;
import org.fiware.kiara.exceptions.DynamicTypeException;
import org.fiware.kiara.typecode.data.MapTypeDescriptor;
import org.fiware.kiara.typecode.impl.data.DataTypeDescriptorImpl;

public class DynamicMapImpl extends DynamicContainerImpl implements DynamicMap {
    
    private int m_maxSize;
    private ArrayList<DynamicData> m_keyMembers;
    private DynamicData m_keyContentType;
    
    public DynamicMapImpl(MapTypeDescriptor mapDescriptor) {
        super(mapDescriptor, "DynamicMapImpl");
        this.m_maxSize = mapDescriptor.getMaxSize();
        this.m_keyMembers = new ArrayList<DynamicData>(this.m_maxSize);
        this.m_members = new ArrayList<DynamicData>(this.m_maxSize);
    }
    
    @Override
    public boolean put(DynamicData key, DynamicData value) {
        if (key.getTypeDescriptor().getKind() == this.m_keyContentType.getTypeDescriptor().getKind()) {
            if (value.getTypeDescriptor().getKind() == this.m_contentType.getTypeDescriptor().getKind()) {
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
        if (key.getTypeDescriptor().getKind() != this.m_keyContentType.getTypeDescriptor().getKind()) {
            throw new DynamicTypeException(this.m_className + " The key type specified (" + key.getTypeDescriptor().getKind() + ") is not the same as the one defined in the map descriptor.");
        }
        
        for (int i=0; i < this.m_members.size(); ++i) {
            if (this.m_keyMembers.get(i).equals(key)) {
                return this.m_members.get(i);
            }
        }
        
        return null;
    }

   private boolean existsInMap(DynamicData value) {
        for (int i=0; i < this.m_keyMembers.size(); ++i) {
            if (this.m_keyMembers.get(i).equals(value)) {
                return true;
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
        return m_contentType;
    }

    public void setValueContentType(DynamicData valueContentType) {
        this.m_contentType = valueContentType;
    }

    

    

}
