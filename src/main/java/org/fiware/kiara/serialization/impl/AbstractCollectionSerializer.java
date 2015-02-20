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
import java.util.Collection;

/**
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 * @param <E>
 * @param <T>
 */
public abstract class AbstractCollectionSerializer<E, T extends Collection<E>> implements Serializer<T> {

    private final Serializer<E> elementSerializer;

    public <S extends Serializer<E>> AbstractCollectionSerializer(S elementSerializer) {
        this.elementSerializer = elementSerializer;
    }

    protected abstract T createContainer(int initialCapacity);

    @Override
    public void write(SerializerImpl impl, BinaryOutputStream message, String name, T object) throws IOException {
        final int length = object.size();
        impl.serializeI32(message, "", length);
        for (E element : object) {
            elementSerializer.write(impl, message, name, element);
        }
    }

    @Override
    public T read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        final int length = impl.deserializeI32(message, "");

        T container = createContainer(length);

        for (int i = 0; i < length; ++i) {
            container.add(elementSerializer.read(impl, message, name));
        }

        return container;
    }

}
