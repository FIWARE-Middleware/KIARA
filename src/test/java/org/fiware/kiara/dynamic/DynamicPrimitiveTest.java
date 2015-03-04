package org.fiware.kiara.dynamic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.fiware.kiara.dynamic.data.DynamicPrimitive;
import org.fiware.kiara.dynamic.impl.DynamicTypeBuilderImpl;
import org.fiware.kiara.serialization.MockTransportMessage;
import org.fiware.kiara.serialization.impl.CDRSerializer;
import org.fiware.kiara.transport.impl.TransportMessage;
import org.fiware.kiara.typecode.TypeDescriptorBuilder;
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.PrimitiveTypeDescriptor;
import org.fiware.kiara.typecode.impl.TypeDescriptorBuilderImpl;
import org.fiware.kiara.typecode.impl.data.DataTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.PrimitiveTypeDescriptorImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DynamicPrimitiveTest {
    
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

    /*
     * booleanPrimitiveTest
     */
    @Test
    public void booleanPrimitiveTest() {
        
        PrimitiveTypeDescriptor booleanDesc = (PrimitiveTypeDescriptor) tdbuilder.createTypeDescriptor(TypeKind.BOOLEAN_TYPE, "");
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
        
        PrimitiveTypeDescriptor byteDesc = (PrimitiveTypeDescriptor) tdbuilder.createTypeDescriptor(TypeKind.BYTE_TYPE, "");
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
        
        PrimitiveTypeDescriptor int16Desc = (PrimitiveTypeDescriptor) tdbuilder.createTypeDescriptor(TypeKind.INT_16_TYPE, "");
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
        
        PrimitiveTypeDescriptor uint16Desc = (PrimitiveTypeDescriptor) tdbuilder.createTypeDescriptor(TypeKind.UINT_16_TYPE, "");
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
        
        PrimitiveTypeDescriptor int32Desc = (PrimitiveTypeDescriptor) tdbuilder.createTypeDescriptor(TypeKind.INT_32_TYPE, "");
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
        
        PrimitiveTypeDescriptor uint32Desc = (PrimitiveTypeDescriptor) tdbuilder.createTypeDescriptor(TypeKind.UINT_32_TYPE, "");
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
        
        PrimitiveTypeDescriptor int64Desc = (PrimitiveTypeDescriptor) tdbuilder.createTypeDescriptor(TypeKind.INT_64_TYPE, "");
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
        
        PrimitiveTypeDescriptor uint64Desc = (PrimitiveTypeDescriptor) tdbuilder.createTypeDescriptor(TypeKind.UINT_64_TYPE, "");
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
        
        PrimitiveTypeDescriptor float32Desc = (PrimitiveTypeDescriptor) tdbuilder.createTypeDescriptor(TypeKind.FLOAT_32_TYPE, "");
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
        
        PrimitiveTypeDescriptor float64Desc = (PrimitiveTypeDescriptor) tdbuilder.createTypeDescriptor(TypeKind.FLOAT_64_TYPE, "");
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
        
        PrimitiveTypeDescriptor charDesc = (PrimitiveTypeDescriptor) tdbuilder.createTypeDescriptor(TypeKind.CHAR_8_TYPE, "");
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
        
        PrimitiveTypeDescriptor stringDesc = (PrimitiveTypeDescriptor) tdbuilder.createTypeDescriptor(TypeKind.STRING_TYPE, "");
        stringDesc.setMaxFixedLength(5);
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(stringDesc);
        if (!dyn.set((String) "Test")) {
            assertTrue(false);
        }
        assertEquals(dyn.get(), (String) "Test");
        
        reset();
    }

        

}
