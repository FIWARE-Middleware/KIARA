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
package org.fiware.kiara.ps.rtps.writer;

import org.fiware.kiara.ps.rtps.Endpoint;
import org.fiware.kiara.ps.rtps.attributes.RemoteReaderAttributes;
import org.fiware.kiara.ps.rtps.attributes.WriterAttributes;
import org.fiware.kiara.ps.rtps.common.TopicKind;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.history.HistoryCache;
import org.fiware.kiara.ps.rtps.history.WriterHistoryCache;
import org.fiware.kiara.ps.rtps.messages.RTPSMessage;
import org.fiware.kiara.ps.rtps.messages.RTPSMessageBuilder;
import org.fiware.kiara.ps.rtps.messages.common.types.ChangeKind;
import org.fiware.kiara.ps.rtps.messages.common.types.RTPSEndian;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.fiware.kiara.ps.rtps.messages.elements.SequenceNumber;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.reader.RTPSReader;
import org.fiware.kiara.ps.rtps.writer.timedevent.UnsentChangesNotEmptyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* The {@link RTPSWriter} class represents an RTPS level writer entity.
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public abstract class RTPSWriter extends Endpoint {

    /**
     * Makes the {@link RTPSWriter} to push every change
     */
    protected boolean m_pushMode;
    
    /**
     * The {@link RTPSMessage} to be sent
     */
    protected RTPSMessage m_rtpsMessage;
    
    /**
     * Indicates if liveliness has been asserted
     */
    protected boolean m_livelinessAsserted;
    
    /**
     * Indicates if there are changes to be sent
     */
    protected UnsentChangesNotEmptyEvent m_unsentChangesNotEmpty;
    
    /**
     * {@link HistoryCache} of the {@link RTPSWriter}
     */
    protected WriterHistoryCache m_history;
    
    /**
     * Listener to invoke events
     */
    protected WriterListener m_listener;
    
    /**
     * Logging object
     */
    private static final Logger logger = LoggerFactory.getLogger(RTPSWriter.class);

    /**
     * {@link RTPSWriter} constructor
     * 
     * @param participant The {@link RTPSParticipant} that creates the {@link RTPSWriter}
     * @param guid The {@link RTPSWriter} {@link GUID}
     * @param att Attributes for configuration
     * @param history The {@link WriterHistoryCache} to add {@link CacheChange}s
     * @param listener The {@link WriterListener} to be ivoked when an event should occur
     */
    public RTPSWriter(RTPSParticipant participant, GUID guid, WriterAttributes att, WriterHistoryCache history, WriterListener listener) {
        super(participant, guid, att.endpointAtt);
        this.m_livelinessAsserted = false;
        this.m_history = history;
        this.m_history.m_writer = this;
        this.m_pushMode = true;
        this.m_listener = listener;
        this.initHeader();
        logger.debug("RTPSWriter created successfully");
    }
    
    /**
     * Initializes the {@link RTPSMessage} header
     */
    private void initHeader() {
        m_rtpsMessage =  RTPSMessageBuilder.createMessage(this.m_history.getAttributes().payloadMaxSize, RTPSEndian.LITTLE_ENDIAN); // TODO Set endiannes defined by user
        RTPSMessageBuilder.addHeader(m_rtpsMessage, this.m_guid.getGUIDPrefix());
       
    }

    /**
     * Creates a new {@link CacheChange}
     * 
     * @param changeKind The type of {@link CacheChange}
     * @param handle The associated {@link InstanceHandle}
     * @return A new {@link CacheChange}
     */
    public CacheChange newChange(ChangeKind changeKind, InstanceHandle handle) {
        logger.debug("Creating new change");
        CacheChange ch = this.m_history.reserveCache();
        
        if (ch == null) {
            logger.warn("Problem reserving Cache from the History");
            return null;
        }
        
        ch.setKind(changeKind);
        
        if (this.m_att.topicKind == TopicKind.WITH_KEY && handle == null) {
            logger.warn("Changes in KEYED Writers need a valid instanceHandle");
        }
        
        ch.setInstanceHandle(handle);
        ch.setWriterGUID(this.m_guid);
        
        return ch;
    }
    
    /**
     * Get the minimum {@link SequenceNumber}
     * 
     * @return The minimum {@link SequenceNumber}
     */
    public SequenceNumber getSeqNumMin() {
        CacheChange change = this.m_history.getMinChange();
        if (change != null) {
            return change.getSequenceNumber();
        } else {
            return new SequenceNumber().setUnknown();
        }
    }
    
    /**
     * Get the maximum {@link SequenceNumber}
     * 
     * @return The maximum {@link SequenceNumber}
     */
    public SequenceNumber getSeqNumMax() {
        CacheChange change = this.m_history.getMaxChange();
        if (change != null) {
            return change.getSequenceNumber();
        } else {
            return new SequenceNumber().setUnknown();
        }
    }
    
    /**
     * Get the maximum serialized size for the topic type
     * 
     * @return The maximum serialized size for the topic type
     */
    public int getTypeMaxSerialized() {
        return this.m_history.getTypeMaxSerialized();
    }
    
    /**
     * Indicates if the {@link CacheChange} has been acked by all {@link RTPSReader}s
     * 
     * @param change The {@link CacheChange} to be checked
     * @return true is it has been acked by all; false otherwise
     */
    public boolean isAckedByAll(CacheChange change) {
        return true;
    }
    
    /**
     * Adds a new matched {@link RemoteReaderAttributes}
     * 
     * @param ratt The {@link RemoteReaderAttributes} to be added
     * @return true on success; false otherwise
     */
    public abstract boolean matchedReaderAdd(RemoteReaderAttributes ratt);
    
    /**
     * Removes a new matched {@link RemoteReaderAttributes}
     * 
     * @param ratt The {@link RemoteReaderAttributes} to be removed
     * @return true on success; false otherwise
     */
    public abstract boolean matchedReaderRemove(RemoteReaderAttributes ratt);
    
    /**
     * Checks if a new {@link RemoteReaderAttributes} has matched
     * 
     * @param ratt The matched {@link RemoteReaderAttributes}
     * @return true if it matches; false otherwise
     */
    public abstract boolean matchedReaderIsMatched(RemoteReaderAttributes ratt);
    
    /**
     * Updates the {@link WriterAttributes}
     * 
     * @param att The {@link WriterAttributes} to copy the data from
     */
    public abstract void updateAttributes(WriterAttributes att);
    
    /**
     * Sends the unsent {@link CacheChange} objects
     */
    public abstract void unsentChangesNotEmpty();

    /**
     * This method is executed when a new {@link CacheChange} has been added to the {@link WriterHistoryCache} 
     * 
     * @param change The added {@link CacheChange} 
     */
    public abstract void unsentChangeAddedToHistory(CacheChange change);

    /**
     * This method is executed when a new {@link CacheChange} has been removed from the {@link WriterHistoryCache} 
     * 
     * @param change The removed {@link CacheChange}
     * @return true on successful removal; false otherwise
     */
    public abstract boolean changeRemovedByHistory(CacheChange change);
    
    /**
     * Set the livelinessAsserted attribute
     * 
     * @param value The new livelinessAsserted value
     */
    public void setLivelinessAsserted(boolean value) {
        this.m_livelinessAsserted = value;
    }
    
    /**
     * Get if liveliness has been asserted 
     * 
     * @return true if asserted; false otherwise
     */
    public boolean getLivelinessAsserted() {
        return this.m_livelinessAsserted;
    }

    /**
     * Get the {@link WriterListener}
     * 
     * @return The {@link WriterListener}
     */
    public WriterListener getListener() {
        return m_listener;
    }
}
