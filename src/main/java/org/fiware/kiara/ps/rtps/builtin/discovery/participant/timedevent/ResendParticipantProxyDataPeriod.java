package org.fiware.kiara.ps.rtps.builtin.discovery.participant.timedevent;

import org.fiware.kiara.ps.rtps.builtin.discovery.participant.PDPSimple;
import org.fiware.kiara.ps.rtps.messages.RTPSMessage;
import org.fiware.kiara.ps.rtps.resources.TimedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResendParticipantProxyDataPeriod extends TimedEvent {
    
    private RTPSMessage m_dataMsg;
    
    private PDPSimple m_PDP;
    
    private static final Logger logger = LoggerFactory.getLogger(ResendParticipantProxyDataPeriod.class);

    public ResendParticipantProxyDataPeriod(PDPSimple PDP, double milliseconds) {
        super(milliseconds);
        this.m_PDP = PDP;
    }
    
    public void delete() {
        this.stopTimer();
    }

    @Override
    public void event(EventCode code, String msg) {
        if (code == EventCode.EVENT_SUCCESS) {
            logger.info("Resend Discovery Data ");
            this.m_PDP.getLocalParticipantProxyData().increaseManualLivelinessCount();
            this.m_PDP.announceParticipantState(false);
        } else if (code == EventCode.EVENT_ABORT) {
            logger.info("Response Data aborted");
            this.stopSemaphorePost();
        } else {
            logger.info("MSG: " + msg);
        }
    }

}
