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
package org.fiware.kiara.ps.rtps.common;

import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.messages.elements.SequenceNumber;

/**
*
* Struct ChangeFromWriter used to indicate the state of a specific change 
* with respect to a specific writer, as well as its relevance.
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public class ChangeFromWriter {

    /**
     * {@link ChangeFromWriterStatus} representing the status
     */
    public ChangeFromWriterStatus status;

    /**
     * Boolean specifying if this change is relevant
     */
    public boolean isRelevant;

    /**
     * The {@link SequenceNumber} of the change
     */
    public final SequenceNumber seqNum;

    /**
     * Boolean specifying if this change is valid
     */
    private boolean m_isValid;

    /**
     * {@link CacheChange} associated to the change
     */
    private CacheChange m_change;

    public ChangeFromWriter() {
        this.status = ChangeFromWriterStatus.UNKNOWN;
        this.isRelevant = true;
        this.seqNum = new SequenceNumber();
        this.m_isValid = false;
    }

    /**
     * Get the CacheChange
     * 
     * @return CacheChange
     */
    public CacheChange getChange() {
        return this.m_change;
    }

    /**
     * Set the ChacheChange attribute
     * 
     * @param change The CacheChange object to be set
     * @return boolean value indicating if the CacheChange has been correctly set
     */
    public boolean setChange(CacheChange change) {
        this.m_isValid = true;
        this.seqNum.copy(change.getSequenceNumber());
        this.m_change = change;
        return true;
    }

    /**
     * Set change as not valid
     */
    public void notValid() {
        isRelevant = false;
        m_isValid = false;
        m_change = null;
    }

    /**
     * Get the boolean value of the isValid attribute
     * 
     * @return The boolean isValid attribute
     */
    public boolean isValid() {
        return this.m_isValid;
    }

}
