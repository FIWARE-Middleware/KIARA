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

import java.util.ArrayList;
import java.util.List;

import org.fiware.kiara.ps.rtps.attributes.ReaderAttributes;
import org.fiware.kiara.ps.rtps.attributes.RemoteWriterAttributes;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.history.ReaderHistoryCache;
import org.fiware.kiara.ps.rtps.messages.elements.EntityId;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.GUIDPrefix;
import org.fiware.kiara.ps.rtps.messages.elements.EntityId.EntityIdEnum;
import org.fiware.kiara.ps.rtps.messages.elements.SequenceNumber;
import org.fiware.kiara.ps.rtps.messages.elements.SequenceNumberSet;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.resources.ListenResource;
import org.fiware.kiara.util.ReturnParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents an Stateless RTPReader
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class StatelessReader extends RTPSReader {

    /**
     * Matched writers
     */
    private final List<RemoteWriterAttributes> m_matchedWriters;

    /**
     * Logging object
     */
    private static final Logger logger = LoggerFactory.getLogger(StatelessReader.class);

    /**
     * {@link StatelessReader} constructor
     * 
     * @param participant {@link RTPSParticipant} who created the {@link StatelessReader}
     * @param guid {@link GUID} of the reader
     * @param att {@link ReaderAttributes} for configuration
     * @param history {@link ReaderHistoryCache} to store {@link CacheChange} objects
     * @param listener listener Listener to invoke
     */
    public StatelessReader(RTPSParticipant participant, GUID guid,
            ReaderAttributes att, ReaderHistoryCache history,
            ReaderListener listener) {
        super(participant, guid, att, history, listener);
        this.m_matchedWriters = new ArrayList<>();
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
                    logger.debug("Writer " + wdata.guid + " removed from " + this.m_guid.getEntityId());
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
    public boolean acceptMsgFrom(GUID writerID, ReturnParam<WriterProxy> proxy, boolean checktrusted) {
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
    public boolean nextUntakenCache(ReturnParam<CacheChange> change, ReturnParam<WriterProxy> proxy) {
        this.m_mutex.lock();
        try {
            return this.m_history.getMinChange(change);
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

    /**
     * Data processing for the Stateles RTPSReader
     */
    @Override
    public boolean processDataMsg(CacheChange change, ListenResource listenResource, boolean hasTimestamp, Timestamp timestamp, GUIDPrefix sourceGuidPrefix) {

        this.m_mutex.lock();
        try {

            if (this.acceptMsgFrom(change.getWriterGUID(), null, true)) {


                logger.debug("Trying to add change {} to Reader {}", change.getSequenceNumber().toLong(), getGuid().getEntityId());

                CacheChange changeToAdd = reserveCache();

                if (changeToAdd != null) {
                    if (!changeToAdd.copy(change)) {
                        logger.warn("Problem copying CacheChange");
                        releaseCache(changeToAdd);
                        return false;
                    }
                } else {
                    logger.error("Problem reserving CacheChange in reader");
                    return false;
                }

                if (hasTimestamp) {
                    changeToAdd.setSourceTimestamp(timestamp);
                }

                this.m_mutex.unlock();
                try {
                    if (!changeReceived(changeToAdd, null)) {
                        logger.debug("MessageReceiver not adding CacheChange");
                        releaseCache(changeToAdd);
                        if (getGuid().getEntityId().equals(new EntityId(EntityIdEnum.ENTITYID_SPDP_BUILTIN_RTPSPARTICIPANT_READER))) {
                            listenResource.getRTPSParticipant().assertRemoteRTPSParticipantLiveliness(sourceGuidPrefix);
                        }
                    } 
                } finally {
                    this.m_mutex.lock();
                }



                //            logger.debug("Trying to add change {} to Reader {}", change.getSequenceNumber().toLong(), this.getGuid().getEntityId());
                //            CacheChange changeToAdd = this.reserveCache();
                //
                //            if (changeToAdd != null) {
                //                if (!changeToAdd.copy(change)) {
                //                    logger.warn("Problem copying CacheChange");
                //                    this.releaseCache(changeToAdd);
                //                    return false;
                //                }
                //            } else {
                //                logger.error("Problem reserving CacheChange in reader");
                //                return false;
                //            }
                //
                //            if (hasTimestamp) {
                //                changeToAdd.setSourceTimestamp(timestamp);
                //            }
                //
                //            if (!this.changeReceived(changeToAdd, null)) {
                //                logger.debug("MessageReceiver not adding CacheChange");
                //                this.releaseCache(changeToAdd);
                //                if (this.getGuid().getEntityId().equals(new EntityId(EntityIdEnum.ENTITYID_SPDP_BUILTIN_RTPSPARTICIPANT_READER))) {
                //                    listenResource.getRTPSParticipant().assertRemoteRTPSParticipantLiveliness(changeToAdd.getWriterGUID().getGUIDPrefix());
                //                }
                //            }

            }
        } finally {
            this.m_mutex.unlock();
        }
        return true;

    }

    @Override
    public boolean processHeartbeatMsg(GUID writerGUID, int hbCount, SequenceNumber firstSN, SequenceNumber lastSN, boolean finalFlag, boolean livelinessFlag) {
        return true;
    }

    @Override
    public boolean processGapMsg(GUID writerGUID, SequenceNumber gapStart, SequenceNumberSet gapList) {
        return true;
    }

}
