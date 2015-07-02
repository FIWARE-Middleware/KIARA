package org.fiware.kiara.ps.rtps.reader.timedevent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.fiware.kiara.ps.publisher.WriterProxy;
import org.fiware.kiara.ps.rtps.common.ChangeFromWriter;
import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.messages.RTPSMessage;
import org.fiware.kiara.ps.rtps.messages.RTPSMessageBuilder;
import org.fiware.kiara.ps.rtps.messages.common.types.RTPSEndian;
import org.fiware.kiara.ps.rtps.messages.elements.Count;
import org.fiware.kiara.ps.rtps.messages.elements.SequenceNumberSet;
import org.fiware.kiara.ps.rtps.resources.TimedEvent;
import org.fiware.kiara.ps.rtps.resources.TimedEvent.EventCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeartbeatResponseDelay extends TimedEvent {
    
    public WriterProxy writerProxy;
    
    public RTPSMessage heartbeatResponseMsg;
    
    private static final Logger logger = LoggerFactory.getLogger(HeartbeatResponseDelay.class);
    
    private final Lock m_mutex = new ReentrantLock(true);
    
    public HeartbeatResponseDelay(WriterProxy proxy, double interval) {
        super(interval); // TODO Check what to do with IO_SERVICE equivalent
        this.writerProxy = proxy;
    }
    
    public void event(EventCode code, String msg) {
        
        if (code == EventCode.EVENT_SUCCESS) {
            List<ChangeFromWriter> changeList = new ArrayList<ChangeFromWriter>();
            this.m_mutex.lock();
            try {
                changeList = this.writerProxy.getMissingChanges();
            } finally {
                this.m_mutex.unlock();
            }
            if (!changeList.isEmpty() || this.writerProxy.hearbeatFinalFlag) {
                SequenceNumberSet sns = new SequenceNumberSet();
                if (this.writerProxy.getAvailableChangesMax() == null) { // No changes available
                    logger.error("No available changes");
                }
                sns.getBase().increment();
                
                for (ChangeFromWriter cit : changeList) {
                    if (!sns.add(cit.seqNum)) {
                        logger.warn("Error sending seqNum " + cit.seqNum.toLong() + " with SeqNumSet Base: " + sns.getBase().toLong());
                        break;
                    }
                }
                
                this.writerProxy.acknackCount++;
                logger.info("Sending ACKNACK");
                
                boolean isFinal = false;
                if (sns.isSetEmpty()) {
                    isFinal = true;
                }
                
                RTPSMessage rtpsMessage = RTPSMessageBuilder.createMessage(RTPSEndian.LITTLE_ENDIAN); // TODO Think about default endian
                RTPSMessageBuilder.addHeader(rtpsMessage, this.writerProxy.statefulReader.getGuid().getGUIDPrefix());
                RTPSMessageBuilder.addSubmessageAckNack(
                    rtpsMessage, 
                    this.writerProxy.statefulReader.getGuid().getEntityId(), 
                    this.writerProxy.att.guid.getEntityId(), 
                    sns, 
                    new Count(this.writerProxy.acknackCount), 
                    isFinal
                );
                
                for (Locator lit : this.writerProxy.att.endpoint.unicastLocatorList.getLocators()) {
                    this.writerProxy.statefulReader.getRTPSParticipant().sendSync(rtpsMessage, lit);
                }
                
                for (Locator lit : this.writerProxy.att.endpoint.multicastLocatorList.getLocators()) {
                    this.writerProxy.statefulReader.getRTPSParticipant().sendSync(rtpsMessage, lit);
                }
            }
        } else if (code == EventCode.EVENT_ABORT) {
            logger.info("Response aborted");
            this.stopSemaphorePost();
        } else {
            logger.info("Response message " + msg);
        }
        
    }

}
