package org.fiware.kiara.ps.qos.policies;

import java.io.IOException;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class OwnershipStrengthQosPolicy extends Parameter {

    // TODO
    
    public QosPolicy parent;
    
    public int value;

    public OwnershipStrengthQosPolicy() {
        super(ParameterId.PID_OWNERSHIP_STRENGTH, (short) 4);
        this.parent = new QosPolicy(false);
        this.value = 0;
    }

    @Override
    public void deserializeContent(SerializerImpl impl,
            BinaryInputStream message, String name) throws IOException {
        // TODO Auto-generated method stub
        
    }
    
    
}
