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
package org.fiware.kiara.dynamic.impl.data;

import org.fiware.kiara.dynamic.data.DynamicData;
import org.fiware.kiara.dynamic.data.DynamicPrimitive;
import org.fiware.kiara.exceptions.DynamicTypeException;
import org.fiware.kiara.typecode.data.PrimitiveTypeDescriptor;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*
*/
public class DynamicPrimitiveImpl extends DynamicDataImpl implements DynamicPrimitive {
    
    private Object m_value;
    private int m_maxLength;
    
    public DynamicPrimitiveImpl(PrimitiveTypeDescriptor dataDescriptor) {
        super(dataDescriptor, "DynamicPrimitiveImpl");
        this.initialize(dataDescriptor);
    }
    
    @Override
    public boolean set(Object value) {
        Class<?> c = value.getClass();
        if (isPrimitive(c)) {
            if(this.typeFits(value)) {
                checkStringSize(value);
                this.m_value = value; // TODO Check if value is primitive or not
                return true;
            } else {
                throw new DynamicTypeException(this.m_className + " - A value of type " + value.getClass() + " cannot be assigned to a " + this.m_typeDescriptor.getKind() + " dynamic type.");
            }
        }
        
        return false;
    }
    
    @Override
    public boolean set(DynamicData value) {
        if (value instanceof DynamicPrimitive) {
            DynamicPrimitiveImpl primitiveType = (DynamicPrimitiveImpl) value;
            if (primitiveType.m_typeDescriptor.getKind() == this.m_typeDescriptor.getKind()) {
                if(this.typeFits(primitiveType.get())) {
                    this.m_value = primitiveType.get();
                    return true;
                }
            }
        }
        
        return false;
    }
    
    @Override
    public Object get() {
        return this.m_value;
    }
    
    @Override
    public boolean equals(Object anotherObject) {
        if (anotherObject instanceof DynamicPrimitive) {
            if (((DynamicPrimitive) anotherObject).getTypeDescriptor().getKind() == this.m_typeDescriptor.getKind()) {
                if (((DynamicPrimitive) anotherObject).get().equals(this.m_value)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private void checkStringSize(Object value) {
        if (value.getClass().equals(String.class)) {
            String stringValue = (String) value;
            if (stringValue.length() > this.m_maxLength) {
                throw new DynamicTypeException(this.m_className + " - The length of the String value cannot greater than the one specified in the type descriptor.");
            }
        }
    }
    
    private boolean isPrimitive(Class<?> c) {
        if (c.equals(Byte.class) || 
            c.equals(Short.class) || 
            c.equals(Integer.class) || 
            c.equals(Long.class) || 
            c.equals(Float.class) || 
            c.equals(Double.class) || 
            c.equals(Boolean.class) || 
            c.equals(Character.class) || 
            c.equals(String.class))
        {
                return true;
        }
        
        return false;
    }
    
    private void initialize(PrimitiveTypeDescriptor dataDescriptor) {
        switch (this.m_typeDescriptor.getKind()) { 
        case BOOLEAN_TYPE:
            this.m_value = false;
            break;
        case BYTE_TYPE:
            this.m_value = (byte) 0;
            break;
        case INT_16_TYPE:
        case UINT_16_TYPE:
            this.m_value = (short) 0;
            break;
        case INT_32_TYPE:
        case UINT_32_TYPE:
            this.m_value = (int) 0;
            break;
        case INT_64_TYPE:
        case UINT_64_TYPE:
            this.m_value = (long) 0;
            break;
        case FLOAT_32_TYPE:
            this.m_value = (float) 0.0;
            break;
        case FLOAT_64_TYPE:
            this.m_value = (double) 0.0;
            break;
        case CHAR_8_TYPE:
            this.m_value = (char) '0';
            break;
        case STRING_TYPE:
            this.m_maxLength = dataDescriptor.getMaxFixedLength();
            this.m_value = (String) "";
            break;
        default:
            this.m_value = null;
            break;
        }
    }
    
    private boolean typeFits(Object value) {
        Class<?> c = value.getClass();
        switch (this.m_typeDescriptor.getKind()) { 
        case BOOLEAN_TYPE:
            if(!c.equals(Boolean.class)) {
                return false;
            }
            break;
        case BYTE_TYPE:
            if(!c.equals(Byte.class)) {
                return false;
            }
            break;
        case INT_16_TYPE:
        case UINT_16_TYPE:
            if(!c.equals(Short.class)) {
                return false;
            }
            break;
        case INT_32_TYPE:
        case UINT_32_TYPE:
            if(!c.equals(Integer.class)) {
                return false;
            }
            break;
        case INT_64_TYPE:
        case UINT_64_TYPE:
            if(!c.equals(Long.class)) {
                return false;
            }
            break;
        case FLOAT_32_TYPE:
            if(!c.equals(Float.class)) {
                return false;
            }
            break;
        case FLOAT_64_TYPE:
            if(!c.equals(Double.class)) {
                return false;
            }
            break;
        case CHAR_8_TYPE:
            if(!c.equals(Character.class)) {
                return false;
            }
            break;
        case STRING_TYPE:
            if(!c.equals(String.class)) {
                return false;
            }
            break;
        default:
            return false;
        }
        
        return true;
    }

    

}
