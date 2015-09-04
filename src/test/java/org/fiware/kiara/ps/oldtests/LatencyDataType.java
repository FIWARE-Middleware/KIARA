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
package org.fiware.kiara.ps.oldtests;

import java.io.IOException;
import org.fiware.kiara.ps.rtps.messages.elements.SerializedPayload;
import org.fiware.kiara.ps.topic.TopicDataType;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;

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
        BinaryOutputStream bos = new BinaryOutputStream(m_typeSize);
        try {
            bos.writeIntLE(data.seqnum);
            if (data.data != null) {
                bos.writeIntLE(data.data.length);
                bos.write(data.data);
            } else {
                bos.writeIntLE(0);
            }
        } catch (IOException ex) {
            return false;
        }
        payload.setBuffer(bos.toByteArray());
        return true;
    }

    @Override
    public LatencyType deserialize(SerializedPayload payload) {
        BinaryInputStream bis = new BinaryInputStream(payload.getBuffer(), 0, payload.getLength());
        try {
            LatencyType data = new LatencyType();
            data.seqnum = bis.readIntLE();
            final int siz = bis.readIntLE();
            if (siz == 0) {
                data.data = null;
            } else {
                data.data = new byte[siz];
                bis.readFully(data.data);
            }
            return data;
        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    public LatencyType createData() {
        return new LatencyType();
    }

}
