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
 * @param <T>
 */
public class ArrayAsArraySerializer<T> implements Serializer<T[]> {

    private final Serializer<T> elementSerializer;
    private final Class<T> componentType;
    private final int arrayDim;

    public <M extends Serializer<T>> ArrayAsArraySerializer(M elementSerializer, Class<T> componentType, int arrayDim) {
        this.elementSerializer = elementSerializer;
        this.componentType = componentType;
        this.arrayDim = arrayDim;
    }

    @Override
    public void write(SerializerImpl impl, BinaryOutputStream message, String name, T[] object) throws IOException {
        for (int i = 0; i < arrayDim; ++i) {
            elementSerializer.write(impl, message, name, object[i]);
        }
    }

    @Override
    public T[] read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        T[] array = (T[])Array.newInstance(componentType, arrayDim);

        for (int i = 0; i < arrayDim; ++i) {
            array[i] = elementSerializer.read(impl, message, name);
        }

        return array;
    }

}
