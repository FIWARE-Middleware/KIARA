package org.fiware.kiara.serialization;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.fiware.kiara.serialization.impl.*;

import org.junit.Before;
import org.junit.Test;
import org.fiware.kiara.serialization.types.GenericEnumeration;
import org.fiware.kiara.serialization.types.GenericType;

public class CDRSequenceTest {

    private CDRSerializer ser;

    @Before
    public void init() {
        this.ser = new CDRSerializer();
    }

    /*
     * CharSequenceTest
     */
    @Test
    public void deserializeSequenceCharTest() {
        ArrayList<Character> in = new ArrayList<>(1);
        for (int i = 0; i < 10; ++i) {
            in.add('A');
        }

        List<Character> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            org.fiware.kiara.serialization.impl.Serializer<List<Character>> s
                    = new ListAsSequenceSerializer<>(new BasicSerializers.CharSerializer());

            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));


    }

    @Test
    public void deserializeMultiDimSequenceCharTest() {
        List<List<Character>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            List<Character> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add('A');
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<List<Character>>> s
                = new ListAsSequenceSerializer<>(new ListAsSequenceSerializer<>(new BasicSerializers.CharSerializer()));

        List<?> out = null;

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
    public void deserializeSequenceByteTest() {
        ArrayList<Byte> in = new ArrayList<>(1);
        for (int i = 0; i < 10; ++i) {
            in.add((byte) 5);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<Byte>> s
                = new ListAsSequenceSerializer<>(new BasicSerializers.ByteSerializer());

        List<?> out = null;

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
    public void deserializeMultiDimSequenceByteTest() {
        List<List<Byte>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            ArrayList<Byte> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add((byte) 5);
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<List<Byte>>> s
                = new ListAsSequenceSerializer<>(new ListAsSequenceSerializer<>(new BasicSerializers.ByteSerializer()));

        List<?> out = null;

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
    public void deserializeSequenceI16Test() {
        ArrayList<Short> in = new ArrayList<>(1);
        for (int i = 0; i < 10; ++i) {
            in.add((short) 5);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<Short>> s
                = new ListAsSequenceSerializer<>(new BasicSerializers.I16Serializer());

        List<?> out = null;

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
    public void deserializeMultiDimSequenceI16Test() {
        List<List<Short>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            ArrayList<Short> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add((short) 5);
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<List<Short>>> s
                = new ListAsSequenceSerializer<>(new ListAsSequenceSerializer<>(new BasicSerializers.I16Serializer()));

        List<?> out = null;

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
    public void deserializeSequenceUI16Test() {
        ArrayList<Short> in = new ArrayList<>(1);
        for (int i = 0; i < 10; ++i) {
            in.add((short) 5);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<Short>> s
                = new ListAsSequenceSerializer<>(new BasicSerializers.UI16Serializer());

        List<?> out = null;

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
    public void deserializeMultiDimSequenceUI16Test() {
        List<List<Short>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            ArrayList<Short> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add((short) 5);
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<List<Short>>> s
                = new ListAsSequenceSerializer<>(new ListAsSequenceSerializer<>(new BasicSerializers.UI16Serializer()));

        List<?> out = null;

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
    public void deserializeSequenceI32Test() {
        ArrayList<Integer> in = new ArrayList<>(1);
        for (int i = 0; i < 10; ++i) {
            in.add((int) 5);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<Integer>> s
                = new ListAsSequenceSerializer<>(new BasicSerializers.I32Serializer());

        List<?> out = null;

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
    public void deserializeMultiDimSequenceI32Test() {
        List<List<Integer>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            ArrayList<Integer> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add((int) 5);
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<List<Integer>>> s
                = new ListAsSequenceSerializer<>(new ListAsSequenceSerializer<>(new BasicSerializers.I32Serializer()));

        List<?> out = null;

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
    public void deserializeSequenceUI32Test() {
        ArrayList<Integer> in = new ArrayList<>(1);
        for (int i = 0; i < 10; ++i) {
            in.add((int) 5);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<Integer>> s
                = new ListAsSequenceSerializer<>(new BasicSerializers.UI32Serializer());

        List<?> out = null;

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
    public void deserializeMultiDimSequenceUI32Test() {
        List<List<Integer>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            ArrayList<Integer> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add((int) 5);
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<List<Integer>>> s
                = new ListAsSequenceSerializer<>(new ListAsSequenceSerializer<>(new BasicSerializers.UI32Serializer()));

        List<?> out = null;

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
    public void deserializeSequenceI64Test() {
        ArrayList<Long> in = new ArrayList<>(1);
        for (int i = 0; i < 10; ++i) {
            in.add((long) 5);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<Long>> s
                = new ListAsSequenceSerializer<>(new BasicSerializers.I64Serializer());


        List<?> out = null;

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
    public void deserializeMultiDimSequenceI64Test() {
        List<List<Long>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            ArrayList<Long> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add((long) 5);
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<List<Long>>> s
                = new ListAsSequenceSerializer<>(new ListAsSequenceSerializer<>(new BasicSerializers.I64Serializer()));

        List<?> out = null;

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
    public void deserializeSequenceUI64Test() {
        ArrayList<Long> in = new ArrayList<>(1);
        for (int i = 0; i < 10; ++i) {
            in.add((long) 5);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<Long>> s
                = new ListAsSequenceSerializer<>(new BasicSerializers.UI64Serializer());

        List<?> out = null;

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
    public void deserializeMultiDimSequenceUI64Test() {
        List<List<Long>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            ArrayList<Long> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add((long) 5);
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<List<Long>>> s
                = new ListAsSequenceSerializer<>(new ListAsSequenceSerializer<>(new BasicSerializers.UI64Serializer()));


        List<?> out = null;

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
    public void deserializeSequenceFloat32Test() {
        ArrayList<Float> in = new ArrayList<>(1);
        for (int i = 0; i < 10; ++i) {
            in.add((float) 5.5);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<Float>> s
                = new ListAsSequenceSerializer<>(new BasicSerializers.Float32Serializer());

        List<?> out = null;

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
    public void deserializeMultiDimSequenceFloat32Test() {
        List<List<Float>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            ArrayList<Float> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add((float) 5.5);
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<List<Float>>> s
                = new ListAsSequenceSerializer<>(new ListAsSequenceSerializer<>(new BasicSerializers.Float32Serializer()));

        List<?> out = null;

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
    public void deserializeSequenceFloat64Test() {
        ArrayList<Double> in = new ArrayList<>(1);
        for (int i = 0; i < 10; ++i) {
            in.add((double) 5.5);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<Double>> s
                = new ListAsSequenceSerializer<>(new BasicSerializers.Float64Serializer());

        List<?> out = null;

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
    public void deserializeMultiDimSequenceFloat64Test() {
        List<List<Double>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            ArrayList<Double> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add((double) 5.5);
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<List<Double>>> s
                = new ListAsSequenceSerializer<>(new ListAsSequenceSerializer<>(new BasicSerializers.Float64Serializer()));


        List<?> out = null;

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
    public void deserializeSequenceBooleanTest() {
        ArrayList<Boolean> in = new ArrayList<>(1);
        for (int i = 0; i < 10; ++i) {
            if (i % 2 == 0) {
                in.add((boolean) true);
            } else {
                in.add((boolean) false);
            }
        }

        org.fiware.kiara.serialization.impl.Serializer<List<Boolean>> s
                = new ListAsSequenceSerializer<>(new BasicSerializers.BooleanSerializer());

        List<?> out = null;

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
    public void deserializeMultiDimSequenceBooleanTest() {
        List<List<Boolean>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            ArrayList<Boolean> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                if (i % 2 == 0) {
                    inner.add((boolean) true);
                } else {
                    inner.add((boolean) false);
                }
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<List<Boolean>>> s
                = new ListAsSequenceSerializer<>(new ListAsSequenceSerializer<>(new BasicSerializers.BooleanSerializer()));

        List<?> out = null;

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
    public void deserializeSequenceStringTest() {
        ArrayList<String> in = new ArrayList<>(1);
        for (int i = 0; i < 10; ++i) {
            in.add((String) "Hello World " + i);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<String>> s
                = new ListAsSequenceSerializer<>(new BasicSerializers.StringSerializer());

        List<?> out = null;

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
    public void deserializeMultiDimSequenceStringTest() {
        List<List<String>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            ArrayList<String> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add((String) "Hello World " + j);
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<List<String>>> s
                = new ListAsSequenceSerializer<>(new ListAsSequenceSerializer<>(new BasicSerializers.StringSerializer()));

        List<?> out = null;

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
     * EnumSequenceTest
     */

    @Test
    public void deserializeSequenceEnumTest() {
        ArrayList<GenericEnumeration> in = new ArrayList<>(1);
        for(int i=0; i < 10; ++i) {
            in.add(GenericEnumeration.second_val);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<GenericEnumeration>> s
                = new ListAsSequenceSerializer<>(new EnumSerializer<>(GenericEnumeration.class));

        List<?> out = null;

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
    public void deserializeMultiDimSequenceEnumTest() {
        List<List<GenericEnumeration>> in = new ArrayList<>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<GenericEnumeration> inner = new ArrayList<>(5);
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

        org.fiware.kiara.serialization.impl.Serializer<List<List<GenericEnumeration>>> s
                = new ListAsSequenceSerializer<>(new ListAsSequenceSerializer<>(new EnumSerializer<>(GenericEnumeration.class)));

        List<?> out = null;

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
    public void deserializeSequenceTest() {
        ArrayList<GenericType> in = new ArrayList<>(1);
        for (int i = 0; i < 10; ++i) {
            in.add(new GenericType(i, "Hello World " + i));
        }

        org.fiware.kiara.serialization.impl.Serializer<List<GenericType>> s
                = new ListAsSequenceSerializer<>(new ObjectSerializer<>(GenericType.class));

        List<?> out = null;

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
    public void deserializeMultiDimSequenceTest() {
        List<List<GenericType>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            ArrayList<GenericType> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add(new GenericType(j, "Hello World " + j));
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<List<GenericType>>> s
                = new ListAsSequenceSerializer<>(new ListAsSequenceSerializer<>(new ObjectSerializer<>(GenericType.class)));

        List<?> out = null;

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

}
