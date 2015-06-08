package org.fiware.kiara.ps.publisher;

import org.fiware.kiara.ps.attributes.PublisherAttributes;
import org.fiware.kiara.ps.participant.Participant;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.writer.RTPSWriter;
import org.fiware.kiara.ps.topic.TopicDataType;

public class Publisher {
    
    private Participant m_participant;
    
    private RTPSWriter m_writer;
    
    private TopicDataType m_type;
    
    private PublisherAttributes m_att;
    
    private PublisherHistory m_history;
    
    private PublisherListener m_listener;
    
    private Publisher m_userPublisher;
    
    private RTPSParticipant m_rtpsParticipant;
    
    public Publisher(Participant participant, TopicDataType dataType, PublisherAttributes att, PublisherListener listener) {
        this.m_participant = participant;
        this.m_writer = null;
        this.m_type = dataType;
        this.m_att = att;
        //this.m_history = new PublisherHistory(); // TODO Implement constructor
    }
    
    /*public boolean write(Object data) {
        
    }
    
    private boolean createNewChange(ChangeKind kind, Object data) {
        
    }
    
    public boolean dispose(Object data) {
        
    }
    
    public boolean unregister(Object data) {
        
    }
    
    public boolean disposeAndUnregister(Object data) {
        
    }
    
    public int removeAllChanges() {
        
    }*/
    
    public PublisherAttributes getAttributes() {
        return this.m_att;
    }
    
    public GUID getGuid() {
        return this.m_writer.getGuid();
    }
    
    public RTPSParticipant getRTPSParticipant() {
        return this.m_rtpsParticipant;
    }
    
    public void setRTPSParticipant(RTPSParticipant participant) {
        this.m_rtpsParticipant = participant;
    }

}
