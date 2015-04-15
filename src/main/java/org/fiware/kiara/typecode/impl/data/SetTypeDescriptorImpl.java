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
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.data.SetTypeDescriptor;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*
*/
public class SetTypeDescriptorImpl extends ContainerTypeDescriptorImpl implements SetTypeDescriptor {

    public SetTypeDescriptorImpl() {
        super(TypeKind.SET_TYPE);
    }
    
    @Override
    public boolean isSet() {
        return true;
    }
    
    @Override
    public void setMaxSize(int size) {
        if (size <= 0) {
            throw new TypeDescriptorException("SetTypeDescriptorImpl - Maximum set size must be greater than zero.");
        }
        super.setMaxSize(size);
    }
    
    @Override
    public boolean setElementType(DataTypeDescriptor contentType) {
        this.m_contentType = contentType;
        return true;
    }
    
    @Override
    public DataTypeDescriptor getElementType() {
        return this.m_contentType;
    }

}
