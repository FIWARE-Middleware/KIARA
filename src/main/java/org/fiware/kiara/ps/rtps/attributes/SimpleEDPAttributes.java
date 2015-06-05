package org.fiware.kiara.ps.rtps.attributes;

public class SimpleEDPAttributes {
    
    public boolean usePulicationWriterAndSubscriptionReader;
    
    public boolean usePulicationReaderAndSubscriptionWriter;
    
    public SimpleEDPAttributes() {
        this.usePulicationReaderAndSubscriptionWriter = true;
        this.usePulicationWriterAndSubscriptionReader = true;
    }

}
