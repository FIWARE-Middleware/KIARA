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

import org.fiware.kiara.exceptions.TypeDescriptorException;
import org.fiware.kiara.typecode.TypeDescriptor;
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.data.ExceptionTypeDescriptor;
import org.fiware.kiara.typecode.data.Member;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*
*/
public class ExceptionTypeDescriptorImpl extends MemberedTypeDescriptorImpl implements ExceptionTypeDescriptor {
    
    //private String m_message;

    public ExceptionTypeDescriptorImpl(String name) {
        super(TypeKind.EXCEPTION_TYPE);
       // this.m_message = message;
    }
    
    @Override
    public boolean isException() {
        return true;
    }
    
    /*public String getMessage() {
        return this.m_message;
    }*/

    @Override
    public void addMember(TypeDescriptor member, String name) {
        if (member instanceof DataTypeDescriptor) {
            if (!this.exists(name)) {
                this.m_members.add(new MemberImpl((DataTypeDescriptor) member, name));
            } else {
                throw new TypeDescriptorException("ExceptionTypeDescriptorImpl - A member with name " + name + " already exists in this exception.");
            }
        } else {
            throw new TypeDescriptorException("ExceptionTypeDescriptorImpl - A TypeDescriptor of type " + member.getKind() + " cannot be added. Only DataTypeDescriptor objects allowed.");
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
