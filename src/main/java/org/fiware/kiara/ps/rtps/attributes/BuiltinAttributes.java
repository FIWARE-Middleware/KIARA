package org.fiware.kiara.ps.rtps.attributes;

import org.fiware.kiara.ps.rtps.common.LocatorList;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;

public class BuiltinAttributes {
    
    public boolean useSimplePDP; // SimpleParticipantDiscoveryProtocol
    
    public boolean useWriterLP; // WriterLivelinessProtocol
    
    public boolean useSimpleEDP; // SimpleEndpointDiscoveryProtocol
    
    public boolean useStaticEDP; // StaticEndpointDiscoveryProtocol
    
    public int domainID;
    
    public Timestamp leaseDuration;
    
    public Timestamp leaseDurationAnnouncementPeriod;
    
    public LocatorList metatrafficUnicastLocatorList;
    
    public LocatorList metatrafficMulticastLocatorList;
    
    private String m_staticEndpointXMLFilename;
    
    public BuiltinAttributes() {
        this.useSimplePDP = true;
        this.useWriterLP = true;
        this.useSimpleEDP = false;
        this.m_staticEndpointXMLFilename = "";
        this.domainID = 0;
        this.leaseDuration = new Timestamp(500, 0);
        this.leaseDurationAnnouncementPeriod = new Timestamp(250, 0);
        this.useWriterLP = true;
    }
    
    public String getStaticEndpointXMLFilename() {
        return this.m_staticEndpointXMLFilename;
    }
    
    public void setStaticEndpointXMLFilename(String filename) {
        this.m_staticEndpointXMLFilename = filename;
    }

}
