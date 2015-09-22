package org.fiware.kiara.ps.rtps.writer.timedevent;

import java.util.concurrent.locks.Lock;

import org.fiware.kiara.ps.rtps.common.ChangeForReader;
import org.fiware.kiara.ps.rtps.common.ChangeForReaderStatus;
import org.fiware.kiara.ps.rtps.common.ReliabilityKind;
import org.fiware.kiara.ps.rtps.resources.TimedEvent;
import org.fiware.kiara.ps.rtps.writer.ReaderProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NackSupressionDuration extends TimedEvent {
    
    private ReaderProxy m_RP;
    
    private static final Logger logger = LoggerFactory.getLogger(NackSupressionDuration.class);

    public NackSupressionDuration(ReaderProxy RP, double milliseconds) {
        super(milliseconds);
        this.m_RP = RP;
    }

    @Override
    public void event(EventCode code, String msg) {
        if (code == EventCode.EVENT_SUCCESS) {
            
            logger.info("Nack supression event");
            System.out.println("Nack supression event executing");
            Lock guardW = this.m_RP.getSFW().getMutex();
            guardW.lock();
            try {
                System.out.println("ChangesForReader Size: " + this.m_RP.getChangesForReader().size());
                for (ChangeForReader cit : this.m_RP.getChangesForReader()) {
                    if (cit.status == ChangeForReaderStatus.UNDERWAY) {
                        if (this.m_RP.att.endpoint.reliabilityKind == ReliabilityKind.RELIABLE) {
                            System.out.println("Change " + cit.getSequenceNumber().toLong() + " Set to UNACKNOWLEDGED");
                            cit.status = ChangeForReaderStatus.UNACKNOWLEDGED;
                        } else {
                            cit.status = ChangeForReaderStatus.ACKNOWLEDGED;
                        }
                    }
                }
                
                // NEW Added so that the event only executes a single time
                this.stopTimer();
                
            } finally {
                guardW.unlock();
            }
            
        } else if (code == EventCode.EVENT_ABORT) {
            logger.info("Nack supression aborted");
            this.stopSemaphorePost();
        } else {
            logger.info("Nack response message: {}", msg);
        }
    }

}
