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
import org.fiware.kiara.typecode.data.PrimitiveTypeDescriptor;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*
*/
public class PrimitiveTypeDescriptorImpl extends DataTypeDescriptorImpl implements PrimitiveTypeDescriptor {

    private boolean m_primitive = false;
    private int m_maxFixedLength = 0;
    
    public PrimitiveTypeDescriptorImpl(TypeKind kind) {
        super(kind);
        this.initialize();
    }
    
    @Override
    public boolean isData() {
        return true;
    }
    
    @Override
    public boolean isPrimitive() {
        return this.m_primitive; 
    }
    
    @Override
    public boolean isString() {
        return this.m_kind == TypeKind.STRING_TYPE;
    }

    @Override
    public PrimitiveTypeDescriptor setMaxFixedLength(int length) {
        if (this.m_kind == TypeKind.STRING_TYPE) {
            this.m_maxFixedLength = length;
        } else {
            throw new TypeDescriptorException("PrimitiveTypeDescriptor - Only PrimitiveTypeDescriptor objects whose kind is STRING_TYPE are allowed to have maximum fixed length.");
        }
        return this;
    }

    @Override
    public int getMaxFixedLength() {
        if (this.m_kind == TypeKind.STRING_TYPE) {
            return this.m_maxFixedLength;
        } else {
            throw new TypeDescriptorException("PrimitiveTypeDescriptor - Only PrimitiveTypeDescriptor objects whose kind is STRING_TYPE are allowed to have maximum fixed length.");
        }
    }
    
    /*
     * Private Methods
     */
    
    private void initialize() {
        switch (this.m_kind) {
            case BOOLEAN_TYPE:
                this.m_primitive = true;
                break;
            case BYTE_TYPE:
                this.m_primitive = true;
                break;
            case INT_16_TYPE:
                this.m_primitive = true;
                break;
            case INT_32_TYPE:
                this.m_primitive = true;
                break;
            case INT_64_TYPE:
                this.m_primitive = true;
                break;
            case FLOAT_32_TYPE:
                this.m_primitive = true;
                break;
            case FLOAT_64_TYPE:
                this.m_primitive = true;
                break;
            case CHAR_8_TYPE:
                this.m_primitive = true;
                break;
            case STRING_TYPE:
                this.m_primitive = true;
                this.m_maxFixedLength = 255;
                break;
            default:
                break;
        }
    }

}
