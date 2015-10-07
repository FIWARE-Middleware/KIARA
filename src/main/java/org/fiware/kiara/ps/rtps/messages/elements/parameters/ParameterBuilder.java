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
package org.fiware.kiara.ps.rtps.messages.elements.parameters;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.qos.policies.DeadLineQosPolicy;
import org.fiware.kiara.ps.qos.policies.DestinationOrderQosPolicy;
import org.fiware.kiara.ps.qos.policies.DurabilityQosPolicy;
import org.fiware.kiara.ps.qos.policies.LatencyBudgetQosPolicy;
import org.fiware.kiara.ps.qos.policies.LifespanQosPolicy;
import org.fiware.kiara.ps.qos.policies.LivelinessQosPolicy;
import org.fiware.kiara.ps.qos.policies.OwnershipQosPolicy;
import org.fiware.kiara.ps.qos.policies.OwnershipStrengthQosPolicy;
import org.fiware.kiara.ps.qos.policies.ReliabilityQosPolicy;
import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;

/**
 * Class in charge of constructing all the different {@link Parameter} objects
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public class ParameterBuilder {

    /**
     * Methos used to create a {@link Parameter} object depending on its {@link ParameterId}
     * 
     * @param pid The {@link Parameter} identifier
     * @param length The {@link Parameter} length
     * @return The new {@link Parameter} object
     */
    public static Parameter createParameter(ParameterId pid, short length) {
        Parameter param = null;
        switch (pid) {
        case PID_PAD:
        case PID_SENTINEL:
            param = new ParameterSentinel();
            break;
        case PID_USER_DATA:
        case PID_TOPIC_NAME:
            param = new ParameterString(ParameterId.PID_TOPIC_NAME);
            break;
        case PID_TYPE_NAME:
            param = new ParameterString(ParameterId.PID_TYPE_NAME);
            break;
        case PID_ENTITY_NAME:
            param = new ParameterString(ParameterId.PID_ENTITY_NAME);
            break;
        case PID_GROUP_DATA:
        case PID_TOPIC_DATA:
        case PID_DURABILITY:
            param = new DurabilityQosPolicy();
            break;
        case PID_DURABILITY_SERVICE:
        case PID_DEADLINE:
            param = new DeadLineQosPolicy();
            break;
        case PID_LATENCY_BUDGET:
            param = new LatencyBudgetQosPolicy();
            break;
        case PID_LIVELINESS:
            param = new LivelinessQosPolicy();
            break;
        case PID_RELIABILITY:
            param = new ReliabilityQosPolicy();
            break;
        case PID_LIFESPAN:
            param = new LifespanQosPolicy();
            break;
        case PID_DESTINATION_ORDER:
            param = new DestinationOrderQosPolicy();
            break;
        case PID_HISTORY:
        case PID_RESOURCE_LIMITS:
        case PID_OWNERSHIP:
            param = new OwnershipQosPolicy();
            break;
        case PID_OWNERSHIP_STRENGTH:
            param = new OwnershipStrengthQosPolicy();
            break;
        case PID_PRESENTATION:
        case PID_PARTITION:
        case PID_TIME_BASED_FILTER:
        case PID_TRANSPORT_PRIORITY:
        case PID_PROTOCOL_VERSION:
            param = new ParameterProtocolVersion();
            break;
        case PID_VENDORID:
            param = new ParameterVendorId();
            break;
        case PID_UNICAST_LOCATOR:
            param = new ParameterLocator(ParameterId.PID_UNICAST_LOCATOR);
            break;
        case PID_MULTICAST_LOCATOR:
            param = new ParameterLocator(ParameterId.PID_MULTICAST_LOCATOR);
            break;
        case PID_DEFAULT_UNICAST_LOCATOR:
            param = new ParameterLocator(ParameterId.PID_DEFAULT_UNICAST_LOCATOR);
            break;
        case PID_DEFAULT_MULTICAST_LOCATOR:
            param = new ParameterLocator(ParameterId.PID_DEFAULT_MULTICAST_LOCATOR);
            break;
        case PID_METATRAFFIC_UNICAST_LOCATOR:
            param = new ParameterLocator(ParameterId.PID_METATRAFFIC_UNICAST_LOCATOR);
            break;
        case PID_METATRAFFIC_MULTICAST_LOCATOR:
            param = new ParameterLocator(ParameterId.PID_METATRAFFIC_MULTICAST_LOCATOR);
            break;
        case PID_DEFAULT_UNICAST_PORT:
            param = new ParameterPort(ParameterId.PID_DEFAULT_UNICAST_PORT);
            break;
        case PID_METATRAFFIC_UNICAST_PORT:
            param = new ParameterPort(ParameterId.PID_METATRAFFIC_UNICAST_PORT);
            break;
        case PID_METATRAFFIC_MULTICAST_PORT:
            param = new ParameterPort(ParameterId.PID_METATRAFFIC_MULTICAST_PORT);
            break;
        case PID_MULTICAST_IPADDRESS:
            param = new ParameterIPv4Address(ParameterId.PID_MULTICAST_IPADDRESS);
            break;
        case PID_DEFAULT_UNICAST_IPADDRESS:
            param = new ParameterIPv4Address(ParameterId.PID_DEFAULT_UNICAST_IPADDRESS);
            break;
        case PID_METATRAFFIC_UNICAST_IPADDRESS:
            param = new ParameterIPv4Address(ParameterId.PID_METATRAFFIC_UNICAST_IPADDRESS);
            break;
        case PID_METATRAFFIC_MULTICAST_IPADDRESS:
            param = new ParameterIPv4Address(ParameterId.PID_METATRAFFIC_MULTICAST_IPADDRESS);
            break;
        case PID_EXPECTS_INLINE_QOS:
            param = new ParameterBool();
            break;
        case PID_PARTICIPANT_MANUAL_LIVELINESS_COUNT:
            param = new ParameterCount();
            break;
        case PID_PARTICIPANT_BUILTIN_ENDPOINTS:
        case PID_PARTICIPANT_LEASE_DURATION:
            param = new ParameterTime();
            break;
        case PID_CONTENT_FILTER_PROPERTY:
        case PID_PARTICIPANT_GUID:
            param = new ParameterGuid(ParameterId.PID_PARTICIPANT_GUID);
            break;
        case PID_GROUP_GUID:
            param = new ParameterGuid(ParameterId.PID_GROUP_GUID);
            break;
        case PID_ENDPOINT_GUID:
            param = new ParameterGuid(ParameterId.PID_ENDPOINT_GUID);
            break;
        case PID_PARTICIPANT_ENTITYID:
        case PID_GROUP_ENTITYID:
            param = new ParameterEntityId(ParameterId.PID_GROUP_ENTITYID);
            break;
        case PID_BUILTIN_ENDPOINT_SET:
            param = new ParameterBuiltinEndpointSet();
            break;
        case PID_PROPERTY_LIST:
            param = new ParameterPropertyList();
            break;
        case PID_TYPE_MAX_SIZE_SERIALIZED:
            param = new ParameterMaxSerializedSize();
            break;
        case PID_KEY_HASH:
            param = new ParameterKey(new InstanceHandle());
            break;
        case PID_STATUS_INFO:
            param = new ParameterStatus();
            break;
        }

        return param;
    }

}
