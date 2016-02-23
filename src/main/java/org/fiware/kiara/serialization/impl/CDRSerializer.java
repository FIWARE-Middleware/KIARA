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
import java.util.concurrent.atomic.AtomicInteger;


/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public class CDRSerializer implements SerializerImpl {

    private final AtomicInteger nextId;
    private boolean endianness = false; // false = BIG_ENDIAN; true = LITTLE_ENDIAN

    public CDRSerializer() {
        nextId = new AtomicInteger(1);
    }
    
    public CDRSerializer(boolean endianness) {
        nextId = new AtomicInteger(1);
        this.endianness = endianness;
    }
    
    public void setEndianness(boolean endianness) {
        this.endianness = endianness;
    }
    
    public boolean getEndianness() {
        return this.endianness;
    }

    @Override
    public String getName() {
        return "cdr";
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
    public String getContentType() {
        return "application/octet-stream";
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
     * Auxiliary functions
     */
    
    public void addPadding(BinaryOutputStream message, int nBytes) throws IOException
    {
        byte [] bytes = new byte[nBytes];
        message.write(bytes);
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
        if (!endianness) {
                message.writeShort(value);
        } else {
                message.writeShortLE(value);
        }
    }

    @Override
    public short deserializeI16(BinaryInputStream message, String name) throws IOException
    {
        short value = 0;
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            jumpPadding(message, padding_len);
        }
        if (!endianness) {
                return message.readShort();
        } else {
                return message.readShortLE();
        }
    }

    @Override
    public void serializeUI16(BinaryOutputStream message, String name, short value) throws IOException
    {
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            writePadding(message, padding_len);
        }
        if (!endianness) {
                message.writeShort(value);
        } else {
                message.writeShortLE(value);
        }
    }

    @Override
    public short deserializeUI16(BinaryInputStream message, String name) throws IOException
    {
        short value = 0;
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            jumpPadding(message, padding_len);
        }
        if (!endianness) {
                return message.readShort();
        } else {
                return message.readShortLE();
        }
    }

    @Override
    public void serializeI32(BinaryOutputStream message, String name, int value) throws IOException
    {
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            writePadding(message, padding_len);
        }
        if (!endianness) {
                message.writeInt(value);
        } else {
                message.writeIntLE(value);
        }
    }

    @Override
    public int deserializeI32(BinaryInputStream message, String name) throws IOException
    {
        int value = 0;
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            jumpPadding(message, padding_len);
        }
        if (!endianness) {
                return message.readInt();
        } else {
                return message.readIntLE();
        }
    }

    @Override
    public void serializeUI32(BinaryOutputStream message, String name, int value) throws IOException
    {
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            writePadding(message, padding_len);
        }
        if (!endianness) {
                message.writeInt(value);
        } else {
                message.writeIntLE(value);
        }
    }

    @Override
    public int deserializeUI32(BinaryInputStream message, String name) throws IOException
    {
        int value = 0;
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            jumpPadding(message, padding_len);
        }
        if (!endianness) {
                return message.readInt();
        } else {
                return message.readIntLE();
        }
    }

    @Override
    public void serializeI64(BinaryOutputStream message, String name, long value) throws IOException
    {
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            writePadding(message, padding_len);
        }
        if (!endianness) {
                message.writeLong(value);
        } else {
                message.writeLongLE(value);
        }
    }

    @Override
    public long deserializeI64(BinaryInputStream message, String name) throws IOException
    {
        long value = 0;
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            jumpPadding(message, padding_len);
        }
        if (!endianness) {
                return message.readLong();
        } else {
                return message.readLongLE();
        }
    }

    @Override
    public void serializeUI64(BinaryOutputStream message, String name, long value) throws IOException
    {
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            writePadding(message, padding_len);
        }
        if (!endianness) {
                message.writeLong(value);
        } else {
                message.writeLongLE(value);
        }
    }

    @Override
    public long deserializeUI64(BinaryInputStream message, String name) throws IOException
    {
        long value = 0;
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            jumpPadding(message, padding_len);
        }
        if (!endianness) {
                return message.readLong();
        } else {
                return message.readLongLE();
        }
    }

    @Override
    public void serializeFloat32(BinaryOutputStream message, String name, float value) throws IOException
    {
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            writePadding(message, padding_len);
        }
        if (!endianness) {
                message.writeFloat(value);
        } else {
                message.writeFloatLE(value);
        }
    }

    @Override
    public float deserializeFloat32(BinaryInputStream message, String name) throws IOException
    {
        float value = 0;
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            jumpPadding(message, padding_len);
        }
        if (!endianness) {
                return message.readFloat();
        } else {
                return message.readFloatLE();
        }
    }

    @Override
    public void serializeFloat64(BinaryOutputStream message, String name, double value) throws IOException
    {
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            writePadding(message, padding_len);
        }
        if (!endianness) {
                message.writeDouble(value);
        } else {
                message.writeDoubleLE(value);
        }
    }

    @Override
    public double deserializeFloat64(BinaryInputStream message, String name) throws IOException
    {
        double value = 0;
        int padding_len = calculatePadding(message, value);
        if (padding_len != 0) {
            jumpPadding(message, padding_len);
        }
        if (!endianness) {
                return message.readDouble();
        } else {
                return message.readDoubleLE();
        }
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
        byte[] dest = new byte[bytes.length + 1];
     
        // copy ciphertext into start of destination (from pos 0, copy ciphertext.length bytes)
        System.arraycopy(bytes, 0, dest, 0, bytes.length);

        // copy mac into end of destination (from pos ciphertext.length, copy mac.length bytes)
        System.arraycopy(new byte[]{'\0'}, 0, dest, bytes.length, 1);
        
        // Serialization
        this.serializeI32(message, "", dest.length);
        message.write(dest);
    }

    @Override
    public String deserializeString(BinaryInputStream message, String name) throws IOException
    {
        int length = 0;
        length = this.deserializeI32(message, "");
        byte[] bytes = new byte[length];
        message.readFully(bytes);
        byte[] finalBytes = new byte[bytes.length - 1];
        System.arraycopy(bytes, 0, finalBytes, 0, bytes.length - 1);
        return new String(finalBytes);
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
