package org.fiware.kiara.dynamic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import org.fiware.kiara.dynamic.data.DynamicPrimitive;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.CDRSerializer;
import org.fiware.kiara.typecode.TypeDescriptorBuilder;
import org.fiware.kiara.typecode.TypeDescriptorBuilderImpl;
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.PrimitiveTypeDescriptor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DynamicPrimitiveTest {
    
    private CDRSerializer ser;
    //private ByteBuffer buffer;
    BinaryOutputStream bos;
    BinaryInputStream bis;
    //private TransportMessage message;
    DynamicValueBuilder builder;
    TypeDescriptorBuilder tdbuilder;

    @Before
    public void init() {
        this.ser = new CDRSerializer();
        this.bos = new BinaryOutputStream();
        builder = DynamicValueBuilderImpl.getInstance();
        tdbuilder = TypeDescriptorBuilderImpl.getInstance();
    }

    @After
    public void detach() {
        //this.message.getPayload().clear();
    }

    public void reset() {
        this.bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
    }

    /*
     * booleanPrimitiveTest
     */
    @Test
    public void booleanPrimitiveTest() {
        
        PrimitiveTypeDescriptor booleanDesc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.BOOLEAN_TYPE);
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(booleanDesc);
        dyn.set(false);
        assertEquals(dyn.get(), false);
        
    }
    
    /*
     * bytePrimitiveTest
     */
    @Test
    public void bytePrimitiveTest() {
        
        PrimitiveTypeDescriptor byteDesc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.BYTE_TYPE);
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(byteDesc);
        if (!dyn.set((byte) 4)) {
            assertTrue(false);
        }
        assertEquals(dyn.get(), (byte) 4);
        
    }
    
    /*
     * int16PrimitiveTest
     */
    @Test
    public void int16PrimitiveTest() {
        
        PrimitiveTypeDescriptor int16Desc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.INT_16_TYPE);
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(int16Desc);
        if (!dyn.set((short) 4)) {
            assertTrue(false);
        }
        assertEquals(dyn.get(), (short) 4);
        
    }
    
    /*
     * uint16PrimitiveTest
     */
    @Test
    public void uint16PrimitiveTest() {
        
        PrimitiveTypeDescriptor uint16Desc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.UINT_16_TYPE);
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(uint16Desc);
        if (!dyn.set((short) 4)) {
            assertTrue(false);
        }
        assertEquals(dyn.get(), (short) 4);
        
    }
    
    /*
     * int32PrimitiveTest
     */
    @Test
    public void int32PrimitiveTest() {
        
        PrimitiveTypeDescriptor int32Desc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(int32Desc);
        if (!dyn.set((int) 4)) {
            assertTrue(false);
        }
        assertEquals(dyn.get(), (int) 4);
        
    }
    
    /*
     * uint32PrimitiveTest
     */
    @Test
    public void uint32PrimitiveTest() {
        
        PrimitiveTypeDescriptor uint32Desc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.UINT_32_TYPE);
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(uint32Desc);
        if (!dyn.set((int) 4)) {
            assertTrue(false);
        }
        assertEquals(dyn.get(), (int) 4);
        
    }
    
    /*
     * int64PrimitiveTest
     */
    @Test
    public void int64PrimitiveTest() {
        
        PrimitiveTypeDescriptor int64Desc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.INT_64_TYPE);
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(int64Desc);
        if (!dyn.set((long) 4)) {
            assertTrue(false);
        }
        assertEquals(dyn.get(), (long) 4);
        
    }
    
    /*
     * uint64PrimitiveTest
     */
    @Test
    public void uint64PrimitiveTest() {
        
        PrimitiveTypeDescriptor uint64Desc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.UINT_64_TYPE);
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(uint64Desc);
        if (!dyn.set((long) 4)) {
            assertTrue(false);
        }
        assertEquals(dyn.get(), (long) 4);
        
    }
    
    /*
     * float32PrimitiveTest
     */
    @Test
    public void float32PrimitiveTest() {
        
        PrimitiveTypeDescriptor float32Desc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.FLOAT_32_TYPE);
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(float32Desc);
        if (!dyn.set((float) 4)) {
            assertTrue(false);
        }
        assertEquals(dyn.get(), (float) 4);
        
    }
    
    /*
     * uint64PrimitiveTest
     */
    @Test
    public void float64PrimitiveTest() {
        
        PrimitiveTypeDescriptor float64Desc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.FLOAT_64_TYPE);
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(float64Desc);
        if (!dyn.set((double) 4)) {
            assertTrue(false);
        }
        assertEquals(dyn.get(), (double) 4);
        
    }
    
    /*
     * charPrimitiveTest
     */
    @Test
    public void charPrimitiveTest() {
        
        PrimitiveTypeDescriptor charDesc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.CHAR_8_TYPE);
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(charDesc);
        if (!dyn.set((char) 'S')) {
            assertTrue(false);
        }
        assertEquals(dyn.get(), (char) 'S');
        
    }
    
    /*
     * stringPrimitiveTest
     */
    @Test
    public void stringPrimitiveTest() {
        
        PrimitiveTypeDescriptor stringDesc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.STRING_TYPE);
        stringDesc.setMaxFixedLength(5);
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(stringDesc);
        if (!dyn.set((String) "Test")) {
            assertTrue(false);
        }
        assertEquals(dyn.get(), (String) "Test");
        
    }
    
    // ---------------- Serialization tests ---------------------------------
    
    /*
     * booleanPrimitiveSerializationTest
     */
    @Test
    public void booleanPrimitiveSerializationTest() {
        
        PrimitiveTypeDescriptor desc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.BOOLEAN_TYPE);
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(desc);
        boolean value = true;
        
        dyn.set(value);
        
        try {
            dyn.serialize(ser, bos, "");
            reset();
            dyn.deserialize(ser, bis, "");
            assertEquals(value, (boolean) dyn.get());
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        
    }
    
    /*
     * bytePrimitiveSerializationTest
     */
    @Test
    public void bytePrimitiveSerializationTest() {
        
        PrimitiveTypeDescriptor desc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.BYTE_TYPE);
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(desc);
        byte value = (byte) 33;
        
        dyn.set(value);
        
        try {
            dyn.serialize(ser, bos, "");
            reset();
            dyn.deserialize(ser, bis, "");
            assertEquals(value, (byte) dyn.get());
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        
    }

    /*
     * int16PrimitiveSerializationTest
     */
    @Test
    public void int16PrimitiveSerializationTest() {
        
        PrimitiveTypeDescriptor desc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.INT_16_TYPE);
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(desc);
        short value = (short) 26;
        
        dyn.set(value);
        
        try {
            dyn.serialize(ser, bos, "");
            reset();
            dyn.deserialize(ser, bis, "");
            assertEquals(value, (short) dyn.get());
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        
    }
    
    /*
     * uint16PrimitiveSerializationTest
     */
    @Test
    public void uint16PrimitiveSerializationTest() {
        
        PrimitiveTypeDescriptor desc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.UINT_16_TYPE);
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(desc);
        short value = (short) 44;
        
        dyn.set(value);
        
        try {
            dyn.serialize(ser, bos, "");
            reset();
            dyn.deserialize(ser, bis, "");
            assertEquals(value, (short) dyn.get());
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        
    }
    
    /*
     * int32PrimitiveSerializationTest
     */
    @Test
    public void int32PrimitiveSerializationTest() {
        
        PrimitiveTypeDescriptor desc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(desc);
        int value = (int) 15;
        
        dyn.set(value);
        
        try {
            dyn.serialize(ser, bos, "");
            reset();
            dyn.deserialize(ser, bis, "");
            assertEquals(value, (int) dyn.get());
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        
    }
    
    /*
     * uint32PrimitiveSerializationTest
     */
    @Test
    public void uint32PrimitiveSerializationTest() {
        
        PrimitiveTypeDescriptor desc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.UINT_32_TYPE);
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(desc);
        int value = (int) 9;
        
        dyn.set(value);
        
        try {
            dyn.serialize(ser, bos, "");
            reset();
            dyn.deserialize(ser, bis, "");
            assertEquals(value, (int) dyn.get());
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        
    }
    
    /*
     * int32PrimitiveSerializationTest
     */
    @Test
    public void int64PrimitiveSerializationTest() {
        
        PrimitiveTypeDescriptor desc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.INT_64_TYPE);
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(desc);
        long value = (long) 5;
        
        dyn.set(value);
        
        try {
            dyn.serialize(ser, bos, "");
            reset();
            dyn.deserialize(ser, bis, "");
            assertEquals(value, (long) dyn.get());
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        
    }
    
    /*
     * uint64PrimitiveSerializationTest
     */
    @Test
    public void uint64PrimitiveSerializationTest() {
        
        PrimitiveTypeDescriptor desc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.UINT_64_TYPE);
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(desc);
        long value = (long) 2589;
        
        dyn.set(value);
        
        try {
            dyn.serialize(ser, bos, "");
            reset();
            dyn.deserialize(ser, bis, "");
            assertEquals(value, (long) dyn.get());
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        
    }
    
    /*
     * float32PrimitiveSerializationTest
     */
    @Test
    public void float32PrimitiveSerializationTest() {
        
        PrimitiveTypeDescriptor desc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.FLOAT_32_TYPE);
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(desc);
        float value = (float) 5.45;
        
        dyn.set(value);
        
        try {
            dyn.serialize(ser, bos, "");
            reset();
            dyn.deserialize(ser, bis, "");
            assertTrue(value == (float) dyn.get());
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        
    }
    
    /*
     * float64PrimitiveSerializationTest
     */
    @Test
    public void float64PrimitiveSerializationTest() {
        
        PrimitiveTypeDescriptor desc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.FLOAT_64_TYPE);
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(desc);
        double value = (double) 57.86;
        
        dyn.set(value);
        
        try {
            dyn.serialize(ser, bos, "");
            reset();
            dyn.deserialize(ser, bis, "");
            assertTrue(value == (double) dyn.get());
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        
    }
    
    /*
     * charPrimitiveSerializationTest
     */
    @Test
    public void charPrimitiveSerializationTest() {
        
        PrimitiveTypeDescriptor desc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.CHAR_8_TYPE);
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(desc);
        char value = (char) 'm';
        
        dyn.set(value);
        
        try {
            dyn.serialize(ser, bos, "");
            reset();
            dyn.deserialize(ser, bis, "");
            assertTrue(value == (char) dyn.get());
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        
    }
    
    /*
     * stringPrimitiveSerializationTest
     */
    @Test
    public void stringPrimitiveSerializationTest() {
        
        PrimitiveTypeDescriptor desc = (PrimitiveTypeDescriptor) tdbuilder.createPrimitiveType(TypeKind.STRING_TYPE);
        DynamicPrimitive dyn = (DynamicPrimitive) builder.createData(desc);
        String value = (String) "Hello World!";
        
        dyn.set(value);
        
        try {
            dyn.serialize(ser, bos, "");
            reset();
            dyn.deserialize(ser, bis, "");
            assertTrue(value.equals((String) dyn.get()));
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        
    }
    
        

}
