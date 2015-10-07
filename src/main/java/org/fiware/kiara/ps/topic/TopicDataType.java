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

import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.fiware.kiara.ps.rtps.messages.elements.SerializedPayload;

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

    /**
     *  Indicates whether the method to obtain the key has been implemented.
     */
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
    
    /**
     * Get the Key associated to the data inside an {@link InstanceHandle} object
     * 
     * @param data The data to get the key from
     * @param ihandle {@link InstanceHandle} to store the key into
     * @return boolean if success; false otherwise
     */
    public abstract boolean getKey(T data, InstanceHandle ihandle);
    
}
