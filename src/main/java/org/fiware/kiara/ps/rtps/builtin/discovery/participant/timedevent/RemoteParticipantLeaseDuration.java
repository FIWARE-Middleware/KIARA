package org.fiware.kiara.ps.rtps.builtin.discovery.participant.timedevent;

import org.fiware.kiara.ps.rtps.builtin.data.ParticipantProxyData;
import org.fiware.kiara.ps.rtps.builtin.discovery.participant.PDPSimple;
import org.fiware.kiara.ps.rtps.resources.TimedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class RemoteRTPSParticipantLeaseDuration, TimedEvent designed to remove a
 * remote RTPSParticipant and all its Readers and Writers from the local
 * RTPSParticipant if it fails to announce its liveliness each leaseDuration
 * period.
 */
public class RemoteParticipantLeaseDuration extends TimedEvent {

    /**
     * Reference to the PDPSimple object.
     */
    private PDPSimple m_PDP;

    /**
     * Reference to the RTPSParticipantProxyData object that contains this
     * temporal event.
     */
    private ParticipantProxyData m_participantProxyData;

    /**
     * Logging object
     */
    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(RemoteParticipantLeaseDuration.class);

    /**
     * Default {@link RemoteParticipantLeaseDuration} constructor
     * 
     * @param PDP The {@link PDPSimple} object who uses the {@link RemoteParticipantLeaseDuration}
     * @param pData The {@link ParticipantProxyData}
     * @param interval Period of time
     */
    public RemoteParticipantLeaseDuration(PDPSimple PDP, ParticipantProxyData pData, double interval) {
        super(interval);
        this.m_PDP = PDP;
        this.m_participantProxyData = pData;
    }

    /**
     * Stops the timer 
     */
    public void delete() {
        this.stopTimer();
    }

    /**
     * Temporal event that check if the RTPSParticipant is alive, and removes it
     * if not.
     *
     * @param code Code representing the status of the event
     * @param msg Message associated to the event
     */
    @Override
    public void event(EventCode code, String msg) {
        if (code == EventCode.EVENT_SUCCESS) {
            logger.debug("Checking RTPSParticipant: " + this.m_participantProxyData.getParticipantName() + " with GUID: " + this.m_participantProxyData.getGUID().getGUIDPrefix());
            if (this.m_participantProxyData.getIsAlive()) {
                logger.debug("RTPSParticipant {} is still ALIVE", this.m_participantProxyData.getGUID());
                this.m_participantProxyData.setIsAlive(false);
            } else {
                logger.debug("RTPSParticipant no longer ALIVE, trying to remove: {} ", this.m_participantProxyData.getGUID());
                this.m_PDP.removeRemoteParticipant(this.m_participantProxyData.getGUID());
                this.stopTimer();
            }
            //this.restartTimer();
        } else if (code == EventCode.EVENT_ABORT) {
            logger.info("Stopped for {} with ID: {}", this.m_participantProxyData.getParticipantName(), this.m_participantProxyData.getGUID().getGUIDPrefix());
        } else {
            logger.info("MSG: {}", msg);
        }
    }

    /**
     * Get the {@link PDPSimple} reference
     * 
     * @return The {@link PDPSimple} reference
     */
    public PDPSimple getPDP() {
        return m_PDP;
    }

    /**
     * Get the {@link ParticipantProxyData} reference
     * 
     * @return The {@link ParticipantProxyData} reference
     */
    public ParticipantProxyData getParticipantProxyData() {
        return m_participantProxyData;
    }

}
