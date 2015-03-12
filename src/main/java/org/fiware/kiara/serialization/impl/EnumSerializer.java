/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2015 German Research Center for Artificial Intelligence (DFKI)
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
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 * @param <E>
 */
public class EnumSerializer<E extends Enum> implements Serializer<E> {

    private final Class<E> enumClass;

    public EnumSerializer(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public void write(SerializerImpl impl, BinaryOutputStream message, String name, E object) throws IOException {
        impl.serializeEnum(message, name, object);
    }

    @Override
    public E read(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        return impl.deserializeEnum(message, name, enumClass);
    }

}
