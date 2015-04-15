package org.fiware.kiara.dynamic;

import static org.junit.Assert.*;

import java.io.IOException;
import org.fiware.kiara.Kiara;
import org.fiware.kiara.complextypes.MyStruct;
import org.fiware.kiara.complextypes.TestServiceTest;
import org.fiware.kiara.dynamic.data.DynamicArray;
import org.fiware.kiara.dynamic.data.DynamicData;
import org.fiware.kiara.dynamic.data.DynamicEnum;
import org.fiware.kiara.dynamic.data.DynamicException;
import org.fiware.kiara.dynamic.data.DynamicList;
import org.fiware.kiara.dynamic.data.DynamicMap;
import org.fiware.kiara.dynamic.data.DynamicPrimitive;
import org.fiware.kiara.dynamic.data.DynamicSet;
import org.fiware.kiara.dynamic.data.DynamicStruct;
import org.fiware.kiara.dynamic.data.DynamicUnion;
import org.fiware.kiara.exceptions.DynamicTypeException;
import org.fiware.kiara.exceptions.TypeDescriptorException;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.CDRSerializer;
import org.fiware.kiara.serialization.types.EnumSwitchUnion;
import org.fiware.kiara.serialization.types.EnumSwitcher;
import org.fiware.kiara.struct.PrimitiveTypesStruct;
import org.fiware.kiara.struct.StructServiceTest;
import org.fiware.kiara.typecode.TypeDescriptorBuilder;
import org.fiware.kiara.typecode.TypeKind;
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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DynamicMemberedTest {

    private CDRSerializer ser;
    BinaryOutputStream bos;
    BinaryInputStream bis;
    DynamicValueBuilder builder;
    TypeDescriptorBuilder tdbuilder;

    @Before
    public void init() {
        this.ser = new CDRSerializer();
        this.bos = new BinaryOutputStream();
        builder = Kiara.getDynamicValueBuilder();
        tdbuilder = Kiara.getTypeDescriptorBuilder();
    }

    @After
    public void detach() {
        //this.message.getPayload().clear();
    }

    public void reset() {
        this.bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
    }

    @Test
    public void structSimpleDescriptionTest() {
        StructTypeDescriptor structDesc = (StructTypeDescriptor) tdbuilder.createStructType("MyStruct");

        try {
            structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE), "MyInteger");
            structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.STRING_TYPE), "MyString");
        } catch (TypeDescriptorException e) {
            assertTrue(false);
            return;
        }
        assertTrue(true);
    }

    @Test
    public void structWrongMemberDescriptionTest() {
        StructTypeDescriptor structDesc = (StructTypeDescriptor) tdbuilder.createStructType("MyStruct");

        try {
            structDesc.addMember(tdbuilder.createFunctionType("name"), "MyFunction");
        } catch (TypeDescriptorException e) {
            assertTrue(true);
            return;
        }
        assertTrue(false);
    }

    @Test
    public void structRepeatedMemberDescriptionTest() {
        StructTypeDescriptor structDesc = (StructTypeDescriptor) tdbuilder.createStructType("MyStruct");

        try {
            structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE), "MyInteger");
            structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.STRING_TYPE), "MyInteger");
        } catch (TypeDescriptorException e) {
            assertTrue(true);
            return;
        }
        assertTrue(false);
    }

    @Test
    public void structNoSuchMemberDescriptionTest() {
        StructTypeDescriptor structDesc = (StructTypeDescriptor) tdbuilder.createStructType("MyStruct");

        try {
            structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE), "MyInteger");
            structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.STRING_TYPE), "MyString");

            DataTypeDescriptor member = structDesc.getMember("NotExsists");
            if (member == null) {
                assertTrue(true);
                return;
            }
        } catch (TypeDescriptorException e) {
            assertTrue(false);
            return;
        }
        assertTrue(false);
    }

    @Test
    public void structSimpleDynamicTest() {
        StructTypeDescriptor structDesc = (StructTypeDescriptor) tdbuilder.createStructType("MyStruct");

        try {
            structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE), "MyInteger");
            structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.STRING_TYPE), "MyString");

            DynamicStruct dynStruct = (DynamicStruct) builder.createData(structDesc);
            ((DynamicPrimitive) dynStruct.getMember("MyInteger")).set(3);
            ((DynamicPrimitive) dynStruct.getMember("MyString")).set("Hello World");
        } catch (TypeDescriptorException e) {
            assertTrue(false);
            return;
        }
        assertTrue(true);
    }

    @Test
    public void structNoSuchMemberDynamicTest() {
        StructTypeDescriptor structDesc = (StructTypeDescriptor) tdbuilder.createStructType("MyStruct");

        try {
            structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE), "MyInteger");

            DynamicStruct dynStruct = (DynamicStruct) builder.createData(structDesc);
            if (dynStruct.getMember("NotExists") == null) {
                assertTrue(true);
                return;
            }
        } catch (TypeDescriptorException e) {
            assertTrue(false);
            return;
        }
        assertTrue(false);
    }

// ------------------------------- Unions --------------------------------------------
    @Test
    public void unionCharDiscriminatorTest() {
        PrimitiveTypeDescriptor charDesc = tdbuilder.createPrimitiveType(TypeKind.CHAR_8_TYPE);
        PrimitiveTypeDescriptor intDesc = tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        PrimitiveTypeDescriptor uintDesc = tdbuilder.createPrimitiveType(TypeKind.UINT_32_TYPE);

        UnionTypeDescriptor unionCharDesc = tdbuilder.createUnionType("MyUnion", charDesc);
        unionCharDesc.addMember(intDesc, "MyInt", false, 'a', 'c');
        unionCharDesc.addMember(uintDesc, "MyUint", false, 'b');

        DynamicUnion dynCharUnion = (DynamicUnion) builder.createData(unionCharDesc);
        dynCharUnion._d('b');

        try {
            assertTrue(dynCharUnion.getMember("MyUint").getTypeDescriptor().getKind() == TypeKind.UINT_32_TYPE);
            assertTrue(dynCharUnion.getMember("MyInt") == null);
        } catch (DynamicTypeException e) {
            assertTrue(false);
        }

        reset();
    }

    @Test
    public void unionBooleanDiscriminatorTest() {
        PrimitiveTypeDescriptor booleanDesc = tdbuilder.createPrimitiveType(TypeKind.BOOLEAN_TYPE);
        PrimitiveTypeDescriptor intDesc = tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        PrimitiveTypeDescriptor uintDesc = tdbuilder.createPrimitiveType(TypeKind.UINT_32_TYPE);

        UnionTypeDescriptor unionCharDesc = tdbuilder.createUnionType("MyUnion", booleanDesc);
        unionCharDesc.addMember(intDesc, "MyInt", false, true);
        unionCharDesc.addMember(uintDesc, "MyUint", false, false);

        DynamicUnion dynCharUnion = (DynamicUnion) builder.createData(unionCharDesc);
        dynCharUnion._d(false);

        try {
            assertTrue(dynCharUnion.getMember("MyUint").getTypeDescriptor().getKind() == TypeKind.UINT_32_TYPE);
            assertTrue(dynCharUnion.getMember("MyInt") == null);
        } catch (DynamicTypeException e) {
            assertTrue(false);
        }

        reset();
    }

    @Test
    public void unionIntDiscriminatorTest() {
        PrimitiveTypeDescriptor intDesc = tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        PrimitiveTypeDescriptor uintDesc = tdbuilder.createPrimitiveType(TypeKind.UINT_32_TYPE);

        UnionTypeDescriptor unionCharDesc = tdbuilder.createUnionType("MyUnion", intDesc);
        unionCharDesc.addMember(intDesc, "MyInt", false, 1, 3);
        unionCharDesc.addMember(uintDesc, "MyUint", false, 2);

        DynamicUnion dynCharUnion = (DynamicUnion) builder.createData(unionCharDesc);
        dynCharUnion._d(2);

        try {
            assertTrue(dynCharUnion.getMember("MyUint").getTypeDescriptor().getKind() == TypeKind.UINT_32_TYPE);
            assertTrue(dynCharUnion.getMember("MyInt") == null);
        } catch (DynamicTypeException e) {
            assertTrue(false);
        }

        reset();
    }

    @Test
    public void unionUintDiscriminatorTest() {
        PrimitiveTypeDescriptor intDesc = tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        PrimitiveTypeDescriptor uintDesc = tdbuilder.createPrimitiveType(TypeKind.UINT_32_TYPE);

        UnionTypeDescriptor unionCharDesc = tdbuilder.createUnionType("MyUnion", uintDesc);
        unionCharDesc.addMember(intDesc, "MyInt", false, 1, 3);
        unionCharDesc.addMember(uintDesc, "MyUint", false, 2);

        DynamicUnion dynCharUnion = (DynamicUnion) builder.createData(unionCharDesc);
        dynCharUnion._d(2);

        try {
            assertTrue(dynCharUnion.getMember("MyUint").getTypeDescriptor().getKind() == TypeKind.UINT_32_TYPE);
            assertTrue(dynCharUnion.getMember("MyInt") == null);
        } catch (DynamicTypeException e) {
            assertTrue(false);
        }

        reset();
    }

    @Test
    public void unionEnumDiscriminatorTest() {
        EnumTypeDescriptor enumDesc = tdbuilder.createEnumType("MyEnum", "value_one", "value_two", "value_three");
        PrimitiveTypeDescriptor intDesc = tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        PrimitiveTypeDescriptor uintDesc = tdbuilder.createPrimitiveType(TypeKind.UINT_32_TYPE);

        UnionTypeDescriptor unionCharDesc = tdbuilder.createUnionType("MyUnion", enumDesc);
        unionCharDesc.addMember(intDesc, "MyInt", false, "value_one", "value_three");
        unionCharDesc.addMember(uintDesc, "MyUint", false, "value_two");

        DynamicUnion dynCharUnion = (DynamicUnion) builder.createData(unionCharDesc);
        dynCharUnion._d("value_two");

        try {
            assertTrue(dynCharUnion.getMember("MyUint").getTypeDescriptor().getKind() == TypeKind.UINT_32_TYPE);
            assertTrue(dynCharUnion.getMember("MyInt") == null);
        } catch (DynamicTypeException e) {
            assertTrue(false);
        }

        reset();
    }

    @Test
    public void unionWrongUnionDescriptionTest() {

        try {
            PrimitiveTypeDescriptor floatDesc = tdbuilder.createPrimitiveType(TypeKind.FLOAT_32_TYPE);
            UnionTypeDescriptor unionCharDesc = tdbuilder.createUnionType("MyUnion", floatDesc);
            assertTrue(false);
        } catch (TypeDescriptorException e) {
            assertTrue(true);
        }

        reset();
    }

    @Test
    public void unionWrongDiscriminatorDescriptionTest() {
        try {
            EnumTypeDescriptor enumDesc = tdbuilder.createEnumType("MyEnum", "value_one", "value_two", "value_three");
            PrimitiveTypeDescriptor intDesc = tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
            PrimitiveTypeDescriptor uintDesc = tdbuilder.createPrimitiveType(TypeKind.UINT_32_TYPE);

            UnionTypeDescriptor unionCharDesc = tdbuilder.createUnionType("MyUnion", enumDesc);
            unionCharDesc.addMember(intDesc, "MyInt", false, "value_one", "error");

            assertTrue(false);
        } catch (TypeDescriptorException e) {
            assertTrue(true);
        }

        reset();
    }

    @Test
    public void unionWrongDiscriminatorSetTest() {

        try {
            EnumTypeDescriptor enumDesc = tdbuilder.createEnumType("MyEnum", "value_one", "value_two", "value_three");
            PrimitiveTypeDescriptor intDesc = tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
            PrimitiveTypeDescriptor uintDesc = tdbuilder.createPrimitiveType(TypeKind.UINT_32_TYPE);

            UnionTypeDescriptor unionCharDesc = tdbuilder.createUnionType("MyUnion", enumDesc);
            unionCharDesc.addMember(intDesc, "MyInt", false, "value_one", "value_three");
            unionCharDesc.addMember(uintDesc, "MyUint", false, "value_two");

            DynamicUnion dynCharUnion = (DynamicUnion) builder.createData(unionCharDesc);
            dynCharUnion._d("error");
            assertTrue(false);
        } catch (DynamicTypeException e) {
            assertTrue(true);
        }

        reset();
    }

    /*
     * noDefaultUnionTest
     */
    @Test
    public void unionNoDefaultTest() {

        PrimitiveTypeDescriptor charDesc = tdbuilder.createPrimitiveType(TypeKind.CHAR_8_TYPE);
        PrimitiveTypeDescriptor intDesc = tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        PrimitiveTypeDescriptor uintDesc = tdbuilder.createPrimitiveType(TypeKind.UINT_32_TYPE);

        UnionTypeDescriptor unionCharDesc = tdbuilder.createUnionType("MyUnion", charDesc);
        unionCharDesc.addMember(intDesc, "MyInt", false, 'a', 'c');
        unionCharDesc.addMember(uintDesc, "MyUint", false, 'b');

        DynamicUnion dynCharUnion = (DynamicUnion) builder.createData(unionCharDesc);

        try {
            assertTrue(dynCharUnion.getMember("MyInt").getTypeDescriptor().getKind() == TypeKind.INT_32_TYPE);
            assertTrue(dynCharUnion.getMember("MyUint") == null);
        } catch (DynamicTypeException e) {
            assertTrue(false);
        }

        reset();
    }

    /*
     * defaultUnionTest
     */
    @Test
    public void unionDefaultTest() {

        PrimitiveTypeDescriptor charDesc = tdbuilder.createPrimitiveType(TypeKind.CHAR_8_TYPE);
        PrimitiveTypeDescriptor intDesc = tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        PrimitiveTypeDescriptor uintDesc = tdbuilder.createPrimitiveType(TypeKind.UINT_32_TYPE);

        UnionTypeDescriptor unionCharDesc = tdbuilder.createUnionType("MyUnion", charDesc);
        unionCharDesc.addMember(intDesc, "MyInt", false, 'a', 'c');
        unionCharDesc.addMember(uintDesc, "MyUint", false, 'b');
        unionCharDesc.addMember(uintDesc, "MyDefaultUint", true);

        DynamicUnion dynCharUnion = (DynamicUnion) builder.createData(unionCharDesc);

        try {
            assertTrue(dynCharUnion.getMember("MyDefaultUint").getTypeDescriptor().getKind() == TypeKind.UINT_32_TYPE);
            assertTrue(dynCharUnion.getMember("MyUint") == null);
            assertTrue(dynCharUnion.getMember("MyInt") == null);
        } catch (DynamicTypeException e) {
            assertTrue(false);
        }

        reset();
    }

    /*
     * defaultUnionCaseOneTest
     */
    @Test
    public void unionDefaultCaseOneTest() {

        PrimitiveTypeDescriptor charDesc = tdbuilder.createPrimitiveType(TypeKind.CHAR_8_TYPE);
        PrimitiveTypeDescriptor intDesc = tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        PrimitiveTypeDescriptor uintDesc = tdbuilder.createPrimitiveType(TypeKind.UINT_32_TYPE);

        UnionTypeDescriptor unionCharDesc = tdbuilder.createUnionType("MyUnion", charDesc);
        unionCharDesc.addMember(intDesc, "MyInt", true, 'a', 'c');
        unionCharDesc.addMember(uintDesc, "MyUint", false, 'b');

        DynamicUnion dynCharUnion = (DynamicUnion) builder.createData(unionCharDesc);

        try {
            assertTrue(dynCharUnion.getMember("MyInt").getTypeDescriptor().getKind() == TypeKind.INT_32_TYPE);
            assertTrue(dynCharUnion.getMember("MyUint") == null);
        } catch (DynamicTypeException e) {
            assertTrue(false);
        }

        reset();
    }

    /*
     * defaultUnionCaseTwoTest
     */
    @Test
    public void unionDefaultCaseTwoTest() {

        PrimitiveTypeDescriptor charDesc = tdbuilder.createPrimitiveType(TypeKind.CHAR_8_TYPE);
        PrimitiveTypeDescriptor intDesc = tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        PrimitiveTypeDescriptor uintDesc = tdbuilder.createPrimitiveType(TypeKind.UINT_32_TYPE);

        UnionTypeDescriptor unionCharDesc = tdbuilder.createUnionType("MyUnion", charDesc);
        unionCharDesc.addMember(intDesc, "MyInt", false, 'a', 'c');
        unionCharDesc.addMember(uintDesc, "MyUint", true);

        DynamicUnion dynCharUnion = (DynamicUnion) builder.createData(unionCharDesc);

        try {
            assertTrue(dynCharUnion.getMember("MyUint").getTypeDescriptor().getKind() == TypeKind.UINT_32_TYPE);
            assertTrue(dynCharUnion.getMember("MyInt") == null);
        } catch (DynamicTypeException e) {
            assertTrue(false);
        }

        reset();
    }

    /*
     * defaultUnionCaseThreeTest
     */
    @Test
    public void unionDefaultCaseThreeTest() {

        PrimitiveTypeDescriptor charDesc = tdbuilder.createPrimitiveType(TypeKind.CHAR_8_TYPE);
        PrimitiveTypeDescriptor intDesc = tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        PrimitiveTypeDescriptor uintDesc = tdbuilder.createPrimitiveType(TypeKind.UINT_32_TYPE);

        UnionTypeDescriptor unionCharDesc = tdbuilder.createUnionType("MyUnion", charDesc);
        unionCharDesc.addMember(intDesc, "MyInt", false, 'a', 'c');
        unionCharDesc.addMember(uintDesc, "MyUint", false, 'b');

        DynamicUnion dynCharUnion = (DynamicUnion) builder.createData(unionCharDesc);

        try {
            assertTrue(dynCharUnion.getMember("MyInt").getTypeDescriptor().getKind() == TypeKind.INT_32_TYPE);
            assertTrue(dynCharUnion.getMember("MyUint") == null);
        } catch (DynamicTypeException e) {
            assertTrue(false);
        }

        reset();
    }

    /*
     * specificDefaultUnionTest
     */
    @Test
    public void unionSpecificDefaultTest() {

        PrimitiveTypeDescriptor charDesc = tdbuilder.createPrimitiveType(TypeKind.CHAR_8_TYPE);
        PrimitiveTypeDescriptor intDesc = tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        PrimitiveTypeDescriptor uintDesc = tdbuilder.createPrimitiveType(TypeKind.UINT_32_TYPE);

        UnionTypeDescriptor unionCharDesc = tdbuilder.createUnionType("MyUnion", charDesc);
        unionCharDesc.addMember(intDesc, "MyInt", true, 'a', 'c');
        unionCharDesc.addMember(uintDesc, "MyUint", false, 'b');

        DynamicUnion dynCharUnion = (DynamicUnion) builder.createData(unionCharDesc);
        dynCharUnion._d('b');

        try {
            assertTrue(dynCharUnion.getMember("MyUint").getTypeDescriptor().getKind() == TypeKind.UINT_32_TYPE);
            assertTrue(dynCharUnion.getMember("MyInt") == null);
        } catch (DynamicTypeException e) {
            assertTrue(false);
        }

        reset();
    }

    /*
     * specificDefaultUnionTest
     */
    @Test
    public void unionSetMemberTest() {

        PrimitiveTypeDescriptor charDesc = tdbuilder.createPrimitiveType(TypeKind.CHAR_8_TYPE);
        PrimitiveTypeDescriptor intDesc = tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        PrimitiveTypeDescriptor uintDesc = tdbuilder.createPrimitiveType(TypeKind.UINT_32_TYPE);

        UnionTypeDescriptor unionCharDesc = tdbuilder.createUnionType("MyUnion", charDesc);
        unionCharDesc.addMember(intDesc, "MyInt", false, 'a', 'c');
        unionCharDesc.addMember(uintDesc, "MyUint", true, 'b');

        DynamicUnion dynCharUnion = (DynamicUnion) builder.createData(unionCharDesc);
        dynCharUnion._d('b');

        DynamicPrimitive dynInt = (DynamicPrimitive) builder.createData(intDesc);
        DynamicPrimitive dynInt2 = (DynamicPrimitive) builder.createData(intDesc);
        dynInt.set(3);
        dynInt2.set(5);

        try {
            dynCharUnion.setMember("MyInt", dynInt);
            assertEquals(((DynamicPrimitive) dynCharUnion.getMember("MyInt")).get(), 3);
            dynCharUnion.setMember("MyInt", dynInt2);
            assertEquals(((DynamicPrimitive) dynCharUnion.getMember("MyInt")).get(), 5);
            assertTrue(dynCharUnion.getMember("MyInt").getTypeDescriptor().getKind() == TypeKind.INT_32_TYPE);
            assertTrue(dynCharUnion.getMember("MyUint") == null);
        } catch (DynamicTypeException e) {
            assertTrue(false);
        }

        reset();
    }

    // ------------------- Serialization tests ----------------------
    /*
     * primitiveStructMembersSerializationTest
     */
    @Test
    public void primitiveStructMembersSerializationTest() {

        StructTypeDescriptor structDesc = (StructTypeDescriptor) tdbuilder.createStructType("MyStruct");

        try {

            structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.CHAR_8_TYPE), "MyChar");
            structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.BYTE_TYPE), "MyByte");
            structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.INT_16_TYPE), "MyShort");
            structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.UINT_16_TYPE), "MyUShort");
            structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE), "MyInt");
            structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.UINT_32_TYPE), "MyUInt");
            structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.INT_64_TYPE), "MyLong");
            structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.UINT_64_TYPE), "MyULong");
            structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.FLOAT_32_TYPE), "MyFloat");
            structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.FLOAT_64_TYPE), "MyDouble");
            structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.STRING_TYPE), "MyString");

            DynamicStruct inputStruct = (DynamicStruct) builder.createData(structDesc);
            DynamicStruct outputStruct = (DynamicStruct) builder.createData(structDesc);

            ((DynamicPrimitive) inputStruct.getMember("MyChar")).set('a');
            ((DynamicPrimitive) inputStruct.getMember("MyByte")).set((byte) 2);
            ((DynamicPrimitive) inputStruct.getMember("MyShort")).set((short) 3);
            ((DynamicPrimitive) inputStruct.getMember("MyUShort")).set((short) 4);
            ((DynamicPrimitive) inputStruct.getMember("MyInt")).set((int) 5);
            ((DynamicPrimitive) inputStruct.getMember("MyUInt")).set((int) 6);
            ((DynamicPrimitive) inputStruct.getMember("MyLong")).set((long) 7);
            ((DynamicPrimitive) inputStruct.getMember("MyULong")).set((long) 8);
            ((DynamicPrimitive) inputStruct.getMember("MyFloat")).set((float) 7);
            ((DynamicPrimitive) inputStruct.getMember("MyDouble")).set((double) 8);
            ((DynamicPrimitive) inputStruct.getMember("MyLong")).set((long) 7);
            ((DynamicPrimitive) inputStruct.getMember("MyString")).set((String) "Hello World!");

            inputStruct.serialize(ser, bos, "");
            reset();
            outputStruct.deserialize(ser, bis, "");
            assertTrue(inputStruct.equals(outputStruct));

        } catch (TypeDescriptorException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    /*
     * containerStructMembersSerializationTest
     */
    @Test
    public void containerStructMembersSerializationTest() {

        StructTypeDescriptor structDesc = (StructTypeDescriptor) tdbuilder.createStructType("MyStruct");

        try {

            PrimitiveTypeDescriptor intDesc = tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
            PrimitiveTypeDescriptor stringDesc = tdbuilder.createPrimitiveType(TypeKind.STRING_TYPE);

            ArrayTypeDescriptor arrayDesc = tdbuilder.createArrayType(intDesc, 3, 5);
            ListTypeDescriptor listType = tdbuilder.createListType(intDesc, 5);
            SetTypeDescriptor setType = tdbuilder.createSetType(intDesc, 5);
            MapTypeDescriptor mapDesc = tdbuilder.createMapType(intDesc, stringDesc, 5);

            structDesc.addMember(arrayDesc, "MyArray");
            structDesc.addMember(listType, "MyList");
            structDesc.addMember(setType, "MySet");
            structDesc.addMember(mapDesc, "MyMap");

            DynamicStruct inputStruct = (DynamicStruct) builder.createData(structDesc);
            DynamicStruct outputStruct = (DynamicStruct) builder.createData(structDesc);

            DynamicPrimitive dynString = (DynamicPrimitive) builder.createData(stringDesc);
            DynamicPrimitive dynInt = (DynamicPrimitive) builder.createData(intDesc);
            dynInt.set(55);

            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 5; ++j) {
                    ((DynamicArray) inputStruct.getMember("MyArray")).setElementAt(dynInt, i, j);
                }
            }

            for (int i = 0; i < 5; ++i) {
                ((DynamicList) inputStruct.getMember("MyList")).add(dynInt);
            }

            for (int i = 0; i < 5; ++i) {
                DynamicPrimitive localDynInt = (DynamicPrimitive) builder.createData(intDesc);
                localDynInt.set(i);
                ((DynamicSet) inputStruct.getMember("MySet")).add(localDynInt);
            }

            for (int i = 0; i < 5; ++i) {
                DynamicPrimitive localDynInt = (DynamicPrimitive) builder.createData(intDesc);
                localDynInt.set(i);
                dynString.set("Hello World" + i);
                ((DynamicMap) inputStruct.getMember("MyMap")).put(localDynInt, dynString);
            }

            inputStruct.serialize(ser, bos, "");
            reset();
            outputStruct.deserialize(ser, bis, "");
            assertTrue(inputStruct.equals(outputStruct));

        } catch (TypeDescriptorException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    /*
     * memberedStructMembersSerializationTest
     */
    @Test
    public void memberedStructMembersSerializationTest() {

        StructTypeDescriptor structDesc = (StructTypeDescriptor) tdbuilder.createStructType("MyStruct");

        try {

            PrimitiveTypeDescriptor stringDesc = tdbuilder.createPrimitiveType(TypeKind.STRING_TYPE);
            PrimitiveTypeDescriptor intDesc = tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
            StructTypeDescriptor innerStructDesc = (StructTypeDescriptor) tdbuilder.createStructType("MyInnerStruct");
            innerStructDesc.addMember(intDesc, "MyInt");
            innerStructDesc.addMember(stringDesc, "MyString");

            EnumTypeDescriptor enumDesc = tdbuilder.createEnumType("MyEnum", "value_one", "value_two", "value_three");

            structDesc.addMember(innerStructDesc, "MyInnerStruct");
            structDesc.addMember(enumDesc, "MyEnum");

            DynamicStruct inputStruct = (DynamicStruct) builder.createData(structDesc);
            DynamicStruct outputStruct = (DynamicStruct) builder.createData(structDesc);

            ((DynamicPrimitive) ((DynamicStruct) inputStruct.getMember("MyInnerStruct")).getMember("MyInt")).set(3);
            ((DynamicPrimitive) ((DynamicStruct) inputStruct.getMember("MyInnerStruct")).getMember("MyString")).set("Hello World");
            ((DynamicEnum) inputStruct.getMember("MyEnum")).set("value_two");

            inputStruct.serialize(ser, bos, "");
            reset();
            outputStruct.deserialize(ser, bis, "");
            assertTrue(inputStruct.equals(outputStruct));

        } catch (TypeDescriptorException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    /*
     * enumSerializationTest
     */
    @Test
    public void enumSerializationTest() {
        EnumTypeDescriptor enumDesc = tdbuilder.createEnumType("MyEnum", "value_one", "value_two", "value_three");
        DynamicEnum inputEnum = (DynamicEnum) builder.createData(enumDesc);
        DynamicEnum outputEnum = (DynamicEnum) builder.createData(enumDesc);

        inputEnum.set("value_two");

        try {
            inputEnum.serialize(ser, bos, "");
            reset();
            outputEnum.deserialize(ser, bis, "");

        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    /*
     * unionCharDiscriminatorSerializationTest
     */
    @Test
    public void unionCharDiscriminatorSerializationTest() {
        PrimitiveTypeDescriptor charDesc = tdbuilder.createPrimitiveType(TypeKind.CHAR_8_TYPE);
        PrimitiveTypeDescriptor intDesc = tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        PrimitiveTypeDescriptor uintDesc = tdbuilder.createPrimitiveType(TypeKind.UINT_32_TYPE);

        UnionTypeDescriptor unionCharDesc = tdbuilder.createUnionType("MyUnion", charDesc);
        unionCharDesc.addMember(intDesc, "MyInt", false, 'a', 'c');
        unionCharDesc.addMember(uintDesc, "MyUint", false, 'b');

        DynamicUnion dynCharUnion = (DynamicUnion) builder.createData(unionCharDesc);
        dynCharUnion._d('b');
        ((DynamicPrimitive) dynCharUnion.getMember("MyUint")).set(5);

        DynamicUnion outputCharUnion = (DynamicUnion) builder.createData(unionCharDesc);

        try {

            dynCharUnion.serialize(ser, bos, "");
            reset();
            outputCharUnion.deserialize(ser, bis, "");

            assertTrue(dynCharUnion.equals(outputCharUnion));

        } catch (DynamicTypeException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        reset();
    }

    /*
     * unionBooleanDiscriminatorSerializationTest
     */
    @Test
    public void unionBooleanDiscriminatorSerializationTest() {
        PrimitiveTypeDescriptor booleanDesc = tdbuilder.createPrimitiveType(TypeKind.BOOLEAN_TYPE);
        PrimitiveTypeDescriptor intDesc = tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        PrimitiveTypeDescriptor uintDesc = tdbuilder.createPrimitiveType(TypeKind.UINT_32_TYPE);

        UnionTypeDescriptor unionBooleanDesc = tdbuilder.createUnionType("MyUnion", booleanDesc);
        unionBooleanDesc.addMember(intDesc, "MyInt", false, true);
        unionBooleanDesc.addMember(uintDesc, "MyUint", false, false);

        DynamicUnion dynBooleanUnion = (DynamicUnion) builder.createData(unionBooleanDesc);
        dynBooleanUnion._d(true);
        ((DynamicPrimitive) dynBooleanUnion.getMember("MyInt")).set(5);

        DynamicUnion outputBooleanUnion = (DynamicUnion) builder.createData(unionBooleanDesc);

        try {

            dynBooleanUnion.serialize(ser, bos, "");
            reset();
            outputBooleanUnion.deserialize(ser, bis, "");

            assertTrue(dynBooleanUnion.equals(outputBooleanUnion));

        } catch (DynamicTypeException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        reset();
    }

    /*
     * unionIntDiscriminatorSerializationTest
     */
    @Test
    public void unionIntDiscriminatorSerializationTest() {
        PrimitiveTypeDescriptor intDesc = tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        PrimitiveTypeDescriptor uintDesc = tdbuilder.createPrimitiveType(TypeKind.UINT_32_TYPE);

        UnionTypeDescriptor unionBooleanDesc = tdbuilder.createUnionType("MyUnion", intDesc);
        unionBooleanDesc.addMember(intDesc, "MyInt", false, 1, 3, 4);
        unionBooleanDesc.addMember(uintDesc, "MyUint", false, 2, 5);

        DynamicUnion dynBooleanUnion = (DynamicUnion) builder.createData(unionBooleanDesc);
        dynBooleanUnion._d(2);
        ((DynamicPrimitive) dynBooleanUnion.getMember("MyUint")).set(5);

        DynamicUnion outputBooleanUnion = (DynamicUnion) builder.createData(unionBooleanDesc);

        try {

            dynBooleanUnion.serialize(ser, bos, "");
            reset();
            outputBooleanUnion.deserialize(ser, bis, "");

            assertTrue(dynBooleanUnion.equals(outputBooleanUnion));

        } catch (DynamicTypeException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        reset();
    }

    /*
     * unionUIntDiscriminatorSerializationTest
     */
    @Test
    public void unionUIntDiscriminatorSerializationTest() {
        PrimitiveTypeDescriptor intDesc = tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        PrimitiveTypeDescriptor uintDesc = tdbuilder.createPrimitiveType(TypeKind.UINT_32_TYPE);

        UnionTypeDescriptor unionBooleanDesc = tdbuilder.createUnionType("MyUnion", uintDesc);
        unionBooleanDesc.addMember(intDesc, "MyInt", false, 1, 3, 4);
        unionBooleanDesc.addMember(uintDesc, "MyUint", false, 2, 5);

        DynamicUnion dynBooleanUnion = (DynamicUnion) builder.createData(unionBooleanDesc);
        dynBooleanUnion._d(2);
        ((DynamicPrimitive) dynBooleanUnion.getMember("MyUint")).set(5);

        DynamicUnion outputBooleanUnion = (DynamicUnion) builder.createData(unionBooleanDesc);

        try {

            dynBooleanUnion.serialize(ser, bos, "");
            reset();
            outputBooleanUnion.deserialize(ser, bis, "");

            assertTrue(dynBooleanUnion.equals(outputBooleanUnion));

        } catch (DynamicTypeException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        reset();
    }

    /*
     * unionEnumDiscriminatorSerializationTest
     */
    @Test
    public void unionEnumDiscriminatorSerializationTest() {
        EnumTypeDescriptor enumDesc = tdbuilder.createEnumType("MyEnum", "value_one", "value_two", "value_three");
        PrimitiveTypeDescriptor intDesc = tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        PrimitiveTypeDescriptor uintDesc = tdbuilder.createPrimitiveType(TypeKind.UINT_32_TYPE);

        UnionTypeDescriptor unionEnumDesc = tdbuilder.createUnionType("MyUnion", enumDesc);
        unionEnumDesc.addMember(intDesc, "MyInt", false, "value_one", "value_three");
        unionEnumDesc.addMember(uintDesc, "MyUint", false, "value_two");

        DynamicUnion dynEnumUnion = (DynamicUnion) builder.createData(unionEnumDesc);
        dynEnumUnion._d("value_two");
        ((DynamicPrimitive) dynEnumUnion.getMember("MyUint")).set(5);

        DynamicUnion outputEnumUnion = (DynamicUnion) builder.createData(unionEnumDesc);

        try {

            dynEnumUnion.serialize(ser, bos, "");
            reset();
            outputEnumUnion.deserialize(ser, bis, "");

            assertTrue(dynEnumUnion.equals(outputEnumUnion));

        } catch (DynamicTypeException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        reset();
    }

    /*
     * primitiveExceptionMembersSerializationTest
     */
    @Test
    public void primitiveExceptionMembersSerializationTest() {

        ExceptionTypeDescriptor exceptionDesc = (ExceptionTypeDescriptor) tdbuilder.createExceptionType("MyStruct");

        try {

            exceptionDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.CHAR_8_TYPE), "MyChar");
            exceptionDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.BYTE_TYPE), "MyByte");
            exceptionDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.INT_16_TYPE), "MyShort");
            exceptionDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.UINT_16_TYPE), "MyUShort");
            exceptionDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE), "MyInt");
            exceptionDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.UINT_32_TYPE), "MyUInt");
            exceptionDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.INT_64_TYPE), "MyLong");
            exceptionDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.UINT_64_TYPE), "MyULong");
            exceptionDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.FLOAT_32_TYPE), "MyFloat");
            exceptionDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.FLOAT_64_TYPE), "MyDouble");
            exceptionDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.STRING_TYPE), "MyString");

            DynamicException inputException = (DynamicException) builder.createData(exceptionDesc);
            DynamicException outputException = (DynamicException) builder.createData(exceptionDesc);

            ((DynamicPrimitive) inputException.getMember("MyChar")).set('a');
            ((DynamicPrimitive) inputException.getMember("MyByte")).set((byte) 2);
            ((DynamicPrimitive) inputException.getMember("MyShort")).set((short) 3);
            ((DynamicPrimitive) inputException.getMember("MyUShort")).set((short) 4);
            ((DynamicPrimitive) inputException.getMember("MyInt")).set((int) 5);
            ((DynamicPrimitive) inputException.getMember("MyUInt")).set((int) 6);
            ((DynamicPrimitive) inputException.getMember("MyLong")).set((long) 7);
            ((DynamicPrimitive) inputException.getMember("MyULong")).set((long) 8);
            ((DynamicPrimitive) inputException.getMember("MyFloat")).set((float) 7);
            ((DynamicPrimitive) inputException.getMember("MyDouble")).set((double) 8);
            ((DynamicPrimitive) inputException.getMember("MyLong")).set((long) 7);
            ((DynamicPrimitive) inputException.getMember("MyString")).set((String) "Hello World!");

            inputException.serialize(ser, bos, "");
            reset();
            outputException.deserialize(ser, bis, "");
            assertTrue(inputException.equals(outputException));

        } catch (TypeDescriptorException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    /*
     * containerExceptionMembersSerializationTest
     */
    @Test
    public void containerExceptionMembersSerializationTest() {

        ExceptionTypeDescriptor exceptionDesc = (ExceptionTypeDescriptor) tdbuilder.createExceptionType("MyStruct");

        try {

            PrimitiveTypeDescriptor intDesc = tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
            PrimitiveTypeDescriptor stringDesc = tdbuilder.createPrimitiveType(TypeKind.STRING_TYPE);

            ArrayTypeDescriptor arrayDesc = tdbuilder.createArrayType(intDesc, 3, 5);
            ListTypeDescriptor listType = tdbuilder.createListType(intDesc, 5);
            SetTypeDescriptor setType = tdbuilder.createSetType(intDesc, 5);
            MapTypeDescriptor mapDesc = tdbuilder.createMapType(intDesc, stringDesc, 5);

            exceptionDesc.addMember(arrayDesc, "MyArray");
            exceptionDesc.addMember(listType, "MyList");
            exceptionDesc.addMember(setType, "MySet");
            exceptionDesc.addMember(mapDesc, "MyMap");

            DynamicException inputException = (DynamicException) builder.createData(exceptionDesc);
            DynamicException outputException = (DynamicException) builder.createData(exceptionDesc);

            DynamicPrimitive dynString = (DynamicPrimitive) builder.createData(stringDesc);
            DynamicPrimitive dynInt = (DynamicPrimitive) builder.createData(intDesc);
            dynInt.set(55);

            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 5; ++j) {
                    ((DynamicArray) inputException.getMember("MyArray")).setElementAt(dynInt, i, j);
                }
            }

            for (int i = 0; i < 5; ++i) {
                ((DynamicList) inputException.getMember("MyList")).add(dynInt);
            }

            for (int i = 0; i < 5; ++i) {
                DynamicPrimitive localDynInt = (DynamicPrimitive) builder.createData(intDesc);
                localDynInt.set(i);
                ((DynamicSet) inputException.getMember("MySet")).add(localDynInt);
            }

            for (int i = 0; i < 5; ++i) {
                DynamicPrimitive localDynInt = (DynamicPrimitive) builder.createData(intDesc);
                localDynInt.set(i);
                dynString.set("Hello World" + i);
                ((DynamicMap) inputException.getMember("MyMap")).put(localDynInt, dynString);
            }

            inputException.serialize(ser, bos, "");
            reset();
            outputException.deserialize(ser, bis, "");
            assertTrue(inputException.equals(outputException));

        } catch (TypeDescriptorException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    /*
     * memberedExceptionMembersSerializationTest
     */
    @Test
    public void memberedExceptionMembersSerializationTest() {

        ExceptionTypeDescriptor exceptionDesc = (ExceptionTypeDescriptor) tdbuilder.createExceptionType("MyStruct");

        try {

            PrimitiveTypeDescriptor stringDesc = tdbuilder.createPrimitiveType(TypeKind.STRING_TYPE);
            PrimitiveTypeDescriptor intDesc = tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
            StructTypeDescriptor innerStructDesc = (StructTypeDescriptor) tdbuilder.createStructType("MyInnerStruct");
            innerStructDesc.addMember(intDesc, "MyInt");
            innerStructDesc.addMember(stringDesc, "MyString");

            EnumTypeDescriptor enumDesc = tdbuilder.createEnumType("MyEnum", "value_one", "value_two", "value_three");

            exceptionDesc.addMember(innerStructDesc, "MyInnerStruct");
            exceptionDesc.addMember(enumDesc, "MyEnum");

            DynamicException inputException = (DynamicException) builder.createData(exceptionDesc);
            DynamicException outputException = (DynamicException) builder.createData(exceptionDesc);

            ((DynamicPrimitive) ((DynamicStruct) inputException.getMember("MyInnerStruct")).getMember("MyInt")).set(3);
            ((DynamicPrimitive) ((DynamicStruct) inputException.getMember("MyInnerStruct")).getMember("MyString")).set("Hello World");
            ((DynamicEnum) inputException.getMember("MyEnum")).set("value_two");

            inputException.serialize(ser, bos, "");
            reset();
            outputException.deserialize(ser, bis, "");
            assertTrue(inputException.equals(outputException));

        } catch (TypeDescriptorException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void staticDynamicStructSerializationTest1() throws Exception {
        StructTypeDescriptor structDesc = tdbuilder.createStructType("MyStruct");
        structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE), "myInt");
        structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.STRING_TYPE), "myString");
        structDesc.addMember(tdbuilder.createArrayType(tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE), 10, 5), "arrayInt");
        structDesc.addMember(tdbuilder.createArrayType(tdbuilder.createPrimitiveType(TypeKind.STRING_TYPE).setMaxFixedLength(10), 10), "arrayString");
        structDesc.addMember(tdbuilder.createListType(tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE), 8), "sequenceInt");

        MyStruct inSt;
        MyStruct outSt = new MyStruct();

        for (int i = 0; i < 10; ++i) {
            inSt = TestServiceTest.TestServiceServantImpl.createMyStruct(i, "Test "+i);

            DynamicData dynOutSt = builder.createData(structDesc);
            bos.reset();
            inSt.serialize(ser, bos, "");
            reset();
            dynOutSt.deserialize(ser, bis, "");
            reset();
            bos.reset();
            dynOutSt.serialize(ser, bos, "");
            reset();
            outSt.deserialize(ser, bis, "");

            assertEquals(inSt, outSt);
        }
    }

    @Test
    public void staticDynamicStructSerializationTest2() throws Exception {
        PrimitiveTypesStruct inSt = StructServiceTest.StructServiceServantImpl.createPrimitiveTypesStruct();
        PrimitiveTypesStruct outSt = new PrimitiveTypesStruct();

        StructTypeDescriptor structDesc = tdbuilder.createStructType("PrimitiveTypesStruct");
        structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.CHAR_8_TYPE), "myChar");
        structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.BYTE_TYPE), "myByte");
        structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.UINT_16_TYPE), "myUShort");
        structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.INT_16_TYPE), "myShort");
        structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.UINT_32_TYPE), "myUInt");
        structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE), "myInt");
        structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.UINT_64_TYPE), "myULong");
        structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.INT_64_TYPE), "myLong");
        structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.FLOAT_32_TYPE), "myFloat");
        structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.FLOAT_64_TYPE), "myDouble");
        structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.BOOLEAN_TYPE), "myBoolean");
        structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.STRING_TYPE), "myString");
        structDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.STRING_TYPE).setMaxFixedLength(8), "myString5");

        DynamicData dynOutSt = builder.createData(structDesc);
        bos.reset();
        inSt.serialize(ser, bos, "");
        reset();
        dynOutSt.deserialize(ser, bis, "");
        reset();
        bos.reset();
        dynOutSt.serialize(ser, bos, "");
        reset();
        outSt.deserialize(ser, bis, "");

        assertEquals(inSt, outSt);
    }

    @Test
    public void staticDynamicUnionSerializationTest() throws Exception {

        EnumTypeDescriptor enumDesc = tdbuilder.createEnumType("EnumSwitcher", "option_1", "option_2", "option_3");

        UnionTypeDescriptor unionDesc = tdbuilder.createUnionType("EnumSwitchUnion", enumDesc);
        unionDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE), "intVal", false, "option_1", "option_2");
        unionDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.STRING_TYPE), "stringVal", false, "option_3");
        unionDesc.addMember(tdbuilder.createPrimitiveType(TypeKind.FLOAT_32_TYPE), "floatVal", true);



        EnumSwitchUnion inUn = new EnumSwitchUnion();
        inUn._d(EnumSwitcher.option_3);
        inUn.setStringVal("Test Option 3");
        EnumSwitchUnion outUn = new EnumSwitchUnion();

        DynamicData dynOutSt = builder.createData(unionDesc);
        bos.reset();
        inUn.serialize(ser, bos, "");
        reset();
        dynOutSt.deserialize(ser, bis, "");
        reset();
        bos.reset();
        dynOutSt.serialize(ser, bos, "");
        reset();
        outUn.deserialize(ser, bis, "");

        assertEquals(inUn, outUn);
    }

}
