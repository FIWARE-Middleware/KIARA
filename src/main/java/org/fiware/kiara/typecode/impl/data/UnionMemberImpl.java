package org.fiware.kiara.typecode.impl.data;

import java.util.ArrayList;

import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.data.UnionMember;

public class UnionMemberImpl<T> extends MemberImpl implements UnionMember {
    
    private ArrayList<T> m_labels;
    
    private boolean m_isDefault;

    public UnionMemberImpl(DataTypeDescriptor typeDescriptor, String name, ArrayList<T> labels, boolean isDefault) {
        super(typeDescriptor, name);
        this.m_labels = labels;
        this.m_isDefault = isDefault;
    }
    
    public ArrayList<T> getLabels() {
        return this.m_labels;
    }
    
    public boolean isDefault() {
        return this.m_isDefault;
    }
    
    /*public void addLabel(T label) {
        this.m_labels.add(label);
    }*/

}
