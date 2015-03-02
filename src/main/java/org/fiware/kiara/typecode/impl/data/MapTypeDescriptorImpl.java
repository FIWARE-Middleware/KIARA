package org.fiware.kiara.typecode.impl.data;

import org.fiware.kiara.exceptions.TypeDescriptorException;
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.data.MapTypeDescriptor;

public class MapTypeDescriptorImpl extends ConstructedTypeDescriptorImpl implements MapTypeDescriptor {

    private DataTypeDescriptor m_keyDescriptor = null;
    private DataTypeDescriptor m_valueDescriptor = null;
    
    private int m_maximumSize;
    
    public MapTypeDescriptorImpl() {
        super(TypeKind.MAP_TYPE, "map");
        this.m_name = "map";
    }
    
    @Override
    public boolean isMap() {
        return true;
    }
    
    @Override
    public boolean setKeyTypeDescriptor(DataTypeDescriptor keyDescriptor) {
        if (this.m_kind == TypeKind.MAP_TYPE) {
            this.m_keyDescriptor = keyDescriptor;
            return true;
        }
        return false;
    }
    
    @Override
    public boolean setValueTypeDescriptor(DataTypeDescriptor valueDescriptor) {
        if (this.m_kind == TypeKind.MAP_TYPE) {
            this.m_valueDescriptor = valueDescriptor;
            return true;
        }
        return false;
    }
    
    @Override
    public DataTypeDescriptor getKeyTypeDescriptor() {
        return this.m_keyDescriptor;
    }

    @Override
    public DataTypeDescriptor getValueTypeDescriptor() {
        return this.m_valueDescriptor;
    }
    
    @Override
    public void setMaxSize(int size) {
        if (size <= 0) {
            throw new TypeDescriptorException("ListTypeDescriptor - Maximum map size must be greater than zero.");
        }
        this.m_maximumSize = size;
    }
    
    @Override
    public int getMaxSize() {
        return this.m_maximumSize;
    }
    
    

}
