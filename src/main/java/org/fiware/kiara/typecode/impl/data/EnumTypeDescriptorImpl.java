package org.fiware.kiara.typecode.impl.data;

import org.fiware.kiara.typecode.TypeKind;

public class EnumTypeDescriptorImpl extends MemberedTypeDescriptorImpl {

    public EnumTypeDescriptorImpl() {
        super(TypeKind.ENUM_TYPE);
    }
    
    @Override
    public boolean isEnum() {
        return true;
    }
    
    

}
