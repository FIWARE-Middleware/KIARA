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
package org.fiware.kiara.ps.rtps.reader;

import org.fiware.kiara.ps.rtps.Endpoint;
import org.fiware.kiara.ps.rtps.attributes.EndpointAttributes;
import org.fiware.kiara.ps.rtps.attributes.ReaderAttributes;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.history.ReaderHistoryCache;
import org.fiware.kiara.ps.rtps.messages.elements.EntityId;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;

public class RTPSReader extends Endpoint {

    protected boolean m_acceptMessagesFromUnknownWriters;
    
    protected EntityId m_trustedWriterEntityId;

    public RTPSReader(RTPSParticipant participant, GUID guid, ReaderAttributes att, ReaderHistoryCache history, ReaderListener listener) {
        super(participant, guid, att.endpointAtt);
        // TODO Auto-generated constructor stub
        this.m_acceptMessagesFromUnknownWriters = true;
        this.m_trustedWriterEntityId = new EntityId();
    }

    public void changeRemovedByHistory(CacheChange change) {
        // TODO Auto-generated method stub

    }

    public boolean acceptMsgDirectedTo(EntityId readerId) {
        // TODO Auto-generated method stub
        return false;
    }

    public void setTrustedWriter(EntityId writerId) {
        this.m_acceptMessagesFromUnknownWriters = false;
        this.m_trustedWriterEntityId = writerId;
    }

}
