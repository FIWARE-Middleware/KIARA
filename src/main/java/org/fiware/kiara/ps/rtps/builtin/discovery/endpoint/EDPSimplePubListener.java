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
package org.fiware.kiara.ps.rtps.builtin.discovery.endpoint;

import java.util.List;
import java.util.concurrent.locks.Lock;

import org.fiware.kiara.ps.rtps.builtin.data.ParticipantProxyData;
import org.fiware.kiara.ps.rtps.builtin.data.WriterProxyData;
import org.fiware.kiara.ps.rtps.common.MatchingInfo;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.messages.common.types.ChangeKind;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.fiware.kiara.ps.rtps.reader.RTPSReader;
import org.fiware.kiara.ps.rtps.reader.ReaderListener;
import org.fiware.kiara.ps.rtps.writer.RTPSWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents the {@link ReaderListener} to be called by the EDP
 * builtin publisher 
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public class EDPSimplePubListener extends ReaderListener {
    
    /**
     * {@link EDPSimple} reference to the object representing the Endpoint Discovery Protocol
     */
    public EDPSimple edpSimple;
    
    /**
     * {@link WriterProxyData} representing a remote {@link RTPSWriter}
     */
    public WriterProxyData writerProxyData;
    
    /**
     * Logging object
     */
    private static final Logger logger = LoggerFactory.getLogger(EDPSimplePubListener.class);
    
    /**
     * Default {@link EDPSimplePubListener} constructor
     * 
     * @param edpSimple {@link EDPSimple} reference
     */
    public EDPSimplePubListener(EDPSimple edpSimple) {
        this.edpSimple = edpSimple;
        this.writerProxyData = new WriterProxyData();
    }

    /**
     * Method to be executed when a new {@link RTPSReader} matches
     */
    @Override
    public void onReaderMatched(RTPSReader reader, MatchingInfo info) {
        // Do Nothing
    }

    /**
     * Method to be executed when a new {@link CacheChange} has been added  
     */
    @Override
    public void onNewCacheChangeAdded(RTPSReader reader, CacheChange change) {
        // TODO Auto-generated method stub
        if (!computeKey(change)) {
            logger.warn("Received change with no Key");
        }
        if (change.getKind() == ChangeKind.ALIVE) {
            
            this.writerProxyData.clear();
            if (this.writerProxyData.readFromCDRMessage(change)) {
                change.setInstanceHandle(this.writerProxyData.getKey());
                if (this.writerProxyData.getGUID().getGUIDPrefix().equals(this.edpSimple.m_RTPSParticipant.getGUID().getGUIDPrefix())) {
                    logger.info("Message from own RTPSParticipant, ignoring");
                    this.edpSimple.pubReaderPair.getSecond().removeChange(change);
                    return;
                }
                
                // Look if it is an updated information
                WriterProxyData wdata = new WriterProxyData();
                ParticipantProxyData pdata = new ParticipantProxyData();
                ParticipantProxyData pdataAux = new ParticipantProxyData();
                if (this.edpSimple.m_PDP.addWriterProxyData(this.writerProxyData, true, wdata, pdata)) { // Added new data
                    // Check locators
                    if (wdata.getUnicastLocatorList().isEmpty() && wdata.getMulticastLocatorList().isEmpty()) {
                        wdata.getUnicastLocatorList().copy(pdata.getDefaultMulticastLocatorList());
                        wdata.getMulticastLocatorList().copy(pdata.getDefaultMulticastLocatorList());
                    }
                    wdata.setIsAlive(true);
                    this.edpSimple.pairingWriterProxy(wdata);
                } else if (pdataAux.equals(pdata) == true) {
                    logger.warn("Message from UNKNOWN RTPSParticipant, removing");
                    this.edpSimple.pubReaderPair.getSecond().removeChange(change);
                    return;
                } else { // Not added, it was already there
                    Lock mutex = this.edpSimple.pubReaderPair.getSecond().getMutex();
                    mutex.lock();
                    try {
                        List<CacheChange> changes = this.edpSimple.pubReaderPair.getSecond().getChanges();
                        for (int i=0; i < changes.size(); ++i) {
                            CacheChange ch = changes.get(i);
                            if (ch.getInstanceHandle().equals(change.getInstanceHandle())) {
                                this.edpSimple.pubReaderPair.getSecond().removeChange(ch);
                                i--;
                            }
                        }
                        wdata.update(this.writerProxyData);
                        this.edpSimple.pairingWriterProxy(wdata);
                    } finally {
                        mutex.unlock();
                    }
                }
            }
        } else {
            logger.info("Disposed Remote Writer, removing...");
            GUID auxGUID = change.getInstanceHandle().toGUID();
            this.edpSimple.pubWriterPair.getSecond().removeChange(change);
            this.edpSimple.removeWriterProxy(auxGUID);
        }
    }
    
    /**
     * Method to compute the key
     * @param change CacheChange to store the computed key
     * @return boolean if sucess; false otherwise
     */
    public boolean computeKey(CacheChange change) {
        if (change.getInstanceHandle().equals(new InstanceHandle())) {
            // TODO Review
        }
        return true;
    }

}
