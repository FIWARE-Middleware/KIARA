/**
 * This code is based on the org.apache.activemq.util.ByteArrayOutputStream.
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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Very similar to the java.io.ByteArrayOutputStream but this version is not
 * thread safe and the resulting data is returned in a ByteSequence to avoid an
 * extra byte[] allocation.
 */
public class BinaryOutputStream extends OutputStream {

    private byte[] buffer;
    private int size;

    public BinaryOutputStream() {
        this(1028);
    }

    public BinaryOutputStream(int capacity) {
        buffer = new byte[capacity];
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public int getBufferOffset() {
        return 0;
    }

    public int getBufferLength() {
        return size;
    }

    public ByteBuffer getByteBuffer() {
        return ByteBuffer.wrap(getBuffer(), getBufferOffset(), getBufferLength());
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public int getPosition() {
        return size();
    }

    public void setPosition(int pos) {
        checkCapacity(pos);
        size = pos;
    }

    @Override
    public void write(int b) {
        int newsize = size + 1;
        checkCapacity(newsize);
        buffer[size] = (byte) b;
        size = newsize;
    }

    @Override
    public void write(byte b[], int off, int len) {
        int newsize = size + len;
        checkCapacity(newsize);
        System.arraycopy(b, off, buffer, size, len);
        size = newsize;
    }

    /**
     * Ensures the the buffer has at least the minimumCapacity specified.
     *
     * @param minimumCapacity
     */
    private void checkCapacity(int minimumCapacity) {
        if (minimumCapacity > buffer.length) {
            byte b[] = new byte[Math.max(buffer.length << 1, minimumCapacity)];
            System.arraycopy(buffer, 0, b, 0, size);
            buffer = b;
        }
    }

    public void reset() {
        size = 0;
    }

    public byte[] toByteArray() {
        byte rc[] = new byte[size];
        System.arraycopy(buffer, 0, rc, 0, size);
        return rc;
    }

    public int size() {
        return size;
    }

    public void writeByte(int v) throws IOException {
        this.write(v);
    }

    public void writeShort(int v) throws IOException {
        write((v >>> 8) & 0xFF);
        write((v) & 0xFF);
    }

    public void writeShortLE(int v) throws IOException {
        write((v) & 0xFF);
        write((v >>> 8) & 0xFF);
    }

    public void writeInt(int v) throws IOException {
        write((v >>> 24) & 0xFF);
        write((v >>> 16) & 0xFF);
        write((v >>> 8) & 0xFF);
        write((v) & 0xFF);
    }

    public void writeIntLE(int v) throws IOException {
        write((v) & 0xFF);
        write((v >>> 8) & 0xFF);
        write((v >>> 16) & 0xFF);
        write((v >>> 24) & 0xFF);
    }

    private final byte longBuffer[] = new byte[8];

    public void writeLong(long v) throws IOException {
        longBuffer[0] = (byte) (v >>> 56);
        longBuffer[1] = (byte) (v >>> 48);
        longBuffer[2] = (byte) (v >>> 40);
        longBuffer[3] = (byte) (v >>> 32);
        longBuffer[4] = (byte) (v >>> 24);
        longBuffer[5] = (byte) (v >>> 16);
        longBuffer[6] = (byte) (v >>> 8);
        longBuffer[7] = (byte) (v);
        write(longBuffer, 0, 8);
    }

    public void writeLongLE(long v) throws IOException {
        longBuffer[0] = (byte) (v);
        longBuffer[1] = (byte) (v >>> 8);
        longBuffer[2] = (byte) (v >>> 16);
        longBuffer[3] = (byte) (v >>> 24);
        longBuffer[4] = (byte) (v >>> 32);
        longBuffer[5] = (byte) (v >>> 40);
        longBuffer[6] = (byte) (v >>> 48);
        longBuffer[7] = (byte) (v >>> 56);
        write(longBuffer, 0, 8);
    }

    public void writeFloat(float v) throws IOException {
        writeInt(Float.floatToIntBits(v));
    }

    public void writeFloatLE(float v) throws IOException {
        writeIntLE(Float.floatToIntBits(v));
    }

    public void writeDouble(double v) throws IOException {
        writeLong(Double.doubleToLongBits(v));
    }

    public void writeDoubleLE(double v) throws IOException {
        writeLongLE(Double.doubleToLongBits(v));
    }

}
