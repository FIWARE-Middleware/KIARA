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

import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.EnumTypeDescriptor;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*
*/
public class EnumTypeDescriptorImpl extends MemberedTypeDescriptorImpl implements EnumTypeDescriptor {
    
    @SuppressWarnings("unused")
    //private String m_name;

    public EnumTypeDescriptorImpl(String name) {
        super(TypeKind.ENUM_TYPE, name);
     //   this.m_name = name;
    }
    
    @Override
    public boolean isEnum() {
        return true;
    }

    @Override
    public void addValue(String value) {
        this.m_members.add(new EnumMemberImpl(value));
    }
    
    

}
