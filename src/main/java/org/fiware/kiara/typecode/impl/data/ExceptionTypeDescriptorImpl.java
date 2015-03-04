package org.fiware.kiara.typecode.impl.data;

import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.ExceptionTypeDescriptor;

public class ExceptionTypeDescriptorImpl extends MemberedTypeDescriptorImpl implements ExceptionTypeDescriptor {

    public ExceptionTypeDescriptorImpl(TypeKind kind, String name) {
        super(kind);
    }
    
    @Override
    public boolean isException() {
        return true;
    }

}
