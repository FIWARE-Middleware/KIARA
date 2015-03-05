package org.fiware.kiara.typecode.data;

import org.fiware.kiara.typecode.impl.data.DataTypeDescriptorImpl;

public interface MapTypeDescriptor extends ContainerTypeDescriptor {
    
    public boolean setKeyTypeDescriptor(DataTypeDescriptor keyDescriptor);
    
    public DataTypeDescriptor getKeyTypeDescriptor();
    
    public boolean setValueTypeDescriptor(DataTypeDescriptor valueDescriptor);
    
    public DataTypeDescriptor getValueTypeDescriptor();

}
