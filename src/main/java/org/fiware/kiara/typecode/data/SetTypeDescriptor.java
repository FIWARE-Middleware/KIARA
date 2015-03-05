package org.fiware.kiara.typecode.data;

public interface SetTypeDescriptor extends ContainerTypeDescriptor {
    
    public DataTypeDescriptor getContentType();

    public boolean setContentType(DataTypeDescriptor contentType);

}
