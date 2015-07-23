package org.fiware.kiara.ps.common;

import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipantDiscoveryInfo;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipantListener;

public class RTPSPartListener extends RTPSParticipantListener {

    @Override
    public void onRTPSParticipantDiscovery(RTPSParticipant participant,
            RTPSParticipantDiscoveryInfo info) {
        System.out.println("+++++++++ DISCOVERED +++++++++++++");
    }

}
