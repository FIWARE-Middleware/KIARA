package org.fiware.kiara.typecode.impl.data;

import org.fiware.kiara.typecode.TypeKind;

public class ExceptionTypeDescriptorImpl extends MemberedTypeDescriptorImpl {

    public ExceptionTypeDescriptorImpl(TypeKind kind, String name) {
        super(kind, name);
    }
    
    @Override
    public boolean isException() {
        return true;
    }

}
