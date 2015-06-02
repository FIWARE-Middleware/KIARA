package org.fiware.kiara.ps.rtps.participant;

public abstract class RTPSParticipantListener {
    
    public abstract void onRTPSParticipantDiscovery(RTPSParticipant participant, RTPSParticipantDiscoveryInfo info); 

}
