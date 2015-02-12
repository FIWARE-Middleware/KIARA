package org.fiware.kiara.serialization;

import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.fiware.kiara.serialization.impl.CDRSerializer;
import org.fiware.kiara.serialization.types.GenericEnumeration;
import org.fiware.kiara.serialization.types.GenericType;
import org.fiware.kiara.transport.impl.TransportMessage;

public class CDRArrayTest {

    private CDRSerializer ser;
    private ByteBuffer buffer;
    private TransportMessage message;

    @Before
    public void init() {
        this.ser = new CDRSerializer();
        this.buffer = ByteBuffer.allocate(500);
        this.buffer.order(ByteOrder.LITTLE_ENDIAN);
        this.message = new MockTransportMessage(buffer);
    }

    @After
    public void detach() {
        this.message.getPayload().clear();
    }

    public void reset() {
        this.message.getPayload().clear();
    }
    
    /*
     * SerializeArrayChar
     */

    @Test
    public void serializeArrayCharTest() {
        ArrayList<Character> in = new ArrayList<Character>(1);
        in.add('a');

        try {
            ser.serializeArrayChar(message, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    /*
     * SerializeArrayByte
     */

    @Test
    public void serializeArrayByteTest() {
        List<Byte> in = new ArrayList<Byte>();
        in.add((byte) 5);

        try {
            ser.serializeArrayByte(message, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    /*
     * SerializeArrayI16
     */

    @Test
    public void serializeArrayI16Test() {
        List<Short> in = new ArrayList<Short>();
        in.add((short) 5);

        try {
            ser.serializeArrayI16(message, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    /*
     * SerializeArrayUI16
     */

    @Test
    public void serializeArrayUI16Test() {
        List<Short> in = new ArrayList<Short>();
        in.add((short) 5);

        try {
            ser.serializeArrayUI16(message, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    /*
     * SerializeArrayI32
     */

    @Test
    public void serializeArrayI32Test() {
        List<Integer> in = new ArrayList<Integer>();
        in.add((int) 5);

        try {
            ser.serializeArrayI32(message, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    /*
     * SerializeArrayUI32
     */

    @Test
    public void serializeArrayUI32Test() {
        List<Integer> in = new ArrayList<Integer>();
        in.add((int) 5);

        try {
            ser.serializeArrayUI32(message, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    /*
     * SerializeArrayI64
     */

    @Test
    public void serializeArrayI64Test() {
        List<Long> in = new ArrayList<Long>();
        in.add((long) 5);

        try {
            ser.serializeArrayI64(message, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    /*
     * SerializeArrayUI64
     */

    @Test
    public void serializeArrayUI64Test() {
        List<Long> in = new ArrayList<Long>();
        in.add((long) 5);

        try {
            ser.serializeArrayUI64(message, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    /*
     * SerializeArrayFloat32
     */

    @Test
    public void serializeArrayFloat32Test() {
        List<Float> in = new ArrayList<Float>();
        in.add((float) 5.0);

        try {
            ser.serializeArrayFloat32(message, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    /*
     * SerializeArrayFloat64
     */

    @Test
    public void serializeArrayFloat64Test() {
        List<Double> in = new ArrayList<Double>();
        in.add((double) 5.0);

        try {
            ser.serializeArrayFloat64(message, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    /*
     * SerializeArrayBoolean
     */

    @Test
    public void serializeArrayBooleanTest() {
        List<Boolean> in = new ArrayList<Boolean>();
        in.add(true);

        try {
            ser.serializeArrayBoolean(message, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    /*
     * SerializeArrayString
     */

    @Test
    public void serializeArrayStringTest() {
        List<String> in = new ArrayList<String>();
        in.add("one");

        try {
            ser.serializeArrayString(message, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }
    
    /*
     * SerializeArrayEnum
     */

    @Test
    public void serializeArrayEnumTest() {
        List<GenericEnumeration> in = new ArrayList<GenericEnumeration>();
        GenericEnumeration content = GenericEnumeration.second_val;
        in.add(content);

        try {
            ser.serializeArray(message, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    /*
     * Array of generic types
     */

    @Test
    public void serializeArrayTest() {
        List<GenericType> in = new ArrayList<GenericType>();
        in.add(new GenericType(1, "one"));

        try {
            ser.serializeArray(message, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    /*
     * SerializeArrayChar
     */
    
    @Test
    public <T> void deserializeUniDimArrayCharTest() {
        ArrayList<Character> in = new ArrayList<Character>(1);
        for(int i=0; i < 1; ++i) {
            in.add('A');
        }

        List<T> out = null;
        
        try {
            ser.serializeArrayChar(message, "", in, 1);
            message.getPayload().rewind();
            out = ser.deserializeArrayChar(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }

    @Test
    public <T> void deserializeMultiDimArrayCharTest() {
        List<ArrayList<Character>> in = new ArrayList<ArrayList<Character>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<Character> inner = new ArrayList<Character>(5);
            for (int j=0; j < 5; ++j) {
                inner.add('A');
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeArrayChar(message, "", in, 3, 5);
            message.getPayload().rewind();
            out = ser.deserializeArrayChar(message, "", 3, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    /*
     * SerializeArrayByte
     */
    
    @Test
    public <T> void deserializeUniDimArrayByteTest() {
        ArrayList<Byte> in = new ArrayList<Byte>(1);
        for(int i=0; i < 1; ++i) {
            in.add((byte) 55);
        }

        List<T> out = null;
        
        try {
            ser.serializeArrayByte(message, "", in, 1);
            message.getPayload().rewind();
            out = ser.deserializeArrayByte(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }

    
    
    @Test
    public <T> void deserializeMultiDimArrayByteTest() {
        List<ArrayList<Byte>> in = new ArrayList<ArrayList<Byte>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<Byte> inner = new ArrayList<Byte>(5);
            for (int j=0; j < 5; ++j) {
                inner.add((byte) 55);
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeArrayByte(message, "", in, 3, 5);
            message.getPayload().rewind();
            out = ser.deserializeArrayByte(message, "", 3, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    /*
     * SerializeArrayI16
     */
    
    @Test
    public <T> void deserializeUniDimArrayI16Test() {
        ArrayList<Short> in = new ArrayList<Short>(1);
        for(int i=0; i < 1; ++i) {
            in.add((short) 55);
        }

        List<T> out = null;
        
        try {
            ser.serializeArrayI16(message, "", in, 1);
            message.getPayload().rewind();
            out = ser.deserializeArrayI16(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }

    
    
    @Test
    public <T> void deserializeMultiDimArrayI16Test() {
        List<ArrayList<Short>> in = new ArrayList<ArrayList<Short>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<Short> inner = new ArrayList<Short>(5);
            for (int j=0; j < 5; ++j) {
                inner.add((short) 55);
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeArrayI16(message, "", in, 3, 5);
            message.getPayload().rewind();
            out = ser.deserializeArrayI16(message, "", 3, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    /*
     * SerializeArrayUI16
     */
    
    @Test
    public <T> void deserializeUniDimArraUyI16Test() {
        ArrayList<Short> in = new ArrayList<Short>(1);
        for(int i=0; i < 1; ++i) {
            in.add((short) 55);
        }

        List<T> out = null;
        
        try {
            ser.serializeArrayUI16(message, "", in, 1);
            message.getPayload().rewind();
            out = ser.deserializeArrayUI16(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }

    
    
    @Test
    public <T> void deserializeMultiDimArrayUI16Test() {
        List<ArrayList<Short>> in = new ArrayList<ArrayList<Short>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<Short> inner = new ArrayList<Short>(5);
            for (int j=0; j < 5; ++j) {
                inner.add((short) 55);
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeArrayUI16(message, "", in, 3, 5);
            message.getPayload().rewind();
            out = ser.deserializeArrayUI16(message, "", 3, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    /*
     * SerializeArrayI32
     */
    
    @Test
    public <T> void deserializeUniDimArrayI32Test() {
        ArrayList<Integer> in = new ArrayList<Integer>(1);
        for(int i=0; i < 1; ++i) {
            in.add((int) 55);
        }

        List<T> out = null;
        
        try {
            ser.serializeArrayI32(message, "", in, 1);
            message.getPayload().rewind();
            out = ser.deserializeArrayI32(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }

    
    
    @Test
    public <T> void deserializeMultiDimArrayI32Test() {
        List<ArrayList<Integer>> in = new ArrayList<ArrayList<Integer>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<Integer> inner = new ArrayList<Integer>(5);
            for (int j=0; j < 5; ++j) {
                inner.add((int) 55);
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeArrayI32(message, "", in, 3, 5);
            message.getPayload().rewind();
            out = ser.deserializeArrayI32(message, "", 3, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    /*
     * SerializeArrayUI64
     */
    
    @Test
    public <T> void deserializeUniDimArraUyI32Test() {
        ArrayList<Integer> in = new ArrayList<Integer>(1);
        for(int i=0; i < 1; ++i) {
            in.add((int) 55);
        }

        List<T> out = null;
        
        try {
            ser.serializeArrayUI32(message, "", in, 1);
            message.getPayload().rewind();
            out = ser.deserializeArrayUI32(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }

    
    
    @Test
    public <T> void deserializeMultiDimArrayUI32Test() {
        List<ArrayList<Integer>> in = new ArrayList<ArrayList<Integer>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<Integer> inner = new ArrayList<Integer>(5);
            for (int j=0; j < 5; ++j) {
                inner.add((int) 55);
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeArrayUI32(message, "", in, 3, 5);
            message.getPayload().rewind();
            out = ser.deserializeArrayUI32(message, "", 3, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    /*
     * SerializeArrayI64
     */
    
    @Test
    public <T> void deserializeUniDimArrayI64Test() {
        ArrayList<Long> in = new ArrayList<Long>(1);
        for(int i=0; i < 1; ++i) {
            in.add((long) 55);
        }

        List<T> out = null;
        
        try {
            ser.serializeArrayI64(message, "", in, 1);
            message.getPayload().rewind();
            out = ser.deserializeArrayI64(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }

    
    
    @Test
    public <T> void deserializeMultiDimArrayI64Test() {
        List<ArrayList<Long>> in = new ArrayList<ArrayList<Long>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<Long> inner = new ArrayList<Long>(5);
            for (int j=0; j < 5; ++j) {
                inner.add((long) 55);
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeArrayI64(message, "", in, 3, 5);
            message.getPayload().rewind();
            out = ser.deserializeArrayI64(message, "", 3, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    /*
     * SerializeArrayUI64
     */
    
    @Test
    public <T> void deserializeUniDimArraUyI64Test() {
        ArrayList<Long> in = new ArrayList<Long>(1);
        for(int i=0; i < 1; ++i) {
            in.add((long) 55);
        }

        List<T> out = null;
        
        try {
            ser.serializeArrayUI64(message, "", in, 1);
            message.getPayload().rewind();
            out = ser.deserializeArrayUI64(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }

    
    
    @Test
    public <T> void deserializeMultiDimArrayUI64Test() {
        List<ArrayList<Long>> in = new ArrayList<ArrayList<Long>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<Long> inner = new ArrayList<Long>(5);
            for (int j=0; j < 5; ++j) {
                inner.add((long) 55);
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeArrayUI64(message, "", in, 3, 5);
            message.getPayload().rewind();
            out = ser.deserializeArrayUI64(message, "", 3, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    /*
     * SerializeArrayFloat32
     */
    
    @Test
    public <T> void deserializeUniDimArrayFloat32Test() {
        ArrayList<Float> in = new ArrayList<Float>(1);
        for(int i=0; i < 1; ++i) {
            in.add((float) 55);
        }

        List<T> out = null;
        
        try {
            ser.serializeArrayFloat32(message, "", in, 1);
            message.getPayload().rewind();
            out = ser.deserializeArrayFloat32(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }

    
    
    @Test
    public <T> void deserializeMultiDimArrayFloat32Test() {
        List<ArrayList<Float>> in = new ArrayList<ArrayList<Float>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<Float> inner = new ArrayList<Float>(5);
            for (int j=0; j < 5; ++j) {
                inner.add((float) 55);
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeArrayFloat32(message, "", in, 3, 5);
            message.getPayload().rewind();
            out = ser.deserializeArrayFloat32(message, "", 3, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    /*
     * SerializeArrayFloat64
     */
    
    @Test
    public <T> void deserializeUniDimArrayFloat64Test() {
        ArrayList<Double> in = new ArrayList<Double>(1);
        for(int i=0; i < 1; ++i) {
            in.add((double) 55);
        }

        List<T> out = null;
        
        try {
            ser.serializeArrayFloat64(message, "", in, 1);
            message.getPayload().rewind();
            out = ser.deserializeArrayFloat64(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }

    
    
    @Test
    public <T> void deserializeMultiDimArrayFloat64Test() {
        List<ArrayList<Double>> in = new ArrayList<ArrayList<Double>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<Double> inner = new ArrayList<Double>(5);
            for (int j=0; j < 5; ++j) {
                inner.add((double) 55);
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeArrayFloat64(message, "", in, 3, 5);
            message.getPayload().rewind();
            out = ser.deserializeArrayFloat64(message, "", 3, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    /*
     * SerializeArrayBoolean
     */
    
    @Test
    public <T> void deserializeUniDimArrayBooleanTest() {
        ArrayList<Boolean> in = new ArrayList<Boolean>(1);
        for(int i=0; i < 1; ++i) {
            in.add((boolean) true);
        }

        List<T> out = null;
        
        try {
            ser.serializeArrayBoolean(message, "", in, 1);
            message.getPayload().rewind();
            out = ser.deserializeArrayBoolean(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }

    
    
    @Test
    public <T> void deserializeMultiDimArrayBooleanTest() {
        List<ArrayList<Boolean>> in = new ArrayList<ArrayList<Boolean>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<Boolean> inner = new ArrayList<Boolean>(5);
            for (int j=0; j < 5; ++j) {
                inner.add((boolean) true);
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeArrayBoolean(message, "", in, 3, 5);
            message.getPayload().rewind();
            out = ser.deserializeArrayBoolean(message, "", 3, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    /*
     * SerializeArrayString
     */
    
    @Test
    public <T> void deserializeUniDimArrayStringTest() {
        ArrayList<String> in = new ArrayList<String>(1);
        for(int i=0; i < 1; ++i) {
            in.add((String) "Hello World");
        }

        List<T> out = null;
        
        try {
            ser.serializeArrayString(message, "", in, 1);
            message.getPayload().rewind();
            out = ser.deserializeArrayString(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }

    
    
    @Test
    public <T> void deserializeMultiDimArrayStringTest() {
        List<ArrayList<String>> in = new ArrayList<ArrayList<String>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<String> inner = new ArrayList<String>(5);
            for (int j=0; j < 5; ++j) {
                inner.add((String) "Hello World");
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeArrayString(message, "", in, 3, 5);
            message.getPayload().rewind();
            out = ser.deserializeArrayString(message, "", 3, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in,out));

        reset();
    }
    
    /*
     * SerializeArrayString
     */
    
    @Test
    public <T> void deserializeUniDimArrayEnumTest() {
        ArrayList<GenericEnumeration> in = new ArrayList<GenericEnumeration>(1);
        for(int i=0; i < 1; ++i) {
            in.add(GenericEnumeration.second_val);
        }

        List<T> out = null;
        
        try {
            ser.serializeArray(message, "", in, 1);
            message.getPayload().rewind();
            out = ser.deserializeArray(message, "", GenericEnumeration.class, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }

    
    
    @Test
    public <T> void deserializeMultiDimArrayEnumTest() {
        List<ArrayList<GenericEnumeration>> in = new ArrayList<ArrayList<GenericEnumeration>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<GenericEnumeration> inner = new ArrayList<GenericEnumeration>(5);
            for (int j=0; j < 5; ++j) {
                if (i % 2 == 0) {
                    if (j % 2 == 0) {
                        inner.add(GenericEnumeration.third_val);
                    } else {
                        inner.add(GenericEnumeration.second_val);
                    }
                } else {
                    inner.add(GenericEnumeration.first_val);
                }
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeArray(message, "", in, 3, 5);
            message.getPayload().rewind();
            out = ser.deserializeArray(message, "", GenericEnumeration.class, 3, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in,out));

        reset();
    }
    
    /*
     * SerializeArray
     */
    
    @SuppressWarnings("unchecked")
    @Test
    public <T> void deserializeUniDimArrayTest() {
        ArrayList<GenericType> in = new ArrayList<GenericType>(1);
        for(int i=0; i < 1; ++i) {
            in.add(new GenericType(i, "HelloWorld"));
        }

        List<T> out = null;
        
        try {
            ser.serializeArray(message, "", in, 1);
            message.getPayload().rewind();
            out = (List<T>) ser.deserializeArray(message, "", GenericType.class, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in,out));

        reset();
    }

    
    
    @SuppressWarnings("unchecked")
    @Test
    public <T> void deserializeMultiDimArrayTest() {
        List<ArrayList<GenericType>> in = new ArrayList<ArrayList<GenericType>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<GenericType> inner = new ArrayList<GenericType>(5);
            for (int j=0; j < 5; ++j) {
                inner.add(new GenericType(j, "HelloWorld"));
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeArray(message, "", in, 3, 5);
            message.getPayload().rewind();
            out = (List<T>) ser.deserializeArray(message, "", GenericType.class, 3, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }

}
