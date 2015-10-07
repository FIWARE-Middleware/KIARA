/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2015 Proyectos y Sistemas de Mantenimiento S.L. (eProsima)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fiware.kiara.ps.rtps.builtin.data;

import static org.fiware.kiara.ps.qos.parameter.ParameterId.PID_ENDPOINT_GUID;
import static org.fiware.kiara.ps.qos.parameter.ParameterId.PID_EXPECTS_INLINE_QOS;
import static org.fiware.kiara.ps.qos.parameter.ParameterId.PID_KEY_HASH;
import static org.fiware.kiara.ps.qos.parameter.ParameterId.PID_MULTICAST_LOCATOR;
import static org.fiware.kiara.ps.qos.parameter.ParameterId.PID_PARTICIPANT_GUID;
import static org.fiware.kiara.ps.qos.parameter.ParameterId.PID_PROTOCOL_VERSION;
import static org.fiware.kiara.ps.qos.parameter.ParameterId.PID_TOPIC_NAME;
import static org.fiware.kiara.ps.qos.parameter.ParameterId.PID_TYPE_NAME;
import static org.fiware.kiara.ps.qos.parameter.ParameterId.PID_UNICAST_LOCATOR;
import static org.fiware.kiara.ps.qos.parameter.ParameterId.PID_VENDORID;
import static org.fiware.kiara.ps.qos.policies.DurabilityQosPolicyKind.TRANSIENT_LOCAL_DURABILITY_QOS;
import static org.fiware.kiara.ps.qos.policies.ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS;
import static org.fiware.kiara.ps.rtps.common.DurabilityKind.TRANSIENT_LOCAL;
import static org.fiware.kiara.ps.rtps.common.DurabilityKind.VOLATILE;
import static org.fiware.kiara.ps.rtps.common.EndpointKind.READER;
import static org.fiware.kiara.ps.rtps.common.ReliabilityKind.BEST_EFFORT;
import static org.fiware.kiara.ps.rtps.common.ReliabilityKind.RELIABLE;
import static org.fiware.kiara.ps.rtps.common.TopicKind.NO_KEY;
import static org.fiware.kiara.ps.rtps.messages.elements.Parameter.PARAMETER_BOOL_LENGTH;
import static org.fiware.kiara.ps.rtps.messages.elements.Parameter.PARAMETER_GUID_LENGTH;
import static org.fiware.kiara.ps.rtps.messages.elements.Parameter.PARAMETER_LOCATOR_LENGTH;

import java.io.IOException;

import org.fiware.kiara.ps.qos.ReaderQos;
import org.fiware.kiara.ps.qos.policies.DeadLineQosPolicy;
import org.fiware.kiara.ps.qos.policies.DestinationOrderQosPolicy;
import org.fiware.kiara.ps.qos.policies.DurabilityQosPolicy;
import org.fiware.kiara.ps.qos.policies.DurabilityServiceQosPolicy;
import org.fiware.kiara.ps.qos.policies.GroupDataQosPolicy;
import org.fiware.kiara.ps.qos.policies.LatencyBudgetQosPolicy;
import org.fiware.kiara.ps.qos.policies.LifespanQosPolicy;
import org.fiware.kiara.ps.qos.policies.LivelinessQosPolicy;
import org.fiware.kiara.ps.qos.policies.OwnershipQosPolicy;
import org.fiware.kiara.ps.qos.policies.PartitionQosPolicy;
import org.fiware.kiara.ps.qos.policies.PresentationQosPolicy;
import org.fiware.kiara.ps.qos.policies.ReliabilityQosPolicy;
import org.fiware.kiara.ps.qos.policies.TimeBasedFilterQosPolicy;
import org.fiware.kiara.ps.qos.policies.TopicDataQosPolicy;
import org.fiware.kiara.ps.qos.policies.UserDataQosPolicy;
import org.fiware.kiara.ps.rtps.attributes.RemoteReaderAttributes;
import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.common.LocatorList;
import org.fiware.kiara.ps.rtps.common.TopicKind;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.ps.rtps.messages.elements.ParameterList;
import org.fiware.kiara.ps.rtps.messages.elements.SerializedPayload;
import org.fiware.kiara.ps.rtps.messages.elements.parameters.ParameterBool;
import org.fiware.kiara.ps.rtps.messages.elements.parameters.ParameterGuid;
import org.fiware.kiara.ps.rtps.messages.elements.parameters.ParameterKey;
import org.fiware.kiara.ps.rtps.messages.elements.parameters.ParameterLocator;
import org.fiware.kiara.ps.rtps.messages.elements.parameters.ParameterProtocolVersion;
import org.fiware.kiara.ps.rtps.messages.elements.parameters.ParameterString;
import org.fiware.kiara.ps.rtps.messages.elements.parameters.ParameterVendorId;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class ReaderProxyData, used to represent all the information on a Reader
 * (both local and remote) with the purpose of implementing the discovery.
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class ReaderProxyData {

    /**
     * {@link ReaderProxyData} GUID
     */
    private final GUID m_guid;
    
    /**
     * Boolean value indicating if the inlineQos are going to be used 
     */
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

    /**
     * Logging object
     */
    private static final Logger logger = LoggerFactory.getLogger(WriterProxyData.class);

    /**
     * Default {@link ReaderProxyData} constructor
     */
    public ReaderProxyData() {
        this.m_guid = new GUID();
        m_unicastLocatorList = new LocatorList();
        m_multicastLocatorList = new LocatorList();
        m_key = new InstanceHandle();
        m_RTPSParticipantKey = new InstanceHandle();
        m_typeName = "";
        m_topicName = "";
        m_userDefinedId = 0;
        m_qos = new ReaderQos();
        m_isAlive = false;
        m_topicKind = TopicKind.NO_KEY;
        m_parameterList = new ParameterList();
        m_remoteAtt = new RemoteReaderAttributes();
    }

    /**
     * Get GUID
     *
     * @return GUID
     * @see GUID
     */
    public GUID getGUID() {
        return this.m_guid;
    }

    /**
     * Set GUID
     *
     * @param value GUID
     * @see GUID
     */
    public void setGUID(GUID value) {
        m_guid.copy(value);
    }

    /**
     * Set key
     *
     * @param value GUID
     */
    public void setKey(GUID value) {
        m_key.setGuid(m_guid);
    }
    
    /**
     * Get the Key
     * 
     * @return InstanceHandle representing the key
     */
    public InstanceHandle getKey() {
        return this.m_key;
    }

    /**
     * Set key
     *
     * @param value instance handle
     */
    public void setKey(InstanceHandle value) {
        m_key.copy(value);
    }

    /**
     * Get RTPS participant key
     *
     * @return RTPS participant key
     */
    public InstanceHandle getRTPSParticipantKey() {
        return m_RTPSParticipantKey;
    }

    /**
     * Set RTPS participant key
     *
     * @param value GUID
     * @see GUID
     */
    public void setRTPSParticipantKey(GUID value) {
        m_RTPSParticipantKey.setGuid(m_guid);
    }

    /**
     * Set RTPS participant key
     *
     * @param value instance handle
     * @see InstanceHandle
     */
    public void setRTPSParticipantKey(InstanceHandle value) {
        m_RTPSParticipantKey.copy(value);
    }

    /**
     * Get expectsInlineQos flag
     *
     * @return expectsInlineQos flag
     */
    public boolean getExpectsInlineQos() {
        return m_expectsInlineQos;
    }

    /**
     * Set expectsInlineQos flag
     *
     * @param value expectsInlineQos flag
     */
    public void setExpectsInlineQos(boolean value) {
        m_expectsInlineQos = value;
    }

    /**
     * Get isAlive flag
     *
     * @return isAlive flag
     */
    public boolean getIsAlive() {
        return m_isAlive;
    }

    /**
     * Set isAlive flag
     *
     * @param value isAlive flag
     */
    public void setIsAlive(boolean value) {
        m_isAlive = value;
    }

    /**
     * Get unicast locator list
     *
     * @return unicast locator list
     * @see LocatorList
     */
    public LocatorList getUnicastLocatorList() {
        return m_unicastLocatorList;
    }

    /**
     * Set unicast locator list
     *
     * @param unicastLocatorList unicast locator list
     * @see LocatorList
     */
    public void setUnicastLocatorList(LocatorList unicastLocatorList) {
        this.m_unicastLocatorList.copy(unicastLocatorList);
    }

    /**
     * Get multicast locator list
     *
     * @return multicast locator list
     */
    public LocatorList getMulticastLocatorList() {
        return m_multicastLocatorList;
    }

    /**
     * Set multicast locator list
     *
     * @param m_multicastLocatorList multicast locator list
     */
    public void setMulticastLocatorList(LocatorList m_multicastLocatorList) {
        this.m_multicastLocatorList.copy(m_multicastLocatorList);
    }

    /**
     * Get topic name
     *
     * @return topic name
     */
    public String getTopicName() {
        return m_topicName;
    }

    /**
     * Set topic name
     *
     * @param value topic name
     */
    public void setTopicName(String value) {
        m_topicName = value;
    }

    /**
     * Get type name
     *
     * @return type name
     */
    public String getTypeName() {
        return m_typeName;
    }

    /**
     * Set type name
     *
     * @param value type name
     */
    public void setTypeName(String value) {
        m_typeName = value;
    }

    /**
     * Get topic kind
     *
     * @return topic kind
     */
    public TopicKind getTopicKind() {
        return m_topicKind;
    }

    /**
     * Set topic kind
     *
     * @param value topic kind
     */
    public void setTopicKind(TopicKind value) {
        m_topicKind = value;
    }

    /**
     * Get reader QoS
     *
     * @return reader QoS
     */
    public ReaderQos getQos() {
        return m_qos;
    }

    /**
     * Set reader QoS
     *
     * @param value reader QoS
     */
    public void setQos(ReaderQos value) {
        m_qos.copy(value);
    }

    /**
     * Get user defined ID
     *
     * @return user defined ID
     */
    public short getUserDefinedId() {
        return m_userDefinedId;
    }

    /**
     * Set user defined ID
     *
     * @param value user defined ID
     */
    public void setUserDefinedId(short value) {
        m_userDefinedId = value;
    }

    /**
     * Convert the data to a parameter list to send this information as a RTPS
     * message.
     * 
     * @return The ParameterList
     */
    public ParameterList toParameterList() {
        m_parameterList.deleteParams();
        for (Locator lit : m_unicastLocatorList) {
            ParameterLocator p = new ParameterLocator(PID_UNICAST_LOCATOR, PARAMETER_LOCATOR_LENGTH, lit);
            m_parameterList.addParameter(p);
        }
        for (Locator lit : m_multicastLocatorList) {
            ParameterLocator p = new ParameterLocator(PID_MULTICAST_LOCATOR, PARAMETER_LOCATOR_LENGTH, lit);
            m_parameterList.addParameter(p);
        }
        {
            ParameterBool p = new ParameterBool(PID_EXPECTS_INLINE_QOS, PARAMETER_BOOL_LENGTH, m_expectsInlineQos);
            m_parameterList.addParameter(p);
        }
        {
            ParameterGuid p = new ParameterGuid(PID_PARTICIPANT_GUID, PARAMETER_GUID_LENGTH, m_RTPSParticipantKey);
            m_parameterList.addParameter(p);
        }
        {
            ParameterString p = new ParameterString(PID_TOPIC_NAME, (short) 0, m_topicName);
            m_parameterList.addParameter(p);
        }
        {
            ParameterString p = new ParameterString(PID_TYPE_NAME, (short) 0, m_typeName);
            m_parameterList.addParameter(p);
        }
        {
            ParameterKey p = new ParameterKey(PID_KEY_HASH, (short) 16, m_key);
            m_parameterList.addParameter(p);
        }
        {
            ParameterGuid p = new ParameterGuid(PID_ENDPOINT_GUID, (short) 16, m_guid);
            m_parameterList.addParameter(p);
        }
        {
            ParameterProtocolVersion p = new ParameterProtocolVersion(PID_PROTOCOL_VERSION, (short) 4);
            m_parameterList.addParameter(p);
        }
        {
            ParameterVendorId p = new ParameterVendorId(PID_VENDORID, (short) 4);
            m_parameterList.addParameter(p);
        }
        if (m_qos.durability.parent.getSendAlways() || m_qos.durability.parent.hasChanged) {
            DurabilityQosPolicy p = new DurabilityQosPolicy();
            p.copy(m_qos.durability);
            m_parameterList.addParameter(p);
        }
        if (m_qos.durabilityService.parent.getSendAlways() || m_qos.durabilityService.parent.hasChanged) {
            DurabilityServiceQosPolicy p = new DurabilityServiceQosPolicy();
            p.copy(m_qos.durabilityService);
            m_parameterList.addParameter(p);
        }
        if (m_qos.deadline.parent.getSendAlways() || m_qos.deadline.parent.hasChanged) {
            DeadLineQosPolicy p = new DeadLineQosPolicy();
            p.copy(m_qos.deadline);
            m_parameterList.addParameter(p);
        }
        if (m_qos.latencyBudget.parent.getSendAlways() || m_qos.latencyBudget.parent.hasChanged) {
            LatencyBudgetQosPolicy p = new LatencyBudgetQosPolicy();
            p.copy(m_qos.latencyBudget);
            m_parameterList.addParameter(p);
        }
        if (m_qos.liveliness.parent.getSendAlways() || m_qos.liveliness.parent.hasChanged) {
            LivelinessQosPolicy p = new LivelinessQosPolicy();
            p.copy(m_qos.liveliness);
            m_parameterList.addParameter(p);
        }
        if (m_qos.reliability.parent.getSendAlways() || m_qos.reliability.parent.hasChanged) {
            ReliabilityQosPolicy p = new ReliabilityQosPolicy();
            p.copy(m_qos.reliability);
            m_parameterList.addParameter(p);
        }
        if (m_qos.lifespan.parent.getSendAlways() || m_qos.lifespan.parent.hasChanged) {
            LifespanQosPolicy p = new LifespanQosPolicy();
            p.copy(m_qos.lifespan);
            m_parameterList.addParameter(p);
        }
        if (m_qos.userData.parent.getSendAlways() || m_qos.userData.parent.hasChanged) {
            UserDataQosPolicy p = new UserDataQosPolicy();
            p.copy(m_qos.userData);
            m_parameterList.addParameter(p);
        }
        if (m_qos.timeBasedFilter.parent.getSendAlways() || m_qos.timeBasedFilter.parent.hasChanged) {
            TimeBasedFilterQosPolicy p = new TimeBasedFilterQosPolicy();
            p.copy(m_qos.timeBasedFilter);
            m_parameterList.addParameter(p);
        }
        if (m_qos.ownership.parent.getSendAlways() || m_qos.ownership.parent.hasChanged) {
            OwnershipQosPolicy p = new OwnershipQosPolicy();
            p.copy(m_qos.ownership);
            m_parameterList.addParameter(p);
        }
        if (m_qos.destinationOrder.parent.getSendAlways() || m_qos.destinationOrder.parent.hasChanged) {
            DestinationOrderQosPolicy p = new DestinationOrderQosPolicy();
            p.copy(m_qos.destinationOrder);
            m_parameterList.addParameter(p);
        }
        if (m_qos.presentation.parent.getSendAlways() || m_qos.presentation.parent.hasChanged) {
            PresentationQosPolicy p = new PresentationQosPolicy();
            p.copy(m_qos.presentation);
            m_parameterList.addParameter(p);
        }
        if (m_qos.partition.parent.getSendAlways() || m_qos.partition.parent.hasChanged) {
            PartitionQosPolicy p = new PartitionQosPolicy();
            p.copy(m_qos.partition);
            m_parameterList.addParameter(p);
        }
        if (m_qos.topicData.parent.getSendAlways() || m_qos.topicData.parent.hasChanged) {
            TopicDataQosPolicy p = new TopicDataQosPolicy();
            p.copy(m_qos.topicData);
            m_parameterList.addParameter(p);
        }
        if (m_qos.groupData.parent.getSendAlways() || m_qos.groupData.parent.hasChanged) {
            GroupDataQosPolicy p = new GroupDataQosPolicy();
            p.copy(m_qos.groupData);
            m_parameterList.addParameter(p);
        }
        if (m_qos.timeBasedFilter.parent.getSendAlways() || m_qos.timeBasedFilter.parent.hasChanged) {
            TimeBasedFilterQosPolicy p = new TimeBasedFilterQosPolicy();
            p.copy(m_qos.timeBasedFilter);
            m_parameterList.addParameter(p);
        }
        this.m_parameterList.addSentinel();
        
        logger.debug("DiscoveredReaderData converted to ParameterList with {} parameters", m_parameterList.getParameters().size());
        return this.m_parameterList;
    }

    /**
     * Clear (put to default) the information.
     */
    public void clear() {
        m_expectsInlineQos = false;
        m_guid.copy(new GUID());
        m_unicastLocatorList.clear();
        m_multicastLocatorList.clear();
        m_key.copy(new InstanceHandle());
        m_RTPSParticipantKey.copy(new InstanceHandle());
        m_typeName = "";
        m_topicName = "";
        m_userDefinedId = 0;
        m_qos.copy(new ReaderQos());
        m_isAlive = true;
        m_topicKind = NO_KEY;

        m_parameterList.deleteParams();
        m_parameterList.resetList();
    }

    /**
     * Update the information (only certain fields can be updated).
     *
     * @param rdata Poitner to the object from which we are going to update.
     */
    public void update(ReaderProxyData rdata) {
        m_unicastLocatorList.copy(rdata.m_unicastLocatorList);
        m_multicastLocatorList.copy(rdata.m_multicastLocatorList);
        m_qos.setQos(rdata.getQos(), false);
        m_isAlive = rdata.getIsAlive();
    }

    /**
     * Copy ALL the information from another object.
     *
     * @param rdata Reference to the object from where the information must be
     * copied.
     */
    public void copy(ReaderProxyData rdata) {
        m_guid.copy(rdata.m_guid);
        m_unicastLocatorList.copy(rdata.m_unicastLocatorList);
        m_multicastLocatorList.copy(rdata.m_multicastLocatorList);
        m_key.copy(rdata.m_key);
        m_RTPSParticipantKey.copy(rdata.m_RTPSParticipantKey);
        m_typeName = rdata.m_typeName;
        m_topicName = rdata.m_topicName;
        m_userDefinedId = rdata.m_userDefinedId;
        m_qos.copy(rdata.m_qos);
        //cout << "COPYING DATA: expects inlineQOS : " << rdata->m_expectsInlineQos << endl;
        m_expectsInlineQos = rdata.m_expectsInlineQos;
        m_isAlive = rdata.m_isAlive;
        m_topicKind = rdata.m_topicKind;
    }

    /**
     * Convert the ProxyData information to RemoteReaderAttributes object.
     *
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
    
    /**
     * Get the ParameterList
     * 
     * @return The ParameterList
     */
    public ParameterList getParameterList() {
        return this.m_parameterList;
    }
    
    public synchronized boolean readFromCDRMessage(CacheChange change) {
        
        SerializedPayload payload = change.getSerializedPayload();
        payload.updateSerializer();
        BinaryInputStream bis = new BinaryInputStream(payload.getBuffer());
        ParameterList parameterList = new ParameterList();
        try {
            parameterList.deserialize(payload.getSerializer(), bis, "");
        } catch (IOException e) {
            logger.error(e.getMessage());
            return false;
            //e.printStackTrace();
        }
        for (Parameter param : parameterList.getParameters()) {
            
            switch (param.getParameterId()) {
            case PID_DURABILITY: 
            {
                DurabilityQosPolicy p = (DurabilityQosPolicy) param;
                this.m_qos.durability.copy(p);
                break;
            }
            case PID_DURABILITY_SERVICE:
            {
                DurabilityServiceQosPolicy p = (DurabilityServiceQosPolicy) param;
                this.m_qos.durabilityService.copy(p);
                break;
            }
            case PID_DEADLINE:
            {
                DeadLineQosPolicy p = (DeadLineQosPolicy) param;
                this.m_qos.deadline.copy(p);
                break;
            }
            case PID_LATENCY_BUDGET:
            {
                LatencyBudgetQosPolicy p = (LatencyBudgetQosPolicy) param;
                this.m_qos.latencyBudget.copy(p);
                break;
            }
            case PID_LIVELINESS:
            {
                LivelinessQosPolicy p = (LivelinessQosPolicy) param;
                this.m_qos.liveliness.copy(p);
                break;
            }
            case PID_RELIABILITY:
            {
                ReliabilityQosPolicy p = (ReliabilityQosPolicy) param;
                this.m_qos.reliability.copy(p);
                break;
            }
            
            case PID_LIFESPAN:
            {
                LifespanQosPolicy p = (LifespanQosPolicy) param;
                this.m_qos.lifespan.copy(p);
                break;
            }
            case PID_USER_DATA:
            {
                UserDataQosPolicy p = (UserDataQosPolicy) param;
                this.m_qos.userData.copy(p);
                break;
            }
            case PID_TIME_BASED_FILTER:
            {
                TimeBasedFilterQosPolicy p = (TimeBasedFilterQosPolicy) param;
                this.m_qos.timeBasedFilter.copy(p);
                break;
            }
            case PID_OWNERSHIP:
            {
                OwnershipQosPolicy p = (OwnershipQosPolicy) param;
                this.m_qos.ownership.copy(p);
                break;
            }
            case PID_DESTINATION_ORDER:
            {
                DestinationOrderQosPolicy p = (DestinationOrderQosPolicy) param;
                this.m_qos.destinationOrder.copy(p);
                break;
            }
            case PID_PRESENTATION:
            {
                PresentationQosPolicy p = (PresentationQosPolicy) param;
                this.m_qos.presentation.copy(p);
                break;
            }
            case PID_PARTITION:
            {
                PartitionQosPolicy p = (PartitionQosPolicy) param;
                this.m_qos.partition.copy(p);
                break;
            }
            case PID_TOPIC_DATA:
            {
                TopicDataQosPolicy p = (TopicDataQosPolicy) param;
                this.m_qos.topicData.copy(p);
                break;
            }
            case PID_GROUP_DATA:
            {
                GroupDataQosPolicy p = (GroupDataQosPolicy) param;
                this.m_qos.groupData.copy(p);
                break;
            }
            case PID_TOPIC_NAME:
            {
                ParameterString p = (ParameterString) param;
                this.m_topicName = new String(p.getString());
                break;
            }
            case PID_TYPE_NAME:
            {
                ParameterString p = (ParameterString) param;
                this.m_typeName = new String(p.getString());
                break;
            }
            case PID_PARTICIPANT_GUID:
            {
                ParameterGuid pGuid = (ParameterGuid) param;
                this.m_RTPSParticipantKey = new InstanceHandle(pGuid.getGUID());
                break;
            }
            case PID_ENDPOINT_GUID:
            {
                ParameterGuid pGuid = (ParameterGuid) param;
                this.m_guid.copy(pGuid.getGUID());
                break;
            }
            case PID_UNICAST_LOCATOR:
            {
                ParameterLocator p = (ParameterLocator) param;
                this.m_unicastLocatorList.pushBack(p.getLocator());
                break;
            }
            case PID_MULTICAST_LOCATOR:
            {
                ParameterLocator p = (ParameterLocator) param;
                this.m_multicastLocatorList.pushBack(p.getLocator());
                break;
            }
            case PID_EXPECTS_INLINE_QOS:
            {
                ParameterBool p = (ParameterBool) param;
                this.m_expectsInlineQos = p.getBool();
                break;
            }
            case PID_KEY_HASH:
            {
                ParameterKey p = (ParameterKey) param;
                this.m_key.copy(p.getKey());
                this.m_guid.copy(this.m_key.toGUID());
                break;
            }
            default:
                logger.debug("Parameter with ID {} NOT CONSIDERED", param.getParameterId());
                break;
            }
        
        }
        
        if (this.m_guid.getEntityId().getValue(3) == 0x04) {
            this.m_topicKind = NO_KEY;
        } else if (this.m_guid.getEntityId().getValue(3) == 0x07) {
            this.m_topicKind = TopicKind.WITH_KEY;
        }
        
        return true;
        
    }

}
