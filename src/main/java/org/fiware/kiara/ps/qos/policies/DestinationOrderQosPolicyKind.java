package org.fiware.kiara.ps.qos.policies;

public enum DestinationOrderQosPolicyKind {
    BY_RECEPTION_TIMESTAMP_DESTINATIONORDER_QOS((byte) 0), //!< By Reception Timestamp, default value.
    BY_SOURCE_TIMESTAMP_DESTINATIONORDER_QOS((byte) 1); //!< By Source Timestamp.
    
    private byte m_value;
    
    private DestinationOrderQosPolicyKind(byte value) {
        this.m_value = value;
    }
    
    public byte getValue() {
        return this.m_value;
    }
}
