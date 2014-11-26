package com.kiara.serialization.impl;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Vector;

import com.kiara.netty.TransportMessageDispatcher;
import com.kiara.serialization.Serializable;
import com.kiara.serialization.Serializer;
import com.kiara.transport.impl.TransportMessage;

public interface SerializerImpl extends Serializer {

    public Object getNewMessageId();

    public void serializeMessageId(TransportMessage message, Object messageId);

    public Object deserializeMessageId(TransportMessage message);

    public boolean equalMessageIds(Object id1, Object id2);

    /*
     * Services
     */

    public void serializeService(TransportMessage message, String service);

    public String deserializeService(TransportMessage message);

    /*
     * Operations
     */

    public void serializeOperation(TransportMessage message, String operation);

    public String deserializeOperation(TransportMessage message);

    /*
     * Basic Types
     */

    public void serializeChar(TransportMessage message, String name, char value);

    public char deserializeChar(TransportMessage message, String name);

    public void serializeByte(TransportMessage message, String name, byte value);

    public byte deserializeByte(TransportMessage message, String name);


    public void serializeI16(TransportMessage message, String name, short value);

    public short deserializeI16(TransportMessage message, String name);

    public void serializeUI16(TransportMessage message, String name, short value);

    public short deserializeUI16(TransportMessage message, String name);


    public void serializeI32(TransportMessage message, String name, int value);

    public int deserializeI32(TransportMessage message, String name);

    public void serializeUI32(TransportMessage message, String name, int value);

    public int deserializeUI32(TransportMessage message, String name);


    public void serializeI64(TransportMessage message, String name, long value);

    public long deserializeI64(TransportMessage message, String name);

    public void serializeUI64(TransportMessage message, String name, long value);

    public long deserializeUI64(TransportMessage message, String name);


    public void serializeFloat32(TransportMessage message, String name, float value);

    public float deserializeFloat32(TransportMessage message, String name);

    public void serializeFloat64(TransportMessage message, String name, double value);

    public double deserializeFloat64(TransportMessage message, String name);


    public void serializeBoolean(TransportMessage message, String name, boolean value);

    public boolean deserializeBoolean(TransportMessage message, String name);


    public void serializeString(TransportMessage message, String data);

    public String deserializeString(TransportMessage message);

    /*
     * Generic types
     */

    public <T extends Serializable> void serialize(TransportMessage message, String name, T value);

    public <T extends Serializable> T deserialize(TransportMessage message, String name, Class<T> example) throws InstantiationException, IllegalAccessException;

    /*
     * Arrays
     */

    public void serializeArrayChar(TransportMessage message, String name, ArrayList<Character> array);

    public ArrayList<Character> deserializeArrayChar(TransportMessage message, String name, int length);

    public void serializeArrayByte(TransportMessage message, String name, ArrayList<Byte> array);

    public ArrayList<Byte> deserializeArrayByte(TransportMessage message, String name, int length);

    public void serializeArrayI16(TransportMessage message, String name, ArrayList<Short> array);

    public ArrayList<Short> deserializeArrayI16(TransportMessage message, String name, int length);

    public void serializeArrayUI16(TransportMessage message, String name, ArrayList<Short> array);

    public ArrayList<Short> deserializeArrayUI16(TransportMessage message, String name, int length);

    public void serializeArrayI32(TransportMessage message, String name, ArrayList<Integer> array);

    public ArrayList<Integer> deserializeArrayI32(TransportMessage message, String name, int length);

    public void serializeArrayUI32(TransportMessage message, String name, ArrayList<Integer> array);

    public ArrayList<Integer> deserializeArrayUI32(TransportMessage message, String name, int length);

    public void serializeArrayI64(TransportMessage message, String name, ArrayList<Long> array);

    public ArrayList<Long> deserializeArrayI64(TransportMessage message, String name, int length);

    public void serializeArrayUI64(TransportMessage message, String name, ArrayList<Long> array);

    public ArrayList<Long> deserializeArrayUI64(TransportMessage message, String name, int length);

    public void serializeArrayFloat32(TransportMessage message, String name, ArrayList<Float> array);

    public ArrayList<Float> deserializeArrayFloat32(TransportMessage message, String name, int length);

    public void serializeArrayFloat64(TransportMessage message, String name, ArrayList<Double> array);

    public ArrayList<Double> deserializeArrayFloat64(TransportMessage message, String name, int length);

    public void serializeArrayBoolean(TransportMessage message, String name, ArrayList<Boolean> array);

    public ArrayList<Boolean> deserializeArrayBoolean(TransportMessage message, String name, int length);

    public void serializeArrayString(TransportMessage message, String name, ArrayList<String> array);

    public ArrayList<String> deserializeArrayString(TransportMessage message, String name, int length);

    /*
     * Array of generic types
     */

    public <T extends Serializable> void serializeArray(TransportMessage message, String name, ArrayList<T> array);

    public <T extends Serializable> ArrayList<T> deserializeArray(TransportMessage message, String name, Class<T> example, int length) throws InstantiationException, IllegalAccessException;

    public void serializeArrayBegin(TransportMessage message, String name, int length);

    public void serializeArrayEnd(TransportMessage message, String name);

    public int deserializeArrayBegin(TransportMessage message, String name);

    public void deserializeArrayEnd(TransportMessage message, String name);





}
