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

import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.data.Member;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*
*/
public class MemberImpl implements Member {
    
    private String m_name;
    private DataTypeDescriptor m_typeDescriptor;
    
    public MemberImpl(DataTypeDescriptor typeDescriptor, String name) {
        this.m_name = name;
        this.m_typeDescriptor = typeDescriptor;
    }

    @Override
    public String getName() {
        return this.m_name;
    }

    @Override
    public DataTypeDescriptor getTypeDescriptor() {
        return this.m_typeDescriptor;
    }

}
