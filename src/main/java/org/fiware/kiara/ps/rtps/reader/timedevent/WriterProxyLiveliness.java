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

import org.fiware.kiara.ps.rtps.common.MatchingInfo;
import org.fiware.kiara.ps.rtps.common.MatchingStatus;
import org.fiware.kiara.ps.rtps.reader.RTPSReader;
import org.fiware.kiara.ps.rtps.reader.WriterProxy;
import org.fiware.kiara.ps.rtps.resources.TimedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link TimedEvent} class to periodically check the writer liveliness
 * and remove not alive {@link WriterProxy}s
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public class WriterProxyLiveliness extends TimedEvent {

    /**
     * Logging object
     */
    private static final Logger logger = LoggerFactory.getLogger(WriterProxy.class);

    /**
     * Reference to the WriterProxy associated with this specific event.
     */
    public WriterProxy writerProxy;

    /**
     * Boolean value to not execute the event the first time
     */
    private boolean recentlyCreated = true;

    /**
     * {@link WriterProxyLiveliness} constructor
     * 
     * @param proxy {@link WriterProxy} to remove writers from
     * @param interval Time interval to execute the event
     */
    public WriterProxyLiveliness(WriterProxy proxy, double interval) {
        super(interval);
        writerProxy = proxy;
    }

    /**
     * Main event behaviour
     */
    @Override
    public void event(EventCode code, String msg) {
        this.m_mutex.lock();
        try {
            if (code == EventCode.EVENT_SUCCESS) {
                if (recentlyCreated) {
                    recentlyCreated = false;
                } else {
                    logger.debug("Deleting writer {}", this.writerProxy.att.guid);
                    if (this.writerProxy.statefulReader.matchedWriterRemove(this.writerProxy.att)) {
                        if (this.writerProxy.statefulReader.getListener() != null) {
                            MatchingInfo info = new MatchingInfo(MatchingStatus.REMOVED_MATCHING, this.writerProxy.att.guid);
                            this.writerProxy.statefulReader.getListener().onReaderMatched((RTPSReader) this.writerProxy.statefulReader, info);
                        }
                    }
                    this.writerProxy.writerProxyLiveliness = null;
                    this.writerProxy.destroy();
                }
            } else if (code == EventCode.EVENT_ABORT) {
                logger.debug("WriterProxyLiveliness aborted");
            } else {
                logger.debug("Message: {}", msg);
            }
        } finally {
            this.m_mutex.unlock();
        }
    }

    /**
     * Restarts the timer and sets the recentlyCreated attribute to true again
     */
    @Override
    public void restartTimer() {
        this.recentlyCreated = true;
        super.restartTimer();
    }

}
