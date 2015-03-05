package org.fiware.kiara.typecode.data;

import java.util.ArrayList;

public interface ArrayTypeDescriptor extends ContainerTypeDescriptor {
    
    public DataTypeDescriptor getContentType();

    public boolean setContentType(DataTypeDescriptor contentType);
    
    public void setDimensionsLength(int... dimensions);
    
    public ArrayList<Integer> getDimensions();

}
