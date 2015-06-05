package org.fiware.kiara.ps.qos.policies;

import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;

public class LatencyBudgetQosPolicy {
    // TODO
    
    public QosPolicy parent;
    
    public Timestamp duration;
    
    public LatencyBudgetQosPolicy() {
        this.parent = new QosPolicy(true);
        this.duration = new Timestamp().timeZero();
    }
    
}
