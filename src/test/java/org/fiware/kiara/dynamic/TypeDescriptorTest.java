package org.fiware.kiara.dynamic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.fiware.kiara.exceptions.TypeDescriptorException;
import org.fiware.kiara.serialization.MockTransportMessage;
import org.fiware.kiara.serialization.impl.CDRSerializer;
import org.fiware.kiara.transport.impl.TransportMessage;
import org.fiware.kiara.typecode.TypeDescriptorBuilder;
import org.fiware.kiara.typecode.TypeDescriptorBuilderImpl;
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.ArrayTypeDescriptor;
import org.fiware.kiara.typecode.data.ListTypeDescriptor;
import org.fiware.kiara.typecode.data.MapTypeDescriptor;
import org.fiware.kiara.typecode.data.PrimitiveTypeDescriptor;
import org.fiware.kiara.typecode.data.SetTypeDescriptor;
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
            builder.createPrimitiveType(TypeKind.BOOLEAN_TYPE);
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
            builder.createPrimitiveType(TypeKind.BYTE_TYPE);
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
            builder.createPrimitiveType(TypeKind.INT_16_TYPE);
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
            builder.createPrimitiveType(TypeKind.UINT_16_TYPE);
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
            builder.createPrimitiveType(TypeKind.INT_32_TYPE);
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
            builder.createPrimitiveType(TypeKind.UINT_32_TYPE);
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
            builder.createPrimitiveType(TypeKind.INT_64_TYPE);
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
            builder.createPrimitiveType(TypeKind.UINT_64_TYPE);
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
            builder.createPrimitiveType(TypeKind.FLOAT_32_TYPE);
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
            builder.createPrimitiveType(TypeKind.FLOAT_64_TYPE);
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
            builder.createPrimitiveType(TypeKind.BOOLEAN_TYPE);
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
            PrimitiveTypeDescriptor stringDesc = builder.createPrimitiveType(TypeKind.STRING_TYPE);
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
    @SuppressWarnings("unused")
    @Test
    public void arrayTest() {
        try {
            PrimitiveTypeDescriptor primitiveDesc = builder.createPrimitiveType(TypeKind.INT_32_TYPE);
            ArrayTypeDescriptor arrayDesc = builder.createArrayType(primitiveDesc, 3, 5);
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
            PrimitiveTypeDescriptor primitiveDesc = builder.createPrimitiveType(TypeKind.INT_32_TYPE);
            arrayDesc = builder.createArrayType(primitiveDesc, 3, 8, 2);
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
    @SuppressWarnings("unused")
    @Test
    public void arrayTestNoDimensions() {
        try {
            PrimitiveTypeDescriptor primitiveDesc = builder.createPrimitiveType(TypeKind.INT_32_TYPE);
            ArrayTypeDescriptor arrayDesc = builder.createArrayType(primitiveDesc);
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
    @SuppressWarnings("unused")
    @Test
    public void arrayTestNegativeDimensions() {
        try {
            PrimitiveTypeDescriptor primitiveDesc = builder.createPrimitiveType(TypeKind.INT_32_TYPE);
            ArrayTypeDescriptor arrayDesc = builder.createArrayType(primitiveDesc, 3, -1);
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
    @SuppressWarnings("unused")
    @Test
    public void arrayTestWrongContentType() {
        try {
            PrimitiveTypeDescriptor primitiveDesc = builder.createPrimitiveType(TypeKind.INT_32_TYPE);
            ArrayTypeDescriptor arrayDesc = builder.createArrayType(primitiveDesc, 1);
            ArrayTypeDescriptor innerArrayDesc = builder.createArrayType(arrayDesc, 1);
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
    @SuppressWarnings("unused")
    @Test
    public void listTest() {
        try {
            PrimitiveTypeDescriptor primitiveDesc = (PrimitiveTypeDescriptor) builder.createPrimitiveType(TypeKind.INT_32_TYPE);
            ListTypeDescriptor listDesc = builder.createListType(primitiveDesc, 10);
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
    @SuppressWarnings("unused")
    @Test
    public void wrongMaxSizeListTest() {
        try {
            PrimitiveTypeDescriptor primitiveDesc = (PrimitiveTypeDescriptor) builder.createPrimitiveType(TypeKind.INT_32_TYPE);
            ListTypeDescriptor listDesc = builder.createListType(primitiveDesc, -1);
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
    @SuppressWarnings("unused")
    @Test
    public void setTest() {
        try {
            PrimitiveTypeDescriptor primitiveDesc = (PrimitiveTypeDescriptor) builder.createPrimitiveType(TypeKind.INT_32_TYPE);
            SetTypeDescriptor listDesc = builder.createSetType(primitiveDesc, 10);
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
    @SuppressWarnings("unused")
    @Test
    public void wrongMaxSizeSetTest() {
        try {
            PrimitiveTypeDescriptor primitiveDesc = (PrimitiveTypeDescriptor) builder.createPrimitiveType(TypeKind.INT_32_TYPE);
            primitiveDesc.setMaxFixedLength(15);
            SetTypeDescriptor setDesc = builder.createSetType(primitiveDesc, 0);
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
    @SuppressWarnings("unused")
    @Test
    public void mapTest() {
        try {
            PrimitiveTypeDescriptor primitiveKeyDesc = builder.createPrimitiveType(TypeKind.INT_32_TYPE);
            PrimitiveTypeDescriptor primitiveValueDesc = builder.createPrimitiveType(TypeKind.STRING_TYPE);
            primitiveValueDesc.setMaxFixedLength(15);
            MapTypeDescriptor mapDesc = (MapTypeDescriptor) builder.createMapType(primitiveKeyDesc, primitiveValueDesc, 15);
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
    @SuppressWarnings("unused")
    @Test
    public void wrongMaxSizeMapTest() {
        try {
            PrimitiveTypeDescriptor primitiveKeyDesc = (PrimitiveTypeDescriptor) builder.createPrimitiveType(TypeKind.INT_32_TYPE);
            PrimitiveTypeDescriptor primitiveValueDesc = (PrimitiveTypeDescriptor) builder.createPrimitiveType(TypeKind.STRING_TYPE);
            primitiveValueDesc.setMaxFixedLength(15);
            MapTypeDescriptor mapDesc = (MapTypeDescriptor) builder.createMapType(primitiveKeyDesc, primitiveValueDesc, 0);
        } catch (TypeDescriptorException e) {
            assertTrue(true);
            return;
        }
        assertTrue(false);
        
        reset();
    }
    
    


}
