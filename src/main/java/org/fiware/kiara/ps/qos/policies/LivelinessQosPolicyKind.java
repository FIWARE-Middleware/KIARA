package org.fiware.kiara.ps.qos.policies;

public enum LivelinessQosPolicyKind {
    AUTOMATIC_LIVELINESS_QOS((byte) 0), //!< Automatic Liveliness, default value.
    MANUAL_BY_PARTICIPANT_LIVELINESS_QOS((byte) 1), //!< MANUAL_BY_PARTICIPANT_LIVELINESS_QOS
    MANUAL_BY_TOPIC_LIVELINESS_QOS((byte) 2); //!< MANUAL_BY_TOPIC_LIVELINESS_QOS
    
    private byte m_value;
    
    private LivelinessQosPolicyKind(byte value) {
        this.m_value = value;
    }
    
    public byte geValue() {
        return this.m_value;
    }
}
