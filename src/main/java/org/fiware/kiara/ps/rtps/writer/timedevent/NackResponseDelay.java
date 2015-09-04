package org.fiware.kiara.ps.rtps.writer.timedevent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

import org.fiware.kiara.ps.rtps.common.ChangeForReader;
import org.fiware.kiara.ps.rtps.common.ChangeForReaderStatus;
import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.messages.RTPSMessage;
import org.fiware.kiara.ps.rtps.messages.RTPSMessageBuilder;
import org.fiware.kiara.ps.rtps.messages.RTPSMessageGroup;
import org.fiware.kiara.ps.rtps.messages.common.types.RTPSEndian;
import org.fiware.kiara.ps.rtps.messages.elements.SequenceNumber;
import org.fiware.kiara.ps.rtps.resources.TimedEvent;
import org.fiware.kiara.ps.rtps.writer.ReaderProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NackResponseDelay extends TimedEvent {
    
    private ReaderProxy m_RP;
    
    //private RTPSMessageGroup m_messages;
    
    // Modified from cpp version
    private RTPSMessage m_rtpsMessage;
    
    private static final Logger logger = LoggerFactory.getLogger(NackResponseDelay.class);

    public NackResponseDelay(ReaderProxy RP, double milliseconds) {
        super(milliseconds);
        this.m_RP = RP;
        this.m_rtpsMessage = RTPSMessageBuilder.createMessage(RTPSEndian.LITTLE_ENDIAN); // TODO Think about default endian
        RTPSMessageBuilder.addHeader(this.m_rtpsMessage, this.m_RP.getSFW().getGuid().getGUIDPrefix());
    }

    @Override
    public void event(EventCode code, String msg) {
        // TODO Check if creating an RTPSMessage inside each if block solves sending all the NACK responses
        if (code == EventCode.EVENT_SUCCESS) {
            logger.info("Responding to Acknack msg");
            System.out.println("Responding to Acknack msg");
            Lock guardW = this.m_RP.getSFW().getMutex();
            guardW.lock();
            try {
                Lock guard = this.m_RP.getMutex();
                guard.lock();
                try {
                    List<ChangeForReader> vec = this.m_RP.requestedChanges();
                    if (!vec.isEmpty()) {
                        logger.info("Requested {} changes", vec.size());
                        List<CacheChange> relevantChanges = new ArrayList<CacheChange>();
                        List<SequenceNumber> notRelevantChanges = new ArrayList<SequenceNumber>();
                        for (ChangeForReader cit : vec) {
                            cit.status = ChangeForReaderStatus.UNDERWAY;
                            if (cit.isRelevant && cit.isValid()) { 
                                relevantChanges.add(cit.getChange());
                            } else {
                                notRelevantChanges.add(cit.getSequenceNumber());
                            }
                        }
                        this.m_RP.isRequestedChangesEmpty = true;
                        if (!relevantChanges.isEmpty()) {
                            RTPSMessageGroup.sendChangesAsData(
                                    this.m_RP.getSFW(), 
                                    relevantChanges, 
                                    this.m_RP.att.endpoint.unicastLocatorList, 
                                    this.m_RP.att.endpoint.multicastLocatorList, 
                                    this.m_RP.att.expectsInlineQos, 
                                    this.m_RP.att.guid.getEntityId());
                        }
                        if (!notRelevantChanges.isEmpty()) {
                            RTPSMessageGroup.sendChangesAsGap(
                                    this.m_RP.getSFW(), 
                                    notRelevantChanges, 
                                    this.m_RP.att.guid.getEntityId(),
                                    this.m_RP.att.endpoint.unicastLocatorList, 
                                    this.m_RP.att.endpoint.multicastLocatorList);
                        }
                        if (relevantChanges.isEmpty() && notRelevantChanges.isEmpty()) {
                            RTPSMessage message = RTPSMessageBuilder.createMessage();
                            SequenceNumber first = this.m_RP.getSFW().getSeqNumMin();
                            SequenceNumber last = this.m_RP.getSFW().getSeqNumMin(); // TODO Review if this should be getSeqNumMax()
                            if (!first.isUnknown() && !last.isUnknown() && last.isGreaterOrEqualThan(first)) {
                                this.m_RP.getSFW().incrementHBCount();
                                RTPSMessageBuilder.addSubmessageHeartbeat(
                                        message, 
                                        this.m_RP.att.guid.getEntityId(), 
                                        this.m_RP.getSFW().getGuid().getEntityId(), 
                                        first, 
                                        last, 
                                        this.m_RP.getSFW().getHeartbeatCount(), 
                                        true, 
                                        false);
                            }
                            for (Locator lit : this.m_RP.att.endpoint.unicastLocatorList.getLocators()) {
                                this.m_RP.getSFW().getRTPSParticipant().sendSync(message, lit);
                            }
                            for (Locator lit : this.m_RP.att.endpoint.multicastLocatorList.getLocators()) {
                                this.m_RP.getSFW().getRTPSParticipant().sendSync(message, lit);
                            }
                            
                        }
                    }
                } finally {
                    guard.unlock();
                }
            } finally {
                guardW.unlock();
            }
            
        } else if (code == EventCode.EVENT_ABORT) {
            logger.info("Nack response aborted");
            this.stopSemaphorePost();
        } else {
            logger.info("Nack response message: {}", msg);
        }
    }

}
