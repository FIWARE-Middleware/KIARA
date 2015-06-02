package org.fiware.kiara.ps.rtps.participant;

import java.util.List;
import java.util.Map.Entry;

import org.fiware.kiara.ps.rtps.messages.elements.GUID;

public class RTPSParticipantDiscoveryInfo {
    
    public DiscoveryStatus status;
    
    public GUID guid;
    
    public List<Entry<String, String>> propertyList;
    
    public List<Byte> userData;
    
    public String RTPSParticipantName;
    

}
