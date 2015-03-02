package org.fiware.kiara.typecode.impl.data;

import org.fiware.kiara.typecode.TypeKind;

public class UnionTypeDescriptorImpl extends MemberedTypeDescriptorImpl {

    public UnionTypeDescriptorImpl() {
        super(TypeKind.UNION_TYPE, "union");
    }
    
    @Override
    public boolean isUnion() {
        return true;
    }
    
    

}
