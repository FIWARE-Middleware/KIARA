package org.fiware.kiara.typecode.impl.data;

import org.fiware.kiara.exceptions.TypeDescriptorException;
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.ListTypeDescriptor;

public class ListTypeDescriptorImpl extends ContainerTypeDescriptorImpl implements ListTypeDescriptor {

    public ListTypeDescriptorImpl() {
        super(TypeKind.LIST_TYPE, "list");
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
    
    

}
