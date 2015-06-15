package org.fiware.kiara.ps.publisher;

import org.fiware.kiara.ps.attributes.PublisherAttributes;
import org.fiware.kiara.ps.participant.Participant;
import org.fiware.kiara.ps.rtps.common.MatchingInfo;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.rtps.writer.RTPSWriter;
import org.fiware.kiara.ps.rtps.writer.WriterListener;
import org.fiware.kiara.ps.topic.TopicDataType;
import org.fiware.kiara.ps.topic.TopicDataTypeOld;

public class Publisher {
    
    public class PublisherWriterListener extends WriterListener {

        private Publisher m_publisher;
        
        public PublisherWriterListener(Publisher publisher) {
            this.m_publisher = publisher;
        }

        @Override
        public void onWriterMatcher(RTPSWriter writer, MatchingInfo info) {
            if (this.m_publisher.m_listener != null) {
                this.m_publisher.m_listener.onPublicationMatched(m_userPublisher, info);
            }
        }
        
    }
    
    private Participant m_participant;
    
    private RTPSWriter m_writer;
    
    private TopicDataType m_type;
    
    private PublisherAttributes m_att;
    
    private PublisherHistory m_history;
    
    private PublisherListener m_listener;
    
    private Publisher m_userPublisher;
    
    private RTPSParticipant m_rtpsParticipant;
    
    private PublisherWriterListener m_writerListener;
    
    public Publisher(Participant participant, TopicDataType dataType, PublisherAttributes att, PublisherListener listener) {
        this.m_participant = participant;
        this.m_writer = null;
        this.m_type = dataType;
        this.m_att = att;
        this.m_writerListener = new PublisherWriterListener(this);
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
    
    public PublisherHistory getHistory() {
        return this.m_history;
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

    public WriterListener getWriterListener() {
        return this.m_writerListener;
    }

    public void setWriter(RTPSWriter writer) {
        this.m_writer = writer;
    }

}
