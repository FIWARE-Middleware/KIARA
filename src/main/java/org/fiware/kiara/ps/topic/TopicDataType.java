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
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.CDRSerializer;
import org.fiware.kiara.serialization.impl.Serializable;

/**
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 * @param <T>
 */
public abstract class TopicDataType<T> {

    /**
     *  Data Type Name.
     */
    private String m_topicDataTypeName;

    /**
     *  Maximum Type size in bytes. (If the type includes a string the user MUST ensure that the maximum
     *  size of the string respect the maximum defined size.).
     */
    protected int m_typeSize;

    //! Indicates whether the method to obtain the key has been implemented.
    protected boolean m_isGetKeyDefined;

    /**
     * Serialize method, it should be implemented by the user, since it is
     * abstract. It is VERY IMPORTANT that the user sets the serializedPaylaod
     * length correctly.
     *
     * @param data Reference to the data
     * @param payload Reference to the payload
     * @return True if correct.
     */
    public abstract boolean serialize(T data, SerializedPayload payload);

    /**
     * Deserialize method, it should be implemented by the user, since it is
     * abstract.
     *
     * @param payload Reference to the payload
     * @return True if correct.
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     */
    public abstract T deserialize(SerializedPayload payload) throws InstantiationException, IllegalAccessException;

    /**
     * Create a Data Type.
     *
     * @return Void reference to the created object.
     */
    public abstract T createData();
    
    /**
     * Set topic data type name
     *
     * @param name Topic data type name
     */
    public void setName(String name) {
        m_topicDataTypeName = name;
    }

    /**
     * Get topic data type name
     *
     * @return Topic data type name
     */
    public String getName() {
        return m_topicDataTypeName;
    }

    /**
     * Checks if the key has been defined for this TopicDataType
     * 
     * @return true if the key has been defined; false otherwise
     */
    public boolean isGetKeyDefined() {
        return m_isGetKeyDefined;
    }
    
    /**
     * Set if the key has been defined for this TopicDataType
     * 
     */
    public void setGetKeyDefined(boolean defined) {
        m_isGetKeyDefined = defined;
    }

    /**
     * Get the type size
     * 
     * @return The type size
     */
    public int getTypeSize() {
        return m_typeSize;
    }
    
    public abstract boolean getKey(T data, InstanceHandle ihandle);
    
//    public boolean getKey(T data, InstanceHandle ihandle) {
//        if (this.m_isGetKeyDefined) {
//            BinaryOutputStream bos = new BinaryOutputStream();
//            CDRSerializer ser = new CDRSerializer(false);
//    
//            try {
//                //System.out.println(data.getClass().getSuperclass().getName());
//                System.out.println(data instanceof Serializable);
//                System.out.println(data instanceof KeyedType);
//                System.out.println(data instanceof HelloWorldType);
//                ((KeyedType) data).serializeKey(ser, bos, "");
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//    
//            try {
//                MessageDigest dig = MessageDigest.getInstance("MD5");
//                byte[] md5 = dig.digest(bos.getBuffer());
//                for (int i=0; i < md5.length; ++i) {
//                    ihandle.setValue(i, md5[i]);
//                }
//    
//            } catch (NoSuchAlgorithmException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//    
//            return true;
//        }
//
//        return false;
//    }
}
