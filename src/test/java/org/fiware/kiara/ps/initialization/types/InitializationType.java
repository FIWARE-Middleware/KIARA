package org.fiware.kiara.ps.initialization.types;

import org.fiware.kiara.ps.rtps.messages.elements.SerializedPayload;
import org.fiware.kiara.ps.topic.SerializableDataType;

public class InitializationType extends SerializableDataType<Initialization> {

    public InitializationType() {
        super(Initialization.class, "Initialization", SerializedPayload.PAYLOAD_MAX_SIZE, true);
    }

    @Override
    public Initialization createData() {
        return new Initialization();
    }
    
    public void setTypeSizeToZero() {
        this.m_typeSize = 0;
    }
    
    public void setTypeSizeToMax() {
        this.m_typeSize = 64001;
    }
    
}
