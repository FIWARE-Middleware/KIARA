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
package org.fiware.kiara.typecode.impl;

import org.fiware.kiara.typecode.TypeDescriptor;
import org.fiware.kiara.typecode.TypeKind;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*
*/
public class TypeDescriptorImpl implements TypeDescriptor {

    protected TypeKind m_kind = null;
    
    /*
     * Public Methods
     */
    
    protected TypeDescriptorImpl(TypeKind kind) {
        this.m_kind = kind;
    }
    
    @Override
    public boolean isData() {
        return false;
    }
    
    @Override
    public TypeKind getKind() {
        return this.m_kind;
    }
    
    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public boolean isContainer() {
        return false;
    }
    
    @Override
    public boolean isArray() {
        return false;
    }
    
    @Override
    public boolean isList() {
        return false;
    }
    
    @Override
    public boolean isMap() {
        return false;
    }
    
    @Override
    public boolean isSet() {
        return false;
    }
    
    @Override
    public boolean isMembered() {
        return false;
    }
    
    @Override
    public boolean isEnum() {
        return false;
    }
    
    @Override
    public boolean isUnion() {
        return false;
    }
    
    @Override
    public boolean isStruct() {
        return false;
    }
    
    @Override
    public boolean isException() {
        return false;
    }
    
    // ---------------------------- Services ----------------------------
    
    @Override
    public boolean isService() {
        return false;
    }

    @Override
    public boolean isFunction() {
        return false;
    }
    
}
