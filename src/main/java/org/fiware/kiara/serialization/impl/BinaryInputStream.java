/**
 * This code is based on the org.apache.activemq.util.ByteArrayInputStream.
 * Original copyright:
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.fiware.kiara.serialization.impl;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Very similar to the java.io.ByteArrayInputStream but this version is not
 * thread safe.
 */
public class BinaryInputStream extends InputStream {

    private byte[] buffer;
    int limit;
    private int pos;
    int mark;

    public BinaryInputStream(byte data[]) {
        this(data, 0, data.length);
    }

    public BinaryInputStream(byte data[], int offset, int size) {
        this.buffer = data;
        this.mark = offset;
        this.pos = offset;
        this.limit = Math.min(offset + size, data.length);
    }

    public static BinaryInputStream fromByteBuffer(ByteBuffer buffer) {
        final ByteBuffer buf = buffer.duplicate();
        if (buf.hasArray()) {
            return new BinaryInputStream(buf.array(), buf.arrayOffset(), buf.remaining());
        } else {
            final byte[] bytes = new byte[buf.remaining()];
            buf.get(bytes);
            return new BinaryInputStream(bytes);
        }
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public int getBufferOffset() {
        return mark;
    }

    public int getBufferLength() {
        return limit - mark;
    }

    public void setBuffer(byte[] data, int offset, int size) {
        this.buffer = data;
        this.mark = offset;
        this.pos = offset;
        this.limit = offset + size;
    }

    public void setBuffer(byte[] data) {
        setBuffer(data, 0, data.length);
    }

    public int getPosition() {
        return this.pos;
    }

    public void setPosition(int pos) {
        this.pos = pos <= limit ? pos : limit;
    }

    @Override
    public int read() throws IOException {
        if (pos < limit) {
            return buffer[pos++] & 0xff;
        } else {
            return -1;
        }
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public int read(byte b[], int off, int len) {
        if (pos < limit) {
            len = Math.min(len, limit - pos);
            if (len > 0) {
                System.arraycopy(buffer, pos, b, off, len);
                pos += len;
            }
            return len;
        } else {
            return -1;
        }
    }

    @Override
    public long skip(long len) throws IOException {
        if (pos < limit) {
            len = Math.min(len, limit - pos);
            if (len > 0) {
                pos += len;
            }
            return len;
        } else {
            return -1;
        }
    }

    @Override
    public int available() {
        return limit - pos;
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public void mark(int markpos) {
        mark = pos;
    }

    @Override
    public void reset() {
        pos = mark;
    }

    public byte readByte() throws IOException {
        return (byte) readUnsignedByte();
    }

    public int readUnsignedByte() throws IOException {
        int b = read();
        if (0 > b) {
            throw new EOFException();
        }
        return b;
    }

    private byte readAndCheckByte() throws IOException {
        int b1 = read();

        if (-1 == b1) {
            throw new EOFException();
        }

        return (byte) b1;
    }

    public short readShort() throws IOException {
        return (short) readUnsignedShort();
    }

    public int readUnsignedShort() throws IOException {
        byte b1 = readAndCheckByte();
        byte b2 = readAndCheckByte();

        return (b1 & 0xFF) << 8 | (b2 & 0xFF);
    }

    public short readShortLE() throws IOException {
        return (short) readUnsignedShortLE();
    }

    public int readUnsignedShortLE() throws IOException {
        byte b1 = readAndCheckByte();
        byte b2 = readAndCheckByte();

        return (b2 & 0xFF) << 8 | (b1 & 0xFF);
    }

    public int readInt() throws IOException {
        byte b1 = readAndCheckByte();
        byte b2 = readAndCheckByte();
        byte b3 = readAndCheckByte();
        byte b4 = readAndCheckByte();

        return b1 << 24 | (b2 & 0xFF) << 16 | (b3 & 0xFF) << 8 | (b4 & 0xFF);
    }

    public int readIntLE() throws IOException {
        byte b1 = readAndCheckByte();
        byte b2 = readAndCheckByte();
        byte b3 = readAndCheckByte();
        byte b4 = readAndCheckByte();

        return b4 << 24 | (b3 & 0xFF) << 16 | (b2 & 0xFF) << 8 | (b1 & 0xFF);
    }

    public final void readFully(byte b[], int off, int len) throws IOException {
        if (len < 0) {
            throw new IndexOutOfBoundsException();
        }
        int n = 0;
        while (n < len) {
            int count = read(b, off + n, len - n);
            if (count < 0) {
                throw new EOFException();
            }
            n += count;
        }
    }

    public final void readFully(byte b[]) throws IOException {
        readFully(b, 0, b.length);
    }

    public final int skipBytes(int n) throws IOException {
        return (int) skip(n);
    }

    private final byte readBuffer[] = new byte[8];

    public long readLong() throws IOException {
        readFully(readBuffer, 0, 8);
        return (((long) readBuffer[0] << 56)
                + ((long) (readBuffer[1] & 0xFF) << 48)
                + ((long) (readBuffer[2] & 0xFF) << 40)
                + ((long) (readBuffer[3] & 0xFF) << 32)
                + ((long) (readBuffer[4] & 0xFF) << 24)
                + ((readBuffer[5] & 0xFF) << 16)
                + ((readBuffer[6] & 0xFF) << 8)
                + ((readBuffer[7] & 0xFF)));
    }

    public long readLongLE() throws IOException {
        readFully(readBuffer, 0, 8);
        return (((long) readBuffer[7] << 56)
                + ((long) (readBuffer[6] & 0xFF) << 48)
                + ((long) (readBuffer[5] & 0xFF) << 40)
                + ((long) (readBuffer[4] & 0xFF) << 32)
                + ((long) (readBuffer[3] & 0xFF) << 24)
                + ((readBuffer[2] & 0xFF) << 16)
                + ((readBuffer[1] & 0xFF) << 8)
                + ((readBuffer[0] & 0xFF)));
    }

    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    public float readFloatLE() throws IOException {
        return Float.intBitsToFloat(readIntLE());
    }

    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    public double readDoubleLE() throws IOException {
        return Double.longBitsToDouble(readLongLE());
    }
}
