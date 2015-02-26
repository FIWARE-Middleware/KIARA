package org.fiware.kiara.dynamic.impl;

import java.beans.PropertyChangeSupport;

import org.fiware.kiara.dynamic.DynamicType;
import org.fiware.kiara.typecode.TypeDescriptor;

public abstract class DynamicTypeImpl implements DynamicType {
    
    protected TypeDescriptor m_typeDescriptor;
    protected String m_className;
    
    public DynamicTypeImpl(TypeDescriptor typeDescriptor, String className) {
        this.m_typeDescriptor = typeDescriptor;
        this.m_className = className;
    }
    
    public TypeDescriptor getTypeDescriptor() {
        return this.m_typeDescriptor;
    }
    
    /*
    public boolean set(Object value);
    
    public Object get();
    
    public boolean set(DynamicDataType value);
    
    public DynamicType getElementAt(int... position);
    
    public boolean setElementAt(DynamicDataType value, int... position);
    
    public boolean setMapPair(Object key, Object value);
    */
    
    

}
