package org.fiware.kiara.ps.qos.policies;

import java.io.IOException;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class OwnershipQosPolicy extends Parameter {

    // TODO
    
    public QosPolicy parent;
    
    public OwnershipQosPolicyKind kind;

    public OwnershipQosPolicy() {
        super(ParameterId.PID_OWNERSHIP, Parameter.PARAMETER_KIND_LENGTH);
        this.parent = new QosPolicy(true);
        this.kind = OwnershipQosPolicyKind.SHARED_OWNERSHIP_QOS;
    }

    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        // Do nothing
        
    }

}
