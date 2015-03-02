package org.fiware.kiara.dynamic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.fiware.kiara.dynamic.impl.DynamicTypeBuilderImpl;
import org.fiware.kiara.exceptions.TypeDescriptorException;
import org.fiware.kiara.serialization.MockTransportMessage;
import org.fiware.kiara.serialization.impl.CDRSerializer;
import org.fiware.kiara.transport.impl.TransportMessage;
import org.fiware.kiara.typecode.TypeDescriptorBuilder;
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.ArrayTypeDescriptor;
import org.fiware.kiara.typecode.data.ListTypeDescriptor;
import org.fiware.kiara.typecode.data.MapTypeDescriptor;
import org.fiware.kiara.typecode.data.PrimitiveTypeDescriptor;
import org.fiware.kiara.typecode.data.SetTypeDescriptor;
import org.fiware.kiara.typecode.impl.TypeDescriptorBuilderImpl;
import org.fiware.kiara.typecode.impl.data.DataTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.PrimitiveTypeDescriptorImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;



public class TypeDescriptorTest {
    
    private CDRSerializer ser;
    private ByteBuffer buffer;
    private TransportMessage message;
    TypeDescriptorBuilder builder;
    
    @Before
    public void init() {
        this.ser = new CDRSerializer();
        this.buffer = ByteBuffer.allocate(500);
        this.buffer.order(ByteOrder.LITTLE_ENDIAN);
        this.message = new MockTransportMessage(buffer);
        builder = TypeDescriptorBuilderImpl.getInstance();
    }

    @After
    public void detach() {
        this.message.getPayload().clear();
    }

    public void reset() {
        this.message.getPayload().clear();
    }
    
    /*
     * booleanPrimitiveTest
     */
    @Test
    public void booleanPrimitiveTest() {
        
        try {
            builder.createTypeDescriptor(TypeKind.BOOLEAN_TYPE, "");
        } catch (TypeDescriptorException e) {
            assertTrue(false);
            return;
        }
        assertTrue(true);
        
        reset();
    }
    
    /*
     * bytePrimitiveTest
     */
    @Test
    public void bytePrimitiveTest() {
        
        try {
            builder.createTypeDescriptor(TypeKind.BYTE_TYPE, "");
        } catch (TypeDescriptorException e) {
            assertTrue(false);
            return;
        }
        assertTrue(true);
        
        reset();
    }
    
    /*
     * int16PrimitiveTest
     */
    @Test
    public void int16PrimitiveTest() {
        
        try {
            builder.createTypeDescriptor(TypeKind.INT_16_TYPE, "");
        } catch (TypeDescriptorException e) {
            assertTrue(false);
            return;
        }
        assertTrue(true);
        
        reset();
    }
    
    /*
     * uint16PrimitiveTest
     */
    @Test
    public void uint16PrimitiveTest() {
        
        try {
            builder.createTypeDescriptor(TypeKind.UINT_16_TYPE, "");
        } catch (TypeDescriptorException e) {
            assertTrue(false);
            return;
        }
        assertTrue(true);
        
        reset();
    }
    
    /*
     * int32PrimitiveTest
     */
    @Test
    public void int32PrimitiveTest() {
        
        try {
            builder.createTypeDescriptor(TypeKind.INT_32_TYPE, "");
        } catch (TypeDescriptorException e) {
            assertTrue(false);
            return;
        }
        assertTrue(true);
        
        reset();
    }
    
    /*
     * uint32PrimitiveTest
     */
    @Test
    public void uint32PrimitiveTest() {
        
        try {
            builder.createTypeDescriptor(TypeKind.UINT_32_TYPE, "");
        } catch (TypeDescriptorException e) {
            assertTrue(false);
            return;
        }
        assertTrue(true);
        
        reset();
    }
    
    /*
     * int64PrimitiveTest
     */
    @Test
    public void int64PrimitiveTest() {
        
        try {
            builder.createTypeDescriptor(TypeKind.INT_64_TYPE, "");
        } catch (TypeDescriptorException e) {
            assertTrue(false);
            return;
        }
        assertTrue(true);
        
        reset();
    }
    
    /*
     * uint64PrimitiveTest
     */
    @Test
    public void uint64PrimitiveTest() {
        
        try {
            builder.createTypeDescriptor(TypeKind.UINT_64_TYPE, "");
        } catch (TypeDescriptorException e) {
            assertTrue(false);
            return;
        }
        assertTrue(true);
        
        reset();
    }
    
    /*
     * float32PrimitiveTest
     */
    @Test
    public void float32PrimitiveTest() {
        
        try {
            builder.createTypeDescriptor(TypeKind.FLOAT_32_TYPE, "");
        } catch (TypeDescriptorException e) {
            assertTrue(false);
            return;
        }
        assertTrue(true);
        
        reset();
    }
    
    /*
     * uint64PrimitiveTest
     */
    @Test
    public void float64PrimitiveTest() {
        
        try {
            builder.createTypeDescriptor(TypeKind.FLOAT_64_TYPE, "");
        } catch (TypeDescriptorException e) {
            assertTrue(false);
            return;
        }
        assertTrue(true);
        
        reset();
    }
    
    /*
     * charPrimitiveTest
     */
    @Test
    public void charPrimitiveTest() {
        
        try {
            builder.createTypeDescriptor(TypeKind.BOOLEAN_TYPE, "");
        } catch (TypeDescriptorException e) {
            assertTrue(false);
            return;
        }
        assertTrue(true);
        
        reset();
    }
    
    /*
     * stringPrimitiveTest
     */
    @Test
    public void stringPrimitiveTest() {
        
        try {
            PrimitiveTypeDescriptor stringDesc = (PrimitiveTypeDescriptor) builder.createTypeDescriptor(TypeKind.STRING_TYPE, "");
            stringDesc.setMaxFixedLength(15);
            
        } catch (TypeDescriptorException e) {
            assertTrue(false);
            return;
        }
        assertTrue(true);
        
        reset();
    }
    
    // Array tests
    
    /*
     * arrayTest
     */
    @Test
    public void arrayTest() {
        try {
            PrimitiveTypeDescriptor primitiveDesc = (PrimitiveTypeDescriptor) builder.createTypeDescriptor(TypeKind.INT_32_TYPE, "");
            ArrayTypeDescriptor arrayDesc = (ArrayTypeDescriptor) builder.createTypeDescriptor(TypeKind.ARRAY_TYPE, "");
            arrayDesc.setContentType(primitiveDesc);
            arrayDesc.setDimensions(3, 5);
        } catch (TypeDescriptorException e) {
            assertTrue(false);
            return;
        }
        assertTrue(true);
        
        reset();
    }
    
    /*
     * arrayTestNumberOfDimensions
     */
    @Test
    public void arrayTestNumberOfDimensions() {
        ArrayTypeDescriptor arrayDesc;
        try {
            arrayDesc = (ArrayTypeDescriptor) builder.createTypeDescriptor(TypeKind.ARRAY_TYPE, "");
            arrayDesc.setDimensions(3, 8, 2);
        } catch (TypeDescriptorException e) {
            assertTrue(false);
            return;
        }
        assertEquals((3*2*8), arrayDesc.getMaxSize());
        
        reset();
    }
    
    /*
     * arrayTestNoDimensions
     */
    @Test
    public void arrayTestNoDimensions() {
        try {
            ArrayTypeDescriptor arrayDesc = (ArrayTypeDescriptor) builder.createTypeDescriptor(TypeKind.ARRAY_TYPE, "");
            arrayDesc.setDimensions();
        } catch (TypeDescriptorException e) {
            assertTrue(true);
            return;
        }
        assertTrue(false);
        
        reset();
    }
    
    /*
     * arrayTestNegativeDimensions
     */
    @Test
    public void arrayTestNegativeDimensions() {
        try {
            ArrayTypeDescriptor arrayDesc = (ArrayTypeDescriptor) builder.createTypeDescriptor(TypeKind.ARRAY_TYPE, "");
            arrayDesc.setDimensions(3, -1);
        } catch (TypeDescriptorException e) {
            assertTrue(true);
            return;
        }
        assertTrue(false);
        
        reset();
    }
    
    /*
     * arrayTestWrongContentType
     */
    @Test
    public void arrayTestWrongContentType() {
        try {
            ArrayTypeDescriptor arrayDesc = (ArrayTypeDescriptor) builder.createTypeDescriptor(TypeKind.ARRAY_TYPE, "");
            ArrayTypeDescriptor innerArrayDesc = (ArrayTypeDescriptor) builder.createTypeDescriptor(TypeKind.ARRAY_TYPE, "");
            arrayDesc.setContentType(innerArrayDesc);
        } catch (TypeDescriptorException e) {
            assertTrue(true);
            return;
        }
        assertTrue(false);
        
        reset();
    }
    
    
    // List tests
    
    /*
     * listTest
     */
    @Test
    public void listTest() {
        try {
            PrimitiveTypeDescriptor primitiveDesc = (PrimitiveTypeDescriptor) builder.createTypeDescriptor(TypeKind.INT_32_TYPE, "");
            ListTypeDescriptor listDesc = (ListTypeDescriptor) builder.createTypeDescriptor(TypeKind.LIST_TYPE, "");
            listDesc.setContentType(primitiveDesc);
            listDesc.setMaxSize(10);
        } catch (TypeDescriptorException e) {
            assertTrue(false);
            return;
        }
        assertTrue(true);
        
        reset();
    }
    
    /*
     * wrongMaxSizeListTest
     */
    @Test
    public void wrongMaxSizeListTest() {
        try {
            PrimitiveTypeDescriptor primitiveDesc = (PrimitiveTypeDescriptor) builder.createTypeDescriptor(TypeKind.INT_32_TYPE, "");
            primitiveDesc.setMaxFixedLength(15);
            ListTypeDescriptor listDesc = (ListTypeDescriptor) builder.createTypeDescriptor(TypeKind.LIST_TYPE, "");
            listDesc.setContentType(primitiveDesc);
            listDesc.setMaxSize(0);
        } catch (TypeDescriptorException e) {
            assertTrue(true);
            return;
        }
        assertTrue(false);
        
        reset();
    }
    
    // Set tests
    
    /*
     * setTest
     */
    @Test
    public void setTest() {
        try {
            PrimitiveTypeDescriptor primitiveDesc = (PrimitiveTypeDescriptor) builder.createTypeDescriptor(TypeKind.INT_32_TYPE, "");
            SetTypeDescriptor setDesc = (SetTypeDescriptor) builder.createTypeDescriptor(TypeKind.SET_TYPE, "");
            setDesc.setContentType(primitiveDesc);
            setDesc.setMaxSize(10);
        } catch (TypeDescriptorException e) {
            assertTrue(false);
            return;
        }
        assertTrue(true);
        
        reset();
    }
    
    /*
     * wrongMaxSizeSetTest
     */
    @Test
    public void wrongMaxSizeSetTest() {
        try {
            PrimitiveTypeDescriptor primitiveDesc = (PrimitiveTypeDescriptor) builder.createTypeDescriptor(TypeKind.INT_32_TYPE, "");
            primitiveDesc.setMaxFixedLength(15);
            SetTypeDescriptor setDesc = (SetTypeDescriptor) builder.createTypeDescriptor(TypeKind.SET_TYPE, "");
            setDesc.setContentType(primitiveDesc);
            setDesc.setMaxSize(0);
        } catch (TypeDescriptorException e) {
            assertTrue(true);
            return;
        }
        assertTrue(false);
        
        reset();
    }
    
    // Map tests
    
    /*
     * mapTest
     */
    @Test
    public void mapTest() {
        try {
            PrimitiveTypeDescriptor primitiveKeyDesc = (PrimitiveTypeDescriptor) builder.createTypeDescriptor(TypeKind.INT_32_TYPE, "");
            PrimitiveTypeDescriptor primitiveValueDesc = (PrimitiveTypeDescriptor) builder.createTypeDescriptor(TypeKind.STRING_TYPE, "");
            primitiveValueDesc.setMaxFixedLength(15);
            MapTypeDescriptor mapDesc = (MapTypeDescriptor) builder.createTypeDescriptor(TypeKind.MAP_TYPE, "");
            mapDesc.setKeyTypeDescriptor(primitiveKeyDesc);
            mapDesc.setValueTypeDescriptor(primitiveValueDesc);
            mapDesc.setMaxSize(10);
        } catch (TypeDescriptorException e) {
            assertTrue(false);
            return;
        }
        assertTrue(true);
        
        reset();
    }
    
    /*
     * wrongMaxSizeMapTest
     */
    @Test
    public void wrongMaxSizeMapTest() {
        try {
            PrimitiveTypeDescriptor primitiveKeyDesc = (PrimitiveTypeDescriptor) builder.createTypeDescriptor(TypeKind.INT_32_TYPE, "");
            PrimitiveTypeDescriptor primitiveValueDesc = (PrimitiveTypeDescriptor) builder.createTypeDescriptor(TypeKind.STRING_TYPE, "");
            primitiveValueDesc.setMaxFixedLength(15);
            MapTypeDescriptor mapDesc = (MapTypeDescriptor) builder.createTypeDescriptor(TypeKind.MAP_TYPE, "");
            mapDesc.setKeyTypeDescriptor(primitiveKeyDesc);
            mapDesc.setValueTypeDescriptor(primitiveValueDesc);
            mapDesc.setMaxSize(0);
        } catch (TypeDescriptorException e) {
            assertTrue(true);
            return;
        }
        assertTrue(false);
        
        reset();
    }
    
    


}
