package org.fiware.kiara.ps.rtps.builtin.discovery.endpoint;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import org.fiware.kiara.ps.attributes.TopicAttributes;
import org.fiware.kiara.ps.qos.ReaderQos;
import org.fiware.kiara.ps.qos.WriterQos;
import static org.fiware.kiara.ps.qos.policies.DurabilityQosPolicyKind.TRANSIENT_LOCAL_DURABILITY_QOS;
import static org.fiware.kiara.ps.qos.policies.DurabilityQosPolicyKind.VOLATILE_DURABILITY_QOS;
import static org.fiware.kiara.ps.qos.policies.ReliabilityQosPolicyKind.BEST_EFFORT_RELIABILITY_QOS;
import static org.fiware.kiara.ps.qos.policies.ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS;
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
 *
 */
public abstract class EDP {

    /**
     * Pointer to the PDPSimple object that contains the endpoint discovery
     * protocol.
     */
    private PDPSimple m_PDP;
    /**
     * Pointer to the RTPSParticipant.
     */
    private RTPSParticipant m_RTPSParticipant;

    private static final Logger logger = LoggerFactory.getLogger(EDP.class);

    /**
     * Constructor.
     *
     * @param p Pointer to the PDPSimple
     * @param part Pointer to the RTPSParticipantImpl
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
    public abstract boolean initEDP(BuiltinAttributes m_discovery);

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
     * @param pdata Pointer to the ParticipantProxyData to remove
     */
    public void removeRemoteEndpoints(ParticipantProxyData pdata) {
    }

    /**
     * Abstract method that removes a local Reader from the discovery method
     *
     * @param R Pointer to the Reader to remove.
     * @return True if correctly removed.
     */
    public abstract boolean removeLocalReader(RTPSReader R);

    /**
     * Abstract method that removes a local Writer from the discovery method
     *
     * @param W Pointer to the Writer to remove.
     * @return True if correctly removed.
     */
    public abstract boolean removeLocalWriter(RTPSWriter W);

    /**
     * After a new local ReaderProxyData has been created some processing is
     * needed (depends on the implementation).
     *
     * @param rdata Pointer to the ReaderProxyData object.
     * @return True if correct.
     */
    public abstract boolean processLocalReaderProxyData(ReaderProxyData rdata);

    /**
     * After a new local WriterProxyData has been created some processing is
     * needed (depends on the implementation).
     *
     * @param wdata Pointer to the Writer ProxyData object.
     * @return True if correct.
     */
    public abstract boolean processLocalWriterProxyData(WriterProxyData wdata);

    public boolean newLocalReaderProxyData(RTPSReader reader,
            TopicAttributes att, ReaderQos rqos) {

        logger.info("Adding {} in topic {}", reader.getGuid().getEntityId(), att.topicName);

        ReaderProxyData rpd = new ReaderProxyData();
        rpd.setIsAlive(true);
        rpd.setExpectsInlineQos(reader.getExpectsInlineQos());
        rpd.setGUID(reader.getGuid());
        rpd.setKey(rpd.getGUID());
        rpd.setMulticastLocatorList(reader.getAttributes().getMulticastLocatorList());
        rpd.setUnicastLocatorList(reader.getAttributes().getUnicastLocatorList());
        rpd.setRTPSParticipantKey(m_RTPSParticipant.getGuid());
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

    public boolean newLocalWriterProxyData(RTPSWriter writer,
            TopicAttributes att, WriterQos wqos) {
        logger.info("Adding {} in topic {}", writer.getGuid().getEntityId(), att.topicName);

        WriterProxyData wpd = new WriterProxyData();
        wpd.setIsAlive(true);
        wpd.setGUID(writer.getGuid());
        wpd.setKey(wpd.getGUID());
        wpd.setMulticastLocatorList(writer.getAttributes().getMulticastLocatorList());
        wpd.setUnicastLocatorList(writer.getAttributes().getUnicastLocatorList());
        wpd.setRTPSParticipantKey(m_RTPSParticipant.getGuid());
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

    public boolean updatedLocalReader(RTPSReader reader, ReaderQos rqos) {
        ReaderProxyData rdata = this.m_PDP.lookupReaderProxyData(reader.getGuid());
        if (rdata != null) {
            rdata.getQos().setQos(rqos, false);
            rdata.setExpectsInlineQos(reader.getExpectsInlineQos());
            processLocalReaderProxyData(rdata);
            //this->updatedReaderProxy(rdata);
            pairingReaderProxy(rdata);
            pairingReader(reader);
            return true;
        }
        return false;
    }

    public boolean updatedLocalWriter(RTPSWriter writer, WriterQos wqos) {
        WriterProxyData wdata = this.m_PDP.lookupWriterProxyData(writer.getGuid());
        if (wdata != null) {
            wdata.getQos().setQos(wqos, false);
            processLocalWriterProxyData(wdata);
            //this->updatedWriterProxy(wdata);
            pairingWriterProxy(wdata);
            pairingWriter(writer);
            return true;
        }
        return false;
    }

    public boolean removeWriterProxy(GUID writer) {
        logger.info("RTPS EDP {}", writer);
        WriterProxyData wdata = this.m_PDP.lookupWriterProxyData(writer);
        if (wdata != null) {
            logger.info("RTPS_EDP: in topic: {}", wdata.getTopicName());
            unpairWriterProxy(wdata);
            this.m_PDP.removeWriterProxyData(wdata);
            return true;
        }
        return false;
    }

    public boolean removeReaderProxy(GUID reader) {
        logger.info("RTPS EDP {}", reader);
        ReaderProxyData rdata = this.m_PDP.lookupReaderProxyData(reader);
        if (rdata != null) {
            logger.info("RTPS_EDP: in topic: {}", rdata.getTopicName());
            unpairReaderProxy(rdata);
            this.m_PDP.removeReaderProxyData(rdata);
            return true;
        }
        return false;
    }

    public boolean unpairWriterProxy(WriterProxyData wdata) {
        logger.info("RTPS_EDP {}  in topic: {}", wdata.getGUID(), wdata.getTopicName());
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

    public boolean unpairReaderProxy(ReaderProxyData rdata) {
        logger.info("RTPS_EDP {} in topic: {}", rdata.getGUID(), rdata.getTopicName());
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
     * @param wdata Pointer to the WriterProxyData object.
     * @param rdata Pointer to the ReaderProxyData object.
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

    public boolean pairingReaderProxy(ReaderProxyData rdata) {
        // TODO implement
        return false;
    }

    public boolean pairingWriterProxy(WriterProxyData wdata) {
        // TODO implement
        return false;
    }

    public boolean pairingReader(RTPSReader R) {
        // TODO implement
        return false;
    }

    public boolean pairingWriter(RTPSWriter W) {
        // TODO implement
        return false;
    }

}
