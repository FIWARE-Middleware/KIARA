package org.fiware.kiara.serialization;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.fiware.kiara.serialization.impl.*;

import org.junit.Before;
import org.junit.Test;
import org.fiware.kiara.serialization.types.GenericEnumeration;
import org.fiware.kiara.serialization.types.GenericType;

public class CDRSetTest {

    private CDRSerializer ser;

    @Before
    public void init() {
        this.ser = new CDRSerializer();
    }

    /*
     * CharSequenceTest
     */
    @Test
    public void deserializeSetCharTest() {
        Set<Character> in = new HashSet<>();
        for (int i = 0; i < 10; ++i) {
            in.add(new String(i + "").charAt(0));
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<Character>> s
                = new SetAsSetSerializer<>(new BasicSerializers.CharSerializer());

        Set<Character> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));


    }

    @Test
    public void deserializeMultiDimSetCharTest() {
        int c = 0;
        Set<Set<Character>> in = new HashSet<>(2);
        for (int i = 0; i < 2; ++i) {
            HashSet<Character> inner = new HashSet<>(3);
            for (int j = 0; j < 3; ++j) {
                inner.add(new String(c + "").charAt(0));
                ++c;
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<Set<Character>>> s
                = new SetAsSetSerializer<>(new SetAsSetSerializer<>(new BasicSerializers.CharSerializer()));

        Set<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));


    }

    /*
     * ByteSequenceTest
     */
    @Test
    public void deserializeSetByteTest() {
        Set<Byte> in = new HashSet<>();
        for (int i = 0; i < 10; ++i) {
            in.add((byte) i);
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<Byte>> s
                = new SetAsSetSerializer<>(new BasicSerializers.ByteSerializer());

        Set<Byte> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));


    }

    @Test
    public void deserializeMultiDimSetByteTest() {
        int c = 0;
        Set<Set<Byte>> in = new HashSet<>(2);
        for (int i = 0; i < 2; ++i) {
            HashSet<Byte> inner = new HashSet<>(3);
            for (int j = 0; j < 3; ++j) {
                inner.add((byte) c);
                ++c;
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<Set<Byte>>> s
                = new SetAsSetSerializer<>(new SetAsSetSerializer<>(new BasicSerializers.ByteSerializer()));

        Set<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));


    }

    /*
     * I16SequenceTest
     */
    @Test
    public void deserializeSetI16Test() {
        Set<Short> in = new HashSet<>();
        for (int i = 0; i < 10; ++i) {
            in.add((short) i);
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<Short>> s
                = new SetAsSetSerializer<>(new BasicSerializers.I16Serializer());

        Set<Short> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));


    }

    @Test
    public void deserializeMultiDimSetI16Test() {
        int c = 0;
        Set<Set<Short>> in = new HashSet<>(2);
        for (int i = 0; i < 2; ++i) {
            HashSet<Short> inner = new HashSet<>(3);
            for (int j = 0; j < 3; ++j) {
                inner.add((short) c);
                ++c;
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<Set<Short>>> s
                = new SetAsSetSerializer<>(new SetAsSetSerializer<>(new BasicSerializers.I16Serializer()));

        Set<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));


    }

    /*
     * UI16SequenceTest
     */
    @Test
    public void deserializeSetUI16Test() {
        Set<Short> in = new HashSet<>();
        for (int i = 0; i < 10; ++i) {
            in.add((short) i);
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<Short>> s
                = new SetAsSetSerializer<>(new BasicSerializers.UI16Serializer());

        Set<Short> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));


    }

    @Test
    public void deserializeMultiDimSetUI16Test() {
        int c = 0;
        Set<Set<Short>> in = new HashSet<>(2);
        for (int i = 0; i < 2; ++i) {
            HashSet<Short> inner = new HashSet<>(3);
            for (int j = 0; j < 3; ++j) {
                inner.add((short) c);
                ++c;
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<Set<Short>>> s
                = new SetAsSetSerializer<>(new SetAsSetSerializer<>(new BasicSerializers.UI16Serializer()));


        Set<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));


    }

    /*
     * I32SequenceTest
     */
    @Test
    public void deserializeSetI32Test() {
        Set<Integer> in = new HashSet<>();
        for (int i = 0; i < 10; ++i) {
            in.add(i);
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<Integer>> s
                = new SetAsSetSerializer<>(new BasicSerializers.I32Serializer());

        Set<Integer> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));


    }

    @Test
    public void deserializeMultiDimSetI32Test() {
        int c = 0;
        Set<Set<Integer>> in = new HashSet<>(2);
        for (int i = 0; i < 2; ++i) {
            HashSet<Integer> inner = new HashSet<>(3);
            for (int j = 0; j < 3; ++j) {
                inner.add(c);
                ++c;
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<Set<Integer>>> s
                = new SetAsSetSerializer<>(new SetAsSetSerializer<>(new BasicSerializers.I32Serializer()));

        Set<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));


    }

    /*
     * UI32SequenceTest
     */
    @Test
    public void deserializeSetUI32Test() {
        Set<Integer> in = new HashSet<>();
        for (int i = 0; i < 10; ++i) {
            in.add(i);
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<Integer>> s
                = new SetAsSetSerializer<>(new BasicSerializers.UI32Serializer());

        Set<Integer> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));


    }

    @Test
    public void deserializeMultiDimSetUI32Test() {
        int c = 0;
        Set<Set<Integer>> in = new HashSet<>(2);
        for (int i = 0; i < 2; ++i) {
            HashSet<Integer> inner = new HashSet<>(3);
            for (int j = 0; j < 3; ++j) {
                inner.add(c);
                ++c;
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<Set<Integer>>> s
                = new SetAsSetSerializer<>(new SetAsSetSerializer<>(new BasicSerializers.UI32Serializer()));

        Set<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));


    }

    /*
     * I64SequenceTest
     */
    @Test
    public void deserializeSetI64Test() {
        Set<Long> in = new HashSet<>();
        for (int i = 0; i < 10; ++i) {
            in.add((long) i);
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<Long>> s
                = new SetAsSetSerializer<>(new BasicSerializers.I64Serializer());

        Set<Long> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));


    }

    @Test
    public void deserializeMultiDimSetI64Test() {
        int c = 0;
        Set<Set<Long>> in = new HashSet<>(2);
        for (int i = 0; i < 2; ++i) {
            HashSet<Long> inner = new HashSet<>(3);
            for (int j = 0; j < 3; ++j) {
                inner.add((long) c);
                ++c;
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<Set<Long>>> s
                = new SetAsSetSerializer<>(new SetAsSetSerializer<>(new BasicSerializers.I64Serializer()));

        Set<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));


    }

    /*
     * UI32SequenceTest
     */
    @Test
    public void deserializeSetUI64Test() {
        Set<Long> in = new HashSet<>();
        for (int i = 0; i < 10; ++i) {
            in.add((long) i);
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<Long>> s
                = new SetAsSetSerializer<>(new BasicSerializers.UI64Serializer());

        Set<Long> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));
    }

    @Test
    public void deserializeMultiDimSetUI64Test() {
        int c = 0;
        Set<Set<Long>> in = new HashSet<>(2);
        for (int i = 0; i < 2; ++i) {
            HashSet<Long> inner = new HashSet<>(3);
            for (int j = 0; j < 3; ++j) {
                inner.add((long) c);
                ++c;
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<Set<Long>>> s
                = new SetAsSetSerializer<>(new SetAsSetSerializer<>(new BasicSerializers.UI64Serializer()));

        Set<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));


    }

    /*
     * Float32SequenceTest
     */
    @Test
    public void deserializeSetFloat32Test() {
        Set<Float> in = new HashSet<>();
        for (int i = 0; i < 10; ++i) {
            in.add((float) i);
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<Float>> s
                = new SetAsSetSerializer<>(new BasicSerializers.Float32Serializer());

        Set<Float> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));


    }

    @Test
    public void deserializeMultiDimSetFloat32Test() {
        int c = 0;
        Set<Set<Float>> in = new HashSet<>(2);
        for (int i = 0; i < 2; ++i) {
            HashSet<Float> inner = new HashSet<>(3);
            for (int j = 0; j < 3; ++j) {
                inner.add((float) c);
                ++c;
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<Set<Float>>> s
                = new SetAsSetSerializer<>(new SetAsSetSerializer<>(new BasicSerializers.Float32Serializer()));

        Set<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));


    }

    /*
     * Float64SequenceTest
     */
    @Test
    public void deserializeSetFloat64Test() {
        Set<Double> in = new HashSet<>();
        for (int i = 0; i < 10; ++i) {
            in.add((double) i);
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<Double>> s
                = new SetAsSetSerializer<>(new BasicSerializers.Float64Serializer());

        Set<Double> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));


    }

    @Test
    public void deserializeMultiDimSetFloat64Test() {
        int c = 0;
        Set<Set<Double>> in = new HashSet<>(2);
        for (int i = 0; i < 2; ++i) {
            HashSet<Double> inner = new HashSet<>(3);
            for (int j = 0; j < 3; ++j) {
                inner.add((double) c);
                ++c;
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<Set<Double>>> s
                = new SetAsSetSerializer<>(new SetAsSetSerializer<>(new BasicSerializers.Float64Serializer()));

        Set<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));


    }

    /*
     * BooleanSequenceTest
     */
    @Test
    public void deserializeSetBooleanTest() {
        Set<Boolean> in = new HashSet<>();
        for (int i = 0; i < 2; ++i) {
            if (i == 0) {
                in.add(false);
            } else {
                in.add(true);
            }
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<Boolean>> s
                = new SetAsSetSerializer<>(new BasicSerializers.BooleanSerializer());

        Set<Boolean> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));


    }

    @Test
    public void deserializeMultiDimSetBooleanTest() {
        Set<Set<Boolean>> in = new HashSet<>(2);
        for (int i = 0; i < 2; ++i) {
            HashSet<Boolean> inner = new HashSet<>(3);
            for (int j = 0; j < 3; ++j) {
                inner.add(true);
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<Set<Boolean>>> s
                = new SetAsSetSerializer<>(new SetAsSetSerializer<>(new BasicSerializers.BooleanSerializer()));

        Set<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));


    }

    /*
     * StringSequenceTest
     */
    @Test
    public void deserializeSetStringTest() {
        Set<String> in = new HashSet<>();
        for (int i = 0; i < 10; ++i) {
            in.add(new String("Set " + i));
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<String>> s
                = new SetAsSetSerializer<>(new BasicSerializers.StringSerializer());

        Set<String> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));


    }

    @Test
    public void deserializeMultiDimSetStringTest() {
        Set<Set<String>> in = new HashSet<>(2);
        for (int i = 0; i < 2; ++i) {
            HashSet<String> inner = new HashSet<>(3);
            for (int j = 0; j < 3; ++j) {
                inner.add(new String("Set " + i));
            }
            in.add(inner);
        }
        org.fiware.kiara.serialization.impl.Serializer<Set<Set<String>>> s
                = new SetAsSetSerializer<>(new SetAsSetSerializer<>(new BasicSerializers.StringSerializer()));

        Set<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));


    }

    /*
     * EnumSetTest
     */

    @Test
    public void deserializeSetEnumTest() {
        Set<GenericEnumeration> in = new HashSet<>();
        for(int i=0; i < 10; ++i) {
            in.add(GenericEnumeration.second_val);
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<GenericEnumeration>> s
                = new SetAsSetSerializer<>(new EnumSerializer<>(GenericEnumeration.class));

        Set<GenericEnumeration> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));


    }

    @Test
    public void deserializeMultiDimSetEnumTest() {
        Set<Set<GenericEnumeration>> in = new HashSet<>(2);
        for(int i=0; i < 2; ++i) {
            HashSet<GenericEnumeration> inner = new HashSet<>(3);
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

        org.fiware.kiara.serialization.impl.Serializer<Set<Set<GenericEnumeration>>> s
                = new SetAsSetSerializer<>(new SetAsSetSerializer<>(new EnumSerializer<>(GenericEnumeration.class)));

        Set<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));


    }

    /*
     * StringSequenceTest
     */
    @Test
    public void deserializeSetTest() {
        Set<GenericType> in = new HashSet<>();
        for (int i = 0; i < 10; ++i) {
            in.add(new GenericType(i, "Hello World " + i));
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<GenericType>> s
                = new SetAsSetSerializer<>(new ObjectSerializer<>(GenericType.class));

        Set<GenericType> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));


    }

    @Test
    public void deserializeMultiDimSetTest() {
        int c = 0;
        Set<Set<GenericType>> in = new HashSet<>(2);
        for (int i = 0; i < 2; ++i) {
            HashSet<GenericType> inner = new HashSet<>(3);
            for (int j = 0; j < 3; ++j) {
                inner.add(new GenericType(c, "Hello World " + i + j));
                ++c;
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<Set<GenericType>>> s
                = new SetAsSetSerializer<>(new SetAsSetSerializer<>(new ObjectSerializer<>(GenericType.class)));

        Set<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));


    }

}
