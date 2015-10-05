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

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public class ReaderLocator {
    // TODO Implement
    
    private Locator m_locator;
    
    private boolean m_expectsInlineQos;
    
    private List<CacheChange> m_requestedChanges;
    
    private List<CacheChange> m_unsentChanges;
    
    private int m_nUsed;
    
    public ReaderLocator() {
        this.m_expectsInlineQos = false;
        this.m_nUsed = 1;
        this.m_requestedChanges = new ArrayList<CacheChange>();
        this.m_unsentChanges = new ArrayList<CacheChange>();
    }
    
    public ReaderLocator(Locator locator, boolean expectsQos) {
        this.m_expectsInlineQos = false;
        this.m_nUsed = 1;
        this.m_locator = locator;
        this.m_expectsInlineQos = expectsQos;
        this.m_requestedChanges = new ArrayList<CacheChange>();
        this.m_unsentChanges = new ArrayList<CacheChange>();
    }
    
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

    public Locator getLocator() {
        return m_locator;
    }

    public void setLocator(Locator m_locator) {
        this.m_locator = m_locator;
    }

    public boolean getExpectsInlineQos() {
        return m_expectsInlineQos;
    }

    public void setExpectsInlineQos(boolean m_expectsInlineQos) {
        this.m_expectsInlineQos = m_expectsInlineQos;
    }

    public List<CacheChange> getRequestedChanges() {
        return m_requestedChanges;
    }

    public void setRequestedChanges(List<CacheChange> m_requestedChanges) {
        this.m_requestedChanges = m_requestedChanges;
    }

    public List<CacheChange> getUnsentChanges() {
        return m_unsentChanges;
    }

    public void setUnsentChanges(List<CacheChange> m_unsentChanges) {
        this.m_unsentChanges = m_unsentChanges;
    }

    public int getUsed() {
        return m_nUsed;
    }

    public void setUsed(int m_used) {
        this.m_nUsed = m_used;
    }
    
    public void increaseUsed() {
        ++this.m_nUsed;
    }

    public void decreaseUsed() {
        --this.m_nUsed;
    }
    
}
