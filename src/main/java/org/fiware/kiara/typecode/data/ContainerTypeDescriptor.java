package org.fiware.kiara.typecode.data;

import java.util.ArrayList;

import org.fiware.kiara.typecode.impl.data.DataTypeDescriptorImpl;

public interface ContainerTypeDescriptor extends DataTypeDescriptor {
    
    /*public void addMembers(DataTypeDescriptor... members);
    
    public ArrayList<DataTypeDescriptor> getMembers();*/
    
    
    public void setMaxSize(int length);
    
    public int getMaxSize();
  
}
