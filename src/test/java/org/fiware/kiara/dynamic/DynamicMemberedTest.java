package org.fiware.kiara.dynamic;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.fiware.kiara.dynamic.data.DynamicPrimitive;
import org.fiware.kiara.dynamic.data.DynamicStruct;
import org.fiware.kiara.exceptions.TypeDescriptorException;
import org.fiware.kiara.serialization.MockTransportMessage;
import org.fiware.kiara.serialization.impl.CDRSerializer;
import org.fiware.kiara.transport.impl.TransportMessage;
import org.fiware.kiara.typecode.TypeDescriptorBuilder;
import org.fiware.kiara.typecode.TypeDescriptorBuilderImpl;
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.data.StructTypeDescriptor;
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

}
