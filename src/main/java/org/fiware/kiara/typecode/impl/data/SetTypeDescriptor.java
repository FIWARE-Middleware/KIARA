package org.fiware.kiara.typecode.impl.data;

import org.fiware.kiara.typecode.TypeKind;

public class SetTypeDescriptor extends ContainerTypeDescriptor {

    public SetTypeDescriptor() {
        super(TypeKind.SET_TYPE, "set");
    }
    
    @Override
    public boolean isSet() {
        return true;
    }

}
