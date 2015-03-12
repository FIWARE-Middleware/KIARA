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
import java.util.Map;

/**
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 * @param <K>
 * @param <V>
 * @param <T>
 */
public abstract class AbstractMapSerializer<K, V, T extends Map<K, V>> implements Serializer<T> {

    private final Serializer<K> keySerializer;
    private final Serializer<V> valueSerializer;

    public <KS extends Serializer<K>, VS extends Serializer<V>> AbstractMapSerializer(KS keySerializer, VS valueSerializer) {
        this.keySerializer = keySerializer;
        this.valueSerializer = valueSerializer;
    }

    protected abstract T createContainer(int initialCapacity);

    @Override
    public void write(SerializerImpl impl, BinaryOutputStream message, String name, T object) throws IOException {
        final int length = object.size();
        impl.serializeI32(message, name, length);
        for (Map.Entry<K, V> entry : object.entrySet()) {
            keySerializer.write(impl, message, name, entry.getKey());
            valueSerializer.write(impl, message, name, entry.getValue());
        }
    }

    @Override
    public T read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        final int length = impl.deserializeI32(message, name);

        T container = createContainer(length);

        for (int i = 0; i < length; ++i) {
            K key = keySerializer.read(impl, message, name);
            V value = valueSerializer.read(impl, message, name);
            container.put(key, value);
        }

        return container;
    }

}
