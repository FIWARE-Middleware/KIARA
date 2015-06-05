package org.fiware.kiara.ps.qos.policies;

public enum DurabilityQosPolicyKind {
    
    VOLATILE_DURABILITY_QOS((byte) 0), //!< Volatile Durability (default for Subscribers).
    TRANSIENT_LOCAL_DURABILITY_QOS((byte) 1), //!< Transient Local Durability (default for Publishers).
    TRANSIENT_DURABILITY_QOS((byte) 2), //!< NOT IMPLEMENTED.
    PERSISTENT_DURABILITY_QOS((byte) 3); //!< NOT IMPLEMENTED.
    
    private byte m_value;
    
    private DurabilityQosPolicyKind(byte value) {
        this.m_value = value;
    }
    
    public byte getValue() {
        return this.m_value;
    }

}
