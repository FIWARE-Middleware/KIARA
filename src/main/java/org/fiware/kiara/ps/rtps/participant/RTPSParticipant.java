package org.fiware.kiara.ps.rtps.participant;

import org.fiware.kiara.ps.rtps.attributes.ParticipantAttributes;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.GUIDPrefix;

public class RTPSParticipant {
    
    private GUID m_guid;
    
    //private BuiltinProtocols m_builtinProtocols;
    
    public RTPSParticipant(
            ParticipantAttributes participantAtt, 
            GUIDPrefix guidPrefix,
            RTPSParticipantListener partListener
            ) {
        this.m_guid = new GUID();
        this.m_guid.setGUIDPrefix(guidPrefix);
        //this.m_builtinProtocols = null;
        
    }
    
    public GUID getGUID() {
        return this.m_guid;
    }
    
    public void setGUID(GUID guid) {
        this.m_guid = guid;
    }

}
