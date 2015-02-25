package org.fiware.kiara.typecode.impl.data;

import org.fiware.kiara.typecode.TypeKind;

public class StructTypeDescriptor extends MemberedTypeDescriptor {

    public StructTypeDescriptor() {
        super(TypeKind.STRUCT_TYPE, "struct");
    }
    
    @Override
    public boolean isStruct() {
        return true;
    }
    
}
