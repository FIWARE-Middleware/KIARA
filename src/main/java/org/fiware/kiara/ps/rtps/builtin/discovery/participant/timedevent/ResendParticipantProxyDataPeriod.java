package org.fiware.kiara.ps.rtps.builtin.discovery.participant.timedevent;

import org.fiware.kiara.ps.rtps.builtin.discovery.participant.PDPSimple;
import org.fiware.kiara.ps.rtps.resources.TimedEvent;

public class ResendParticipantProxyDataPeriod extends TimedEvent {

    public ResendParticipantProxyDataPeriod(double milliseconds) {
        super(milliseconds);
        // TODO Auto-generated constructor stub
    }

    public ResendParticipantProxyDataPeriod(PDPSimple pdpSimple,
            double milliseconds) {
        super(milliseconds);
    }

    @Override
    public void event(EventCode code, String msg) {
        // TODO Auto-generated method stub
        
    }

}
