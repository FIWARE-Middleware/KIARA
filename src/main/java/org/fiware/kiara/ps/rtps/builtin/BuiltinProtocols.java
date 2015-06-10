package org.fiware.kiara.ps.rtps.builtin;

import org.fiware.kiara.ps.attributes.TopicAttributes;
import org.fiware.kiara.ps.qos.ReaderQos;
import org.fiware.kiara.ps.qos.WriterQos;
import org.fiware.kiara.ps.rtps.attributes.BuiltinAttributes;
import org.fiware.kiara.ps.rtps.builtin.discovery.participant.PDPSimple;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.reader.RTPSReader;
import org.fiware.kiara.ps.rtps.writer.RTPSWriter;

public class BuiltinProtocols {

    // TODO Implement
    
    private PDPSimple m_PDP;
    
    public BuiltinProtocols() {
        this.m_PDP = null;
    }

    public boolean initBuiltinProtocols(RTPSParticipant rtpsParticipant, BuiltinAttributes builtinAtt) {
        // TODO Implement
        return false;
    }

    public boolean addLocalWriter(RTPSWriter writer, TopicAttributes topicAtt, WriterQos wqos) {
        // TODO Implement
        return false;
    }

    public boolean addLocalReader(RTPSReader reader, TopicAttributes topicAtt, ReaderQos rqos) {
        // TODO Implement
        return false;
    }

    public boolean updateLocalWriter(RTPSWriter writer, WriterQos wqos) {
        // TODO Implement
        return false;
    }

    public boolean updateLocalReader(RTPSReader reader, ReaderQos rqos) {
        // TODO Implement
        return false;
    }

    public void removeLocalWriter(RTPSWriter endpoint) {
        // TODO Implement
        
    }

    public void removeLocalReader(RTPSReader endpoint) {
        // TODO Implement
        
    }

    public void announceRTPSParticipantState() {
        // TODO Implement
        
    }

    public void stopRTPSParticipantAnnouncement() {
        // TODO Implement
        
    }

    public void resetRTPSParticipantAnnouncement() {
        // TODO Implement
        
    }
    
    public PDPSimple getPDP() {
        return this.m_PDP;
    }
    
    public void setPDP(PDPSimple PDP) {
        this.m_PDP = PDP;
    }
    

}
