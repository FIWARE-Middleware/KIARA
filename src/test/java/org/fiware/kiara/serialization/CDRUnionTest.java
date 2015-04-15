package org.fiware.kiara.serialization;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.fiware.kiara.serialization.impl.*;

import org.junit.Before;
import org.junit.Test;
import org.fiware.kiara.serialization.types.BooleanSwitchUnion;
import org.fiware.kiara.serialization.types.CharSwitchUnion;
import org.fiware.kiara.serialization.types.EnumSwitchUnion;
import org.fiware.kiara.serialization.types.EnumSwitcher;
import org.fiware.kiara.serialization.types.IntSwitchUnion;


public class CDRUnionTest {

    private CDRSerializer ser;

    @Before
    public void init() {
        this.ser = new CDRSerializer();
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
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serialize(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserialize(bis, "", IntSwitchUnion.class);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(in.equals(out));


    }

    @Test
    public void deserializeArrayIntSwitchUnionTest() {
        List<IntSwitchUnion> in = new ArrayList<>();
        for (int i=0; i < 5; ++i) {
            IntSwitchUnion obj = new IntSwitchUnion();
            obj._d(2);
            obj.setStringVal("MyString");
            in.add(obj);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<IntSwitchUnion>> s
                = new ListAsArraySerializer<>(5, new ObjectSerializer<>(IntSwitchUnion.class));

        List<IntSwitchUnion> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(in.equals(out));


    }

    @Test
    public void deserializeMultiDimArrayIntSwitchUnionTest() {
        List<List<IntSwitchUnion>> in = new ArrayList<>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<IntSwitchUnion> inner = new ArrayList<>(5);
            for (int j=0; j < 5; ++j) {
                IntSwitchUnion obj = new IntSwitchUnion();
                obj._d(2);
                obj.setStringVal("MyString");
                inner.add(obj);
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<List<IntSwitchUnion>>> s
                = new ListAsArraySerializer<>(3, new ListAsArraySerializer<>(5, new ObjectSerializer<>(IntSwitchUnion.class)));


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
    public void deserializeSequenceIntSwitchUnionTest() {
        List<IntSwitchUnion> in = new ArrayList<>();
        for (int i=0; i < 5; ++i) {
            IntSwitchUnion obj = new IntSwitchUnion();
            obj._d(2);
            obj.setStringVal("MyString");
            in.add(obj);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<IntSwitchUnion>> s
                = new ListAsSequenceSerializer<>(new ObjectSerializer<>(IntSwitchUnion.class));

        List<IntSwitchUnion> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(in.equals(out));


    }

    @Test
    public void deserializeMultiDimSequenceIntSwitchUnionTest() {
        List<List<IntSwitchUnion>> in = new ArrayList<>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<IntSwitchUnion> inner = new ArrayList<>(5);
            for (int j=0; j < 5; ++j) {
                IntSwitchUnion obj = new IntSwitchUnion();
                obj._d(2);
                obj.setStringVal("MyString");
                inner.add(obj);
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<List<IntSwitchUnion>>> s
                = new ListAsSequenceSerializer<>(new ListAsSequenceSerializer<>(new ObjectSerializer<>(IntSwitchUnion.class)));

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
    public void deserializeSetIntSwitchUnionTest() {
        Set<IntSwitchUnion> in = new HashSet<>();
        IntSwitchUnion obj;
        for(int i=0; i < 10; ++i) {
            obj = new IntSwitchUnion();
            obj._d(2);
            obj.setStringVal("MyString" + i);
            in.add(obj);
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<IntSwitchUnion>> s
                = new SetAsSetSerializer<>(new ObjectSerializer<>(IntSwitchUnion.class));

        Set<IntSwitchUnion> out = null;

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
    public void deserializeMultiDimSetIntSwitchUnionTest() {
        int c = 0;
        Set<Set<IntSwitchUnion>> in = new HashSet<>(2);
        for(int i=0; i < 2; ++i) {
            HashSet<IntSwitchUnion> inner = new HashSet<>(3);
            for (int j=0; j < 3; ++j) {
                IntSwitchUnion obj = new IntSwitchUnion();
                obj._d(2);
                obj.setStringVal("MyString" + i);
                inner.add(obj);
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<Set<IntSwitchUnion>>> s
                = new SetAsSetSerializer<>(new SetAsSetSerializer<>(new ObjectSerializer<>(IntSwitchUnion.class)));

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
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serialize(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserialize(bis, "", CharSwitchUnion.class);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(in.equals(out));


    }

    @Test
    public void deserializeArrayCharSwitchUnionTest() {
        List<CharSwitchUnion> in = new ArrayList<>();
        for (int i=0; i < 5; ++i) {
            CharSwitchUnion obj = new CharSwitchUnion();
            obj._d('3');
            obj.setStringVal("MyString");
            in.add(obj);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<CharSwitchUnion>> s
                = new ListAsArraySerializer<>(5, new ObjectSerializer<>(CharSwitchUnion.class));

        List<CharSwitchUnion> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(in.equals(out));


    }

    @Test
    public void deserializeMultiDimArrayCharSwitchUnionTest() {
        List<List<CharSwitchUnion>> in = new ArrayList<>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<CharSwitchUnion> inner = new ArrayList<>(5);
            for (int j=0; j < 5; ++j) {
                CharSwitchUnion obj = new CharSwitchUnion();
                obj._d('3');
                obj.setStringVal("MyString");
                inner.add(obj);
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<List<CharSwitchUnion>>> s
                = new ListAsArraySerializer<>(3, new ListAsArraySerializer<>(5, new ObjectSerializer<>(CharSwitchUnion.class)));

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
    public void deserializeSequenceCharSwitchUnionTest() {
        List<CharSwitchUnion> in = new ArrayList<>();
        for (int i=0; i < 5; ++i) {
            CharSwitchUnion obj = new CharSwitchUnion();
            obj._d('3');
            obj.setStringVal("MyString");
            in.add(obj);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<CharSwitchUnion>> s
                = new ListAsSequenceSerializer<>(new ObjectSerializer<>(CharSwitchUnion.class));

        List<CharSwitchUnion> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(in.equals(out));


    }

    @Test
    public void deserializeMultiDimSequenceCharSwitchUnionTest() {
        List<List<CharSwitchUnion>> in = new ArrayList<>(3);
        for(int i=0; i < 3; ++i) {
            List<CharSwitchUnion> inner = new ArrayList<>(5);
            for (int j=0; j < 5; ++j) {
                CharSwitchUnion obj = new CharSwitchUnion();
                obj._d('3');
                obj.setStringVal("MyString");
                inner.add(obj);
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<List<CharSwitchUnion>>> s
                = new ListAsSequenceSerializer<>(
                    new ListAsSequenceSerializer<>(new ObjectSerializer<>(CharSwitchUnion.class)));

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
    public void deserializeSetCharSwitchUnionTest() {
        Set<CharSwitchUnion> in = new HashSet<>();
        CharSwitchUnion obj;
        for(int i=0; i < 10; ++i) {
            obj = new CharSwitchUnion();
            obj._d('3');
            obj.setStringVal("MyString" + i);
            in.add(obj);
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<CharSwitchUnion>> s
                = new SetAsSetSerializer<>(new ObjectSerializer<>(CharSwitchUnion.class));

        Set<CharSwitchUnion> out = null;

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
    public void deserializeMultiDimSetCharSwitchUnionTest() {
        int c = 0;
        Set<Set<CharSwitchUnion>> in = new HashSet<>(2);
        for(int i=0; i < 2; ++i) {
            HashSet<CharSwitchUnion> inner = new HashSet<>(3);
            for (int j=0; j < 3; ++j) {
                CharSwitchUnion obj = new CharSwitchUnion();
                obj._d('3');
                obj.setStringVal("MyString" + i);
                inner.add(obj);
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<Set<CharSwitchUnion>>> s
                = new SetAsSetSerializer<>(new SetAsSetSerializer<>(new ObjectSerializer<>(CharSwitchUnion.class)));

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
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serialize(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserialize(bis, "", BooleanSwitchUnion.class);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(in.equals(out));


    }

    @Test
    public void deserializeArrayBooleanSwitchUnionTest() {
        List<BooleanSwitchUnion> in = new ArrayList<>();
        for (int i=0; i < 5; ++i) {
            BooleanSwitchUnion obj = new BooleanSwitchUnion();
            obj._d(false);
            obj.setStringVal("MyString");
            in.add(obj);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<BooleanSwitchUnion>> s
                = new ListAsArraySerializer<>(5, new ObjectSerializer<>(BooleanSwitchUnion.class));

        List<BooleanSwitchUnion> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(in.equals(out));


    }

    @Test
    public void deserializeMultiDimArrayBooleanSwitchUnionTest() {
        List<List<BooleanSwitchUnion>> in = new ArrayList<>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<BooleanSwitchUnion> inner = new ArrayList<>(5);
            for (int j=0; j < 5; ++j) {
                BooleanSwitchUnion obj = new BooleanSwitchUnion();
                obj._d(false);
                obj.setStringVal("MyString");
                inner.add(obj);
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<List<BooleanSwitchUnion>>> s
                = new ListAsArraySerializer<>(3, new ListAsArraySerializer<>(5, new ObjectSerializer<>(BooleanSwitchUnion.class)));

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
    public void deserializeSequenceBooleanSwitchUnionTest() {
        List<BooleanSwitchUnion> in = new ArrayList<>();
        for (int i=0; i < 5; ++i) {
            BooleanSwitchUnion obj = new BooleanSwitchUnion();
            obj._d(false);
            obj.setStringVal("MyString");
            in.add(obj);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<BooleanSwitchUnion>> s
                = new ListAsSequenceSerializer<>(new ObjectSerializer<>(BooleanSwitchUnion.class));

        List<BooleanSwitchUnion> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(in.equals(out));


    }

    @Test
    public void deserializeMultiDimSequenceBooleanSwitchUnionTest() {
        List<List<BooleanSwitchUnion>> in = new ArrayList<>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<BooleanSwitchUnion> inner = new ArrayList<>(5);
            for (int j=0; j < 5; ++j) {
                BooleanSwitchUnion obj = new BooleanSwitchUnion();
                obj._d(false);
                obj.setStringVal("MyString");
                inner.add(obj);
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<List<BooleanSwitchUnion>>> s
                = new ListAsSequenceSerializer<>(new ListAsSequenceSerializer<>(new ObjectSerializer<>(BooleanSwitchUnion.class)));

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
    public void deserializeSetBooleanSwitchUnionTest() {
        Set<BooleanSwitchUnion> in = new HashSet<>();
        BooleanSwitchUnion obj;
        for(int i=0; i < 10; ++i) {
            obj = new BooleanSwitchUnion();
            obj._d(false);
            obj.setStringVal("MyString" + i);
            in.add(obj);
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<BooleanSwitchUnion>> s
                = new SetAsSetSerializer<>(new ObjectSerializer<>(BooleanSwitchUnion.class));

        Set<BooleanSwitchUnion> out = null;

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
    public void deserializeMultiDimSetBooleanSwitchUnionTest() {
        int c = 0;
        Set<Set<BooleanSwitchUnion>> in = new HashSet<>(2);
        for(int i=0; i < 2; ++i) {
            HashSet<BooleanSwitchUnion> inner = new HashSet<>(3);
            for (int j=0; j < 3; ++j) {
                BooleanSwitchUnion obj = new BooleanSwitchUnion();
                obj._d(false);
                obj.setStringVal("MyString" + i);
                inner.add(obj);
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<Set<BooleanSwitchUnion>>> s
                = new SetAsSetSerializer<>(new SetAsSetSerializer<>(new ObjectSerializer<>(BooleanSwitchUnion.class)));

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
            BinaryOutputStream bos = new BinaryOutputStream();
            ser.serialize(bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = ser.deserialize(bis, "", EnumSwitchUnion.class);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(in.equals(out));


    }

    @Test
    public void deserializeArrayEnumSwitchUnionTest() {
        List<EnumSwitchUnion> in = new ArrayList<>();
        for (int i=0; i < 5; ++i) {
            EnumSwitchUnion obj = new EnumSwitchUnion();
            obj._d(EnumSwitcher.option_3);
            obj.setStringVal("MyString");
            in.add(obj);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<EnumSwitchUnion>> s
                = new ListAsArraySerializer<>(5, new ObjectSerializer<>(EnumSwitchUnion.class));

        List<EnumSwitchUnion> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(in.equals(out));


    }

    @Test
    public void deserializeMultiDimArrayEnumSwitchUnionTest() {
        List<List<EnumSwitchUnion>> in = new ArrayList<>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<EnumSwitchUnion> inner = new ArrayList<>(5);
            for (int j=0; j < 5; ++j) {
                EnumSwitchUnion obj = new EnumSwitchUnion();
                obj._d(EnumSwitcher.option_3);
                obj.setStringVal("MyString");
                inner.add(obj);
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<List<EnumSwitchUnion>>> s
                = new ListAsArraySerializer<>(3, new ListAsArraySerializer<>(5, new ObjectSerializer<>(EnumSwitchUnion.class)));

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
    public void deserializeSequenceEnumSwitchUnionTest() {
        List<EnumSwitchUnion> in = new ArrayList<>();
        for (int i=0; i < 5; ++i) {
            EnumSwitchUnion obj = new EnumSwitchUnion();
            obj._d(EnumSwitcher.option_3);
            obj.setStringVal("MyString");
            in.add(obj);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<EnumSwitchUnion>> s
                = new ListAsSequenceSerializer<>(new ObjectSerializer<>(EnumSwitchUnion.class));

        List<EnumSwitchUnion> out = null;

        try {
            BinaryOutputStream bos = new BinaryOutputStream();
            s.write(ser, bos, "", in);
            BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
            out = s.read(ser, bis, "");
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(in.equals(out));


    }

    @Test
    public void deserializeMultiDimSequenceEnumSwitchUnionTest() {
        List<List<EnumSwitchUnion>> in = new ArrayList<>(3);
        for(int i=0; i < 3; ++i) {
            ArrayList<EnumSwitchUnion> inner = new ArrayList<>(5);
            for (int j=0; j < 5; ++j) {
                EnumSwitchUnion obj = new EnumSwitchUnion();
                obj._d(EnumSwitcher.option_3);
                obj.setStringVal("MyString");
                inner.add(obj);
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<List<List<EnumSwitchUnion>>> s
                = new ListAsSequenceSerializer<>(new ListAsSequenceSerializer<>(new ObjectSerializer<>(EnumSwitchUnion.class)));

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
    public void deserializeSetEnumSwitchUnionTest() {
        Set<EnumSwitchUnion> in = new HashSet<>();
        EnumSwitchUnion obj;
        for(int i=0; i < 10; ++i) {
            obj = new EnumSwitchUnion();
            obj._d(EnumSwitcher.option_3);
            obj.setStringVal("MyString" + i);
            in.add(obj);
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<EnumSwitchUnion>> s
                = new SetAsSetSerializer<>(new ObjectSerializer<>(EnumSwitchUnion.class));

        Set<EnumSwitchUnion> out = null;

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
    public void deserializeMultiDimSetEnumSwitchUnionTest() {
        int c = 0;
        Set<Set<EnumSwitchUnion>> in = new HashSet<>(2);
        for(int i=0; i < 2; ++i) {
            HashSet<EnumSwitchUnion> inner = new HashSet<>(3);
            for (int j=0; j < 3; ++j) {
                EnumSwitchUnion obj = new EnumSwitchUnion();
                obj._d(EnumSwitcher.option_3);
                obj.setStringVal("MyString" + i);
                inner.add(obj);
            }
            in.add(inner);
        }

        org.fiware.kiara.serialization.impl.Serializer<Set<Set<EnumSwitchUnion>>> s
                = new SetAsSetSerializer<>(new SetAsSetSerializer<>(new ObjectSerializer<>(EnumSwitchUnion.class)));

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
