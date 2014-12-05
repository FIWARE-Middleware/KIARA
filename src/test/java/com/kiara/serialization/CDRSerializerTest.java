package com.kiara.serialization;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.kiara.serialization.impl.CDRSerializer;
import com.kiara.transport.impl.TransportMessage;

import org.junit.*;

public class CDRSerializerTest {

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
     * SerializeChar
     */

    @Test
    public void serializeCharTest() {
        char in = 'w';

        try {
            ser.serializeChar(message, "MyChar", in);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void deserializeCharTest() {
        char in = 'w';
        char out = 'a';

        try {
            ser.serializeChar(message, "MyChar", in);
            message.getPayload().rewind();
            out = ser.deserializeChar(message, "MyChar");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(in == out);

        reset();
    }

    /*
     * SerializeService
     */

    @Test
    public void serializeServiceTest() {
        String in = "ServiceName";

        try {
            ser.serializeService(message, in);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void deserializeServiceTest() {
        String in = "ServiceName", out = "";

        try {
            ser.serializeService(message, in);
            message.getPayload().rewind();
            out = ser.deserializeService(message);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(in.compareTo(out) == 0);

        reset();
    }

    /*
     * SerializeOperation
     */

    @Test
    public void serializeOperationTest() {
        String in = "OperationName";

        try {
            ser.serializeService(message, in);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void deserializeOperationTest() {
        String in = "OperationName", out = "";

        try {
            ser.serializeService(message, in);
            message.getPayload().rewind();
            out = ser.deserializeService(message);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(in.compareTo(out) == 0);

        reset();
    }

    /*
     * SerializeByte
     */

    @Test
    public void serializeByteTest() {
        byte in = 5;

        try {
            ser.serializeByte(message, "", in);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void deserializeByteTest() {
        byte in = 5, out = 0;

        try {
            ser.serializeByte(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeByte(message, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(in == out);

        reset();
    }

    /*
     * SerializeI16
     */

    @Test
    public void serializeI16Test() {
        short in = 5;

        try {
            ser.serializeI16(message, "", in);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void deserializeI16Test() {
        short in = 5, out = 0;

        try {
            ser.serializeI16(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeI16(message, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(in == out);

        reset();
    }

    /*
     * SerializeUI16
     */

    @Test
    public void serializeUI16Test() {
        short in = 5;

        try {
            ser.serializeUI16(message, "", in);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void deserializeUI16Test() {
        short in = 5, out = 0;

        try {
            ser.serializeUI16(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeUI16(message, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(in == out);

        reset();
    }

    /*
     * SerializeI32
     */

    @Test
    public void serializeI32Test() {
        int in = 5;

        try {
            ser.serializeI32(message, "", in);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void deserializeI32Test() {
        int in = 5, out = 0;

        try {
            ser.serializeI32(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeI32(message, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(in == out);

        reset();
    }

    /*
     * SerializeUI32
     */

    @Test
    public void serializeUI32Test() {
        int in = 5;

        try {
            ser.serializeI32(message, "", in);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void deserializeUI32Test() {
        int in = 5, out = 0;

        try {
            ser.serializeUI32(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeUI32(message, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(in == out);

        reset();
    }

    /*
     * SerializeI64
     */

    @Test
    public void serializeI64Test() {
        long in = 5;

        try {
            ser.serializeI64(message, "", in);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void deserializeI64Test() {
        long in = 5, out = 0;

        try {
            ser.serializeI64(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeI64(message, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(in == out);

        reset();
    }

    /*
     * SerializeUI64
     */

    @Test
    public void serializeUI64Test() {
        long in = 5;

        try {
            ser.serializeUI64(message, "", in);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void deserializeUI64Test() {
        long in = 5, out = 0;

        try {
            ser.serializeUI64(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeUI64(message, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(in == out);

        reset();
    }

    /*
     * SerializeFloat32
     */

    @Test
    public void serializeFloat32Test() {
        float in = (float) 5.0;

        try {
            ser.serializeFloat32(message, "", in);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void deserializeFloat32Test() {
        float in = (float) 5.0, out = (float) 0.0;

        try {
            ser.serializeFloat32(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeFloat32(message, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(in == out);

        reset();
    }

    /*
     * SerializeFloat64
     */

    @Test
    public void serializeFloat64Test() {
        double in = 5.0;

        try {
            ser.serializeFloat64(message, "", in);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void deserializeFloat64Test() {
        double in = 5.0, out = 0.0;

        try {
            ser.serializeFloat64(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeFloat64(message, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(in == out);

        reset();
    }

    /*
     * SerializeBoolean
     */

    @Test
    public void serializeBooleanTest() {
        boolean in = true;

        try {
            ser.serializeBoolean(message, "", in);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void deserializeBooleanTest() {
        boolean in = false, out = true;

        try {
            ser.serializeBoolean(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeBoolean(message, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(in == out);

        reset();
    }

    /*
     * Generic types
     */

    @Test
    public void serializeTest() {
        GenericType in = new GenericType(1, "one");

        try {
            ser.serialize(message, "", in);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void deserializeTest() {
        GenericType in = new GenericType(1, "one");
        GenericType out = new GenericType();

        try {
            ser.serialize(message, "", in);
            message.getPayload().rewind();
            out = ser.deserialize(message, "", GenericType.class);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(in.equals(out));

        reset();
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

    @Test
    public <T> void deserializeArrayCharTest() {
        ArrayList<Character> in = new ArrayList<Character>();
        in.add('a');
        in.add('e');
        in.add('i');
        in.add('o');
        in.add('u');
        List<T> out = null;
        
        try {
            ser.serializeArrayChar(message, "", in, 5);
            message.getPayload().rewind();
            out = ser.deserializeArrayChar(message, "", 5);
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

    @Test
    public void deserializeArrayByteTest() {
        List<Byte> in = new ArrayList<Byte>();
        in.add((byte) 5);
        in.add((byte) 6);
        in.add((byte) 7);
        in.add((byte) 8);
        in.add((byte) 9);
        List<Byte> out = new ArrayList<Byte>();;
        
        try {
            ser.serializeArrayByte(message, "", in, 5);
            message.getPayload().rewind();
            out = ser.deserializeArrayByte(message, "", 5);
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

    @Test
    public void deserializeArrayI16Test() {
        List<Short> in = new ArrayList<Short>();
        in.add((short) 5);
        in.add((short) 6);
        in.add((short) 7);
        in.add((short) 8);
        in.add((short) 9);
        List<Short> out = new ArrayList<Short>();
        
        try {
            ser.serializeArrayI16(message, "", in, 5);
            message.getPayload().rewind();
            out = ser.deserializeArrayI16(message, "", 5);
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

    @Test
    public void deserializeArrayUI16Test() {
        List<Short> in = new ArrayList<Short>();
        in.add((short) 5);
        in.add((short) 6);
        in.add((short) 7);
        in.add((short) 8);
        in.add((short) 9);
        List<Short> out = new ArrayList<Short>();;
        
        try {
            ser.serializeArrayUI16(message, "", in, 5);
            message.getPayload().rewind();
            out = ser.deserializeArrayUI16(message, "", 5);
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

    @Test
    public void deserializeArrayI32Test() {
        List<Integer> in = new ArrayList<Integer>();
        in.add((int) 5);
        in.add((int) 6);
        in.add((int) 7);
        in.add((int) 8);
        in.add((int) 9);
        List<Integer> out = new ArrayList<Integer>();;
        
        try {
            ser.serializeArrayI32(message, "", in, 5);
            message.getPayload().rewind();
            out = ser.deserializeArrayI32(message, "", 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

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

    @Test
    public void deserializeArrayUI32Test() {
        List<Integer> in = new ArrayList<Integer>();
        in.add((int) 5);
        in.add((int) 6);
        in.add((int) 7);
        in.add((int) 8);
        in.add((int) 9);
        List<Integer> out = new ArrayList<Integer>();;
        
        try {
            ser.serializeArrayUI32(message, "", in, 5);
            message.getPayload().rewind();
            out = ser.deserializeArrayUI32(message, "", 5);
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

    @Test
    public void deserializeArrayI64Test() {
        List<Long> in = new ArrayList<Long>();
        in.add((long) 5);
        in.add((long) 6);
        in.add((long) 7);
        in.add((long) 8);
        in.add((long) 9);
        List<Long> out = new ArrayList<Long>();;
        
        try {
            ser.serializeArrayI64(message, "", in, 5);
            message.getPayload().rewind();
            out = ser.deserializeArrayI64(message, "", 5);
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

    @Test
    public void deserializeArrayUI64Test() {
        List<Long> in = new ArrayList<Long>();
        in.add((long) 5);
        in.add((long) 6);
        in.add((long) 7);
        in.add((long) 8);
        in.add((long) 9);
        List<Long> out = new ArrayList<Long>();;
        
        try {
            ser.serializeArrayUI64(message, "", in, 5);
            message.getPayload().rewind();
            out = ser.deserializeArrayUI64(message, "", 5);
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

    @Test
    public void deserializeArrayFloat32Test() {
        List<Float> in = new ArrayList<Float>();
        in.add((float) 5.0);
        in.add((float) 6.1);
        in.add((float) 7.2);
        in.add((float) 8.3);
        in.add((float) 9.4);
        List<Float> out = new ArrayList<Float>();;
        
        try {
            ser.serializeArrayFloat32(message, "", in, 5);
            message.getPayload().rewind();
            out = ser.deserializeArrayFloat32(message, "", 5);
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

    @Test
    public void deserializeArrayFloat64Test() {
        List<Double> in = new ArrayList<Double>();
        in.add((double) 5.0);
        in.add((double) 6.1);
        in.add((double) 7.2);
        in.add((double) 8.3);
        in.add((double) 9.4);
        List<Double> out = new ArrayList<Double>();;
        
        try {
            ser.serializeArrayFloat64(message, "", in, 5);
            message.getPayload().rewind();
            out = ser.deserializeArrayFloat64(message, "", 5);
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

    @Test
    public void deserializeArrayBooleanTest() {
        List<Boolean> in = new ArrayList<Boolean>();
        in.add(false);
        in.add(true);
        in.add(true);
        in.add(false);
        in.add(true);
        List<Boolean> out = new ArrayList<Boolean>();;
        
        try {
            ser.serializeArrayBoolean(message, "", in, 5);
            message.getPayload().rewind();
            out = ser.deserializeArrayBoolean(message, "", 5);
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

    @Test
    public void deserializeArrayStringTest() {
        List<String> in = new ArrayList<String>();
        in.add("one");
        in.add("two");
        in.add("three");
        in.add("four");
        in.add("five");
        List<String> out = new ArrayList<String>();;
        
        try {
            ser.serializeArrayString(message, "", in, 5);
            message.getPayload().rewind();
            out = ser.deserializeArrayString(message, "", 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

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

    @Test
    public void deserializeArrayTest() {
        List<GenericType> in = new ArrayList<GenericType>();
        in.add(new GenericType(1, "one"));
        in.add(new GenericType(2, "two"));
        in.add(new GenericType(3, "three"));
        in.add(new GenericType(4, "four"));
        in.add(new GenericType(5, "five"));
        List<GenericType> out = new ArrayList<GenericType>();;
        
        try {
            ser.serializeArray(message, "", in, 5);
            message.getPayload().rewind();
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

        assertTrue(!error);

        reset();
    }

}
