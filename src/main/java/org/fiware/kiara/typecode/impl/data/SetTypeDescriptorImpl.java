package org.fiware.kiara.typecode.impl.data;

import org.fiware.kiara.exceptions.TypeDescriptorException;
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.SetTypeDescriptor;

public class SetTypeDescriptorImpl extends ContainerTypeDescriptorImpl implements SetTypeDescriptor {

    public SetTypeDescriptorImpl() {
        super(TypeKind.SET_TYPE, "set");
    }
    
    @Override
    public boolean isSet() {
        return true;
    }
    
    @Override
    public void setMaxSize(int size) {
        if (size <= 0) {
            throw new TypeDescriptorException("SetTypeDescriptorImpl - Maximum set size must be greater than zero.");
        }
        super.setMaxSize(size);
    }

}
