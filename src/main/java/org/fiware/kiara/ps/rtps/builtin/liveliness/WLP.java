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
package org.fiware.kiara.ps.rtps.builtin.liveliness;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.fiware.kiara.ps.qos.policies.LivelinessQosPolicyKind.AUTOMATIC_LIVELINESS_QOS;
import static org.fiware.kiara.ps.qos.policies.LivelinessQosPolicyKind.MANUAL_BY_PARTICIPANT_LIVELINESS_QOS;
import static org.fiware.kiara.ps.rtps.builtin.data.ParticipantProxyData.BUILTIN_ENDPOINT_PARTICIPANT_MESSAGE_DATA_READER;
import static org.fiware.kiara.ps.rtps.builtin.data.ParticipantProxyData.BUILTIN_ENDPOINT_PARTICIPANT_MESSAGE_DATA_WRITER;
import static org.fiware.kiara.ps.rtps.builtin.data.ParticipantProxyData.BUILTIN_PARTICIPANT_DATA_MAX_SIZE;
import static org.fiware.kiara.ps.rtps.builtin.data.ParticipantProxyData.DISC_BUILTIN_ENDPOINT_PARTICIPANT_DETECTOR;
import static org.fiware.kiara.ps.rtps.common.DurabilityKind.TRANSIENT_LOCAL;
import static org.fiware.kiara.ps.rtps.common.ReliabilityKind.RELIABLE;
import static org.fiware.kiara.ps.rtps.common.TopicKind.WITH_KEY;
import static org.fiware.kiara.ps.rtps.messages.elements.EntityId.EntityIdEnum.ENTITYID_P2P_BUILTIN_RTPSPARTICIPANT_MESSAGE_READER;
import static org.fiware.kiara.ps.rtps.messages.elements.EntityId.EntityIdEnum.ENTITYID_P2P_BUILTIN_RTPSPARTICIPANT_MESSAGE_WRITER;

import org.fiware.kiara.ps.qos.WriterQos;
import org.fiware.kiara.ps.rtps.attributes.HistoryCacheAttributes;
import org.fiware.kiara.ps.rtps.attributes.ReaderAttributes;
import org.fiware.kiara.ps.rtps.attributes.RemoteReaderAttributes;
import org.fiware.kiara.ps.rtps.attributes.RemoteWriterAttributes;
import org.fiware.kiara.ps.rtps.attributes.WriterAttributes;
import org.fiware.kiara.ps.rtps.builtin.BuiltinProtocols;
import org.fiware.kiara.ps.rtps.builtin.data.ParticipantProxyData;
import org.fiware.kiara.ps.rtps.builtin.data.WriterProxyData;
import org.fiware.kiara.ps.rtps.builtin.liveliness.timedevent.WLivelinessPeriodicAssertion;
import org.fiware.kiara.ps.rtps.common.TopicKind;
import org.fiware.kiara.ps.rtps.history.ReaderHistoryCache;
import org.fiware.kiara.ps.rtps.history.WriterHistoryCache;
import org.fiware.kiara.ps.rtps.messages.elements.EntityId;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.reader.RTPSReader;
import org.fiware.kiara.ps.rtps.reader.ReaderListener;
import org.fiware.kiara.ps.rtps.reader.StatefulReader;
import org.fiware.kiara.ps.rtps.writer.RTPSWriter;
import org.fiware.kiara.ps.rtps.writer.StatefulWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class WLP that implements the Writer Liveliness Protocol described in the
 * RTPS specification.
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public class WLP {

    /**
     * Reference to the local RTPSParticipant.
     */
    private RTPSParticipant m_participant;

    /**
     * Reference to the BuiltinProtocols class.
     */
    private final BuiltinProtocols m_builtinProtocols;

    /**
     * Reference to the builtinRTPSParticipantMEssageWriter.
     */
    private StatefulWriter m_builtinWriter;

    /**
     * Reference to the builtinRTPSParticipantMEssageReader.
     */
    private StatefulReader m_builtinReader;

    /**
     * {@link WriterHistoryCache} of the builtin {@link RTPSWriter}
     */
    WriterHistoryCache m_builtinWriterHistory;
    
    /**
     * {@link ReaderHistoryCache} of the builtin {@link RTPSRReader}
     */
    ReaderHistoryCache m_builtinReaderHistory;

    /**
     * Listener object.
     */
    private WLPListener m_listener;
    /**
     * Reference to the periodic assertion timer object for the automatic
     * liveliness writers.
     */
    private WLivelinessPeriodicAssertion m_livelinessAutomatic;
    /**
     * Reference to the periodic assertion timer object for the manual by
     * RTPSParticipant liveliness writers.
     */
    private WLivelinessPeriodicAssertion m_livelinessManRTPSParticipant;
    /**
     * List of the writers using automatic liveliness.
     */
    private List<RTPSWriter> m_livAutomaticWriters;
    /**
     * List of the writers using manual by RTPSParticipant liveliness.
     */
    private List<RTPSWriter> m_livManRTPSParticipantWriters;
    /**
     * Mutex.
     */
    private final Lock m_mutex;

    /**
     * Minimum time of the automatic writers liveliness period.
     */
    private double m_minAutomatic_MilliSec;
    /**
     * Minimum time of the manual by participant writers liveliness period.
     */
    private double m_minManRTPSParticipant_MilliSec;
    
    /**
     * Logging object
     */
    private static final Logger logger = LoggerFactory.getLogger(WLP.class);

    /**
     * {@link WLP} constructor
     *
     * @param builtinProtocols Reference to the BuiltinProtocols object.
     */
    public WLP(BuiltinProtocols builtinProtocols) {
        m_minAutomatic_MilliSec = Double.MAX_VALUE;
        m_minManRTPSParticipant_MilliSec = Double.MAX_VALUE;
        m_participant = null;
        m_builtinProtocols = builtinProtocols;
        m_builtinWriter = null;
        m_builtinReader = null;
        m_builtinWriterHistory = null;
        m_builtinReaderHistory = null;
        m_listener = null;
        m_livelinessAutomatic = null;
        m_livelinessManRTPSParticipant = null;
        m_mutex = new ReentrantLock(true);
        
        this.m_livAutomaticWriters = new ArrayList<RTPSWriter>();
        this.m_livManRTPSParticipantWriters = new ArrayList<RTPSWriter>();
    }

    /**
     * Initialize the WLP protocol.
     *
     * @param p BuiltinProtocols object.
     * @return true if the initialization was successful.
     */
    public boolean initWL(RTPSParticipant p) {
        logger.info("RTPS LIVELINESS: Beginning Liveliness Protocol");
        m_participant = p;
        return createEndpoints();
    }

    /**
     * Create the endpoints used in the WLP.
     *
     * @return true if correct.
     */
    public boolean createEndpoints() {
        //CREATE WRITER
        HistoryCacheAttributes wHAtt = new HistoryCacheAttributes();
        wHAtt.initialReservedCaches = 20;
        wHAtt.maximumReservedCaches = 1000;
        wHAtt.payloadMaxSize = BUILTIN_PARTICIPANT_DATA_MAX_SIZE;
        m_builtinWriterHistory = new WriterHistoryCache(wHAtt);
        WriterAttributes watt = new WriterAttributes();
        watt.endpointAtt.unicastLocatorList.copy(m_builtinProtocols.getMetatrafficUnicastLocatorList());
        watt.endpointAtt.multicastLocatorList.copy(m_builtinProtocols.getMetatrafficMulticastLocatorList());
        //	Wparam.topic.topicName = "DCPSRTPSParticipantMessage";
        //	Wparam.topic.topicDataType = "RTPSParticipantMessageData";
        watt.endpointAtt.topicKind = TopicKind.NO_KEY;
        watt.endpointAtt.durabilityKind = TRANSIENT_LOCAL;
        watt.endpointAtt.reliabilityKind = RELIABLE;
        RTPSWriter wout = m_participant.createWriter(watt, m_builtinWriterHistory, null, new EntityId(EntityId.EntityIdEnum.ENTITYID_P2P_BUILTIN_RTPSPARTICIPANT_MESSAGE_WRITER), true);
        if (wout != null) {
            m_builtinWriter = (StatefulWriter) (wout);
            logger.debug("RTPS LIVELINESS: Builtin Liveliness Writer created");
        } else {
            logger.error("RTPS LIVELINESS: Liveliness Writer Creation failed ");
            m_builtinWriterHistory = null;
            return false;
        }
        
        HistoryCacheAttributes rHAtt = new HistoryCacheAttributes();
        rHAtt.initialReservedCaches = 100;
        rHAtt.maximumReservedCaches = 2000;
        rHAtt.payloadMaxSize = BUILTIN_PARTICIPANT_DATA_MAX_SIZE;
        m_builtinReaderHistory = new ReaderHistoryCache(rHAtt);
        ReaderAttributes ratt = new ReaderAttributes();
        ratt.endpointAtt.topicKind = TopicKind.NO_KEY;
        ratt.endpointAtt.durabilityKind = TRANSIENT_LOCAL;
        ratt.endpointAtt.reliabilityKind = RELIABLE;
        ratt.expectsInlineQos = true;
        ratt.endpointAtt.unicastLocatorList.copy(m_builtinProtocols.getMetatrafficUnicastLocatorList());
        ratt.endpointAtt.multicastLocatorList.copy(m_builtinProtocols.getMetatrafficMulticastLocatorList());
        //Rparam.topic.topicName = "DCPSRTPSParticipantMessage";
        //Rparam.topic.topicDataType = "RTPSParticipantMessageData";
        //ratt.endpointAtt.topicKind = WITH_KEY;
        //LISTENER CREATION
        m_listener = new WLPListener(this);
        RTPSReader rout = m_participant.createReader(ratt, m_builtinReaderHistory, (ReaderListener) m_listener, new EntityId(ENTITYID_P2P_BUILTIN_RTPSPARTICIPANT_MESSAGE_READER), true);
        if (rout != null) {
            m_builtinReader = (StatefulReader) (rout);
            logger.debug("RTPS LIVELINESS: Builtin Liveliness Reader created");
        } else {
            logger.error("RTPS LIVELINESS: Liveliness Reader Creation failed.");
            m_builtinReaderHistory = null;
            m_listener.destroy();
            m_listener = null;
            return false;
        }

        return true;
    }

    /**
     * Assign the remote endpoints for a newly discovered RTPSParticipant.
     *
     * @param pdata Reference to the RTPSParticipantProxyData object.
     * @return True if correct.
     */
    public boolean assignRemoteEndpoints(ParticipantProxyData pdata) {
        m_mutex.lock();
        try {
            final Lock mutex2 = pdata.getMutex();
            mutex2.lock();
            try {

                logger.debug("RTPS LIVELINESS: For remote RTPSParticipant {}", pdata.getGUID());
                int endp = pdata.getAvailableBuiltinEndpoints();
                int partdet = endp;
                int auxendp = endp;
                partdet &= DISC_BUILTIN_ENDPOINT_PARTICIPANT_DETECTOR;
                auxendp &= BUILTIN_ENDPOINT_PARTICIPANT_MESSAGE_DATA_WRITER;
                //auxendp = 1;
                //FIXME: WRITERLIVELINESS PUT THIS BACK TO THE ORIGINAL LINE
                if ((auxendp != 0 || partdet != 0) && m_builtinReader != null) {
                    logger.debug("RTPS LIVELINESS: Adding remote writer to my local Builtin Reader");
                    RemoteWriterAttributes watt = new RemoteWriterAttributes();
                    watt.guid.setGUIDPrefix(pdata.getGUID().getGUIDPrefix());
                    watt.guid.setEntityId(new EntityId(ENTITYID_P2P_BUILTIN_RTPSPARTICIPANT_MESSAGE_WRITER));
                    watt.endpoint.unicastLocatorList.copy(pdata.getMetatrafficUnicastLocatorList());
                    watt.endpoint.multicastLocatorList.copy(pdata.getMetatrafficMulticastLocatorList());
                    watt.endpoint.topicKind = WITH_KEY;
                    watt.endpoint.durabilityKind = TRANSIENT_LOCAL;
                    watt.endpoint.reliabilityKind = RELIABLE;
                    pdata.getBuiltinWriters().add(watt);
                    m_builtinReader.matchedWriterAdd(watt);
                }
                auxendp = endp;
                auxendp &= BUILTIN_ENDPOINT_PARTICIPANT_MESSAGE_DATA_READER;
                //auxendp = 1;
                //FIXME: WRITERLIVELINESS PUT THIS BACK TO THE ORIGINAL LINE
                if ((auxendp != 0 || partdet != 0) && m_builtinWriter != null) {
                    logger.debug("RTPS LIVELINESS: Adding remote reader to my local Builtin Writer");
                    RemoteReaderAttributes ratt = new RemoteReaderAttributes();
                    ratt.expectsInlineQos = false;
                    ratt.guid.setGUIDPrefix(pdata.getGUID().getGUIDPrefix());
                    ratt.guid.setEntityId(new EntityId(ENTITYID_P2P_BUILTIN_RTPSPARTICIPANT_MESSAGE_READER));
                    ratt.endpoint.unicastLocatorList.copy(pdata.getMetatrafficUnicastLocatorList());
                    ratt.endpoint.multicastLocatorList.copy(pdata.getMetatrafficMulticastLocatorList());
                    ratt.endpoint.topicKind = WITH_KEY;
                    ratt.endpoint.durabilityKind = TRANSIENT_LOCAL;
                    ratt.endpoint.reliabilityKind = RELIABLE;
                    pdata.getBuiltinReaders().add(ratt);
                    m_builtinWriter.matchedReaderAdd(ratt);
                }

                return true;
            } finally {
                mutex2.unlock();
            }
        } finally {
            m_mutex.unlock();
        }
    }

    /**
     * Remove remote endpoints from the liveliness protocol.
     *
     * @param pdata Reference to the ParticipantProxyData to remove
     */
    public void removeRemoteEndpoints(ParticipantProxyData pdata) {
        m_mutex.lock();
        try {
            final Lock mutex2 = pdata.getMutex();
            mutex2.lock();
            try {
                logger.debug("RTPS LIVELINESS: for RTPSParticipant: {}", pdata.getGUID());

                final EntityId c_EntityId_ReaderLiveliness = new EntityId(ENTITYID_P2P_BUILTIN_RTPSPARTICIPANT_MESSAGE_READER);
                final EntityId c_EntityId_WriterLiveliness = new EntityId(ENTITYID_P2P_BUILTIN_RTPSPARTICIPANT_MESSAGE_WRITER);

                for (RemoteReaderAttributes it : pdata.getBuiltinReaders()) {
                    if (it.guid.getEntityId().equals(c_EntityId_ReaderLiveliness) && m_builtinWriter != null) {
                        m_builtinWriter.matchedReaderRemove(it);
                        break;
                    }
                }
                for (RemoteWriterAttributes it : pdata.getBuiltinWriters()) {
                    if (it.guid.getEntityId().equals(c_EntityId_WriterLiveliness) && m_builtinReader != null) {
                        m_builtinReader.matchedWriterRemove(it);
                        break;
                    }
                }

            } finally {
                mutex2.unlock();
            }
        } finally {
            m_mutex.unlock();
        }
    }

    /**
     * Add a local writer to the liveliness protocol.
     *
     * @param writer Reference to the RTPSWriter.
     * @param wqos
     * @return True if correct.
     */
    public boolean addLocalWriter(RTPSWriter writer, WriterQos wqos) {
        m_mutex.lock();
        try {
            logger.debug("RTPS LIVELINESS: {} to Liveliness Protocol", writer.getGuid().getEntityId());
            if (!wqos.liveliness.announcementPeriod.equals(new Timestamp().timeInfinite())) {
                double wAnnouncementPeriodMilliSec = wqos.liveliness.announcementPeriod.toMilliSecondsDouble();
                if (wqos.liveliness.kind == AUTOMATIC_LIVELINESS_QOS) {
                    if (m_livelinessAutomatic == null) {
                        
                        m_livelinessAutomatic = new WLivelinessPeriodicAssertion(this, AUTOMATIC_LIVELINESS_QOS, wAnnouncementPeriodMilliSec);
                        m_minAutomatic_MilliSec = wAnnouncementPeriodMilliSec;
                    
                    } else if (m_minAutomatic_MilliSec > wAnnouncementPeriodMilliSec) {
                        m_minAutomatic_MilliSec = wAnnouncementPeriodMilliSec;
                        m_livelinessAutomatic.updateIntervalMillisec(wAnnouncementPeriodMilliSec);
                        //CHECK IF THE TIMER IS GOING TO BE CALLED AFTER THIS NEW SET LEASE DURATION
                        if (m_livelinessAutomatic.isWaiting() && m_livelinessAutomatic.getRemainingTimeMilliSec() > m_minAutomatic_MilliSec) {
                            m_livelinessAutomatic.stopTimer();
                        }
                        m_livelinessAutomatic.restartTimer();
                    }
                    m_livAutomaticWriters.add(writer);
                } else if (wqos.liveliness.kind == MANUAL_BY_PARTICIPANT_LIVELINESS_QOS) {
                    if (m_livelinessManRTPSParticipant == null) {
                        m_livelinessManRTPSParticipant = new WLivelinessPeriodicAssertion(this, MANUAL_BY_PARTICIPANT_LIVELINESS_QOS, wAnnouncementPeriodMilliSec);
                        //m_livelinessManRTPSParticipant.updateIntervalMillisec(wAnnouncementPeriodMilliSec);
                        //m_livelinessManRTPSParticipant.restartTimer();
                        m_minManRTPSParticipant_MilliSec = wAnnouncementPeriodMilliSec;
                    } else if (m_minManRTPSParticipant_MilliSec > wAnnouncementPeriodMilliSec) {
                        m_minManRTPSParticipant_MilliSec = wAnnouncementPeriodMilliSec;
                        m_livelinessManRTPSParticipant.updateIntervalMillisec(m_minManRTPSParticipant_MilliSec);
                        //CHECK IF THE TIMER IS GOING TO BE CALLED AFTER THIS NEW SET LEASE DURATION
                        if (m_livelinessManRTPSParticipant.isWaiting() && m_livelinessManRTPSParticipant.getRemainingTimeMilliSec() > m_minManRTPSParticipant_MilliSec) {
                            m_livelinessManRTPSParticipant.stopTimer();
                        }
                        m_livelinessManRTPSParticipant.restartTimer();
                    }
                    m_livManRTPSParticipantWriters.add(writer);
                }
            }
            return true;
        } finally {
            m_mutex.unlock();
        }
    }

    /**
     * Remove a local writer from the liveliness protocol.
     *
     * @param writer RTPSWriter.
     * @return True if correct.
     */
    public boolean removeLocalWriter(RTPSWriter writer) {
        m_mutex.lock();
        try {
            logger.debug("RTPS LIVELINESS: {} from Liveliness Protocol", writer.getGuid().getEntityId());
            int wToEraseIndex = -1;
            WriterProxyData wdata = m_builtinProtocols.getPDP().lookupWriterProxyData(writer.getGuid());
            if (wdata != null) {
                boolean found = false;
                if (wdata.getQos().liveliness.kind == AUTOMATIC_LIVELINESS_QOS) {
                    m_minAutomatic_MilliSec = Double.MAX_VALUE;

                    for (int i = 0; i < m_livAutomaticWriters.size(); ++i) {
                        final RTPSWriter it = m_livAutomaticWriters.get(i);
                        WriterProxyData wdata2 = m_builtinProtocols.getPDP().lookupWriterProxyData(it.getGuid());
                        if (wdata2 != null) {
                            final double mintimeWIT = wdata2.getQos().liveliness.announcementPeriod.toMilliSecondsDouble();
                            if (writer.getGuid().getEntityId().equals(it.getGuid().getEntityId())) {
                                found = true;
                                wToEraseIndex = i;
                                continue;
                            }
                            if (m_minAutomatic_MilliSec > mintimeWIT) {
                                m_minAutomatic_MilliSec = mintimeWIT;
                            }
                        }
                    }
                    if (found) {
                        m_livAutomaticWriters.remove(wToEraseIndex);
                        if (m_livelinessAutomatic != null) {
                            if (m_livAutomaticWriters.size() > 0) {
                                m_livelinessAutomatic.updateIntervalMillisec(m_minAutomatic_MilliSec);
                            } else {
                                m_livelinessAutomatic.stopTimer();
                                m_livelinessAutomatic = null;

                            }
                        }
                    }
                } else if (wdata.getQos().liveliness.kind == MANUAL_BY_PARTICIPANT_LIVELINESS_QOS) {
                    m_minManRTPSParticipant_MilliSec = Double.MAX_VALUE;
                    for (int i = 0; i < m_livManRTPSParticipantWriters.size(); ++i) {
                        final RTPSWriter it = m_livAutomaticWriters.get(i);
                        WriterProxyData wdata2 = m_builtinProtocols.getPDP().lookupWriterProxyData(it.getGuid());
                        if (wdata2 != null) {
                            double mintimeWIT = wdata2.getQos().liveliness.announcementPeriod.toMilliSecondsDouble();
                            if (writer.getGuid().getEntityId().equals(it.getGuid().getEntityId())) {
                                found = true;
                                wToEraseIndex = i;
                                continue;
                            }
                            if (m_minManRTPSParticipant_MilliSec > mintimeWIT) {
                                m_minManRTPSParticipant_MilliSec = mintimeWIT;
                            }
                        }
                    }
                    if (found) {
                        m_livManRTPSParticipantWriters.remove(wToEraseIndex);
                        if (m_livelinessManRTPSParticipant != null) {
                            if (m_livManRTPSParticipantWriters.size() > 0) {
                                m_livelinessManRTPSParticipant.updateIntervalMillisec(m_minManRTPSParticipant_MilliSec);
                            } else {
                                m_livelinessManRTPSParticipant.stopTimer();
                                m_livelinessManRTPSParticipant = null;
                            }
                        }
                    }
                } else // OTHER VALUE OF LIVELINESS (BY TOPIC)
                {
                    return true;
                }
                if (found) {
                    return true;
                } else {
                    return false;
                }
            }
            logger.warn("RTPS LIVELINESS: Writer {} not found.", writer.getGuid().getEntityId());
            return false;
        } finally {
            m_mutex.unlock();
        }
    }

    /**
     * Get the builtin protocols
     *
     * @return Builtin protocols
     */
    public BuiltinProtocols getBuiltinProtocols() {
        return m_builtinProtocols;
    }

    /**
     * Update local writer.
     *
     * @param writer Writer to update
     * @param wqos New writer QoS
     * @return True on success
     */
    public boolean updateLocalWriter(RTPSWriter writer, WriterQos wqos) {
        m_mutex.lock();
        try {
            logger.debug("RTPS LIVELINESS: {}", writer.getGuid().getEntityId());
            double wAnnouncementPeriodMilliSec = wqos.liveliness.announcementPeriod.toMilliSecondsDouble();
            if (wqos.liveliness.kind == AUTOMATIC_LIVELINESS_QOS) {
                if (m_livelinessAutomatic == null) {
                    /*
                     m_livelinessAutomatic = new WLivelinessPeriodicAssertion(this, AUTOMATIC_LIVELINESS_QOS);
                     m_livelinessAutomatic.updateIntervalMillisec(wAnnouncementPeriodMilliSec);
                     m_livelinessAutomatic.restartTimer();
                     */
                    m_livelinessAutomatic = new WLivelinessPeriodicAssertion(this, AUTOMATIC_LIVELINESS_QOS, wAnnouncementPeriodMilliSec);
                    m_minAutomatic_MilliSec = wAnnouncementPeriodMilliSec;
                } else if (m_minAutomatic_MilliSec > wAnnouncementPeriodMilliSec) {
                    m_minAutomatic_MilliSec = wAnnouncementPeriodMilliSec;
                    m_livelinessAutomatic.updateIntervalMillisec(wAnnouncementPeriodMilliSec);
                    //CHECK IF THE TIMER IS GOING TO BE CALLED AFTER THIS NEW SET LEASE DURATION
                    if (m_livelinessAutomatic.isWaiting() && m_livelinessAutomatic.getRemainingTimeMilliSec() > m_minAutomatic_MilliSec) {
                        m_livelinessAutomatic.stopTimer();
                    }
                    m_livelinessAutomatic.restartTimer();
                }
            } else if (wqos.liveliness.kind == MANUAL_BY_PARTICIPANT_LIVELINESS_QOS) {
                if (m_livelinessManRTPSParticipant == null) {
                    /*
                     m_livelinessManRTPSParticipant = new WLivelinessPeriodicAssertion(this, MANUAL_BY_PARTICIPANT_LIVELINESS_QOS);
                     m_livelinessManRTPSParticipant.updateIntervalMillisec(wAnnouncementPeriodMilliSec);
                     m_livelinessManRTPSParticipant.restartTimer();
                     */
                    m_livelinessManRTPSParticipant = new WLivelinessPeriodicAssertion(this, MANUAL_BY_PARTICIPANT_LIVELINESS_QOS, wAnnouncementPeriodMilliSec);
                    m_minManRTPSParticipant_MilliSec = wAnnouncementPeriodMilliSec;
                } else if (m_minManRTPSParticipant_MilliSec > wAnnouncementPeriodMilliSec) {
                    m_minManRTPSParticipant_MilliSec = wAnnouncementPeriodMilliSec;
                    m_livelinessManRTPSParticipant.updateIntervalMillisec(m_minManRTPSParticipant_MilliSec);
                    //CHECK IF THE TIMER IS GOING TO BE CALLED AFTER THIS NEW SET LEASE DURATION
                    if (m_livelinessManRTPSParticipant.isWaiting() && m_livelinessManRTPSParticipant.getRemainingTimeMilliSec() > m_minManRTPSParticipant_MilliSec) {
                        m_livelinessManRTPSParticipant.stopTimer();
                    }
                    m_livelinessManRTPSParticipant.restartTimer();
                }
            }
            return true;
        } finally {
            m_mutex.unlock();
        }
    }

    /**
     * Get the RTPS participant
     *
     * @return RTPS participant
     */
    public RTPSParticipant getRTPSParticipant() {
        return m_participant;
    }

    /**
     * Get the mutex
     *
     * @return mutex
     */
    public Lock getMutex() {
        return m_mutex;
    }

    /**
     * Get the builtin writer
     *
     * @return Builtin writer
     */
    public StatefulWriter getBuiltinWriter() {
        return m_builtinWriter;
    }

    /**
     * Get the list of writers using automatic liveness
     *
     * @return List of writers using automatic liveness
     */
    public List<RTPSWriter> getLivAutomaticWriters() {
        return m_livAutomaticWriters;
    }

    /**
     * Get the list of writers using manual by RTPSParticipant liveliness.
     *
     * @return List of writers using manual by RTPSParticipant liveliness.
     */
    public List<RTPSWriter> getLivManRTPSParticipantWriters() {
        return m_livManRTPSParticipantWriters;
    }

    /**
     * Get the builtin writer history
     *
     * @return Builtin writer history
     */
    public WriterHistoryCache getBuiltinWriterHistory() {
        return m_builtinWriterHistory;
    }

    /**
     * Deletes all the information associated to the {@link WLP}
     */
    public void destroy() {
        if (this.m_builtinReader != null) {
            this.m_builtinReader.destroy();
        }
        
        if (this.m_builtinWriter != null) {
            this.m_builtinWriter.destroy();
        }
        
        if (this.m_listener != null) {
            this.m_listener.destroy();
        }
        
        if (this.m_livelinessAutomatic != null) {
            this.m_livelinessAutomatic.stopTimer();
        }
        
        if (this.m_livelinessManRTPSParticipant != null) {
            this.m_livelinessManRTPSParticipant.stopTimer();
        }
    }

}
