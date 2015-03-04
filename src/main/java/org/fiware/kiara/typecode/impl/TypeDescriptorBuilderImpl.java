package org.fiware.kiara.typecode.impl;

import org.fiware.kiara.typecode.TypeDescriptor;
import org.fiware.kiara.typecode.TypeDescriptorBuilder;
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.impl.data.ArrayTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.ListTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.MapTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.PrimitiveTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.SetTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.StructTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.services.FunctionTypeDescriptorImpl;

public class TypeDescriptorBuilderImpl implements TypeDescriptorBuilder {
    
    private static TypeDescriptorBuilderImpl m_instance = null;
    
    private TypeDescriptorBuilderImpl() {
        
    }

    public static TypeDescriptorBuilder getInstance() {
        if (m_instance == null) {
            m_instance = new TypeDescriptorBuilderImpl();
        }
        return m_instance;
    }

    @Override
    public TypeDescriptor createTypeDescriptor(TypeKind kind, String name) {
        switch(kind) {
        case BOOLEAN_TYPE:
        case BYTE_TYPE:
        case INT_16_TYPE:
        case UINT_16_TYPE:
        case INT_32_TYPE:
        case UINT_32_TYPE:
        case INT_64_TYPE:
        case UINT_64_TYPE:
        case FLOAT_32_TYPE:
        case FLOAT_64_TYPE:
        case CHAR_8_TYPE:
        case STRING_TYPE:
            return new PrimitiveTypeDescriptorImpl(kind);
        case ARRAY_TYPE:
            return new ArrayTypeDescriptorImpl();
        case LIST_TYPE:
            return new ListTypeDescriptorImpl();
        case MAP_TYPE:
            return new MapTypeDescriptorImpl();
        case SET_TYPE:
            return new SetTypeDescriptorImpl();
        case ENUM_TYPE:
        case UNION_TYPE:
        case STRUCT_TYPE:
            return new StructTypeDescriptorImpl(name);
        case EXCEPTION_TYPE:
            System.out.println("NOT SUPPORTED YET");
            break;
        case SERVICE_TYPE:
            System.out.println("NOT SUPPORTED YET");
            break;
        case FUNCTION_TYPE:
            return new FunctionTypeDescriptorImpl(name);
        default:
            break;
        }
        return null;
    }

}
