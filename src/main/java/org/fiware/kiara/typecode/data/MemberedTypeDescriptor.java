package org.fiware.kiara.typecode.data;

import java.util.ArrayList;

public interface MemberedTypeDescriptor extends DataTypeDescriptor {

    public void addMembers(DataTypeDescriptor... members);
    
    public ArrayList<DataTypeDescriptor> getMembers();
    
}
