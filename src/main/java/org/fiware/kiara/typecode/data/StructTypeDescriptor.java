package org.fiware.kiara.typecode.data;

import org.fiware.kiara.typecode.TypeDescriptor;

public interface StructTypeDescriptor extends MemberedTypeDescriptor {

    public void addMember(TypeDescriptor member, String name);
    
    public DataTypeDescriptor getMember(String name);
    
}
