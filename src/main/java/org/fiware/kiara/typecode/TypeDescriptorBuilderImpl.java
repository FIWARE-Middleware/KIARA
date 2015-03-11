package org.fiware.kiara.typecode;

import org.fiware.kiara.typecode.data.ArrayTypeDescriptor;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.data.EnumTypeDescriptor;
import org.fiware.kiara.typecode.data.ListTypeDescriptor;
import org.fiware.kiara.typecode.data.MapTypeDescriptor;
import org.fiware.kiara.typecode.data.PrimitiveTypeDescriptor;
import org.fiware.kiara.typecode.data.SetTypeDescriptor;
import org.fiware.kiara.typecode.data.StructTypeDescriptor;
import org.fiware.kiara.typecode.data.UnionTypeDescriptor;
import org.fiware.kiara.typecode.impl.data.ArrayTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.EnumTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.ListTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.MapTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.PrimitiveTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.SetTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.StructTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.UnionTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.services.FunctionTypeDescriptorImpl;
import org.fiware.kiara.typecode.services.FunctionTypeDescriptor;

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
    
    
    /*@Override
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
            System.out.println("NOT SUPPORTED YET");
            break;
        case UNION_TYPE:
            System.out.println("NOT SUPPORTED YET");
            break;
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
     */

}
