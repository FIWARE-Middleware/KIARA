package com.kiara.transport;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class Util {

    public static int littleEndianToInt(byte[] data) {
        return (data[3]) << 24
                | (data[2] & 0xff) << 16
                | (data[1] & 0xff) << 8
                | (data[0] & 0xff);
    }

    public static void readFully(InputStream in, byte b[], int off, int len) throws IOException {
        if (len < 0) {
            throw new IndexOutOfBoundsException();
        }
        int n = 0;
        while (n < len) {
            int count = in.read(b, off + n, len - n);
            if (count < 0) {
                throw new EOFException();
            }
            n += count;
        }
    }
}
