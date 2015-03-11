package org.fiware.kiara.dynamic.impl.data;

import org.fiware.kiara.dynamic.data.DynamicEnum;
import org.fiware.kiara.exceptions.DynamicTypeException;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.data.EnumTypeDescriptor;
import org.fiware.kiara.typecode.data.Member;

public class DynamicEnumImpl extends DynamicMemberedImpl implements DynamicEnum {
    
    private int m_chosenValue = -1; // Default

    public DynamicEnumImpl(DataTypeDescriptor dataDescriptor) {
        super(dataDescriptor, "DynamicEnumImpl");
    }

    @Override
    public void set(String value) {
        int index = 0;
        for (Member member : ((EnumTypeDescriptor) this.m_typeDescriptor).getMembers()) {
            if (value.equals(member.getName())) {
                this.m_chosenValue = index;
                return;
            }
            index++;
        }
        throw new DynamicTypeException(this.m_className + " - The value specified is not amongst the possible enumeration values.");
    }

    @Override
    public String get() {
        if (this.m_chosenValue != -1) {
            return this.m_members.get(this.m_chosenValue).getName();
        }
        return null;
    }
    
    public String getValueAt(int index) {
        return this.m_members.get(index).getName();
    }
    
    public int getChosenValueIndex() {
        return this.m_chosenValue;
    }
    
    public void setChosenValueIndex(int index) {
        this.m_chosenValue = index;
    }
    
    /*public void addMember(String member) {
        
    }*/

}
