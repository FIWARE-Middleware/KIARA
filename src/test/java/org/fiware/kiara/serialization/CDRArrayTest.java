package org.fiware.kiara.serialization;

import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.junit.Before;
import org.junit.Test;
import org.fiware.kiara.serialization.impl.CDRSerializer;
import org.fiware.kiara.serialization.types.GenericEnumeration;
import org.fiware.kiara.serialization.types.GenericType;

public class CDRArrayTest {

    private CDRSerializer ser;

    @Before
    public void init() {
        this.ser = new CDRSerializer();
        ByteBuffer buffer = ByteBuffer.allocate(500);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    /*
     * SerializeArrayChar
     */
    @Test
    public void serializeArrayCharTest() {
        ArrayList<Character> in = new ArrayList<>(1);
        in.add('a');

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayChar(bos, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

    }

    /*
     * SerializeArrayByte
     */
    @Test
    public void serializeArrayByteTest() {
        List<Byte> in = new ArrayList<>();
        in.add((byte) 5);

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayByte(bos, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

    }

    /*
     * SerializeArrayI16
     */
    @Test
    public void serializeArrayI16Test() {
        List<Short> in = new ArrayList<>();
        in.add((short) 5);

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayI16(bos, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

    }

    /*
     * SerializeArrayUI16
     */
    @Test
    public void serializeArrayUI16Test() {
        List<Short> in = new ArrayList<>();
        in.add((short) 5);

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayUI16(bos, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

    }

    /*
     * SerializeArrayI32
     */
    @Test
    public void serializeArrayI32Test() {
        List<Integer> in = new ArrayList<>();
        in.add((int) 5);

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayI32(bos, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

    }

    /*
     * SerializeArrayUI32
     */
    @Test
    public void serializeArrayUI32Test() {
        List<Integer> in = new ArrayList<>();
        in.add((int) 5);

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayUI32(bos, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

    }

    /*
     * SerializeArrayI64
     */
    @Test
    public void serializeArrayI64Test() {
        List<Long> in = new ArrayList<>();
        in.add((long) 5);

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayI64(bos, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

    }

    /*
     * SerializeArrayUI64
     */
    @Test
    public void serializeArrayUI64Test() {
        List<Long> in = new ArrayList<>();
        in.add((long) 5);

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayUI64(bos, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

    }

    /*
     * SerializeArrayFloat32
     */
    @Test
    public void serializeArrayFloat32Test() {
        List<Float> in = new ArrayList<>();
        in.add((float) 5.0);

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayFloat32(bos, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

    }

    /*
     * SerializeArrayFloat64
     */
    @Test
    public void serializeArrayFloat64Test() {
        List<Double> in = new ArrayList<>();
        in.add((double) 5.0);

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayFloat64(bos, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

    }

    /*
     * SerializeArrayBoolean
     */
    @Test
    public void serializeArrayBooleanTest() {
        List<Boolean> in = new ArrayList<>();
        in.add(true);

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayBoolean(bos, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

    }

    /*
     * SerializeArrayString
     */
    @Test
    public void serializeArrayStringTest() {
        List<String> in = new ArrayList<>();
        in.add("one");

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayString(bos, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

    }

    /*
     * SerializeArrayEnum
     */
    @Test
    public void serializeArrayEnumTest() {
        List<GenericEnumeration> in = new ArrayList<>();
        GenericEnumeration content = GenericEnumeration.second_val;
        in.add(content);

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArray(bos, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

    }

    /*
     * Array of generic types
     */
    @Test
    public void serializeArrayTest() {
        List<GenericType> in = new ArrayList<>();
        in.add(new GenericType(1, "one"));

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArray(bos, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

    }

    /*
     * SerializeArrayChar
     */
    @Test
    public <T> void deserializeUniDimArrayCharTest() {
        ArrayList<Character> in = new ArrayList<>(1);
        for (int i = 0; i < 1; ++i) {
            in.add('A');
        }

        List<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayChar(bos, "", in, 1);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeArrayChar(bis, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    @Test
    public <T> void deserializeMultiDimArrayCharTest() {
        List<ArrayList<Character>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            ArrayList<Character> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add('A');
            }
            in.add(inner);
        }

        List<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayChar(bos, "", in, 3, 5);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeArrayChar(bis, "", 3, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    /*
     * SerializeArrayByte
     */
    @Test
    public <T> void deserializeUniDimArrayByteTest() {
        ArrayList<Byte> in = new ArrayList<>(1);
        for (int i = 0; i < 1; ++i) {
            in.add((byte) 55);
        }

        List<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayByte(bos, "", in, 1);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeArrayByte(bis, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    @Test
    public <T> void deserializeMultiDimArrayByteTest() {
        List<ArrayList<Byte>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            ArrayList<Byte> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add((byte) 55);
            }
            in.add(inner);
        }

        List<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayByte(bos, "", in, 3, 5);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeArrayByte(bis, "", 3, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    /*
     * SerializeArrayI16
     */
    @Test
    public <T> void deserializeUniDimArrayI16Test() {
        ArrayList<Short> in = new ArrayList<>(1);
        for (int i = 0; i < 1; ++i) {
            in.add((short) 55);
        }

        List<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayI16(bos, "", in, 1);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeArrayI16(bis, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    @Test
    public <T> void deserializeMultiDimArrayI16Test() {
        List<ArrayList<Short>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            ArrayList<Short> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add((short) 55);
            }
            in.add(inner);
        }

        List<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayI16(bos, "", in, 3, 5);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeArrayI16(bis, "", 3, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    /*
     * SerializeArrayUI16
     */
    @Test
    public <T> void deserializeUniDimArraUyI16Test() {
        ArrayList<Short> in = new ArrayList<>(1);
        for (int i = 0; i < 1; ++i) {
            in.add((short) 55);
        }

        List<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayUI16(bos, "", in, 1);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeArrayUI16(bis, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    @Test
    public <T> void deserializeMultiDimArrayUI16Test() {
        List<ArrayList<Short>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            ArrayList<Short> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add((short) 55);
            }
            in.add(inner);
        }

        List<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayUI16(bos, "", in, 3, 5);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeArrayUI16(bis, "", 3, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    /*
     * SerializeArrayI32
     */
    @Test
    public <T> void deserializeUniDimArrayI32Test() {
        ArrayList<Integer> in = new ArrayList<>(1);
        for (int i = 0; i < 1; ++i) {
            in.add((int) 55);
        }

        List<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayI32(bos, "", in, 1);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeArrayI32(bis, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    @Test
    public <T> void deserializeMultiDimArrayI32Test() {
        List<ArrayList<Integer>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            ArrayList<Integer> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add((int) 55);
            }
            in.add(inner);
        }

        List<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayI32(bos, "", in, 3, 5);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeArrayI32(bis, "", 3, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    /*
     * SerializeArrayUI64
     */
    @Test
    public <T> void deserializeUniDimArraUyI32Test() {
        ArrayList<Integer> in = new ArrayList<>(1);
        for (int i = 0; i < 1; ++i) {
            in.add((int) 55);
        }

        List<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayUI32(bos, "", in, 1);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeArrayUI32(bis, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    @Test
    public <T> void deserializeMultiDimArrayUI32Test() {
        List<ArrayList<Integer>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            ArrayList<Integer> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add((int) 55);
            }
            in.add(inner);
        }

        List<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayUI32(bos, "", in, 3, 5);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeArrayUI32(bis, "", 3, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    /*
     * SerializeArrayI64
     */
    @Test
    public <T> void deserializeUniDimArrayI64Test() {
        ArrayList<Long> in = new ArrayList<>(1);
        for (int i = 0; i < 1; ++i) {
            in.add((long) 55);
        }

        List<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayI64(bos, "", in, 1);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeArrayI64(bis, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    @Test
    public <T> void deserializeMultiDimArrayI64Test() {
        List<ArrayList<Long>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            ArrayList<Long> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add((long) 55);
            }
            in.add(inner);
        }

        List<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayI64(bos, "", in, 3, 5);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeArrayI64(bis, "", 3, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    /*
     * SerializeArrayUI64
     */
    @Test
    public <T> void deserializeUniDimArraUyI64Test() {
        ArrayList<Long> in = new ArrayList<>(1);
        for (int i = 0; i < 1; ++i) {
            in.add((long) 55);
        }

        List<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayUI64(bos, "", in, 1);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeArrayUI64(bis, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    @Test
    public <T> void deserializeMultiDimArrayUI64Test() {
        List<ArrayList<Long>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            ArrayList<Long> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add((long) 55);
            }
            in.add(inner);
        }

        List<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayUI64(bos, "", in, 3, 5);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeArrayUI64(bis, "", 3, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    /*
     * SerializeArrayFloat32
     */
    @Test
    public <T> void deserializeUniDimArrayFloat32Test() {
        ArrayList<Float> in = new ArrayList<>(1);
        for (int i = 0; i < 1; ++i) {
            in.add((float) 55);
        }

        List<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayFloat32(bos, "", in, 1);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeArrayFloat32(bis, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    @Test
    public <T> void deserializeMultiDimArrayFloat32Test() {
        List<ArrayList<Float>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            ArrayList<Float> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add((float) 55);
            }
            in.add(inner);
        }

        List<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayFloat32(bos, "", in, 3, 5);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeArrayFloat32(bis, "", 3, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    /*
     * SerializeArrayFloat64
     */
    @Test
    public <T> void deserializeUniDimArrayFloat64Test() {
        ArrayList<Double> in = new ArrayList<>(1);
        for (int i = 0; i < 1; ++i) {
            in.add((double) 55);
        }

        List<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayFloat64(bos, "", in, 1);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeArrayFloat64(bis, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    @Test
    public <T> void deserializeMultiDimArrayFloat64Test() {
        List<ArrayList<Double>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            ArrayList<Double> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add((double) 55);
            }
            in.add(inner);
        }

        List<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayFloat64(bos, "", in, 3, 5);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeArrayFloat64(bis, "", 3, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    /*
     * SerializeArrayBoolean
     */
    @Test
    public <T> void deserializeUniDimArrayBooleanTest() {
        ArrayList<Boolean> in = new ArrayList<>(1);
        for (int i = 0; i < 1; ++i) {
            in.add((boolean) true);
        }

        List<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayBoolean(bos, "", in, 1);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeArrayBoolean(bis, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    @Test
    public <T> void deserializeMultiDimArrayBooleanTest() {
        List<ArrayList<Boolean>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            ArrayList<Boolean> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add((boolean) true);
            }
            in.add(inner);
        }

        List<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayBoolean(bos, "", in, 3, 5);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeArrayBoolean(bis, "", 3, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    /*
     * SerializeArrayString
     */
    @Test
    public <T> void deserializeUniDimArrayStringTest() {
        ArrayList<String> in = new ArrayList<>(1);
        for (int i = 0; i < 1; ++i) {
            in.add((String) "Hello World");
        }

        List<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayString(bos, "", in, 1);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeArrayString(bis, "", 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    @Test
    public <T> void deserializeMultiDimArrayStringTest() {
        List<ArrayList<String>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            ArrayList<String> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add((String) "Hello World");
            }
            in.add(inner);
        }

        List<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArrayString(bos, "", in, 3, 5);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeArrayString(bis, "", 3, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    /*
     * SerializeArrayString
     */
    @Test
    public <T> void deserializeUniDimArrayEnumTest() {
        ArrayList<GenericEnumeration> in = new ArrayList<>(1);
        for (int i = 0; i < 1; ++i) {
            in.add(GenericEnumeration.second_val);
        }

        List<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArray(bos, "", in, 1);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeArray(bis, "", GenericEnumeration.class, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    @Test
    public <T> void deserializeMultiDimArrayEnumTest() {
        List<ArrayList<GenericEnumeration>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            ArrayList<GenericEnumeration> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
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
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArray(bos, "", in, 3, 5);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeArray(bis, "", GenericEnumeration.class, 3, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    /*
     * SerializeArray
     */
    @SuppressWarnings("unchecked")
    @Test
    public <T> void deserializeUniDimArrayTest() {
        ArrayList<GenericType> in = new ArrayList<>(1);
        for (int i = 0; i < 1; ++i) {
            in.add(new GenericType(i, "HelloWorld"));
        }

        List<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArray(bos, "", in, 1);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = (List<T>) ser.deserializeArray(bis, "", GenericType.class, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    @SuppressWarnings("unchecked")
    @Test
    public <T> void deserializeMultiDimArrayTest() {
        List<ArrayList<GenericType>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            ArrayList<GenericType> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add(new GenericType(j, "HelloWorld"));
            }
            in.add(inner);
        }

        List<T> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeArray(bos, "", in, 3, 5);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = (List<T>) ser.deserializeArray(bis, "", GenericType.class, 3, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

}
