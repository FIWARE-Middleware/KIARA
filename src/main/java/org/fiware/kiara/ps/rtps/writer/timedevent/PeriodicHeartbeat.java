/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2015 German Research Center for Artificial Intelligence (DFKI)
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
import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.common.LocatorList;
import org.fiware.kiara.ps.rtps.messages.RTPSMessage;
import org.fiware.kiara.ps.rtps.messages.RTPSMessageBuilder;
import org.fiware.kiara.ps.rtps.messages.common.types.RTPSEndian;
import org.fiware.kiara.ps.rtps.messages.elements.SequenceNumber;
import org.fiware.kiara.ps.rtps.resources.TimedEvent;

import static org.fiware.kiara.ps.rtps.resources.TimedEvent.EventCode.EVENT_ABORT;
import static org.fiware.kiara.ps.rtps.resources.TimedEvent.EventCode.EVENT_SUCCESS;

import org.fiware.kiara.ps.rtps.writer.ReaderProxy;
import org.fiware.kiara.ps.rtps.writer.StatefulWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public class PeriodicHeartbeat extends TimedEvent {

    private static final Logger logger = LoggerFactory.getLogger(PeriodicHeartbeat.class);

    private StatefulWriter m_SFW;

    public PeriodicHeartbeat(StatefulWriter writer, double interval) {
        super(interval);
        m_SFW = writer;
    }

    public void destroy() {

    }

    @Override
    public void event(EventCode code, String msg) {
        if (code == EVENT_SUCCESS) {
            final SequenceNumber firstSeq = new SequenceNumber();
            final SequenceNumber lastSeq = new SequenceNumber();
            final LocatorList locList = new LocatorList();
            boolean unacked_changes = false;

            final Lock mutex = m_SFW.getMutex();
            mutex.lock();
            try {//BEGIN PROTECTION
                List<ChangeForReader> unack = new ArrayList<ChangeForReader>();
                for (ReaderProxy it : m_SFW.getMatchedReaders()) {
                    unack.clear();
                    if (!unacked_changes) {
                        unack = it.unackedChanges();
                        if (!unack.isEmpty()) {
                            unacked_changes = true;
                        }
                    }
                    locList.pushBack(it.att.endpoint.unicastLocatorList);
                    locList.pushBack(it.att.endpoint.multicastLocatorList);
                }
                firstSeq.copy(m_SFW.getSeqNumMin());
                lastSeq.copy(m_SFW.getSeqNumMax());
            } finally {
                mutex.unlock();
            }

            if (unacked_changes) {
                if (!firstSeq.isUnknown() && !lastSeq.isUnknown() && lastSeq.isGreaterOrEqualThan(firstSeq)) {
                    m_SFW.incrementHBCount();
                    RTPSMessage rtpsMessage = RTPSMessageBuilder.createMessage(RTPSEndian.LITTLE_ENDIAN); // TODO Think about default endian
                    RTPSMessageBuilder.addHeader(rtpsMessage, m_SFW.getGuid().getGUIDPrefix());
                    RTPSMessageBuilder.addSubmessageHeartbeat(rtpsMessage,
                            m_SFW.getHBReaderEntityId(),
                            m_SFW.getGuid().getEntityId(),
                            firstSeq,
                            lastSeq,
                            m_SFW.getHeartbeatCount(),
                            false,
                            false);

//                    logger.info("RTPS WRITER: {} Sending Heartbeat ({} - {})",
//                            m_SFW.getGuid().getEntityId(), firstSeq, lastSeq
//                    );
                    rtpsMessage.serialize();
                    for (Locator lit : locList) {
                        m_SFW.getRTPSParticipant().sendSync(rtpsMessage, lit);
                    }

                }
                //Reset TIMER
                //restartTimer();
            } else {
                stopTimer();
            }

        } else if (code == EVENT_ABORT) {
            logger.info("RTPS WRITER: Aborted");
            stopSemaphorePost();
        } else {
            logger.info("RTPS WRITER: Boost message: {}");
        }

    }

}
