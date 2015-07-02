package org.fiware.kiara.ps.rtps.reader.timedevent;

import org.fiware.kiara.ps.publisher.WriterProxy;
import org.fiware.kiara.ps.rtps.resources.TimedEvent;

public class WriterProxyLiveliness extends TimedEvent {
    
    // TODO Implement
    
    public WriterProxy writerProxy;

    public WriterProxyLiveliness(EventCode code, double interval) {
        super(interval);
        
    }

    @Override
    public void event(EventCode code, String msg) {
        // TODO Auto-generated method stub
        
    }

}
