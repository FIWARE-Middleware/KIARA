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
package org.fiware.kiara.ps.rtps.reader.timedevent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.fiware.kiara.ps.rtps.common.ChangeFromWriter;
import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.messages.RTPSMessage;
import org.fiware.kiara.ps.rtps.messages.RTPSMessageBuilder;
import org.fiware.kiara.ps.rtps.messages.common.types.RTPSEndian;
import org.fiware.kiara.ps.rtps.messages.elements.Count;
import org.fiware.kiara.ps.rtps.messages.elements.SequenceNumber;
import org.fiware.kiara.ps.rtps.messages.elements.SequenceNumberSet;
import org.fiware.kiara.ps.rtps.reader.WriterProxy;
import org.fiware.kiara.ps.rtps.resources.TimedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link TimedEvent} class to periodically check the received CacheChanges
 * and send the appropiate ACKNACK messages
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public class HeartbeatResponseDelay extends TimedEvent {
    
    /**
     * {@link WriterProxy} who sent the data
     */
    public WriterProxy writerProxy;
    
    /**
     * ACKNACK {@link RTPSMessage} 
     */
    public RTPSMessage heartbeatResponseMsg;
    
    /**
     * Logging object
     */
    private static final Logger logger = LoggerFactory.getLogger(HeartbeatResponseDelay.class);
    
    /**
     * Mutex
     */
    private final Lock m_mutex = new ReentrantLock(true);
    
    /**
     * {@link HeartbeatResponseDelay} constructor
     * 
     * @param proxy WriterProxy to request changes
     * @param interval Time interval to resend the ACKNACK message
     */
    public HeartbeatResponseDelay(WriterProxy proxy, double interval) {
        super(interval); 
        this.writerProxy = proxy;
    }
    
    /**
     * Main event behaviour
     */
    public void event(EventCode code, String msg) {
        
        if (code == EventCode.EVENT_SUCCESS) {
            List<ChangeFromWriter> changeList = new ArrayList<ChangeFromWriter>();
            this.m_mutex.lock();
            try {
                changeList = this.writerProxy.getMissingChanges();
            } finally {
                this.m_mutex.unlock();
            }
            if (changeList != null) {
                if (!changeList.isEmpty() || !this.writerProxy.hearbeatFinalFlag) {
                    SequenceNumberSet sns = new SequenceNumberSet();
                    SequenceNumber base = this.writerProxy.getAvailableChangesMax();
                    if (base == null) { // No changes available
                        logger.error("No available changes");
                    }
                    sns.setBase(base);
                    sns.getBase().increment();
                    
                    for (ChangeFromWriter cit : changeList) {
                        if (!sns.add(cit.seqNum)) {
                            logger.warn("Error adding seqNum " + cit.seqNum.toLong() + " with SeqNumSet Base: " + sns.getBase().toLong());
                            break;
                        }
                    }
                    
                    this.writerProxy.acknackCount++;
                    logger.debug("Sending ACKNACK");
                    
                    boolean isFinal = false;
                    if (sns.isSetEmpty()) {
                        isFinal = true;
                    }
                    
                    RTPSMessage rtpsMessage = RTPSMessageBuilder.createMessage(RTPSEndian.LITTLE_ENDIAN); // TODO Think about default endian
                    RTPSMessageBuilder.addHeader(rtpsMessage, this.writerProxy.statefulReader.getGuid().getGUIDPrefix());
                    RTPSMessageBuilder.addSubmessageAckNack(
                        rtpsMessage, 
                        this.writerProxy.statefulReader.getGuid().getEntityId(), 
                        this.writerProxy.att.guid.getEntityId(), 
                        sns, 
                        new Count(this.writerProxy.acknackCount), 
                        isFinal
                    );
                    
                    rtpsMessage.serialize();
                    
                    for (Locator lit : this.writerProxy.att.endpoint.unicastLocatorList.getLocators()) {
                        this.writerProxy.statefulReader.getRTPSParticipant().sendSync(rtpsMessage, lit);
                    }
                    
                    for (Locator lit : this.writerProxy.att.endpoint.multicastLocatorList.getLocators()) {
                        this.writerProxy.statefulReader.getRTPSParticipant().sendSync(rtpsMessage, lit);
                    }
                }
            }
        } else if (code == EventCode.EVENT_ABORT) {
            logger.debug("Response aborted");
            this.stopSemaphorePost();
        } else {
            logger.debug("Response message " + msg);
        }
        
        this.stopTimer();
        
    }

}
