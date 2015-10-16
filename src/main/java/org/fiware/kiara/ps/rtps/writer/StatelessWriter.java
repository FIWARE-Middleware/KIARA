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

import org.fiware.kiara.ps.rtps.attributes.RemoteReaderAttributes;
import org.fiware.kiara.ps.rtps.attributes.WriterAttributes;
import org.fiware.kiara.ps.rtps.common.DurabilityKind;
import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.common.LocatorList;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.history.WriterHistoryCache;
import org.fiware.kiara.ps.rtps.messages.RTPSMessageGroup;
import org.fiware.kiara.ps.rtps.messages.elements.EntityId;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.EntityId.EntityIdEnum;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.writer.timedevent.UnsentChangesNotEmptyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* This class represents a Stateless {@link RTPSWriter}
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public class StatelessWriter extends RTPSWriter {
    
    /**
     * The {@link ReaderLocator} list
     */
    private List<ReaderLocator> m_readerLocator;
    
    /**
     * The list of {@link RemoteReaderAttributes} references
     */
    private List<RemoteReaderAttributes> m_matchedReaders;
    
    /**
     * Logging object
     */
    private static final Logger logger = LoggerFactory.getLogger(StatelessWriter.class);

    /**
     * {@link StatelessWriter} constructor
     * 
     * @param participant The {@link RTPSParticipant} that creates the {@link StatelessWriter}
     * @param guid The {@link StatelessWriter} {@link GUID}
     * @param att The {@link WriterAttributes} for configuration
     * @param history The {@link WriterHistoryCache} to store the new {@link CacheChange}
     * @param listener The {@link WriterListener} to be called
     */
    public StatelessWriter(RTPSParticipant participant, GUID guid, WriterAttributes att, WriterHistoryCache history, WriterListener listener) {
        super(participant, guid, att, history, listener);
        this.m_readerLocator = new ArrayList<ReaderLocator>();
        this.m_matchedReaders = new ArrayList<RemoteReaderAttributes>();
    }
    
    /**
     * Destroys the {@link StatelessWriter}
     */
    @Override
    public void destroy() {
        logger.debug("RTPS WRITER: StatefulWriter destructor");
        super.destroy();
        m_matchedReaders.clear();
    }
    
    @Override
    public void unsentChangeAddedToHistory(CacheChange change) {
        this.m_mutex.lock();
        try {
            List<CacheChange> changes = new ArrayList<CacheChange>();
            changes.add(change);
            
            LocatorList locList = new LocatorList();
            LocatorList locList2 = new LocatorList();
            
            this.setLivelinessAsserted(true);
            
            if (!this.m_readerLocator.isEmpty()) {
                
                for (ReaderLocator it : this.m_readerLocator) {
                    locList.pushBack(it.getLocator());
                }
                
                if (this.m_guid.getEntityId().equals(new EntityId(EntityIdEnum.ENTITYID_SPDP_BUILTIN_RTPSPARTICIPANT_WRITER))) {
                    RTPSMessageGroup.sendChangesAsData((RTPSWriter) this, changes, locList, locList2, false, new EntityId(EntityIdEnum.ENTITYID_SPDP_BUILTIN_RTPSPARTICIPANT_READER));
                } else {
                    RTPSMessageGroup.sendChangesAsData((RTPSWriter) this, changes, locList, locList2, false, new EntityId(EntityIdEnum.ENTITYID_UNKNOWN));
                }
                
            } else {
                logger.warn("No reader locator to send change");
            }
        } finally {
            this.m_mutex.unlock();
        }
        
    }
    
    @Override
    public boolean changeRemovedByHistory(CacheChange change) {
        return true;
    }
    
    @Override
    public void unsentChangesNotEmpty() {
        this.m_mutex.lock();
        int total = 0;
        try {
            for (ReaderLocator it : this.m_readerLocator) {
                if (!it.getUnsentChanges().isEmpty()) {
                    total = it.getUnsentChanges().size();
                    if (this.m_pushMode) {
                        if (this.m_guid.getEntityId().equals(new EntityId(EntityIdEnum.ENTITYID_SPDP_BUILTIN_RTPSPARTICIPANT_WRITER))) {
                            RTPSMessageGroup.sendChangesAsData((RTPSWriter) this, it.getUnsentChanges(), it.getLocator(), it.getExpectsInlineQos(), new EntityId(EntityIdEnum.ENTITYID_SPDP_BUILTIN_RTPSPARTICIPANT_READER));
                        } else {
                            RTPSMessageGroup.sendChangesAsData((RTPSWriter) this, it.getUnsentChanges(), it.getLocator(), it.getExpectsInlineQos(), new EntityId(EntityIdEnum.ENTITYID_UNKNOWN));
                        }
                        it.getUnsentChanges().clear();
                    }
                }
            }
            logger.debug("Finished sending unsent changes (Total sent: {})", total);
        } finally {
            this.m_mutex.unlock();
        }
    }
    
    @Override
    public boolean matchedReaderAdd(RemoteReaderAttributes ratt) {
       
        this.m_mutex.lock();
        try {
            
            if (!ratt.guid.equals(new GUID())) {
                for (RemoteReaderAttributes it : this.m_matchedReaders) {
                    if (it.guid.equals(ratt.guid)) {
                        logger.warn("Attempting to add existing reader");
                        return false;
                    }
                }
            }
            
            boolean unsentChangesNotEmpty = false;
            
            for (Locator lit : ratt.endpoint.unicastLocatorList.getLocators()) {
                unsentChangesNotEmpty |= addLocator(ratt, lit);
            }
            
            for (Locator lit : ratt.endpoint.multicastLocatorList.getLocators()) {
                unsentChangesNotEmpty |= addLocator(ratt, lit);
            }
            
            if (unsentChangesNotEmpty) {
                this.m_unsentChangesNotEmpty = new UnsentChangesNotEmptyEvent(this, 1000);
            }
            
            this.m_matchedReaders.add(ratt);
            
        } finally {
            this.m_mutex.unlock();
        }
        
        return true;
    }
    
    /**
     * Adds a new {@link Locator} to the {@link StatelessWriter}
     * 
     * @param ratt The attributes of the {@link StatelessWriter}
     * @param loc The {@link Locator} to be added
     * @return true on success; false otherwise
     */
    public boolean addLocator(RemoteReaderAttributes ratt, Locator loc) {
        logger.debug("Adding Locator {} to StatelessWriter with GUID {}", loc.toString(), this.m_guid);
        boolean found = false;
        ReaderLocator end = null;
        for (ReaderLocator it: this.m_readerLocator) {
            if (it.getLocator().equals(loc)) {
                it.increaseUsed();
                found = true;
                end = it;
                break;
            }
        }
        if (!found) {
            ReaderLocator rl = new ReaderLocator();
            rl.setExpectsInlineQos(ratt.expectsInlineQos);
            rl.setLocator(loc);
            this.m_readerLocator.add(rl);
            end = this.m_readerLocator.get(this.m_readerLocator.size()-1);
        }
        if (ratt.endpoint.durabilityKind == DurabilityKind.TRANSIENT_LOCAL) {
            for (CacheChange it : this.m_history.getChanges()) {
                if (end != null) {
                    end.getUnsentChanges().add(it);
                }
            }
        }
        if (end != null && !end.getUnsentChanges().isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean matchedReaderRemove(RemoteReaderAttributes ratt) {
        this.m_mutex.lock();
        try {
            
            boolean found = false;
            if (ratt.guid.equals(new GUID())) {
                found = true;
            } else {
                for (int i=0; i < this.m_matchedReaders.size(); ++i) {
                    RemoteReaderAttributes it = this.m_matchedReaders.get(i);
                    if (it.guid.equals(ratt.guid)) {
                        found = true;
                        this.m_matchedReaders.remove(it);
                        i--;
                        break;
                    }
                }
            }
            if (found) {
                logger.debug("Reader Proxy removed");
                for (Locator lit : ratt.endpoint.unicastLocatorList.getLocators()) {
                    removeLocator(lit);
                }
                for (Locator lit : ratt.endpoint.multicastLocatorList.getLocators()) {
                    removeLocator(lit);
                }
                return true;
            }
            
            return false;
            
        } finally {
            this.m_mutex.unlock();
        }
    }
    
    /**
     * Removes the specified {@link Locator}
     * 
     * @param loc The {@link Locator} to remove
     * @return true on success; false otherwise
     */
    private boolean removeLocator(Locator loc) {
        for (int i=0; i < this.m_readerLocator.size(); ++i) {
            ReaderLocator it = this.m_readerLocator.get(i);
            if (it.getLocator().equals(loc)) {
                it.decreaseUsed();
                if (it.getUsed() == 0) {
                    this.m_readerLocator.remove(it);
                    i--;
                }
                break;
            }
        }
        return true;
    }

    @Override
    public boolean matchedReaderIsMatched(RemoteReaderAttributes ratt) {
        this.m_mutex.lock();
        try {
            
            for (RemoteReaderAttributes it : this.m_matchedReaders) {
                if (it.guid.equals(ratt.guid)) {
                    return true;
                }
            }
            return false;
            
        } finally {
            this.m_mutex.unlock();
        }
    }
    
    /**
     * Resets all the unsent {@link CacheChange} references and sends them
     */
    public void unsentChangesReset() {
        this.m_mutex.lock();
        try {
            
            for (ReaderLocator it : this.m_readerLocator) {
                it.getUnsentChanges().clear();
                for (CacheChange change : this.m_history.getChanges()) {
                    it.getUnsentChanges().add(change);
                }
            }
            unsentChangesNotEmpty();
            
        } finally {
            this.m_mutex.unlock();
        }
    }

    @Override
    public void updateAttributes(WriterAttributes att) {
        // Do Nothing (for now)
    }

   

}
