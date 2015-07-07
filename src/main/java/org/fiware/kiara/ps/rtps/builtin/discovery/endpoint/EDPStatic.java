package org.fiware.kiara.ps.rtps.builtin.discovery.endpoint;

import org.fiware.kiara.ps.rtps.builtin.data.ParticipantProxyData;
import org.fiware.kiara.ps.rtps.builtin.discovery.participant.PDPSimple;
import org.fiware.kiara.ps.rtps.messages.elements.EntityId;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;

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

}
