package org.fiware.kiara.ps.rtps.attributes;

import java.util.List;

public class ParticipantAttributes {
    
    public int defaultSendPort;
    
    public int sendSocketBufferSize;
    
    public int listenSocketBufferSize;
    
    public BuiltinAttributes builtinAtt;
    
    public PortParameters portParameters;
    
    public List<Byte> userData;
    
    public int participantID;
    
    boolean useIPv4ToSend;
    
    boolean useIPv6ToSend;
    
    private String m_name;
    
    public void setName(String name) {
        this.m_name = name;
    }
    
    public String getName() {
        return this.m_name;
    }

}
