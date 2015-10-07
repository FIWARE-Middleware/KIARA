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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.fiware.kiara.ps.rtps.messages.elements.SerializedPayload;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.CDRSerializer;
import org.fiware.kiara.serialization.impl.Serializable;
import org.fiware.kiara.serialization.impl.SerializerImpl;

/**
 * Class to use as a Topic definition for the user's data types. This 
 * will be used to register said types.
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 * @param <T>
 */
public abstract class SerializableDataType<T extends Serializable & KeyedType> extends TopicDataType<T> {

    /**
     * Class definition of the user data type
     */
    private Class<T> dataClass;

    /**
     * SerializableDataType constructor
     * 
     * @param dataClass The class reference to the user's data type
     * @param name The name of the type
     * @param typeSize The type's data size
     * @param isKeyDefined Indicates whether the type is keyed or not
     */
    public SerializableDataType(Class<T> dataClass, String name, int typeSize, boolean isKeyDefined) {
        if (dataClass == null) {
            throw new NullPointerException("dataClass");
        }
        this.dataClass = dataClass;
        this.m_typeSize = typeSize;
        this.m_isGetKeyDefined = isKeyDefined;
        setName(name);
        setGetKeyDefined(isKeyDefined);
    }

    /**
     * Serializes the user data type
     */
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

    /**
     * Deserializes the user data type
     */
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

    /**
     * Creates a new instance of the user data type
     */
    @Override
    public T createData() {
        try {
            return dataClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            return null;
        }
    }
    
    /**
     * Get the key stored in an {@link InstanceHandle} object
     */
    @Override
    public boolean getKey(T data, InstanceHandle ihandle) {
        if (this.m_isGetKeyDefined) {
            BinaryOutputStream bos = new BinaryOutputStream();
            CDRSerializer ser = new CDRSerializer(false);
    
            try {
                data.serializeKey(ser, bos, "");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    
            try {
                MessageDigest dig = MessageDigest.getInstance("MD5");
                byte[] md5 = dig.digest(bos.getBuffer());
                for (int i=0; i < md5.length; ++i) {
                    ihandle.setValue(i, md5[i]);
                }
    
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    
            return true;
        }

        return false;
    }
    

}
