package org.fiware.kiara.typecode.data;

public interface ListTypeDescriptor extends ContainerTypeDescriptor {
    
    public DataTypeDescriptor getContentType();

    public boolean setContentType(DataTypeDescriptor contentType);

}
