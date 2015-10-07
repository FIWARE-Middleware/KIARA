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

import java.util.ArrayList;
import java.util.List;

import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.messages.elements.SequenceNumber;

import com.google.common.cache.Cache;

/**
* Class that represent a reader locator
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public class ReaderLocator {
    
    /**
     * Associated {@link Locator}
     */
    private Locator m_locator;
    
    /**
     * Indicated whether to expect InlineQoS or not
     */
    private boolean m_expectsInlineQos;
    
    /**
     * Lits of requested {@link CacheChange}s
     */
    private List<CacheChange> m_requestedChanges;
    
    /**
     * List of unsent {@link CacheChange}s
     */
    private List<CacheChange> m_unsentChanges;
    
    /**
     * Number of times used 
     */
    private int m_nUsed;
    
    /**
     * Default {@link ReaderLocator} constructor
     */
    public ReaderLocator() {
        this.m_expectsInlineQos = false;
        this.m_nUsed = 1;
        this.m_requestedChanges = new ArrayList<CacheChange>();
        this.m_unsentChanges = new ArrayList<CacheChange>();
    }
    
    /**
     * Alternative {@link ReaderLocator} constructor
     * 
     * @param locator Associated {@link Locator}
     * @param expectsQos Indicates whether to expext InlineQoS or not
     */
    public ReaderLocator(Locator locator, boolean expectsQos) {
        this.m_expectsInlineQos = false;
        this.m_nUsed = 1;
        this.m_locator = locator;
        this.m_expectsInlineQos = expectsQos;
        this.m_requestedChanges = new ArrayList<CacheChange>();
        this.m_unsentChanges = new ArrayList<CacheChange>();
    }
    
    /**
     * Get the next requested {@link CacheChange}
     * 
     * @return The next requested {@link CacheChange}
     */
    public CacheChange nextRequestedChange() {
        CacheChange retVal = null;
        if (!this.m_requestedChanges.isEmpty()) {
            SequenceNumber minSeqNum = this.m_requestedChanges.get(0).getSequenceNumber();
            for (CacheChange it : this.m_requestedChanges) {
                if (minSeqNum.isGreaterThan(it.getSequenceNumber())) {
                    minSeqNum = it.getSequenceNumber();
                    retVal = it;
                }
            }
        }
        return retVal;
    }
    
    /**
     * Get the next unsent {@link CacheChange}
     * 
     * @return The next unsent {@link CacheChange}
     */
    public CacheChange nextUnsentChange() {
        CacheChange retVal = null;
        if (!this.m_unsentChanges.isEmpty()) {
            SequenceNumber minSeqNum = this.m_unsentChanges.get(0).getSequenceNumber();
            retVal = this.m_unsentChanges.get(0);
            for (CacheChange it : this.m_unsentChanges) {
                if (minSeqNum.isGreaterThan(it.getSequenceNumber())) {
                    minSeqNum = it.getSequenceNumber();
                    retVal = it;
                }
            }
        }
        return retVal;
    }
    
    /**
     * Removes the next requested {@link CacheChange}
     * 
     * @param change The next requested {@link CacheChange}
     * @return true on success; false otherwise
     */
    public boolean removeRequestedChange(CacheChange change) {
        for (int index = 0; index < this.m_requestedChanges.size(); index++) {
            CacheChange it = this.m_requestedChanges.get(index);
            if (change.equals(it)) {
                this.m_requestedChanges.remove(it);
                index--;
                return true;
            }
        }
        return false;
    }
    
    /**
     * Removes the next unsent {@link CacheChange}
     * 
     * @param change The next unsent {@link CacheChange}
     * @return true on success; false otherwise
     */
    public boolean removeUnsentChange(CacheChange change) {
        for (int index = 0; index < this.m_unsentChanges.size(); index++) {
            CacheChange it = this.m_unsentChanges.get(index);
            if (change.equals(it)) {
                this.m_unsentChanges.remove(it);
                index--;
                return true;
            }
        }
        return false;
    }

    /**
     * Get the associated {@link Locator}
     * 
     * @return The associated {@link Locator}
     */
    public Locator getLocator() {
        return m_locator;
    }

    /**
     * Set the associated {@link Locator}
     * 
     * @param m_locator The {@link Locator} to be set
     */
    public void setLocator(Locator m_locator) {
        this.m_locator = m_locator;
    }

    /**
     * Get the expectsInlineQos attribute
     * 
     * @return The expectsInlineQos attribute
     */
    public boolean getExpectsInlineQos() {
        return m_expectsInlineQos;
    }

    /**
     * Set the expectsInlineQos attribute
     * 
     * @param m_expectsInlineQos The expectsInlineQos attribute new value
     */
    public void setExpectsInlineQos(boolean m_expectsInlineQos) {
        this.m_expectsInlineQos = m_expectsInlineQos;
    }

    /**
     * Get the requested {@link Cache}
     * 
     * @return The requested {@link Cache} list
     */
    public List<CacheChange> getRequestedChanges() {
        return m_requestedChanges;
    }

    /**
     * Set the requested {@link CacheChange} list
     * 
     * @param m_requestedChanges The requested {@link CacheChange} list
     */
    public void setRequestedChanges(List<CacheChange> m_requestedChanges) {
        this.m_requestedChanges = m_requestedChanges;
    }

    /**
     * Get the list of unsent {@link CacheChange}s
     * 
     * @return The list of unsent {@link CacheChange}s
     */
    public List<CacheChange> getUnsentChanges() {
        return m_unsentChanges;
    }

    /**
     * Set the list of unsent {@link CacheChange}s
     * 
     * @param m_unsentChanges The list of unsent {@link CacheChange}s
     */
    public void setUnsentChanges(List<CacheChange> m_unsentChanges) {
        this.m_unsentChanges = m_unsentChanges;
    }

    /**
     * Get the nUsed attribute
     * 
     * @return The nUsed attribute
     */
    public int getUsed() {
        return m_nUsed;
    }

    /**
     * Set the nUsed atribute
     * 
     * @param nUsed The new nUsed value to be set
     */
    public void setUsed(int nUsed) {
        this.m_nUsed = nUsed;
    }
    
    /**
     * Increases the nUsed attribute in one unit
     */
    public void increaseUsed() {
        ++this.m_nUsed;
    }

    /**
     * Decreases the nUsed attribute in one unit
     */
    public void decreaseUsed() {
        --this.m_nUsed;
    }
    
}
