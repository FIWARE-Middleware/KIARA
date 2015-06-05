package org.fiware.kiara.ps.qos.policies;

import java.io.IOException;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class DeadLineQosPolicy extends Parameter {

    // TODO
    
    public QosPolicy parent;
    
    public Timestamp period;
    
    public DeadLineQosPolicy() {
        super(ParameterId.PID_DEADLINE, Parameter.PARAMETER_KIND_LENGTH);
        this.parent = new QosPolicy(true);
        this.period = new Timestamp().timeInfinite();
    } 

    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        // Do nothing
        
    }

}
