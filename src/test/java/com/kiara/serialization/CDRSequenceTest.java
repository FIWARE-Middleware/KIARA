package com.kiara.serialization;

import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.kiara.serialization.impl.CDRSerializer;
import com.kiara.transport.impl.TransportMessage;

public class CDRSequenceTest {

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
     * CharSequenceTest
     */

    @Test
    public <T> void deserializeSequenceCharTest() {
        ArrayList<Character> in = new ArrayList<Character>(1);
        for(int i=0; i < 10; ++i) {
            in.add('A');
        }

        List<Character> out = null;
        
        try {
            ser.serializeSequenceChar(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequenceChar(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSequenceCharTest() {
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
            ser.serializeSequenceChar(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequenceChar(message, "", 2);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    /*
     * ByteSequenceTest
     */

    @Test
    public <T> void deserializeSequenceByteTest() {
        ArrayList<Byte> in = new ArrayList<Byte>(1);
        for(int i=0; i < 10; ++i) {
            in.add((byte) 5);
        }

        List<T> out = null;
        
        try {
            ser.serializeSequenceByte(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequenceByte(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSequenceByteTest() {
        List<ArrayList<Byte>> in = new ArrayList<ArrayList<Byte>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<Byte> inner = new ArrayList<Byte>(5);
            for (int j=0; j < 5; ++j) {
                inner.add((byte) 5);
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeSequenceByte(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequenceByte(message, "", 2);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    /*
     * I16SequenceTest
     */

    @Test
    public <T> void deserializeSequenceI16Test() {
        ArrayList<Short> in = new ArrayList<Short>(1);
        for(int i=0; i < 10; ++i) {
            in.add((short) 5);
        }

        List<T> out = null;
        
        try {
            ser.serializeSequenceI16(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequenceI16(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSequenceI16Test() {
        List<ArrayList<Short>> in = new ArrayList<ArrayList<Short>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<Short> inner = new ArrayList<Short>(5);
            for (int j=0; j < 5; ++j) {
                inner.add((short) 5);
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeSequenceI16(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequenceI16(message, "", 2);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    /*
     * UI16SequenceTest
     */

    @Test
    public <T> void deserializeSequenceUI16Test() {
        ArrayList<Short> in = new ArrayList<Short>(1);
        for(int i=0; i < 10; ++i) {
            in.add((short) 5);
        }

        List<T> out = null;
        
        try {
            ser.serializeSequenceUI16(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequenceUI16(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSequenceUI16Test() {
        List<ArrayList<Short>> in = new ArrayList<ArrayList<Short>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<Short> inner = new ArrayList<Short>(5);
            for (int j=0; j < 5; ++j) {
                inner.add((short) 5);
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeSequenceUI16(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequenceUI16(message, "", 2);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    /*
     * I32SequenceTest
     */

    @Test
    public <T> void deserializeSequenceI32Test() {
        ArrayList<Integer> in = new ArrayList<Integer>(1);
        for(int i=0; i < 10; ++i) {
            in.add((int) 5);
        }

        List<T> out = null;
        
        try {
            ser.serializeSequenceI32(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequenceI32(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSequenceI32Test() {
        List<ArrayList<Integer>> in = new ArrayList<ArrayList<Integer>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<Integer> inner = new ArrayList<Integer>(5);
            for (int j=0; j < 5; ++j) {
                inner.add((int) 5);
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeSequenceI32(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequenceI32(message, "", 2);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    /*
     * UI32SequenceTest
     */

    @Test
    public <T> void deserializeSequenceUI32Test() {
        ArrayList<Integer> in = new ArrayList<Integer>(1);
        for(int i=0; i < 10; ++i) {
            in.add((int) 5);
        }

        List<T> out = null;
        
        try {
            ser.serializeSequenceUI32(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequenceUI32(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSequenceUI32Test() {
        List<ArrayList<Integer>> in = new ArrayList<ArrayList<Integer>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<Integer> inner = new ArrayList<Integer>(5);
            for (int j=0; j < 5; ++j) {
                inner.add((int) 5);
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeSequenceUI32(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequenceUI32(message, "", 2);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    /*
     * I64SequenceTest
     */

    @Test
    public <T> void deserializeSequenceI64Test() {
        ArrayList<Long> in = new ArrayList<Long>(1);
        for(int i=0; i < 10; ++i) {
            in.add((long) 5);
        }

        List<T> out = null;
        
        try {
            ser.serializeSequenceI64(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequenceI64(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSequenceI64Test() {
        List<ArrayList<Long>> in = new ArrayList<ArrayList<Long>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<Long> inner = new ArrayList<Long>(5);
            for (int j=0; j < 5; ++j) {
                inner.add((long) 5);
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeSequenceI64(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequenceI64(message, "", 2);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    /*
     * UI32SequenceTest
     */

    @Test
    public <T> void deserializeSequenceUI64Test() {
        ArrayList<Long> in = new ArrayList<Long>(1);
        for(int i=0; i < 10; ++i) {
            in.add((long) 5);
        }

        List<T> out = null;
        
        try {
            ser.serializeSequenceUI64(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequenceUI64(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSequenceUI64Test() {
        List<ArrayList<Long>> in = new ArrayList<ArrayList<Long>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<Long> inner = new ArrayList<Long>(5);
            for (int j=0; j < 5; ++j) {
                inner.add((long) 5);
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeSequenceUI64(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequenceUI64(message, "", 2);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    /*
     * Float32SequenceTest
     */

    @Test
    public <T> void deserializeSequenceFloat32Test() {
        ArrayList<Float> in = new ArrayList<Float>(1);
        for(int i=0; i < 10; ++i) {
            in.add((float) 5.5);
        }

        List<T> out = null;
        
        try {
            ser.serializeSequenceFloat32(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequenceFloat32(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSequenceFloat32Test() {
        List<ArrayList<Float>> in = new ArrayList<ArrayList<Float>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<Float> inner = new ArrayList<Float>(5);
            for (int j=0; j < 5; ++j) {
                inner.add((float) 5.5);
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeSequenceFloat32(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequenceFloat32(message, "", 2);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    /*
     * Float64SequenceTest
     */

    @Test
    public <T> void deserializeSequenceFloat64Test() {
        ArrayList<Double> in = new ArrayList<Double>(1);
        for(int i=0; i < 10; ++i) {
            in.add((double) 5.5);
        }

        List<T> out = null;
        
        try {
            ser.serializeSequenceFloat64(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequenceFloat64(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSequenceFloat64Test() {
        List<ArrayList<Double>> in = new ArrayList<ArrayList<Double>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<Double> inner = new ArrayList<Double>(5);
            for (int j=0; j < 5; ++j) {
                inner.add((double) 5.5);
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeSequenceFloat64(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequenceFloat64(message, "", 2);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    /*
     * BooleanSequenceTest
     */

    @Test
    public <T> void deserializeSequenceBooleanTest() {
        ArrayList<Boolean> in = new ArrayList<Boolean>(1);
        for(int i=0; i < 10; ++i) {
            if (i%2 == 0) {
                in.add((boolean) true);
            } else {
                in.add((boolean) false);
            }
        }

        List<T> out = null;
        
        try {
            ser.serializeSequenceBoolean(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequenceBoolean(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSequenceBooleanTest() {
        List<ArrayList<Boolean>> in = new ArrayList<ArrayList<Boolean>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<Boolean> inner = new ArrayList<Boolean>(5);
            for (int j=0; j < 5; ++j) {
                if (i%2 == 0) {
                    inner.add((boolean) true);
                } else {
                    inner.add((boolean) false);
                }
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeSequenceBoolean(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequenceBoolean(message, "", 2);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    /*
     * StringSequenceTest
     */

    @Test
    public <T> void deserializeSequenceStringTest() {
        ArrayList<String> in = new ArrayList<String>(1);
        for(int i=0; i < 10; ++i) {
            in.add((String) "Hello World " + i);
        }

        List<T> out = null;
        
        try {
            ser.serializeSequenceString(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequenceString(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSequenceStringTest() {
        List<ArrayList<String>> in = new ArrayList<ArrayList<String>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<String> inner = new ArrayList<String>(5);
            for (int j=0; j < 5; ++j) {
                inner.add((String) "Hello World " + j);
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeSequenceString(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequenceString(message, "", 2);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    /*
     * StringSequenceTest
     */

    @Test
    public <T> void deserializeSequenceTest() {
        ArrayList<GenericType> in = new ArrayList<GenericType>(1);
        for(int i=0; i < 10; ++i) {
            in.add(new GenericType(i, "Hello World "+i));
        }

        List<T> out = null;
        
        try {
            ser.serializeSequence(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequence(message, "", GenericType.class, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSequenceTest() {
        List<ArrayList<GenericType>> in = new ArrayList<ArrayList<GenericType>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<GenericType> inner = new ArrayList<GenericType>(5);
            for (int j=0; j < 5; ++j) {
                inner.add(new GenericType(j, "Hello World "+j));
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeSequence(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequence(message, "", GenericType.class, 2);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    
}
