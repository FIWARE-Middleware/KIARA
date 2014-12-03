package com.kiara.serialization.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.kiara.transport.impl.TransportMessage;

public class CDRSerializer implements SerializerImpl {

    private final AtomicInteger nextId;

    public CDRSerializer() {
        nextId = new AtomicInteger(1);
    }

    public static int alignment(int current_alignment, int dataSize) {
        return (dataSize - (current_alignment % dataSize)) & (dataSize-1);
    }

    private <T> int calculatePadding(TransportMessage message, T type) {
        int pos = message.getPayload().position();
        int align = 0;

        if (type instanceof Short) {
            align = pos % (Short.SIZE / 8);
            if (align != 0) {
                int padding_len = (Short.SIZE / 8) - align;
                return padding_len;
            }
        } else if (type instanceof Integer) {
            align = pos % (Integer.SIZE / 8);
            if (align != 0) {
                int padding_len = (Integer.SIZE / 8) - align;
                return padding_len;
            }
        } else if (type instanceof Long) {
            align = pos % (Long.SIZE / 8);
            if (align != 0) {
                int padding_len = (Long.SIZE / 8) - align;
                return padding_len;
            }
        } else if (type instanceof Float) {
            align = pos % (Float.SIZE / 8);
            if (align != 0) {
                int padding_len = (Float.SIZE / 8) - align;
                return padding_len;
            }
        } else if (type instanceof Double) {
            align = pos % (Double.SIZE / 8);
            if (align != 0) {
                int padding_len = (Double.SIZE / 8) - align;
                return padding_len;
            }
        }

        return 0;
    }

    private void writePadding(TransportMessage message, int padding_len) {
        byte[] padding = new byte[padding_len];
        message.getPayload().put(padding);
    }

    private void jumpPadding(TransportMessage message, int padding_len) {
        int pos = message.getPayload().position();
        message.getPayload().position(pos+padding_len);
    }

    @Override
    public Object getNewMessageId() {
        return nextId.getAndIncrement();
    }
    
    @Override
    public boolean equalMessageIds(Object id1, Object id2) {
        if (id1 == id2) {
            return true;
        }
        if (id1 == null || id2 == null) {
            return false;
        }
        return id1.equals(id2);
    }
    
    @Override
    public void serializeMessageId(TransportMessage message, Object messageId) {
        serializeI32(message, "",  (Integer) messageId);
    }
    
    @Override
    public Object deserializeMessageId(TransportMessage message) {
        final int id = deserializeI32(message, "");
        return id;
    }
    
    @Override
    public void serializeService(TransportMessage message, String service) {
        this.serializeString(message, "", service);
    }

    @Override
    public String deserializeService(TransportMessage message) {
        return this.deserializeString(message, "");
    }

    @Override
    public void serializeOperation(TransportMessage message, String operation) {
        this.serializeString(message, "", operation);
    }

    @Override
    public String deserializeOperation(TransportMessage message) {
        return this.deserializeString(message, "");
    }
    
    /*
     * Primitive types
     */

    @Override
    public void serializeChar(TransportMessage message, String name, char value)
    {
        String byteString = String.valueOf(value);
        byte [] bytes = byteString.getBytes();
        message.getPayload().put(bytes);
    }
    
    @Override
    public char deserializeChar(TransportMessage message, String name)
    {
        byte b = message.getPayload().get();
        return (char) (b & 0xFF);
    }
    
    @Override
    public void serializeByte(TransportMessage message, String name, byte value)
    {
        message.getPayload().put(value);
    }
    
    @Override
    public byte deserializeByte(TransportMessage message, String name)
    {
        return message.getPayload().get();
    }
    
    @Override
    public void serializeI16(TransportMessage message, String name, short value)
    {
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            writePadding(message, padding_len);
        }
        message.getPayload().putShort(value);
    }
    
    @Override
    public short deserializeI16(TransportMessage message, String name)
    {
        short value = 0;
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            jumpPadding(message, padding_len);
        }
        return message.getPayload().getShort();
    }
    
    @Override
    public void serializeUI16(TransportMessage message, String name, short value)
    {
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            writePadding(message, padding_len);
        }
        message.getPayload().putShort(value);
    }
    
    @Override
    public short deserializeUI16(TransportMessage message, String name)
    {
        short value = 0;
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            jumpPadding(message, padding_len);
        }
        return message.getPayload().getShort();
    }
    
    @Override
    public void serializeI32(TransportMessage message, String name, int value)
    {
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            writePadding(message, padding_len);
        }
        message.getPayload().putInt(value);
    }
    
    @Override
    public int deserializeI32(TransportMessage message, String name)
    {
        int value = 0;
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            jumpPadding(message, padding_len);
        }
        return message.getPayload().getInt();
    }
    
    @Override
    public void serializeUI32(TransportMessage message, String name, int value)
    {
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            writePadding(message, padding_len);
        }
        message.getPayload().putInt(value);
    }
    
    @Override
    public int deserializeUI32(TransportMessage message, String name)
    {
        int value = 0;
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            jumpPadding(message, padding_len);
        }
        return message.getPayload().getInt();
    }
    
    @Override
    public void serializeI64(TransportMessage message, String name, long value)
    {
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            writePadding(message, padding_len);
        }
        message.getPayload().putLong(value);
    }
    
    @Override
    public long deserializeI64(TransportMessage message, String name)
    {
        long value = 0;
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            jumpPadding(message, padding_len);
        }
        return message.getPayload().getLong();
    }
    
    @Override
    public void serializeUI64(TransportMessage message, String name, long value)
    {
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            writePadding(message, padding_len);
        }
        message.getPayload().putLong(value);
    }
    
    @Override
    public long deserializeUI64(TransportMessage message, String name)
    {
        long value = 0;
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            jumpPadding(message, padding_len);
        }
        return message.getPayload().getLong();
    }
    
    @Override
    public void serializeFloat32(TransportMessage message, String name, float value)
    {
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            writePadding(message, padding_len);
        }
        message.getPayload().putFloat(value);
    }
    
    @Override
    public float deserializeFloat32(TransportMessage message, String name)
    {
        float value = 0;
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            jumpPadding(message, padding_len);
        }
        return message.getPayload().getFloat();
    }
    
    @Override
    public void serializeFloat64(TransportMessage message, String name, double value)
    {
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            writePadding(message, padding_len);
        }
        message.getPayload().putDouble(value);
    }
    
    @Override
    public double deserializeFloat64(TransportMessage message, String name)
    {
        double value = 0;
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            jumpPadding(message, padding_len);
        }
        return message.getPayload().getDouble();
    }
    
    @Override
    public void serializeBoolean(TransportMessage message, String name, boolean value)
    {
        message.getPayload().put((byte) (value ? 1 : 0));
    }
    
    @Override
    public boolean deserializeBoolean(TransportMessage message, String name)
    {
        return message.getPayload().get() != 0;
    }
    
    @Override
    public void serializeString(TransportMessage message, String name, String value)
    {
        byte[] bytes = value.getBytes();
        this.serializeI32(message, "", bytes.length);
        message.getPayload().put(bytes);
    }
    
    @Override
    public String deserializeString(TransportMessage message, String name)
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
    
    @Override
    public <T extends Serializable> void serialize(TransportMessage message, String name, T value)
    {
        value.serialize(this, message, name);
    }
    
    @Override
    public <T extends Serializable> T deserialize(TransportMessage message, String name, Class<T> example) throws InstantiationException, IllegalAccessException {
        T object = example.newInstance();
        object.deserialize(this, message, name);
        return object;
    }

    /*
     * Arrays
     */

    @Override
    public void serializeArrayChar(TransportMessage message, String name, List<Character> array) {
        for (int i=0; i < array.size(); i++) {
            this.serializeChar(message, name, array.get(i));
        }
    }

    @Override
    public List<Character> deserializeArrayChar(TransportMessage message, String name, int length) {
        List<Character> ret = new ArrayList<Character>();
        
        for (int i=0; i < length; i++) {
            char currentVal = this.deserializeChar(message, name);
            ret.add(i, currentVal);
        }

        return ret;
    }
    
    @Override
    public void serializeArrayByte(TransportMessage message, String name, List<Byte> array) {
        for (int i=0; i < array.size(); i++) {
            this.serializeByte(message, name, array.get(i));
        }
    }

    @Override
    public List<Byte> deserializeArrayByte(TransportMessage message, String name, int length) {
        List<Byte> ret = new ArrayList<Byte>();
        
        for (int i=0; i < length; i++) {
            byte currentVal = this.deserializeByte(message, name);
            ret.add(i, currentVal);
        }

        return ret;
    }

    @Override
    public void serializeArrayI16(TransportMessage message, String name, List<Short> array) {
        for (int i=0; i < array.size(); i++) {
            this.serializeI16(message, name, array.get(i));
        }
    }

    @Override
    public List<Short> deserializeArrayI16(TransportMessage message, String name, int length) {
        List<Short> ret = new ArrayList<Short>();
        
        for (int i=0; i < length; i++) {
            short currentVal = this.deserializeI16(message, name);
            ret.add(i, currentVal);
        }

        return ret;
    }

    @Override
    public void serializeArrayUI16(TransportMessage message, String name, List<Short> array) {
        for (int i=0; i < array.size(); i++) {
            this.serializeUI16(message, name, array.get(i));
        }
    }

    @Override
    public List<Short> deserializeArrayUI16(TransportMessage message, String name, int length) {
        List<Short> ret = new ArrayList<Short>();
        
        for (int i=0; i < length; i++) {
            short currentVal = this.deserializeUI16(message, name);
            ret.add(i, currentVal);
        }

        return ret;
    }

    @Override
    public void serializeArrayI32(TransportMessage message, String name, List<Integer> array) {
        for (int i=0; i < array.size(); i++) {
            this.serializeI32(message, name, array.get(i));
        }
    }

    @Override
    public List<Integer> deserializeArrayI32(TransportMessage message, String name, int length) {
        List<Integer> ret = new ArrayList<Integer>();
        
        for (int i=0; i < length; i++) {
            int currentVal = this.deserializeI32(message, name);
            ret.add(i, currentVal);
        }

        return ret;
    }

    @Override
    public void serializeArrayUI32(TransportMessage message, String name, List<Integer> array) {
        for (int i=0; i < array.size(); i++) {
            this.serializeUI32(message, name, array.get(i));
        }
    }

    @Override
    public List<Integer> deserializeArrayUI32(TransportMessage message, String name, int length) {
        List<Integer> ret = new ArrayList<Integer>();
        
        for (int i=0; i < length; i++) {
            int currentVal = this.deserializeUI32(message, name);
            ret.add(i, currentVal);
        }

        return ret;
    }

    @Override
    public void serializeArrayI64(TransportMessage message, String name, List<Long> array) {
        for (int i=0; i < array.size(); i++) {
            this.serializeI64(message, name, array.get(i));
        }
    }

    @Override
    public List<Long> deserializeArrayI64(TransportMessage message, String name, int length) {
        List<Long> ret = new ArrayList<Long>();
        
        for (int i=0; i < length; i++) {
            long currentVal = this.deserializeI64(message, name);
            ret.add(i, currentVal);
        }

        return ret;
    }

    @Override
    public void serializeArrayUI64(TransportMessage message, String name, List<Long> array) {
        for (int i=0; i < array.size(); i++) {
            this.serializeUI64(message, name, array.get(i));
        }
    }

    @Override
    public List<Long> deserializeArrayUI64(TransportMessage message, String name, int length) {
        List<Long> ret = new ArrayList<Long>();
        
        for (int i=0; i < length; i++) {
            long currentVal = this.deserializeUI64(message, name);
            ret.add(i, currentVal);
        }

        return ret;
    }

    @Override
    public void serializeArrayFloat32(TransportMessage message, String name, List<Float> array) {
        for (int i=0; i < array.size(); i++) {
            this.serializeFloat32(message, name, array.get(i));
        }
    }

    @Override
    public List<Float> deserializeArrayFloat32(TransportMessage message, String name, int length) {
        List<Float> ret = new ArrayList<Float>();
        
        for (int i=0; i < length; i++) {
            float currentVal = (float) this.deserializeFloat32(message, name);
            ret.add(i, currentVal);
        }

        return ret;
    }

    @Override
    public void serializeArrayFloat64(TransportMessage message, String name, List<Double> array) {
        for (int i=0; i < array.size(); i++) {
            this.serializeFloat64(message, name, array.get(i));
        }
    }

    @Override
    public List<Double> deserializeArrayFloat64(TransportMessage message, String name, int length) {
        List<Double> ret = new ArrayList<Double>();
        
        for (int i=0; i < length; i++) {
            double currentVal = (double) this.deserializeFloat64(message, name);
            ret.add(i, currentVal);
        }

        return ret;
    }

    @Override
    public void serializeArrayBoolean(TransportMessage message, String name, List<Boolean> array) {
        for (int i=0; i < array.size(); i++) {
            this.serializeBoolean(message, name, array.get(i));
        }
    }

    @Override
    public List<Boolean> deserializeArrayBoolean(TransportMessage message, String name, int length) {
        List<Boolean> ret = new ArrayList<Boolean>();
        
        for (int i=0; i < length; i++) {
            boolean currentVal = this.deserializeBoolean(message, name);
            ret.add(i, currentVal);
        }

        return ret;
    }

    @Override
    public void serializeArrayString(TransportMessage message, String name, List<String> array) {
        for (int i=0; i < array.size(); i++) {
            this.serializeString(message, "", array.get(i));
        }
    }

    @Override
    public List<String> deserializeArrayString(TransportMessage message, String name, int length) {
        List<String> ret = new ArrayList<String>();
        
        for (int i=0; i < length; i++) {
            String currentVal = this.deserializeString(message, "");
            ret.add(i, currentVal);
        }

        return ret;
    }

    /*
     * Array of generic types
     */
    
    @Override
    public <T extends Serializable> void serializeArray(TransportMessage message, String name, List<T> array) {
        this.serializeArrayBegin(message, name, array.size());
        for (int i=0; i < array.size(); i++) {
            array.get(i).serialize(this, message, name);
        }
        this.serializeArrayEnd(message, name);
    }

    @Override
    public <T extends Serializable> List<T> deserializeArray(TransportMessage message, String name, Class<T> example, int length) throws InstantiationException, IllegalAccessException {
        this.deserializeArrayBegin(message, name);
        List<T> ret = new ArrayList<T>();
        T object;

        for (int i=0; i < length; i++) {
            object = example.newInstance();
            object.deserialize(this, message, name);
            ret.add(i, object);
        }
        this.deserializeArrayEnd(message, name);
        return ret;
    }

    @Override
    public void serializeArrayBegin(TransportMessage message, String name,int length) {
        // Do nothing
    }

    @Override
    public void serializeArrayEnd(TransportMessage message, String name) {
        // Do nothing
    }

    @Override
    public int deserializeArrayBegin(TransportMessage message, String name) {
        // Do nothing
        return 0;
    }

    @Override
    public void deserializeArrayEnd(TransportMessage message, String name) {
        // Do nothing
    }




}
