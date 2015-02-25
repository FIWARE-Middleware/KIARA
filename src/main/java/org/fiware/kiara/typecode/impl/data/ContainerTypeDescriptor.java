package org.fiware.kiara.typecode.impl.data;

import org.fiware.kiara.typecode.TypeKind;

public class ContainerTypeDescriptor extends ConstructedTypeDescriptor {

    private DataTypeDescriptor m_contentType = null;
    
    private int m_maximumSize;
    
    public ContainerTypeDescriptor(TypeKind kind, String name) {
        super(kind, name);
        this.m_maximumSize = 100;
    }
    
    public ContainerTypeDescriptor(TypeKind kind, String name, DataTypeDescriptor contentType) {
        super(kind, name);
        this.m_maximumSize = 100;
    }
    
    @Override
    public boolean setContentType(DataTypeDescriptor contentType) {
        this.m_contentType = contentType;
        return true;
    }
    
    @Override
    public DataTypeDescriptor getContentType() {
        return this.m_contentType;
    }
    
    @Override
    public boolean isContainer() {
        return true;
    }
    
    @Override
    public void setMaxSize(int size) {
        this.m_maximumSize = size;
    }
    
    @Override
    public int getMaxSize() {
        return this.m_maximumSize;
    }
    
    

}
