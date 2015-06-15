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
package org.fiware.kiara.ps.topic;

import java.io.IOException;
import org.fiware.kiara.ps.rtps.messages.elements.SerializedPayload;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.Serializable;
import org.fiware.kiara.serialization.impl.SerializerImpl;

/**
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 * @param <T>
 */
public class SerializableDataType<T extends Serializable> extends TopicDataType<T> {

    private Class<T> dataClass;

    public SerializableDataType(Class<T> dataClass, String name, int typeSize, boolean isGetKeyDefined) {
        if (dataClass == null) {
            throw new NullPointerException("dataClass");
        }
        this.dataClass = dataClass;
        this.m_typeSize = typeSize;
        this.m_isGetKeyDefined = isGetKeyDefined;
        setName(name);
    }

    @Override
    public boolean serialize(T data, SerializedPayload payload) {
        BinaryOutputStream bos = new BinaryOutputStream();
        SerializerImpl ser = payload.getSerializer();
        try {
            ser.serialize(bos, "", data);
        } catch (IOException ex) {
            return false;
        }
        payload.setBuffer(bos.toByteArray());
        return true;
    }

    @Override
    public T deserialize(SerializedPayload payload) throws InstantiationException, IllegalAccessException {
        BinaryInputStream bis = new BinaryInputStream(payload.getBuffer(), 0, payload.getLength());
        SerializerImpl ser = payload.getSerializer();
        try {
            return ser.deserialize(bis, "", dataClass);
        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    public T createData() {
        try {
            return dataClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            return null;
        }
    }

}
