package org.fiware.kiara.ps.qos.policies;

public enum PresentationQosPolicyAccessScopeKind {
    
    INSTANCE_PRESENTATION_QOS((byte) 0), //!< Instance Presentation, default value.
    TOPIC_PRESENTATION_QOS((byte) 1), //!< Topic Presentation.
    GROUP_PRESENTATION_QOS((byte) 2);//!< Group Presentation.
    
    private byte m_value;
    
    private PresentationQosPolicyAccessScopeKind(byte value) {
        this.m_value = value;
    }
    
    public byte getValue() {
        return this.m_value;
    }

}
