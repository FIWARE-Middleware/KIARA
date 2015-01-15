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
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.fiware.kiara.serialization.impl.CDRSerializer;
import org.fiware.kiara.serialization.types.GenericEnumeration;
import org.fiware.kiara.serialization.types.GenericType;
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
        for (int i = 0; i < 10; ++i) {
            in.add(new String(i + "").charAt(0));
        }

        Set<Character> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeSetChar(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeSetChar(bis, "", 1);
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
        for (int i = 0; i < 2; ++i) {
            HashSet<Character> inner = new HashSet<Character>(3);
            for (int j = 0; j < 3; ++j) {
                inner.add(new String(c + "").charAt(0));
                ++c;
            }
            in.add(inner);
        }

        Set<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeSetChar(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeSetChar(bis, "", 2);
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
        for (int i = 0; i < 10; ++i) {
            in.add((byte) i);
        }

        Set<Byte> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeSetByte(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeSetByte(bis, "", 1);
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
        for (int i = 0; i < 2; ++i) {
            HashSet<Byte> inner = new HashSet<Byte>(3);
            for (int j = 0; j < 3; ++j) {
                inner.add((byte) c);
                ++c;
            }
            in.add(inner);
        }

        Set<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeSetByte(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeSetByte(bis, "", 2);
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
        for (int i = 0; i < 10; ++i) {
            in.add((short) i);
        }

        Set<Short> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeSetI16(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeSetI16(bis, "", 1);
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
        for (int i = 0; i < 2; ++i) {
            HashSet<Short> inner = new HashSet<Short>(3);
            for (int j = 0; j < 3; ++j) {
                inner.add((short) c);
                ++c;
            }
            in.add(inner);
        }

        Set<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeSetI16(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeSetI16(bis, "", 2);
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
        for (int i = 0; i < 10; ++i) {
            in.add((short) i);
        }

        Set<Short> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeSetUI16(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeSetUI16(bis, "", 1);
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
        for (int i = 0; i < 2; ++i) {
            HashSet<Short> inner = new HashSet<Short>(3);
            for (int j = 0; j < 3; ++j) {
                inner.add((short) c);
                ++c;
            }
            in.add(inner);
        }

        Set<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeSetUI16(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeSetUI16(bis, "", 2);
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
        for (int i = 0; i < 10; ++i) {
            in.add((int) i);
        }

        Set<Integer> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeSetI32(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeSetI32(bis, "", 1);
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
        for (int i = 0; i < 2; ++i) {
            HashSet<Integer> inner = new HashSet<Integer>(3);
            for (int j = 0; j < 3; ++j) {
                inner.add((int) c);
                ++c;
            }
            in.add(inner);
        }

        Set<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeSetI32(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeSetI32(bis, "", 2);
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
        for (int i = 0; i < 10; ++i) {
            in.add((int) i);
        }

        Set<Integer> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeSetUI32(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeSetUI32(bis, "", 1);
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
        for (int i = 0; i < 2; ++i) {
            HashSet<Integer> inner = new HashSet<Integer>(3);
            for (int j = 0; j < 3; ++j) {
                inner.add((int) c);
                ++c;
            }
            in.add(inner);
        }

        Set<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeSetUI32(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeSetUI32(bis, "", 2);
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
        for (int i = 0; i < 10; ++i) {
            in.add((long) i);
        }

        Set<Long> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeSetI64(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeSetI64(bis, "", 1);
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
        for (int i = 0; i < 2; ++i) {
            HashSet<Long> inner = new HashSet<Long>(3);
            for (int j = 0; j < 3; ++j) {
                inner.add((long) c);
                ++c;
            }
            in.add(inner);
        }

        Set<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeSetI64(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeSetI64(bis, "", 2);
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
        for (int i = 0; i < 10; ++i) {
            in.add((long) i);
        }

        Set<Long> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeSetUI64(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeSetUI64(bis, "", 1);
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
        for (int i = 0; i < 2; ++i) {
            HashSet<Long> inner = new HashSet<Long>(3);
            for (int j = 0; j < 3; ++j) {
                inner.add((long) c);
                ++c;
            }
            in.add(inner);
        }

        Set<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeSetUI64(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeSetUI64(bis, "", 2);
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
        for (int i = 0; i < 10; ++i) {
            in.add((float) i);
        }

        Set<Float> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeSetFloat32(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeSetFloat32(bis, "", 1);
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
        for (int i = 0; i < 2; ++i) {
            HashSet<Float> inner = new HashSet<Float>(3);
            for (int j = 0; j < 3; ++j) {
                inner.add((float) c);
                ++c;
            }
            in.add(inner);
        }

        Set<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeSetFloat32(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeSetFloat32(bis, "", 2);
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
        for (int i = 0; i < 10; ++i) {
            in.add((double) i);
        }

        Set<Double> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeSetFloat64(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeSetFloat64(bis, "", 1);
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
        for (int i = 0; i < 2; ++i) {
            HashSet<Double> inner = new HashSet<Double>(3);
            for (int j = 0; j < 3; ++j) {
                inner.add((double) c);
                ++c;
            }
            in.add(inner);
        }

        Set<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeSetFloat64(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeSetFloat64(bis, "", 2);
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
        for (int i = 0; i < 2; ++i) {
            if (i == 0) {
                in.add(false);
            } else {
                in.add(true);
            }
        }

        Set<Boolean> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeSetBoolean(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeSetBoolean(bis, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }

    @Test
    public <T> void deserializeMultiDimSetBooleanTest() {
        Set<HashSet<Boolean>> in = new HashSet<HashSet<Boolean>>(2);
        for (int i = 0; i < 2; ++i) {
            HashSet<Boolean> inner = new HashSet<Boolean>(3);
            for (int j = 0; j < 3; ++j) {
                inner.add(true);
            }
            in.add(inner);
        }

        Set<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeSetBoolean(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeSetBoolean(bis, "", 2);
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
        for (int i = 0; i < 10; ++i) {
            in.add(new String("Set " + i));
        }

        Set<String> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeSetString(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeSetString(bis, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }

    @Test
    public <T> void deserializeMultiDimSetStringTest() {
        Set<HashSet<String>> in = new HashSet<HashSet<String>>(2);
        for (int i = 0; i < 2; ++i) {
            HashSet<String> inner = new HashSet<String>(3);
            for (int j = 0; j < 3; ++j) {
                inner.add(new String("Set " + i));
            }
            in.add(inner);
        }

        Set<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeSetString(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeSetString(bis, "", 2);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }

    /*
     * EnumSetTest
     */

    @Test
    public <T> void deserializeSetEnumTest() {
        Set<GenericEnumeration> in = new HashSet<GenericEnumeration>();
        for(int i=0; i < 10; ++i) {
            in.add(GenericEnumeration.second_val);
        }

        Set<GenericEnumeration> out = null;
        
        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeSet(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeSet(bis, "", GenericEnumeration.class, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSetEnumTest() {
        Set<HashSet<GenericEnumeration>> in = new HashSet<HashSet<GenericEnumeration>>(2);
        for(int i=0; i < 2; ++i) {
            HashSet<GenericEnumeration> inner = new HashSet<GenericEnumeration>(3);
            for (int j=0; j < 3; ++j) {
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

        Set<T> out = null;
        
        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeSet(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeSet(bis, "", GenericEnumeration.class, 2);
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
        for (int i = 0; i < 10; ++i) {
            in.add(new GenericType(i, "Hello World " + i));
        }

        Set<GenericType> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeSet(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeSet(bis, "", GenericType.class, 1);
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
        for (int i = 0; i < 2; ++i) {
            HashSet<GenericType> inner = new HashSet<GenericType>(3);
            for (int j = 0; j < 3; ++j) {
                inner.add(new GenericType(c, "Hello World " + i + j));
                ++c;
            }
            in.add(inner);
        }

        Set<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeSet(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeSet(bis, "", GenericType.class, 2);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }

}
