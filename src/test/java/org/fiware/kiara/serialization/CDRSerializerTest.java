package org.fiware.kiara.serialization;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.fiware.kiara.serialization.impl.CDRSerializer;
import org.fiware.kiara.transport.impl.TransportMessage;

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

}
