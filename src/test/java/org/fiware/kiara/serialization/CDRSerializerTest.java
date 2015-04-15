package org.fiware.kiara.serialization;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;

import org.fiware.kiara.serialization.impl.CDRSerializer;
import org.fiware.kiara.serialization.types.GenericEnumeration;
import org.fiware.kiara.serialization.types.GenericType;
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
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeChar(bos, "MyChar", in);
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
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeChar(bos, "MyChar", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeChar(bis, "MyChar");
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
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeService(bos, in);
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
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeService(bos, in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeService(bis);
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
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeService(bos, in);
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
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeService(bos, in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeService(bis);
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
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeByte(bos, "", in);
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
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeByte(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeByte(bis, "");
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
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeI16(bos, "", in);
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
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeI16(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeI16(bis, "");
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
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeUI16(bos, "", in);
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
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeUI16(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeUI16(bis, "");
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
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeI32(bos, "", in);
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
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeI32(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeI32(bis, "");
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
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeI32(bos, "", in);
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
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeUI32(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeUI32(bis, "");
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
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeI64(bos, "", in);
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
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeI64(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeI64(bis, "");
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
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeUI64(bos, "", in);
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
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeUI64(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeUI64(bis, "");
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
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeFloat32(bos, "", in);
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
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeFloat32(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeFloat32(bis, "");
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
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeFloat64(bos, "", in);
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
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeFloat64(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeFloat64(bis, "");
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
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeBoolean(bos, "", in);
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
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeBoolean(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeBoolean(bis, "");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(in == out);

        reset();
    }

    /*
     * Enumeration types
     */
    
    @Test
    public void serializeEnumTest() {
        GenericEnumeration in = GenericEnumeration.second_val;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeEnum(bos, "", in);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);

        reset();
    }

    @Test
    public void deserializeEnumTest() {
        GenericEnumeration in= GenericEnumeration.second_val;
        GenericEnumeration out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serializeEnum(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserializeEnum(bis, "", GenericEnumeration.class);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(in.equals(out));

        reset();
    }
    
    /*
     * Generic types
     */

    @Test
    public void serializeTest() {
        GenericType in = new GenericType(1, "one");

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serialize(bos, "", in);
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
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serialize(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserialize(bis, "", GenericType.class);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(in.equals(out));

        reset();
    }
    
   
    
}
