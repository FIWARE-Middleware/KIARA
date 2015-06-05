package org.fiware.kiara.ps.qos.policies;

import java.io.IOException;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class LifespanQosPolicy extends Parameter {

    // TODO
    
    public QosPolicy parent;
    
    public Timestamp duration;

    public LifespanQosPolicy() {
        super(ParameterId.PID_LIFESPAN, Parameter.PARAMETER_TIME_LENGTH);
        this.parent = new QosPolicy(true);
        this.duration = new Timestamp().timeInfinite();
    }

    @Override
    public void deserializeContent(SerializerImpl impl,
            BinaryInputStream message, String name) throws IOException {
        // Do nothing
        
    }
    

}
