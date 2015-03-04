package org.fiware.kiara.typecode.impl.data;

import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.data.Member;

public class MemberImpl implements Member {
    
    private String m_name;
    private DataTypeDescriptor m_typeDescriptor;
    
    public MemberImpl(DataTypeDescriptor typeDescriptor, String name) {
        this.m_name = name;
        this.m_typeDescriptor = typeDescriptor;
    }

    @Override
    public String getName() {
        return this.m_name;
    }

    @Override
    public DataTypeDescriptor getTypeDescriptor() {
        return this.m_typeDescriptor;
    }

}
