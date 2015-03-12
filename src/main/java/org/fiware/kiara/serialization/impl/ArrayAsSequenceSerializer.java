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
import java.lang.reflect.Array;

/**
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 * @param <E>
 */
public class ArrayAsSequenceSerializer<E> implements Serializer<E[]> {

    private final Class<E> componentType;
    private final Serializer<E> elementSerializer;

    public <M extends Serializer<E>> ArrayAsSequenceSerializer(Class<E> componentType, M elementSerializer) {
        this.elementSerializer = elementSerializer;
        this.componentType = componentType;
    }

    @Override
    public void write(SerializerImpl impl, BinaryOutputStream message, String name, E[] object) throws IOException {
        impl.serializeSequenceBegin(message, name);

        final int length = object.length;
        impl.serializeI32(message, "", length);

        for (int i = 0; i < length; ++i) {
            elementSerializer.write(impl, message, name, object[i]);
        }
        impl.serializeSequenceEnd(message, name);
    }

    @Override
    public E[] read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        impl.deserializeSequenceBegin(message, name);
        final int length = impl.deserializeI32(message, "");
        E[] array = (E[]) Array.newInstance(componentType, length);
        for (int i = 0; i < length; ++i) {
            array[i] = elementSerializer.read(impl, message, name);
        }
        impl.deserializeArrayEnd(message, name);
        return array;
    }

}
