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

import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.Member;
import org.fiware.kiara.typecode.data.MemberedTypeDescriptor;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*
*/
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
