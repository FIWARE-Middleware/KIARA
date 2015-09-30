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

import java.util.concurrent.locks.Lock;

import org.fiware.kiara.ps.rtps.attributes.BuiltinAttributes;
import org.fiware.kiara.ps.rtps.attributes.HistoryCacheAttributes;
import org.fiware.kiara.ps.rtps.attributes.ReaderAttributes;
import org.fiware.kiara.ps.rtps.attributes.RemoteReaderAttributes;
import org.fiware.kiara.ps.rtps.attributes.RemoteWriterAttributes;
import org.fiware.kiara.ps.rtps.attributes.WriterAttributes;
import org.fiware.kiara.ps.rtps.builtin.data.ParticipantProxyData;
import org.fiware.kiara.ps.rtps.builtin.data.ReaderProxyData;
import org.fiware.kiara.ps.rtps.builtin.data.WriterProxyData;
import org.fiware.kiara.ps.rtps.builtin.discovery.participant.PDPSimple;
import org.fiware.kiara.ps.rtps.common.DurabilityKind;
import org.fiware.kiara.ps.rtps.common.EncapsulationKind;
import org.fiware.kiara.ps.rtps.common.ReliabilityKind;
import org.fiware.kiara.ps.rtps.common.TopicKind;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.history.ReaderHistoryCache;
import org.fiware.kiara.ps.rtps.history.WriterHistoryCache;
import org.fiware.kiara.ps.rtps.messages.common.types.ChangeKind;
import org.fiware.kiara.ps.rtps.messages.common.types.RTPSEndian;
import org.fiware.kiara.ps.rtps.messages.elements.EntityId;
import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.fiware.kiara.ps.rtps.messages.elements.ParameterList;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.reader.RTPSReader;
import org.fiware.kiara.ps.rtps.reader.StatefulReader;
import org.fiware.kiara.ps.rtps.utils.InfoEndianness;
import org.fiware.kiara.ps.rtps.writer.RTPSWriter;
import org.fiware.kiara.ps.rtps.writer.StatefulWriter;
import org.fiware.kiara.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class EDPSimple, implements the Simple Endpoint Discovery Protocol defined in
 * the RTPS specification. Inherits from EDP class.
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class EDPSimple extends EDP {

    /**
     * Attributes related to discovery
     */
    public BuiltinAttributes discoveryAttributes;

    /**
     * Pair formed by {@link StatefulWriter} and an associasted {@link WriterHistoryCache}
     */
    public Pair<StatefulWriter, WriterHistoryCache> pubWriterPair;

    /**
     * Pair formed by {@link StatefulReader} and an associasted {@link ReaderHistoryCache}
     */
    public Pair<StatefulReader, ReaderHistoryCache> pubReaderPair;

    /**
     * Pair formed by {@link StatefulWriter} and an associasted {@link WriterHistoryCache}
     */
    public Pair<StatefulWriter, WriterHistoryCache> subWriterPair;

    /**
     * Pair formed by {@link StatefulReader} and an associasted {@link ReaderHistoryCache}
     */
    public Pair<StatefulReader, ReaderHistoryCache> subReaderPair;

    /**
     * Publication Listener instance of {@link EDPSimplePubListener}
     */
    public EDPSimplePubListener pubListener;

    /**
     * Subscription Listener instance of {@link EDPSimpleSubListener}
     */
    public EDPSimpleSubListener subListener;

    /*
     * Private Attributes
     */

    private static final Logger logger = LoggerFactory.getLogger(EDPSimple.class);

    /**
     * Constructor.
     *
     * @param pdpSimple Reference to the PDPSimple
     * @param rtpsParticipant Reference to the RTPSParticipantImpl
     */
    public EDPSimple(PDPSimple pdpSimple, RTPSParticipant rtpsParticipant) {
        super(pdpSimple, rtpsParticipant);
        this.pubWriterPair = new Pair<StatefulWriter, WriterHistoryCache>(null, null);
        this.pubReaderPair = new Pair<StatefulReader, ReaderHistoryCache>(null, null);
        this.subWriterPair = new Pair<StatefulWriter, WriterHistoryCache>(null, null);
        this.subReaderPair = new Pair<StatefulReader, ReaderHistoryCache>(null, null);
    }

    /**
     * Initialization method.
     *
     * @param discoveryAttributes Reference to the BuiltinAttributes.
     * @return True if correct.
     */
    @Override
    public boolean initEDP(BuiltinAttributes discoveryAttributes) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        logger.info("Beginning Simple Endpoint Discovery Protocol");
        this.discoveryAttributes = discoveryAttributes;

        if (!createSEDPEndpoints()) {
            return false;
        }

        return true;
    }


    /**
     * Method used to create the builting Endpoints needed for the SEDP
     * 
     * @return true if endpoints were correctly initialized; false otherwise
     */
    private boolean createSEDPEndpoints() {

        logger.info("Beginning to create SEDP Endpoints");
        boolean created = true;
        HistoryCacheAttributes historyAtt;

        if (this.discoveryAttributes.simpleEDP.usePulicationWriterAndSubscriptionReader) {

            historyAtt = new HistoryCacheAttributes();
            WriterAttributes writerAtt = new WriterAttributes();
            ReaderAttributes readerAtt = new ReaderAttributes();

            historyAtt.initialReservedCaches = 100;
            historyAtt.maximumReservedCaches = 5000;
            historyAtt.payloadMaxSize = ParticipantProxyData.DISCOVERY_PUBLICATION_DATA_MAX_SIZE;

            this.pubWriterPair.setSecond(new WriterHistoryCache(historyAtt));
            writerAtt.endpointAtt.reliabilityKind = ReliabilityKind.RELIABLE;
            writerAtt.endpointAtt.topicKind = TopicKind.WITH_KEY;
            writerAtt.endpointAtt.unicastLocatorList.copy(this.m_PDP.getLocalParticipantProxyData().getMetatrafficMulticastLocatorList());
            writerAtt.endpointAtt.multicastLocatorList.copy(this.m_PDP.getLocalParticipantProxyData().getMetatrafficMulticastLocatorList());
            writerAtt.endpointAtt.durabilityKind = DurabilityKind.TRANSIENT_LOCAL;
            RTPSWriter writer = this.m_RTPSParticipant.createWriter(writerAtt, this.pubWriterPair.getSecond(), null, EntityId.createSEDPPubWriter(), true);
            if (writer != null) {
                this.pubWriterPair.setFirst((StatefulWriter) writer);
                logger.debug("SEDP Publication Writer created");
            } else {
                // TODO
                created &= false;
            }

            historyAtt = new HistoryCacheAttributes();
            historyAtt.initialReservedCaches = 100;
            historyAtt.maximumReservedCaches = 1000000;
            historyAtt.payloadMaxSize = ParticipantProxyData.DISCOVERY_SUBSCRIPTION_DATA_MAX_SIZE;

            this.subReaderPair.setSecond(new ReaderHistoryCache(historyAtt));
            readerAtt.expectsInlineQos = false;
            readerAtt.endpointAtt.reliabilityKind = ReliabilityKind.RELIABLE;
            readerAtt.endpointAtt.topicKind = TopicKind.WITH_KEY;
            readerAtt.endpointAtt.unicastLocatorList.copy(this.m_PDP.getLocalParticipantProxyData().getMetatrafficUnicastLocatorList());
            readerAtt.endpointAtt.multicastLocatorList.copy(this.m_PDP.getLocalParticipantProxyData().getMetatrafficMulticastLocatorList());
            readerAtt.endpointAtt.durabilityKind = DurabilityKind.TRANSIENT_LOCAL;

            this.subListener = new EDPSimpleSubListener(this);
            RTPSReader reader = this.m_RTPSParticipant.createReader(readerAtt, this.subReaderPair.getSecond(), this.subListener, EntityId.createSEDPSubReader(), true);
            if (reader != null) {
                this.subReaderPair.setFirst((StatefulReader) reader);
                logger.debug("SEDP Subscription Reader created");
            } else {
                //TODO
            }

        }

        if (this.discoveryAttributes.simpleEDP.usePulicationReaderAndSubscriptionWriter) {

            historyAtt = new HistoryCacheAttributes();
            WriterAttributes writerAtt = new WriterAttributes();
            ReaderAttributes readerAtt = new ReaderAttributes();

            historyAtt.initialReservedCaches = 100;
            historyAtt.maximumReservedCaches = 1000000;
            historyAtt.payloadMaxSize = ParticipantProxyData.DISCOVERY_PUBLICATION_DATA_MAX_SIZE;

            this.pubReaderPair.setSecond(new ReaderHistoryCache(historyAtt));
            readerAtt.expectsInlineQos = false;
            readerAtt.endpointAtt.reliabilityKind = ReliabilityKind.RELIABLE;
            readerAtt.endpointAtt.topicKind = TopicKind.WITH_KEY;
            readerAtt.endpointAtt.unicastLocatorList.copy(this.m_PDP.getLocalParticipantProxyData().getMetatrafficUnicastLocatorList());
            readerAtt.endpointAtt.multicastLocatorList.copy(this.m_PDP.getLocalParticipantProxyData().getMetatrafficMulticastLocatorList());
            readerAtt.endpointAtt.durabilityKind = DurabilityKind.TRANSIENT_LOCAL;

            this.pubListener = new EDPSimplePubListener(this);
            RTPSReader reader = this.m_RTPSParticipant.createReader(readerAtt, this.pubReaderPair.getSecond(), this.pubListener, EntityId.createSEDPPubReader(), true);
            if (reader != null) {
                this.pubReaderPair.setFirst((StatefulReader) reader);
                logger.debug("SEDP Publication Reader created");
            } else {
                // TODO
                created &= false;
            }

            historyAtt = new HistoryCacheAttributes();
            historyAtt.initialReservedCaches = 100;
            historyAtt.maximumReservedCaches = 5000;
            historyAtt.payloadMaxSize = ParticipantProxyData.DISCOVERY_SUBSCRIPTION_DATA_MAX_SIZE;

            this.subWriterPair.setSecond(new WriterHistoryCache(historyAtt));
            writerAtt.endpointAtt.reliabilityKind = ReliabilityKind.RELIABLE;
            writerAtt.endpointAtt.topicKind = TopicKind.WITH_KEY;
            writerAtt.endpointAtt.unicastLocatorList.copy(this.m_PDP.getLocalParticipantProxyData().getMetatrafficUnicastLocatorList());
            writerAtt.endpointAtt.multicastLocatorList.copy(this.m_PDP.getLocalParticipantProxyData().getMetatrafficMulticastLocatorList());
            writerAtt.endpointAtt.durabilityKind = DurabilityKind.TRANSIENT_LOCAL;

            RTPSWriter writer = this.m_RTPSParticipant.createWriter(writerAtt, this.subWriterPair.getSecond(), null, EntityId.createSEDPSubWriter(), true);
            if (writer != null) {
                this.subWriterPair.setFirst((StatefulWriter) writer);
                logger.debug("SEDP Subscription Writer created");
            } else {
                // TODO
                created &= false;
            }

        }

        logger.info("SEDP Endpoints creation finished");
        return created;

    }

    /**
     * This method generates the corresponding change in the subscription writer
     * and send it to all known remote endpoints.
     *
     * @param rdata Reference to the ReaderProxyData object.
     * @return true if correct.
     */
    @Override
    public boolean processLocalReaderProxyData(ReaderProxyData rdata) {
        logger.debug("Processing LocalReaderProxy data with EntityId {}", rdata.getGUID().getEntityId());
        if (this.subWriterPair.getFirst() != null) {
            CacheChange change = this.subWriterPair.getFirst().newChange(ChangeKind.ALIVE, rdata.getKey());
            if (change != null) {
                ParameterList parameterList = rdata.toParameterList();
                change.getSerializedPayload().setEncapsulationKind(InfoEndianness.checkMachineEndianness() == RTPSEndian.BIG_ENDIAN ? EncapsulationKind.PL_CDR_BE : EncapsulationKind.PL_CDR_LE);
                change.getSerializedPayload().deleteParameters();
                change.getSerializedPayload().addParameters(parameterList);
                Lock mutex = this.subWriterPair.getSecond().getMutex();
                mutex.lock();
                try {
                    for (int i=0; i < this.subWriterPair.getSecond().getChanges().size(); ++i) {
                        CacheChange cit = this.subWriterPair.getSecond().getChanges().get(i);
                        if (cit.getInstanceHandle().equals(change.getInstanceHandle())) {
                            this.subWriterPair.getSecond().removeChange(cit);
                            i--;
                            break;
                        }
                    }
                } finally {
                    mutex.unlock();
                }
                this.subWriterPair.getSecond().addChange(change);
                return true;
            }
            return false;
        }
        return true;

    }

    /**
     * This method generates the corresponding change in the publciations writer
     * and send it to all known remote endpoints.
     *
     * @param wdata Reference to the WriterProxyData object.
     * @return true if correct.
     */
    @Override
    public boolean processLocalWriterProxyData(WriterProxyData wdata) {
        logger.debug("Processing LocalWriterProxy data with EntityId {}", wdata.getGUID().getEntityId());
        if (this.pubWriterPair.getFirst() != null) {
            CacheChange change = this.pubWriterPair.getFirst().newChange(ChangeKind.ALIVE, wdata.getKey());
            if (change != null) {
                ParameterList parameterList = wdata.toParameterList();
                change.getSerializedPayload().setEncapsulationKind(InfoEndianness.checkMachineEndianness() == RTPSEndian.BIG_ENDIAN ? EncapsulationKind.PL_CDR_BE : EncapsulationKind.PL_CDR_LE);
                change.getSerializedPayload().deleteParameters();
                change.getSerializedPayload().addParameters(parameterList);
                Lock mutex = this.pubWriterPair.getSecond().getMutex();
                mutex.lock();
                try {
                    for (int i=0; i < this.pubWriterPair.getSecond().getChanges().size(); ++i) {
                        CacheChange cit = this.pubWriterPair.getSecond().getChanges().get(i);
                        if (cit.getInstanceHandle().equals(change.getInstanceHandle())) {
                            this.pubWriterPair.getSecond().removeChange(change);
                            i--;
                            break;
                        }
                    }
                } finally {
                    mutex.unlock();
                }
                // this.lock(); // TODO FIXME
                this.pubWriterPair.getSecond().addChange(change);
                return true;
            }
            return false;
        }
        return true;
    }

    /**
     * This methods generates the change disposing of the local Reader and calls
     * the unpairing and removal methods of the base class.
     *
     * @param reader Reference to the RTPSReader object.
     * @return true if correct; false otherwise
     */
    @Override
    public boolean removeLocalReader(RTPSReader reader) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        logger.debug("Removing local reader {}", reader.getGuid().getEntityId());
        if (this.subWriterPair.getFirst() != null) {
            InstanceHandle instanceHandle = new InstanceHandle(reader.getGuid());
            CacheChange change = this.subWriterPair.getFirst().newChange(ChangeKind.NOT_ALIVE_DISPOSED_UNREGISTERED, instanceHandle);
            if (change != null) {
                Lock mutex = this.subWriterPair.getSecond().getMutex();
                mutex.lock();
                try {
                    for (int i=0; i < this.subWriterPair.getSecond().getChanges().size(); ++i) {
                        CacheChange cit = this.subWriterPair.getSecond().getChanges().get(i);
                        if (cit.getInstanceHandle().equals(change.getInstanceHandle())) {
                            this.subWriterPair.getSecond().removeChange(cit);
                            i--;
                            break;
                        }
                    }
                    this.subWriterPair.getSecond().addChange(change);
                } finally {
                    mutex.unlock();
                }
            }
        }
        return removeReaderProxy(reader.getGuid());
    }

    /**
     * This methods generates the change disposing of the local Writer and calls
     * the unpairing and removal methods of the base class.
     *
     * @param W Reference to the RTPSWriter object.
     * @return True if correct.
     */
    @Override
    public boolean removeLocalWriter(RTPSWriter writer) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        logger.debug("Removing local writer {}", writer.getGuid().getEntityId());
        if (this.pubWriterPair.getFirst() != null) {
            InstanceHandle instanceHandle = new InstanceHandle(writer.getGuid());
            CacheChange change = this.pubWriterPair.getFirst().newChange(ChangeKind.NOT_ALIVE_DISPOSED_UNREGISTERED, instanceHandle);
            if (change != null) {
                Lock mutex = this.pubWriterPair.getSecond().getMutex();
                mutex.lock();
                try {
                    for (int i=0; i < this.pubWriterPair.getSecond().getChanges().size(); ++i) {
                        CacheChange cit = this.pubWriterPair.getSecond().getChanges().get(i);
                        if (cit.getInstanceHandle().equals(change.getInstanceHandle())) {
                            this.pubWriterPair.getSecond().removeChange(cit);
                            i--;
                            break;
                        }
                    }
                    this.pubWriterPair.getSecond().addChange(change);
                } finally {
                    mutex.unlock();
                }
            }
        }
        return removeWriterProxy(writer.getGuid());
    }

    /**
     * This method assigns the remote builtin endpoints that the remote
     * RTPSParticipant indicates is using to our local builtin endpoints.
     *
     * @param pdata GUIDPrefix the RTPSParticipantProxyData object.
     */
    @Override
    public void assignRemoteEndpoints(ParticipantProxyData pdata) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        logger.debug("New EDP received, adding remote Endpoints to the SimpleEDP endpoints");
        int endp = pdata.getAvailableBuiltinEndpoints();
        int auxEndp = endp;

        Lock mutex = pdata.getMutex();
        mutex.lock();
        try {
            auxEndp &= ParticipantProxyData.DISC_BUILTIN_ENDPOINT_PUBLICATION_ANNOUNCER;
            if (auxEndp != 0 && this.pubReaderPair.getFirst() != null) { 
                logger.debug("Adding SEDP Pub Writer to the Pub Reader");
                RemoteWriterAttributes watt = new RemoteWriterAttributes();
                watt.guid.getGUIDPrefix().copy(pdata.getGUID().getGUIDPrefix());
                watt.guid.getEntityId().copy(EntityId.createSEDPPubWriter());
                watt.endpoint.unicastLocatorList.copy(pdata.getMetatrafficUnicastLocatorList());
                watt.endpoint.multicastLocatorList.copy(pdata.getMetatrafficMulticastLocatorList());
                watt.endpoint.reliabilityKind = ReliabilityKind.RELIABLE;
                watt.endpoint.durabilityKind = DurabilityKind.TRANSIENT_LOCAL;
                pdata.getBuiltinWriters().add(watt);
                this.pubReaderPair.getFirst().matchedWriterAdd(watt);
            }

            auxEndp = endp;
            auxEndp &= ParticipantProxyData.DISC_BUILTIN_ENDPOINT_PUBLICATION_DETECTOR;
            if (auxEndp != 0 && this.pubWriterPair.getFirst() != null) {
                logger.debug("Adding SEDP Pub Reader to the Pub Writer");
                RemoteReaderAttributes ratt = new RemoteReaderAttributes(); 
                ratt.expectsInlineQos = false;
                ratt.guid.getGUIDPrefix().copy(pdata.getGUID().getGUIDPrefix());
                ratt.guid.getEntityId().copy(EntityId.createSEDPPubReader());
                ratt.endpoint.unicastLocatorList.copy(pdata.getMetatrafficUnicastLocatorList());
                ratt.endpoint.multicastLocatorList.copy(pdata.getMetatrafficMulticastLocatorList());
                ratt.endpoint.durabilityKind = DurabilityKind.TRANSIENT_LOCAL;
                ratt.endpoint.reliabilityKind = ReliabilityKind.RELIABLE;
                pdata.getBuiltinReaders().add(ratt);
                this.pubWriterPair.getFirst().matchedReaderAdd(ratt);
            }

            auxEndp = endp;
            auxEndp &= ParticipantProxyData.DISC_BUILTIN_ENDPOINT_SUBSCRIPTION_ANNOUNCER;
            if (auxEndp != 0 && this.subReaderPair.getFirst() != null) {
                logger.debug("Adding SEDP Sub Writer to the Sub Reader");
                RemoteWriterAttributes watt = new RemoteWriterAttributes();
                watt.guid.getGUIDPrefix().copy(pdata.getGUID().getGUIDPrefix());
                watt.guid.getEntityId().copy(EntityId.createSEDPSubWriter());
                watt.endpoint.unicastLocatorList.copy(pdata.getMetatrafficUnicastLocatorList());
                watt.endpoint.multicastLocatorList.copy(pdata.getMetatrafficMulticastLocatorList());
                watt.endpoint.reliabilityKind = ReliabilityKind.RELIABLE;
                watt.endpoint.durabilityKind = DurabilityKind.TRANSIENT_LOCAL;
                pdata.getBuiltinWriters().add(watt);
                this.subReaderPair.getFirst().matchedWriterAdd(watt);
            }

            auxEndp = endp;
            auxEndp &= ParticipantProxyData.DISC_BUILTIN_ENDPOINT_SUBSCRIPTION_DETECTOR;
            if (auxEndp != 0 && this.subWriterPair.getFirst() != null) {
                logger.debug("Adding SEDP Sub Reader to the Sub Writer");
                RemoteReaderAttributes ratt = new RemoteReaderAttributes(); 
                ratt.expectsInlineQos = false;
                ratt.guid.getGUIDPrefix().copy(pdata.getGUID().getGUIDPrefix());
                ratt.guid.getEntityId().copy(EntityId.createSEDPSubReader());
                ratt.endpoint.unicastLocatorList.copy(pdata.getMetatrafficUnicastLocatorList());
                ratt.endpoint.multicastLocatorList.copy(pdata.getMetatrafficMulticastLocatorList());
                ratt.endpoint.durabilityKind = DurabilityKind.TRANSIENT_LOCAL;
                ratt.endpoint.reliabilityKind = ReliabilityKind.RELIABLE;
                pdata.getBuiltinReaders().add(ratt);
                this.subWriterPair.getFirst().matchedReaderAdd(ratt);
            }

            //            this.release();

        } finally {
            mutex.unlock();
        }
    }

    public void removeRemoteEndpoints(ParticipantProxyData pdata) {
        logger.debug("Removing remote endpoints for RTPSParticipant {}", pdata.getGUID());
        Lock mutex = pdata.getMutex();
        mutex.lock();
        try {
            for (RemoteReaderAttributes it : pdata.getBuiltinReaders()) {
                if (it.guid.getEntityId().equals(EntityId.createSEDPPubReader()) && this.pubWriterPair.getFirst() != null) {
                    this.pubWriterPair.getFirst().matchedReaderRemove(it);
                    continue;
                }
                if (it.guid.getEntityId().equals(EntityId.createSEDPSubReader()) && this.subWriterPair.getFirst() != null) {
                    this.subWriterPair.getFirst().matchedReaderRemove(it);
                    continue;
                }
            }
            for (RemoteWriterAttributes it : pdata.getBuiltinWriters()) {
                if (it.guid.getEntityId().equals(EntityId.createSEDPPubWriter()) && this.pubReaderPair.getFirst() != null) {
                    this.pubReaderPair.getFirst().matchedWriterRemove(it);
                    continue;
                }
                if (it.guid.getEntityId().equals(EntityId.createSEDPSubWriter()) && this.subReaderPair.getFirst() != null) {
                    this.subReaderPair.getFirst().matchedWriterRemove(it);
                    continue;
                }
            }
        } finally {
            mutex.unlock();
        }
    }


}
