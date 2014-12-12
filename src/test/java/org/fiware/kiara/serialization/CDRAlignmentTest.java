package org.fiware.kiara.serialization;

import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.fiware.kiara.serialization.impl.CDRSerializer;
import org.fiware.kiara.transport.impl.TransportMessage;

public class CDRAlignmentTest {

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
     * CharAlignTest
     */

    @Test
    public void CharAlignSerializeTest() {
        char in = 'w';
        char i = 'f';

        try {
            ser.serializeChar(message, "MyChar", in);
            ser.serializeChar(message, "MyShort", i);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void CharAlignDeserializeTest() {
        char cin = 'w', cout = '0';
        char iin = 'f', iout = '0';

        try {
            ser.serializeChar(message, "", cin);
            ser.serializeChar(message, "", iin);
            message.getPayload().rewind();
            cout = ser.deserializeChar(message, "");
            iout = ser.deserializeChar(message, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue((cin == cout) && (iin == iout));

        reset();
    }

    /*
     * ByteAlignTest
     */

    @Test
    public void ByteAlignSerializeTest() {
        char in = 'w';
        byte i = 5;

        try {
            ser.serializeChar(message, "MyChar", in);
            ser.serializeByte(message, "MyShort", i);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void ByteAlignDeserializeTest() {
        char cin = 'w', cout = '0';
        byte iin = 5, iout = 0;

        try {
            ser.serializeChar(message, "", cin);
            ser.serializeByte(message, "", iin);
            message.getPayload().rewind();
            cout = ser.deserializeChar(message, "");
            iout = ser.deserializeByte(message, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue((cin == cout) && (iin == iout));

        reset();
    }

    /*
     * I16AlignTest
     */

    @Test
    public void I16AlignSerializeTest() {
        char in = 'w';
        short i = 55;

        try {
            ser.serializeChar(message, "MyChar", in);
            ser.serializeI16(message, "MyShort", i);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void I16AlignDeserializeTest() {
        char cin = 'w', cout = '0';
        short iin = 5, iout = 0;

        try {
            ser.serializeChar(message, "", cin);
            ser.serializeI16(message, "", iin);
            message.getPayload().rewind();
            cout = ser.deserializeChar(message, "");
            iout = ser.deserializeI16(message, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue((cin == cout) && (iin == iout));

        reset();
    }

    /*
     * UI16AlignTest
     */

    @Test
    public void UI16AlignSerializeTest() {
        char in = 'w';
        short i = 55;

        try {
            ser.serializeChar(message, "MyChar", in);
            ser.serializeUI32(message, "MyInt", i);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void UI16AlignDeserializeTest() {
        char cin = 'w', cout = '0';
        short iin = 5, iout = 0;

        try {
            ser.serializeChar(message, "", cin);
            ser.serializeUI16(message, "", iin);
            message.getPayload().rewind();
            cout = ser.deserializeChar(message, "");
            iout = ser.deserializeUI16(message, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue((cin == cout) && (iin == iout));

        reset();
    }

    /*
     * I32AlignTest
     */

    @Test
    public void I32AlignSerializeTest() {
        char in = 'w';
        int i = 55;

        try {
            ser.serializeChar(message, "MyChar", in);
            ser.serializeI32(message, "MyInt", i);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void I32AlignDeserializeTest() {
        char cin = 'w', cout = '0';
        int iin = 5, iout = 0;

        try {
            ser.serializeChar(message, "", cin);
            ser.serializeI32(message, "", iin);
            message.getPayload().rewind();
            cout = ser.deserializeChar(message, "");
            iout = ser.deserializeI32(message, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue((cin == cout) && (iin == iout));

        reset();
    }

    /*
     * UI32AlignTest
     */

    @Test
    public void UI32AlignSerializeTest() {
        char in = 'w';
        int i = 55;

        try {
            ser.serializeChar(message, "MyChar", in);
            ser.serializeUI32(message, "MyInt", i);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void UI32AlignDeserializeTest() {
        char cin = 'w', cout = '0';
        int iin = 5, iout = 0;

        try {
            ser.serializeChar(message, "", cin);
            ser.serializeUI32(message, "", iin);
            message.getPayload().rewind();
            cout = ser.deserializeChar(message, "");
            iout = ser.deserializeUI32(message, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue((cin == cout) && (iin == iout));

        reset();
    }

    /*
     * I64AlignTest
     */

    @Test
    public void I64AlignSerializeTest() {
        char in = 'w';
        long i = 55;

        try {
            ser.serializeChar(message, "MyChar", in);
            ser.serializeI64(message, "MyInt", i);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void I64AlignDeserializeTest() {
        char cin = 'w', cout = '0';
        long iin = 5, iout = 0;

        try {
            ser.serializeChar(message, "", cin);
            ser.serializeI64(message, "", iin);
            message.getPayload().rewind();
            cout = ser.deserializeChar(message, "");
            iout = ser.deserializeI64(message, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue((cin == cout) && (iin == iout));

        reset();
    }

    /*
     * UI64AlignTest
     */

    @Test
    public void UI64AlignSerializeTest() {
        char in = 'w';
        long i = 55;

        try {
            ser.serializeChar(message, "MyChar", in);
            ser.serializeUI64(message, "MyInt", i);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void UI64AlignDeserializeTest() {
        char cin = 'w', cout = '0';
        long iin = 5, iout = 0;

        try {
            ser.serializeChar(message, "", cin);
            ser.serializeUI64(message, "", iin);
            message.getPayload().rewind();
            cout = ser.deserializeChar(message, "");
            iout = ser.deserializeUI64(message, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue((cin == cout) && (iin == iout));

        reset();
    }

    /*
     * Float32AlignTest
     */

    @Test
    public void Float32AlignSerializeTest() {
        char in = 'w';
        float i = (float) 55.5;

        try {
            ser.serializeChar(message, "MyChar", in);
            ser.serializeFloat32(message, "MyInt", i);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void Float32AlignDeserializeTest() {
        char cin = 'w', cout = '0';
        float iin = (float) 5.5, iout = (float) 0.0;

        try {
            ser.serializeChar(message, "", cin);
            ser.serializeFloat32(message, "", iin);
            message.getPayload().rewind();
            cout = ser.deserializeChar(message, "");
            iout = ser.deserializeFloat32(message, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue((cin == cout) && (iin == iout));

        reset();
    }

    /*
     * Float64AlignTest
     */

    @Test
    public void Float64AlignSerializeTest() {
        char in = 'w';
        double i = 55.5;

        try {
            ser.serializeChar(message, "MyChar", in);
            ser.serializeFloat64(message, "MyInt", i);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void Float64AlignDeserializeTest() {
        char cin = 'w', cout = '0';
        double iin = 5.5, iout = 0.0;

        try {
            ser.serializeChar(message, "", cin);
            ser.serializeFloat64(message, "", iin);
            message.getPayload().rewind();
            cout = ser.deserializeChar(message, "");
            iout = ser.deserializeFloat64(message, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue((cin == cout) && (iin == iout));

        reset();
    }

    /*
     * BooleanAlignTest
     */

    @Test
    public void BooleanAlignSerializeTest() {
        char in = 'w';
        boolean i = true;

        try {
            ser.serializeChar(message, "MyChar", in);
            ser.serializeBoolean(message, "MyInt", i);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void BooleanAlignDeserializeTest() {
        char cin = 'w', cout = '0';
        boolean iin = true, iout = false;

        try {
            ser.serializeChar(message, "", cin);
            ser.serializeBoolean(message, "", iin);
            message.getPayload().rewind();
            cout = ser.deserializeChar(message, "");
            iout = ser.deserializeBoolean(message, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue((cin == cout) && (iin == iout));

        reset();
    }

    /*
     * StringAlignTest
     */

    @Test
    public void StringAlignSerializeTest() {
        char in = 'w';
        String i = "Hello World!";

        try {
            ser.serializeChar(message, "MyChar", in);
            ser.serializeString(message, "MyInt", i);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void StringAlignDeserializeTest() {
        char cin = 'w', cout = '0';
        String iin = "Hello World!", iout = "";

        try {
            ser.serializeChar(message, "", cin);
            ser.serializeString(message, "", iin);
            message.getPayload().rewind();
            cout = ser.deserializeChar(message, "");
            iout = ser.deserializeString(message, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue((cin == cout) && (iin.compareTo(iout) == 0));

        reset();
    }

    /*
     * Generic types
     */

    @Test
    public void serializeAlignTest() {
        char a = 'w';
        GenericType in = new GenericType(1, "one");

        try {
            ser.serializeChar(message, "", a);
            ser.serialize(message, "", in);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void deserializeAlignTest() {
        char cin = 'w', cout = '0';
        GenericType in = new GenericType(1, "one");
        GenericType out = new GenericType();

        try {
            ser.serializeChar(message, "", cin);
            ser.serialize(message, "", in);
            message.getPayload().rewind();
            cout = ser.deserializeChar(message, "");
            out = ser.deserialize(message, "", GenericType.class);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(cin == cout && in.equals(out));

        reset();
    }

    /*
     * SerializeArrayChar
     */

    @Test
    public void serializeAlignArrayCharTest() {
        char c = 'w';
        List<Character> in = new ArrayList<Character>();
        in.add('a');

        try {
            ser.serializeChar(message, "", c);
            ser.serializeArrayChar(message, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void deserializeAlignArrayCharTest() {
        char cin = 'w', cout = '0';
        ArrayList<Character> in = new ArrayList<Character>();
        in.add('a');
        in.add('e');
        in.add('i');
        in.add('o');
        in.add('u');
        List<Character> out = new ArrayList<Character>();;

        try {
            ser.serializeChar(message, "", cin);
            ser.serializeArrayChar(message, "", in, 5);
            message.getPayload().rewind();
            cout = ser.deserializeChar(message, "");
            out = ser.deserializeArrayChar(message, "", 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        boolean error = false;
        for (int i=0; i < in.size(); ++i) {
            if (in.get(i) != out.get(i)) {
                error = true;
            }
        }

        assertTrue(!error && cin == cout);

        reset();
    }

    /*
     * SerializeArrayByte
     */

    @Test
    public void serializeAlignArrayByteTest() {
        char c = 'w';
        List<Byte> in = new ArrayList<Byte>();
        in.add((byte) 5);

        try {
            ser.serializeChar(message, "", c);
            ser.serializeArrayByte(message, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void deserializeAlignArrayByteTest() {
        char cin = 'w', cout = '0';
        List<Byte> in = new ArrayList<Byte>();
        in.add((byte) 5);
        in.add((byte) 6);
        in.add((byte) 7);
        in.add((byte) 8);
        in.add((byte) 9);
        List<Byte> out = new ArrayList<Byte>();;

        try {
            ser.serializeChar(message, "", cin);
            ser.serializeArrayByte(message, "", in, 5);
            message.getPayload().rewind();
            cout = ser.deserializeChar(message, "");
            out = ser.deserializeArrayByte(message, "", 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        boolean error = false;
        for (int i=0; i < in.size(); ++i) {
            if (in.get(i) != out.get(i)) {
                error = true;
            }
        }

        assertTrue(!error && cin == cout);

        reset();
    }

    /*
     * SerializeArrayI16
     */

    @Test
    public void serializeAlignArrayI16Test() {
        char c = 'w';
        List<Short> in = new ArrayList<Short>();
        in.add((short) 5);

        try {
            ser.serializeChar(message, "", c);
            ser.serializeArrayI16(message, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void deserializeAlignArrayI16Test() {
        char cin = 'w', cout = '0';
        List<Short> in = new ArrayList<Short>();
        in.add((short) 5);
        in.add((short) 6);
        in.add((short) 7);
        in.add((short) 8);
        in.add((short) 9);
        List<Short> out = new ArrayList<Short>();;

        try {
            ser.serializeChar(message, "", cin);
            ser.serializeArrayI16(message, "", in, 5);
            message.getPayload().rewind();
            cout = ser.deserializeChar(message, "");
            out = ser.deserializeArrayI16(message, "", 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        boolean error = false;
        for (int i=0; i < in.size(); ++i) {
            if (in.get(i) != out.get(i)) {
                error = true;
            }
        }

        assertTrue(!error && cin == cout);

        reset();
    }

    /*
     * SerializeArrayUI16
     */

    @Test
    public void serializeAlignArrayUI16Test() {
        char c = 'w';
        List<Short> in = new ArrayList<Short>();
        in.add((short) 5);

        try {
            ser.serializeChar(message, "", c);
            ser.serializeArrayUI16(message, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void deserializeAlignArrayUI16Test() {
        char cin = 'w', cout = '0';
        List<Short> in = new ArrayList<Short>();
        in.add((short) 5);
        in.add((short) 6);
        in.add((short) 7);
        in.add((short) 8);
        in.add((short) 9);
        List<Short> out = new ArrayList<Short>();;

        try {
            ser.serializeChar(message, "", cin);
            ser.serializeArrayUI16(message, "", in, 5);
            message.getPayload().rewind();
            cout = ser.deserializeChar(message, "");
            out = ser.deserializeArrayUI16(message, "", 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        boolean error = false;
        for (int i=0; i < in.size(); ++i) {
            if (in.get(i) != out.get(i)) {
                error = true;
            }
        }

        assertTrue(!error && cin == cout);

        reset();
    }

    /*
     * SerializeArrayI32
     */

    @Test
    public void serializeAlignArrayI32Test() {
        char c = 'w';
        List<Integer> in = new ArrayList<Integer>();
        in.add((int) 5);

        try {
            ser.serializeChar(message, "", c);
            ser.serializeArrayI32(message, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void deserializeAlignArrayI32Test() {
        char cin = 'w', cout = '0';
        List<Integer> in = new ArrayList<Integer>();
        in.add((int) 5);
        in.add((int) 6);
        in.add((int) 7);
        in.add((int) 8);
        in.add((int) 9);
        List<Integer> out = new ArrayList<Integer>();;

        try {
            ser.serializeChar(message, "", cin);
            ser.serializeArrayI32(message, "", in, 5);
            message.getPayload().rewind();
            cout = ser.deserializeChar(message, "");
            out = ser.deserializeArrayI32(message, "", 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        boolean error = false;
        for (int i=0; i < in.size(); ++i) {
            if (in.get(i) != out.get(i)) {
                error = true;
            }
        }

        assertTrue(!error && cin == cout);

        reset();
    }

    /*
     * SerializeArrayUI32
     */

    @Test
    public void serializeAlignArrayUI32Test() {
        char c = 'w';
        List<Integer> in = new ArrayList<Integer>();
        in.add((int) 5);

        try {
            ser.serializeChar(message, "", c);
            ser.serializeArrayUI32(message, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void deserializeAlignArrayUI32Test() {
        char cin = 'w', cout = '0';
        List<Integer> in = new ArrayList<Integer>();
        in.add((int) 5);
        in.add((int) 6);
        in.add((int) 7);
        in.add((int) 8);
        in.add((int) 9);
        List<Integer> out = new ArrayList<Integer>();;

        try {
            ser.serializeChar(message, "", cin);
            ser.serializeArrayUI32(message, "", in, 5);
            message.getPayload().rewind();
            cout = ser.deserializeChar(message, "");
            out = ser.deserializeArrayUI32(message, "", 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        boolean error = false;
        for (int i=0; i < in.size(); ++i) {
            if (in.get(i) != out.get(i)) {
                error = true;
            }
        }

        assertTrue(!error && cin == cout);

        reset();
    }

    /*
     * SerializeArrayI64
     */

    @Test
    public void serializeAlignArrayI64Test() {
        char c = 'w';
        List<Long> in = new ArrayList<Long>();
        in.add((long) 5);

        try {
            ser.serializeChar(message, "", c);
            ser.serializeArrayI64(message, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void deserializeAlignArrayI64Test() {
        char cin = 'w', cout = '0';
        List<Long> in = new ArrayList<Long>();
        in.add((long) 5);
        in.add((long) 6);
        in.add((long) 7);
        in.add((long) 8);
        in.add((long) 9);
        List<Long> out = new ArrayList<Long>();;

        try {
            ser.serializeChar(message, "", cin);
            ser.serializeArrayI64(message, "", in, 5);
            message.getPayload().rewind();
            cout = ser.deserializeChar(message, "");
            out = ser.deserializeArrayI64(message, "", 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        boolean error = false;
        for (int i=0; i < in.size(); ++i) {
            if (in.get(i) != out.get(i)) {
                error = true;
            }
        }

        assertTrue(!error && cin == cout);

        reset();
    }

    /*
     * SerializeArrayUI64
     */

    @Test
    public void serializeAlignArrayUI64Test() {
        char c = 'w';
        List<Long> in = new ArrayList<Long>();
        in.add((long) 5);

        try {
            ser.serializeChar(message, "", c);
            ser.serializeArrayUI64(message, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void deserializeAlignArrayUI64Test() {
        char cin = 'w', cout = '0';
        List<Long> in = new ArrayList<Long>();
        in.add((long) 5);
        in.add((long) 6);
        in.add((long) 7);
        in.add((long) 8);
        in.add((long) 9);
        List<Long> out = new ArrayList<Long>();;

        try {
            ser.serializeChar(message, "", cin);
            ser.serializeArrayUI64(message, "", in, 5);
            message.getPayload().rewind();
            cout = ser.deserializeChar(message, "");
            out = ser.deserializeArrayUI64(message, "", 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        boolean error = false;
        for (int i=0; i < in.size(); ++i) {
            if (in.get(i) != out.get(i)) {
                error = true;
            }
        }

        assertTrue(!error && cin == cout);

        reset();
    }

    /*
     * SerializeArrayFloat32
     */

    @Test
    public void serializeAlignArrayFloat32Test() {
        char c = 'w';
        List<Float> in = new ArrayList<Float>();
        in.add((float) 5.0);

        try {
            ser.serializeChar(message, "", c);
            ser.serializeArrayFloat32(message, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void deserializeAlignArrayFloat32Test() {
        char cin = 'w', cout = '0';
        List<Float> in = new ArrayList<Float>();
        in.add((float) 5.0);
        in.add((float) 6.1);
        in.add((float) 7.2);
        in.add((float) 8.3);
        in.add((float) 9.4);
        List<Float> out = new ArrayList<Float>();;

        try {
            ser.serializeChar(message, "", cin);
            ser.serializeArrayFloat32(message, "", in, 5);
            message.getPayload().rewind();
            cout = ser.deserializeChar(message, "");
            out = ser.deserializeArrayFloat32(message, "", 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        boolean error = false;
        for (int i=0; i < in.size(); ++i) {
            if (java.lang.Float.compare(in.get(i), out.get(i)) != 0) {
                error = true;
            }
        }

        assertTrue(!error && cin == cout);

        reset();
    }

    /*
     * SerializeArrayFloat64
     */

    @Test
    public void serializeAlignArrayFloat64Test() {
        char c = 'w';
        List<Double> in = new ArrayList<Double>();
        in.add((double) 5.0);

        try {
            ser.serializeChar(message, "", c);
            ser.serializeArrayFloat64(message, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void deserializeAlignArrayFloat64Test() {
        char cin = 'w', cout = '0';
        List<Double> in = new ArrayList<Double>();
        in.add((double) 5.0);
        in.add((double) 6.1);
        in.add((double) 7.2);
        in.add((double) 8.3);
        in.add((double) 9.4);
        List<Double> out = new ArrayList<Double>();;

        try {
            ser.serializeChar(message, "", cin);
            ser.serializeArrayFloat64(message, "", in, 5);
            message.getPayload().rewind();
            cout = ser.deserializeChar(message, "");
            out = ser.deserializeArrayFloat64(message, "", 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        boolean error = false;
        for (int i=0; i < in.size(); ++i) {
            if (java.lang.Double.compare(in.get(i), out.get(i)) != 0) {
                error = true;
            }
        }

        assertTrue(!error && cin == cout);

        reset();
    }

    /*
     * SerializeArrayBoolean
     */

    @Test
    public void serializeAlignArrayBooleanTest() {
        char c = 'w';
        List<Boolean> in = new ArrayList<Boolean>();
        in.add(true);

        try {
            ser.serializeChar(message, "", c);
            ser.serializeArrayBoolean(message, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void deserializeAlignArrayBooleanTest() {
        char cin = 'w', cout = '0';
        List<Boolean> in = new ArrayList<Boolean>();
        in.add(false);
        in.add(true);
        in.add(true);
        in.add(false);
        in.add(true);
        List<Boolean> out = new ArrayList<Boolean>();;

        try {
            ser.serializeChar(message, "", cin);
            ser.serializeArrayBoolean(message, "", in, 5);
            message.getPayload().rewind();
            cout = ser.deserializeChar(message, "");
            out = ser.deserializeArrayBoolean(message, "", 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        boolean error = false;
        for (int i=0; i < in.size(); ++i) {
            if (in.get(i) != out.get(i)) {
                error = true;
            }
        }

        assertTrue(!error && cin == cout);

        reset();
    }

    /*
     * SerializeArrayString
     */

    @Test
    public void serializeAlignArrayStringTest() {
        char c = 'w';
        List<String> in = new ArrayList<String>();
        in.add("one");

        try {
            ser.serializeChar(message, "", c);
            ser.serializeArrayString(message, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void deserializeAlignArrayStringTest() {
        char cin = 'w', cout = '0';
        List<String> in = new ArrayList<String>();
        in.add("one");
        in.add("two");
        in.add("three");
        in.add("four");
        in.add("five");
        List<String> out = new ArrayList<String>();;

        try {
            ser.serializeChar(message, "", cin);
            ser.serializeArrayString(message, "", in, 5);
            message.getPayload().rewind();
            cout = ser.deserializeChar(message, "");
            out = ser.deserializeArrayString(message, "", 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        boolean error = false;
        for (int i=0; i < in.size(); ++i) {
            if (in.get(i).compareTo(out.get(i)) != 0) {
                error = true;
            }
        }

        assertTrue(!error && cin == cout);

        reset();
    }

    /*
     * Array of generic types
     */

    @Test
    public void serializeAlignArrayTest() {
        char c = 'w';
        List<GenericType> in = new ArrayList<GenericType>();
        in.add(new GenericType(1, "one"));

        try {
            ser.serializeChar(message, "", c);
            ser.serializeArray(message, "", in, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void deserializeAlignArrayTest() {
        char cin = 'w', cout = '0';
        List<GenericType> in = new ArrayList<GenericType>();
        in.add(new GenericType(1, "one"));
        in.add(new GenericType(2, "two"));
        in.add(new GenericType(3, "three"));
        in.add(new GenericType(4, "four"));
        in.add(new GenericType(5, "five"));
        List<GenericType> out = new ArrayList<GenericType>();;

        try {
            ser.serializeChar(message, "", cin);
            ser.serializeArray(message, "", in, 5);
            message.getPayload().rewind();
            cout = ser.deserializeChar(message, "");
            out = ser.deserializeArray(message, "", GenericType.class, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        boolean error = false;
        for (int i=0; i < in.size(); ++i) {
            if (!in.get(i).equals(out.get(i))) {
                error = true;
            }
        }

        assertTrue(!error && cin == cout);

        reset();
    }


}
