/* Test based on LittleEndianDataOutputStreamTest.java from guava library
 * Original copyright:
 *
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fiware.kiara.serialization;

import com.google.common.primitives.Bytes;
import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.junit.Test;

/**
 * Test class for {@link BinaryOutputStreamTest}.
 *
 * @author Keith Bottner
 * @author Dmitri Rubinstein
 */
public class BinaryOutputStreamTest {

    @Test
    public void testSetPosition() throws IOException {
        BinaryOutputStream bos = new BinaryOutputStream();
        bos.setPosition(4);
        bos.writeInt(54321);
        org.junit.Assert.assertEquals(8, bos.getPosition());
        bos.setPosition(0);
        bos.writeInt(12345);
        org.junit.Assert.assertEquals(4, bos.getPosition());
        bos.setPosition(8);
        org.junit.Assert.assertEquals(8, bos.getPosition());

        BinaryInputStream bis = new BinaryInputStream(bos.getBuffer(), bos.getBufferOffset(), bos.getBufferLength());
        org.junit.Assert.assertEquals(12345, bis.readInt());
        org.junit.Assert.assertEquals(54321, bis.readInt());
    }

    @Test
    public void testWriteLittleEndian() throws IOException {

        BinaryOutputStream out = new BinaryOutputStream();

        /* Write out various test values in LITTLE ENDIAN FORMAT */
        out.write(new byte[]{-100, 100});
        out.writeByte(100);
        out.writeByte(-100);
        out.writeByte((byte) 200);
        out.writeShortLE((short) -30000);
        out.writeShortLE((short) 50000);
        out.writeIntLE(0xCAFEBABE);
        out.writeLongLE(0xDEADBEEFCAFEBABEL);
        out.writeFloatLE(Float.intBitsToFloat(0xCAFEBABE));
        out.writeDoubleLE(Double.longBitsToDouble(0xDEADBEEFCAFEBABEL));

        byte[] data = out.toByteArray();

        /* Setup input streams */
        DataInput in = new DataInputStream(new ByteArrayInputStream(data));

        /* Read in various values NORMALLY */
        byte[] b = new byte[2];
        in.readFully(b);

        org.junit.Assert.assertEquals(-100, b[0]);
        org.junit.Assert.assertEquals(100, b[1]);
        org.junit.Assert.assertEquals(100, in.readByte());
        org.junit.Assert.assertEquals(-100, in.readByte());
        org.junit.Assert.assertEquals(200, in.readUnsignedByte());
        org.junit.Assert.assertEquals(-12150, in.readShort());
        org.junit.Assert.assertEquals(20675, in.readUnsignedShort());
        org.junit.Assert.assertEquals(0xBEBAFECA, in.readInt());
        org.junit.Assert.assertEquals(0xBEBAFECAEFBEADDEL, in.readLong());
        org.junit.Assert.assertEquals(0xBEBAFECA, Float.floatToIntBits(in.readFloat()));
        org.junit.Assert.assertEquals(0xBEBAFECAEFBEADDEL, Double.doubleToLongBits(in.readDouble()));
    }

    private static void assertEquals(byte[] expected, byte[] actual) {
        org.junit.Assert.assertEquals(Bytes.asList(expected), Bytes.asList(actual));
    }
}
