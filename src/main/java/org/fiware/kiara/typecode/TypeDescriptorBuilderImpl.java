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
package org.fiware.kiara.typecode;

import org.fiware.kiara.typecode.data.ArrayTypeDescriptor;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.data.EnumTypeDescriptor;
import org.fiware.kiara.typecode.data.ExceptionTypeDescriptor;
import org.fiware.kiara.typecode.data.ListTypeDescriptor;
import org.fiware.kiara.typecode.data.MapTypeDescriptor;
import org.fiware.kiara.typecode.data.PrimitiveTypeDescriptor;
import org.fiware.kiara.typecode.data.SetTypeDescriptor;
import org.fiware.kiara.typecode.data.StructTypeDescriptor;
import org.fiware.kiara.typecode.data.UnionTypeDescriptor;
import org.fiware.kiara.typecode.impl.data.ArrayTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.EnumTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.ExceptionTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.ListTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.MapTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.PrimitiveTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.SetTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.StructTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.UnionTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.services.FunctionTypeDescriptorImpl;
import org.fiware.kiara.typecode.services.FunctionTypeDescriptor;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*
*/
public class TypeDescriptorBuilderImpl implements TypeDescriptorBuilder {
    
    private TypeDescriptorBuilderImpl() {}
    
    private static class LazyDescriptorBuilderHolder {
        private static TypeDescriptorBuilderImpl m_instance = new TypeDescriptorBuilderImpl();
    }

    public static TypeDescriptorBuilder getInstance() {
        return LazyDescriptorBuilderHolder.m_instance;
    }

    @Override
    public PrimitiveTypeDescriptor createPrimitiveType(TypeKind kind) {
        return new PrimitiveTypeDescriptorImpl(kind);
    }

    @Override
    public ArrayTypeDescriptor createArrayType(DataTypeDescriptor contentDescriptor, int... dimensionsLength) {
        ArrayTypeDescriptor ret = new ArrayTypeDescriptorImpl();
        ret.setContentType(contentDescriptor);
        ret.setDimensionsLength(dimensionsLength);
        return ret;
    }

    @Override
    public ListTypeDescriptor createListType(DataTypeDescriptor contentDescriptor, int maxSize) {
        ListTypeDescriptor ret = new ListTypeDescriptorImpl();
        ret.setContentType(contentDescriptor);
        ret.setMaxSize(maxSize);
        return ret;
    }
    
    @Override
    public SetTypeDescriptor createSetType(DataTypeDescriptor contentDescriptor, int maxSize) {
        SetTypeDescriptor ret = new SetTypeDescriptorImpl();
        ret.setContentType(contentDescriptor);
        ret.setMaxSize(maxSize);
        return ret;
    }
    
    @Override
    public MapTypeDescriptor createMapType(DataTypeDescriptor keyDescriptor, DataTypeDescriptor valueDescriptor, int maxSize) {
        MapTypeDescriptor ret = new MapTypeDescriptorImpl();
        ret.setKeyTypeDescriptor(keyDescriptor);
        ret.setValueTypeDescriptor(valueDescriptor);
        ret.setMaxSize(maxSize);
        return ret;
    }
    
    @Override
    public StructTypeDescriptor createStructType(String name) {
        StructTypeDescriptor ret = new StructTypeDescriptorImpl(name);
        return ret;
    }

    @Override
    public FunctionTypeDescriptor createFunctionType(String name) {
        FunctionTypeDescriptor ret = new FunctionTypeDescriptorImpl(name);
        return ret;
    }

    @Override
    public EnumTypeDescriptor createEnumType(String name, String... values) {
        EnumTypeDescriptorImpl ret = new EnumTypeDescriptorImpl(name);
        for (String value : values) {
            ret.addValue(value);
        }
        return ret;
    }

    @Override
    public UnionTypeDescriptor createUnionType(String name, DataTypeDescriptor discriminatorDescriptor) {
        UnionTypeDescriptorImpl ret = new UnionTypeDescriptorImpl(name, discriminatorDescriptor);
        return ret;
    }

    @Override
    public ExceptionTypeDescriptor createExceptionType(String name, String message) {
        ExceptionTypeDescriptor ret = new ExceptionTypeDescriptorImpl(name, message);
        return ret;
    }
    
    
    

}
