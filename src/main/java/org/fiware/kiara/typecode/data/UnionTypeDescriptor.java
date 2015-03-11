package org.fiware.kiara.typecode.data;

public interface UnionTypeDescriptor extends MemberedTypeDescriptor {
    
    public <T> boolean addMember(DataTypeDescriptor typeDescriptor, String name, boolean isDefault, T... labels);

}
