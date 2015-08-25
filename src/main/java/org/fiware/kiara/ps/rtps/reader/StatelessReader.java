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
package org.fiware.kiara.ps.rtps.reader;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.fiware.kiara.ps.publisher.WriterProxy;
import org.fiware.kiara.ps.rtps.attributes.ReaderAttributes;
import org.fiware.kiara.ps.rtps.attributes.RemoteWriterAttributes;
import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.history.ReaderHistoryCache;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.util.ReturnParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public class StatelessReader extends RTPSReader {

    private final List<RemoteWriterAttributes> m_matchedWriters;

    private static final Logger logger = LoggerFactory.getLogger(StatelessReader.class);

    public StatelessReader(RTPSParticipant participant, GUID guid,
            ReaderAttributes att, ReaderHistoryCache history,
            ReaderListener listener) {
        super(participant, guid, att, history, listener);
        this.m_matchedWriters = new ArrayList<>();

        /*RemoteWriterAttributes test = new RemoteWriterAttributes();
        Locator l = new Locator();
        try {
            byte [] addr = new byte[16];
            byte [] obtainedAddr = InetAddress.getByName("239.255.0.1").getAddress();
            //byte [] obtainedAddr = InetAddress.getByName("192.168.1.133").getAddress();
            addr[12] = obtainedAddr[0];
            addr[13] = obtainedAddr[1];
            addr[14] = obtainedAddr[2];
            addr[15] = obtainedAddr[3];
            l.setAddress(addr);
            l.setPort(7400);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        test.endpoint.unicastLocatorList.pushBack(l);
        matchedWriterAdd(test);*/
    }

    @Override
    public boolean matchedWriterAdd(RemoteWriterAttributes wdata) {
        this.m_mutex.lock();
        try {
            for (RemoteWriterAttributes it : this.m_matchedWriters) {
                if (it.guid.equals(wdata.guid)) {
                    logger.warn("Attempting to add existing writer");
                    return false;
                }
            }
            logger.debug("Writer {} added to {}", wdata.guid, this.m_guid.getEntityId());
            this.m_matchedWriters.add(wdata);
            this.m_acceptMessagesFromUnknownWriters = false;
            return true;
        } finally {
            this.m_mutex.unlock();
        }
    }

    @Override
    public boolean matchedWriterRemove(RemoteWriterAttributes wdata) {
        this.m_mutex.lock();
        try {
            for (RemoteWriterAttributes it : this.m_matchedWriters) {
                if (it.guid.equals(wdata.guid)) {
                    logger.info("Writer " + wdata.guid + " removed from " + this.m_guid.getEntityId());
                    this.m_matchedWriters.remove(wdata);
                    return true;
                }
            }
            return false;
        } finally {
            this.m_mutex.unlock();
        }
    }

    @Override
    public boolean matchedWriterIsMatched(RemoteWriterAttributes wdata) {
        this.m_mutex.lock();
        try {
            for (RemoteWriterAttributes it : this.m_matchedWriters) {
                if (it.guid.equals(wdata.guid)) {
                    return true;
                }
            }
            return false;
        } finally {
            this.m_mutex.unlock();
        }
    }

    @Override
    public boolean changeReceived(CacheChange change, WriterProxy proxy) {
        this.m_mutex.lock();
        try {
            if (this.m_history.receivedChange(change)) {
                if (this.m_listener != null) {
                    this.m_mutex.unlock();
                    try {
                        this.m_listener.onNewCacheChangeAdded(this, change);
                    } finally {
                        this.m_mutex.lock();
                    }
                } 
                this.m_history.postChange();
                return true;
            }
            return false;
        } finally {
            this.m_mutex.unlock();
        }
    }

    @Override
    public CacheChange nextUnreadCache(WriterProxy proxy) {
        this.m_mutex.lock();
        try {
            for (CacheChange it : this.m_history.getChanges()) {
                if (!it.isRead()) {
                    return it;
                }
            }
            logger.info("No unread elements left");
            return null;
        } finally {
            this.m_mutex.unlock();
        }
    }
    
    @Override
    public boolean changeRemovedByHistory(CacheChange change, WriterProxy proxy) {
        return true;
    }

    @Override
    public boolean acceptMsgFrom(GUID writerID, ReturnParam<WriterProxy> proxy) {
        this.m_mutex.lock();
        try {
            if (this.m_acceptMessagesFromUnknownWriters) {
                return true;
            } else {
                if (writerID.getEntityId().equals(this.m_trustedWriterEntityId)) {
                    return true;
                }
                for (RemoteWriterAttributes it : this.m_matchedWriters) {
                    if (it.guid.equals(writerID)) {
                        return true;
                    }
                }
            }
            return false;
        } finally {
            this.m_mutex.unlock();
        }
    }

    @Override
    public CacheChange nextUntakenCache(WriterProxy proxy) {
        this.m_mutex.lock();
        try {
            return this.m_history.getMinChange();
        } finally {
            this.m_mutex.unlock();
        }
    }

    @Override
    public boolean nextUntakenCache(CacheChange change, ReturnParam<WriterProxy> proxy) {
        this.m_mutex.lock();
        try {
            /*CacheChange retChange = new CacheChange();
            retChange.copy(this.m_history.getMinChange(change))*/
            return this.m_history.getMinChange(change);
            //return retChange;
        } finally {
            this.m_mutex.unlock();
        }
    }

    @Override
    public boolean nextUnreadCache(ReturnParam<CacheChange> change, ReturnParam<WriterProxy> proxy) {
        this.m_mutex.lock();
        try {
            for (CacheChange it : this.m_history.getChanges()) {
                if (!it.isRead()) {
                    change.value = it;
                    return true;
                }
            }
            logger.info("No unread elements left");
            return false;
        } finally {
            this.m_mutex.unlock();
        }
    }

}
