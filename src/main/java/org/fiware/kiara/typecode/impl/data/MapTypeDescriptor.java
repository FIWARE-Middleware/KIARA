package org.fiware.kiara.typecode.impl.data;

import org.fiware.kiara.typecode.TypeKind;

public class MapTypeDescriptor extends ConstructedTypeDescriptor {

    private DataTypeDescriptor m_keyDescriptor = null;
    private DataTypeDescriptor m_valueDescriptor = null;
    
    public MapTypeDescriptor() {
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
    
    

}
