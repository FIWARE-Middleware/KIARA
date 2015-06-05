package org.fiware.kiara.ps.qos.policies;

import java.io.IOException;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class PresentationQosPolicy extends Parameter {

    // TODO
    
    public QosPolicy parent;
    
    public PresentationQosPolicyAccessScopeKind accessScope;
    
    public boolean coherentAccess;
    
    public boolean orderedAccess;

    public PresentationQosPolicy() {
        super(ParameterId.PID_PRESENTATION, Parameter.PARAMETER_PRESENTATION_LENGTH);
        this.parent = new QosPolicy(false);
        this.accessScope = PresentationQosPolicyAccessScopeKind.INSTANCE_PRESENTATION_QOS;
        this.coherentAccess = false;
        this.orderedAccess = false;
    }

    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        // Do nothing
        
    }
    

}
