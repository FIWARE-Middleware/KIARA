/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2015 Proyectos y Sistemas de Mantenimiento S.L. (eProsima)
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
package org.fiware.kiara.ps.qos.policies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

/**
 * Class UserDataQosPolicy, to transmit user data during the discovery phase.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class UserDataQosPolicy extends Parameter {

    /**
     * {@link QosPolicy} acting as a parent class
     */
    public QosPolicy parent;

    /**
     * {@link UserDataQosPolicy} byte array value
     */
    private List<Byte> dataBuf;

    /**
     * Default {@link UserDataQosPolicy} constructor
     */
    public UserDataQosPolicy() {
        super(ParameterId.PID_USER_DATA, (short) 0);
        this.parent = new QosPolicy(false);
    }

    /**
     * Get the byte array policy value
     * 
     * @return The byte array policy value
     */
    public List<Byte> getDataBuf() {
        return this.dataBuf;
    }

    /**
     * Set the byte array policy value
     * 
     * @param buf The byte array to be set
     */
    public void setDataBuf(List<Byte> buf) {
        if (buf == null) {
            dataBuf = null;
        } else if (dataBuf != null && dataBuf.size() == buf.size()) {
            System.arraycopy(buf, 0, dataBuf, 0, buf.size());
        } else {
            dataBuf = new ArrayList<Byte>(buf);
        }
    }

    /**
     * This method copies two instnces of {@link UserDataQosPolicy}
     * @param value The {@link UserDataQosPolicy} to be copied
     */
    public void copy(UserDataQosPolicy value) {
        parent.copy(value.parent);
        setDataBuf(value.dataBuf);
    }

    /**
     * Deserializes only the contents of a {@link UserDataQosPolicy} (not the {@link QosPolicy} contents)
     */
    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        // Do nothing
    }

}
