package org.fiware.kiara.dynamic;

import java.util.ArrayList;

import org.fiware.kiara.dynamic.data.DynamicData;
import org.fiware.kiara.dynamic.data.DynamicEnum;
import org.fiware.kiara.dynamic.data.DynamicPrimitive;
import org.fiware.kiara.dynamic.data.DynamicStruct;
import org.fiware.kiara.dynamic.data.DynamicUnion;
import org.fiware.kiara.dynamic.impl.data.DynamicArrayImpl;
import org.fiware.kiara.dynamic.impl.data.DynamicDataImpl;
import org.fiware.kiara.dynamic.impl.data.DynamicEnumImpl;
import org.fiware.kiara.dynamic.impl.data.DynamicListImpl;
import org.fiware.kiara.dynamic.impl.data.DynamicMapImpl;
import org.fiware.kiara.dynamic.impl.data.DynamicPrimitiveImpl;
import org.fiware.kiara.dynamic.impl.data.DynamicSetImpl;
import org.fiware.kiara.dynamic.impl.data.DynamicStructImpl;
import org.fiware.kiara.dynamic.impl.data.DynamicUnionImpl;
import org.fiware.kiara.dynamic.impl.services.DynamicFunction;
import org.fiware.kiara.exceptions.DynamicTypeException;
import org.fiware.kiara.typecode.data.ArrayTypeDescriptor;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.data.EnumTypeDescriptor;
import org.fiware.kiara.typecode.data.ListTypeDescriptor;
import org.fiware.kiara.typecode.data.MapTypeDescriptor;
import org.fiware.kiara.typecode.data.Member;
import org.fiware.kiara.typecode.data.PrimitiveTypeDescriptor;
import org.fiware.kiara.typecode.data.SetTypeDescriptor;
import org.fiware.kiara.typecode.data.StructTypeDescriptor;
import org.fiware.kiara.typecode.data.UnionMember;
import org.fiware.kiara.typecode.data.UnionTypeDescriptor;
import org.fiware.kiara.typecode.impl.data.ArrayTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.DataTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.EnumMemberImpl;
import org.fiware.kiara.typecode.impl.data.MapTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.PrimitiveTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.ListTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.SetTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.UnionMemberImpl;
import org.fiware.kiara.typecode.impl.data.UnionTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.services.FunctionTypeDescriptorImpl;
import org.fiware.kiara.typecode.services.FunctionTypeDescriptor;

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
        return new DynamicFunction((FunctionTypeDescriptorImpl) functionDescriptor);
    }

    @Override
    public DynamicData createData(DataTypeDescriptor dataDescriptor) {
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
        case LIST_TYPE:
            return this.createListType((ListTypeDescriptor) dataDescriptor);
        case MAP_TYPE:
            return this.createMapType((MapTypeDescriptor) dataDescriptor);
        case SET_TYPE:
            return this.createSetType((SetTypeDescriptor) dataDescriptor);
        case ENUM_TYPE:
            return this.createEnumType((EnumTypeDescriptor) dataDescriptor);
        case UNION_TYPE:
            return this.createUnionType((UnionTypeDescriptor) dataDescriptor);
        case STRUCT_TYPE:
            return this.createStructType((StructTypeDescriptor) dataDescriptor);
        case EXCEPTION_TYPE:
        default:
            break;
        }
        
        return null;
    }
    
    private DynamicPrimitive createPrimitiveType(PrimitiveTypeDescriptor dataDescriptor) {
        return new DynamicPrimitiveImpl(dataDescriptor);
    }
    
    private DynamicArrayImpl createArrayType(ArrayTypeDescriptor arrayDescriptor) {
        DynamicArrayImpl ret = new DynamicArrayImpl(arrayDescriptor);
        if (arrayDescriptor.getContentType() == null) {
            throw new DynamicTypeException("DynamicTypeBuilder - The content type for this array descriptor has not been defined.");
        }
        ret.setContentType(this.createData(arrayDescriptor.getContentType()));
        for (int i=0; i < arrayDescriptor.getMaxSize(); ++i) {
            ret.addElement(this.createData(arrayDescriptor.getContentType()));
        }
        return ret;
    }
    
    private DynamicListImpl createListType(ListTypeDescriptor listDescriptor) {
        DynamicListImpl ret = new DynamicListImpl(listDescriptor);
        if (listDescriptor.getContentType() == null) {
            throw new DynamicTypeException("DynamicTypeBuilder - The content type for this list descriptor has not been defined.");
        }
        ret.setContentType(this.createData(listDescriptor.getContentType()));
        return ret;
    }
    
    private DynamicSetImpl createSetType(SetTypeDescriptor setDescriptor) {
        DynamicSetImpl ret = new DynamicSetImpl(setDescriptor);
        if (setDescriptor.getContentType() == null) {
            throw new DynamicTypeException("DynamicTypeBuilder - The content type for this set descriptor has not been defined.");
        }
        
        ret.setContentType(this.createData(setDescriptor.getContentType()));
        return ret;
    }
    
    private DynamicMapImpl createMapType(MapTypeDescriptor mapDescriptor) {
        if (mapDescriptor.getKeyTypeDescriptor() == null) {
            throw new DynamicTypeException("DynamicTypeBuilder - The content key type for this map descriptor has not been defined.");
        }
        
        if (mapDescriptor.getValueTypeDescriptor() == null) {
            throw new DynamicTypeException("DynamicTypeBuilder - The content value type for this map descriptor has not been defined.");
        }
        
        DynamicMapImpl ret = new DynamicMapImpl(mapDescriptor);
        
        ret.setKeyContentType(this.createData((DataTypeDescriptor) mapDescriptor.getKeyTypeDescriptor()));
        ret.setValueContentType(this.createData((DataTypeDescriptor) mapDescriptor.getValueTypeDescriptor()));
        return ret;
    }
    
    private DynamicStruct createStructType(StructTypeDescriptor dataDescriptor) {
        DynamicStructImpl ret = new DynamicStructImpl(dataDescriptor);
        for (Member member : dataDescriptor.getMembers()) {
            DynamicData dynData = this.createData(member.getTypeDescriptor());
            ret.addMember(dynData, member.getName());
        }
        return ret;
    }
    
    private DynamicEnum createEnumType(EnumTypeDescriptor dataDescriptor) {
        DynamicEnumImpl ret = new DynamicEnumImpl(dataDescriptor);
        for (Member member : dataDescriptor.getMembers()) {
            ret.addMember(null, member.getName());
        }
        return ret;
    }
    
    private DynamicUnion createUnionType(UnionTypeDescriptor dataDescriptor) {
        if (dataDescriptor.getMembers().size() == 0) {
            throw new DynamicTypeException("DynamicTypeBuilder - No members have been assigned to this enumeration.");
        }
        DynamicUnionImpl ret = new DynamicUnionImpl(dataDescriptor);
        ret.setDiscriminator(this.createData(((UnionTypeDescriptorImpl) dataDescriptor).getDiscriminator()));
        for (Member member : dataDescriptor.getMembers()) {
            UnionMemberImpl<?> unionMember = (UnionMemberImpl<?>) member;
            DynamicData dynData = this.createData(unionMember.getTypeDescriptor());
            ret.addMember(dynData, unionMember.getName(), unionMember.getLabels(), unionMember.isDefault());
        }
        ret.setDefaultDiscriminatorValue();
        return ret;
    }
    
    /*private ArrayList<DynamicData> createDynamicLabels(UnionMemberImpl unionMember) {
        ArrayList<DynamicData> labels = new ArrayList<DynamicData>();
        for (Object label : unionMember.getLabels()) {
           this.createData(dataDescriptor)
        }
        return labels;
    }*/

}
