/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2014 German Research Center for Artificial Intelligence (DFKI)
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
package com.kiara.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class Buffers {

    public static ByteBuffer stringToBuffer(String string, String charsetName) throws UnsupportedEncodingException {
        return ByteBuffer.wrap(string.getBytes(charsetName));
    }

    public static String bufferToString(ByteBuffer buffer) {
        if (buffer.hasArray()) {
            return new String(buffer.array(), buffer.arrayOffset(), buffer.remaining());
        } else {
            int oldPos = buffer.position();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            buffer.position(oldPos);
            return new String(bytes);
        }
    }

    public static String bufferToString(ByteBuffer buffer, String charsetName) throws UnsupportedEncodingException {
        if (buffer.hasArray()) {
            return new String(buffer.array(), buffer.arrayOffset(), buffer.remaining(), charsetName);
        } else {
            int oldPos = buffer.position();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            buffer.position(oldPos);
            return new String(bytes, charsetName);
        }
    }

}
