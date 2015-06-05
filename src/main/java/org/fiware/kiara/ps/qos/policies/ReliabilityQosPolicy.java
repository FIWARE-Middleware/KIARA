package org.fiware.kiara.ps.qos.policies;

import java.io.IOException;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class ReliabilityQosPolicy extends Parameter {
    
    // TODO
    
    public QosPolicy parent;
    
    public ReliabilityQosPolicyKind kind;
    
    public Timestamp maxBlockingTime;
    
    public ReliabilityQosPolicy() {
        super(ParameterId.PID_RELIABILITY, (short) (Parameter.PARAMETER_KIND_LENGTH + Parameter.PARAMETER_KIND_LENGTH));
        this.parent = new QosPolicy(true);
        this.kind = ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS;
        this.maxBlockingTime = new Timestamp();
    }

    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        // Do nothing
    }
}
