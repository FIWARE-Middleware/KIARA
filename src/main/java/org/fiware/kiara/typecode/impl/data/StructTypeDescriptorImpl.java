package org.fiware.kiara.typecode.impl.data;

import org.fiware.kiara.exceptions.TypeDescriptorException;
import org.fiware.kiara.typecode.TypeDescriptor;
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.data.Member;
import org.fiware.kiara.typecode.data.StructTypeDescriptor;

public class StructTypeDescriptorImpl extends MemberedTypeDescriptorImpl implements StructTypeDescriptor {

    private String m_name;
    
    public StructTypeDescriptorImpl(String name) {
        super(TypeKind.STRUCT_TYPE);
        this.m_name = name;
    }
    
    @Override
    public boolean isStruct() {
        return true;
    }
    
    @Override
    public void addMember(TypeDescriptor member, String name) {
        if (member instanceof DataTypeDescriptor) {
            if (!this.exists(name)) {
                this.m_members.add(new MemberImpl((DataTypeDescriptor) member, name));
            } else {
                throw new TypeDescriptorException("MemberedTypeDescriptor - A member with name " + name + " already exists in this structure.");
            }
        } else {
            throw new TypeDescriptorException("MemberedTypeDescriptor - A TypeDescriptor of type " + member.getKind() + " cannot be added. Only DataTypeDescriptor objects allowed.");
        }
    }    
    
    @Override
    public DataTypeDescriptor getMember(String name) {
        for (Member member : this.m_members) {
            if (member.getName().equals(name)) {
                return member.getTypeDescriptor();
            }
        }
        return null;
    }
    
    private boolean exists(String name) {
        for (Member member : this.m_members) {
            if (member.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
    
}
