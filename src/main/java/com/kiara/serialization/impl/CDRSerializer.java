package com.kiara.serialization.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.reflect.TypeToken;
import com.kiara.transport.impl.TransportMessage;

public class CDRSerializer implements SerializerImpl {

    private final AtomicInteger nextId;

    public CDRSerializer() {
        nextId = new AtomicInteger(1);
    }

    public Object getNewMessageId() {
        return nextId.getAndIncrement();
    }

    public boolean equalMessageIds(Object id1, Object id2) {
        if (id1 == id2) {
            return true;
        }
        if (id1 == null || id2 == null) {
            return false;
        }
        return id1.equals(id2);
    }

    public void serializeMessageId(TransportMessage message, Object messageId) {
        serializeI32(message, "",  (Integer) messageId);
    }

    public Object deserializeMessageId(TransportMessage message) {
        final int id = deserializeI32(message, "");
        return id;
    }

    public void serializeService(TransportMessage message, String service) {
        this.serializeString(message, service);
    }

    public String deserializeService(TransportMessage message) {
        return this.deserializeString(message);
    }

    public void serializeOperation(TransportMessage message, String operation) {
        this.serializeString(message, operation);
    }

    public String deserializeOperation(TransportMessage message) {
        return this.deserializeString(message);
    }

    /*public <T> void serialize(TransportMessage message, String name, T value) {
        System.out.println("generic");

    }

    public <T> T deserialize(TransportMessage message, String name, T value) {
        return null;
    }*/

    /*
     * Primitive types
     */

    public void serializeChar(TransportMessage message, String name, char value)
    {
        String byteString = String.valueOf(value);
        byte [] bytes = byteString.getBytes();
        message.getPayload().put(bytes);
    }

    public char deserializeChar(TransportMessage message, String name)
    {
        byte b = message.getPayload().get();
        return (char) (b & 0xFF);
    }

    public void serializeByte(TransportMessage message, String name, byte value)
    {
        message.getPayload().put(value);
    }

    public void deserializeByte(TransportMessage message, String name, byte value)
    {
        message.getPayload().put(value);
    }

    public byte deserializeByte(TransportMessage message, String name)
    {
        return message.getPayload().get();
    }

    public void serializeI16(TransportMessage message, String name, short value)
    {
        message.getPayload().putShort(value);
    }

    public short deserializeI16(TransportMessage message, String name)
    {
        return message.getPayload().getShort();
    }

    public void serializeUI16(TransportMessage message, String name, short value)
    {
        message.getPayload().putShort(value);
    }

    public short deserializeUI16(TransportMessage message, String name)
    {
        return message.getPayload().getShort();
    }

    public void serializeI32(TransportMessage message, String name, int value)
    {
        message.getPayload().putInt(value);
    }

    public int deserializeI32(TransportMessage message, String name)
    {
        return message.getPayload().getInt();
    }

    public void serializeUI32(TransportMessage message, String name, int value)
    {
        message.getPayload().putInt(value);
    }

    public int deserializeUI32(TransportMessage message, String name)
    {
        return message.getPayload().getInt();
    }

    public void serializeI64(TransportMessage message, String name, long value)
    {
        message.getPayload().putLong(value);
    }

    public long deserializeI64(TransportMessage message, String name)
    {
        return message.getPayload().getLong();
    }

    public void serializeUI64(TransportMessage message, String name, long value)
    {
        message.getPayload().putLong(value);
    }

    public long deserializeUI64(TransportMessage message, String name)
    {
        return message.getPayload().getLong();
    }

    public void serializeFloat32(TransportMessage message, String name, float value)
    {
        message.getPayload().putFloat(value);
    }

    public float deserializeFloat32(TransportMessage message, String name)
    {
        return message.getPayload().getFloat();
    }

    public void serializeFloat64(TransportMessage message, String name, double value)
    {
        message.getPayload().putDouble(value);
    }

    public double deserializeFloat64(TransportMessage message, String name)
    {
        return message.getPayload().getDouble();
    }

    public void serializeBoolean(TransportMessage message, String name, boolean value)
    {
        message.getPayload().put((byte) (value ? 1 : 0));
    }

    public boolean deserializeBoolean(TransportMessage message, String name)
    {
        return message.getPayload().get() != 0;
    }

    public void serializeString(TransportMessage message, String value)
    {
        byte[] bytes = value.getBytes();
        this.serializeI32(message, "", bytes.length);
        message.getPayload().put(bytes);
    }

    public String deserializeString(TransportMessage message)
    {
        int length = 0;
        length = this.deserializeI32(message, "");
        byte[] bytes = new byte[length];
        message.getPayload().get(bytes);
        return new String(bytes);
    }

    /*
     * Generic types
     */

    public <T extends Serializable> void serialize(TransportMessage message, String name, T value)
    {
        value.serialize(this, message, name);
    }

    public <T extends Serializable> T deserialize(TransportMessage message, String name, Class<T> example) throws InstantiationException, IllegalAccessException {
        T object = example.newInstance();
        object.deserialize(this, message, name);
        return object;
    }

    /*
     * Arrays
     */

    public void serializeArrayChar(TransportMessage message, String name, ArrayList<Character> array) {
        for (int i=0; i < array.size(); i++) {
            this.serializeChar(message, name, array.get(i));
        }
    }

    public ArrayList<Character> deserializeArrayChar(TransportMessage message, String name, int length) {
        ArrayList<Character> ret = new ArrayList<Character>();

        for (int i=0; i < length; i++) {
            char currentVal = this.deserializeChar(message, name);
            ret.add(i, currentVal);
        }

        return ret;
    }

    public void serializeArrayByte(TransportMessage message, String name, ArrayList<Byte> array) {
        for (int i=0; i < array.size(); i++) {
            this.serializeByte(message, name, array.get(i));
        }
    }

    public ArrayList<Byte> deserializeArrayByte(TransportMessage message, String name, int length) {
        ArrayList<Byte> ret = new ArrayList<Byte>();

        for (int i=0; i < length; i++) {
            byte currentVal = this.deserializeByte(message, name);
            ret.add(i, currentVal);
        }

        return ret;
    }

    public void serializeArrayI16(TransportMessage message, String name, ArrayList<Short> array) {
        for (int i=0; i < array.size(); i++) {
            this.serializeI16(message, name, array.get(i));
        }
    }

    public ArrayList<Short> deserializeArrayI16(TransportMessage message, String name, int length) {
        ArrayList<Short> ret = new ArrayList<Short>();

        for (int i=0; i < length; i++) {
            short currentVal = this.deserializeI16(message, name);
            ret.add(i, currentVal);
        }

        return ret;
    }

    public void serializeArrayUI16(TransportMessage message, String name, ArrayList<Short> array) {
        for (int i=0; i < array.size(); i++) {
            this.serializeUI16(message, name, array.get(i));
        }
    }

    public ArrayList<Short> deserializeArrayUI16(TransportMessage message, String name, int length) {
        ArrayList<Short> ret = new ArrayList<Short>();

        for (int i=0; i < length; i++) {
            short currentVal = this.deserializeUI16(message, name);
            ret.add(i, currentVal);
        }

        return ret;
    }

    public void serializeArrayI32(TransportMessage message, String name, ArrayList<Integer> array) {
        for (int i=0; i < array.size(); i++) {
            this.serializeI32(message, name, array.get(i));
        }
    }

    public ArrayList<Integer> deserializeArrayI32(TransportMessage message, String name, int length) {
        ArrayList<Integer> ret = new ArrayList<Integer>();

        for (int i=0; i < length; i++) {
            int currentVal = this.deserializeI32(message, name);
            ret.add(i, currentVal);
        }

        return ret;
    }

    public void serializeArrayUI32(TransportMessage message, String name, ArrayList<Integer> array) {
        for (int i=0; i < array.size(); i++) {
            this.serializeUI32(message, name, array.get(i));
        }
    }

    public ArrayList<Integer> deserializeArrayUI32(TransportMessage message, String name, int length) {
        ArrayList<Integer> ret = new ArrayList<Integer>();

        for (int i=0; i < length; i++) {
            int currentVal = this.deserializeUI32(message, name);
            ret.add(i, currentVal);
        }

        return ret;
    }

    public void serializeArrayI64(TransportMessage message, String name, ArrayList<Long> array) {
        for (int i=0; i < array.size(); i++) {
            this.serializeI64(message, name, array.get(i));
        }
    }

    public ArrayList<Long> deserializeArrayI64(TransportMessage message, String name, int length) {
        ArrayList<Long> ret = new ArrayList<Long>();

        for (int i=0; i < length; i++) {
            long currentVal = this.deserializeI64(message, name);
            ret.add(i, currentVal);
        }

        return ret;
    }

    public void serializeArrayUI64(TransportMessage message, String name, ArrayList<Long> array) {
        for (int i=0; i < array.size(); i++) {
            this.serializeUI64(message, name, array.get(i));
        }
    }

    public ArrayList<Long> deserializeArrayUI64(TransportMessage message, String name, int length) {
        ArrayList<Long> ret = new ArrayList<Long>();

        for (int i=0; i < length; i++) {
            long currentVal = this.deserializeUI64(message, name);
            ret.add(i, currentVal);
        }

        return ret;
    }

    public void serializeArrayFloat32(TransportMessage message, String name, ArrayList<Float> array) {
        for (int i=0; i < array.size(); i++) {
            this.serializeFloat32(message, name, array.get(i));
        }
    }

    public ArrayList<Float> deserializeArrayFloat32(TransportMessage message, String name, int length) {
        ArrayList<Float> ret = new ArrayList<Float>();

        for (int i=0; i < length; i++) {
            float currentVal = (float) this.deserializeFloat32(message, name);
            ret.add(i, currentVal);
        }

        return ret;
    }

    public void serializeArrayFloat64(TransportMessage message, String name, ArrayList<Double> array) {
        for (int i=0; i < array.size(); i++) {
            this.serializeFloat64(message, name, array.get(i));
        }
    }

    public ArrayList<Double> deserializeArrayFloat64(TransportMessage message, String name, int length) {
        ArrayList<Double> ret = new ArrayList<Double>();

        for (int i=0; i < length; i++) {
            double currentVal = (double) this.deserializeFloat64(message, name);
            ret.add(i, currentVal);
        }

        return ret;
    }

    public void serializeArrayBoolean(TransportMessage message, String name, ArrayList<Boolean> array) {
        for (int i=0; i < array.size(); i++) {
            this.serializeBoolean(message, name, array.get(i));
        }
    }

    public ArrayList<Boolean> deserializeArrayBoolean(TransportMessage message, String name, int length) {
        ArrayList<Boolean> ret = new ArrayList<Boolean>();

        for (int i=0; i < length; i++) {
            boolean currentVal = this.deserializeBoolean(message, name);
            ret.add(i, currentVal);
        }

        return ret;
    }

    public void serializeArrayString(TransportMessage message, String name, ArrayList<String> array) {
        for (int i=0; i < array.size(); i++) {
            this.serializeString(message, array.get(i));
        }
    }

    public ArrayList<String> deserializeArrayString(TransportMessage message, String name, int length) {
        ArrayList<String> ret = new ArrayList<String>();

        for (int i=0; i < length; i++) {
            String currentVal = this.deserializeString(message);
            ret.add(i, currentVal);
        }

        return ret;
    }

    /*
     * Array of generic types
     */

    public <T extends Serializable> void serializeArray(TransportMessage message, String name, ArrayList<T> array) {
        this.serializeArrayBegin(message, name, array.size());
        for (int i=0; i < array.size(); i++) {
            array.get(i).serialize(this, message, name);
        }
        this.serializeArrayEnd(message, name);
    }

    public <T extends Serializable> ArrayList<T> deserializeArray(TransportMessage message, String name, Class<T> example, int length) throws InstantiationException, IllegalAccessException {
        this.deserializeArrayBegin(message, name);
        ArrayList<T> ret = new ArrayList<T>();
        T object;

        for (int i=0; i < length; i++) {
            object = example.newInstance();
            object.deserialize(this, message, name);
            ret.add(i, object);
        }
        this.deserializeArrayEnd(message, name);
        return ret;
    }

    public void serializeArrayBegin(TransportMessage message, String name,int length) {
        // Do nothing
    }

    public void serializeArrayEnd(TransportMessage message, String name) {
        // Do nothing
    }

    public int deserializeArrayBegin(TransportMessage message, String name) {
        // Do nothing
        return 0;
    }

    public void deserializeArrayEnd(TransportMessage message, String name) {
        // Do nothing
    }




}
