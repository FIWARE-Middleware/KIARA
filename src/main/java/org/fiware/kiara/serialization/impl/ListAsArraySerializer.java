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
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 * @param <E>
 */
public class ListAsArraySerializer<E> extends AbstractCollectionAsArraySerializer<E, List<E>> {

    public <S extends Serializer<E>> ListAsArraySerializer(int arrayDim, S elementSerializer) {
        super(arrayDim, elementSerializer);
    }

    @Override
    public void write(SerializerImpl impl, BinaryOutputStream message, String name, List<E> sequence) throws IOException {
        impl.serializeArrayBegin(message, name, sequence.size());
        super.write(impl, message, name, sequence);
        impl.serializeArrayEnd(message, name);
    }

    @Override
    public List<E> read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        impl.deserializeArrayBegin(message, name);
        List<E> result = super.read(impl, message, name);
        impl.deserializeArrayEnd(message, name);
        return result;
    }

    @Override
    protected List<E> createContainer(int initialCapacity) {
        return new ArrayList<>(initialCapacity);
    }

}
