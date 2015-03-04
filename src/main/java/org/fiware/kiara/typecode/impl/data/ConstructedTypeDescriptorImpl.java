package org.fiware.kiara.typecode.impl.data;

import org.fiware.kiara.typecode.TypeKind;

public class ConstructedTypeDescriptorImpl extends DataTypeDescriptorImpl {

    private boolean m_constructed = false;
    public ConstructedTypeDescriptorImpl(TypeKind kind, String name) {
        super(kind);
        this.m_constructed = true;
    }

    @Override
    public boolean isConstructed() {
        return this.m_constructed;
    }
    
}
