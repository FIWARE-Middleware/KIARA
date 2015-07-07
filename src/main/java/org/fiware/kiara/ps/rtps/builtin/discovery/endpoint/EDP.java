package org.fiware.kiara.ps.rtps.builtin.discovery.endpoint;

import java.util.concurrent.locks.Lock;
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
import org.fiware.kiara.ps.rtps.writer.RTPSWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class EDP, base class for Endpoint Discovery Protocols. It contains generic
 * methods used by the two EDP implemented (EDPSimple and EDPStatic), as well as
 * abstract methods definitions required by the specific implementations.
 *
 * @ingroup DISCOVERY_MODULE
 */
public class EDP {

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

    public void initEDP(BuiltinAttributes m_discovery) {
        // TODO Auto-generated method stub
        // FIXME abstract method
    }

    /**
     * Abstract method that assigns remote endpoints when a new
     * RTPSParticipantProxyData is discovered.
     *
     * @param pdata Discovered ParticipantProxyData
     */
    public void assignRemoteEndpoints(ParticipantProxyData pdata) {
        // FIXME abstract method
    }

    /**
     * After a new local ReaderProxyData has been created some processing is
     * needed (depends on the implementation).
     *
     * @param rdata Pointer to the ReaderProxyData object.
     * @return True if correct.
     */
    public boolean processLocalReaderProxyData(ReaderProxyData rdata) {
        // FIXME abstract method
        return false;
    }

    /**
     * After a new local WriterProxyData has been created some processing is
     * needed (depends on the implementation).
     *
     * @param wdata Pointer to the Writer ProxyData object.
     * @return True if correct.
     */
    public boolean processLocalWriterProxyData(WriterProxyData wdata) {
        // FIXME abstract method
        return false;
    }

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

    public boolean removeLocalWriter(RTPSWriter writer) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean removeLocalReader(RTPSReader reader) {
        // TODO Auto-generated method stub
        return false;
    }

    public void removeRemoteEndpoints(ParticipantProxyData pdata) {
        // TODO Auto-generated method stub

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
