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

import org.fiware.kiara.ps.publisher.WriterProxy;
import org.fiware.kiara.ps.rtps.Endpoint;
import org.fiware.kiara.ps.rtps.attributes.EndpointAttributes;
import org.fiware.kiara.ps.rtps.attributes.ReaderAttributes;
import org.fiware.kiara.ps.rtps.attributes.RemoteWriterAttributes;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.history.ReaderHistoryCache;
import org.fiware.kiara.ps.rtps.messages.elements.EntityId;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public abstract class RTPSReader extends Endpoint {
    
    protected ReaderHistoryCache m_history;
    
    protected ReaderListener m_listener;

    protected boolean m_acceptMessagesToUnknownReaders;
    
    protected boolean m_acceptMessagesFromUnknownWriters;
    
    protected EntityId m_trustedWriterEntityId;
    
    protected boolean m_expectsInlineQos;
    
    private static final Logger logger = LoggerFactory.getLogger(RTPSReader.class);

    public RTPSReader(RTPSParticipant participant, GUID guid, ReaderAttributes att, ReaderHistoryCache history, ReaderListener listener) {
        super(participant, guid, att.endpointAtt);
        // TODO Auto-generated constructor stub
        this.m_acceptMessagesFromUnknownWriters = true;
        this.m_acceptMessagesToUnknownReaders = true;
        this.m_trustedWriterEntityId = new EntityId();
        this.m_history = history;
        this.m_history.setReader(this);
        this.m_listener = listener;
        this.m_expectsInlineQos = att.expectsInlineQos;
        
        logger.info("RTPSReader created correctly");
    }

    public abstract boolean matchedWriterAdd(RemoteWriterAttributes wdata);
    
    public abstract boolean matchedWriterRemove(RemoteWriterAttributes wdata);
    
    public abstract boolean matchedWriterIsMatched(RemoteWriterAttributes wdata);
    
    public abstract boolean changeReceived(CacheChange change, WriterProxy proxy);
    
    public abstract boolean changeRemovedByHistory(CacheChange change, WriterProxy proxy);
    
    public abstract CacheChange nextUntakenCache(WriterProxy proxy);
    
    public abstract CacheChange nextUnreadCache(WriterProxy proxy);
    
    public abstract boolean nextUntakenCache(CacheChange change, WriterProxy proxy);
    
    public abstract boolean nextUnreadCache(CacheChange change, WriterProxy proxy);
    
    public abstract boolean acceptMsgFrom(GUID rntityGUID, WriterProxy proxy);

    public boolean reserveCache(CacheChange change) {
        change = this.m_history.reserveCache();
        if (change != null) {
            return true;
        }
        return false;
    }
    
    public CacheChange reserveCache() {
        return this.m_history.reserveCache();
    }
    
    public boolean releaseCache(CacheChange change) {
        this.m_history.releaseCache(change);
        return true;
    }

    public boolean acceptMsgDirectedTo(EntityId readerId) {
        if (readerId.equals(this.m_guid.getEntityId())) {
            return true;
        }
        if (this.m_acceptMessagesToUnknownReaders && readerId.equals(new EntityId())) {
            return true;
        } 
        return false;
    }
    
    public void setTrustedWriter(EntityId writerId) {
        this.m_acceptMessagesFromUnknownWriters = false;
        this.m_trustedWriterEntityId = writerId;
    }

    public ReaderHistoryCache getHistory() {
        return this.m_history;
    }
    
    

}
