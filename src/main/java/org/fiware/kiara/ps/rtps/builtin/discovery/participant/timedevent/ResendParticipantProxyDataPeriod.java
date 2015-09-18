package org.fiware.kiara.ps.rtps.builtin.discovery.participant.timedevent;

import org.fiware.kiara.ps.rtps.builtin.discovery.participant.PDPSimple;
import org.fiware.kiara.ps.rtps.messages.RTPSMessage;
import org.fiware.kiara.ps.rtps.resources.TimedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class ResendParticipantProxyDataPeriod, TimedEvent used to periodically send
 * the RTPSParticipantDiscovery Data.
 */
public class ResendParticipantProxyDataPeriod extends TimedEvent {

    /** Auxiliary data message. */
    private RTPSMessage m_dataMsg;

    /** Reference to the PDPSimple object. */
    private PDPSimple m_PDP;

    private static final Logger logger = LoggerFactory.getLogger(ResendParticipantProxyDataPeriod.class);

    /**
     * Constructor.
     *
     * @param PDP Reference to the PDPSimple.
     * @param milliseconds Interval in ms.
     */
    public ResendParticipantProxyDataPeriod(PDPSimple PDP, double milliseconds) {
        super(milliseconds);
        this.m_PDP = PDP;
    }

    public void delete() {
        this.stopTimer();
    }

    /**
     * Method invoked when the event occurs. This temporal event resends the
     * RTPSParticipantProxyData to all remote RTPSParticipants.
     *
     * @param code Code representing the status of the event
     * @param msg Message associated to the event
     */
    @Override
    public void event(EventCode code, String msg) {
        if (code == EventCode.EVENT_SUCCESS) {
            logger.debug("Resend Discovery Data ");
            this.m_PDP.getLocalParticipantProxyData().increaseManualLivelinessCount();
            this.m_PDP.announceParticipantState(false);
        } else if (code == EventCode.EVENT_ABORT) {
            logger.debug("Response Data aborted");
            this.stopSemaphorePost();
        } else {
            logger.debug("MSG: {}", msg);
        }
    }

}
