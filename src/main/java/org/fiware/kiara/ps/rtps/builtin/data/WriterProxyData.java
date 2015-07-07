package org.fiware.kiara.ps.rtps.builtin.data;

import org.fiware.kiara.ps.qos.WriterQos;
import org.fiware.kiara.ps.rtps.attributes.RemoteWriterAttributes;
import org.fiware.kiara.ps.rtps.common.LocatorList;
import org.fiware.kiara.ps.rtps.common.TopicKind;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.fiware.kiara.ps.rtps.messages.elements.ParameterList;

public class WriterProxyData {
    
    private GUID m_guid;
    
    private LocatorList m_unicastLocatorList;
    
    private LocatorList m_multicastLocatorList;
    
    private InstanceHandle m_key;
    
    private InstanceHandle m_RTPSParticipantKey;
    
    private String m_typeName;
    
    private String m_topicName;
    
    private WriterQos m_qos;
    
    private int m_typeMaxSerialized;
    
    private boolean m_isAlive;
    
    private TopicKind m_topicKind;
    
    private ParameterList m_parameterList;
    
    private RemoteWriterAttributes m_remoteAtt;
    
    public WriterProxyData() {
        this.m_guid = new GUID();
    }

    public void copy(WriterProxyData rit) {
        // TODO Implement
        
    }

    public GUID getGUID() {
        return m_guid;
    }

    public void setGUID(GUID guid) {
        this.m_guid = guid;
    }

    public LocatorList getUnicastLocatorList() {
        return m_unicastLocatorList;
    }

    public void setUnicastLocatorList(LocatorList unicastLocatorList) {
        this.m_unicastLocatorList = unicastLocatorList;
    }

    public LocatorList getMulticastLocatorList() {
        return m_multicastLocatorList;
    }

    public void setMulticastLocatorList(LocatorList multicastLocatorList) {
        this.m_multicastLocatorList = multicastLocatorList;
    }

    public InstanceHandle getKey() {
        return m_key;
    }

    public void setKey(InstanceHandle key) {
        this.m_key = key;
    }

    public InstanceHandle getRTPSParticipantKey() {
        return m_RTPSParticipantKey;
    }

    public void setRTPSParticipantKey(InstanceHandle RTPSParticipantKey) {
        this.m_RTPSParticipantKey = RTPSParticipantKey;
    }

    public String getTypeName() {
        return m_typeName;
    }

    public void setTypeName(String typeName) {
        this.m_typeName = typeName;
    }

    public String getTopicName() {
        return m_topicName;
    }

    public void setTopicName(String topicName) {
        this.m_topicName = topicName;
    }

    public WriterQos getQos() {
        return m_qos;
    }

    public void setQos(WriterQos qos) {
        this.m_qos = qos;
    }

    public int getTypeMaxSerialized() {
        return m_typeMaxSerialized;
    }

    public void setTypeMaxSerialized(int typeMaxSerialized) {
        this.m_typeMaxSerialized = typeMaxSerialized;
    }

    public boolean isIsAlive() {
        return m_isAlive;
    }

    public void setIsAlive(boolean isAlive) {
        this.m_isAlive = isAlive;
    }

    public TopicKind getTopicKind() {
        return m_topicKind;
    }

    public void setTopicKind(TopicKind topicKind) {
        this.m_topicKind = topicKind;
    }

    public ParameterList getParameterList() {
        return m_parameterList;
    }

    public void setParameterList(ParameterList parameterList) {
        this.m_parameterList = parameterList;
    }

    public RemoteWriterAttributes getRemoteAtt() {
        return m_remoteAtt;
    }

    public void setRemoteAtt(RemoteWriterAttributes remoteAtt) {
        this.m_remoteAtt = remoteAtt;
    }

    

}
