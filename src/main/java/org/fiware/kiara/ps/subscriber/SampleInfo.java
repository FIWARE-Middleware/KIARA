package org.fiware.kiara.ps.subscriber;

import org.fiware.kiara.ps.rtps.messages.common.types.ChangeKind;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;

public class SampleInfo {
    
    public ChangeKind sampleKind;
    
    public GUID writerGUID;
    
    public short ownershipStrength;
    
    public Timestamp sourceTimestamp;
    
    public InstanceHandle handle;
    
    public SampleInfo() {
        this.sampleKind = ChangeKind.ALIVE;
        this.writerGUID = new GUID();
        this.ownershipStrength = 0;
        this.sourceTimestamp = new Timestamp();
        this.handle = new InstanceHandle();
    }

}
