package org.fiware.kiara.typecode.data;

import java.util.ArrayList;

import org.fiware.kiara.typecode.impl.data.DataTypeDescriptorImpl;

public interface ContainerTypeDescriptor extends ConstructedTypeDescriptor {
    
    /*public void addMembers(DataTypeDescriptor... members);
    
    public ArrayList<DataTypeDescriptor> getMembers();*/
    
    public boolean setContentType(DataTypeDescriptor contentType);
    
    public DataTypeDescriptor getContentType();

}
