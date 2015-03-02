package org.fiware.kiara.typecode;

public interface TypeDescriptorBuilder {
    
    public TypeDescriptor createTypeDescriptor(TypeKind kind, String name);

}
