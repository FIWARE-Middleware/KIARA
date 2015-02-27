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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


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

    private <T> int calculatePadding(int position, T type) {
        int pos = position;
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

    private <T> int calculatePadding(BinaryInputStream message, T type) {
        return calculatePadding(message.getPosition(), type);
    }

    private <T> int calculatePadding(BinaryOutputStream message, T type) {
        return calculatePadding(message.getPosition(), type);
    }

    private void writePadding(BinaryOutputStream message, int padding_len) throws IOException {
        byte[] padding = new byte[padding_len];
        message.write(padding);
    }

    private void jumpPadding(BinaryOutputStream message, int padding_len) {
        int pos = message.getPosition();
        message.setPosition(pos+padding_len);
    }

    private void jumpPadding(BinaryInputStream message, int padding_len) {
        int pos = message.getPosition();
        message.setPosition(pos+padding_len);
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
    public void serializeMessageId(BinaryOutputStream message, Object messageId) throws IOException {
        serializeI32(message, "",  (Integer) messageId);
    }

    @Override
    public Object deserializeMessageId(BinaryInputStream message) throws IOException {
        final int id = deserializeI32(message, "");
        return id;
    }

    @Override
    public void serializeService(BinaryOutputStream message, String service) throws IOException {
        this.serializeString(message, "", service);
    }

    @Override
    public String deserializeService(BinaryInputStream message) throws IOException {
        return this.deserializeString(message, "");
    }

    @Override
    public void serializeOperation(BinaryOutputStream message, String operation) throws IOException {
        this.serializeString(message, "", operation);
    }

    @Override
    public String deserializeOperation(BinaryInputStream message) throws IOException {
        return this.deserializeString(message, "");
    }

    /*
     * Primitive types
     */

    @Override
    public void serializeChar(BinaryOutputStream message, String name, char value) throws IOException
    {
        String byteString = String.valueOf(value);
        byte [] bytes = byteString.getBytes();
        message.write(bytes);
    }

    @Override
    public char deserializeChar(BinaryInputStream message, String name) throws IOException
    {
        byte b = message.readByte();
        return (char) (b & 0xFF);
    }

    @Override
    public void serializeByte(BinaryOutputStream message, String name, byte value) throws IOException
    {
        message.writeByte(value);
    }

    @Override
    public byte deserializeByte(BinaryInputStream message, String name) throws IOException
    {
        return message.readByte();
    }

    @Override
    public void serializeI16(BinaryOutputStream message, String name, short value) throws IOException
    {
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            writePadding(message, padding_len);
        }
        message.writeShort(value);
    }

    @Override
    public short deserializeI16(BinaryInputStream message, String name) throws IOException
    {
        short value = 0;
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            jumpPadding(message, padding_len);
        }
        return message.readShort();
    }

    @Override
    public void serializeUI16(BinaryOutputStream message, String name, short value) throws IOException
    {
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            writePadding(message, padding_len);
        }
        message.writeShort(value);
    }

    @Override
    public short deserializeUI16(BinaryInputStream message, String name) throws IOException
    {
        short value = 0;
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            jumpPadding(message, padding_len);
        }
        return message.readShort();
    }

    @Override
    public void serializeI32(BinaryOutputStream message, String name, int value) throws IOException
    {
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            writePadding(message, padding_len);
        }
        message.writeInt(value);
    }

    @Override
    public int deserializeI32(BinaryInputStream message, String name) throws IOException
    {
        int value = 0;
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            jumpPadding(message, padding_len);
        }
        return message.readInt();
    }

    @Override
    public void serializeUI32(BinaryOutputStream message, String name, int value) throws IOException
    {
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            writePadding(message, padding_len);
        }
        message.writeInt(value);
    }

    @Override
    public int deserializeUI32(BinaryInputStream message, String name) throws IOException
    {
        int value = 0;
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            jumpPadding(message, padding_len);
        }
        return message.readInt();
    }

    @Override
    public void serializeI64(BinaryOutputStream message, String name, long value) throws IOException
    {
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            writePadding(message, padding_len);
        }
        message.writeLong(value);
    }

    @Override
    public long deserializeI64(BinaryInputStream message, String name) throws IOException
    {
        long value = 0;
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            jumpPadding(message, padding_len);
        }
        return message.readLong();
    }

    @Override
    public void serializeUI64(BinaryOutputStream message, String name, long value) throws IOException
    {
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            writePadding(message, padding_len);
        }
        message.writeLong(value);
    }

    @Override
    public long deserializeUI64(BinaryInputStream message, String name) throws IOException
    {
        long value = 0;
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            jumpPadding(message, padding_len);
        }
        return message.readLong();
    }

    @Override
    public void serializeFloat32(BinaryOutputStream message, String name, float value) throws IOException
    {
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            writePadding(message, padding_len);
        }
        message.writeFloat(value);
    }

    @Override
    public float deserializeFloat32(BinaryInputStream message, String name) throws IOException
    {
        float value = 0;
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            jumpPadding(message, padding_len);
        }
        return message.readFloat();
    }

    @Override
    public void serializeFloat64(BinaryOutputStream message, String name, double value) throws IOException
    {
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            writePadding(message, padding_len);
        }
        message.writeDouble(value);
    }

    @Override
    public double deserializeFloat64(BinaryInputStream message, String name) throws IOException
    {
        double value = 0;
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            jumpPadding(message, padding_len);
        }
        return message.readDouble();
    }

    @Override
    public void serializeBoolean(BinaryOutputStream message, String name, boolean value) throws IOException
    {
        message.write((byte) (value ? 1 : 0));
    }

    @Override
    public boolean deserializeBoolean(BinaryInputStream message, String name) throws IOException
    {
        return message.readByte() != 0;
    }

    @Override
    public void serializeString(BinaryOutputStream message, String name, String value) throws IOException
    {
        byte[] bytes = value.getBytes();
        this.serializeI32(message, "", bytes.length);
        message.write(bytes);
    }

    @Override
    public String deserializeString(BinaryInputStream message, String name) throws IOException
    {
        int length = 0;
        length = this.deserializeI32(message, "");
        byte[] bytes = new byte[length];
        message.readFully(bytes);
        return new String(bytes);
    }


    @Override
    public void serializeData(BinaryOutputStream message, String name, byte[] data, int offset, int length) throws IOException {
        // serialize as sequence<octet>
        this.serializeUI32(message, name, length);
        message.write(data, offset, length);
    }

    @Override
    public byte[] deserializeData(BinaryInputStream message, String name) throws IOException {
        // deserialize as sequence<octet>
        final int length = this.deserializeUI32(message, name);
        byte[] bytes = new byte[length];
        message.readFully(bytes);
        return bytes;
    }

    /*
     * Generic types
     */

    @Override
    public <T extends Serializable> void serialize(BinaryOutputStream message, String name, T value) throws IOException
    {
        value.serialize(this, message, name);
    }

    @Override
    public <T extends Serializable> T deserialize(BinaryInputStream message, String name, Class<T> example) throws InstantiationException, IllegalAccessException, IOException {
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

    @Deprecated
    @Override
    public <T> void serializeArrayChar(BinaryOutputStream message, String name, List<T> array, int... dims) throws IOException {

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

    @Deprecated
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> List<M> deserializeArrayChar(BinaryInputStream message, String name, int... dims) throws IOException {

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

    @Deprecated
    @Override
    public <T> void serializeArrayByte(BinaryOutputStream message, String name, List<T> array, int... dims) throws IOException {

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

    @Deprecated
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> List<M> deserializeArrayByte(BinaryInputStream message, String name, int... dims) throws IOException {

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

    @Deprecated
    @Override
    public <T> void serializeArrayI16(BinaryOutputStream message, String name, List<T> array, int... dims) throws IOException {

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

    @Deprecated
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> List<M> deserializeArrayI16(BinaryInputStream message, String name, int... dims) throws IOException {

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

    @Deprecated
    @Override
    public <T> void serializeArrayUI16(BinaryOutputStream message, String name, List<T> array, int... dims) throws IOException {

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

    @Deprecated
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> List<M> deserializeArrayUI16(BinaryInputStream message, String name, int... dims) throws IOException {

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

    @Deprecated
    @Override
    public <T> void serializeArrayI32(BinaryOutputStream message, String name, List<T> array, int... dims) throws IOException {

        int len = dims[0];

        if (dims.length > 1) {
            for (int i=0; i < len; ++i) {
                List<?> inner_array = (List<?>) array.get(i);
                this.serializeArrayI32(message, name, inner_array, trimDimensions(dims));
            }
        } 
        else if (array.get(0) instanceof Integer) {
            for (int i=0; i < len; ++i) {
                this.serializeI32(message, name, (Integer) array.get(i));
            }
        }
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> List<M> deserializeArrayI32(BinaryInputStream message, String name, int... dims) throws IOException {

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

    @Deprecated
    @Override
    public <T> void serializeArrayUI32(BinaryOutputStream message, String name, List<T> array, int... dims) throws IOException {

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

    @Deprecated
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> List<M> deserializeArrayUI32(BinaryInputStream message, String name, int... dims) throws IOException {

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

    @Deprecated
    @Override
    public <T> void serializeArrayI64(BinaryOutputStream message, String name, List<T> array, int... dims) throws IOException {

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

    @Deprecated
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> List<M> deserializeArrayI64(BinaryInputStream message, String name, int... dims) throws IOException {

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

    @Deprecated
    @Override
    public <T> void serializeArrayUI64(BinaryOutputStream message, String name, List<T> array, int... dims) throws IOException {

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

    @Deprecated
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> List<M> deserializeArrayUI64(BinaryInputStream message, String name, int... dims) throws IOException {

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

    @Deprecated
    @Override
    public <T> void serializeArrayFloat32(BinaryOutputStream message, String name, List<T> array, int... dims) throws IOException {

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

    @Deprecated
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> List<M> deserializeArrayFloat32(BinaryInputStream message, String name, int... dims) throws IOException {

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

    @Deprecated
    @Override
    public <T> void serializeArrayFloat64(BinaryOutputStream message, String name, List<T> array, int... dims) throws IOException {

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

    @Deprecated
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> List<M> deserializeArrayFloat64(BinaryInputStream message, String name, int... dims) throws IOException {

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

    @Deprecated
    @Override
    public <T> void serializeArrayBoolean(BinaryOutputStream message, String name, List<T> array, int... dims) throws IOException {

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

    @Deprecated
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> List<M> deserializeArrayBoolean(BinaryInputStream message, String name, int... dims) throws IOException {

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

    @Deprecated
    @Override
    public <T> void serializeArrayString(BinaryOutputStream message, String name, List<T> array, int... dims) throws IOException {

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

    @Deprecated
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> List<M> deserializeArrayString(BinaryInputStream message, String name, int... dims) throws IOException {

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

    @Deprecated
    @SuppressWarnings("unchecked")
    @Override
    public <T> void serializeArray(BinaryOutputStream message, String name, List<T> array, int... dims) throws IOException {

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

    @Deprecated
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> List<M> deserializeArray(BinaryInputStream message, String name, Class<T> example, int... dims) throws InstantiationException, IllegalAccessException, IOException {

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

    @Deprecated
    @Override
    public <T> void serializeSequenceChar(BinaryOutputStream message, String name, List<T> sequence) throws IOException {
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

    @Deprecated
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> List<M> deserializeSequenceChar(BinaryInputStream message, String name, int depth) throws IOException {

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

    @Deprecated
    @Override
    public <T> void serializeSequenceByte(BinaryOutputStream message, String name, List<T> sequence) throws IOException {
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

    @Deprecated
  @SuppressWarnings("unchecked")
  @Override
  public <T, M> List<M> deserializeSequenceByte(BinaryInputStream message, String name, int depth) throws IOException {

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

    @Deprecated
      @Override
      public <T> void serializeSequenceI16(BinaryOutputStream message, String name, List<T> sequence) throws IOException {
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

     @Deprecated
     @SuppressWarnings("unchecked")
     @Override
     public <T, M> List<M> deserializeSequenceI16(BinaryInputStream message, String name, int depth) throws IOException {

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


     @Deprecated
     @Override
     public <T> void serializeSequenceUI16(BinaryOutputStream message, String name, List<T> sequence) throws IOException {
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

    @Deprecated
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> List<M> deserializeSequenceUI16(BinaryInputStream message, String name, int depth) throws IOException {

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

    @Deprecated
    @Override
    public <T> void serializeSequenceI32(BinaryOutputStream message, String name, List<T> sequence) throws IOException {
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

    @Deprecated
   @SuppressWarnings("unchecked")
   @Override
   public <T, M> List<M> deserializeSequenceI32(BinaryInputStream message, String name, int depth) throws IOException {

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

    @Deprecated
   @Override
   public <T> void serializeSequenceUI32(BinaryOutputStream message, String name, List<T> sequence) throws IOException {
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

    @Deprecated
  @SuppressWarnings("unchecked")
  @Override
  public <T, M> List<M> deserializeSequenceUI32(BinaryInputStream message, String name, int depth) throws IOException {

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

    @Deprecated
  @Override
  public <T> void serializeSequenceI64(BinaryOutputStream message, String name, List<T> sequence) throws IOException {
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

    @Deprecated
     @SuppressWarnings("unchecked")
     @Override
     public <T, M> List<M> deserializeSequenceI64(BinaryInputStream message, String name, int depth) throws IOException {

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

    @Deprecated
     @Override
     public <T> void serializeSequenceUI64(BinaryOutputStream message, String name, List<T> sequence) throws IOException {
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

    @Deprecated
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> List<M> deserializeSequenceUI64(BinaryInputStream message, String name, int depth) throws IOException {

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

    @Deprecated
    @Override
    public <T> void serializeSequenceFloat32(BinaryOutputStream message, String name, List<T> sequence) throws IOException {
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

       @Deprecated
       @SuppressWarnings("unchecked")
       @Override
       public <T, M> List<M> deserializeSequenceFloat32(BinaryInputStream message, String name, int depth) throws IOException {

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

       @Deprecated
       @Override
       public <T> void serializeSequenceFloat64(BinaryOutputStream message, String name, List<T> sequence) throws IOException {
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

      @Deprecated
      @SuppressWarnings("unchecked")
      @Override
      public <T, M> List<M> deserializeSequenceFloat64(BinaryInputStream message, String name, int depth) throws IOException {

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

      @Deprecated
      @Override
      public <T> void serializeSequenceBoolean(BinaryOutputStream message, String name, List<T> sequence) throws IOException {
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

         @Deprecated
         @SuppressWarnings("unchecked")
         @Override
         public <T, M> List<M> deserializeSequenceBoolean(BinaryInputStream message, String name, int depth) throws IOException {

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

         @Deprecated
         @Override
         public <T> void serializeSequenceString(BinaryOutputStream message, String name, List<T> sequence) throws IOException {
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

        @Deprecated
        @SuppressWarnings("unchecked")
        @Override
        public <T, M> List<M> deserializeSequenceString(BinaryInputStream message, String name, int depth) throws IOException {

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

        @Deprecated
        @Override
        public <T> void serializeSequence(BinaryOutputStream message, String name, List<T> sequence) throws IOException {
            if (sequence.size() > 0) {
                this.serializeI32(message, "", ((List<?>) sequence).size());
                if (sequence.get(0) instanceof List) {
                    for (int i=0; i < sequence.size(); ++i) {
                        this.serializeSequence(message, name, (List<?>) sequence.get(i));
                    }
                } else {
                    for (int i=0; i < sequence.size(); ++i) {
                        if (sequence.get(i) instanceof Serializable) {
                            this.serialize(message, name, (Serializable) sequence.get(i));
                        } else if (sequence.get(i) instanceof Enum) {
                            this.serializeEnum(message, name, (Enum) sequence.get(i));
                        }
                    }
                }
            }
        }

        @Deprecated
        @SuppressWarnings("unchecked")
        @Override
        public <T, M> List<M> deserializeSequence(BinaryInputStream message, String name, Class<T> example, int depth) throws InstantiationException, IllegalAccessException, IOException {
            int length = this.deserializeI32(message, "");

            ArrayList<M> array = new ArrayList<M>(length);

            if (depth != 1) {
                for (int i=0; i < length; ++i) {
                    array.add((M) this.deserializeSequence(message, name, example, depth-1));
                }
            } else if (depth == 1) {
                T object;
                for (int i=0; i < length; ++i) {
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
    * Auxiliary functions
    */

    @Override
    public void serializeArrayBegin(BinaryOutputStream message, String name,int length) throws IOException {
        // Do nothing
    }

    @Override
    public void serializeArrayEnd(BinaryOutputStream message, String name) throws IOException {
        // Do nothing
    }

    @Override
    public int deserializeArrayBegin(BinaryInputStream message, String name) throws IOException {
        // Do nothing
        return 0;
    }

    @Override
    public void deserializeArrayEnd(BinaryInputStream message, String name) throws IOException {
        // Do nothing
    }

    @Override
    public void serializeStructBegin(BinaryOutputStream message, String name) throws IOException {
        // Do nothing
    }

    @Override
    public void serializeStructEnd(BinaryOutputStream message, String name) throws IOException {
        // Do nothing
    }

    @Override
    public int deserializeStructBegin(BinaryInputStream message, String name) throws IOException {
        // Do nothing
        return 0;
    }

    @Override
    public void deserializeStructEnd(BinaryInputStream message, String name) throws IOException {
        // Do nothing
    }

    @Override
    public void serializeSequenceBegin(BinaryOutputStream message, String name) throws IOException {
        // Do nothing
    }

    @Override
    public void serializeSequenceEnd(BinaryOutputStream message, String name) throws IOException {
        // Do nothing
    }

    @Override
    public int deserializeSequenceBegin(BinaryInputStream message, String name) throws IOException {
        // Do nothing
        return 0;
    }

    @Override
    public void deserializeSequenceEnd(BinaryInputStream message, String name) throws IOException {
        // Do nothing
    }

    @Override
    public void serializeUnionBegin(BinaryOutputStream message, String name) throws IOException {
        // Do nothing
    }

    @Override
    public void serializeUnionEnd(BinaryOutputStream message, String name) throws IOException {
        // Do nothing
    }

    @Override
    public int deserializeUnionBegin(BinaryInputStream message, String name) throws IOException {
        // Do nothing
        return 0;
    }

    @Override
    public void deserializeUnionEnd(BinaryInputStream message, String name) throws IOException {
        // Do nothing
    }
    
    /*
     * Enumerations types
     */
    
    @Override
    public <E extends Enum> void serializeEnum(BinaryOutputStream message, String name, E value) throws IOException {
        this.serializeUI32(message, name, value.ordinal());
    }
    
    @Override
    public <E extends Enum> E deserializeEnum(BinaryInputStream message,String name, Class<E> example) throws IOException {
        Enum ret = example.getEnumConstants()[this.deserializeUI32(message, name)];
        return (E) ret;
    }
    
    /*
     * Sets
     */

    @Deprecated
    @Override
    public <T> void serializeSetChar(BinaryOutputStream message, String name, Set<T> set) throws IOException {
        if (set.size() > 0) {
            this.serializeI32(message, "", ((Set<?>) set).size());
            Object firstElement = set.iterator().next();
            if (firstElement instanceof Set) {
                Iterator<?> it = set.iterator();
                while(it.hasNext()) {
                    this.serializeSetChar(message, name, (Set<?>) it.next());
                }
            } else {
                Iterator<?> it = set.iterator();
                while(it.hasNext()) {
                    this.serializeChar(message, name, (Character) it.next());
                }
            }
        }
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> Set<M> deserializeSetChar(BinaryInputStream message,String name, int depth) throws IOException {
        int length = this.deserializeI32(message, "");
        
        HashSet<M> array = new HashSet<M>(length);
        
        if (depth != 1) {
            for (int i=0; i < length; ++i) {
                array.add((M) this.deserializeSetChar(message, name, depth-1));
            }
        } else if (depth == 1) {
            for (int i=0; i < length; ++i) {
                array.add((M) (Character) this.deserializeChar(message, name));
            }
        }
        
        return array;
    }

    @Deprecated
    @Override
    public <T> void serializeSetByte(BinaryOutputStream message, String name, Set<T> set) throws IOException {
        if (set.size() > 0) {
            this.serializeI32(message, "", ((Set<?>) set).size());
            Object firstElement = set.iterator().next();
            if (firstElement instanceof Set) {
                Iterator<?> it = set.iterator();
                while(it.hasNext()) {
                    this.serializeSetByte(message, name, (Set<?>) it.next());
                }
            } else {
                Iterator<?> it = set.iterator();
                while(it.hasNext()) {
                    this.serializeByte(message, name, (Byte) it.next());
                }
            }
        }
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> Set<M> deserializeSetByte(BinaryInputStream message, String name, int depth) throws IOException {
        int length = this.deserializeI32(message, "");
        
        HashSet<M> array = new HashSet<M>(length);
        
        if (depth != 1) {
            for (int i=0; i < length; ++i) {
                array.add((M) this.deserializeSetByte(message, name, depth-1));
            }
        } else if (depth == 1) {
            for (int i=0; i < length; ++i) {
                array.add((M) (Byte) this.deserializeByte(message, name));
            }
        }
        
        return array;
    }

    @Deprecated
    @Override
    public <T> void serializeSetI16(BinaryOutputStream message, String name, Set<T> set) throws IOException {
        if (set.size() > 0) {
            this.serializeI32(message, "", ((Set<?>) set).size());
            Object firstElement = set.iterator().next();
            if (firstElement instanceof Set) {
                Iterator<?> it = set.iterator();
                while(it.hasNext()) {
                    this.serializeSetI16(message, name, (Set<?>) it.next());
                }
            } else {
                Iterator<?> it = set.iterator();
                while(it.hasNext()) {
                    this.serializeI16(message, name, (Short) it.next());
                }
            }
        }
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> Set<M> deserializeSetI16(BinaryInputStream message, String name, int depth) throws IOException {
        int length = this.deserializeI32(message, "");
        
        HashSet<M> array = new HashSet<M>(length);
        
        if (depth != 1) {
            for (int i=0; i < length; ++i) {
                array.add((M) this.deserializeSetI16(message, name, depth-1));
            }
        } else if (depth == 1) {
            for (int i=0; i < length; ++i) {
                array.add((M) (Short) this.deserializeI16(message, name));
            }
        }
        
        return array;
    }

    @Deprecated
    @Override
    public <T> void serializeSetUI16(BinaryOutputStream message, String name, Set<T> set) throws IOException {
        if (set.size() > 0) {
            this.serializeI32(message, "", ((Set<?>) set).size());
            Object firstElement = set.iterator().next();
            if (firstElement instanceof Set) {
                Iterator<?> it = set.iterator();
                while(it.hasNext()) {
                    this.serializeSetUI16(message, name, (Set<?>) it.next());
                }
            } else {
                Iterator<?> it = set.iterator();
                while(it.hasNext()) {
                    this.serializeUI16(message, name, (Short) it.next());
                }
            }
        }
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> Set<M> deserializeSetUI16(BinaryInputStream message, String name, int depth) throws IOException {
        int length = this.deserializeI32(message, "");
        
        HashSet<M> array = new HashSet<M>(length);
        
        if (depth != 1) {
            for (int i=0; i < length; ++i) {
                array.add((M) this.deserializeSetUI16(message, name, depth-1));
            }
        } else if (depth == 1) {
            for (int i=0; i < length; ++i) {
                array.add((M) (Short) this.deserializeUI16(message, name));
            }
        }
        
        return array;
    }

    @Deprecated
    @Override
    public <T> void serializeSetI32(BinaryOutputStream message, String name, Set<T> set) throws IOException {
        if (set.size() > 0) {
            this.serializeI32(message, "", ((Set<?>) set).size());
            Object firstElement = set.iterator().next();
            if (firstElement instanceof Set) {
                Iterator<?> it = set.iterator();
                while(it.hasNext()) {
                    this.serializeSetI32(message, name, (Set<?>) it.next());
                }
            } else {
                Iterator<?> it = set.iterator();
                while(it.hasNext()) {
                    this.serializeI32(message, name, (Integer) it.next());
                }
            }
        }
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> Set<M> deserializeSetI32(BinaryInputStream message, String name, int depth) throws IOException {
        int length = this.deserializeI32(message, "");
        
        HashSet<M> array = new HashSet<M>(length);
        
        if (depth != 1) {
            for (int i=0; i < length; ++i) {
                array.add((M) this.deserializeSetI32(message, name, depth-1));
            }
        } else if (depth == 1) {
            for (int i=0; i < length; ++i) {
                array.add((M) (Integer) this.deserializeI32(message, name));
            }
        }
        
        return array;
    }

    @Deprecated
    @Override
    public <T> void serializeSetUI32(BinaryOutputStream message, String name, Set<T> set) throws IOException {
        if (set.size() > 0) {
            this.serializeI32(message, "", ((Set<?>) set).size());
            Object firstElement = set.iterator().next();
            if (firstElement instanceof Set) {
                Iterator<?> it = set.iterator();
                while(it.hasNext()) {
                    this.serializeSetUI32(message, name, (Set<?>) it.next());
                }
            } else {
                Iterator<?> it = set.iterator();
                while(it.hasNext()) {
                    this.serializeUI32(message, name, (Integer) it.next());
                }
            }
        }
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> Set<M> deserializeSetUI32(BinaryInputStream message, String name, int depth) throws IOException {
        int length = this.deserializeI32(message, "");
        
        HashSet<M> array = new HashSet<M>(length);
        
        if (depth != 1) {
            for (int i=0; i < length; ++i) {
                array.add((M) this.deserializeSetUI32(message, name, depth-1));
            }
        } else if (depth == 1) {
            for (int i=0; i < length; ++i) {
                array.add((M) (Integer) this.deserializeUI32(message, name));
            }
        }
        
        return array;
    }

    @Deprecated
    @Override
    public <T> void serializeSetI64(BinaryOutputStream message, String name, Set<T> set) throws IOException {
        if (set.size() > 0) {
            this.serializeI32(message, "", ((Set<?>) set).size());
            Object firstElement = set.iterator().next();
            if (firstElement instanceof Set) {
                Iterator<?> it = set.iterator();
                while(it.hasNext()) {
                    this.serializeSetI64(message, name, (Set<?>) it.next());
                }
            } else {
                Iterator<?> it = set.iterator();
                while(it.hasNext()) {
                    this.serializeI64(message, name, (Long) it.next());
                }
            }
        }
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> Set<M> deserializeSetI64(BinaryInputStream message, String name, int depth) throws IOException {
        int length = this.deserializeI32(message, "");
        
        HashSet<M> array = new HashSet<M>(length);
        
        if (depth != 1) {
            for (int i=0; i < length; ++i) {
                array.add((M) this.deserializeSetI64(message, name, depth-1));
            }
        } else if (depth == 1) {
            for (int i=0; i < length; ++i) {
                array.add((M) (Long) this.deserializeI64(message, name));
            }
        }
        
        return array;
    }

    @Deprecated
    @Override
    public <T> void serializeSetUI64(BinaryOutputStream message, String name, Set<T> set) throws IOException {
        if (set.size() > 0) {
            this.serializeI32(message, "", ((Set<?>) set).size());
            Object firstElement = set.iterator().next();
            if (firstElement instanceof Set) {
                Iterator<?> it = set.iterator();
                while(it.hasNext()) {
                    this.serializeSetUI64(message, name, (Set<?>) it.next());
                }
            } else {
                Iterator<?> it = set.iterator();
                while(it.hasNext()) {
                    this.serializeUI64(message, name, (Long) it.next());
                }
            }
        }
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> Set<M> deserializeSetUI64(BinaryInputStream message, String name, int depth) throws IOException {
        int length = this.deserializeI32(message, "");
        
        HashSet<M> array = new HashSet<M>(length);
        
        if (depth != 1) {
            for (int i=0; i < length; ++i) {
                array.add((M) this.deserializeSetUI64(message, name, depth-1));
            }
        } else if (depth == 1) {
            for (int i=0; i < length; ++i) {
                array.add((M) (Long) this.deserializeUI64(message, name));
            }
        }
        
        return array;
    }

    @Deprecated
    @Override
    public <T> void serializeSetFloat32(BinaryOutputStream message, String name, Set<T> set) throws IOException {
        if (set.size() > 0) {
            this.serializeI32(message, "", ((Set<?>) set).size());
            Object firstElement = set.iterator().next();
            if (firstElement instanceof Set) {
                Iterator<?> it = set.iterator();
                while(it.hasNext()) {
                    this.serializeSetFloat32(message, name, (Set<?>) it.next());
                }
            } else {
                Iterator<?> it = set.iterator();
                while(it.hasNext()) {
                    this.serializeFloat32(message, name, (Float) it.next());
                }
            }
        }
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> Set<M> deserializeSetFloat32(BinaryInputStream message, String name, int depth) throws IOException {
        int length = this.deserializeI32(message, "");
        
        HashSet<M> array = new HashSet<M>(length);
        
        if (depth != 1) {
            for (int i=0; i < length; ++i) {
                array.add((M) this.deserializeSetFloat32(message, name, depth-1));
            }
        } else if (depth == 1) {
            for (int i=0; i < length; ++i) {
                array.add((M) (Float) this.deserializeFloat32(message, name));
            }
        }
        
        return array;
    }

    @Deprecated
    @Override
    public <T> void serializeSetFloat64(BinaryOutputStream message, String name, Set<T> set) throws IOException {
        if (set.size() > 0) {
            this.serializeI32(message, "", ((Set<?>) set).size());
            Object firstElement = set.iterator().next();
            if (firstElement instanceof Set) {
                Iterator<?> it = set.iterator();
                while(it.hasNext()) {
                    this.serializeSetFloat64(message, name, (Set<?>) it.next());
                }
            } else {
                Iterator<?> it = set.iterator();
                while(it.hasNext()) {
                    this.serializeFloat64(message, name, (Double) it.next());
                }
            }
        }
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> Set<M> deserializeSetFloat64(BinaryInputStream message, String name, int depth) throws IOException {
        int length = this.deserializeI32(message, "");
        
        HashSet<M> array = new HashSet<M>(length);
        
        if (depth != 1) {
            for (int i=0; i < length; ++i) {
                array.add((M) this.deserializeSetFloat64(message, name, depth-1));
            }
        } else if (depth == 1) {
            for (int i=0; i < length; ++i) {
                array.add((M) (Double) this.deserializeFloat64(message, name));
            }
        }
        
        return array;
    }

    @Deprecated
    @Override
    public <T> void serializeSetBoolean(BinaryOutputStream message, String name, Set<T> set) throws IOException {
        if (set.size() > 0) {
            this.serializeI32(message, "", ((Set<?>) set).size());
            Object firstElement = set.iterator().next();
            if (firstElement instanceof Set) {
                Iterator<?> it = set.iterator();
                while(it.hasNext()) {
                    this.serializeSetBoolean(message, name, (Set<?>) it.next());
                }
            } else {
                Iterator<?> it = set.iterator();
                while(it.hasNext()) {
                    this.serializeBoolean(message, name, (Boolean) it.next());
                }
            }
        }
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> Set<M> deserializeSetBoolean(BinaryInputStream message, String name, int depth) throws IOException {
        int length = this.deserializeI32(message, "");
        
        HashSet<M> array = new HashSet<M>(length);
        
        if (depth != 1) {
            for (int i=0; i < length; ++i) {
                array.add((M) this.deserializeSetBoolean(message, name, depth-1));
            }
        } else if (depth == 1) {
            for (int i=0; i < length; ++i) {
                array.add((M) (Boolean) this.deserializeBoolean(message, name));
            }
        }
        
        return array;
    }

    @Deprecated
    @Override
    public <T> void serializeSetString(BinaryOutputStream message, String name, Set<T> set) throws IOException {
        if (set.size() > 0) {
            this.serializeI32(message, "", ((Set<?>) set).size());
            Object firstElement = set.iterator().next();
            if (firstElement instanceof Set) {
                Iterator<?> it = set.iterator();
                while(it.hasNext()) {
                    this.serializeSetString(message, name, (Set<?>) it.next());
                }
            } else {
                Iterator<?> it = set.iterator();
                while(it.hasNext()) {
                    this.serializeString(message, name, (String) it.next());
                }
            }
        }
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> Set<M> deserializeSetString(BinaryInputStream message, String name, int depth) throws IOException {
        int length = this.deserializeI32(message, "");
        
        HashSet<M> array = new HashSet<M>(length);
        
        if (depth != 1) {
            for (int i=0; i < length; ++i) {
                array.add((M) this.deserializeSetString(message, name, depth-1));
            }
        } else if (depth == 1) {
            for (int i=0; i < length; ++i) {
                array.add((M) (String) this.deserializeString(message, name));
            }
        }
        
        return array;
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    @Override
    public <T> void serializeSet(BinaryOutputStream message, String name, Set<T> set) throws IOException {
        if (set.size() > 0) {
            this.serializeI32(message, "", ((Set<?>) set).size());
            Object firstElement = set.iterator().next();
            if (firstElement instanceof Set) {
                Iterator<?> it = set.iterator();
                while(it.hasNext()) {
                    this.serializeSet(message, name, (Set<?>) it.next());
                }
            } else {
                Iterator<?> it = set.iterator();
                while(it.hasNext()) {
                    Object obj = it.next();
                    if (obj instanceof Serializable) {
                        this.serialize(message, name, (Serializable) (T) obj);
                    } else if (obj instanceof Enum) {
                        this.serializeEnum(message, name, (Enum) obj);
                    }
                }
            }
        }
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    @Override
    public <T, M> Set<M> deserializeSet(BinaryInputStream message, String name, Class<T> example, int depth) throws IOException, InstantiationException, IllegalAccessException {
        int length = this.deserializeI32(message, "");
        
        HashSet<M> array = new HashSet<M>(length);
        
        if (depth != 1) {
            for (int i=0; i < length; ++i) {
                array.add((M) this.deserializeSet(message, name, example, depth-1));
            }
        } else if (depth == 1) {
            T object;
            for (int i=0; i < length; ++i) {
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
