package org.fiware.kiara.ps.rtps.builtin.data;

import org.fiware.kiara.ps.qos.WriterQos;
import static org.fiware.kiara.ps.qos.policies.DurabilityQosPolicyKind.TRANSIENT_LOCAL_DURABILITY_QOS;
import static org.fiware.kiara.ps.qos.policies.ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS;
import org.fiware.kiara.ps.rtps.attributes.RemoteWriterAttributes;
import static org.fiware.kiara.ps.rtps.common.DurabilityKind.TRANSIENT_LOCAL;
import static org.fiware.kiara.ps.rtps.common.DurabilityKind.VOLATILE;
import static org.fiware.kiara.ps.rtps.common.EndpointKind.WRITER;
import org.fiware.kiara.ps.rtps.common.LocatorList;
import static org.fiware.kiara.ps.rtps.common.ReliabilityKind.BEST_EFFORT;
import static org.fiware.kiara.ps.rtps.common.ReliabilityKind.RELIABLE;
import org.fiware.kiara.ps.rtps.common.TopicKind;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.fiware.kiara.ps.rtps.messages.elements.ParameterList;

public class WriterProxyData {

    private final GUID m_guid;
    /**
     * Unicast locator list
     */
    private final LocatorList m_unicastLocatorList;
    /**
     * Multicast locator list
     */
    private final LocatorList m_multicastLocatorList;
    /**
     * GUID_t of the Reader converted to InstanceHandle_t
     */
    private final InstanceHandle m_key;
    /**
     * GUID_t of the participant converted to InstanceHandle
     */
    private final InstanceHandle m_RTPSParticipantKey;
    /**
     * Type name
     */
    private String m_typeName;
    /**
     * Topic name
     */
    private String m_topicName;
    /**
     * User defined ID
     */
    private short m_userDefinedId;
    /**
     * Writer Qos
     */
    private final WriterQos m_qos;
    /**
     * Maximum size of the type associated with this Writer, serialized.
     */
    private int m_typeMaxSerialized;
    /**
     * Indicates if the Writer is Alive.
     */
    private boolean m_isAlive;
    /**
     * Topic kind
     */
    private TopicKind m_topicKind;
    /**
     * Parameter list
     */
    private ParameterList m_parameterList;

    /**
     * Remote Attributes associated with this proxy data.
     */
    private RemoteWriterAttributes m_remoteAtt;

    public WriterProxyData() {
        this.m_guid = new GUID();
        m_unicastLocatorList = new LocatorList();
        m_multicastLocatorList = new LocatorList();
        m_key = new InstanceHandle();
        m_RTPSParticipantKey = new InstanceHandle();
        m_qos = new WriterQos();
        m_parameterList = new ParameterList();
        m_remoteAtt = new RemoteWriterAttributes();
    }

    public void copy(WriterProxyData rit) {
        // TODO Implement

    }

    public GUID getGUID() {
        return m_guid;
    }

    public void setGUID(GUID guid) {
        m_guid.copy(guid);
    }

    public LocatorList getUnicastLocatorList() {
        return m_unicastLocatorList;
    }

    public void setUnicastLocatorList(LocatorList unicastLocatorList) {
        this.m_unicastLocatorList.copy(unicastLocatorList);
    }

    public LocatorList getMulticastLocatorList() {
        return m_multicastLocatorList;
    }

    public void setMulticastLocatorList(LocatorList m_multicastLocatorList) {
        this.m_multicastLocatorList.copy(m_multicastLocatorList);
    }

    public InstanceHandle getKey() {
        return m_key;
    }

    public void setKey(InstanceHandle key) {
        m_key.copy(key);
    }

    public void setKey(GUID value) {
        m_key.setGuid(m_guid);
    }

    public InstanceHandle getRTPSParticipantKey() {
        return m_RTPSParticipantKey;
    }

    public void setRTPSParticipantKey(InstanceHandle RTPSParticipantKey) {
        this.m_RTPSParticipantKey.copy(RTPSParticipantKey);
    }

    public void setRTPSParticipantKey(GUID value) {
        this.m_RTPSParticipantKey.setGuid(m_guid);
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
        this.m_qos.copy(qos);
    }

    public int getTypeMaxSerialized() {
        return m_typeMaxSerialized;
    }

    public void setTypeMaxSerialized(int typeMaxSerialized) {
        this.m_typeMaxSerialized = typeMaxSerialized;
    }

    public boolean getIsAlive() {
        return m_isAlive;
    }

    public void setIsAlive(boolean value) {
        this.m_isAlive = value;
    }

    public TopicKind getTopicKind() {
        return m_topicKind;
    }

    public void setTopicKind(TopicKind value) {
        this.m_topicKind = value;
    }

    public short getUserDefinedId() {
        return m_userDefinedId;
    }

    public void setUserDefinedId(short value) {
        m_userDefinedId = value;
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

    /**
    * Convert the ProxyData information to RemoteWriterAttributes object.
    * @return Reference to the RemoteWriterAttributes object.
    */
    public RemoteWriterAttributes toRemoteWriterAttributes() {
        m_remoteAtt.setGUID(m_guid);
        m_remoteAtt.livelinessLeaseDuration.copy(m_qos.liveliness.leaseDuration);
        m_remoteAtt.ownershipStrength = (short)m_qos.ownershipStrength.value;
        m_remoteAtt.endpoint.durabilityKind = m_qos.durability.kind == TRANSIENT_LOCAL_DURABILITY_QOS ? TRANSIENT_LOCAL : VOLATILE;
        m_remoteAtt.endpoint.endpointKind = WRITER;
        m_remoteAtt.endpoint.topicKind = m_topicKind;
        m_remoteAtt.endpoint.reliabilityKind = m_qos.reliability.kind == RELIABLE_RELIABILITY_QOS ? RELIABLE : BEST_EFFORT;
        m_remoteAtt.endpoint.unicastLocatorList.copy(this.m_unicastLocatorList);
        m_remoteAtt.endpoint.multicastLocatorList.copy(this.m_multicastLocatorList);
        return m_remoteAtt;
    }

}
