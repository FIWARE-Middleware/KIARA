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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public class StatelessWriter extends RTPSWriter {
    
    // TODO Implement
    
    private List<ReaderLocator> m_readerLocator;
    
    private List<RemoteReaderAttributes> m_matchedReaders;
    
    //private final Lock m_mutex;
    
    private static final Logger logger = LoggerFactory.getLogger(StatelessWriter.class);

    public StatelessWriter(RTPSParticipant participant, GUID guid, WriterAttributes att, WriterHistoryCache history, WriterListener listener) {
        super(participant, guid, att, history, listener);
        //this.m_mutex = new ReentrantLock(true);
        this.m_readerLocator = new ArrayList<ReaderLocator>();
        this.m_matchedReaders = new ArrayList<RemoteReaderAttributes>();
        
        // TODO Remove this:
        //RemoteReaderAttributes rratt = new RemoteReaderAttributes();
        //rratt.endpoint.durabilityKind = DurabilityKind.TRANSIENT_LOCAL;
        
        /*Locator l = new Locator();
        try {
            byte [] addr = new byte[16];
            //byte [] obtainedAddr = InetAddress.getByName("239.255.0.1").getAddress();
            byte [] obtainedAddr = InetAddress.getByName("192.168.1.133").getAddress();
            addr[12] = obtainedAddr[0];
            addr[13] = obtainedAddr[1];
            addr[14] = obtainedAddr[2];
            addr[15] = obtainedAddr[3];
            l.setAddress(addr);
            l.setPort(27414);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        rratt.endpoint.multicastLocatorList.pushBack(l);
        matchedReaderAdd(rratt);*/
    }
    
    public void unsentChangeAddedToHistory(CacheChange change) {
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
        
    }
    
    @Override
    public boolean changeRemovedByHistory(CacheChange change) {
        return true;
    }
    
    @Override
    public void unsentChangesNotEmpty() {
        this.m_mutex.lock();
        try {
            for (ReaderLocator it : this.m_readerLocator) {
                if (!it.getUnsentChanges().isEmpty()) {
                    if (this.m_pushMode) {
                        if (this.m_guid.getEntityId() == new EntityId(EntityIdEnum.ENTITYID_SPDP_BUILTIN_RTPSPARTICIPANT_WRITER)) {
                            RTPSMessageGroup.sendChangesAsData((RTPSWriter) this, it.getUnsentChanges(), it.getLocator(), it.getExpectsInlineQos(), new EntityId(EntityIdEnum.ENTITYID_SPDP_BUILTIN_RTPSPARTICIPANT_WRITER));
                        } else {
                            RTPSMessageGroup.sendChangesAsData((RTPSWriter) this, it.getUnsentChanges(), it.getLocator(), it.getExpectsInlineQos(), new EntityId(EntityIdEnum.ENTITYID_UNKNOWN));
                        }
                        it.getUnsentChanges().clear();
                    }
                }
            }
            logger.info("Finished sending unsent changes");
        } finally {
            this.m_mutex.unlock();
        }
    }
    
    /*
     * MATCHED_READER-RELATED METHODS 
     */

    
    @Override
    public boolean matchedReaderAdd(RemoteReaderAttributes ratt) {
       
        this.m_mutex.lock();
        try {
            
            if (ratt.guid.equals(new GUID())) {
                for (RemoteReaderAttributes it : this.m_matchedReaders) {
                    if (it.guid.equals(ratt.guid)) {
                        logger.warn("Attempting to add existing reader");
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
                this.m_unsentChangesNotEmpty.restartTimer();
                this.m_unsentChangesNotEmpty = null;
            }
            
            this.m_matchedReaders.add(ratt);
            
        } finally {
            this.m_mutex.unlock();
        }
        
        return true;
    }
    
    public boolean addLocator(RemoteReaderAttributes ratt, Locator loc) {
        logger.info("Adding Locator: " + loc.toString() + " to StatelessWriter");
        boolean found = false;
        for (ReaderLocator it: this.m_readerLocator) {
            if (it.getLocator().equals(loc)) {
                it.increaseUsed();
                found = true;
                break;
            }
        }
        ReaderLocator end = null;
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
                logger.info("Reader Proxy removed");
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
        // TODO Do Nothing (for now)
    }

   

}
