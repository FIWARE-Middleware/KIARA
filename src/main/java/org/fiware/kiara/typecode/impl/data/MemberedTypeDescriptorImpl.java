package org.fiware.kiara.typecode.impl.data;

import java.util.ArrayList;

import org.fiware.kiara.typecode.TypeDescriptor;
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.data.MemberedTypeDescriptor;

public class MemberedTypeDescriptorImpl extends DataTypeDescriptorImpl implements MemberedTypeDescriptor {
    
    private boolean m_membered = false;
    private ArrayList<DataTypeDescriptor> m_members;

    public MemberedTypeDescriptorImpl(TypeKind kind, String name) {
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
