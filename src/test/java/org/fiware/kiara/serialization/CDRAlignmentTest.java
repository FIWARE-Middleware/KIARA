package org.fiware.kiara.serialization;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.fiware.kiara.serialization.impl.*;

import org.fiware.kiara.serialization.impl.Serializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.fiware.kiara.serialization.types.GenericType;

public class CDRAlignmentTest {

    private SerializerImpl ser;

    @Before
    public void init() {
        this.ser = new CDRSerializer();
    }

    @After
    public void detach() {
    }

    /*
     * CharAlignTest
     */
    @Test
    public void CharAlignSerializeTest() {
        char in = 'w';
        char i = 'f';

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeChar(bos, "MyChar", in);
            ser.serializeChar(bos, "MyShort", i);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);
    }

    @Test
    public void CharAlignDeserializeTest() {
        char cin = 'w', cout = '0';
        char iin = 'f', iout = '0';

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeChar(bos, "", cin);
            ser.serializeChar(bos, "", iin);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            cout = ser.deserializeChar(bis, "");
            iout = ser.deserializeChar(bis, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue((cin == cout) && (iin == iout));


    }

    /*
     * ByteAlignTest
     */
    @Test
    public void ByteAlignSerializeTest() {
        char in = 'w';
        byte i = 5;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeChar(bos, "MyChar", in);
            ser.serializeByte(bos, "MyShort", i);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);


    }

    @Test
    public void ByteAlignDeserializeTest() {
        char cin = 'w', cout = '0';
        byte iin = 5, iout = 0;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            ser.serializeChar(bos, "", cin);
            ser.serializeByte(bos, "", iin);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            cout = ser.deserializeChar(bis, "");
            iout = ser.deserializeByte(bis, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue((cin == cout) && (iin == iout));


    }

    /*
     * I16AlignTest
     */
    @Test
    public void I16AlignSerializeTest() {
        char in = 'w';
        short i = 55;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            ser.serializeChar(bos, "MyChar", in);
            ser.serializeI16(bos, "MyShort", i);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);


    }

    @Test
    public void I16AlignDeserializeTest() {
        char cin = 'w', cout = '0';
        short iin = 5, iout = 0;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            ser.serializeChar(bos, "", cin);
            ser.serializeI16(bos, "", iin);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            cout = ser.deserializeChar(bis, "");
            iout = ser.deserializeI16(bis, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue((cin == cout) && (iin == iout));


    }

    /*
     * UI16AlignTest
     */
    @Test
    public void UI16AlignSerializeTest() {
        char in = 'w';
        short i = 55;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            ser.serializeChar(bos, "MyChar", in);
            ser.serializeUI16(bos, "MyInt", i);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);


    }

    @Test
    public void UI16AlignDeserializeTest() {
        char cin = 'w', cout = '0';
        short iin = 5, iout = 0;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            ser.serializeChar(bos, "", cin);
            ser.serializeUI16(bos, "", iin);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            cout = ser.deserializeChar(bis, "");
            iout = ser.deserializeUI16(bis, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue((cin == cout) && (iin == iout));


    }

    /*
     * I32AlignTest
     */
    @Test
    public void I32AlignSerializeTest() {
        char in = 'w';
        int i = 55;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            ser.serializeChar(bos, "MyChar", in);
            ser.serializeI32(bos, "MyInt", i);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);


    }

    @Test
    public void I32AlignDeserializeTest() {
        char cin = 'w', cout = '0';
        int iin = 5, iout = 0;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            ser.serializeChar(bos, "", cin);
            ser.serializeI32(bos, "", iin);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            cout = ser.deserializeChar(bis, "");
            iout = ser.deserializeI32(bis, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue((cin == cout) && (iin == iout));


    }

    /*
     * UI32AlignTest
     */
    @Test
    public void UI32AlignSerializeTest() {
        char in = 'w';
        int i = 55;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            ser.serializeChar(bos, "MyChar", in);
            ser.serializeUI32(bos, "MyInt", i);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);


    }

    @Test
    public void UI32AlignDeserializeTest() {
        char cin = 'w', cout = '0';
        int iin = 5, iout = 0;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            ser.serializeChar(bos, "", cin);
            ser.serializeUI32(bos, "", iin);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            cout = ser.deserializeChar(bis, "");
            iout = ser.deserializeUI32(bis, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue((cin == cout) && (iin == iout));


    }

    /*
     * I64AlignTest
     */
    @Test
    public void I64AlignSerializeTest() {
        char in = 'w';
        long i = 55;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            ser.serializeChar(bos, "MyChar", in);
            ser.serializeI64(bos, "MyInt", i);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);


    }

    @Test
    public void I64AlignDeserializeTest() {
        char cin = 'w', cout = '0';
        long iin = 5, iout = 0;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            ser.serializeChar(bos, "", cin);
            ser.serializeI64(bos, "", iin);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            cout = ser.deserializeChar(bis, "");
            iout = ser.deserializeI64(bis, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue((cin == cout) && (iin == iout));


    }

    /*
     * UI64AlignTest
     */
    @Test
    public void UI64AlignSerializeTest() {
        char in = 'w';
        long i = 55;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            ser.serializeChar(bos, "MyChar", in);
            ser.serializeUI64(bos, "MyInt", i);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);


    }

    @Test
    public void UI64AlignDeserializeTest() {
        char cin = 'w', cout = '0';
        long iin = 5, iout = 0;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            ser.serializeChar(bos, "", cin);
            ser.serializeUI64(bos, "", iin);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            cout = ser.deserializeChar(bis, "");
            iout = ser.deserializeUI64(bis, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue((cin == cout) && (iin == iout));


    }

    /*
     * Float32AlignTest
     */
    @Test
    public void Float32AlignSerializeTest() {
        char in = 'w';
        float i = (float) 55.5;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            ser.serializeChar(bos, "MyChar", in);
            ser.serializeFloat32(bos, "MyInt", i);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);


    }

    @Test
    public void Float32AlignDeserializeTest() {
        char cin = 'w', cout = '0';
        float iin = (float) 5.5, iout = (float) 0.0;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            ser.serializeChar(bos, "", cin);
            ser.serializeFloat32(bos, "", iin);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            cout = ser.deserializeChar(bis, "");
            iout = ser.deserializeFloat32(bis, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue((cin == cout) && (iin == iout));


    }

    /*
     * Float64AlignTest
     */
    @Test
    public void Float64AlignSerializeTest() {
        char in = 'w';
        double i = 55.5;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            ser.serializeChar(bos, "MyChar", in);
            ser.serializeFloat64(bos, "MyInt", i);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);


    }

    @Test
    public void Float64AlignDeserializeTest() {
        char cin = 'w', cout = '0';
        double iin = 5.5, iout = 0.0;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            ser.serializeChar(bos, "", cin);
            ser.serializeFloat64(bos, "", iin);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            cout = ser.deserializeChar(bis, "");
            iout = ser.deserializeFloat64(bis, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue((cin == cout) && (iin == iout));


    }

    /*
     * BooleanAlignTest
     */
    @Test
    public void BooleanAlignSerializeTest() {
        char in = 'w';
        boolean i = true;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            ser.serializeChar(bos, "MyChar", in);
            ser.serializeBoolean(bos, "MyInt", i);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);


    }

    @Test
    public void BooleanAlignDeserializeTest() {
        char cin = 'w', cout = '0';
        boolean iin = true, iout = false;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            ser.serializeChar(bos, "", cin);
            ser.serializeBoolean(bos, "", iin);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            cout = ser.deserializeChar(bis, "");
            iout = ser.deserializeBoolean(bis, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue((cin == cout) && (iin == iout));


    }

    /*
     * StringAlignTest
     */
    @Test
    public void StringAlignSerializeTest() {
        char in = 'w';
        String i = "Hello World!";

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            ser.serializeChar(bos, "MyChar", in);
            ser.serializeString(bos, "MyInt", i);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);


    }

    @Test
    public void StringAlignDeserializeTest() {
        char cin = 'w', cout = '0';
        String iin = "Hello World!", iout = "";

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            ser.serializeChar(bos, "", cin);
            ser.serializeString(bos, "", iin);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            cout = ser.deserializeChar(bis, "");
            iout = ser.deserializeString(bis, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue((cin == cout) && (iin.compareTo(iout) == 0));


    }

    /*
     * Generic types
     */
    @Test
    public void serializeAlignTest() {
        char a = 'w';
        GenericType in = new GenericType(1, "one");

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            ser.serializeChar(bos, "", a);
            ser.serialize(bos, "", in);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);


    }

    @Test
    public void deserializeAlignTest() {
        char cin = 'w', cout = '0';
        GenericType in = new GenericType(1, "one");
        GenericType out = new GenericType();

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            ser.serializeChar(bos, "", cin);
            ser.serialize(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            cout = ser.deserializeChar(bis, "");
            out = ser.deserialize(bis, "", GenericType.class);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(cin == cout && in.equals(out));


    }

    /*
     * SerializeArrayChar
     */
    @Test
    public void serializeAlignArrayCharTest() {
        char c = 'w';
        List<Character> in = new ArrayList<>();
        in.add('a');

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            ser.serializeChar(bos, "", c);
            new ListAsArraySerializer<>(1, new BasicSerializers.CharSerializer()).write(ser, bos, "", in);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);


    }

    @Test
    public void deserializeAlignArrayCharTest() {
        char cin = 'w', cout = '0';
        ArrayList<Character> in = new ArrayList<>();
        in.add('a');
        in.add('e');
        in.add('i');
        in.add('o');
        in.add('u');
        List<Character> out = new ArrayList<>();

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            Serializer<List<Character>> s = new ListAsArraySerializer<>(5, new BasicSerializers.CharSerializer());

            ser.serializeChar(bos, "", cin);
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            cout = ser.deserializeChar(bis, "");
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        boolean error = false;
        for (int i = 0; i < in.size(); ++i) {
            if (in.get(i) != out.get(i)) {
                error = true;
            }
        }

        assertTrue(!error && cin == cout);


    }

    /*
     * SerializeArrayByte
     */
    @Test
    public void serializeAlignArrayByteTest() {
        char c = 'w';
        List<Byte> in = new ArrayList<>();
        in.add((byte) 5);

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            Serializer<List<Byte>> s = new ListAsArraySerializer<>(1, new BasicSerializers.ByteSerializer());

            ser.serializeChar(bos, "", c);
            s.write(ser, bos, "", in);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);


    }

    @Test
    public void deserializeAlignArrayByteTest() {
        char cin = 'w', cout = '0';
        List<Byte> in = new ArrayList<>();
        in.add((byte) 5);
        in.add((byte) 6);
        in.add((byte) 7);
        in.add((byte) 8);
        in.add((byte) 9);
        List<Byte> out = new ArrayList<>();

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            Serializer<List<Byte>> s = new ListAsArraySerializer<>(5, new BasicSerializers.ByteSerializer());

            ser.serializeChar(bos, "", cin);
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            cout = ser.deserializeChar(bis, "");
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        boolean error = false;
        for (int i = 0; i < in.size(); ++i) {
            if (!Objects.equals(in.get(i), out.get(i))) {
                error = true;
            }
        }

        assertTrue(!error && cin == cout);


    }

    /*
     * SerializeArrayI16
     */
    @Test
    public void serializeAlignArrayI16Test() {
        char c = 'w';
        List<Short> in = new ArrayList<>();
        in.add((short) 5);

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            Serializer<List<Short>> s = new ListAsArraySerializer<>(1, new BasicSerializers.I16Serializer());

            ser.serializeChar(bos, "", c);
            s.write(ser, bos, "", in);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);


    }

    @Test
    public void deserializeAlignArrayI16Test() {
        char cin = 'w', cout = '0';
        List<Short> in = new ArrayList<>();
        in.add((short) 5);
        in.add((short) 6);
        in.add((short) 7);
        in.add((short) 8);
        in.add((short) 9);
        List<Short> out = new ArrayList<>();

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            Serializer<List<Short>> s = new ListAsArraySerializer<>(5, new BasicSerializers.I16Serializer());

            ser.serializeChar(bos, "", cin);
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            cout = ser.deserializeChar(bis, "");
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        boolean error = false;
        for (int i = 0; i < in.size(); ++i) {
            if (!Objects.equals(in.get(i), out.get(i))) {
                error = true;
            }
        }

        assertTrue(!error && cin == cout);


    }

    /*
     * SerializeArrayUI16
     */
    @Test
    public void serializeAlignArrayUI16Test() {
        char c = 'w';
        List<Short> in = new ArrayList<>();
        in.add((short) 5);

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            Serializer<List<Short>> s = new ListAsArraySerializer<>(1, new BasicSerializers.UI16Serializer());

            ser.serializeChar(bos, "", c);
            s.write(ser, bos, "", in);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);


    }

    @Test
    public void deserializeAlignArrayUI16Test() {
        char cin = 'w', cout = '0';
        List<Short> in = new ArrayList<>();
        in.add((short) 5);
        in.add((short) 6);
        in.add((short) 7);
        in.add((short) 8);
        in.add((short) 9);
        List<Short> out = new ArrayList<>();

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            Serializer<List<Short>> s = new ListAsArraySerializer<>(5, new BasicSerializers.UI16Serializer());


            ser.serializeChar(bos, "", cin);
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            cout = ser.deserializeChar(bis, "");
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        boolean error = false;
        for (int i = 0; i < in.size(); ++i) {
            if (!Objects.equals(in.get(i), out.get(i))) {
                error = true;
            }
        }

        assertTrue(!error && cin == cout);


    }

    /*
     * SerializeArrayI32
     */
    @Test
    public void serializeAlignArrayI32Test() {
        char c = 'w';
        List<Integer> in = new ArrayList<>();
        in.add((int) 5);

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            Serializer<List<Integer>> s = new ListAsArraySerializer<>(1, new BasicSerializers.I32Serializer());

            ser.serializeChar(bos, "", c);
            s.write(ser, bos, "", in);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);


    }

    @Test
    public void deserializeAlignArrayI32Test() {
        char cin = 'w', cout = '0';
        List<Integer> in = new ArrayList<>();
        in.add((int) 5);
        in.add((int) 6);
        in.add((int) 7);
        in.add((int) 8);
        in.add((int) 9);
        List<Integer> out = new ArrayList<>();

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            Serializer<List<Integer>> s = new ListAsArraySerializer<>(5, new BasicSerializers.I32Serializer());

            ser.serializeChar(bos, "", cin);
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            cout = ser.deserializeChar(bis, "");
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        boolean error = false;
        for (int i = 0; i < in.size(); ++i) {
            if (!Objects.equals(in.get(i), out.get(i))) {
                error = true;
            }
        }

        assertTrue(!error && cin == cout);


    }

    /*
     * SerializeArrayUI32
     */
    @Test
    public void serializeAlignArrayUI32Test() {
        char c = 'w';
        List<Integer> in = new ArrayList<>();
        in.add((int) 5);

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            Serializer<List<Integer>> s = new ListAsArraySerializer<>(1, new BasicSerializers.UI32Serializer());

            ser.serializeChar(bos, "", c);
            s.write(ser, bos, "", in);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);


    }

    @Test
    public void deserializeAlignArrayUI32Test() {
        char cin = 'w', cout = '0';
        List<Integer> in = new ArrayList<>();
        in.add((int) 5);
        in.add((int) 6);
        in.add((int) 7);
        in.add((int) 8);
        in.add((int) 9);
        List<Integer> out = new ArrayList<>();

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            Serializer<List<Integer>> s = new ListAsArraySerializer<>(5, new BasicSerializers.UI32Serializer());

            ser.serializeChar(bos, "", cin);
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            cout = ser.deserializeChar(bis, "");
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        boolean error = false;
        for (int i = 0; i < in.size(); ++i) {
            if (!Objects.equals(in.get(i), out.get(i))) {
                error = true;
            }
        }

        assertTrue(!error && cin == cout);


    }

    /*
     * SerializeArrayI64
     */
    @Test
    public void serializeAlignArrayI64Test() {
        char c = 'w';
        List<Long> in = new ArrayList<>();
        in.add((long) 5);

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            Serializer<List<Long>> s = new ListAsArraySerializer<>(1, new BasicSerializers.I64Serializer());

            ser.serializeChar(bos, "", c);
            s.write(ser, bos, "", in);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);


    }

    @Test
    public void deserializeAlignArrayI64Test() {
        char cin = 'w', cout = '0';
        List<Long> in = new ArrayList<>();
        in.add((long) 5);
        in.add((long) 6);
        in.add((long) 7);
        in.add((long) 8);
        in.add((long) 9);
        List<Long> out = new ArrayList<>();

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            Serializer<List<Long>> s = new ListAsArraySerializer<>(5, new BasicSerializers.I64Serializer());

            ser.serializeChar(bos, "", cin);
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            cout = ser.deserializeChar(bis, "");
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        boolean error = false;
        for (int i = 0; i < in.size(); ++i) {
            if (!Objects.equals(in.get(i), out.get(i))) {
                error = true;
            }
        }

        assertTrue(!error && cin == cout);


    }

    /*
     * SerializeArrayUI64
     */
    @Test
    public void serializeAlignArrayUI64Test() {
        char c = 'w';
        List<Long> in = new ArrayList<>();
        in.add((long) 5);

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            Serializer<List<Long>> s = new ListAsArraySerializer<>(1, new BasicSerializers.UI64Serializer());

            ser.serializeChar(bos, "", c);
            s.write(ser, bos, "", in);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);


    }

    @Test
    public void deserializeAlignArrayUI64Test() {
        char cin = 'w', cout = '0';
        List<Long> in = new ArrayList<>();
        in.add((long) 5);
        in.add((long) 6);
        in.add((long) 7);
        in.add((long) 8);
        in.add((long) 9);
        List<Long> out = new ArrayList<>();

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            Serializer<List<Long>> s = new ListAsArraySerializer<>(5, new BasicSerializers.UI64Serializer());

            ser.serializeChar(bos, "", cin);
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            cout = ser.deserializeChar(bis, "");
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        boolean error = false;
        for (int i = 0; i < in.size(); ++i) {
            if (!Objects.equals(in.get(i), out.get(i))) {
                error = true;
            }
        }

        assertTrue(!error && cin == cout);


    }

    /*
     * SerializeArrayFloat32
     */
    @Test
    public void serializeAlignArrayFloat32Test() {
        char c = 'w';
        List<Float> in = new ArrayList<>();
        in.add((float) 5.0);

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            Serializer<List<Float>> s = new ListAsArraySerializer<>(1, new BasicSerializers.Float32Serializer());

            ser.serializeChar(bos, "", c);
            s.write(ser, bos, "", in);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);


    }

    @Test
    public void deserializeAlignArrayFloat32Test() {
        char cin = 'w', cout = '0';
        List<Float> in = new ArrayList<>();
        in.add((float) 5.0);
        in.add((float) 6.1);
        in.add((float) 7.2);
        in.add((float) 8.3);
        in.add((float) 9.4);
        List<Float> out = new ArrayList<>();

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            Serializer<List<Float>> s = new ListAsArraySerializer<>(5, new BasicSerializers.Float32Serializer());

            ser.serializeChar(bos, "", cin);
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            cout = ser.deserializeChar(bis, "");
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        boolean error = false;
        for (int i = 0; i < in.size(); ++i) {
            if (java.lang.Float.compare(in.get(i), out.get(i)) != 0) {
                error = true;
            }
        }

        assertTrue(!error && cin == cout);


    }

    /*
     * SerializeArrayFloat64
     */
    @Test
    public void serializeAlignArrayFloat64Test() {
        char c = 'w';
        List<Double> in = new ArrayList<>();
        in.add((double) 5.0);

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            Serializer<List<Double>> s = new ListAsArraySerializer<>(1, new BasicSerializers.Float64Serializer());

            ser.serializeChar(bos, "", c);
            s.write(ser, bos, "", in);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);


    }

    @Test
    public void deserializeAlignArrayFloat64Test() {
        char cin = 'w', cout = '0';
        List<Double> in = new ArrayList<>();
        in.add((double) 5.0);
        in.add((double) 6.1);
        in.add((double) 7.2);
        in.add((double) 8.3);
        in.add((double) 9.4);
        List<Double> out = new ArrayList<>();

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            Serializer<List<Double>> s = new ListAsArraySerializer<>(5, new BasicSerializers.Float64Serializer());

            ser.serializeChar(bos, "", cin);
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            cout = ser.deserializeChar(bis, "");
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        boolean error = false;
        for (int i = 0; i < in.size(); ++i) {
            if (java.lang.Double.compare(in.get(i), out.get(i)) != 0) {
                error = true;
            }
        }

        assertTrue(!error && cin == cout);


    }

    /*
     * SerializeArrayBoolean
     */
    @Test
    public void serializeAlignArrayBooleanTest() {
        char c = 'w';
        List<Boolean> in = new ArrayList<>();
        in.add(true);

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            Serializer<List<Boolean>> s = new ListAsArraySerializer<>(1, new BasicSerializers.BooleanSerializer());

            ser.serializeChar(bos, "", c);
            s.write(ser, bos, "", in);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);


    }

    @Test
    public void deserializeAlignArrayBooleanTest() {
        char cin = 'w', cout = '0';
        List<Boolean> in = new ArrayList<>();
        in.add(false);
        in.add(true);
        in.add(true);
        in.add(false);
        in.add(true);
        List<Boolean> out = new ArrayList<>();

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            Serializer<List<Boolean>> s = new ListAsArraySerializer<>(5, new BasicSerializers.BooleanSerializer());

            ser.serializeChar(bos, "", cin);
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            cout = ser.deserializeChar(bis, "");
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        boolean error = false;
        for (int i = 0; i < in.size(); ++i) {
            if (in.get(i) != out.get(i)) {
                error = true;
            }
        }

        assertTrue(!error && cin == cout);


    }

    /*
     * SerializeArrayString
     */
    @Test
    public void serializeAlignArrayStringTest() {
        char c = 'w';
        List<String> in = new ArrayList<>();
        in.add("one");

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            Serializer<List<String>> s = new ListAsArraySerializer<>(1, new BasicSerializers.StringSerializer());

            ser.serializeChar(bos, "", c);
            s.write(ser, bos, "", in);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);


    }

    @Test
    public void deserializeAlignArrayStringTest() {
        char cin = 'w', cout = '0';
        List<String> in = new ArrayList<>();
        in.add("one");
        in.add("two");
        in.add("three");
        in.add("four");
        in.add("five");
        List<String> out = new ArrayList<>();

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            Serializer<List<String>> s = new ListAsArraySerializer<>(5, new BasicSerializers.StringSerializer());

            ser.serializeChar(bos, "", cin);
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            cout = ser.deserializeChar(bis, "");
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        boolean error = false;
        for (int i = 0; i < in.size(); ++i) {
            if (in.get(i).compareTo(out.get(i)) != 0) {
                error = true;
            }
        }

        assertTrue(!error && cin == cout);


    }

    /*
     * Array of generic types
     */
    @Test
    public void serializeAlignArrayTest() {
        char c = 'w';
        List<GenericType> in = new ArrayList<>();
        in.add(new GenericType(1, "one"));

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            Serializer<List<GenericType>> s
                    = new ListAsArraySerializer<>(1, new ObjectSerializer<>(GenericType.class));

            ser.serializeChar(bos, "", c);
            s.write(ser, bos, "", in);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);


    }

    @Test
    public void deserializeAlignArrayTest() {
        char cin = 'w', cout = '0';
        List<GenericType> in = new ArrayList<>();
        in.add(new GenericType(1, "one"));
        in.add(new GenericType(2, "two"));
        in.add(new GenericType(3, "three"));
        in.add(new GenericType(4, "four"));
        in.add(new GenericType(5, "five"));
        List<GenericType> out = new ArrayList<>();

        Serializer<List<GenericType>> s
            = new ListAsArraySerializer<>(5, new ObjectSerializer<>(GenericType.class));

        try {
            BinaryOutputStream bos = new BinaryOutputStream();

            ser.serializeChar(bos, "", cin);
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            cout = ser.deserializeChar(bis, "");
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        boolean error = false;
        for (int i = 0; i < in.size(); ++i) {
            if (!in.get(i).equals(out.get(i))) {
                error = true;
            }
        }

        assertTrue(!error && cin == cout);


    }

}
