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
import org.fiware.kiara.ps.rtps.attributes.ReaderAttributes;
import org.fiware.kiara.ps.rtps.attributes.RemoteWriterAttributes;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.history.HistoryCache;
import org.fiware.kiara.ps.rtps.history.ReaderHistoryCache;
import org.fiware.kiara.ps.rtps.messages.RTPSMessage;
import org.fiware.kiara.ps.rtps.messages.elements.EntityId;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.GUIDPrefix;
import org.fiware.kiara.ps.rtps.messages.elements.SequenceNumber;
import org.fiware.kiara.ps.rtps.messages.elements.SequenceNumberSet;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.resources.ListenResource;
import org.fiware.kiara.util.ReturnParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* This class represents an RTPS reader, manages the reception of data from the writers.
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public abstract class RTPSReader extends Endpoint {
    
    /**
     * {@link HistoryCache} of the {@link RTPSReader}
     */
    protected ReaderHistoryCache m_history;
    
    /**
     * Listener 
     */
    protected ReaderListener m_listener;

    /**
     * Accept messages to unknown readers
     */
    protected boolean m_acceptMessagesToUnknownReaders;
    
    /**
     * Accept messages from unknown writers
     */
    protected boolean m_acceptMessagesFromUnknownWriters;
    
    /**
     * Trusted writer (for builtin)
     */
    protected EntityId m_trustedWriterEntityId;
    
    /**
     * Indicates whether it expects InlineQoS or not
     */
    protected boolean m_expectsInlineQos;
    
    /**
     * Logging object
     */
    private static final Logger logger = LoggerFactory.getLogger(RTPSReader.class);

    /**
     * {@link RTPSReader} constructor
     * 
     * @param participant {@link RTPSParticipant} who creates the {@link RTPSReader}
     * @param guid {@link GUID} of the reader
     * @param att {@link ReaderAttributes} for configuration
     * @param history {@link ReaderHistoryCache} to store {@link CacheChange} objects
     * @param listener Listener to invoke
     */
    public RTPSReader(RTPSParticipant participant, GUID guid, ReaderAttributes att, ReaderHistoryCache history, ReaderListener listener) {
        super(participant, guid, att.endpointAtt);

        this.m_acceptMessagesFromUnknownWriters = true;
        this.m_acceptMessagesToUnknownReaders = true;
        this.m_trustedWriterEntityId = new EntityId();
        this.m_history = history;
        this.m_history.setReader(this);
        this.m_listener = listener;
        this.m_expectsInlineQos = att.expectsInlineQos;
        
        logger.debug("RTPSReader created successfully");
    }
    
    /**
     * Destroys the information associated to the {@link RTPSReader}
     */
    public void destroy() {
       // Do nothing
    }
    
    /**
     * Adds a new matched writer by using its {@link RemoteWriterAttributes}
     * 
     * @param wdata The {@link RemoteWriterAttributes} to be added
     * @return true on success; false otherwise
     */
    public abstract boolean matchedWriterAdd(RemoteWriterAttributes wdata);
    
    /**
     * Adds a new matched writer by using its {@link RemoteWriterAttributes}
     * 
     * @param wdata The {@link RemoteWriterAttributes} to be added
     * @return true on success; false otherwise
     */
    public abstract boolean matchedWriterRemove(RemoteWriterAttributes wdata);
    
    /**
     * Adds a new matched writer by using its {@link RemoteWriterAttributes}
     * 
     * @param wdata The {@link RemoteWriterAttributes}
     * @return true on success; false otherwise
     */
    public abstract boolean matchedWriterIsMatched(RemoteWriterAttributes wdata);
    
    /**
     * This method id executed when a new {@link CacheChange} has been received
     * 
     * @param change The received {@link CacheChange}
     * @param proxy The {@link WriterProxy}
     * @return true on success; false otherwise
     */
    public abstract boolean changeReceived(CacheChange change, WriterProxy proxy);
    
    /**
     * This method id executed when a {@link CacheChange} has been removed
     * 
     * @param change The removed {@link CacheChange}
     * @param proxy The {@link WriterProxy}
     * @return true on success; false otherwise
     */
    public abstract boolean changeRemovedByHistory(CacheChange change, WriterProxy proxy);
    
    /**
     * This method takes and returnd the next untaken {@link CacheChange}
     * 
     * @param proxy The {@link WriterProxy}
     * @return true on success; false otherwise
     */
    public abstract CacheChange nextUntakenCache(WriterProxy proxy);
    
    /**
     * This method takes and returnd the next unread {@link CacheChange}
     * 
     * @param proxy The {@link WriterProxy}
     * @return true on success; false otherwise
     */
    public abstract CacheChange nextUnreadCache(WriterProxy proxy);

    /**
     * This method takes and returnd the next untaken {@link CacheChange}
     * 
     * @param change
     * @param proxy The {@link WriterProxy}
     * @return true on success; false otherwise
     */
    public abstract boolean nextUntakenCache(ReturnParam<CacheChange> change, ReturnParam<WriterProxy> proxy);

    /**
     * This method takes and returnd the next unread {@link CacheChange}
     * 
     * @param change
     * @param proxy The {@link WriterProxy}
     * @return true on success; false otherwise
     */
    public abstract boolean nextUnreadCache(ReturnParam<CacheChange> change, ReturnParam<WriterProxy> proxy);

    /**
     * Check if the reader accepts messages from a writer with a specific GUID_t.
     *
     * @param entityGUID GUID to check
     * @param proxy Reference of the WriterProxy. Since we already look for it wee return the references
     * so the execution can run faster.
     * @param checktrusted Asks to check trusted entities
     * @return true if the reader accepts messages from the writer with GUID_t entityGUID.
     */
    public abstract boolean acceptMsgFrom(GUID entityGUID, ReturnParam<WriterProxy> proxy, boolean checktrusted);
    
    
    /**
     * Processes the {@link CacheChange} according to the RTPSReader behaviour
     * 
     * @param change The {@link CacheChange} to process
     * @param listenResource The associated {@link ListenResource}
     * @param hasTimestamp Indicates if timestamp should be provided
     * @param timestamp The {@link Timestamp} to be provided
     * @param sourceGuidPrefix Source {@link GUIDPrefix}
     * @return true on success; false otherwise
     */
    public abstract boolean processDataMsg(CacheChange change, ListenResource listenResource, boolean hasTimestamp, Timestamp timestamp, GUIDPrefix sourceGuidPrefix);
    
    /**
     * Processes the received HEARTBEAT message
     * 
     * @param writerGUID The {@link GUID} of the writer
     * @param hbCount The HEARTBEAT count
     * @param firstSN The first {@link SequenceNumber}
     * @param lastSN The last {@link SequenceNumber}
     * @param finalFlag Flag indicating if the message is final
     * @param livelinessFlag FLag indicating if the liveliness is activated
     * @return true on success; false otherwise
     */
    public abstract boolean processHeartbeatMsg(GUID writerGUID, int hbCount, SequenceNumber firstSN, SequenceNumber lastSN, boolean finalFlag, boolean livelinessFlag);
    
    /**
     * Processes the received GAP message
     * 
     * @param writerGUID The {@link GUID} of the writer
     * @param gapStart Starting {@link SequenceNumber}
     * @param gapList List of {@link SequenceNumber} ({@link SequenceNumberSet})
     * @return true on success; false otherwise
     */
    public abstract boolean processGapMsg(GUID writerGUID, SequenceNumber gapStart, SequenceNumberSet gapList);

    /**
     * Gets a free {@link CacheChange} from the {@link HistoryCache}
     * 
     * @param change The {@link CacheChange} reference
     * @return true on success; false otherwise
     */
    public boolean reserveCache(CacheChange change) {
        change = this.m_history.reserveCache();
        if (change != null) {
            return true;
        }
        return false;
    }
    
    /**
     * Reserves a {@link CacheChange} from the {@link HistoryCache}
     * 
     * @return The reserved {@link CacheChange}
     */
    public CacheChange reserveCache() {
        return this.m_history.reserveCache();
    }
    
    /**
     * Releases a {@link CacheChange} from the {@link HistoryCache}
     * 
     * @param change The reserved {@link CacheChange}
     * @return true on success; false otherwise 
     */
    public boolean releaseCache(CacheChange change) {
        this.m_history.releaseCache(change);
        return true;
    }

    /**
     * Checks if the {@link RTPSReader} accepts messages directed to the specified {@link EntityId}
     * 
     * @param readerId The {@link EntityId}
     * @return true if the {@link RTPSMessage} accepts the messages, false otherwise
     */
    public boolean acceptMsgDirectedTo(EntityId readerId) {
        if (readerId.equals(this.m_guid.getEntityId())) {
            return true;
        }
        if (this.m_acceptMessagesToUnknownReaders && readerId.equals(EntityId.createUnknown())) {
            return true;
        } 
        return false;
    }
    
    /**
     * Set the trusted writer id
     * 
     * @param writerId The {@link EntityId}
     */
    public void setTrustedWriter(EntityId writerId) {
        this.m_acceptMessagesFromUnknownWriters = false;
        this.m_trustedWriterEntityId = writerId;
    }

    /**
     * Set the acceptMessagesFromUnknownWriters boolean value
     * 
     * @param value The new boolean value to be set
     */
    public void setAcceptMessagesFromUnknownWriters(boolean value) {
        m_acceptMessagesFromUnknownWriters = value;
    }

    /**
     * Get the expectsInlineQos attribute
     * 
     * @return True if the reader expects Inline QOS.
     */
    public boolean getExpectsInlineQos() {
        return m_expectsInlineQos;
    }

    /**
     * Get the {@link ReaderHistoryCache}
     * 
     * @return The {@link ReaderHistoryCache}
     */
    public ReaderHistoryCache getHistory() {
        return this.m_history;
    }

    /**
     * Get the {@link ReaderListener}
     * 
     * @return The {@link ReaderListener}
     */
    public ReaderListener getListener() {
        return m_listener;
    }

    
}
