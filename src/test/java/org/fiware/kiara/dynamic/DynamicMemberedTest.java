package org.fiware.kiara.dynamic;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.fiware.kiara.dynamic.data.DynamicPrimitive;
import org.fiware.kiara.dynamic.data.DynamicStruct;
import org.fiware.kiara.dynamic.data.DynamicUnion;
import org.fiware.kiara.exceptions.DynamicTypeException;
import org.fiware.kiara.exceptions.TypeDescriptorException;
import org.fiware.kiara.serialization.MockTransportMessage;
import org.fiware.kiara.serialization.impl.CDRSerializer;
import org.fiware.kiara.transport.impl.TransportMessage;
import org.fiware.kiara.typecode.TypeDescriptorBuilder;
import org.fiware.kiara.typecode.TypeDescriptorBuilderImpl;
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.data.EnumTypeDescriptor;
import org.fiware.kiara.typecode.data.PrimitiveTypeDescriptor;
import org.fiware.kiara.typecode.data.StructTypeDescriptor;
import org.fiware.kiara.typecode.data.UnionTypeDescriptor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DynamicMemberedTest {
    
    private CDRSerializer ser;
    private ByteBuffer buffer;
    private TransportMessage message;
    DynamicTypeBuilder builder;
    TypeDescriptorBuilder tdbuilder;

    @Before
    public void init() {
        this.ser = new CDRSerializer();
        this.buffer = ByteBuffer.allocate(500);
        this.buffer.order(ByteOrder.LITTLE_ENDIAN);
        this.message = new MockTransportMessage(buffer);
        builder = DynamicTypeBuilderImpl.getInstance();
        tdbuilder = TypeDescriptorBuilderImpl.getInstance();
    }

    @After
    public void detach() {
        this.message.getPayload().clear();
    }

    public void reset() {
        this.message.getPayload().clear();
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

}
