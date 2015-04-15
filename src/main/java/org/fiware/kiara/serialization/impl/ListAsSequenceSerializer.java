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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 * @param <E>
 */
public class ListAsSequenceSerializer<E> extends AbstractCollectionSerializer<E, List<E>> {

    public <M extends Serializer<E>> ListAsSequenceSerializer(M elementSerializer) {
        super(elementSerializer);
    }

    @Override
    public void write(SerializerImpl impl, BinaryOutputStream message, String name, List<E> sequence) throws IOException {
        impl.serializeSequenceBegin(message, name);
        super.write(impl, message, name, sequence);
        impl.serializeSequenceEnd(message, name);
    }

    @Override
    public List<E> read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        impl.deserializeSequenceBegin(message, name);
        List<E> result = super.read(impl, message, name);
        impl.deserializeSequenceEnd(message, name);
        return result;
    }

    @Override
    protected List<E> createContainer(int initialCapacity) {
        return new ArrayList<>(initialCapacity);
    }

}
