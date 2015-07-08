package org.fiware.kiara.ps.rtps.builtin.discovery.endpoint;

import org.fiware.kiara.ps.rtps.attributes.BuiltinAttributes;
import org.fiware.kiara.ps.rtps.builtin.data.ParticipantProxyData;
import org.fiware.kiara.ps.rtps.builtin.data.ReaderProxyData;
import org.fiware.kiara.ps.rtps.builtin.data.WriterProxyData;
import org.fiware.kiara.ps.rtps.builtin.discovery.participant.PDPSimple;
import org.fiware.kiara.ps.rtps.messages.elements.EntityId;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.reader.RTPSReader;
import org.fiware.kiara.ps.rtps.writer.RTPSWriter;

public class EDPStatic extends EDP {

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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean processLocalWriterProxyData(WriterProxyData wdata) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
