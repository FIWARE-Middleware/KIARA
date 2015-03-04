package org.fiware.kiara.typecode.impl.data;

import java.util.ArrayList;

import org.fiware.kiara.exceptions.TypeDescriptorException;
import org.fiware.kiara.typecode.TypeDescriptor;
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.data.Member;
import org.fiware.kiara.typecode.data.MemberedTypeDescriptor;

public class MemberedTypeDescriptorImpl extends DataTypeDescriptorImpl implements MemberedTypeDescriptor {
    
    private boolean m_membered = false;
    protected ArrayList<Member> m_members;
    
    public MemberedTypeDescriptorImpl(TypeKind kind/*, String name*/) {
        super(kind/*, name*/);
        this.m_members = new ArrayList<Member>();
    }
    
    @Override
    public boolean isMembered() {
        return this.m_membered;
    }
    
    @Override
    public ArrayList<Member> getMembers() {
        return this.m_members;
        //return null;
    }

    
    
    

}
