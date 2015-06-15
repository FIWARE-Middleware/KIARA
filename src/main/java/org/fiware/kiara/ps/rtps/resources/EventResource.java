package org.fiware.kiara.ps.rtps.resources;

import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;

public class EventResource {
    
    private EventThread m_thread;
    
    private RTPSParticipant m_participant;
    
    public EventResource() {
        
    }

    public void initThread(RTPSParticipant participant) {
        // TODO Implement
        
        /*this.m_participant = participant;
        this.m_thread = new EventThread(this);
        this.m_thread.run();
        this.m_participant.resourceSemaphoreWait();*/
    }
    
    public void announceThread() {
        System.out.println("Executing announceThread");
        this.m_participant.resourceSemaphorePost();
    }
    
    public void startDatagramChannel() {
        // TODO Implement. Equivalent to start_io_service
    }
    
    

}
