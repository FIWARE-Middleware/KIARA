package org.fiware.kiara.ps.rtps.builtin.data;

import org.fiware.kiara.ps.rtps.messages.elements.GUID;

public class ReaderProxyData {
    
    private GUID m_guid;
    
    public ReaderProxyData() {
        this.m_guid = new GUID();
    }

    public GUID getGUID() {
        return this.m_guid;
    }

    public void copy(ReaderProxyData rit) {
        // TODO Implement
        
    }

}
