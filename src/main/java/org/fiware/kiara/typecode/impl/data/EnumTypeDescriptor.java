package org.fiware.kiara.typecode.impl.data;

import org.fiware.kiara.typecode.TypeKind;

public class EnumTypeDescriptor extends MemberedTypeDescriptor {

    public EnumTypeDescriptor() {
        super(TypeKind.ENUM_TYPE, "enum");
    }
    
    @Override
    public boolean isEnum() {
        return true;
    }
    
    

}
