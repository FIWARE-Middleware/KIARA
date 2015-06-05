package org.fiware.kiara.ps.qos.policies;

public enum ReliabilityQosPolicyKind {
    
    BEST_EFFORT_RELIABILITY_QOS((byte) 0),
    RELIABLE_RELIABILITY_QOS((byte) 1);
    
    private byte m_value;
    
    private ReliabilityQosPolicyKind(byte value) {
        this.m_value = value;
    }
    
    public byte getValue() {
        return this.m_value;
    }

}
