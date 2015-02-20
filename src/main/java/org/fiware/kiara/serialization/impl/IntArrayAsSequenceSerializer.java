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
package org.fiware.kiara.serialization.impl;

import java.io.IOException;

/**
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public class IntArrayAsSequenceSerializer implements Serializer<int[]> {

    @Override
    public void write(SerializerImpl impl, BinaryOutputStream message, String name, int[] object) throws IOException {
        impl.serializeSequenceBegin(message, name);
        impl.serializeI32(message, "", object.length);

        for (int i = 0; i < object.length; ++i) {
            impl.serializeI32(message, name, object[i]);
        }
        impl.serializeSequenceEnd(message, name);
    }

    @Override
    public int[] read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        impl.deserializeSequenceBegin(message, name);
        int length = impl.deserializeI32(message, "");
        int[] array = new int[length];

        for (int i = 0; i < length; ++i) {
            array[i] = impl.deserializeI32(message, name);
        }
        impl.deserializeSequenceEnd(message, name);
        return array;
    }

}
