package org.fiware.kiara.ps.participant;

public abstract class ParticipantListener {
    
    public abstract void onParticipantDiscovery(Participant p, ParticipantDiscoveryInfo info);
    
}
