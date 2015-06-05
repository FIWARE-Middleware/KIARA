package org.fiware.kiara.ps.qos.policies;

public enum OwnershipQosPolicyKind {
    SHARED_OWNERSHIP_QOS ((byte) 0), //!< Shared Ownership, default value.
    EXCLUSIVE_OWNERSHIP_QOS((byte) 1); //!< Exclusive ownership
    
    private byte m_value;
    
    private OwnershipQosPolicyKind(byte value) {
        this.m_value = value;
    }
    
    public byte getValue() {
        return this.m_value;
    }
}
