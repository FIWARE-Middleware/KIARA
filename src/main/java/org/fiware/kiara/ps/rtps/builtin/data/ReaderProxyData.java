package org.fiware.kiara.ps.rtps.builtin.data;

import org.fiware.kiara.ps.qos.ReaderQos;
import static org.fiware.kiara.ps.qos.policies.DurabilityQosPolicyKind.TRANSIENT_LOCAL_DURABILITY_QOS;
import static org.fiware.kiara.ps.qos.policies.ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS;
import org.fiware.kiara.ps.rtps.attributes.RemoteReaderAttributes;
import static org.fiware.kiara.ps.rtps.common.DurabilityKind.TRANSIENT_LOCAL;
import static org.fiware.kiara.ps.rtps.common.DurabilityKind.VOLATILE;
import static org.fiware.kiara.ps.rtps.common.EndpointKind.READER;
import org.fiware.kiara.ps.rtps.common.LocatorList;
import static org.fiware.kiara.ps.rtps.common.ReliabilityKind.BEST_EFFORT;
import static org.fiware.kiara.ps.rtps.common.ReliabilityKind.RELIABLE;
import org.fiware.kiara.ps.rtps.common.TopicKind;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.fiware.kiara.ps.rtps.messages.elements.ParameterList;

public class ReaderProxyData {

    private final GUID m_guid;
    private boolean m_expectsInlineQos;
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
    private InstanceHandle m_RTPSParticipantKey;
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
     * Reader Qos
     */
    private final ReaderQos m_qos;
    /**
     * Field to indicate if the Reader is Alive.
     */
    private boolean m_isAlive;
    /**
     * Topic kind
     */
    private TopicKind m_topicKind;
    /**
     * Parameter list
     */
    private final ParameterList m_parameterList;

    /**
     * Remote Attributes associated with this proxy data.
     */
    private final RemoteReaderAttributes m_remoteAtt;

    public ReaderProxyData() {
        this.m_guid = new GUID();
        m_unicastLocatorList = new LocatorList();
        m_multicastLocatorList = new LocatorList();
        m_key = new InstanceHandle();
        m_RTPSParticipantKey = new InstanceHandle();
        m_qos = new ReaderQos();
        m_parameterList = new ParameterList();
        m_remoteAtt = new RemoteReaderAttributes();
    }

    public GUID getGUID() {
        return this.m_guid;
    }

    public void setGUID(GUID value) {
        m_guid.copy(value);
    }

    public void setKey(GUID value) {
        m_key.setGuid(m_guid);
    }

    public void setKey(InstanceHandle value) {
        m_key.copy(value);
    }

    public InstanceHandle getRTPSParticipantKey() {
        return m_RTPSParticipantKey;
    }

    public void setRTPSParticipantKey(GUID value) {
        m_RTPSParticipantKey.setGuid(m_guid);
    }

    public void setRTPSParticipantKey(InstanceHandle value) {
        m_RTPSParticipantKey.copy(value);
    }

    public boolean getExpectsInlineQos() {
        return m_expectsInlineQos;
    }

    public void setExpectsInlineQos(boolean value) {
        m_expectsInlineQos = value;
    }

    public boolean getIsAlive() {
        return m_isAlive;
    }

    public void setIsAlive(boolean value) {
        m_isAlive = value;
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

    public String getTopicName() {
        return m_topicName;
    }

    public void setTopicName(String value) {
        m_topicName = value;
    }

    public String getTypeName() {
        return m_typeName;
    }

    public void setTypeName(String value) {
        m_typeName = value;
    }

    public TopicKind getTopicKind() {
        return m_topicKind;
    }

    public void setTopicKind(TopicKind value) {
        m_topicKind = value;
    }

    public ReaderQos getQos() {
        return m_qos;
    }

    public void setQos(ReaderQos value) {
        m_qos.copy(value);
    }

    public short getUserDefinedId() {
        return m_userDefinedId;
    }

    public void setUserDefinedId(short value) {
        m_userDefinedId = value;
    }

    public void copy(ReaderProxyData rit) {
        // TODO Implement

    }

    /**
    * Convert the ProxyData information to RemoteReaderAttributes object.
    * @return Reference to the RemoteReaderAttributes object.
    */
    public RemoteReaderAttributes toRemoteReaderAttributes() {
        m_remoteAtt.setGUID(m_guid);
        m_remoteAtt.expectsInlineQos = this.m_expectsInlineQos;
        m_remoteAtt.endpoint.durabilityKind = m_qos.durability.kind == TRANSIENT_LOCAL_DURABILITY_QOS ? TRANSIENT_LOCAL : VOLATILE;
        m_remoteAtt.endpoint.endpointKind = READER;
        m_remoteAtt.endpoint.topicKind = m_topicKind;
        m_remoteAtt.endpoint.reliabilityKind = m_qos.reliability.kind == RELIABLE_RELIABILITY_QOS ? RELIABLE : BEST_EFFORT;
        m_remoteAtt.endpoint.unicastLocatorList.copy(this.m_unicastLocatorList);
        m_remoteAtt.endpoint.multicastLocatorList.copy(this.m_multicastLocatorList);
        return m_remoteAtt;
    }

}
