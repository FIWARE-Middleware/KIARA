package org.fiware.kiara.impl;

import java.util.ArrayList;

import org.fiware.kiara.dynamic.services.DynamicProxy;
import org.fiware.kiara.serialization.Serializer;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.transport.Transport;
//import org.fiware.kiara.generator.util.Utils;
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
import org.fiware.kiara.typecode.impl.FunctionTypeDescriptor;
import org.fiware.kiara.typecode.services.ServiceTypeDescriptor;

import com.eprosima.idl.parser.tree.Definition;
import com.eprosima.idl.parser.tree.DefinitionContainer;
import com.eprosima.idl.parser.tree.Module;
import com.eprosima.idl.parser.tree.Interface;
import com.eprosima.idl.parser.tree.Operation;
import com.eprosima.idl.parser.tree.Param;
import com.eprosima.idl.parser.typecode.ArrayTypeCode;
import com.eprosima.idl.parser.typecode.EnumTypeCode;
import com.eprosima.idl.parser.typecode.MapTypeCode;
import com.eprosima.idl.parser.typecode.Member;
import com.eprosima.idl.parser.typecode.SequenceTypeCode;
import com.eprosima.idl.parser.typecode.SetTypeCode;
import com.eprosima.idl.parser.typecode.StructTypeCode;
import com.eprosima.idl.parser.typecode.TypeCode;
import com.eprosima.idl.parser.typecode.UnionMember;
import com.eprosima.idl.parser.typecode.UnionTypeCode;

import java.util.List;

import org.fiware.kiara.Kiara;

public class TypeMapper {

    public static List<DynamicProxy> processTree(ParserContextImpl ctx, Serializer serializer, Transport transport) {
        return createProxies(getServiceTypes(ctx), serializer, transport);
    }

    public static List<DynamicProxy> createProxies(List<ServiceTypeDescriptor> serviceTypes, Serializer serializer, Transport transport) {
        ArrayList<DynamicProxy> proxies = new ArrayList<>();

        // For now, we only need serviceTypes, but the rest of the type definitions will be needed for publish-subscribe
        for (ServiceTypeDescriptor serviceType : serviceTypes) {
            final DynamicProxy proxy = Kiara.getDynamicValueBuilder().createService(serviceType, (SerializerImpl) serializer, transport);
            proxies.add(proxy);
        }

        return proxies;
    }

    public static DynamicProxy createDynamicProxy(ServiceTypeDescriptor serviceType, Serializer serializer, Transport transport) {
        return Kiara.getDynamicValueBuilder().createService(serviceType, (SerializerImpl) serializer, transport);
    }

    public static List<ServiceTypeDescriptor> getServiceTypes(ParserContextImpl ctx) {
        final List<ServiceTypeDescriptor> serviceTypes = new ArrayList<>();
        for (Definition definition : ctx.getDefinitions()) {
            addServiceTypes(definition, serviceTypes);
        }
        return serviceTypes;
    }

    private static void addServiceTypes(Definition definition, List<ServiceTypeDescriptor> serviceTypes) {
        if (definition.isIsInterface()) {
            Interface ifz = (Interface) definition;
            ServiceTypeDescriptor serviceDesc = mapService(ifz);
            for (Operation operation : ifz.getAll_operations()) {
                FunctionTypeDescriptor functionDesc = mapFunction(operation);
                if (functionDesc != null) {
                    serviceDesc.addFunction(functionDesc);
                }
            }

            serviceTypes.add(serviceDesc);
        } else if (definition.isIsModule()) {
            for (Definition moduleDefinition : ((Module) definition).getDefinitions()) {
                addServiceTypes(moduleDefinition, serviceTypes);
            }
        }
    }

    private static ServiceTypeDescriptor mapService(Interface ifz) {
        ServiceTypeDescriptor service = Kiara.getTypeDescriptorBuilder().createServiceType(ifz.getName(), ifz.getScopedname());
        return service;
    }

    private static FunctionTypeDescriptor mapFunction(Operation operation) {
        FunctionTypeDescriptor functionDesc = Kiara.getTypeDescriptorBuilder().createFunctionType(operation.getName());
        functionDesc.setReturnType(mapType(operation.getRettype()));
        for (Param parameter : operation.getParameters()) {
            functionDesc.addParameter(mapType(parameter.getTypecode()), parameter.getName());
        }
        for (com.eprosima.idl.parser.tree.Exception exception : operation.getExceptions()) {
            functionDesc.addException(mapException(exception));
        }
        return functionDesc;
    }

    private static ExceptionTypeDescriptor mapException(com.eprosima.idl.parser.tree.Exception exception) {
        ExceptionTypeDescriptor excDesc = Kiara.getTypeDescriptorBuilder().createExceptionType(exception.getName());
        for (Member member : exception.getMembers()) {
            excDesc.addMember(mapType(member.getTypecode()), member.getName());
        }
        return excDesc;
    }

    private static DataTypeDescriptor mapType(TypeCode tc) {
        if (tc == null) { // when tc is null, type is void
            return Kiara.getTypeDescriptorBuilder().createVoidType();
        }
        // Primitive Types
        if (tc.isPrimitive() || tc.isString()) {
            //System.out.println("Mapping primitive type " + tc.getStType() +"... ");
            org.fiware.kiara.typecode.TypeKind kind = convertKind(tc);
            if (kind != null) {
                PrimitiveTypeDescriptor td = Kiara.getTypeDescriptorBuilder().createPrimitiveType(kind);
                if (tc.isString()) {
                    td.setMaxFixedLength(Integer.parseInt(tc.getMaxsize()));
                }
                return td;
            }
            return null;
        }
        // Container types
        if (tc.isIsType_f()) { // Arrays
            ArrayTypeCode at = (ArrayTypeCode) tc;
            //System.out.println("Mapping array");
            int size = at.getDimensions().size();
            int array[] = new int[size];
            for (int i = 0; i < size; ++i) {
                array[i] = Integer.parseInt(at.getDimensions().get(i));
            }
            ArrayTypeDescriptor td = Kiara.getTypeDescriptorBuilder().createArrayType(mapType(at.getContentTypeCode()), array);
            return td;
            //td.setMaxSize(Integer.parseInt(at.getMaxsize()));
        }
        if (tc.isIsType_e()) { // Lists
            SequenceTypeCode st = (SequenceTypeCode) tc;
            //System.out.println("Mapping list");
            ListTypeDescriptor td = Kiara.getTypeDescriptorBuilder().createListType(mapType(st.getContentTypeCode()), Integer.parseInt(st.getMaxsize()));
            return td;
        }
        if (tc.isType_set()) { // Sets
            SetTypeCode st = (SetTypeCode) tc;
            //System.out.println("Mapping set");
            SetTypeDescriptor td = Kiara.getTypeDescriptorBuilder().createSetType(mapType(st.getContentTypeCode()), Integer.parseInt(st.getMaxsize()));
            return td;
        }
        if (tc.isType_map()) { // Maps
            MapTypeCode mt = (MapTypeCode) tc;
            //System.out.println("Mapping map");
            MapTypeDescriptor td = Kiara.getTypeDescriptorBuilder().createMapType(mapType(mt.getKeyTypeCode()), mapType(mt.getValueTypeCode()), Integer.parseInt(mt.getMaxsize()));
            return td;
        }

        // Membered types
        if (tc.getKind() == 0x0000000a) { // Struct typecode
            StructTypeCode st = (StructTypeCode) tc;
            //System.out.println("Mapping structure " + st.getName() +"... ");
            StructTypeDescriptor td = Kiara.getTypeDescriptorBuilder().createStructType(st.getName());
            for (com.eprosima.idl.parser.typecode.Member member : st.getMembers()) {
                td.addMember(mapType(member.getTypecode()), member.getName());
            }
            return td;
        }
        if (tc.isIsType_c()) { // Enumerations
            EnumTypeCode et = (EnumTypeCode) tc;
            //System.out.println("Mapping enum");
            ArrayList<String> values = new ArrayList<String>();
            for (Member member : et.getMembers()) {
                values.add(member.getName());
            }
            EnumTypeDescriptor td = Kiara.getTypeDescriptorBuilder().createEnumType(et.getName(), (String[]) values.toArray());
            return td;
        }
        if (tc.isIsType_b()) { // Unions
            UnionTypeCode ut = (UnionTypeCode) tc;
            UnionTypeDescriptor td = Kiara.getTypeDescriptorBuilder().createUnionType(ut.getName(), mapType(ut.getDiscriminator()));
            for (Member member : ut.getMembers()) {
                UnionMember unionMember = (UnionMember) member;
                addMember(td, ut, unionMember);
            }
            //addMember(td, ut, ut.getDefaultMember());
            return td;
        }
        return null;
    }

    private static void addMember(UnionTypeDescriptor td, UnionTypeCode ut, UnionMember unionMember) {
        if (unionMember != null) {
            switch (ut.getDiscriminator().getKind()) {

                case com.eprosima.idl.parser.typecode.TypeCode.KIND_OCTET:
                    ArrayList<Byte> byteLabels = new ArrayList<Byte>();
                    for (String label : unionMember.getLabels()) {
                        byteLabels.add(Byte.parseByte(label));
                    }
                    td.addMember(mapType(unionMember.getTypecode()), unionMember.getName(), unionMember.isDefault(), byteLabels.toArray());
                    break;

                case com.eprosima.idl.parser.typecode.TypeCode.KIND_BOOLEAN:
                    ArrayList<Boolean> booleanLabels = new ArrayList<Boolean>();
                    for (String label : unionMember.getLabels()) {
                        booleanLabels.add(Boolean.parseBoolean(label));
                    }
                    td.addMember(mapType(unionMember.getTypecode()), unionMember.getName(), unionMember.isDefault(), booleanLabels.toArray());
                    break;

                case com.eprosima.idl.parser.typecode.TypeCode.KIND_LONG:
                case com.eprosima.idl.parser.typecode.TypeCode.KIND_ULONG:
                    ArrayList<Integer> intLabels = new ArrayList<Integer>();
                    for (String label : unionMember.getLabels()) {
                        intLabels.add(Integer.parseInt(label));
                    }
                    td.addMember(mapType(unionMember.getTypecode()), unionMember.getName(), unionMember.isDefault(), intLabels.toArray());
                    break;

                case com.eprosima.idl.parser.typecode.TypeCode.KIND_ENUM:
                    ArrayList<String> stringLabels = new ArrayList<String>();
                    for (String label : unionMember.getLabels()) {
                        stringLabels.add(label);
                    }
                    td.addMember(mapType(unionMember.getTypecode()), unionMember.getName(), unionMember.isDefault(), stringLabels.toArray());
                    break;
            }
        }
    }

    private static org.fiware.kiara.typecode.TypeKind convertKind(com.eprosima.idl.parser.typecode.TypeCode tc) {
        switch (tc.getKind()) {
            case com.eprosima.idl.parser.typecode.TypeCode.KIND_CHAR:
                return org.fiware.kiara.typecode.TypeKind.CHAR_8_TYPE;
            case com.eprosima.idl.parser.typecode.TypeCode.KIND_OCTET:
                return org.fiware.kiara.typecode.TypeKind.BYTE_TYPE;

            case com.eprosima.idl.parser.typecode.TypeCode.KIND_SHORT:
                return org.fiware.kiara.typecode.TypeKind.INT_16_TYPE;
            case com.eprosima.idl.parser.typecode.TypeCode.KIND_USHORT:
                return org.fiware.kiara.typecode.TypeKind.UINT_16_TYPE;

            case com.eprosima.idl.parser.typecode.TypeCode.KIND_LONG:
                return org.fiware.kiara.typecode.TypeKind.INT_32_TYPE;
            case com.eprosima.idl.parser.typecode.TypeCode.KIND_ULONG:
                return org.fiware.kiara.typecode.TypeKind.UINT_32_TYPE;

            case com.eprosima.idl.parser.typecode.TypeCode.KIND_LONGLONG:
                return org.fiware.kiara.typecode.TypeKind.INT_64_TYPE;
            case com.eprosima.idl.parser.typecode.TypeCode.KIND_ULONGLONG:
                return org.fiware.kiara.typecode.TypeKind.UINT_64_TYPE;

            case com.eprosima.idl.parser.typecode.TypeCode.KIND_FLOAT:
                return org.fiware.kiara.typecode.TypeKind.FLOAT_32_TYPE;
            case com.eprosima.idl.parser.typecode.TypeCode.KIND_DOUBLE:
                return org.fiware.kiara.typecode.TypeKind.FLOAT_64_TYPE;

            case com.eprosima.idl.parser.typecode.TypeCode.KIND_BOOLEAN:
                return org.fiware.kiara.typecode.TypeKind.BOOLEAN_TYPE;

            case com.eprosima.idl.parser.typecode.TypeCode.KIND_STRING:
                return org.fiware.kiara.typecode.TypeKind.STRING_TYPE;
            default:
                return null;
        }
    }

}
