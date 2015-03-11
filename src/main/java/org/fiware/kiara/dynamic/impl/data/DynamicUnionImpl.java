package org.fiware.kiara.dynamic.impl.data;

import java.util.ArrayList;

import org.fiware.kiara.dynamic.data.DynamicData;
import org.fiware.kiara.dynamic.data.DynamicEnum;
import org.fiware.kiara.dynamic.data.DynamicMember;
import org.fiware.kiara.dynamic.data.DynamicPrimitive;
import org.fiware.kiara.dynamic.data.DynamicUnion;
import org.fiware.kiara.exceptions.DynamicTypeException;
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.data.EnumTypeDescriptor;
import org.fiware.kiara.typecode.data.UnionTypeDescriptor;
import org.fiware.kiara.typecode.impl.data.UnionTypeDescriptorImpl;

public class DynamicUnionImpl extends DynamicMemberedImpl implements DynamicUnion {
    
    private DynamicData m_discriminator;
    
    private Object m_nextDiscriminatorValue;
    
    private ArrayList<Boolean> m_activeMember;
    
    private int m_defaultIndex = -1; 

    public DynamicUnionImpl(UnionTypeDescriptor dataDescriptor) {
        super(dataDescriptor, "DynamicUnionImpl");
        if (((UnionTypeDescriptorImpl) dataDescriptor).hasDefaultValue()) {
            this.m_defaultIndex = ((UnionTypeDescriptorImpl) dataDescriptor).getDefaultIndex();
        }
        this.m_discriminator = null;
        this.m_activeMember = new ArrayList<Boolean>();
    }
    
    public <T> void addMember(DynamicData dynamicData, String name, ArrayList<T> labels, boolean isDefault) {
        if (!this.exists(name)) {
            this.m_members.add(new DynamicUnionMemberImpl<T>(dynamicData, name, isDefault, labels));
            this.m_activeMember.add(false);
            
        } else {
            throw new DynamicTypeException(this.m_className + " - Another member with the name " + name + " has already been added to this union.");
        }
    }
    
    private boolean exists(String name) {
        for (DynamicMember m : this.m_members) {
            if (m.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
    
    public void setDiscriminator(DynamicData discriminator) {
        this.m_discriminator = discriminator;
    }
    
    @Override
    public DynamicData getMember(String name) {
        int index=0;
        for (DynamicMember member : this.m_members) {
            if(member.getName().equals(name)) {
                break;
            }
            index++;
        }
        if (index < this.m_activeMember.size() && this.m_activeMember.get(index)) {
            return this.m_members.get(index).getDynamicData();
        }
        return null;
    }
    
    /*private DynamicData getCaseValue(DynamicUnionMemberImpl<?> unionMember) {
        for (Object label : unionMember.getLabels()) {
            if (label == this.m_nextDiscriminatorValue) {
                return unionMember.getDynamicData();
            }
        }
        if (unionMember.isDefault()) {
            return unionMember.getDynamicData();
        }
        return null;
    }*/
    
    @Override
    public void setMember(String name, DynamicData data) {
        
    }
    
    public void setDefaultDiscriminatorValue() {
        if (this.m_defaultIndex != -1) {
            this.m_activeMember.set(this.m_defaultIndex, true);
        } else {
            this.m_activeMember.set(0, true);
        }
        
    }
    
    private void setActiveMember(int index) {
        if (index < this.m_activeMember.size()) {
            for(int i=0; i < this.m_activeMember.size(); ++i) {
                if (index == i) {
                    this.m_activeMember.set(i, true);
                } else {
                    this.m_activeMember.set(i, false);
                }
            }
        }
    }
    
    @Override
    public void _d(Object value) {
        
        switch (((UnionTypeDescriptorImpl) this.m_typeDescriptor).getDiscriminator().getKind()) {
        case CHAR_8_TYPE: 
            if (value instanceof Character) {
                ((DynamicPrimitive) this.m_discriminator).set(value);
            } else {
                throw new DynamicTypeException(this.m_className + " - Cannot set a union discriminator of type " + value.getClass() + " into a " + TypeKind.CHAR_8_TYPE + ".");
            }
            break;
        case BOOLEAN_TYPE: 
            if (value instanceof Boolean) {
                ((DynamicPrimitive) this.m_discriminator).set(value);
            } else {
                throw new DynamicTypeException(this.m_className + " - Cannot set a union discriminator of type " + value.getClass() + " into a " + TypeKind.BOOLEAN_TYPE + ".");
            }
            
            break;
        case INT_32_TYPE: 
            if (value instanceof Integer) {
                ((DynamicPrimitive) this.m_discriminator).set(value);
            } else {
                throw new DynamicTypeException(this.m_className + " - Cannot set a union discriminator of type " + value.getClass() + " into a " + TypeKind.INT_32_TYPE + ".");
            }
            
            break;
        case UINT_32_TYPE: 
            if (value instanceof Integer) {
                ((DynamicPrimitive) this.m_discriminator).set(value);
            } else {
                throw new DynamicTypeException(this.m_className + " - Cannot set a union discriminator of type " + value.getClass() + " into a " + TypeKind.UINT_32_TYPE + ".");
            }
            
            break;
        case ENUM_TYPE: 
            if (value instanceof String) {
                ((DynamicEnum) this.m_discriminator).set((String) value);
            } else {
                throw new DynamicTypeException(this.m_className + " - Cannot set a union discriminator of type " + value.getClass() + " into a " + TypeKind.STRING_TYPE + ".");
            }
            
            break;
        default:
            break;
        }
        
        int index = 0;
        for (DynamicMember member : this.m_members) {
            DynamicUnionMemberImpl<?> unionMember = (DynamicUnionMemberImpl<?>) member;
            for (Object label : unionMember.getLabels()) {
                if (this.m_discriminator.getTypeDescriptor().isEnum()) {
                    if (((DynamicEnumImpl) this.m_discriminator).getValueAt((int) label).equals(value)) {
                        this.setActiveMember(index);
                        return;
                    }
                } else {
                    if (value == label) {
                        this.setActiveMember(index);
                        return;
                    }
                }
            }
            index++;
        }
        throw new DynamicTypeException(this.m_className + " - Discriminator value " + value.getClass() + " is not amongst the valid values specified when creating the type descriptor.");
        
    }
    
    @Override
    public Object _d() {
        switch (this.m_discriminator.getTypeDescriptor().getKind()) {
        case CHAR_8_TYPE:
        case BOOLEAN_TYPE:
        case INT_32_TYPE:
        case UINT_32_TYPE:
            return ((DynamicPrimitive) this.m_discriminator).get();
        case ENUM_TYPE:
            return ((DynamicEnum) this.m_discriminator).get();
        default:
            return null;
        }
    }

    

}
