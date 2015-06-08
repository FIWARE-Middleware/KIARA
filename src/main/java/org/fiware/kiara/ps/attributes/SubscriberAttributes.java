package org.fiware.kiara.ps.attributes;

import org.fiware.kiara.ps.qos.ReaderQos;
import org.fiware.kiara.ps.rtps.attributes.ReaderTimes;
import org.fiware.kiara.ps.rtps.common.LocatorList;

public class SubscriberAttributes {
    
    private short m_userDefinedID;
    
    private short m_entityID;
    
    public TopicAttributes topic;
    
    public ReaderQos qos;
    
    public ReaderTimes times;
    
    public LocatorList unicastLocatorList;
    
    public LocatorList multicastLocatorList;
    
    public boolean expectsInlineQos;
    
    public SubscriberAttributes() {
        this.m_userDefinedID = -1;
        this.m_entityID = -1;
        this.expectsInlineQos = false;
        
        this.topic = new TopicAttributes();
        this.qos = new ReaderQos();
        this.times = new ReaderTimes();
        this.unicastLocatorList = new LocatorList();
        this.multicastLocatorList = new LocatorList();
    }

    public short getUserDefinedID() {
        return m_userDefinedID;
    }

    public void setUserDefinedID(short m_userDefinedID) {
        this.m_userDefinedID = m_userDefinedID;
    }

    public short getEntityID() {
        return m_entityID;
    }

    public void setEntityID(short m_entityID) {
        this.m_entityID = m_entityID;
    }

}
