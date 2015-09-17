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
package org.fiware.kiara.ps.rtps.history;

import org.fiware.kiara.ps.rtps.messages.common.types.ChangeKind;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.fiware.kiara.ps.rtps.messages.elements.SequenceNumber;
import org.fiware.kiara.ps.rtps.messages.elements.SerializedPayload;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;

/**
 * Structure CacheChange, contains information on a specific cache change.
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class CacheChange implements Comparable<CacheChange> {

    /**
     * Enumeration specifying the {@link ChangeKind} of this change
     */
    private ChangeKind m_changeKind;

    /**
     * Object containing the {@link SerializedPayload} of the CacheChange
     */
    private SerializedPayload m_payload;

    /**
     * {@link GUID} of the CacheChange Writer 
     */
    private GUID m_writerGUID;

    /**
     * {@link SequenceNumber} of the CacheChange
     */
    private SequenceNumber m_sequenceNumber;

    /**
     * {@link InstanceHandle} attached to the CacheChangebiee
     */
    private InstanceHandle m_instanceHandle;

    /**
     * Boolean value indicating whether the CacheChange has been read or not
     */
    boolean m_isRead = false;

    /**
     * {@link Timestamp} associated to this CacheChange
     */
    Timestamp m_sourceTimestamp;

    public CacheChange() {
        this.m_changeKind = ChangeKind.ALIVE;
        this.m_payload = new SerializedPayload();
        this.m_writerGUID = new GUID();
        this.m_sequenceNumber = new SequenceNumber();
        this.m_instanceHandle = new InstanceHandle();
        this.m_sourceTimestamp = new Timestamp();
    }

    /**
     * Get the ChangeKind of the CacheChange
     * 
     * @return ChangeKind of the CacheChange
     */
    public ChangeKind getKind() {
        return this.m_changeKind;
    }

    /**
     * Set the ChangeKind of the CacheChange
     * 
     * @param changeKind ChangeKind to be set
     */
    public void setKind(ChangeKind changeKind) {
        this.m_changeKind = changeKind;
    }

    /**
     * Get the SerializedPayload contained in the CacheChange
     * 
     * @return The SerializedPayload contained in the CacheChange
     */
    public SerializedPayload getSerializedPayload() {
        return this.m_payload;
    }

    /**
     * Set the SerializedPayload contained in the CacheChange
     * 
     * @param payload The SerializedPayload to be set
     */
    public void setSerializedPayload(SerializedPayload payload) {
        this.m_payload = payload;
    }

    /**
     * Get the writer GUID
     * 
     * @return The writer GUID
     */
    public GUID getWriterGUID() {
        return this.m_writerGUID;
    }

    /**
     * Set the writer GUID
     * 
     * @param guid The writer GUID to be set
     */
    public void setWriterGUID(GUID guid) {
        this.m_writerGUID = guid;
    }

    /**
     * Get the SequenceNumber associated to the CacheChange
     * 
     * @return The SequenceNumber of the CacheChange
     */
    public SequenceNumber getSequenceNumber() {
        return this.m_sequenceNumber;
    }

    /**
     * Set the SequenceNumber of the CacheChange
     * 
     * @param seqNum The SequenceNumber to be set
     */
    public void setSequenceNumber(SequenceNumber seqNum) {
        this.m_sequenceNumber = seqNum;
    }

    /**
     * Get the InstanceHandle of the CacheChange
     * 
     * @return The InstanceHandle of the CacheChange
     */
    public InstanceHandle getInstanceHandle() {
        return m_instanceHandle;
    }

    /**
     * Set the InstanceHandle of the CacheChange
     * 
     * @param m_instanceHandle The InstanceHandle to be set
     */
    public void setInstanceHandle(InstanceHandle m_instanceHandle) {
        this.m_instanceHandle = m_instanceHandle;
    }

    /**
     * Returns whether the CacheChange has been read
     * 
     * @return true if the CacheChange has been read; false otherwise
     */
    public boolean isRead() {
        return m_isRead;
    }

    /**
     * Set the read value of the CacheChange
     * 
     * @param isRead The boolean value to be set
     */
    public void setRead(boolean isRead) {
        this.m_isRead = isRead;
    }

    /**
     * Get the Timestamp associated to the CacheChange
     * 
     * @return The associated Timestamp
     */
    public Timestamp getSourceTimestamp() {
        return m_sourceTimestamp;
    }

    /**
     * Set the associated Timestamp
     * 
     * @param m_sourceTimestamp The Timestamp to be set
     */
    public void setSourceTimestamp(Timestamp m_sourceTimestamp) {
        this.m_sourceTimestamp = m_sourceTimestamp;
    }

    /**
     * Compares the CacheChange with another CacheChange
     */
    @Override
    public int compareTo(CacheChange o) {
        if (this.m_sequenceNumber.equals(((CacheChange) o).m_sequenceNumber)){
            return 0;
        } else if (this.m_sequenceNumber.isGreaterOrEqualThan(((CacheChange) o).m_sequenceNumber)){
            return 1;
        } else if (this.m_sequenceNumber.isLowerOrEqualThan(((CacheChange) o).m_sequenceNumber)){
            return -1;
        }
        return 0;
    }

    /**
     * Copy a different change into this one 8all the elements are copied, included the data)
     * 
     * @param ch The CacheChange reference
     * @return true if the content can be copied; false otherwise
     */
    public boolean copy(CacheChange ch) {
        this.m_changeKind = ch.m_changeKind;
        this.m_writerGUID = ch.m_writerGUID;
        this.m_instanceHandle = ch.m_instanceHandle;
        this.m_sourceTimestamp = ch.m_sourceTimestamp;
        this.m_sequenceNumber = ch.m_sequenceNumber;
        return this.m_payload.copy(ch.getSerializedPayload());
    }


}
