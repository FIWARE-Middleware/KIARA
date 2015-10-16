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

import java.util.concurrent.locks.Lock;

import org.fiware.kiara.ps.rtps.common.ChangeForReader;
import org.fiware.kiara.ps.rtps.common.ChangeForReaderStatus;
import org.fiware.kiara.ps.rtps.common.ReliabilityKind;
import org.fiware.kiara.ps.rtps.resources.TimedEvent;
import org.fiware.kiara.ps.rtps.writer.ReaderProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents the Nack supression duration event
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public class NackSupressionDuration extends TimedEvent {

    /**
     * {@link ReaderProxy} to send data to
     */
    private ReaderProxy m_RP;

    /**
     * Logging object 
     */
    private static final Logger logger = LoggerFactory.getLogger(NackSupressionDuration.class);

    /**
     * {@link NackSupressionDuration} constructor
     * 
     * @param RP he {@link ReaderProxy} the is being sent to
     * @param milliseconds Time interval in milliseconds
     */
    public NackSupressionDuration(ReaderProxy RP, double milliseconds) {
        super(milliseconds);
        this.m_RP = RP;
    }

    /**
     * Mein method
     */
    @Override
    public void event(EventCode code, String msg) {
        this.m_mutex.lock();
        try {
            if (code == EventCode.EVENT_SUCCESS) {

                logger.debug("Nack supression event");
                Lock guardW = this.m_RP.getSFW().getMutex();
                guardW.lock();
                try {
                    for (ChangeForReader cit : this.m_RP.getChangesForReader()) {
                        if (cit.status == ChangeForReaderStatus.UNDERWAY) {
                            if (this.m_RP.att.endpoint.reliabilityKind == ReliabilityKind.RELIABLE) {
                                cit.status = ChangeForReaderStatus.UNACKNOWLEDGED;
                            } else {
                                cit.status = ChangeForReaderStatus.ACKNOWLEDGED;
                            }
                        }
                    }

                    this.stopTimer();

                } finally {
                    guardW.unlock();
                }

            } else if (code == EventCode.EVENT_ABORT) {
                logger.info("Nack supression aborted");
            } else {
                logger.info("Nack response message: {}", msg);
            }
        } finally {
            this.m_mutex.unlock();
        }
    }

}
