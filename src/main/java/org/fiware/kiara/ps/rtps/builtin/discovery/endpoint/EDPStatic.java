package org.fiware.kiara.ps.rtps.builtin.discovery.endpoint;

import java.util.concurrent.locks.Lock;
import org.fiware.kiara.ps.rtps.attributes.BuiltinAttributes;
import org.fiware.kiara.ps.rtps.builtin.data.ParticipantProxyData;
import org.fiware.kiara.ps.rtps.builtin.data.ReaderProxyData;
import org.fiware.kiara.ps.rtps.builtin.data.WriterProxyData;
import org.fiware.kiara.ps.rtps.builtin.discovery.participant.PDPSimple;
import org.fiware.kiara.ps.rtps.messages.elements.EntityId;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.reader.RTPSReader;
import org.fiware.kiara.ps.rtps.writer.RTPSWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EDPStatic extends EDP {

    private static final Logger logger = LoggerFactory.getLogger(EDPStatic.class);

    /**
     * Constructor.
     *
     * @param p Pointer to the PDPSimple
     * @param part Pointer to the RTPSParticipantImpl
     */
    public EDPStatic(PDPSimple p, RTPSParticipant part) {
        super(p, part);
    }

    public boolean newRemoteWriter(ParticipantProxyData pdata, short userId, EntityId entId) {
        return true;
    }

    public boolean newRemoteReader(ParticipantProxyData pdata, short userId, EntityId entId) {
        return true;
    }

    @Override
    public boolean initEDP(BuiltinAttributes m_discovery) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void assignRemoteEndpoints(ParticipantProxyData pdata) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeLocalReader(RTPSReader R) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeLocalWriter(RTPSWriter W) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean processLocalReaderProxyData(ReaderProxyData rdata) {
        logger.info("RTPS EDP: {} in topic: {}", rdata.getGUID().getEntityId(), rdata.getTopicName());
        //Add the property list entry to our local pdp
        ParticipantProxyData localpdata = m_PDP.getLocalParticipantProxyData();
        final Lock mutex = localpdata.getMutex();
        mutex.lock();
        try {
            localpdata.getProperties().getProperties().add(EDPStaticProperty.toProperty("Reader", "ALIVE", rdata.getUserDefinedId(), rdata.getGUID().getEntityId()));
            localpdata.setHasChanged(true);
            m_PDP.announceParticipantState(true);
        } finally {
            mutex.unlock();
        }
        return true;
    }

    @Override
    public boolean processLocalWriterProxyData(WriterProxyData wdata) {
        logger.info("RTPS EDP: {} in topic: {}", wdata.getGUID().getEntityId(), wdata.getTopicName());
        //Add the property list entry to our local pdp
        ParticipantProxyData localpdata = m_PDP.getLocalParticipantProxyData();
        final Lock mutex = localpdata.getMutex();
        mutex.lock();
        try {
            localpdata.getProperties().getProperties().add(EDPStaticProperty.toProperty("Writer", "ALIVE",
                    wdata.getUserDefinedId(), wdata.getGUID().getEntityId()));
            localpdata.setHasChanged(true);
            m_PDP.announceParticipantState(true);
        } finally {
            mutex.unlock();
        }
        return true;
    }

}
