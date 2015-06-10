package org.fiware.kiara.ps.rtps;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fiware.kiara.ps.Domain;
import org.fiware.kiara.ps.rtps.attributes.RTPSParticipantAttributes;
import org.fiware.kiara.ps.rtps.attributes.WriterAttributes;
import org.fiware.kiara.ps.rtps.common.LocatorList;
import org.fiware.kiara.ps.rtps.history.WriterHistoryCache;
import org.fiware.kiara.ps.rtps.messages.elements.EntityId;
import org.fiware.kiara.ps.rtps.messages.elements.GUIDPrefix;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipantListener;
import org.fiware.kiara.ps.rtps.utils.IPFinder;
import org.fiware.kiara.ps.rtps.writer.RTPSWriter;
import org.fiware.kiara.ps.rtps.writer.WriterListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RTPSDomain {
    
    private static byte[] vendorId = new byte[] {0x01, 0x0F};
    
    private static int m_maxRTPSParticipantID;
    
    private static List<RTPSParticipant> m_rtpsParticipants;
    
    private static Set<Integer> m_rtpsParticipantsIDs;
    
    private static final Logger logger = LoggerFactory.getLogger(RTPSDomain.class);
    
    public RTPSDomain() {
        m_maxRTPSParticipantID = -1;
        m_rtpsParticipants = new ArrayList<RTPSParticipant>();
        m_rtpsParticipantsIDs = new HashSet<Integer>();
    }
    
    public void stopAll() {
        while(m_rtpsParticipants.size() > 0) {
            RTPSDomain.removeRTPSParticipant(m_rtpsParticipants.get(0));
        }
        logger.info("RTPSParticipants deleted correctly");
    }
    
    public static RTPSParticipant createParticipant(RTPSParticipantAttributes att, RTPSParticipantListener listener) {
        logger.info("Creating RTPSParticipant");
        
        if (att.builtinAtt.leaseDuration.isLowerThan(new Timestamp().timeInfinite()) && att.builtinAtt.leaseDuration.isLowerThan(att.builtinAtt.leaseDurationAnnouncementPeriod)) {
            logger.error("RTPSParticipant Attributes: LeaseDuration should be >= leaseDuration announcement period");
            return null;
        }
        
        if (att.useIPv4ToSend == false && att.useIPv6ToSend == false) {
            logger.error("Use IP4 OR User IP6 to send must be set to true");
            return null;
        }
        
        int ID;
        if (att.participantID < 0) {
            ID = getNewId();
            while (m_rtpsParticipantsIDs.add(ID) == false) {
                ID = getNewId();
            }
        } else {
            ID = att.participantID;
            if (m_rtpsParticipantsIDs.add(ID) == false) {
                logger.error("RTPSParticipant with the same ID already exists");
                return null;
            }
        }
        
        if (!att.defaultUnicastLocatorList.isValid()) {
            logger.error("Default unicast Locator List contains invalid locator0");
            return null;
        }
        
        if (!att.defaultMulticastLocatorList.isValid()) {
            logger.error("Default Multicast Locator List contains invalid Locator");
            return null;
        }
        
        att.participantID = ID;
        
        int pid;
        pid = (int) Thread.currentThread().getId();
        
        GUIDPrefix guidP = new GUIDPrefix(); 
        LocatorList loc = IPFinder.getIPv4Adress();
        if (loc.getLocators().size() > 0) {
            guidP.setValue(0, vendorId[0]);
            guidP.setValue(1, vendorId[1]);
            guidP.setValue(2, loc.begin().getAddress()[14]);
            guidP.setValue(3, loc.begin().getAddress()[15]);
        } else {
            guidP.setValue(0, vendorId[0]);
            guidP.setValue(1, vendorId[1]);
            guidP.setValue(2, (byte) 127);
            guidP.setValue(3, (byte) 1);
        }
        
        byte[] bytesPID = ByteBuffer.allocate(4).putInt(pid).array();
        byte[] bytesID = ByteBuffer.allocate(4).putInt(ID).array();
        for (int i=0; i < 4; ++i) {
            guidP.setValue(4+i, bytesPID[i]);
            guidP.setValue(8+i, bytesID[i]);
        }
        
        RTPSParticipant participant = new RTPSParticipant(att, guidP, listener);
        m_maxRTPSParticipantID = participant.getRTPSParticipantID();
        
        m_rtpsParticipants.add(participant);
        
        return participant;
    }
    
    public static boolean removeRTPSParticipant(RTPSParticipant p) {
        if (p != null) {
            for (RTPSParticipant it : m_rtpsParticipants) {
                if (it.getGUID().getGUIDPrefix().equals(p.getGUID().getGUIDPrefix())) {
                    m_rtpsParticipants.remove(it);
                    return true;
                }
            }
        }
        logger.error("RTPSParticipant not valid or not recognized");
        return false;
    }
    
    public RTPSWriter createRTPSWriter(RTPSParticipant p, WriterAttributes watt, WriterHistoryCache history, WriterListener listener) {
        for (RTPSParticipant it : m_rtpsParticipants) {
            if (it.getGUID().getGUIDPrefix().equals(p.getGUID().getGUIDPrefix())) {
                RTPSWriter writer = it.createWriter(watt, history, listener, new EntityId(), false);
                if (writer != null) {
                    return writer;
                }
                return null;
            }
        }
        return null;
    }
    
    public boolean removeRTPSWriter(RTPSWriter writer) {
        if (writer != null) {
            for (RTPSParticipant it : this.m_rtpsParticipants) {
                if (it.getGUID().getGUIDPrefix().equals(writer.getGuid().getGUIDPrefix())) {
                    
                }
            }
        }
        return false;
    }
    
    private static int getNewId() {
        return ++m_maxRTPSParticipantID;
    }

}
