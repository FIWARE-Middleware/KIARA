package org.fiware.kiara.ps.common;

import org.fiware.kiara.ps.participant.Participant;
import org.fiware.kiara.ps.participant.ParticipantDiscoveryInfo;
import org.fiware.kiara.ps.participant.ParticipantListener;

public class PartListener extends ParticipantListener {

    @Override
    public void onParticipantDiscovery(Participant p,
            ParticipantDiscoveryInfo info) {
        System.out.println("--------------- MATCHED --------------");
        
    }

}
