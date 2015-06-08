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
 */
public abstract class TopicDataTypeAlt<T> {

    //! Data Type Name.
    private String m_topicDataTypeName;

    //! Maximum Type size in bytes. (If the type includes a string the user MUST ensure that the maximum
    //! size of the string respect the maximum defined size.).
    protected int m_typeSize;

    //! Indicates whether the method to obtain the key has been implemented.
    protected boolean m_isGetKeyDefined;

    /**
     * Serialize method, it should be implemented by the user, since it is
     * abstract. It is VERY IMPORTANT that the user sets the serializedPaylaod
     * length correctly.
     *
     * @param[in] data Pointer to the data
     * @param[out] payload Pointer to the payload
     * @return True if correct.
     */
    public abstract boolean serialize(T data, SerializedPayload payload);

    /**
     * Deserialize method, it should be implemented by the user, since it is
     * abstract.
     *
     * @param[in] payload Pointer to the payload
     * @param[out] data Pointer to the data
     * @return True if correct.
     */
    public abstract boolean deserialize(SerializedPayload payload, T data);

    /**
     * Create a Data Type.
     *
     * @return Void pointer to the created object.
     */
    public abstract T createData();

    /**
     * Get the key associated with the data.
     *
     * @param[in] data Pointer to the data.
     * @param[out] ihandle Pointer to the Handle.
     * @return True if correct.
     */
    public boolean getKey(T data, InstanceHandle ihandle) {
        return false;
    }

    /**
     * Set topic data type name
     *
     * @param nam Topic data type name
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

    public boolean isGetKeyDefined() {
        return m_isGetKeyDefined;
    }

    public int getTypeSize() {
        return m_typeSize;
    }
}
