package org.fiware.kiara.typecode.impl.data;

import org.fiware.kiara.exceptions.TypeDescriptorException;
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.data.ListTypeDescriptor;

public class ListTypeDescriptorImpl extends ContainerTypeDescriptorImpl implements ListTypeDescriptor {

    public ListTypeDescriptorImpl() {
        super(TypeKind.LIST_TYPE);
    }
    
    @Override
    public boolean isList() {
        return true;
    }
    
    @Override
    public void setMaxSize(int size) {
        if (size <= 0) {
            throw new TypeDescriptorException("ListTypeDescriptor - Maximum list size must be greater than zero.");
        }
        super.setMaxSize(size);
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
    
    

}
