/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2014 Proyectos y Sistemas de Mantenimiento S.L. (eProsima)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fiware.kiara.serialization.impl;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.fiware.kiara.transport.impl.TransportMessage;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
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
    
    private <T> int calculatePadding(ByteBuffer buffer, T type) {
        int pos = buffer.position();
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
    
    private void writePadding(ByteBuffer buffer, int padding_len) {
        byte[] padding = new byte[padding_len];
        buffer.put(padding);
    }

    private void jumpPadding(TransportMessage message, int padding_len) {
        int pos = message.getPayload().position();
        message.getPayload().position(pos+padding_len);
    }
    
    private void jumpPadding(ByteBuffer buffer, int padding_len) {
        int pos = buffer.position();
        buffer.position(pos+padding_len);
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
    public void serializeMessageId(ByteBuffer buffer, Object messageId) {
        int padding_len = calculatePadding(buffer, messageId);
        if (padding_len != 0) {
            writePadding(buffer, padding_len);
        }
        buffer.putInt((Integer) messageId);
    }
    
    @Override
    public Object deserializeMessageId(ByteBuffer buffer) {
        int value = 0;
        int padding_len = calculatePadding(buffer, value);
        if (padding_len != 0) {
            jumpPadding(buffer, padding_len);
        }
        return buffer.getInt();
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
    
    private int[] trimDimensions(int[] dims) {
        int[] ret = new int[dims.length-1];
        for (int i=0; i < dims.length; ++i) {
            if (i != 0) {
                ret[i-1] = dims[i];
            }
        }
        return ret;
    }

   @Override
    public <T> void serializeArrayChar(TransportMessage message, String name, List<T> array, int... dims) {
        
        int len = dims[0];
        
        if (dims.length > 1) {
            for (int i=0; i < len; ++i) {
                List<?> inner_array = (List<?>) array.get(i);
                this.serializeArrayChar(message, name, inner_array, trimDimensions(dims));
            }
        } else if (array.get(0) instanceof Character) {
            for (int i=0; i < len; ++i) {
                this.serializeChar(message, name, (Character) array.get(i));
            }
        }
    }
    
   @SuppressWarnings("unchecked")
   @Override
    public <T, M> List<M> deserializeArrayChar(TransportMessage message, String name, int... dims) {
        
        int len = dims[0];
        ArrayList<M> array = new ArrayList<M>(len);
        
        if (dims.length > 1) {
            for (int i=0; i < len; ++i) {
                array.add((M) this.deserializeArrayChar(message, name, trimDimensions(dims)));
            }
        } else {
            for (int i=0; i < len; ++i) {
                array.add((M) (Character) this.deserializeChar(message, name));
            }
        }
        
        return array;
    }
    
    @Override
    public <T> void serializeArrayByte(TransportMessage message, String name, List<T> array, int... dims) {
        
        int len = dims[0];
        
        if (dims.length > 1) {
            for (int i=0; i < len; ++i) {
                List<?> inner_array = (List<?>) array.get(i);
                this.serializeArrayByte(message, name, inner_array, trimDimensions(dims));
            }
        } else if (array.get(0) instanceof Byte) {
            for (int i=0; i < len; ++i) {
                this.serializeByte(message, name, (Byte) array.get(i));
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> List<M> deserializeArrayByte(TransportMessage message, String name, int... dims) {
        
        int len = dims[0];
        ArrayList<M> array = new ArrayList<M>(len);
        
        if (dims.length > 1) {
            for (int i=0; i < len; ++i) {
                array.add((M) this.deserializeArrayByte(message, name, trimDimensions(dims)));
            }
        } else {
            for (int i=0; i < len; ++i) {
                array.add((M) (Byte) this.deserializeByte(message, name));
            }
        }
        
        return array;
    }

    @Override
    public <T> void serializeArrayI16(TransportMessage message, String name, List<T> array, int... dims) {
        
        int len = dims[0];
        
        if (dims.length > 1) {
            for (int i=0; i < len; ++i) {
                List<?> inner_array = (List<?>) array.get(i);
                this.serializeArrayI16(message, name, inner_array, trimDimensions(dims));
            }
        } else if (array.get(0) instanceof Short) {
            for (int i=0; i < len; ++i) {
                this.serializeI16(message, name, (Short) array.get(i));
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> List<M> deserializeArrayI16(TransportMessage message, String name, int... dims) {
        
        int len = dims[0];
        ArrayList<M> array = new ArrayList<M>(len);
        
        if (dims.length > 1) {
            for (int i=0; i < len; ++i) {
                array.add((M) this.deserializeArrayI16(message, name, trimDimensions(dims)));
            }
        } else {
            for (int i=0; i < len; ++i) {
                array.add((M) (Short) this.deserializeI16(message, name));
            }
        }
        
        return array;
    }

    @Override
    public <T> void serializeArrayUI16(TransportMessage message, String name, List<T> array, int... dims) {
        
        int len = dims[0];
        
        if (dims.length > 1) {
            for (int i=0; i < len; ++i) {
                List<?> inner_array = (List<?>) array.get(i);
                this.serializeArrayUI16(message, name, inner_array, trimDimensions(dims));
            }
        } else if (array.get(0) instanceof Short) {
            for (int i=0; i < len; ++i) {
                this.serializeUI16(message, name, (Short) array.get(i));
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> List<M> deserializeArrayUI16(TransportMessage message, String name, int... dims) {
        
        int len = dims[0];
        ArrayList<M> array = new ArrayList<M>(len);
        
        if (dims.length > 1) {
            for (int i=0; i < len; ++i) {
                array.add((M) this.deserializeArrayUI16(message, name, trimDimensions(dims)));
            }
        } else {
            for (int i=0; i < len; ++i) {
                array.add((M) (Short) this.deserializeUI16(message, name));
            }
        }
        
        return array;
    }

    @Override
    public <T> void serializeArrayI32(TransportMessage message, String name, List<T> array, int... dims) {
        
        int len = dims[0];
        
        if (dims.length > 1) {
            for (int i=0; i < len; ++i) {
                List<?> inner_array = (List<?>) array.get(i);
                this.serializeArrayI32(message, name, inner_array, trimDimensions(dims));
            }
        } else if (array.get(0) instanceof Integer) {
            for (int i=0; i < len; ++i) {
                this.serializeI32(message, name, (Integer) array.get(i));
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> List<M> deserializeArrayI32(TransportMessage message, String name, int... dims) {
        
        int len = dims[0];
        ArrayList<M> array = new ArrayList<M>(len);
        
        if (dims.length > 1) {
            for (int i=0; i < len; ++i) {
                array.add((M) this.deserializeArrayI32(message, name, trimDimensions(dims)));
            }
        } else {
            for (int i=0; i < len; ++i) {
                array.add((M) (Integer) this.deserializeI32(message, name));
            }
        }
        
        return array;
    }

    @Override
    public <T> void serializeArrayUI32(TransportMessage message, String name, List<T> array, int... dims) {
        
        int len = dims[0];
        
        if (dims.length > 1) {
            for (int i=0; i < len; ++i) {
                List<?> inner_array = (List<?>) array.get(i);
                this.serializeArrayUI32(message, name, inner_array, trimDimensions(dims));
            }
        } else if (array.get(0) instanceof Integer) {
            for (int i=0; i < len; ++i) {
                this.serializeUI32(message, name, (Integer) array.get(i));
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> List<M> deserializeArrayUI32(TransportMessage message, String name, int... dims) {
        
        int len = dims[0];
        ArrayList<M> array = new ArrayList<M>(len);
        
        if (dims.length > 1) {
            for (int i=0; i < len; ++i) {
                array.add((M) this.deserializeArrayUI32(message, name, trimDimensions(dims)));
            }
        } else {
            for (int i=0; i < len; ++i) {
                array.add((M) (Integer) this.deserializeUI32(message, name));
            }
        }
        
        return array;
    }

    @Override
    public <T> void serializeArrayI64(TransportMessage message, String name, List<T> array, int... dims) {
        
        int len = dims[0];
        
        if (dims.length > 1) {
            for (int i=0; i < len; ++i) {
                List<?> inner_array = (List<?>) array.get(i);
                this.serializeArrayI64(message, name, inner_array, trimDimensions(dims));
            }
        } else if (array.get(0) instanceof Long) {
            for (int i=0; i < len; ++i) {
                this.serializeI64(message, name, (Long) array.get(i));
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> List<M> deserializeArrayI64(TransportMessage message, String name, int... dims) {
        
        int len = dims[0];
        ArrayList<M> array = new ArrayList<M>(len);
        
        if (dims.length > 1) {
            for (int i=0; i < len; ++i) {
                array.add((M) this.deserializeArrayI64(message, name, trimDimensions(dims)));
            }
        } else {
            for (int i=0; i < len; ++i) {
                array.add((M) (Long) this.deserializeI64(message, name));
            }
        }
        
        return array;
    }

    @Override
    public <T> void serializeArrayUI64(TransportMessage message, String name, List<T> array, int... dims) {
        
        int len = dims[0];
        
        if (dims.length > 1) {
            for (int i=0; i < len; ++i) {
                List<?> inner_array = (List<?>) array.get(i);
                this.serializeArrayUI64(message, name, inner_array, trimDimensions(dims));
            }
        } else if (array.get(0) instanceof Long) {
            for (int i=0; i < len; ++i) {
                this.serializeUI64(message, name, (Long) array.get(i));
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> List<M> deserializeArrayUI64(TransportMessage message, String name, int... dims) {
        
        int len = dims[0];
        ArrayList<M> array = new ArrayList<M>(len);
        
        if (dims.length > 1) {
            for (int i=0; i < len; ++i) {
                array.add((M) this.deserializeArrayUI64(message, name, trimDimensions(dims)));
            }
        } else {
            for (int i=0; i < len; ++i) {
                array.add((M) (Long) this.deserializeUI64(message, name));
            }
        }
        
        return array;
    }

    @Override
    public <T> void serializeArrayFloat32(TransportMessage message, String name, List<T> array, int... dims) {
        
        int len = dims[0];
        
        if (dims.length > 1) {
            for (int i=0; i < len; ++i) {
                List<?> inner_array = (List<?>) array.get(i);
                this.serializeArrayFloat32(message, name, inner_array, trimDimensions(dims));
            }
        } else if (array.get(0) instanceof Float) {
            for (int i=0; i < len; ++i) {
                this.serializeFloat32(message, name, (Float) array.get(i));
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> List<M> deserializeArrayFloat32(TransportMessage message, String name, int... dims) {
        
        int len = dims[0];
        ArrayList<M> array = new ArrayList<M>(len);
        
        if (dims.length > 1) {
            for (int i=0; i < len; ++i) {
                array.add((M) this.deserializeArrayFloat32(message, name, trimDimensions(dims)));
            }
        } else {
            for (int i=0; i < len; ++i) {
                array.add((M) (Float) this.deserializeFloat32(message, name));
            }
        }
        
        return array;
    }

    @Override
    public <T> void serializeArrayFloat64(TransportMessage message, String name, List<T> array, int... dims) {
        
        int len = dims[0];
        
        if (dims.length > 1) {
            for (int i=0; i < len; ++i) {
                List<?> inner_array = (List<?>) array.get(i);
                this.serializeArrayFloat64(message, name, inner_array, trimDimensions(dims));
            }
        } else if (array.get(0) instanceof Double) {
            for (int i=0; i < len; ++i) {
                this.serializeFloat64(message, name, (Double) array.get(i));
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> List<M> deserializeArrayFloat64(TransportMessage message, String name, int... dims) {
        
        int len = dims[0];
        ArrayList<M> array = new ArrayList<M>(len);
        
        if (dims.length > 1) {
            for (int i=0; i < len; ++i) {
                array.add((M) this.deserializeArrayFloat64(message, name, trimDimensions(dims)));
            }
        } else {
            for (int i=0; i < len; ++i) {
                array.add((M) (Double) this.deserializeFloat64(message, name));
            }
        }
        
        return array;
    }

    @Override
    public <T> void serializeArrayBoolean(TransportMessage message, String name, List<T> array, int... dims) {
        
        int len = dims[0];
        
        if (dims.length > 1) {
            for (int i=0; i < len; ++i) {
                List<?> inner_array = (List<?>) array.get(i);
                this.serializeArrayBoolean(message, name, inner_array, trimDimensions(dims));
            }
        } else if (array.get(0) instanceof Boolean) {
            for (int i=0; i < len; ++i) {
                this.serializeBoolean(message, name, (Boolean) array.get(i));
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> List<M> deserializeArrayBoolean(TransportMessage message, String name, int... dims) {
        
        int len = dims[0];
        ArrayList<M> array = new ArrayList<M>(len);
        
        if (dims.length > 1) {
            for (int i=0; i < len; ++i) {
                array.add((M) this.deserializeArrayBoolean(message, name, trimDimensions(dims)));
            }
        } else {
            for (int i=0; i < len; ++i) {
                array.add((M) (Boolean) this.deserializeBoolean(message, name));
            }
        }
        
        return array;
    }

    @Override
    public <T> void serializeArrayString(TransportMessage message, String name, List<T> array, int... dims) {
        
        int len = dims[0];
        
        if (dims.length > 1) {
            for (int i=0; i < len; ++i) {
                List<?> inner_array = (List<?>) array.get(i);
                this.serializeArrayString(message, name, inner_array, trimDimensions(dims));
            }
        } else if (array.get(0) instanceof String) {
            for (int i=0; i < len; ++i) {
                this.serializeString(message, name, (String) array.get(i));
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> List<M> deserializeArrayString(TransportMessage message, String name, int... dims) {
        
        int len = dims[0];
        ArrayList<M> array = new ArrayList<M>(len);
        
        if (dims.length > 1) {
            for (int i=0; i < len; ++i) {
                array.add((M) this.deserializeArrayString(message, name, trimDimensions(dims)));
            }
        } else {
            for (int i=0; i < len; ++i) {
                array.add((M) (String) this.deserializeString(message, name));
            }
        }
        
        return array;
    }

    /*
     * Array of generic types
     */
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> void serializeArray(TransportMessage message, String name, List<T> array, int... dims) {
        
        int len = dims[0];
        
        if (dims.length > 1) {
            for (int i=0; i < len; ++i) {
                List<? extends Serializable> inner_array = (List<? extends Serializable>) array.get(i);
                this.serializeArray(message, name, inner_array, trimDimensions(dims));
            }
        } else if (array.get(0) instanceof Serializable) {
            for (int i=0; i < len; ++i) {
                this.serialize(message, name, (Serializable) array.get(i));
            }
        } else if(array.get(0) instanceof Enum) {
            for (int i=0; i < len; ++i) {
                this.serializeEnum(message, name, (Enum) array.get(i));
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> List<M> deserializeArray(TransportMessage message, String name, Class<T> example, int... dims) throws InstantiationException, IllegalAccessException {
        
        int len = dims[0];
        ArrayList<M> array = new ArrayList<M>(len);
        
        if (dims.length > 1) {
            for (int i=0; i < len; ++i) {
                array.add((M) this.deserializeArray(message, name, example, trimDimensions(dims)));
            }
        } else {
            T object;
            for (int i=0; i < len; ++i) {
                if (example.isEnum()) {
                    Enum enumObject = null;
                    Class<? extends Enum> enumClass = (Class<? extends Enum>) example;
                    enumObject = this.deserializeEnum(message, name, enumClass);
                    array.add((M) enumObject);

                } else {
                    object = example.newInstance();
                    ((Serializable) object).deserialize(this, message, name);
                    array.add((M) object);
                }
            }
        }
        
        return array;
    }
    
    /*
     * Sequences of simple types
     */
    
    @Override
    public <T> void serializeSequenceChar(TransportMessage message, String name, List<T> sequence) {
        if (sequence.size() > 0) {
            this.serializeI32(message, "", ((List<?>) sequence).size());
            if (sequence.get(0) instanceof List) {
                for (int i=0; i < sequence.size(); ++i) {
                    this.serializeSequenceChar(message, name, (List<?>) sequence.get(i));
                }
            } else {
                for (int i=0; i < sequence.size(); ++i) {
                    this.serializeChar(message, name, (Character) sequence.get(i));
                }
            }
        }
    }
    
   @SuppressWarnings("unchecked")
   @Override
   public <T, M> List<M> deserializeSequenceChar(TransportMessage message, String name, int depth) {
       
       int length = this.deserializeI32(message, "");
       
       ArrayList<M> array = new ArrayList<M>(length);
       
       if (depth != 1) {
           for (int i=0; i < length; ++i) {
               array.add((M) this.deserializeSequenceChar(message, name, depth-1));
           }
       } else if (depth == 1) {
           for (int i=0; i < length; ++i) {
               array.add((M) (Character) this.deserializeChar(message, name));
           }
       }
       
       return array;
    }
   
   @Override
   public <T> void serializeSequenceByte(TransportMessage message, String name, List<T> sequence) {
       if (sequence.size() > 0) {
           this.serializeI32(message, "", ((List<?>) sequence).size());
           if (sequence.get(0) instanceof List) {
               for (int i=0; i < sequence.size(); ++i) {
                   this.serializeSequenceByte(message, name, (List<?>) sequence.get(i));
               }
           } else {
               for (int i=0; i < sequence.size(); ++i) {
                   this.serializeByte(message, name, (Byte) sequence.get(i));
               }
           }
       }
   }
   
  @SuppressWarnings("unchecked")
  @Override
  public <T, M> List<M> deserializeSequenceByte(TransportMessage message, String name, int depth) {
      
      int length = this.deserializeI32(message, "");
      
      ArrayList<M> array = new ArrayList<M>(length);
      
      if (depth != 1) {
          for (int i=0; i < length; ++i) {
              array.add((M) this.deserializeSequenceByte(message, name, depth-1));
          }
      } else if (depth == 1) {
          for (int i=0; i < length; ++i) {
              array.add((M) (Byte) this.deserializeByte(message, name));
          }
      }
      
      return array;
   }
  
      @Override
      public <T> void serializeSequenceI16(TransportMessage message, String name, List<T> sequence) {
          if (sequence.size() > 0) {
              this.serializeI32(message, "", ((List<?>) sequence).size());
              if (sequence.get(0) instanceof List) {
                  for (int i=0; i < sequence.size(); ++i) {
                      this.serializeSequenceI16(message, name, (List<?>) sequence.get(i));
                  }
              } else {
                  for (int i=0; i < sequence.size(); ++i) {
                      this.serializeI16(message, name, (Short) sequence.get(i));
                  }
              }
          }
      }
      
     @SuppressWarnings("unchecked")
     @Override
     public <T, M> List<M> deserializeSequenceI16(TransportMessage message, String name, int depth) {
         
         int length = this.deserializeI32(message, "");
         
         ArrayList<M> array = new ArrayList<M>(length);
         
         if (depth != 1) {
             for (int i=0; i < length; ++i) {
                 array.add((M) this.deserializeSequenceI16(message, name, depth-1));
             }
         } else if (depth == 1) {
             for (int i=0; i < length; ++i) {
                 array.add((M) (Short) this.deserializeI16(message, name));
             }
         }
         
         return array;
     }
     
     @Override
     public <T> void serializeSequenceUI16(TransportMessage message, String name, List<T> sequence) {
         if (sequence.size() > 0) {
             this.serializeI32(message, "", ((List<?>) sequence).size());
             if (sequence.get(0) instanceof List) {
                 for (int i=0; i < sequence.size(); ++i) {
                     this.serializeSequenceUI16(message, name, (List<?>) sequence.get(i));
                 }
             } else {
                 for (int i=0; i < sequence.size(); ++i) {
                     this.serializeUI16(message, name, (Short) sequence.get(i));
                 }
             }
         }
     }
     
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> List<M> deserializeSequenceUI16(TransportMessage message, String name, int depth) {
        
        int length = this.deserializeI32(message, "");
        
        ArrayList<M> array = new ArrayList<M>(length);
        
        if (depth != 1) {
            for (int i=0; i < length; ++i) {
                array.add((M) this.deserializeSequenceUI16(message, name, depth-1));
            }
        } else if (depth == 1) {
            for (int i=0; i < length; ++i) {
                array.add((M) (Short) this.deserializeUI16(message, name));
            }
        }
        
        return array;
    }
    
    @Override
    public <T> void serializeSequenceI32(TransportMessage message, String name, List<T> sequence) {
        if (sequence.size() > 0) {
            this.serializeI32(message, "", ((List<?>) sequence).size());
            if (sequence.get(0) instanceof List) {
                for (int i=0; i < sequence.size(); ++i) {
                    this.serializeSequenceI32(message, name, (List<?>) sequence.get(i));
                }
            } else {
                for (int i=0; i < sequence.size(); ++i) {
                    this.serializeI32(message, name, (Integer) sequence.get(i));
                }
            }
        }
    }
    
   @SuppressWarnings("unchecked")
   @Override
   public <T, M> List<M> deserializeSequenceI32(TransportMessage message, String name, int depth) {
       
       int length = this.deserializeI32(message, "");
       
       ArrayList<M> array = new ArrayList<M>(length);
       
       if (depth != 1) {
           for (int i=0; i < length; ++i) {
               array.add((M) this.deserializeSequenceI32(message, name, depth-1));
           }
       } else if (depth == 1) {
           for (int i=0; i < length; ++i) {
               array.add((M) (Integer) this.deserializeI32(message, name));
           }
       }
       
       return array;
   }
   
   @Override
   public <T> void serializeSequenceUI32(TransportMessage message, String name, List<T> sequence) {
       if (sequence.size() > 0) {
           this.serializeI32(message, "", ((List<?>) sequence).size());
           if (sequence.get(0) instanceof List) {
               for (int i=0; i < sequence.size(); ++i) {
                   this.serializeSequenceUI32(message, name, (List<?>) sequence.get(i));
               }
           } else {
               for (int i=0; i < sequence.size(); ++i) {
                   this.serializeUI32(message, name, (Integer) sequence.get(i));
               }
           }
       }
   }
   
  @SuppressWarnings("unchecked")
  @Override
  public <T, M> List<M> deserializeSequenceUI32(TransportMessage message, String name, int depth) {
      
      int length = this.deserializeI32(message, "");
      
      ArrayList<M> array = new ArrayList<M>(length);
      
      if (depth != 1) {
          for (int i=0; i < length; ++i) {
              array.add((M) this.deserializeSequenceUI32(message, name, depth-1));
          }
      } else if (depth == 1) {
          for (int i=0; i < length; ++i) {
              array.add((M) (Integer) this.deserializeUI32(message, name));
          }
      }
      
      return array;
  }
  
  @Override
  public <T> void serializeSequenceI64(TransportMessage message, String name, List<T> sequence) {
      if (sequence.size() > 0) {
          this.serializeI32(message, "", ((List<?>) sequence).size());
          if (sequence.get(0) instanceof List) {
              for (int i=0; i < sequence.size(); ++i) {
                  this.serializeSequenceI64(message, name, (List<?>) sequence.get(i));
              }
          } else {
              for (int i=0; i < sequence.size(); ++i) {
                  this.serializeI64(message, name, (Long) sequence.get(i));
              }
          }
      }
  }
  
     @SuppressWarnings("unchecked")
     @Override
     public <T, M> List<M> deserializeSequenceI64(TransportMessage message, String name, int depth) {
         
         int length = this.deserializeI32(message, "");
         
         ArrayList<M> array = new ArrayList<M>(length);
         
         if (depth != 1) {
             for (int i=0; i < length; ++i) {
                 array.add((M) this.deserializeSequenceI64(message, name, depth-1));
             }
         } else if (depth == 1) {
             for (int i=0; i < length; ++i) {
                 array.add((M) (Long) this.deserializeI64(message, name));
             }
         }
         
         return array;
     }
     
     @Override
     public <T> void serializeSequenceUI64(TransportMessage message, String name, List<T> sequence) {
         if (sequence.size() > 0) {
             this.serializeI32(message, "", ((List<?>) sequence).size());
             if (sequence.get(0) instanceof List) {
                 for (int i=0; i < sequence.size(); ++i) {
                     this.serializeSequenceUI64(message, name, (List<?>) sequence.get(i));
                 }
             } else {
                 for (int i=0; i < sequence.size(); ++i) {
                     this.serializeUI64(message, name, (Long) sequence.get(i));
                 }
             }
         }
     }
     
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> List<M> deserializeSequenceUI64(TransportMessage message, String name, int depth) {
        
        int length = this.deserializeI32(message, "");
        
        ArrayList<M> array = new ArrayList<M>(length);
        
        if (depth != 1) {
            for (int i=0; i < length; ++i) {
                array.add((M) this.deserializeSequenceUI64(message, name, depth-1));
            }
        } else if (depth == 1) {
            for (int i=0; i < length; ++i) {
                array.add((M) (Long) this.deserializeUI64(message, name));
            }
        }
        
        return array;
    }
    
    @Override
    public <T> void serializeSequenceFloat32(TransportMessage message, String name, List<T> sequence) {
        if (sequence.size() > 0) {
            this.serializeI32(message, "", ((List<?>) sequence).size());
            if (sequence.get(0) instanceof List) {
                for (int i=0; i < sequence.size(); ++i) {
                    this.serializeSequenceFloat32(message, name, (List<?>) sequence.get(i));
                }
            } else {
                for (int i=0; i < sequence.size(); ++i) {
                    this.serializeFloat32(message, name, (Float) sequence.get(i));
                }
            }
        }
    }
    
       @SuppressWarnings("unchecked")
       @Override
       public <T, M> List<M> deserializeSequenceFloat32(TransportMessage message, String name, int depth) {
           
           int length = this.deserializeI32(message, "");
           
           ArrayList<M> array = new ArrayList<M>(length);
           
           if (depth != 1) {
               for (int i=0; i < length; ++i) {
                   array.add((M) this.deserializeSequenceFloat32(message, name, depth-1));
               }
           } else if (depth == 1) {
               for (int i=0; i < length; ++i) {
                   array.add((M) (Float) this.deserializeFloat32(message, name));
               }
           }
           
           return array;
       }
       
       @Override
       public <T> void serializeSequenceFloat64(TransportMessage message, String name, List<T> sequence) {
           if (sequence.size() > 0) {
               this.serializeI32(message, "", ((List<?>) sequence).size());
               if (sequence.get(0) instanceof List) {
                   for (int i=0; i < sequence.size(); ++i) {
                       this.serializeSequenceFloat64(message, name, (List<?>) sequence.get(i));
                   }
               } else {
                   for (int i=0; i < sequence.size(); ++i) {
                       this.serializeFloat64(message, name, (Double) sequence.get(i));
                   }
               }
           }
       }
       
      @SuppressWarnings("unchecked")
      @Override
      public <T, M> List<M> deserializeSequenceFloat64(TransportMessage message, String name, int depth) {
          
          int length = this.deserializeI32(message, "");
          
          ArrayList<M> array = new ArrayList<M>(length);
          
          if (depth != 1) {
              for (int i=0; i < length; ++i) {
                  array.add((M) this.deserializeSequenceFloat64(message, name, depth-1));
              }
          } else if (depth == 1) {
              for (int i=0; i < length; ++i) {
                  array.add((M) (Double) this.deserializeFloat64(message, name));
              }
          }
          
          return array;
      }
      
      @Override
      public <T> void serializeSequenceBoolean(TransportMessage message, String name, List<T> sequence) {
          if (sequence.size() > 0) {
              this.serializeI32(message, "", ((List<?>) sequence).size());
              if (sequence.get(0) instanceof List) {
                  for (int i=0; i < sequence.size(); ++i) {
                      this.serializeSequenceBoolean(message, name, (List<?>) sequence.get(i));
                  }
              } else {
                  for (int i=0; i < sequence.size(); ++i) {
                      this.serializeBoolean(message, name, (Boolean) sequence.get(i));
                  }
              }
          }
      }
      
         @SuppressWarnings("unchecked")
         @Override
         public <T, M> List<M> deserializeSequenceBoolean(TransportMessage message, String name, int depth) {
             
             int length = this.deserializeI32(message, "");
             
             ArrayList<M> array = new ArrayList<M>(length);
             
             if (depth != 1) {
                 for (int i=0; i < length; ++i) {
                     array.add((M) this.deserializeSequenceBoolean(message, name, depth-1));
                 }
             } else if (depth == 1) {
                 for (int i=0; i < length; ++i) {
                     array.add((M) (Boolean) this.deserializeBoolean(message, name));
                 }
             }
             
             return array;
         }
         
         @Override
         public <T> void serializeSequenceString(TransportMessage message, String name, List<T> sequence) {
             if (sequence.size() > 0) {
                 this.serializeI32(message, "", ((List<?>) sequence).size());
                 if (sequence.get(0) instanceof List) {
                     for (int i=0; i < sequence.size(); ++i) {
                         this.serializeSequenceString(message, name, (List<?>) sequence.get(i));
                     }
                 } else {
                     for (int i=0; i < sequence.size(); ++i) {
                         this.serializeString(message, name, (String) sequence.get(i));
                     }
                 }
             }
         }
         
        @SuppressWarnings("unchecked")
        @Override
        public <T, M> List<M> deserializeSequenceString(TransportMessage message, String name, int depth) {
            
            int length = this.deserializeI32(message, "");
            
            ArrayList<M> array = new ArrayList<M>(length);
            
            if (depth != 1) {
                for (int i=0; i < length; ++i) {
                    array.add((M) this.deserializeSequenceString(message, name, depth-1));
                }
            } else if (depth == 1) {
                for (int i=0; i < length; ++i) {
                    array.add((M) (String) this.deserializeString(message, name));
                }
            }
            
            return array;
        }
        
        @Override
        public <T> void serializeSequence(TransportMessage message, String name, List<T> sequence) {
            if (sequence.size() > 0) {
                this.serializeI32(message, "", ((List<?>) sequence).size());
                if (sequence.get(0) instanceof List) {
                    for (int i=0; i < sequence.size(); ++i) {
                        this.serializeSequence(message, name, (List<?>) sequence.get(i));
                    }
                } else {
                    for (int i=0; i < sequence.size(); ++i) {
                        this.serialize(message, name, (Serializable) sequence.get(i));
                    }
                }
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T, M> List<M> deserializeSequence(TransportMessage message, String name, Class<T> example, int depth) throws InstantiationException, IllegalAccessException {
            int length = this.deserializeI32(message, "");
            
            ArrayList<M> array = new ArrayList<M>(length);
            
            if (depth != 1) {
                for (int i=0; i < length; ++i) {
                    array.add((M) this.deserializeSequence(message, name, example, depth-1));
                }
            } else if (depth == 1) {
                T object;
                for (int i=0; i < length; ++i) {
                    object = example.newInstance();
                    ((Serializable) object).deserialize(this, message, name);
                    array.add((M) object);
                }
            }
            
            return array;
        }
     
   
   /*
    * Auxiliary functions
    */

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

    @Override
    public void serializeStructBegin(TransportMessage message, String name) {
        // Do nothing
    }

    @Override
    public void serializeStructEnd(TransportMessage message, String name) {
        // Do nothing
    }

    @Override
    public int deserializeStructBegin(TransportMessage message, String name) {
        // Do nothing
        return 0;
    }

    @Override
    public void deserializeStructEnd(TransportMessage message, String name) {
        // Do nothing
    }
    
    @Override
    public void serializeSequenceBegin(TransportMessage message, String name) {
        // Do nothing
    }

    @Override
    public void serializeSequenceEnd(TransportMessage message, String name) {
        // Do nothing
    }

    @Override
    public int deserializeSequenceBegin(TransportMessage message, String name) {
        // Do nothing
        return 0;
    }

    @Override
    public void deserializeSequenceEnd(TransportMessage message, String name) {
        // Do nothing
    }
    
    @Override
    public void serializeUnionBegin(TransportMessage message, String name) {
        // Do nothing
    }

    @Override
    public void serializeUnionEnd(TransportMessage message, String name) {
        // Do nothing
    }

    @Override
    public int deserializeUnionBegin(TransportMessage message, String name) {
        // Do nothing
        return 0;
    }

    @Override
    public void deserializeUnionEnd(TransportMessage message, String name) {
        // Do nothing
    }
    
    /*
     * Enumerations types
     */
    
    @Override
    public <E extends Enum> void serializeEnum(TransportMessage message, String name, E value) {
        this.serializeUI32(message, name, value.ordinal());
    }
    
    @Override
    public <E extends Enum> E deserializeEnum(TransportMessage message,String name, Class<E> example) {
        Enum ret = example.getEnumConstants()[this.deserializeUI32(message, name)];
        return (E) ret;
    }

    /*@SuppressWarnings("unchecked")
    @Override
    public <E extends Enum> void serializeArrayEnum(TransportMessage message, String name, List<E> array, int... dims) {
        int len = dims[0];
        
        if (dims.length > 1) {
            for (int i=0; i < len; ++i) {
                List<E> inner_array = (List<E>) array.get(i);
                this.serializeArrayEnum(message, name, inner_array, trimDimensions(dims));
            }
        } else if (array.get(0) instanceof Enum) {
            for (int i=0; i < len; ++i) {
                this.serializeEnum(message, name, (E) array.get(i));
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, M> List<M> deserializeArrayEnum(TransportMessage message, String name, Class<E> example, int... dims) {
        int len = dims[0];
        ArrayList<M> array = new ArrayList<M>(len);
        
        if (dims.length > 1) {
            for (int i=0; i < len; ++i) {
                array.add((M) this.deserializeArrayEnum(message, name, example, trimDimensions(dims)));
            }
        } else {
            for (int i=0; i < len; ++i) {
                array.add((Enum) this.deserializeEnum(message, name, example));
            }
        }
        
        return array;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Enum> void serializeSequenceEnum(TransportMessage message, String name, List<E> sequence) {
        if (sequence.size() > 0) {
            this.serializeI32(message, "", ((List<?>) sequence).size());
            if (sequence.get(0) instanceof List) {
                for (int i=0; i < sequence.size(); ++i) {
                    this.serializeSequenceEnum(message, name, (List<E>) sequence.get(i));
                }
            } else {
                for (int i=0; i < sequence.size(); ++i) {
                    this.serializeEnum(message, name, (E) sequence.get(i));
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, M> List<M> deserializeSequenceEnum(TransportMessage message, String name, Class<E> example, int depth) {
        int length = this.deserializeI32(message, "");
        
        ArrayList<M> array = new ArrayList<M>(length);
        
        if (depth != 1) {
            for (int i=0; i < length; ++i) {
                array.add((M) this.deserializeSequenceEnum(message, name, example, depth-1));
            }
        } else if (depth == 1) {
            for (int i=0; i < length; ++i) {
                array.add((M) this.deserializeEnum(message, name, example));
            }
        }
        
        return array;
    }*/

}
