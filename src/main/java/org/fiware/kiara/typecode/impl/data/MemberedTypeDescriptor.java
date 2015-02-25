package org.fiware.kiara.typecode.impl.data;

import java.util.ArrayList;

import org.fiware.kiara.typecode.TypeDescriptor;
import org.fiware.kiara.typecode.TypeKind;

public class MemberedTypeDescriptor extends DataTypeDescriptor {
    
    private boolean m_membered = false;
    private ArrayList<DataTypeDescriptor> m_members;

    public MemberedTypeDescriptor(TypeKind kind, String name) {
        super(kind, name);
        this.m_members = new ArrayList<DataTypeDescriptor>();
    }
    
    @Override
    public boolean isMembered() {
        return this.m_membered;
    }
    
    @Override
    public void addMembers(DataTypeDescriptor... members) {
        for(DataTypeDescriptor desc : members) {
            this.m_members.add(desc);
        }
    }    
    
    @Override
    public ArrayList<DataTypeDescriptor> getMembers() {
        return this.m_members;
    }
    
    

}
