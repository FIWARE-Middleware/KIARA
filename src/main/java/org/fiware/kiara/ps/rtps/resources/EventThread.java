package org.fiware.kiara.ps.rtps.resources;

public class EventThread implements Runnable {
    
    private EventResource m_eventResource;
    
    public EventThread(EventResource resource) {
        this.m_eventResource = resource;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        System.out.println("EventThread Running");
    }

}
