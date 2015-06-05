package org.fiware.kiara.ps.qos.policies;

import java.io.IOException;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class LivelinessQosPolicy extends Parameter {
    
    public QosPolicy parent;
    
    public LivelinessQosPolicyKind kind;
    
    public Timestamp leaseDuration;
    
    public Timestamp announcementPeriod;

    public LivelinessQosPolicy() {
        super(ParameterId.PID_LIVELINESS, (short) (Parameter.PARAMETER_KIND_LENGTH + Parameter.PARAMETER_KIND_LENGTH));
        this.parent = new QosPolicy(true);
        this.kind = LivelinessQosPolicyKind.AUTOMATIC_LIVELINESS_QOS;
        this.leaseDuration = new Timestamp().timeInfinite();
        this.announcementPeriod = new Timestamp().timeInfinite();
    }
    // TODO

    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        // Do nothing
    }
}
