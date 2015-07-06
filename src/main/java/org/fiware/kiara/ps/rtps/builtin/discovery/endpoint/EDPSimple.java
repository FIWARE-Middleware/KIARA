package org.fiware.kiara.ps.rtps.builtin.discovery.endpoint;

import org.fiware.kiara.ps.rtps.builtin.discovery.participant.PDPSimple;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;

public class EDPSimple extends EDP {

    public EDPSimple(PDPSimple pdpSimple, RTPSParticipant m_RTPSParticipant) {
        // FIXME Weird, but original code does not call inherited constructor
        //super(pdpSimple, m_RTPSParticipant);
        super(null, null);
    }

}
