package org.fiware.kiara.ps.qos.policies;

import java.io.IOException;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class TimeBasedFilterQosPolicy extends Parameter {
    // TODO

    public QosPolicy parent;
    
    public Timestamp minimumSeparation;
    
    public TimeBasedFilterQosPolicy() {
        super(ParameterId.PID_TIME_BASED_FILTER, Parameter.PARAMETER_KIND_LENGTH);
        this.parent = new QosPolicy(false);
        this.minimumSeparation = new Timestamp().timeZero();
    }

    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        // Do nothing
        
    }
    
    
    
}
