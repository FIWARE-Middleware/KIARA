package org.fiware.kiara.ps.rtps.builtin.liveliness;

import org.fiware.kiara.ps.qos.WriterQos;
import org.fiware.kiara.ps.rtps.builtin.BuiltinProtocols;
import org.fiware.kiara.ps.rtps.builtin.data.ParticipantProxyData;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.writer.RTPSWriter;

public class WLP {

    public WLP(BuiltinProtocols builtinProtocols) {
        // TODO Auto-generated constructor stub
    }

    public void initWL(RTPSParticipant m_participant) {
        // TODO Auto-generated method stub
        
    }

    public boolean addLocalWriter(RTPSWriter writer, WriterQos wqos) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean updateLocalWriter(RTPSWriter writer, WriterQos wqos) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean removeLocalWriter(RTPSWriter writer) {
        // TODO Auto-generated method stub
        return false;
    }

    public void assignRemoteEndpoints(ParticipantProxyData pdata) {
        // TODO Auto-generated method stub
        
    }

    public void removeRemoteEndpoints(ParticipantProxyData pdata) {
        // TODO Auto-generated method stub
        
    }

    public void destroy() {
        // TODO Auto-generated method stub
        
    }

}
