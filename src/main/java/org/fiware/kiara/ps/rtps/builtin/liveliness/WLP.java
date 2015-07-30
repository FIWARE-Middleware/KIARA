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

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.fiware.kiara.ps.qos.WriterQos;
import org.fiware.kiara.ps.rtps.attributes.HistoryCacheAttributes;
import org.fiware.kiara.ps.rtps.attributes.ReaderAttributes;
import org.fiware.kiara.ps.rtps.attributes.WriterAttributes;
import org.fiware.kiara.ps.rtps.builtin.BuiltinProtocols;
import org.fiware.kiara.ps.rtps.builtin.data.ParticipantProxyData;
import static org.fiware.kiara.ps.rtps.builtin.data.ParticipantProxyData.BUILTIN_PARTICIPANT_DATA_MAX_SIZE;
import org.fiware.kiara.ps.rtps.builtin.liveliness.timedevent.WLivelinessPeriodicAssertion;
import static org.fiware.kiara.ps.rtps.common.DurabilityKind.TRANSIENT_LOCAL;
import static org.fiware.kiara.ps.rtps.common.ReliabilityKind.RELIABLE;
import static org.fiware.kiara.ps.rtps.common.TopicKind.WITH_KEY;
import org.fiware.kiara.ps.rtps.history.ReaderHistoryCache;
import org.fiware.kiara.ps.rtps.history.WriterHistoryCache;
import org.fiware.kiara.ps.rtps.messages.elements.EntityId;
import static org.fiware.kiara.ps.rtps.messages.elements.EntityId.EntityIdEnum.ENTITYID_P2P_BUILTIN_RTPSPARTICIPANT_MESSAGE_READER;
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
 * @ingroup LIVELINESS_MODULE
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public class WLP {

    private static final Logger logger = LoggerFactory.getLogger(WLP.class);

    /**
     * Pointer to the local RTPSParticipant.
     */
    private RTPSParticipant m_participant;

    /**
     * Pointer to the builtinprotocol class.
     */
    private BuiltinProtocols m_builtinProtocols;

    /**
     * Pointer to the builtinRTPSParticipantMEssageWriter.
     */
    private StatefulWriter m_builtinWriter;

    /**
     * Pointer to the builtinRTPSParticipantMEssageReader.
     */
    private StatefulReader m_builtinReader;

    /**
     * Hist
     */
    WriterHistoryCache m_builtinWriterHistory;
    ReaderHistoryCache m_builtinReaderHistory;

    /**
     * Listener object.
     */
    private WLPListener m_listener;
    /**
     * Pointer to the periodic assertion timer object for the automatic
     * liveliness writers.
     */
    private WLivelinessPeriodicAssertion m_livelinessAutomatic;
    /**
     * Pointer to the periodic assertion timer object for the manual by
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
     * Constructor
     *
     * @param builtinProtocols Pointer to the BuiltinProtocols object.
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
        HistoryCacheAttributes hatt = new HistoryCacheAttributes();
        hatt.initialReservedCaches = 20;
        hatt.maximumReservedCaches = 1000;
        hatt.payloadMaxSize = BUILTIN_PARTICIPANT_DATA_MAX_SIZE;
        m_builtinWriterHistory = new WriterHistoryCache(hatt);
        WriterAttributes watt = new WriterAttributes();
        watt.endpointAtt.unicastLocatorList.copy(m_builtinProtocols.getMetatrafficUnicastLocatorList());
        watt.endpointAtt.multicastLocatorList.copy(m_builtinProtocols.getMetatrafficMulticastLocatorList());
        //	Wparam.topic.topicName = "DCPSRTPSParticipantMessage";
        //	Wparam.topic.topicDataType = "RTPSParticipantMessageData";
        watt.endpointAtt.topicKind = WITH_KEY;
        watt.endpointAtt.durabilityKind = TRANSIENT_LOCAL;
        watt.endpointAtt.reliabilityKind = RELIABLE;
        RTPSWriter wout = m_participant.createWriter(watt, m_builtinWriterHistory, null, new EntityId(EntityId.EntityIdEnum.ENTITYID_P2P_BUILTIN_RTPSPARTICIPANT_MESSAGE_WRITER), true);
        if (wout != null) {
            m_builtinWriter = (StatefulWriter) (wout);
            logger.info("RTPS LIVELINESS: Builtin Liveliness Writer created");
        } else {
            logger.error("RTPS LIVELINESS: Liveliness Writer Creation failed ");
            m_builtinWriterHistory = null;
            return false;
        }
        hatt.initialReservedCaches = 100;
        hatt.maximumReservedCaches = 2000;
        hatt.payloadMaxSize = BUILTIN_PARTICIPANT_DATA_MAX_SIZE;
        m_builtinReaderHistory = new ReaderHistoryCache(hatt);
        ReaderAttributes ratt = new ReaderAttributes();
        ratt.endpointAtt.topicKind = WITH_KEY;
        ratt.endpointAtt.durabilityKind = TRANSIENT_LOCAL;
        ratt.endpointAtt.reliabilityKind = RELIABLE;
        ratt.expectsInlineQos = true;
        ratt.endpointAtt.unicastLocatorList.copy(m_builtinProtocols.getMetatrafficUnicastLocatorList());
        ratt.endpointAtt.multicastLocatorList.copy(m_builtinProtocols.getMetatrafficMulticastLocatorList());
        //Rparam.topic.topicName = "DCPSRTPSParticipantMessage";
        //Rparam.topic.topicDataType = "RTPSParticipantMessageData";
        ratt.endpointAtt.topicKind = WITH_KEY;
        //LISTENER CREATION
        m_listener = new WLPListener(this);
        RTPSReader rout = m_participant.createReader(ratt, m_builtinReaderHistory, (ReaderListener) m_listener, new EntityId(ENTITYID_P2P_BUILTIN_RTPSPARTICIPANT_MESSAGE_READER), true);
        if (rout != null) {
            m_builtinReader = (StatefulReader) (rout);
            logger.info("RTPS LIVELINESS: Builtin Liveliness Reader created");
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
     * @param pdata Pointer to the RTPSParticipantProxyData object.
     * @return True if correct.
     */
    public void assignRemoteEndpoints(ParticipantProxyData pdata) {
        // TODO Auto-generated method stub

    }

    /**
     * Remove remote endpoints from the liveliness protocol.
     *
     * @param pdata Pointer to the ParticipantProxyData to remove
     */
    public void removeRemoteEndpoints(ParticipantProxyData pdata) {
        // TODO Auto-generated method stub

    }

    /**
     * Add a local writer to the liveliness protocol.
     *
     * @param W Pointer to the RTPSWriter.
     * @return True if correct.
     */
    public boolean addLocalWriter(RTPSWriter writer, WriterQos wqos) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Remove a local writer from the liveliness protocol.
     *
     * @param Pointer to the RTPSWriter.
     * @return True if correct.
     */
    public boolean removeLocalWriter(RTPSWriter writer) {
        // TODO Auto-generated method stub
        return false;
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
     * @param W Writer to update
     * @param wqos New writer QoS
     * @return True on success
     */
    public boolean updateLocalWriter(RTPSWriter writer, WriterQos wqos) {
        // TODO Auto-generated method stub
        return false;
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

    public void destroy() {
        // TODO Auto-generated method stub

    }

}
