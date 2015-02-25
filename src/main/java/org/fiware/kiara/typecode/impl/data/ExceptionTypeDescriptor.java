package org.fiware.kiara.typecode.impl.data;

import org.fiware.kiara.typecode.TypeKind;

public class ExceptionTypeDescriptor extends MemberedTypeDescriptor {

    public ExceptionTypeDescriptor(TypeKind kind, String name) {
        super(kind, name);
    }
    
    @Override
    public boolean isException() {
        return true;
    }

}
