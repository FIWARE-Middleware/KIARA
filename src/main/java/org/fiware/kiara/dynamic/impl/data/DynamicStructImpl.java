package org.fiware.kiara.dynamic.impl.data;

import org.fiware.kiara.dynamic.data.DynamicData;
import org.fiware.kiara.dynamic.data.DynamicMember;
import org.fiware.kiara.dynamic.data.DynamicStruct;
import org.fiware.kiara.typecode.data.StructTypeDescriptor;

public class DynamicStructImpl extends DynamicMemberedImpl implements DynamicStruct {

    public DynamicStructImpl(StructTypeDescriptor dataDescriptor) {
        super(dataDescriptor, "DynamicStructImpl");
    }

    @Override
    public DynamicData getMember(String name) {
        for (DynamicMember member : this.m_members) {
            if (member.getName().equals(name)) {
                return member.getDynamicData();
            }
        }
        return null;
    }
    
    

}
