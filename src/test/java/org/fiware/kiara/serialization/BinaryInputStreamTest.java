/* Test based on LittleEndianDataInputStreamTest.java from guava library
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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.primitives.Bytes;
import java.io.EOFException;

/**
 * Test class for {@link BinaryInputStream}.
 *
 * @author Chris Nokleberg
 * @author Dmitri Rubinstein
 */
public class BinaryInputStreamTest {

    public BinaryInputStreamTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    private byte[] data;

    @Before
    public void setUp() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);

        initializeData(out);

        data = baos.toByteArray();
    }

    private void initializeData(DataOutputStream out) throws IOException {
        /* Write out various test values NORMALLY */
        out.write(new byte[]{-100, 100});
        out.writeByte(100);
        out.writeByte(-100);
        out.writeByte((byte) 200);
        out.writeShort((short) -30000);
        out.writeShort((short) 50000);
        out.writeInt(0xCAFEBABE);
        out.writeLong(0xDEADBEEFCAFEBABEL);
        out.writeFloat(Float.intBitsToFloat(0xCAFEBABE));
        out.writeDouble(Double.longBitsToDouble(0xDEADBEEFCAFEBABEL));
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testReadFully() throws IOException {
        BinaryInputStream in = new BinaryInputStream(data);
        byte[] b = new byte[data.length];
        in.readFully(b);
        assertEquals(Bytes.asList(data), Bytes.asList(b));
    }

    @Test
    public void testReadUnsignedByte_eof() throws IOException {
        BinaryInputStream in = new BinaryInputStream(new byte[0]);
        try {
            in.readUnsignedByte();
            fail();
        } catch (EOFException expected) {
        }
    }

    @Test
    public void testReadUnsignedShort_eof() throws IOException {
        byte[] buf = {23};
        BinaryInputStream in = new BinaryInputStream(buf);
        try {
            in.readUnsignedShort();
            fail();
        } catch (EOFException expected) {
        }
    }

    @Test
    public void testReadLittleEndian() throws IOException {
        BinaryInputStream in = new BinaryInputStream(data);

        /* Read in various values in LITTLE ENDIAN FORMAT */
        byte[] b = new byte[2];
        in.readFully(b);
        assertEquals(-100, b[0]);
        assertEquals(100, b[1]);
        assertEquals(100, in.readByte());
        assertEquals(-100, in.readByte());
        assertEquals(200, in.readUnsignedByte());
        assertEquals(-30000, in.readShort());
        assertEquals(50000, in.readUnsignedShort());
        assertEquals(0xCAFEBABE, in.readInt());
        assertEquals(0xDEADBEEFCAFEBABEL, in.readLong());
        assertEquals(0xCAFEBABE, Float.floatToIntBits(in.readFloat()));
        assertEquals(0xDEADBEEFCAFEBABEL, Double.doubleToLongBits(in.readDouble()));
    }

    @Test
    public void testReadBigEndian() throws IOException {
        BinaryInputStream in = new BinaryInputStream(data);

        /* Read in various values in LITTLE ENDIAN FORMAT */
        byte[] b = new byte[2];
        in.readFully(b);
        assertEquals(-100, b[0]);
        assertEquals(100, b[1]);
        assertEquals(100, in.readByte());
        assertEquals(-100, in.readByte());
        assertEquals(200, in.readUnsignedByte());
        assertEquals(-12150, in.readShortLE());
        assertEquals(20675, in.readUnsignedShortLE());
        assertEquals(0xBEBAFECA, in.readIntLE());
        assertEquals(0xBEBAFECAEFBEADDEL, in.readLongLE());
        assertEquals(0xBEBAFECA, Float.floatToIntBits(in.readFloatLE()));
        assertEquals(0xBEBAFECAEFBEADDEL, Double.doubleToLongBits(in.readDoubleLE()));
    }

    @Test
    public void testSkipBytes() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);

        /* Write out various test values NORMALLY */
        out.write(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}); // 10 bytes of junk to skip
        initializeData(out);

        byte[] data = baos.toByteArray();

        BinaryInputStream in = new BinaryInputStream(data);
        int bytesSkipped = 0;
        while (bytesSkipped < 10) {
            bytesSkipped += in.skipBytes(10 - bytesSkipped);
        }

        /* Read in various values in LITTLE ENDIAN FORMAT */
        byte[] b = new byte[2];
        in.readFully(b);
        assertEquals(-100, b[0]);
        assertEquals(100, b[1]);
    }

}
