package org.fiware.kiara.dynamic.impl.data;

import java.util.ArrayList;

import org.fiware.kiara.dynamic.data.DynamicData;
import org.fiware.kiara.dynamic.data.DynamicMember;
import org.fiware.kiara.dynamic.data.DynamicMembered;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;

public class DynamicMemberedImpl extends DynamicDataImpl implements DynamicMembered {
    
    protected ArrayList<DynamicMember> m_members;

    public DynamicMemberedImpl(DataTypeDescriptor dataDescriptor, String className) {
        super(dataDescriptor, className);
        this.m_members = new ArrayList<DynamicMember>();
    }
    
    public void addMember(DynamicData dynamicData, String name) {
        this.m_members.add(new DynamicMemberImpl(dynamicData, name));
    }

}
