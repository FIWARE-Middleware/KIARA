package org.fiware.kiara.typecode.impl.data;

import java.util.ArrayList;

import org.fiware.kiara.exceptions.TypeDescriptorException;
import org.fiware.kiara.typecode.TypeDescriptor;
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.data.Member;
import org.fiware.kiara.typecode.data.MemberedTypeDescriptor;

public class MemberedTypeDescriptorImpl extends DataTypeDescriptorImpl implements MemberedTypeDescriptor {
    
    protected ArrayList<Member> m_members;
    
    public MemberedTypeDescriptorImpl(TypeKind kind/*, String name*/) {
        super(kind);
        this.m_members = new ArrayList<Member>();
    }
    
    @Override
    public boolean isMembered() {
        return true;
    }
    
    @Override
    public ArrayList<Member> getMembers() {
        return this.m_members;
    }

    
    
    

}
