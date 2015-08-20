package org.fiware.kiara.ps.rtps.reader.timedevent;

import org.fiware.kiara.ps.publisher.WriterProxy;
import org.fiware.kiara.ps.rtps.resources.TimedEvent;

public class WriterProxyLiveliness extends TimedEvent {

    // TODO Implement

    /**
     * Pointer to the WriterProxy associated with this specific event.
     */
    public WriterProxy writerProxy;

    public WriterProxyLiveliness(WriterProxy p_WP, double interval) {
        super(interval);
        writerProxy = p_WP;
    }

    @Override
    public void event(EventCode code, String msg) {
        // TODO Auto-generated method stub
        
    }

}
