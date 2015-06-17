package org.fiware.kiara.ps.rtps.attributes;

import org.fiware.kiara.ps.rtps.common.EndpointKind;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;

public class RemoteReaderAttributes {
    
    public EndpointAttributes endpoint;
    
    public GUID guid;
    
    public boolean expectsInlineQos;
    
    public RemoteReaderAttributes() {
        this.endpoint = new EndpointAttributes();
        this.guid = new GUID();
        this.endpoint.endpointKind = EndpointKind.READER;
        this.expectsInlineQos = false;
    }

}
