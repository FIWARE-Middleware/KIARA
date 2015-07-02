package org.fiware.kiara.ps.rtps.common;

public enum ChangeFromWriterStatus {
    
    UNKNOWN(0),
    MISSING(1),
    RECEIVED(2),
    LOST(3);
    
    private int m_value;
    
    private ChangeFromWriterStatus(int value) {
        this.m_value = value;
    }

}
