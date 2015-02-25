package org.fiware.kiara.typecode.impl.data;

import org.fiware.kiara.typecode.TypeKind;

public class ConstructedTypeDescriptor extends DataTypeDescriptor {

    private boolean m_constructed = false;
    public ConstructedTypeDescriptor(TypeKind kind, String name) {
        super(kind, name);
        this.m_constructed = true;
    }

    @Override
    public boolean isConstructed() {
        return this.m_constructed;
    }
    
}
