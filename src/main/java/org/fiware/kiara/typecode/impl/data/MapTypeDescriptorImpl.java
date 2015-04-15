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
import org.fiware.kiara.typecode.data.MapTypeDescriptor;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*
*/
public class MapTypeDescriptorImpl extends ContainerTypeDescriptorImpl implements MapTypeDescriptor {

    private DataTypeDescriptor m_keyDescriptor = null;
    
    private int m_maximumSize;
    
    public MapTypeDescriptorImpl() {
        super(TypeKind.MAP_TYPE);
    }
    
    @Override
    public boolean isMap() {
        return true;
    }
    
    @Override
    public boolean setKeyTypeDescriptor(DataTypeDescriptor keyDescriptor) {
        if (this.m_kind == TypeKind.MAP_TYPE) {
            this.m_keyDescriptor = keyDescriptor;
            return true;
        }
        return false;
    }
    
    @Override
    public boolean setValueTypeDescriptor(DataTypeDescriptor valueDescriptor) {
        if (this.m_kind == TypeKind.MAP_TYPE) {
            this.m_contentType = valueDescriptor;
            return true;
        }
        return false;
    }
    
    @Override
    public DataTypeDescriptor getKeyTypeDescriptor() {
        return this.m_keyDescriptor;
    }

    @Override
    public DataTypeDescriptor getValueTypeDescriptor() {
        return this.m_contentType;
    }
    
    @Override
    public void setMaxSize(int size) {
        if (size <= 0) {
            throw new TypeDescriptorException("ListTypeDescriptor - Maximum map size must be greater than zero.");
        }
        this.m_maximumSize = size;
    }
    
    @Override
    public int getMaxSize() {
        return this.m_maximumSize;
    }
    
    

}
