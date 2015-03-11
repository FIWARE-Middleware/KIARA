package org.fiware.kiara.typecode.impl.data;

import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.EnumTypeDescriptor;

public class EnumTypeDescriptorImpl extends MemberedTypeDescriptorImpl implements EnumTypeDescriptor {
    
    private String m_name;

    public EnumTypeDescriptorImpl(String name) {
        super(TypeKind.ENUM_TYPE);
        this.m_name = name;
    }
    
    @Override
    public boolean isEnum() {
        return true;
    }

    @Override
    public void addValue(String value) {
        this.m_members.add(new EnumMemberImpl(value));
    }
    
    

}
