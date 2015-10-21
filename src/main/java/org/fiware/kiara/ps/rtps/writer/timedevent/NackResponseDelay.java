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
package org.fiware.kiara.ps.rtps.writer.timedevent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

import org.fiware.kiara.ps.rtps.common.ChangeForReader;
import org.fiware.kiara.ps.rtps.common.ChangeForReaderStatus;
import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.messages.RTPSMessage;
import org.fiware.kiara.ps.rtps.messages.RTPSMessageBuilder;
import org.fiware.kiara.ps.rtps.messages.RTPSMessageGroup;
import org.fiware.kiara.ps.rtps.messages.common.types.RTPSEndian;
import org.fiware.kiara.ps.rtps.messages.elements.SequenceNumber;
import org.fiware.kiara.ps.rtps.resources.TimedEvent;
import org.fiware.kiara.ps.rtps.writer.ReaderProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for sending the responses to the ACKNACK messages
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public class NackResponseDelay extends TimedEvent {

    /**
     * The {@link ReaderProxy} that sends the ACKNACK
     */
    private ReaderProxy m_RP;

    /**
     * {@link RTPSMessage} to send in response
     */
    private RTPSMessage m_rtpsMessage;

    /**
     * Logging object
     */
    private static final Logger logger = LoggerFactory.getLogger(NackResponseDelay.class);

    /**
     * {@link NackResponseDelay} constructor
     * 
     * @param RP The {@link ReaderProxy} associated to the {@link NackResponseDelay}
     * @param milliseconds Time interval in milliseconds
     */
    public NackResponseDelay(ReaderProxy RP, double milliseconds) {
        super(milliseconds);
        this.m_RP = RP;
        this.m_rtpsMessage = RTPSMessageBuilder.createMessage(RTPSEndian.LITTLE_ENDIAN); // TODO Think about default endian
        RTPSMessageBuilder.addHeader(this.m_rtpsMessage, this.m_RP.getSFW().getGuid().getGUIDPrefix());
    }

    /**
     * Main method
     */
    @Override
    public void event(EventCode code, String msg) {
        // TODO Check if creating an RTPSMessage inside each if block solves sending all the NACK responses
        this.m_mutex.lock();
        try {
            if (code == EventCode.EVENT_SUCCESS) {
                logger.debug("Responding to Acknack msg");
                Lock guardW = this.m_RP.getSFW().getMutex();
                guardW.lock();
                try {
                    Lock guard = this.m_RP.getMutex();
                    guard.lock();
                    try {
                        List<ChangeForReader> vec = this.m_RP.requestedChanges();
                        if (!vec.isEmpty()) {
                            logger.debug("Requested {} changes", vec.size());
                            List<CacheChange> relevantChanges = new ArrayList<CacheChange>();
                            List<SequenceNumber> notRelevantChanges = new ArrayList<SequenceNumber>();
                            for (ChangeForReader cit : vec) {
                                cit.status = ChangeForReaderStatus.UNDERWAY;
                                if (cit.isRelevant && cit.isValid()) { 
                                    relevantChanges.add(cit.getChange());
                                } else {
                                    notRelevantChanges.add(cit.getSequenceNumber());
                                }
                            }
                            this.m_RP.isRequestedChangesEmpty = true;
                            if (!relevantChanges.isEmpty()) {
                                RTPSMessageGroup.sendChangesAsData(
                                        this.m_RP.getSFW(), 
                                        relevantChanges, 
                                        this.m_RP.att.endpoint.unicastLocatorList, 
                                        this.m_RP.att.endpoint.multicastLocatorList, 
                                        this.m_RP.att.expectsInlineQos, 
                                        this.m_RP.att.guid.getEntityId());
                            }
                            if (!notRelevantChanges.isEmpty()) {
                                RTPSMessageGroup.sendChangesAsGap(
                                        this.m_RP.getSFW(), 
                                        notRelevantChanges, 
                                        this.m_RP.att.guid.getEntityId(),
                                        this.m_RP.att.endpoint.unicastLocatorList, 
                                        this.m_RP.att.endpoint.multicastLocatorList);
                            }
                            if (relevantChanges.isEmpty() && notRelevantChanges.isEmpty()) {
                                RTPSMessage message = RTPSMessageBuilder.createMessage();
                                SequenceNumber first = this.m_RP.getSFW().getSeqNumMin();
                                SequenceNumber last = this.m_RP.getSFW().getSeqNumMin(); // TODO Review if this should be getSeqNumMax()
                                if (!first.isUnknown() && !last.isUnknown() && last.isGreaterOrEqualThan(first)) {
                                    this.m_RP.getSFW().incrementHBCount();
                                    RTPSMessageBuilder.addSubmessageHeartbeat(
                                            message, 
                                            this.m_RP.att.guid.getEntityId(), 
                                            this.m_RP.getSFW().getGuid().getEntityId(), 
                                            first, 
                                            last, 
                                            this.m_RP.getSFW().getHeartbeatCount(), 
                                            true, 
                                            false);
                                }
                                for (Locator lit : this.m_RP.att.endpoint.unicastLocatorList.getLocators()) {
                                    this.m_RP.getSFW().getRTPSParticipant().sendSync(message, lit);
                                }
                                for (Locator lit : this.m_RP.att.endpoint.multicastLocatorList.getLocators()) {
                                    this.m_RP.getSFW().getRTPSParticipant().sendSync(message, lit);
                                }

                            }
                        }
                        this.stopTimer();
                    } finally {
                        guard.unlock();
                    }
                } finally {
                    
                    guardW.unlock();
                }

            } else if (code == EventCode.EVENT_ABORT) {
                logger.debug("Nack response aborted");
                //this.stopSemaphorePost();
            } else {
                logger.debug("Nack response message: {}", msg);
            }
        } finally {
            this.m_mutex.unlock();
        }
    }

}
