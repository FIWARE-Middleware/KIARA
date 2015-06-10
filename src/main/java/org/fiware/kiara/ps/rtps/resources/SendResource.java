package org.fiware.kiara.ps.rtps.resources;

import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.messages.RTPSMessage;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;

public class SendResource {
    
    public boolean initSend(RTPSParticipant participant, Locator loc, int sendSockBuffer, boolean useIPv4, boolean useIPv6) {
        // TODO Implement
        return true;
    }

    public Object sendSync(RTPSMessage msg, Locator loc) {
        // TODO Implement
        return null;
    }

    public void looseNextChange() {
        // TODO Implement
        
    }

}
