package org.fiware.kiara.typecode.data;

import java.util.ArrayList;

import org.fiware.kiara.typecode.TypeDescriptor;

public interface MemberedTypeDescriptor extends DataTypeDescriptor {

    public ArrayList<Member> getMembers();
    
}
