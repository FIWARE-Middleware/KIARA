package org.fiware.kiara.typecode.impl.data;

import org.fiware.kiara.typecode.TypeKind;

public class SequenceTypeDescriptor extends ContainerTypeDescriptor {

    public SequenceTypeDescriptor() {
        super(TypeKind.SEQUENCE_TYPE, "list");
    }
    
    @Override
    public boolean isSequence() {
        return true;
    }
    
    

}
