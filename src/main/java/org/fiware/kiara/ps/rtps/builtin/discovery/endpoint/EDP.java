package org.fiware.kiara.ps.rtps.builtin.discovery.endpoint;

import org.fiware.kiara.ps.attributes.TopicAttributes;
import org.fiware.kiara.ps.qos.ReaderQos;
import org.fiware.kiara.ps.qos.WriterQos;
import org.fiware.kiara.ps.rtps.attributes.BuiltinAttributes;
import org.fiware.kiara.ps.rtps.builtin.data.ParticipantProxyData;
import org.fiware.kiara.ps.rtps.builtin.data.ReaderProxyData;
import org.fiware.kiara.ps.rtps.builtin.data.WriterProxyData;
import org.fiware.kiara.ps.rtps.builtin.discovery.participant.PDPSimple;
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
     * Pointer to the PDPSimple object that contains the endpoint discovery protocol.
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
     * Abstract method that assigns remote endpoints when a new RTPSParticipantProxyData is discovered.
     * @param pdata Discovered ParticipantProxyData
     */
    public void assignRemoteEndpoints(ParticipantProxyData pdata) {
        // FIXME abstract method
    }

    /**
     * After a new local ReaderProxyData has been created some processing is needed (depends on the implementation).
     * @param rdata Pointer to the ReaderProxyData object.
     * @return True if correct.
     */
    public boolean processLocalReaderProxyData(ReaderProxyData rdata) {
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
            TopicAttributes topicAtt, WriterQos wqos) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean updatedLocalWriter(RTPSWriter writer, WriterQos wqos) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean updatedLocalReader(RTPSReader reader, ReaderQos rqos) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean removeLocalWriter(RTPSWriter writer) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean removeLocalReader(RTPSReader reader) {
        // TODO Auto-generated method stub
        return false;
    }

    public void unpairReaderProxy(ReaderProxyData rit) {
        // TODO Auto-generated method stub

    }

    public void unpairWriterProxy(WriterProxyData wit) {
        // TODO Auto-generated method stub

    }

    public void removeRemoteEndpoints(ParticipantProxyData pdata) {
        // TODO Auto-generated method stub

    }

    public boolean pairingReaderProxy(ReaderProxyData rdata) {
        // TODO implement
        return false;
    }

    public boolean pairingReader(RTPSReader R) {
        // TODO implement
        return false;
    }

}
