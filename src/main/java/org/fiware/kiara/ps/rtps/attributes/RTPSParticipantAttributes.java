package org.fiware.kiara.ps.rtps.attributes;

import java.util.List;

import org.fiware.kiara.ps.rtps.common.LocatorList;

public class RTPSParticipantAttributes {
    
    public LocatorList defaultUnicastLocatorList;
    
    public LocatorList defaultMulticastLocatorList;
    
    public int defaultSendPort;
    
    public int sendSocketBufferSize;
    
    public int listenSocketBufferSize;
    
    public BuiltinAttributes builtinAtt;
    
    public PortParameters portParameters;
    
    public List<Byte> userData;
    
    public int participantID;
    
    public boolean useIPv4ToSend;
    
    public boolean useIPv6ToSend;
    
    private String m_name;
    
    public RTPSParticipantAttributes() {
        this.defaultUnicastLocatorList = new LocatorList();
        this.defaultMulticastLocatorList = new LocatorList();
        this.defaultSendPort = 10040;
        this.setName("RTPSParticipant");
        this.sendSocketBufferSize = 65536;
        this.listenSocketBufferSize = 65536;
        this.builtinAtt = new BuiltinAttributes();
        this.useIPv4ToSend = true;
        this.useIPv6ToSend = false;
        this.participantID = -1;
        this.portParameters = new PortParameters();
    }
    
    public void setName(String name) {
        this.m_name = name;
    }
    
    public String getName() {
        return this.m_name;
    }

}
