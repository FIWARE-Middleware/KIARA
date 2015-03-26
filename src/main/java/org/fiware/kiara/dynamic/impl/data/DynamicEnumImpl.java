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

import java.io.IOException;

import org.fiware.kiara.dynamic.data.DynamicEnum;
import org.fiware.kiara.exceptions.DynamicTypeException;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.data.EnumTypeDescriptor;
import org.fiware.kiara.typecode.data.Member;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*
*/
public class DynamicEnumImpl extends DynamicMemberedImpl implements DynamicEnum {
    
    private int m_chosenValue = -1; // Default

    public DynamicEnumImpl(DataTypeDescriptor dataDescriptor) {
        super(dataDescriptor, "DynamicEnumImpl");
    }

    @Override
    public void set(String value) {
        int index = 0;
        for (Member member : ((EnumTypeDescriptor) this.m_typeDescriptor).getMembers()) {
            if (value.equals(member.getName())) {
                this.m_chosenValue = index;
                return;
            }
            index++;
        }
        throw new DynamicTypeException(this.m_className + " - The value specified is not amongst the possible enumeration values.");
    }

    @Override
    public String get() {
        if (this.m_chosenValue != -1) {
            return this.m_members.get(this.m_chosenValue).getName();
        }
        return null;
    }
    
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        if (this.m_chosenValue == -1) {
            throw new DynamicTypeException(this.m_className + " - Error serializing. No value has been set for this enumeration.");
        }
        impl.serializeUI32(message, name, this.m_chosenValue);
    }
 
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.m_chosenValue = impl.deserializeUI32(message, name);
    }
    
    @Override
    public boolean equals(Object anotherObject) {
        if (anotherObject instanceof DynamicEnumImpl) {
            return this.m_chosenValue == ((DynamicEnumImpl) anotherObject).m_chosenValue;
        }
        return false;
    }
    
    public String getValueAt(int index) {
        return this.m_members.get(index).getName();
    }
    
    public int getChosenValueIndex() {
        return this.m_chosenValue;
    }
    
    public void setChosenValueIndex(int index) {
        this.m_chosenValue = index;
    }
    
}
