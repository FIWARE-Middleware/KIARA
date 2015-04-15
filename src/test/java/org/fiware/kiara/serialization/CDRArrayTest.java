package org.fiware.kiara.serialization;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.fiware.kiara.serialization.impl.BasicSerializers;

import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.junit.Before;
import org.junit.Test;
import org.fiware.kiara.serialization.impl.CDRSerializer;
import org.fiware.kiara.serialization.impl.EnumSerializer;
import org.fiware.kiara.serialization.impl.ListAsArraySerializer;
import org.fiware.kiara.serialization.impl.ObjectSerializer;
import org.fiware.kiara.serialization.types.GenericEnumeration;
import org.fiware.kiara.serialization.types.GenericType;

public class CDRArrayTest {

    private CDRSerializer ser;

    @Before
    public void init() {
        this.ser = new CDRSerializer();
    }

    /*
     * SerializeArrayChar
     */
    @Test
    public void serializeArrayCharTest() {
        List<Character> in = new ArrayList<>(1);
        in.add('a');

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            org.fiware.kiara.serialization.impl.Serializer<List<Character>> s
                    = new ListAsArraySerializer<>(1, new BasicSerializers.CharSerializer());

            s.write(ser, bos, "", in);
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

            org.fiware.kiara.serialization.impl.Serializer<List<Byte>> s
                    = new ListAsArraySerializer<>(1, new BasicSerializers.ByteSerializer());

            s.write(ser, bos, "", in);
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

            org.fiware.kiara.serialization.impl.Serializer<List<Short>> s
                    = new ListAsArraySerializer<>(1, new BasicSerializers.I16Serializer());

            s.write(ser, bos, "", in);
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

            org.fiware.kiara.serialization.impl.Serializer<List<Short>> s
                    = new ListAsArraySerializer<>(1, new BasicSerializers.UI16Serializer());

            s.write(ser, bos, "", in);
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

            org.fiware.kiara.serialization.impl.Serializer<List<Integer>> s
                    = new ListAsArraySerializer<>(1, new BasicSerializers.I32Serializer());

            s.write(ser, bos, "", in);
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

            org.fiware.kiara.serialization.impl.Serializer<List<Integer>> s
                    = new ListAsArraySerializer<>(1, new BasicSerializers.UI32Serializer());

            s.write(ser, bos, "", in);
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

            org.fiware.kiara.serialization.impl.Serializer<List<Long>> s
                    = new ListAsArraySerializer<>(1, new BasicSerializers.I64Serializer());

            s.write(ser, bos, "", in);
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

            org.fiware.kiara.serialization.impl.Serializer<List<Long>> s
                    = new ListAsArraySerializer<>(1, new BasicSerializers.UI64Serializer());

            s.write(ser, bos, "", in);
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

            org.fiware.kiara.serialization.impl.Serializer<List<Float>> s
                    = new ListAsArraySerializer<>(1, new BasicSerializers.Float32Serializer());

            s.write(ser, bos, "", in);
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

            org.fiware.kiara.serialization.impl.Serializer<List<Double>> s
                = new ListAsArraySerializer<>(1, new BasicSerializers.Float64Serializer());

            s.write(ser, bos, "", in);
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

            org.fiware.kiara.serialization.impl.Serializer<List<Boolean>> s
                = new ListAsArraySerializer<>(1, new BasicSerializers.BooleanSerializer());

            s.write(ser, bos, "", in);
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

            org.fiware.kiara.serialization.impl.Serializer<List<String>> s
                = new ListAsArraySerializer<>(1, new BasicSerializers.StringSerializer());

            s.write(ser, bos, "", in);
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

            org.fiware.kiara.serialization.impl.Serializer<List<GenericEnumeration>> s
                = new ListAsArraySerializer<>(1, new EnumSerializer(GenericEnumeration.class));

            s.write(ser, bos, "", in);
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

            org.fiware.kiara.serialization.impl.Serializer<List<GenericType>> s
                = new ListAsArraySerializer<>(1, new ObjectSerializer(GenericType.class));

            s.write(ser, bos, "", in);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

    }

    /*
     * SerializeArrayChar
     */
    @Test
    public void deserializeUniDimArrayCharTest() {
        List<Character> in = new ArrayList<>(1);
        for (int i = 0; i < 1; ++i) {
            in.add('A');
        }

        org.fiware.kiara.serialization.impl.Serializer<List<Character>> s
            = new ListAsArraySerializer<>(1, new BasicSerializers.CharSerializer());


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
    public void deserializeMultiDimArrayCharTest() {
        List<List<Character>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            List<Character> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add('A');
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<List<Character>>> s1
            = new ListAsArraySerializer<>(3, new ListAsArraySerializer<>(5, new BasicSerializers.CharSerializer()));

        List<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s1.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s1.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    /*
     * SerializeArrayByte
     */
    @Test
    public void deserializeUniDimArrayByteTest() {
        List<Byte> in = new ArrayList<>(1);
        for (int i = 0; i < 1; ++i) {
            in.add((byte) 55);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<Byte>> s1
            = new ListAsArraySerializer<>(1, new BasicSerializers.ByteSerializer());


        List<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s1.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s1.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    @Test
    public void deserializeMultiDimArrayByteTest() {
        List<List<Byte>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            ArrayList<Byte> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add((byte) 55);
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<List<Byte>>> s1
            = new ListAsArraySerializer<>(3, new ListAsArraySerializer<>(5, new BasicSerializers.ByteSerializer()));

        List<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s1.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s1.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    /*
     * SerializeArrayI16
     */
    @Test
    public void deserializeUniDimArrayI16Test() {
        List<Short> in = new ArrayList<>(1);
        for (int i = 0; i < 1; ++i) {
            in.add((short) 55);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<Short>> s1
            = new ListAsArraySerializer<>(1, new BasicSerializers.I16Serializer());

        List<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s1.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s1.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    @Test
    public void deserializeMultiDimArrayI16Test() {
        List<List<Short>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            List<Short> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add((short) 55);
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<List<Short>>> s1
            = new ListAsArraySerializer<>(3, new ListAsArraySerializer<>(5, new BasicSerializers.I16Serializer()));

        List<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s1.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s1.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    /*
     * SerializeArrayUI16
     */
    @Test
    public void deserializeUniDimArrayUI16Test() {
        List<Short> in = new ArrayList<>(1);
        for (int i = 0; i < 1; ++i) {
            in.add((short) 55);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<Short>> s1
            = new ListAsArraySerializer<>(1, new BasicSerializers.UI16Serializer());

        List<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s1.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s1.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    @Test
    public void deserializeMultiDimArrayUI16Test() {
        List<List<Short>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            List<Short> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add((short) 55);
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<List<Short>>> s1
            = new ListAsArraySerializer<>(3, new ListAsArraySerializer<>(5, new BasicSerializers.UI16Serializer()));

        List<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s1.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s1.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    /*
     * SerializeArrayI32
     */
    @Test
    public void deserializeUniDimArrayI32Test() {
        List<Integer> in = new ArrayList<>(1);
        for (int i = 0; i < 1; ++i) {
            in.add((int) 55);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<Integer>> s1
            = new ListAsArraySerializer<>(1, new BasicSerializers.I32Serializer());

        List<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s1.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s1.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    @Test
    public void deserializeMultiDimArrayI32Test() {
        List<List<Integer>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            List<Integer> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add((int) 55);
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<List<Integer>>> s1
            = new ListAsArraySerializer<>(3, new ListAsArraySerializer<>(5, new BasicSerializers.I32Serializer()));

        List<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s1.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s1.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    /*
     * SerializeArrayUI32
     */
    @Test
    public void deserializeUniDimArrayUI32Test() {
        List<Integer> in = new ArrayList<>(1);
        for (int i = 0; i < 1; ++i) {
            in.add((int) 55);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<Integer>> s1
            = new ListAsArraySerializer<>(1, new BasicSerializers.UI32Serializer());

        List<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s1.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s1.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    @Test
    public void deserializeMultiDimArrayUI32Test() {
        List<List<Integer>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            ArrayList<Integer> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add((int) 55);
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<List<Integer>>> s1
            = new ListAsArraySerializer<>(3, new ListAsArraySerializer<>(5, new BasicSerializers.UI32Serializer()));

        List<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s1.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s1.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    /*
     * SerializeArrayI64
     */
    @Test
    public void deserializeUniDimArrayI64Test() {
        ArrayList<Long> in = new ArrayList<>(1);
        for (int i = 0; i < 1; ++i) {
            in.add((long) 55);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<Long>> s1
            = new ListAsArraySerializer<>(1, new BasicSerializers.I64Serializer());

        List<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s1.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s1.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    @Test
    public void deserializeMultiDimArrayI64Test() {
        List<List<Long>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            List<Long> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add((long) 55);
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<List<Long>>> s1
            = new ListAsArraySerializer<>(3, new ListAsArraySerializer<>(5, new BasicSerializers.I64Serializer()));

        List<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s1.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s1.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    /*
     * SerializeArrayUI64
     */
    @Test
    public void deserializeUniDimArrayUI64Test() {
        List<Long> in = new ArrayList<>(1);
        for (int i = 0; i < 1; ++i) {
            in.add((long) 55);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<Long>> s1
            = new ListAsArraySerializer<>(1, new BasicSerializers.UI64Serializer());

        List<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s1.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s1.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    @Test
    public void deserializeMultiDimArrayUI64Test() {
        List<List<Long>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            List<Long> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add((long) 55);
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<List<Long>>> s1
            = new ListAsArraySerializer<>(3, new ListAsArraySerializer<>(5, new BasicSerializers.UI64Serializer()));

        List<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s1.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s1.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    /*
     * SerializeArrayFloat32
     */
    @Test
    public void deserializeUniDimArrayFloat32Test() {
        List<Float> in = new ArrayList<>(1);
        for (int i = 0; i < 1; ++i) {
            in.add((float) 55);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<Float>> s1
            = new ListAsArraySerializer<>(1, new BasicSerializers.Float32Serializer());

        List<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s1.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s1.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    @Test
    public void deserializeMultiDimArrayFloat32Test() {
        List<List<Float>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            List<Float> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add((float) 55);
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<List<Float>>> s1
            = new ListAsArraySerializer<>(3, new ListAsArraySerializer<>(5, new BasicSerializers.Float32Serializer()));

        List<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s1.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s1.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    /*
     * SerializeArrayFloat64
     */
    @Test
    public void deserializeUniDimArrayFloat64Test() {
        List<Double> in = new ArrayList<>(1);
        for (int i = 0; i < 1; ++i) {
            in.add(55.0);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<Double>> s1
                = new ListAsArraySerializer<>(1, new BasicSerializers.Float64Serializer());

        List<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s1.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s1.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    @Test
    public void deserializeMultiDimArrayFloat64Test() {
        List<List<Double>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            List<Double> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add((double) 55);
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<List<Double>>> s1
                = new ListAsArraySerializer<>(3, new ListAsArraySerializer<>(5, new BasicSerializers.Float64Serializer()));

        List<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s1.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s1.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    /*
     * SerializeArrayBoolean
     */
    @Test
    public void deserializeUniDimArrayBooleanTest() {
        List<Boolean> in = new ArrayList<>(1);
        for (int i = 0; i < 1; ++i) {
            in.add((boolean) true);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<Boolean>> s1
                = new ListAsArraySerializer<>(1, new BasicSerializers.BooleanSerializer());

        List<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s1.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s1.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    @Test
    public void deserializeMultiDimArrayBooleanTest() {
        List<List<Boolean>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            List<Boolean> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add((boolean) true);
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<List<Boolean>>> s1
                = new ListAsArraySerializer<>(3, new ListAsArraySerializer<>(5, new BasicSerializers.BooleanSerializer()));

        List<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s1.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s1.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    /*
     * SerializeArrayString
     */
    @Test
    public void deserializeUniDimArrayStringTest() {
        List<String> in = new ArrayList<>(1);
        for (int i = 0; i < 1; ++i) {
            in.add("Hello World");
        }

        org.fiware.kiara.serialization.impl.Serializer<List<String>> s1
                = new ListAsArraySerializer<>(1, new BasicSerializers.StringSerializer());

        List<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s1.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s1.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    @Test
    public void deserializeMultiDimArrayStringTest() {
        List<List<String>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            ArrayList<String> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add("Hello World");
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<List<String>>> s1
                = new ListAsArraySerializer<>(3, new ListAsArraySerializer<>(5, new BasicSerializers.StringSerializer()));

        List<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s1.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s1.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    /*
     * SerializeArrayString
     */
    @Test
    public void deserializeUniDimArrayEnumTest() {
        List<GenericEnumeration> in = new ArrayList<>(1);
        for (int i = 0; i < 1; ++i) {
            in.add(GenericEnumeration.second_val);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<GenericEnumeration>> s1
                = new ListAsArraySerializer<>(1, new EnumSerializer<>(GenericEnumeration.class));

        List<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s1.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s1.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    @Test
    public void deserializeMultiDimArrayEnumTest() {
        List<List<GenericEnumeration>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            List<GenericEnumeration> inner = new ArrayList<>(5);
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

        org.fiware.kiara.serialization.impl.Serializer<List<List<GenericEnumeration>>> s1
                = new ListAsArraySerializer<>(3, new ListAsArraySerializer<>(5, new EnumSerializer<>(GenericEnumeration.class)));

        List<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s1.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s1.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    /*
     * SerializeArray
     */
    @Test
    public void deserializeUniDimArrayTest() {
        List<GenericType> in = new ArrayList<>(1);
        for (int i = 0; i < 1; ++i) {
            in.add(new GenericType(i, "HelloWorld"));
        }

        org.fiware.kiara.serialization.impl.Serializer<List<GenericType>> s1
                = new ListAsArraySerializer<>(1, new ObjectSerializer<>(GenericType.class));

        List<?> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s1.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s1.read(ser, bis, ""); //ser.deserializeArray(bis, "", GenericType.class, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

    @Test
    public void deserializeMultiDimArrayTest() {
        List<List<GenericType>> in = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            ArrayList<GenericType> inner = new ArrayList<>(5);
            for (int j = 0; j < 5; ++j) {
                inner.add(new GenericType(j, "HelloWorld"));
            }
            in.add(inner);
        }

        List<?> out = null;

        org.fiware.kiara.serialization.impl.Serializer<List<List<GenericType>>> s1
                = new ListAsArraySerializer<>(3, new ListAsArraySerializer<>(5, new ObjectSerializer<>(GenericType.class)));

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s1.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s1.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

    }

}
