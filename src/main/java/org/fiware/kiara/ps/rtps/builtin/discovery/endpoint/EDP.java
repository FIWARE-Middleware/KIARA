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

import java.util.Objects;
import java.util.concurrent.locks.Lock;

import static org.fiware.kiara.ps.qos.policies.DurabilityQosPolicyKind.TRANSIENT_LOCAL_DURABILITY_QOS;
import static org.fiware.kiara.ps.qos.policies.DurabilityQosPolicyKind.VOLATILE_DURABILITY_QOS;
import static org.fiware.kiara.ps.qos.policies.ReliabilityQosPolicyKind.BEST_EFFORT_RELIABILITY_QOS;
import static org.fiware.kiara.ps.qos.policies.ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS;

import org.fiware.kiara.ps.attributes.TopicAttributes;
import org.fiware.kiara.ps.qos.ReaderQos;
import org.fiware.kiara.ps.qos.WriterQos;
import org.fiware.kiara.ps.rtps.attributes.BuiltinAttributes;
import org.fiware.kiara.ps.rtps.attributes.RemoteReaderAttributes;
import org.fiware.kiara.ps.rtps.attributes.RemoteWriterAttributes;
import org.fiware.kiara.ps.rtps.builtin.data.ParticipantProxyData;
import org.fiware.kiara.ps.rtps.builtin.data.ReaderProxyData;
import org.fiware.kiara.ps.rtps.builtin.data.WriterProxyData;
import org.fiware.kiara.ps.rtps.builtin.discovery.participant.PDPSimple;
import org.fiware.kiara.ps.rtps.common.MatchingInfo;
import org.fiware.kiara.ps.rtps.common.MatchingStatus;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.reader.RTPSReader;
import org.fiware.kiara.ps.rtps.utils.StringMatching;
import org.fiware.kiara.ps.rtps.writer.RTPSWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class EDP, base class for Endpoint Discovery Protocols. It contains generic
 * methods used by the two EDP implemented (EDPSimple and EDPStatic), as well as
 * abstract methods definitions required by the specific implementations.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public abstract class EDP {

    /**
     * Reference to the PDPSimple object that contains the endpoint discovery
     * protocol.
     */
    protected PDPSimple m_PDP;
    /**
     * Reference to the RTPSParticipant.
     */
    protected RTPSParticipant m_RTPSParticipant;

    private static final Logger logger = LoggerFactory.getLogger(EDP.class);
    
    /**
     * Constructor.
     *
     * @param p Reference to the PDPSimple
     * @param part Reference to the RTPSParticipantImpl
     */
    public EDP(PDPSimple p, RTPSParticipant part) {
        m_PDP = p;
        m_RTPSParticipant = part;
    }

    /**
     * Abstract method to initialize the EDP.
     *
     * @param attributes DiscoveryAttributes structure.
     * @return True if correct.
     */
    public abstract boolean initEDP(BuiltinAttributes attributes);

    /**
     * Abstract method that assigns remote endpoints when a new
     * RTPSParticipantProxyData is discovered.
     *
     * @param pdata Discovered ParticipantProxyData
     */
    public abstract void assignRemoteEndpoints(ParticipantProxyData pdata);

    /**
     * Remove remote endpoints from the endpoint discovery protocol
     *
     * @param pdata Reference to the ParticipantProxyData to remove
     */
    public void removeRemoteEndpoints(ParticipantProxyData pdata) {
    }

    /**
     * Abstract method that removes a local Reader from the discovery method
     *
     * @param R Reference to the Reader to remove.
     * @return True if correctly removed.
     */
    public abstract boolean removeLocalReader(RTPSReader R);

    /**
     * Abstract method that removes a local Writer from the discovery method
     *
     * @param W Reference to the Writer to remove.
     * @return True if correctly removed.
     */
    public abstract boolean removeLocalWriter(RTPSWriter W);

    /**
     * After a new local ReaderProxyData has been created some processing is
     * needed (depends on the implementation).
     *
     * @param rdata Reference to the ReaderProxyData object.
     * @return True if correct.
     */
    public abstract boolean processLocalReaderProxyData(ReaderProxyData rdata);

    /**
     * After a new local WriterProxyData has been created some processing is
     * needed (depends on the implementation).
     *
     * @param wdata Reference to the Writer ProxyData object.
     * @return True if correct.
     */
    public abstract boolean processLocalWriterProxyData(WriterProxyData wdata);

    /**
     * Create a new ReaderPD for a local Reader.
     *
     * @param reader Reference to the RTPSReader.
     * @param att
     * @param rqos
     * @return True if correct.
     */
    public boolean newLocalReaderProxyData(RTPSReader reader, TopicAttributes att, ReaderQos rqos) {

        logger.debug("Adding Reader {} in topic {}", reader.getGuid().getEntityId(), att.topicName);

        ReaderProxyData rpd = new ReaderProxyData();
        rpd.setIsAlive(true);
        rpd.setExpectsInlineQos(reader.getExpectsInlineQos());
        rpd.setGUID(reader.getGuid());
        rpd.setKey(rpd.getGUID());
        rpd.setMulticastLocatorList(reader.getAttributes().getMulticastLocatorList());
        rpd.setUnicastLocatorList(reader.getAttributes().getUnicastLocatorList());
        rpd.setRTPSParticipantKey(m_RTPSParticipant.getGUID());
        rpd.setTopicName(att.getTopicName());
        rpd.setTypeName(att.getTopicDataType());
        rpd.setTopicKind(att.getTopicKind());
        rpd.setQos(rqos);
        rpd.setUserDefinedId(reader.getAttributes().getUserDefinedID());
        reader.setAcceptMessagesFromUnknownWriters(false);
        
        //ADD IT TO THE LIST OF READERPROXYDATA
        if (!this.m_PDP.addReaderProxyData(rpd)) {
            return false;
        }
        
        //DO SOME PROCESSING DEPENDING ON THE IMPLEMENTATION (SIMPLE OR STATIC)
        processLocalReaderProxyData(rpd);
        
        //PAIRING
        pairingReader(reader);
        pairingReaderProxy(rpd);
        return true;
    }

    /**
     * Create a new ReaderPD for a local Writer.
     *
     * @param writer Reference to the RTPSWriter.
     * @param att
     * @param wqos
     * @return True if correct.
     */
    public boolean newLocalWriterProxyData(RTPSWriter writer, TopicAttributes att, WriterQos wqos) {
        
        logger.debug("Adding Writer {} in topic {}", writer.getGuid().getEntityId(), att.topicName);

        WriterProxyData wpd = new WriterProxyData();
        wpd.setIsAlive(true);
        wpd.setGUID(writer.getGuid());
        wpd.setKey(wpd.getGUID());
        wpd.setMulticastLocatorList(writer.getAttributes().getMulticastLocatorList());
        wpd.setUnicastLocatorList(writer.getAttributes().getUnicastLocatorList());
        wpd.setRTPSParticipantKey(m_RTPSParticipant.getGUID());
        wpd.setTopicName(att.getTopicName());
        wpd.setTypeName(att.getTopicDataType());
        wpd.setTopicKind(att.getTopicKind());
        wpd.setTypeMaxSerialized(writer.getTypeMaxSerialized());
        wpd.setQos(wqos);
        wpd.setUserDefinedId(writer.getAttributes().getUserDefinedID());
        
        //ADD IT TO THE LIST OF READERPROXYDATA
        if (!this.m_PDP.addWriterProxyData(wpd)) {
            return false;
        }
        
        //DO SOME PROCESSING DEPENDING ON THE IMPLEMENTATION (SIMPLE OR STATIC)
        processLocalWriterProxyData(wpd);
        
        //PAIRING
        pairingWriterProxy(wpd);
        pairingWriter(writer);
        return true;
    }

    /**
     * A previously created Reader has been updated
     *
     * @param reader Reference to the reader;
     * @param rqos
     * @return True if correctly updated
     */
    public boolean updatedLocalReader(RTPSReader reader, ReaderQos rqos) {
        ReaderProxyData rdata = this.m_PDP.lookupReaderProxyData(reader.getGuid());
        if (rdata != null) {
            rdata.getQos().setQos(rqos, false);
            rdata.setExpectsInlineQos(reader.getExpectsInlineQos());
            processLocalReaderProxyData(rdata);
            //this.updatedReaderProxy(rdata);
            pairingReaderProxy(rdata);
            pairingReader(reader);
            return true;
        }
        return false;
    }

    /**
     * A previously created Writer has been updated
     *
     * @param writer Reference to the Writer
     * @param wqos
     * @return True if correctly updated
     */
    public boolean updatedLocalWriter(RTPSWriter writer, WriterQos wqos) {
        WriterProxyData wdata = this.m_PDP.lookupWriterProxyData(writer.getGuid());
        if (wdata != null) {
            wdata.getQos().setQos(wqos, false);
            processLocalWriterProxyData(wdata);
            //this.updatedWriterProxy(wdata);
            pairingWriterProxy(wdata);
            pairingWriter(writer);
            return true;
        }
        return false;
    }

    /**
     * Remove a WriterProxyDataObject based on its GUID_t.
     *
     * @param writer Reference to the writer GUID.
     * @return True if correct.
     */
    public boolean removeWriterProxy(GUID writer) {
        logger.debug("RTPS EDP Trying to remove WRITER {}", writer);
        WriterProxyData wdata = this.m_PDP.lookupWriterProxyData(writer);
        if (wdata != null) {
            logger.debug("RTPS_EDP Found WRITER {} in topic {}", writer, wdata.getTopicName());
            unpairWriterProxy(wdata);
            this.m_PDP.removeWriterProxyData(wdata);
            return true;
        }
        return false;
    }

    /**
     * Remove a ReaderProxyDataObject based on its GUID.
     *
     * @param reader Reference to the reader GUID.
     * @return True if correct.
     */
    public boolean removeReaderProxy(GUID reader) {
        logger.debug("RTPS EDP Trying to remove READER {}", reader);
        ReaderProxyData rdata = this.m_PDP.lookupReaderProxyData(reader);
        if (rdata != null) {
            logger.debug("RTPS_EDP Found READER {} in topic {}", reader, rdata.getTopicName());
            unpairReaderProxy(rdata);
            this.m_PDP.removeReaderProxyData(rdata);
            return true;
        }
        return false;
    }

    /**
     * Unpair a WriterProxyData object from all local readers.
     *
     * @param wdata Reference to the WriterProxyData object.
     * @return True if correct.
     */
    public boolean unpairWriterProxy(WriterProxyData wdata) {
        logger.debug("RTPS_EDP Unpairing WRITER {} in topic {}", wdata.getGUID(), wdata.getTopicName());
        final Lock mutex = m_RTPSParticipant.getParticipantMutex();
        mutex.lock();
        try {
            for (RTPSReader rit : m_RTPSParticipant.getUserReaders()) {
                RemoteWriterAttributes watt = new RemoteWriterAttributes();
                watt.setGUID(wdata.getGUID());
                if (rit.matchedWriterRemove(watt)) {
                    //MATCHED AND ADDED CORRECTLY:
                    if (rit.getListener() != null) {
                        MatchingInfo info = new MatchingInfo(MatchingStatus.REMOVED_MATCHING, wdata.getGUID());
                        rit.getListener().onReaderMatched(rit, info);
                    }
                }
            }
            return true;
        } finally {
            mutex.unlock();
        }
    }

    /**
     * Unpair a ReaderProxyData object from all local writers.
     *
     * @param rdata Reference to the ReaderProxyData object.
     * @return True if correct.
     */
    public boolean unpairReaderProxy(ReaderProxyData rdata) {
        logger.debug("RTPS_EDP Unpairing READER {} in topic {}", rdata.getGUID(), rdata.getTopicName());
        final Lock mutex = m_RTPSParticipant.getParticipantMutex();
        mutex.lock();
        try {
            for (RTPSWriter wit : m_RTPSParticipant.getUserWriters()) {
                RemoteReaderAttributes ratt = new RemoteReaderAttributes();
                ratt.setGUID(rdata.getGUID());
                if (wit.matchedReaderRemove(ratt)) {
                    //MATCHED AND ADDED CORRECTLY:
                    if (wit.getListener() != null) {
                        MatchingInfo info = new MatchingInfo(MatchingStatus.REMOVED_MATCHING, rdata.getGUID());
                        wit.getListener().onWriterMatched(wit, info);
                    }
                }
            }
            return true;
        } finally {
            mutex.unlock();
        }
    }

    /**
     * Check the validity of a matching between a RTPSWriter and a
     * ReaderProxyData object.
     *
     * @param wdata Reference to the WriterProxyData object.
     * @param rdata Reference to the ReaderProxyData object.
     * @return True if the two can be matched.
     */
    public boolean validMatching(WriterProxyData wdata, ReaderProxyData rdata) {
        if (!Objects.equals(wdata.getTopicName(), rdata.getTopicName())) {
            return false;
        }
        if (!Objects.equals(wdata.getTypeName(), rdata.getTypeName())) {
            return false;
        }
        if (wdata.getTopicKind() != rdata.getTopicKind()) {
            logger.warn("RTPS EDP: INCOMPATIBLE QOS:Remote Reader {} is publishing in topic {} (keyed:{}), local writer publishes as keyed: {}",
                    rdata.getGUID(), rdata.getTopicName(), rdata.getTopicKind(), wdata.getTopicKind());
        }
        if (!rdata.getIsAlive()) { //Matching
            logger.warn("RTPS EDP: ReaderProxyData object is NOT alive");
            return false;
        }
        if (wdata.getQos().reliability.kind == BEST_EFFORT_RELIABILITY_QOS
                && rdata.getQos().reliability.kind == RELIABLE_RELIABILITY_QOS) //Means our writer is BE but the reader wants RE
        {
            logger.warn("RTPS EDP: INCOMPATIBLE QOS (topic: {}):Remote Reader {} is Reliable and local writer is BE ",
                    rdata.getTopicName(), rdata.getGUID());
            return false;
        }
        if (wdata.getQos().durability.kind == VOLATILE_DURABILITY_QOS
                && rdata.getQos().durability.kind == TRANSIENT_LOCAL_DURABILITY_QOS) {
            logger.warn("RTPS EDP: INCOMPATIBLE QOS (topic: {}):RemoteReader has TRANSIENT_LOCAL DURABILITY and we offer VOLATILE",
                    rdata.getTopicName(), rdata.getGUID());
            return false;
        }
        if (wdata.getQos().ownership.kind != rdata.getQos().ownership.kind) {
            logger.warn("RTPS EDP: INCOMPATIBLE QOS (topic: {}):Remote reader {} has different Ownership Kind",
                    rdata.getTopicName(), rdata.getGUID());
            return false;
        }
        //Partition check:
        boolean matched = false;
        if (wdata.getQos().partition.getNames().isEmpty() && rdata.getQos().partition.getNames().isEmpty()) {
            matched = true;
        } else if (wdata.getQos().partition.getNames().isEmpty() && rdata.getQos().partition.getNames().size() > 0) {
            for (String rnameit : rdata.getQos().partition.getNames()) {
                if (rnameit.length() == 0) {
                    matched = true;
                    break;
                }
            }
        } else if (wdata.getQos().partition.getNames().size() > 0 && rdata.getQos().partition.getNames().isEmpty()) {
            for (String wnameit : wdata.getQos().partition.getNames()) {
                if (wnameit.length() == 0) {
                    matched = true;
                    break;
                }

            }
        } else {
            for (String wnameit : wdata.getQos().partition.getNames()) {
                for (String rnameit : rdata.getQos().partition.getNames()) {
                    if (StringMatching.matchString(wnameit, rnameit)) {
                        matched = true;
                        break;
                    }
                }
                if (matched) {
                    break;
                }
            }
        }
        if (!matched) //Different partitions
        {
            logger.warn("RTPS EDP: INCOMPATIBLE QOS (topic: {}): Different Partitions", rdata.getTopicName());
        }
        return matched;
    }

    /**
     * Check the validity of a matching between a RTPSReader and a
     * WriterProxyData object.
     *
     * @param rdata Reference to the ReaderProxyData object.
     * @param wdata Reference to the WriterProxyData object.
     * @return True if the two can be matched.
     */
    public boolean validMatching(ReaderProxyData rdata, WriterProxyData wdata) {
        if (!Objects.equals(rdata.getTopicName(), wdata.getTopicName())) {
            return false;
        }
        if (!Objects.equals(rdata.getTypeName(), wdata.getTypeName())) {
            return false;
        }
        if (rdata.getTopicKind() != wdata.getTopicKind()) {
            logger.warn("RTPS EDP: INCOMPATIBLE QOS:Remote Writer {} is publishing in topic {}(keyed:{}), local reader subscribes as keyed: {}",
                    wdata.getGUID(), wdata.getTopicName(), rdata.getTopicKind());
        }
        if (!wdata.getIsAlive()) {//Matching
            logger.warn("RTPS EDP: WriterProxyData {} is NOT alive", wdata.getGUID());
            return false;
        }
        if (rdata.getQos().reliability.kind == RELIABLE_RELIABILITY_QOS
                && wdata.getQos().reliability.kind == BEST_EFFORT_RELIABILITY_QOS) { //Means our reader is reliable but hte writer is not
            logger.warn("RTPS EDP: INCOMPATIBLE QOS (topic: {}): Remote Writer {} is Best Effort and local reader is RELIABLE ",
                    wdata.getTopicName(), wdata.getGUID());
            return false;
        }
        if (rdata.getQos().durability.kind == TRANSIENT_LOCAL_DURABILITY_QOS
                && wdata.getQos().durability.kind == VOLATILE_DURABILITY_QOS) {
            logger.warn("RTPS EDP: INCOMPATIBLE QOS (topic: {}):RemoteWriter {} has VOLATILE DURABILITY and we want TRANSIENT_LOCAL", wdata.getTopicName(), wdata.getGUID());
            return false;
        }
        if (rdata.getQos().ownership.kind != wdata.getQos().ownership.kind) {
            logger.warn("RTPS EDP: INCOMPATIBLE QOS (topic: {}):Remote Writer has different Ownership Kind", wdata.getTopicName(), wdata.getGUID());
            return false;
        }
        //Partition check:
        boolean matched = false;
        if (rdata.getQos().partition.getNames().isEmpty() && wdata.getQos().partition.getNames().isEmpty()) {
            matched = true;
        } else if (rdata.getQos().partition.getNames().isEmpty() && wdata.getQos().partition.getNames().size() > 0) {
            for (String rnameit : wdata.getQos().partition.getNames()) {
                if (rnameit.length() == 0) {
                    matched = true;
                    break;
                }
            }
        } else if (rdata.getQos().partition.getNames().size() > 0 && wdata.getQos().partition.getNames().isEmpty()) {
            for (String wnameit : rdata.getQos().partition.getNames()) {
                if (wnameit.length() == 0) {
                    matched = true;
                    break;
                }
            }
        } else {
            for (String wnameit : rdata.getQos().partition.getNames()) {
                for (String rnameit : wdata.getQos().partition.getNames()) {
                    if (StringMatching.matchString(wnameit, rnameit)) {
                        matched = true;
                        break;
                    }
                }
                if (matched) {
                    break;
                }
            }
        }
        if (!matched) { //Different partitions
            logger.warn("RTPS EDP: INCOMPATIBLE QOS (topic: {}): Different Partitions", wdata.getTopicName());
        }
        return matched;
    }

    /**
     * Try to pair/unpair a local Reader against all possible writerProxy Data.
     *
     * @param R Reference to the Reader
     * @return True
     */
    public boolean pairingReader(RTPSReader R) {
        ReaderProxyData rdata = this.m_PDP.lookupReaderProxyData(R.getGuid());
        if (rdata != null) {
            logger.debug("Pairing Reader {} in topic {}", R.getGuid(), rdata.getTopicName());
            final Lock mutex = m_PDP.getMutex();
            mutex.lock();
            try {
                for (ParticipantProxyData pit : m_PDP.getParticipantProxies()) {
                    final Lock mutex1 = pit.getMutex();
                    mutex1.lock();
                    try {
                        for (WriterProxyData wdatait : pit.getWriters()) {
                            if (validMatching(rdata, wdatait)) {
                                logger.debug("Valid Matching to writerProxy: {}", wdatait.getGUID());
                                if (R.matchedWriterAdd(wdatait.toRemoteWriterAttributes())) {
                                    //MATCHED AND ADDED CORRECTLY:
                                    if (R.getListener() != null) {
                                        MatchingInfo info = new MatchingInfo(MatchingStatus.MATCHED_MATHING, wdatait.getGUID());
                                        R.getListener().onReaderMatched(R, info);
                                    }
                                }
                            } else {
                                //logger.info(RTPS_EDP,RTPS_CYAN<<"Valid Matching to writerProxy: "<<(*wdatait).m_guid<<RTPS_DEF<<endl);
                                if (R.matchedWriterIsMatched(wdatait.toRemoteWriterAttributes())
                                        && R.matchedWriterRemove(wdatait.toRemoteWriterAttributes())) {
                                    //MATCHED AND ADDED CORRECTLY:
                                    if (R.getListener() != null) {
                                        MatchingInfo info = new MatchingInfo(MatchingStatus.REMOVED_MATCHING, wdatait.getGUID());
                                        R.getListener().onReaderMatched(R, info);
                                    }
                                }
                            }
                        }
                    } finally {
                        mutex1.unlock();
                    }
                }
            } finally {
                mutex.unlock();
            }
            return true;
        }
        return false;
    }

    /**
     * Try to pair/unpair a local Writer against all possible readerProxy Data.
     *
     * @param W Reference to the Writer
     * @return True
     */
    public boolean pairingWriter(RTPSWriter W) {
        WriterProxyData wdata = this.m_PDP.lookupWriterProxyData(W.getGuid());
        if (wdata != null) {
            logger.debug("Pairing Writer {} in topic {}", W.getGuid(), wdata.getTopicName());
            final Lock mutex = m_PDP.getMutex();
            mutex.lock();
            try {
                for (ParticipantProxyData pit : m_PDP.getParticipantProxies()) {
                    final Lock mutex1 = pit.getMutex();
                    mutex1.lock();
                    try {
                        for (ReaderProxyData rdatait : pit.getReaders()) {
                            if (validMatching(wdata, rdatait)) {
                                //std::cout << "VALID MATCHING to " <<(*rdatait).m_guid<< std::endl;
                                logger.debug("Valid Matching to readerProxy {}", rdatait.getGUID());
                                if (W.matchedReaderAdd(rdatait.toRemoteReaderAttributes())) {
                                    //MATCHED AND ADDED CORRECTLY:
                                    if (W.getListener() != null) {
                                        MatchingInfo info = new MatchingInfo(MatchingStatus.MATCHED_MATHING, rdatait.getGUID());
                                        W.getListener().onWriterMatched(W, info);
                                    }
                                }
                            } else {
                                //logger.info(RTPS_EDP,RTPS_CYAN<<"Valid Matching to writerProxy: "<<(*wdatait).m_guid<<RTPS_DEF<<endl);
                                if (W.matchedReaderIsMatched(rdatait.toRemoteReaderAttributes())
                                        && W.matchedReaderRemove(rdatait.toRemoteReaderAttributes())) {
                                    //MATCHED AND ADDED CORRECTLY:
                                    if (W.getListener() != null) {
                                        MatchingInfo info = new MatchingInfo(MatchingStatus.REMOVED_MATCHING, rdatait.getGUID());
                                        W.getListener().onWriterMatched(W, info);
                                    }
                                }
                            }
                        }
                    } finally {
                        mutex1.unlock();
                    }
                }
            } finally {
                mutex.unlock();
            }
            return true;
        }
        return false;
    }

    /**
     * Try to pair/unpair ReaderProxyData.
     *
     * @param rdata Reference to the ReaderProxyData object.
     * @return True.
     */
    public boolean pairingReaderProxy(ReaderProxyData rdata) {
        logger.debug("Pairing Reader Proxy {} in topic: \"{}\"", rdata.getGUID(), rdata.getTopicName());
        final Lock mutex = m_RTPSParticipant.getParticipantMutex();
        mutex.lock();
        try {
            for (RTPSWriter wit : m_RTPSParticipant.getUserWriters()) {
                final Lock mutexW = wit.getMutex();
                mutexW.lock();
                try {
                    WriterProxyData wdata = m_PDP.lookupWriterProxyData(wit.getGuid());
                    if (wdata != null) {
                        if (validMatching(wdata, rdata)) {
                            logger.debug("Valid Matching to local writer {}", wit.getGuid().getEntityId());
                            if (wit.matchedReaderAdd(rdata.toRemoteReaderAttributes())) {
                                //MATCHED AND ADDED CORRECTLY:
                                if (wit.getListener() != null) {
                                    MatchingInfo info = new MatchingInfo(MatchingStatus.MATCHED_MATHING, rdata.getGUID());
                                    wit.getListener().onWriterMatched(wit, info);
                                }
                            }
                        } else {
                            if (wit.matchedReaderIsMatched(rdata.toRemoteReaderAttributes())
                                    && wit.matchedReaderRemove(rdata.toRemoteReaderAttributes())) {
                                //MATCHED AND ADDED CORRECTLY:
                                if (wit.getListener() != null) {
                                    MatchingInfo info = new MatchingInfo(MatchingStatus.REMOVED_MATCHING, rdata.getGUID());
                                    wit.getListener().onWriterMatched(wit, info);
                                }
                            }
                        }
                    }
                } finally {
                    mutexW.unlock();
                }
            }
        } finally {
            mutex.unlock();
        }
        return true;
    }

    /**
     * Try to pair/unpair WriterProxyData.
     *
     * @param wdata Reference to the WriterProxyData.
     * @return True.
     */
    public boolean pairingWriterProxy(WriterProxyData wdata) {
        logger.debug("Pairing Writer Proxy {} in topic: \"{}\"", wdata.getGUID(), wdata.getTopicName());
        final Lock mutex = m_RTPSParticipant.getParticipantMutex();
        mutex.lock();
        try {
            for (RTPSReader rit : m_RTPSParticipant.getUserReaders()) {
                final Lock mutexR = rit.getMutex();
                mutexR.lock();
                 try {
                    ReaderProxyData rdata = m_PDP.lookupReaderProxyData(rit.getGuid());
                    if (rdata != null) {
                        if (validMatching(rdata, wdata)) {
                            logger.debug("Valid Matching to local reader {}", rit.getGuid().getEntityId());
                            if (rit.matchedWriterAdd(wdata.toRemoteWriterAttributes())) {
                                //MATCHED AND ADDED CORRECTLY:
                                if (rit.getListener() != null) {
                                    MatchingInfo info = new MatchingInfo(MatchingStatus.MATCHED_MATHING, wdata.getGUID());
                                    rit.getListener().onReaderMatched(rit, info);
                                }
                            }
                        } else {
                            if (rit.matchedWriterIsMatched(wdata.toRemoteWriterAttributes())
                                    && rit.matchedWriterRemove(wdata.toRemoteWriterAttributes())) {
                                //MATCHED AND ADDED CORRECTLY:
                                if (rit.getListener() != null) {
                                    MatchingInfo info = new MatchingInfo(MatchingStatus.REMOVED_MATCHING, wdata.getGUID());
                                    rit.getListener().onReaderMatched(rit, info);
                                }
                            }
                        }
                    }
                } finally {
                    mutexR.unlock();
                }
            }
        } finally {
            mutex.unlock();
        }
        return true;
    }

    public void destroy() {
        
    }

    

}
