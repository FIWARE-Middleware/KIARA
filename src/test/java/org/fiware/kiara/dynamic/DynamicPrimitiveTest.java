package org.fiware.kiara.dynamic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.fiware.kiara.dynamic.impl.DynamicTypeBuilderImpl;
import org.fiware.kiara.serialization.MockTransportMessage;
import org.fiware.kiara.serialization.impl.CDRSerializer;
import org.fiware.kiara.transport.impl.TransportMessage;
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.impl.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.impl.data.PrimitiveTypeDescriptor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DynamicPrimitiveTest {
    
    private CDRSerializer ser;
    private ByteBuffer buffer;
    private TransportMessage message;
    DynamicTypeBuilder builder;

    @Before
    public void init() {
        this.ser = new CDRSerializer();
        this.buffer = ByteBuffer.allocate(500);
        this.buffer.order(ByteOrder.LITTLE_ENDIAN);
        this.message = new MockTransportMessage(buffer);
        builder = DynamicTypeBuilderImpl.getInstance();
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
        
        DataTypeDescriptor booleanDesc = new PrimitiveTypeDescriptor(TypeKind.BOOLEAN_TYPE, "");
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(booleanDesc);
        dyn.set(false);
        assertEquals(dyn.get(), false);
        
        reset();
    }
    
    /*
     * bytePrimitiveTest
     */
    @Test
    public void bytePrimitiveTest() {
        
        DataTypeDescriptor byteDesc = new PrimitiveTypeDescriptor(TypeKind.BYTE_TYPE, "");
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(byteDesc);
        if (!dyn.set((byte) 4)) {
            assertTrue(false);
        }
        assertEquals(dyn.get(), (byte) 4);
        
        reset();
    }
    
    /*
     * int16PrimitiveTest
     */
    @Test
    public void int16PrimitiveTest() {
        
        DataTypeDescriptor int16Desc = new PrimitiveTypeDescriptor(TypeKind.INT_16_TYPE, "");
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(int16Desc);
        if (!dyn.set((short) 4)) {
            assertTrue(false);
        }
        assertEquals(dyn.get(), (short) 4);
        
        reset();
    }
    
    /*
     * uint16PrimitiveTest
     */
    @Test
    public void uint16PrimitiveTest() {
        
        DataTypeDescriptor uint16Desc = new PrimitiveTypeDescriptor(TypeKind.UINT_16_TYPE, "");
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(uint16Desc);
        if (!dyn.set((short) 4)) {
            assertTrue(false);
        }
        assertEquals(dyn.get(), (short) 4);
        
        reset();
    }
    
    /*
     * int32PrimitiveTest
     */
    @Test
    public void int32PrimitiveTest() {
        
        DataTypeDescriptor int32Desc = new PrimitiveTypeDescriptor(TypeKind.INT_32_TYPE, "");
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(int32Desc);
        if (!dyn.set((int) 4)) {
            assertTrue(false);
        }
        assertEquals(dyn.get(), (int) 4);
        
        reset();
    }
    
    /*
     * uint32PrimitiveTest
     */
    @Test
    public void uint32PrimitiveTest() {
        
        DataTypeDescriptor uint32Desc = new PrimitiveTypeDescriptor(TypeKind.UINT_32_TYPE, "");
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(uint32Desc);
        if (!dyn.set((int) 4)) {
            assertTrue(false);
        }
        assertEquals(dyn.get(), (int) 4);
        
        reset();
    }
    
    /*
     * int64PrimitiveTest
     */
    @Test
    public void int64PrimitiveTest() {
        
        DataTypeDescriptor int64Desc = new PrimitiveTypeDescriptor(TypeKind.INT_64_TYPE, "");
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(int64Desc);
        if (!dyn.set((long) 4)) {
            assertTrue(false);
        }
        assertEquals(dyn.get(), (long) 4);
        
        reset();
    }
    
    /*
     * uint64PrimitiveTest
     */
    @Test
    public void uint64PrimitiveTest() {
        
        DataTypeDescriptor uint64Desc = new PrimitiveTypeDescriptor(TypeKind.UINT_64_TYPE, "");
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(uint64Desc);
        if (!dyn.set((long) 4)) {
            assertTrue(false);
        }
        assertEquals(dyn.get(), (long) 4);
        
        reset();
    }
    
    /*
     * float32PrimitiveTest
     */
    @Test
    public void float32PrimitiveTest() {
        
        DataTypeDescriptor float32Desc = new PrimitiveTypeDescriptor(TypeKind.FLOAT_32_TYPE, "");
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(float32Desc);
        if (!dyn.set((float) 4)) {
            assertTrue(false);
        }
        assertEquals(dyn.get(), (float) 4);
        
        reset();
    }
    
    /*
     * uint64PrimitiveTest
     */
    @Test
    public void float64PrimitiveTest() {
        
        DataTypeDescriptor float64Desc = new PrimitiveTypeDescriptor(TypeKind.FLOAT_64_TYPE, "");
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(float64Desc);
        if (!dyn.set((double) 4)) {
            assertTrue(false);
        }
        assertEquals(dyn.get(), (double) 4);
        
        reset();
    }
    
    /*
     * charPrimitiveTest
     */
    @Test
    public void charPrimitiveTest() {
        
        DataTypeDescriptor charDesc = new PrimitiveTypeDescriptor(TypeKind.CHAR_8_TYPE, "");
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(charDesc);
        if (!dyn.set((char) 'S')) {
            assertTrue(false);
        }
        assertEquals(dyn.get(), (char) 'S');
        
        reset();
    }
    
    /*
     * stringPrimitiveTest
     */
    @Test
    public void stringPrimitiveTest() {
        
        DataTypeDescriptor stringDesc = new PrimitiveTypeDescriptor(TypeKind.STRING_TYPE, "");
        stringDesc.setMaxFixedLength(5);
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(stringDesc);
        if (!dyn.set((String) "Test")) {
            assertTrue(false);
        }
        assertEquals(dyn.get(), (String) "Test");
        
        reset();
    }

        

}
