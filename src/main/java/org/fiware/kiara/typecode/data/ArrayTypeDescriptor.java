package org.fiware.kiara.typecode.data;

import java.util.ArrayList;

public interface ArrayTypeDescriptor extends ContainerTypeDescriptor {
    
    public void setDimensions(int... dimensions);
    
    public ArrayList<Integer> getDimensions();

}
