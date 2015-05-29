package org.fiware.kiara.ps.rtps.messages.elements.parameters;

import java.io.IOException;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;

public class ParameterBuilder {
	
	public static Parameter createParameter(ParameterId pid, short length) {
		Parameter param = null;
		switch (pid) {
		case PID_PAD:
		case PID_SENTINEL:
			param = new ParameterSentinel();
			break;
		case PID_USER_DATA:
		case PID_TOPIC_NAME:
		case PID_TYPE_NAME:
		case PID_GROUP_DATA:
		case PID_TOPIC_DATA:
		case PID_DURABILITY:
		case PID_DURABILITY_SERVICE:
		case PID_DEADLINE:
		case PID_LATENCY_BUDGET:
		case PID_LIVELINESS:
		case PID_RELIABILITY:
		case PID_LIFESPAN:
		case PID_DESTINATION_ORDER:
		case PID_HISTORY:
		case PID_RESOURCE_LIMITS:
		case PID_OWNERSHIP:
		case PID_OWNERSHIP_STRENGTH:
		case PID_PRESENTATION:
		case PID_PARTITION:
		case PID_TIME_BASED_FILTER:
		case PID_TRANSPORT_PRIORITY:
		case PID_PROTOCOL_VERSION:
		case PID_VENDORID:
		case PID_UNICAST_LOCATOR:
		case PID_MULTICAST_LOCATOR:
		case PID_MULTICAST_IPADDRESS:
		case PID_DEFAULT_UNICAST_LOCATOR:
		case PID_DEFAULT_MULTICAST_LOCATOR:
		case PID_METATRAFFIC_UNICAST_LOCATOR:
		case PID_METATRAFFIC_MULTICAST_LOCATOR:
		case PID_DEFAULT_UNICAST_IPADDRESS:
		case PID_DEFAULT_UNICAST_PORT:
		case PID_METATRAFFIC_UNICAST_IPADDRESS:
		case PID_METATRAFFIC_UNICAST_PORT:
		case PID_METATRAFFIC_MULTICAST_IPADDRESS:
		case PID_METATRAFFIC_MULTICAST_PORT:
		case PID_EXPECTS_INLINE_QOS:
		case PID_PARTICIPANT_MANUAL_LIVELINESS_COUNT:
		case PID_PARTICIPANT_BUILTIN_ENDPOINTS:
		case PID_PARTICIPANT_LEASE_DURATION:
		case PID_CONTENT_FILTER_PROPERTY:
		case PID_PARTICIPANT_GUID:
		case PID_PARTICIPANT_ENTITYID:
		case PID_GROUP_GUID:
		case PID_GROUP_ENTITYID:
		case PID_BUILTIN_ENDPOINT_SET:
		case PID_PROPERTY_LIST:
		case PID_TYPE_MAX_SIZE_SERIALIZED:
		case PID_ENTITY_NAME:
		case PID_KEY_HASH:
			param = new ParameterKey(new InstanceHandle());
			break;
		case PID_STATUS_INFO:
		case PID_ENDPOINT_GUID:
		}
		
		return param;
	}

}
