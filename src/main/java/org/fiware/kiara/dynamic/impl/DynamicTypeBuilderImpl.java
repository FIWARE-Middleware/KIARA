package org.fiware.kiara.dynamic.impl;

import org.fiware.kiara.dynamic.DynamicTypeBuilder;
import org.fiware.kiara.dynamic.impl.data.DynamicArrayImpl;
import org.fiware.kiara.dynamic.impl.data.DynamicDataImpl;
import org.fiware.kiara.dynamic.impl.data.DynamicListImpl;
import org.fiware.kiara.dynamic.impl.data.DynamicPrimitiveImpl;
import org.fiware.kiara.dynamic.impl.services.DynamicFunction;
import org.fiware.kiara.exceptions.DynamicTypeException;
import org.fiware.kiara.typecode.impl.data.ArrayTypeDescriptor;
import org.fiware.kiara.typecode.impl.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.impl.data.PrimitiveTypeDescriptor;
import org.fiware.kiara.typecode.impl.data.SequenceTypeDescriptor;
import org.fiware.kiara.typecode.impl.services.FunctionTypeDescriptor;

public class DynamicTypeBuilderImpl implements DynamicTypeBuilder {
    
    private static DynamicTypeBuilderImpl instance = null;
    
    protected DynamicTypeBuilderImpl() { 
        // Makes constructor not accessible.
    }
    
    public static DynamicTypeBuilder getInstance() {
        if (instance == null) {
            instance = new DynamicTypeBuilderImpl();
        }
        
        return instance;
    }
    
    @Override
    public DynamicFunction createFunction(FunctionTypeDescriptor functionDescriptor) {
        return new DynamicFunction(functionDescriptor);
    }

    @Override
    public DynamicDataImpl createData(DataTypeDescriptor dataDescriptor) {
        switch (dataDescriptor.getKind()) {
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
            return this.createPrimitiveType((PrimitiveTypeDescriptor) dataDescriptor);
        case ARRAY_TYPE:
            return this.createArrayType((ArrayTypeDescriptor) dataDescriptor);
        case SEQUENCE_TYPE:
            return this.createListType((SequenceTypeDescriptor) dataDescriptor);
        case MAP_TYPE:
        case SET_TYPE:
        
        case ENUM_TYPE:
        case UNION_TYPE:
        case STRUCT_TYPE:
        case EXCEPTION_TYPE:
        default:
            break;
        }
        
        return null;
    }
    
    private DynamicPrimitiveImpl createPrimitiveType(PrimitiveTypeDescriptor dataDescriptor) {
        return new DynamicPrimitiveImpl(dataDescriptor);
    }
    
    private DynamicArrayImpl createArrayType(ArrayTypeDescriptor arrayDescriptor) {
        DynamicArrayImpl ret = new DynamicArrayImpl(arrayDescriptor);
        if (arrayDescriptor.getContentType() == null) {
            throw new DynamicTypeException("DynamicTypeBuilder - The content type for this array descriptor has not been defined.");
        }
        ret.setContentType(this.createData(arrayDescriptor.getContentType()));
        for (int i=0; i < arrayDescriptor.getLinearSize(); ++i) {
            ret.addElement(this.createData(arrayDescriptor.getContentType()));
        }
        return ret;
    }
    
    private DynamicListImpl createListType(SequenceTypeDescriptor listDescriptor) {
        DynamicListImpl ret = new DynamicListImpl(listDescriptor);
        if (listDescriptor.getContentType() == null) {
            throw new DynamicTypeException("DynamicTypeBuilder - The content type for this array descriptor has not been defined.");
        }
        ret.setContentType(this.createData(listDescriptor.getContentType()));
        for (int i=0; i < listDescriptor.getMaxSize(); ++i) {
            ret.setElementAt(this.createData(listDescriptor.getContentType()), i);
        }
        return ret;
    }

}
