package org.fiware.kiara.ps.qos.policies;

import java.io.IOException;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class DestinationOrderQosPolicy extends Parameter {
    
    // TODO
    
    public QosPolicy parent;
    
    public DestinationOrderQosPolicyKind kind;
    
    public DestinationOrderQosPolicy() {
        super(ParameterId.PID_DESTINATION_ORDER, Parameter.PARAMETER_KIND_LENGTH);
        this.parent = new QosPolicy(true);
        this.kind = DestinationOrderQosPolicyKind.BY_RECEPTION_TIMESTAMP_DESTINATIONORDER_QOS;
    }

    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        // Do nothing
        
    }

}
