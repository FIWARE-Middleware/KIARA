/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2014 Proyectos y Sistemas de Mantenimiento S.L. (eProsima)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fiware.kiara.typecode.impl.data;

import java.util.ArrayList;

import org.fiware.kiara.exceptions.TypeDescriptorException;
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.data.EnumTypeDescriptor;
import org.fiware.kiara.typecode.data.Member;
import org.fiware.kiara.typecode.data.UnionTypeDescriptor;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*
*/
public class UnionTypeDescriptorImpl extends MemberedTypeDescriptorImpl implements UnionTypeDescriptor {
    
    private DataTypeDescriptor m_discriminator;
    
    @SuppressWarnings("unused")
    private String m_name;
    
    private int m_defaultIndex = -1;
    
    public UnionTypeDescriptorImpl(String name, DataTypeDescriptor discriminatorDescriptor) {
        super(TypeKind.UNION_TYPE);
        switch(discriminatorDescriptor.getKind()) {
        case CHAR_8_TYPE:
        case BOOLEAN_TYPE:
        case INT_32_TYPE:
        case UINT_32_TYPE:
        case ENUM_TYPE:
            this.m_discriminator = discriminatorDescriptor;
            break;
        default:
            throw new TypeDescriptorException("UnionTypeDescriptorImpl - Another member with the name " + name + " has already been added to this union.");
        }
        this.m_name = name;
    }
    
    @Override
    public boolean isUnion() {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> boolean addMember(DataTypeDescriptor typeDescriptor, String name, boolean isDefault, T... labels) {
        if (!isDefault && labels.length == 0) {
            throw new TypeDescriptorException("UnionTypeDescriptorImpl - Only a default union member can have no labels assigned to it.");
        }
        switch(m_discriminator.getKind())
        {
        case CHAR_8_TYPE: {
            ArrayList<Character> innerLabels = new ArrayList<Character>();
            for(T label : labels)
            {
                if(label instanceof Character)
                {
                    innerLabels.add((Character) label);
                }
                else
                {
                    throw new TypeDescriptorException("UnionTypeDescriptorImpl - Type " + label.getClass() + " does not match discriminator kind (" + this.m_discriminator.getKind() + ").");
                }
            }
            addMember(name, typeDescriptor, isDefault, innerLabels);
            break;
        }
        case BOOLEAN_TYPE: {
            ArrayList<Boolean> innerLabels = new ArrayList<Boolean>();
            for(T label : labels)
            {
                if(label instanceof Boolean)
                {
                    innerLabels.add((Boolean) label);
                }
                else
                {
                    throw new TypeDescriptorException("UnionTypeDescriptorImpl - Type " + label.getClass() + " does not match discriminator kind (" + this.m_discriminator.getKind() + ").");
                }
            }
            addMember(name, typeDescriptor, isDefault, innerLabels);
            break;
        }
        case INT_32_TYPE: {
            ArrayList<Integer> innerLabels = new ArrayList<Integer>();
            for(T label : labels)
            {
                if(label instanceof Integer)
                {
                    innerLabels.add((Integer) label);
                }
                else
                {
                    throw new TypeDescriptorException("UnionTypeDescriptorImpl - Type " + label.getClass() + " does not match discriminator kind (" + this.m_discriminator.getKind() + ").");
                }
            }
            addMember(name, typeDescriptor, isDefault, innerLabels);
            break;
        }
        case UINT_32_TYPE: {
            ArrayList<Integer> innerLabels = new ArrayList<Integer>();
            for(T label : labels)
            {
                if(label instanceof Integer)
                {
                    innerLabels.add((Integer) label);
                }
                else
                {
                    throw new TypeDescriptorException("UnionTypeDescriptorImpl - Type " + label.getClass() + " does not match discriminator kind (" + this.m_discriminator.getKind() + ").");
                }
            }
            addMember(name, typeDescriptor, isDefault, innerLabels);
            break;
        }
        
        case ENUM_TYPE: {
            ArrayList<Integer> innerLabels = new ArrayList<Integer>();
            for(T label : labels)
            {
                if(label instanceof String)
                {
                    ArrayList<Member> members = ((EnumTypeDescriptor) this.m_discriminator).getMembers();
                    boolean found = false;
                    for (Member m : members) {
                        if (m.getName().equals(label)) {
                            innerLabels.add(members.indexOf(m));
                            found = true;
                        }
                    }
                    if (!found) {
                        throw new TypeDescriptorException("UnionTypeDescriptorImpl - Label " + label + " is not amongst the possible enum values."); 
                    }
                }
                else
                {
                    throw new TypeDescriptorException("UnionTypeDescriptorImpl - Type " + label.getClass() + " does not match discriminator kind (" + this.m_discriminator.getKind() + ").");
                }
            }
            addMember(name, typeDescriptor, isDefault, innerLabels);
            break;
        }
        
        default:
            throw new TypeDescriptorException("UnionTypeDescriptorImpl - A union member cannot be of type " + typeDescriptor.getKind() + ".");
        }
        
        if (isDefault) {
            if (this.m_defaultIndex == -1) {
                this.m_defaultIndex = this.m_members.size()-1;
            }
        }

        return true;
    }
    
    private <T> void addMember(String name, DataTypeDescriptor typeDescriptor, boolean isDefault, ArrayList<T> innerLabels) {
        if (!exists(name)) {
            UnionMemberImpl<T> member = new UnionMemberImpl<T>(typeDescriptor, name, innerLabels, isDefault);
            this.m_members.add(member);
        } else {
            throw new TypeDescriptorException("UnionTypeDescriptorImpl - There is another existing member in this union with the name " + name + ".");
        }
    }
    
    public DataTypeDescriptor getDiscriminator() {
        return this.m_discriminator;
    }
    
    private boolean exists(String name) {
        for (Member member : this.m_members) {
            if (member.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean hasDefaultValue() {
        return this.m_defaultIndex != -1;
    }
    
    public int getDefaultIndex() {
        return this.m_defaultIndex;
    }

}
