package org.fiware.kiara.ps.attributes;

import org.fiware.kiara.ps.rtps.attributes.RTPSParticipantAttributes;

public class ParticipantAttributes {
    
    public RTPSParticipantAttributes rtps;
    
    public ParticipantAttributes() {
        this.rtps = new RTPSParticipantAttributes();
    }

}
