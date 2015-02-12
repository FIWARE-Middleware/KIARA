package org.fiware.kiara.serialization;

import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.fiware.kiara.serialization.impl.CDRSerializer;
import org.fiware.kiara.serialization.types.BooleanSwitchUnion;
import org.fiware.kiara.serialization.types.CharSwitchUnion;
import org.fiware.kiara.serialization.types.EnumSwitchUnion;
import org.fiware.kiara.serialization.types.EnumSwitcher;
import org.fiware.kiara.serialization.types.GenericType;
import org.fiware.kiara.serialization.types.IntSwitchUnion;
import org.fiware.kiara.transport.impl.TransportMessage;


public class CDRUnionTest {
    
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
     * IntUnionTest
     */
    
    @Test
    public void deserializeIntSwitchUnionTest() {
        IntSwitchUnion in = new IntSwitchUnion();
        in._d(2);
        in.setStringVal("MyString");
        
        IntSwitchUnion out = null;

        try {
            ser.serialize(message, "", in);
            message.getPayload().rewind();
            out = ser.deserialize(message, "", IntSwitchUnion.class);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(in.equals(out));

        reset();
    }
    
    @Test
    public void deserializeArrayIntSwitchUnionTest() {
        List<IntSwitchUnion> in = new ArrayList<IntSwitchUnion>();
        for (int i=0; i < 5; ++i) {
            IntSwitchUnion obj = new IntSwitchUnion();
            obj._d(2);
            obj.setStringVal("MyString");
            in.add(obj);
        }
        
        List<IntSwitchUnion> out = null;

        try {
            ser.serializeArray(message, "", in, 5);
            message.getPayload().rewind();
            out = ser.deserializeArray(message, "", IntSwitchUnion.class, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(in.equals(out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimArrayIntSwitchUnionTest() {
        List<ArrayList<IntSwitchUnion>> in = new ArrayList<ArrayList<IntSwitchUnion>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<IntSwitchUnion> inner = new ArrayList<IntSwitchUnion>(5);
            for (int j=0; j < 5; ++j) {
                IntSwitchUnion obj = new IntSwitchUnion();
                obj._d(2);
                obj.setStringVal("MyString");
                inner.add(obj);
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeArray(message, "", in, 3, 5);
            message.getPayload().rewind();
            out = ser.deserializeArray(message, "", IntSwitchUnion.class, 3, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public void deserializeSequenceIntSwitchUnionTest() {
        List<IntSwitchUnion> in = new ArrayList<IntSwitchUnion>();
        for (int i=0; i < 5; ++i) {
            IntSwitchUnion obj = new IntSwitchUnion();
            obj._d(2);
            obj.setStringVal("MyString");
            in.add(obj);
        }
        
        List<IntSwitchUnion> out = null;

        try {
            ser.serializeSequence(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequence(message, "", IntSwitchUnion.class, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(in.equals(out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSequenceIntSwitchUnionTest() {
        List<ArrayList<IntSwitchUnion>> in = new ArrayList<ArrayList<IntSwitchUnion>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<IntSwitchUnion> inner = new ArrayList<IntSwitchUnion>(5);
            for (int j=0; j < 5; ++j) {
                IntSwitchUnion obj = new IntSwitchUnion();
                obj._d(2);
                obj.setStringVal("MyString");
                inner.add(obj);
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeSequence(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequence(message, "", IntSwitchUnion.class, 2);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeSetIntSwitchUnionTest() {
        Set<IntSwitchUnion> in = new HashSet<IntSwitchUnion>();
        IntSwitchUnion obj;
        for(int i=0; i < 10; ++i) {
            obj = new IntSwitchUnion();
            obj._d(2);
            obj.setStringVal("MyString" + i);
            in.add(obj);
        }

        Set<IntSwitchUnion> out = null;
        
        try {
            ser.serializeSet(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSet(message, "", IntSwitchUnion.class, 1);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
        
        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSetIntSwitchUnionTest() {
        int c = 0;
        Set<HashSet<IntSwitchUnion>> in = new HashSet<HashSet<IntSwitchUnion>>(2);
        for(int i=0; i < 2; ++i) {
            HashSet<IntSwitchUnion> inner = new HashSet<IntSwitchUnion>(3);
            for (int j=0; j < 3; ++j) {
                IntSwitchUnion obj = new IntSwitchUnion();
                obj._d(2);
                obj.setStringVal("MyString" + i);
                inner.add(obj);
            }
            in.add(inner);
        }
        
        Set<T> out = null;
        
        try {
            ser.serializeSet(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSet(message, "", IntSwitchUnion.class, 2);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
        
        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    /*
     * CharUnionTest
     */
    
    @Test
    public void deserializeCharSwitchUnionTest() {
        CharSwitchUnion in = new CharSwitchUnion();
        in._d('3');
        in.setStringVal("MyString");
        
        CharSwitchUnion out = null;

        try {
            ser.serialize(message, "", in);
            message.getPayload().rewind();
            out = ser.deserialize(message, "", CharSwitchUnion.class);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(in.equals(out));

        reset();
    }
    
    @Test
    public void deserializeArrayCharSwitchUnionTest() {
        List<CharSwitchUnion> in = new ArrayList<CharSwitchUnion>();
        for (int i=0; i < 5; ++i) {
            CharSwitchUnion obj = new CharSwitchUnion();
            obj._d('3');
            obj.setStringVal("MyString");
            in.add(obj);
        }
        
        List<CharSwitchUnion> out = null;

        try {
            ser.serializeArray(message, "", in, 5);
            message.getPayload().rewind();
            out = ser.deserializeArray(message, "", CharSwitchUnion.class, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(in.equals(out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimArrayCharSwitchUnionTest() {
        List<ArrayList<CharSwitchUnion>> in = new ArrayList<ArrayList<CharSwitchUnion>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<CharSwitchUnion> inner = new ArrayList<CharSwitchUnion>(5);
            for (int j=0; j < 5; ++j) {
                CharSwitchUnion obj = new CharSwitchUnion();
                obj._d('3');
                obj.setStringVal("MyString");
                inner.add(obj);
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeArray(message, "", in, 3, 5);
            message.getPayload().rewind();
            out = ser.deserializeArray(message, "", CharSwitchUnion.class, 3, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public void deserializeSequenceCharSwitchUnionTest() {
        List<CharSwitchUnion> in = new ArrayList<CharSwitchUnion>();
        for (int i=0; i < 5; ++i) {
            CharSwitchUnion obj = new CharSwitchUnion();
            obj._d('3');
            obj.setStringVal("MyString");
            in.add(obj);
        }
        
        List<CharSwitchUnion> out = null;

        try {
            ser.serializeSequence(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequence(message, "", CharSwitchUnion.class, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(in.equals(out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSequenceCharSwitchUnionTest() {
        List<ArrayList<CharSwitchUnion>> in = new ArrayList<ArrayList<CharSwitchUnion>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<CharSwitchUnion> inner = new ArrayList<CharSwitchUnion>(5);
            for (int j=0; j < 5; ++j) {
                CharSwitchUnion obj = new CharSwitchUnion();
                obj._d('3');
                obj.setStringVal("MyString");
                inner.add(obj);
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeSequence(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequence(message, "", CharSwitchUnion.class, 2);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeSetCharSwitchUnionTest() {
        Set<CharSwitchUnion> in = new HashSet<CharSwitchUnion>();
        CharSwitchUnion obj;
        for(int i=0; i < 10; ++i) {
            obj = new CharSwitchUnion();
            obj._d('3');
            obj.setStringVal("MyString" + i);
            in.add(obj);
        }

        Set<CharSwitchUnion> out = null;
        
        try {
            ser.serializeSet(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSet(message, "", CharSwitchUnion.class, 1);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
        
        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSetCharSwitchUnionTest() {
        int c = 0;
        Set<HashSet<CharSwitchUnion>> in = new HashSet<HashSet<CharSwitchUnion>>(2);
        for(int i=0; i < 2; ++i) {
            HashSet<CharSwitchUnion> inner = new HashSet<CharSwitchUnion>(3);
            for (int j=0; j < 3; ++j) {
                CharSwitchUnion obj = new CharSwitchUnion();
                obj._d('3');
                obj.setStringVal("MyString" + i);
                inner.add(obj);
            }
            in.add(inner);
        }
        
        Set<T> out = null;
        
        try {
            ser.serializeSet(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSet(message, "", CharSwitchUnion.class, 2);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
        
        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    
    /*
     * BooleanUnionTest
     */
    
    @Test
    public void deserializeBooleanSwitchUnionTest() {
        BooleanSwitchUnion in = new BooleanSwitchUnion();
        in._d(false);
        in.setStringVal("MyString");
        
        BooleanSwitchUnion out = null;

        try {
            ser.serialize(message, "", in);
            message.getPayload().rewind();
            out = ser.deserialize(message, "", BooleanSwitchUnion.class);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(in.equals(out));

        reset();
    }
    
    @Test
    public void deserializeArrayBooleanSwitchUnionTest() {
        List<BooleanSwitchUnion> in = new ArrayList<BooleanSwitchUnion>();
        for (int i=0; i < 5; ++i) {
            BooleanSwitchUnion obj = new BooleanSwitchUnion();
            obj._d(false);
            obj.setStringVal("MyString");
            in.add(obj);
        }
        
        List<BooleanSwitchUnion> out = null;

        try {
            ser.serializeArray(message, "", in, 5);
            message.getPayload().rewind();
            out = ser.deserializeArray(message, "", BooleanSwitchUnion.class, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(in.equals(out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimArrayBooleanSwitchUnionTest() {
        List<ArrayList<BooleanSwitchUnion>> in = new ArrayList<ArrayList<BooleanSwitchUnion>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<BooleanSwitchUnion> inner = new ArrayList<BooleanSwitchUnion>(5);
            for (int j=0; j < 5; ++j) {
                BooleanSwitchUnion obj = new BooleanSwitchUnion();
                obj._d(false);
                obj.setStringVal("MyString");
                inner.add(obj);
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeArray(message, "", in, 3, 5);
            message.getPayload().rewind();
            out = ser.deserializeArray(message, "", BooleanSwitchUnion.class, 3, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public void deserializeSequenceBooleanSwitchUnionTest() {
        List<BooleanSwitchUnion> in = new ArrayList<BooleanSwitchUnion>();
        for (int i=0; i < 5; ++i) {
            BooleanSwitchUnion obj = new BooleanSwitchUnion();
            obj._d(false);
            obj.setStringVal("MyString");
            in.add(obj);
        }
        
        List<BooleanSwitchUnion> out = null;

        try {
            ser.serializeSequence(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequence(message, "", BooleanSwitchUnion.class, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(in.equals(out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSequenceBooleanSwitchUnionTest() {
        List<ArrayList<BooleanSwitchUnion>> in = new ArrayList<ArrayList<BooleanSwitchUnion>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<BooleanSwitchUnion> inner = new ArrayList<BooleanSwitchUnion>(5);
            for (int j=0; j < 5; ++j) {
                BooleanSwitchUnion obj = new BooleanSwitchUnion();
                obj._d(false);
                obj.setStringVal("MyString");
                inner.add(obj);
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeSequence(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequence(message, "", BooleanSwitchUnion.class, 2);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeSetBooleanSwitchUnionTest() {
        Set<BooleanSwitchUnion> in = new HashSet<BooleanSwitchUnion>();
        BooleanSwitchUnion obj;
        for(int i=0; i < 10; ++i) {
            obj = new BooleanSwitchUnion();
            obj._d(false);
            obj.setStringVal("MyString" + i);
            in.add(obj);
        }

        Set<BooleanSwitchUnion> out = null;
        
        try {
            ser.serializeSet(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSet(message, "", BooleanSwitchUnion.class, 1);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
        
        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSetBooleanSwitchUnionTest() {
        int c = 0;
        Set<HashSet<BooleanSwitchUnion>> in = new HashSet<HashSet<BooleanSwitchUnion>>(2);
        for(int i=0; i < 2; ++i) {
            HashSet<BooleanSwitchUnion> inner = new HashSet<BooleanSwitchUnion>(3);
            for (int j=0; j < 3; ++j) {
                BooleanSwitchUnion obj = new BooleanSwitchUnion();
                obj._d(false);
                obj.setStringVal("MyString" + i);
                inner.add(obj);
            }
            in.add(inner);
        }
        
        Set<T> out = null;
        
        try {
            ser.serializeSet(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSet(message, "", BooleanSwitchUnion.class, 2);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
        
        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    /*
     * EnumUnionTest
     */
    
    @Test
    public void deserializeEnumSwitchUnionTest() {
        EnumSwitchUnion in = new EnumSwitchUnion();
        in._d(EnumSwitcher.option_3);
        in.setStringVal("MyString");
        
        EnumSwitchUnion out = null;

        try {
            ser.serialize(message, "", in);
            message.getPayload().rewind();
            out = ser.deserialize(message, "", EnumSwitchUnion.class);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(in.equals(out));

        reset();
    }
    
    @Test
    public void deserializeArrayEnumSwitchUnionTest() {
        List<EnumSwitchUnion> in = new ArrayList<EnumSwitchUnion>();
        for (int i=0; i < 5; ++i) {
            EnumSwitchUnion obj = new EnumSwitchUnion();
            obj._d(EnumSwitcher.option_3);
            obj.setStringVal("MyString");
            in.add(obj);
        }
        
        List<EnumSwitchUnion> out = null;

        try {
            ser.serializeArray(message, "", in, 5);
            message.getPayload().rewind();
            out = ser.deserializeArray(message, "", EnumSwitchUnion.class, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(in.equals(out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimArrayEnumSwitchUnionTest() {
        List<ArrayList<EnumSwitchUnion>> in = new ArrayList<ArrayList<EnumSwitchUnion>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<EnumSwitchUnion> inner = new ArrayList<EnumSwitchUnion>(5);
            for (int j=0; j < 5; ++j) {
                EnumSwitchUnion obj = new EnumSwitchUnion();
                obj._d(EnumSwitcher.option_3);
                obj.setStringVal("MyString");
                inner.add(obj);
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeArray(message, "", in, 3, 5);
            message.getPayload().rewind();
            out = ser.deserializeArray(message, "", EnumSwitchUnion.class, 3, 5);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public void deserializeSequenceEnumSwitchUnionTest() {
        List<EnumSwitchUnion> in = new ArrayList<EnumSwitchUnion>();
        for (int i=0; i < 5; ++i) {
            EnumSwitchUnion obj = new EnumSwitchUnion();
            obj._d(EnumSwitcher.option_3);
            obj.setStringVal("MyString");
            in.add(obj);
        }
        
        List<EnumSwitchUnion> out = null;

        try {
            ser.serializeSequence(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequence(message, "", EnumSwitchUnion.class, 1);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(in.equals(out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSequenceEnumSwitchUnionTest() {
        List<ArrayList<EnumSwitchUnion>> in = new ArrayList<ArrayList<EnumSwitchUnion>>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<EnumSwitchUnion> inner = new ArrayList<EnumSwitchUnion>(5);
            for (int j=0; j < 5; ++j) {
                EnumSwitchUnion obj = new EnumSwitchUnion();
                obj._d(EnumSwitcher.option_3);
                obj.setStringVal("MyString");
                inner.add(obj);
            }
            in.add(inner);
        }

        List<T> out = null;
        
        try {
            ser.serializeSequence(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSequence(message, "", EnumSwitchUnion.class, 2);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeSetEnumSwitchUnionTest() {
        Set<EnumSwitchUnion> in = new HashSet<EnumSwitchUnion>();
        EnumSwitchUnion obj;
        for(int i=0; i < 10; ++i) {
            obj = new EnumSwitchUnion();
            obj._d(EnumSwitcher.option_3);
            obj.setStringVal("MyString" + i);
            in.add(obj);
        }

        Set<EnumSwitchUnion> out = null;
        
        try {
            ser.serializeSet(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSet(message, "", EnumSwitchUnion.class, 1);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
        
        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    @Test
    public <T> void deserializeMultiDimSetEnumSwitchUnionTest() {
        int c = 0;
        Set<HashSet<EnumSwitchUnion>> in = new HashSet<HashSet<EnumSwitchUnion>>(2);
        for(int i=0; i < 2; ++i) {
            HashSet<EnumSwitchUnion> inner = new HashSet<EnumSwitchUnion>(3);
            for (int j=0; j < 3; ++j) {
                EnumSwitchUnion obj = new EnumSwitchUnion();
                obj._d(EnumSwitcher.option_3);
                obj.setStringVal("MyString" + i);
                inner.add(obj);
            }
            in.add(inner);
        }
        
        Set<T> out = null;
        
        try {
            ser.serializeSet(message, "", in);
            message.getPayload().rewind();
            out = ser.deserializeSet(message, "", EnumSwitchUnion.class, 2);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
        
        assertTrue(Objects.deepEquals(in, out));

        reset();
    }
    
    
    
    
    
}
