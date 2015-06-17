package org.fiware.kiara.ps.rtps.writer.timedevent;

import org.fiware.kiara.ps.rtps.resources.TimedEvent;
import org.fiware.kiara.ps.rtps.writer.RTPSWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnsentChangesNotEmptyEvent extends TimedEvent {
    
    private RTPSWriter m_writer;
    
    private static final Logger logger = LoggerFactory.getLogger(UnsentChangesNotEmptyEvent.class);

    public UnsentChangesNotEmptyEvent(RTPSWriter writer, double milliseconds) {
        super(milliseconds);
        this.m_writer = writer;
    }

    @Override
    public void event(EventCode code, String msg) {
        logger.info("UnsentChangesNotEmptyEvent risen");
        
        if (code == EventCode.EVENT_SUCCESS) {
            this.m_writer.unsentChangesNotEmpty();
        } else if (code == EventCode.EVENT_ABORT) {
            logger.info("UnsentChangesNotEmpty aborted");
            this.stopSemaphorePost();
        } else {
            logger.info("UnsentChangesNotEmpty msg");
        }
        this.stopTimer();
    }

}
