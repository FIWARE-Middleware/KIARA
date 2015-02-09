package org.fiware.kiara.serialization;

import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.fiware.kiara.serialization.impl.CDRSerializer;
import org.fiware.kiara.transport.impl.TransportMessage;

public class CDRSetTest {

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
    public <T> void deserializeSetCharTest() {
        Set<Character> in = new HashSet<Character>();
        for(int i=0; i < 10; ++i) {
            in.add(new String(i+"").charAt(0));
        }

        Set<Character> out = null;
        
        try {
            ser.serializeSetChar(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSetChar(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSetCharTest() {
        int c = 0;
        Set<HashSet<Character>> in = new HashSet<HashSet<Character>>(2);
        for(int i=0; i < 2; ++i) {
            HashSet<Character> inner = new HashSet<Character>(3);
            for (int j=0; j < 3; ++j) {
                inner.add(new String(c+"").charAt(0));
                ++c;
            }
            in.add(inner);
        }

        Set<T> out = null;
        
        try {
            ser.serializeSetChar(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSetChar(message, "", 2);
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
    public <T> void deserializeSetByteTest() {
        Set<Byte> in = new HashSet<Byte>();
        for(int i=0; i < 10; ++i) {
            in.add((byte) i);
        }

        Set<Byte> out = null;
        
        try {
            ser.serializeSetByte(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSetByte(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSetByteTest() {
        int c = 0;
        Set<HashSet<Byte>> in = new HashSet<HashSet<Byte>>(2);
        for(int i=0; i < 2; ++i) {
            HashSet<Byte> inner = new HashSet<Byte>(3);
            for (int j=0; j < 3; ++j) {
                inner.add((byte) c);
                ++c;
            }
            in.add(inner);
        }

        Set<T> out = null;
        
        try {
            ser.serializeSetByte(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSetByte(message, "", 2);
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
    public <T> void deserializeSetI16Test() {
        Set<Short> in = new HashSet<Short>();
        for(int i=0; i < 10; ++i) {
            in.add((short) i);
        }

        Set<Short> out = null;
        
        try {
            ser.serializeSetI16(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSetI16(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSetI16Test() {
        int c = 0;
        Set<HashSet<Short>> in = new HashSet<HashSet<Short>>(2);
        for(int i=0; i < 2; ++i) {
            HashSet<Short> inner = new HashSet<Short>(3);
            for (int j=0; j < 3; ++j) {
                inner.add((short) c);
                ++c;
            }
            in.add(inner);
        }

        Set<T> out = null;
        
        try {
            ser.serializeSetI16(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSetI16(message, "", 2);
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
    public <T> void deserializeSetUI16Test() {
        Set<Short> in = new HashSet<Short>();
        for(int i=0; i < 10; ++i) {
            in.add((short) i);
        }

        Set<Short> out = null;
        
        try {
            ser.serializeSetUI16(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSetUI16(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSetUI16Test() {
        int c = 0;
        Set<HashSet<Short>> in = new HashSet<HashSet<Short>>(2);
        for(int i=0; i < 2; ++i) {
            HashSet<Short> inner = new HashSet<Short>(3);
            for (int j=0; j < 3; ++j) {
                inner.add((short) c);
                ++c;
            }
            in.add(inner);
        }

        Set<T> out = null;
        
        try {
            ser.serializeSetUI16(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSetUI16(message, "", 2);
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
    public <T> void deserializeSetI32Test() {
        Set<Integer> in = new HashSet<Integer>();
        for(int i=0; i < 10; ++i) {
            in.add((int) i);
        }

        Set<Integer> out = null;
        
        try {
            ser.serializeSetI32(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSetI32(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSetI32Test() {
        int c = 0;
        Set<HashSet<Integer>> in = new HashSet<HashSet<Integer>>(2);
        for(int i=0; i < 2; ++i) {
            HashSet<Integer> inner = new HashSet<Integer>(3);
            for (int j=0; j < 3; ++j) {
                inner.add((int) c);
                ++c;
            }
            in.add(inner);
        }

        Set<T> out = null;
        
        try {
            ser.serializeSetI32(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSetI32(message, "", 2);
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
    public <T> void deserializeSetUI32Test() {
        Set<Integer> in = new HashSet<Integer>();
        for(int i=0; i < 10; ++i) {
            in.add((int) i);
        }

        Set<Integer> out = null;
        
        try {
            ser.serializeSetUI32(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSetUI32(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSetUI32Test() {
        int c = 0;
        Set<HashSet<Integer>> in = new HashSet<HashSet<Integer>>(2);
        for(int i=0; i < 2; ++i) {
            HashSet<Integer> inner = new HashSet<Integer>(3);
            for (int j=0; j < 3; ++j) {
                inner.add((int) c);
                ++c;
            }
            in.add(inner);
        }

        Set<T> out = null;
        
        try {
            ser.serializeSetUI32(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSetUI32(message, "", 2);
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
    public <T> void deserializeSetI64Test() {
        Set<Long> in = new HashSet<Long>();
        for(int i=0; i < 10; ++i) {
            in.add((long) i);
        }

        Set<Long> out = null;
        
        try {
            ser.serializeSetI64(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSetI64(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSetI64Test() {
        int c = 0;
        Set<HashSet<Long>> in = new HashSet<HashSet<Long>>(2);
        for(int i=0; i < 2; ++i) {
            HashSet<Long> inner = new HashSet<Long>(3);
            for (int j=0; j < 3; ++j) {
                inner.add((long) c);
                ++c;
            }
            in.add(inner);
        }

        Set<T> out = null;
        
        try {
            ser.serializeSetI64(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSetI64(message, "", 2);
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
    public <T> void deserializeSetUI64Test() {
        Set<Long> in = new HashSet<Long>();
        for(int i=0; i < 10; ++i) {
            in.add((long) i);
        }

        Set<Long> out = null;
        
        try {
            ser.serializeSetUI64(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSetUI64(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSetUI64Test() {
        int c = 0;
        Set<HashSet<Long>> in = new HashSet<HashSet<Long>>(2);
        for(int i=0; i < 2; ++i) {
            HashSet<Long> inner = new HashSet<Long>(3);
            for (int j=0; j < 3; ++j) {
                inner.add((long) c);
                ++c;
            }
            in.add(inner);
        }

        Set<T> out = null;
        
        try {
            ser.serializeSetUI64(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSetUI64(message, "", 2);
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
    public <T> void deserializeSetFloat32Test() {
        Set<Float> in = new HashSet<Float>();
        for(int i=0; i < 10; ++i) {
            in.add((float) i);
        }

        Set<Float> out = null;
        
        try {
            ser.serializeSetFloat32(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSetFloat32(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSetFloat32Test() {
        int c = 0;
        Set<HashSet<Float>> in = new HashSet<HashSet<Float>>(2);
        for(int i=0; i < 2; ++i) {
            HashSet<Float> inner = new HashSet<Float>(3);
            for (int j=0; j < 3; ++j) {
                inner.add((float) c);
                ++c;
            }
            in.add(inner);
        }

        Set<T> out = null;
        
        try {
            ser.serializeSetFloat32(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSetFloat32(message, "", 2);
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
    public <T> void deserializeSetFloat64Test() {
        Set<Double> in = new HashSet<Double>();
        for(int i=0; i < 10; ++i) {
            in.add((double) i);
        }

        Set<Double> out = null;
        
        try {
            ser.serializeSetFloat64(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSetFloat64(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSetFloat64Test() {
        int c = 0;
        Set<HashSet<Double>> in = new HashSet<HashSet<Double>>(2);
        for(int i=0; i < 2; ++i) {
            HashSet<Double> inner = new HashSet<Double>(3);
            for (int j=0; j < 3; ++j) {
                inner.add((double) c);
                ++c;
            }
            in.add(inner);
        }

        Set<T> out = null;
        
        try {
            ser.serializeSetFloat64(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSetFloat64(message, "", 2);
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
    public <T> void deserializeSetBooleanTest() {
        Set<Boolean> in = new HashSet<Boolean>();
        for(int i=0; i < 2; ++i) {
            if  (i==0) {
                in.add(false);
            } else {
                in.add(true);
            }
        }

        Set<Boolean> out = null;
        
        try {
            ser.serializeSetBoolean(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSetBoolean(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSetBooleanTest() {
        Set<HashSet<Boolean>> in = new HashSet<HashSet<Boolean>>(2);
        for(int i=0; i < 2; ++i) {
            HashSet<Boolean> inner = new HashSet<Boolean>(3);
            for (int j=0; j < 3; ++j) {
                inner.add(true);
            }
            in.add(inner);
        }

        Set<T> out = null;
        
        try {
            ser.serializeSetBoolean(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSetBoolean(message, "", 2);
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
    public <T> void deserializeSetStringTest() {
        Set<String> in = new HashSet<String>();
        for(int i=0; i < 10; ++i) {
            in.add(new String("Set "+i));
        }

        Set<String> out = null;
        
        try {
            ser.serializeSetString(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSetString(message, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSetStringTest() {
        Set<HashSet<String>> in = new HashSet<HashSet<String>>(2);
        for(int i=0; i < 2; ++i) {
            HashSet<String> inner = new HashSet<String>(3);
            for (int j=0; j < 3; ++j) {
                inner.add(new String("Set "+i));
            }
            in.add(inner);
        }

        Set<T> out = null;
        
        try {
            ser.serializeSetString(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSetString(message, "", 2);
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
    public <T> void deserializeSetTest() {
        Set<GenericType> in = new HashSet<GenericType>();
        for(int i=0; i < 10; ++i) {
            in.add(new GenericType(i, "Hello World "+i));
        }

        Set<GenericType> out = null;
        
        try {
            ser.serializeSet(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSet(message, "", GenericType.class, 1);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
        
        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSetTest() {
        int c = 0;
        Set<HashSet<GenericType>> in = new HashSet<HashSet<GenericType>>(2);
        for(int i=0; i < 2; ++i) {
            HashSet<GenericType> inner = new HashSet<GenericType>(3);
            for (int j=0; j < 3; ++j) {
                inner.add(new GenericType(c, "Hello World "+i+j));
                ++c;
            }
            in.add(inner);
        }
        
        Set<T> out = null;
        
        try {
            ser.serializeSet(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSet(message, "", GenericType.class, 2);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
        
        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    
}
