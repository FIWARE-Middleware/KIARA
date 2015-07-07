package org.fiware.kiara.ps.rtps.attributes;

import org.fiware.kiara.ps.rtps.common.EndpointKind;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;

public class RemoteWriterAttributes {
    
    public EndpointAttributes endpoint;
    
    public GUID guid;
    
    public Timestamp livelinessLeaseDuration;
    
    public short ownershipStrength;
    
    public RemoteWriterAttributes() {
        this.endpoint = new EndpointAttributes();
        this.guid = new GUID();
        
        endpoint.endpointKind = EndpointKind.WRITER;
        livelinessLeaseDuration = new Timestamp().timeInfinite();
        this.ownershipStrength = 0;
    }

    public void setGUID(GUID other) {
        this.guid.copy(other);
    }

}
