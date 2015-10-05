package org.fiware.kiara.ps.rtps.reader.timedevent;

import java.util.concurrent.TimeUnit;

import org.fiware.kiara.ps.rtps.common.MatchingInfo;
import org.fiware.kiara.ps.rtps.common.MatchingStatus;
import org.fiware.kiara.ps.rtps.reader.RTPSReader;
import org.fiware.kiara.ps.rtps.reader.WriterProxy;
import org.fiware.kiara.ps.rtps.resources.TimedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WriterProxyLiveliness extends TimedEvent {

    private static final Logger logger = LoggerFactory.getLogger(WriterProxy.class);

    /**
     * Reference to the WriterProxy associated with this specific event.
     */
    public WriterProxy writerProxy;
    
    private boolean recentlyCreated = true;

    public WriterProxyLiveliness(WriterProxy p_WP, double interval) {
        super(interval);
        writerProxy = p_WP;
    }

    @Override
    public void event(EventCode code, String msg) {
        if (code == EventCode.EVENT_SUCCESS) {
            if (recentlyCreated) {
                recentlyCreated = false;
            } else {
                logger.debug("Deleting writer {}", this.writerProxy.att.guid);
                if (this.writerProxy.statefulReader.matchedWriterRemove(this.writerProxy.att)) {
                    if (this.writerProxy.statefulReader.getListener() != null) {
                        MatchingInfo info = new MatchingInfo(MatchingStatus.REMOVED_MATCHING, this.writerProxy.att.guid);
                        this.writerProxy.statefulReader.getListener().onReaderMatched((RTPSReader) this.writerProxy.statefulReader, info);
                    }
                }
                // Now delete objects
                this.writerProxy.writerProxyLiveliness = null;
                this.writerProxy.destroy();
            }
            //this.stopTimer();
        } else if (code == EventCode.EVENT_ABORT) {
            this.stopSemaphorePost();
        } else {
            logger.debug("Message: {}", msg);
        }
    }
    
    @Override
    public void restartTimer() {
        this.recentlyCreated = true;
        super.restartTimer();
    }

}
