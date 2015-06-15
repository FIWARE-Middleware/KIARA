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
package org.fiware.kiara.ps;

import org.fiware.kiara.ps.rtps.messages.elements.SerializedPayload;
import org.fiware.kiara.ps.topic.TopicDataType;

/**
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public class LatencyDataType extends TopicDataType<LatencyType> {

    public LatencyDataType() {
        setName("LatencyType");
        m_typeSize = 17000;
        m_isGetKeyDefined = false;
    }

    @Override
    public boolean serialize(LatencyType data, SerializedPayload payload) {
        return true;
    }

    @Override
    public boolean deserialize(SerializedPayload payload, LatencyType data) {
        return true;
    }

    @Override
    public LatencyType createData() {
        return new LatencyType((short) getTypeSize());
    }

}
