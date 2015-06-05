package org.fiware.kiara.ps.attributes;

import org.fiware.kiara.ps.qos.WriterQos;
import org.fiware.kiara.ps.rtps.attributes.WriterTimes;
import org.fiware.kiara.ps.rtps.common.LocatorList;

public class PublisherAttributes {
    
    public TopicAttributes topic;
    
    public WriterQos qos;
    
    public WriterTimes times;
    
    public LocatorList unicastLocatorList;
    
    public LocatorList multicastLocatorList;
    
    private short m_userDefinedId;
    
    private short m_entityId;
    
    public PublisherAttributes() {
        this.m_userDefinedId = -1;
        this.m_entityId = -1;
        
        this.topic = new TopicAttributes();
        this.qos = new WriterQos();
        this.times = new WriterTimes();
        this.unicastLocatorList = new LocatorList();
        this.multicastLocatorList = new LocatorList();
        
    }
    
    public short getUserDefinedId() {
        return this.m_userDefinedId;
    }
    
    public void setUserDefinedId(short userDefinedId) {
        this.m_userDefinedId = userDefinedId;
    }
    
    public short getEntityId() {
        return this.m_entityId;
    }
    
    public void setEntityId(short entityId) {
        this.m_entityId = entityId;
    }

}
