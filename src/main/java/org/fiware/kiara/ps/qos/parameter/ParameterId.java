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
package org.fiware.kiara.ps.qos.parameter;

import java.io.IOException;

import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.ps.rtps.messages.common.types.SubmessageId;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public enum ParameterId{
	
	PID_PAD ((short) 0x0000),
	PID_SENTINEL ((short) 0x0001),
	PID_USER_DATA ((short) 0x002c),
	PID_TOPIC_NAME ((short) 0x0005),
	PID_TYPE_NAME ((short) 0x0007),
	PID_GROUP_DATA ((short) 0x002d),
	PID_TOPIC_DATA ((short) 0x002e),
	PID_DURABILITY ((short) 0x001d),
	PID_DURABILITY_SERVICE ((short) 0x001e),
	PID_DEADLINE ((short) 0x0023),
	PID_LATENCY_BUDGET ((short) 0x0027),
	PID_LIVELINESS ((short) 0x001b),
	PID_RELIABILITY ((short) 0x001A),
	PID_LIFESPAN ((short) 0x002b),
	PID_DESTINATION_ORDER ((short) 0x0025),
	PID_HISTORY ((short) 0x0040),
	PID_RESOURCE_LIMITS ((short) 0x0041),
	PID_OWNERSHIP ((short) 0x001f),
	PID_OWNERSHIP_STRENGTH ((short) 0x0006),
	PID_PRESENTATION ((short) 0x0021),
	PID_PARTITION ((short) 0x0029),
	PID_TIME_BASED_FILTER ((short) 0x0004),
	PID_TRANSPORT_PRIORITY ((short) 0x0049),
	PID_PROTOCOL_VERSION ((short) 0x0015),
	PID_VENDORID ((short) 0x0016),
	PID_UNICAST_LOCATOR ((short) 0x002f),
	PID_MULTICAST_LOCATOR ((short) 0x0030),
	PID_MULTICAST_IPADDRESS ((short) 0x0011),
	PID_DEFAULT_UNICAST_LOCATOR ((short) 0x0031),
	PID_DEFAULT_MULTICAST_LOCATOR ((short) 0x0048),
	PID_METATRAFFIC_UNICAST_LOCATOR ((short) 0x0032),
	PID_METATRAFFIC_MULTICAST_LOCATOR ((short) 0x0033),
	PID_DEFAULT_UNICAST_IPADDRESS ((short) 0x000c),
	PID_DEFAULT_UNICAST_PORT ((short) 0x000e),
	PID_METATRAFFIC_UNICAST_IPADDRESS ((short) 0x0045),
	PID_METATRAFFIC_UNICAST_PORT ((short) 0x000d),
	PID_METATRAFFIC_MULTICAST_IPADDRESS ((short) 0x000b),
	PID_METATRAFFIC_MULTICAST_PORT ((short) 0x0046),
	PID_EXPECTS_INLINE_QOS ((short) 0x0043),
	PID_PARTICIPANT_MANUAL_LIVELINESS_COUNT ((short) 0x0034),
	PID_PARTICIPANT_BUILTIN_ENDPOINTS ((short) 0x0044),
	PID_PARTICIPANT_LEASE_DURATION ((short) 0x0002),
	PID_CONTENT_FILTER_PROPERTY ((short) 0x0035),
	PID_PARTICIPANT_GUID ((short) 0x0050),
	PID_PARTICIPANT_ENTITYID ((short) 0x0051),
	PID_GROUP_GUID ((short) 0x0052),
	PID_GROUP_ENTITYID ((short) 0x0053),
	PID_BUILTIN_ENDPOINT_SET ((short) 0x0058),
	PID_PROPERTY_LIST ((short) 0x0059),
	PID_TYPE_MAX_SIZE_SERIALIZED ((short) 0x0060),
	PID_ENTITY_NAME ((short) 0x0062),
	PID_KEY_HASH ((short) 0x0070),
	PID_STATUS_INFO ((short) 0x0071),
	PID_ENDPOINT_GUID ((short) 0x005a);
	
	private final short m_value;
	
	private ParameterId(short value) {
		this.m_value = value;
	}
	
	public short getValue() {
		return this.m_value;
	}

	/*@Override
	public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
		impl.serializeI16(message, name, this.m_value);
	}*/

	/*@Override
	public void deserialize(SerializerImpl impl, BinaryInputStream message,String name) throws IOException {
		ParameterId id = createFromValue(impl.deserializeI16(message, name));
		this.m_value = id.getValue();
	}*/
	
	public static ParameterId createFromValue(short value) {
		switch (value) {
		case 0x0000:
			return ParameterId.PID_PAD;
		case 0x0001:
			return ParameterId.PID_SENTINEL; 
		case 0x002c:
			return ParameterId.PID_USER_DATA;
		case 0x0005:
			return ParameterId.PID_TOPIC_NAME; 
		case 0x0007:
			return ParameterId.PID_TYPE_NAME;
		case 0x002d:
			return ParameterId.PID_GROUP_DATA;
		case 0x002e:
			return ParameterId.PID_TOPIC_DATA;
		case 0x001d:
			return ParameterId.PID_DURABILITY;
		case 0x001e:
			return ParameterId.PID_DURABILITY_SERVICE;
		case 0x0023:
			return ParameterId.PID_DEADLINE;
		case 0x0027:
			return ParameterId.PID_LATENCY_BUDGET; 
		case 0x001b:
			return ParameterId.PID_LIVELINESS;
		case 0x001A:
			return ParameterId.PID_RELIABILITY;
		case 0x002b:
			return ParameterId.PID_LIFESPAN; 
		case 0x0025:
			return ParameterId.PID_DESTINATION_ORDER; 
		case 0x0040:
			return ParameterId.PID_HISTORY;
		case 0x0041:
			return ParameterId.PID_RESOURCE_LIMITS; 
		case 0x001f:
			return ParameterId.PID_OWNERSHIP;
		case 0x0006:
			return ParameterId.PID_OWNERSHIP_STRENGTH; 
		case 0x0021:
			return ParameterId.PID_PRESENTATION;
		case 0x0029:
			return ParameterId.PID_PARTITION;
		case 0x0004:
			return ParameterId.PID_TIME_BASED_FILTER; 
		case 0x0049:
			return ParameterId.PID_TRANSPORT_PRIORITY; 
		case 0x0015:
			return ParameterId.PID_PROTOCOL_VERSION;
		case 0x0016:
			return ParameterId.PID_VENDORID; 
		case 0x002f:
			return ParameterId.PID_UNICAST_LOCATOR; 
		case 0x0030:
			return ParameterId.PID_MULTICAST_LOCATOR; 
		case 0x0011:
			return ParameterId.PID_MULTICAST_IPADDRESS; 
		case 0x0031:
			return ParameterId.PID_DEFAULT_UNICAST_LOCATOR; 
		case 0x0048:
			return ParameterId.PID_DEFAULT_MULTICAST_LOCATOR; 
		case 0x0032:
			return ParameterId.PID_METATRAFFIC_UNICAST_LOCATOR; 
		case 0x0033:
			return ParameterId.PID_METATRAFFIC_MULTICAST_LOCATOR; 
		case 0x000c:;
			return ParameterId.PID_DEFAULT_UNICAST_IPADDRESS; 
		case 0x000e:
			return ParameterId.PID_DEFAULT_UNICAST_PORT; 
		case 0x0045:
			return ParameterId.PID_METATRAFFIC_UNICAST_IPADDRESS; 
		case 0x000d:
			return ParameterId.PID_METATRAFFIC_UNICAST_PORT; 
		case 0x000b:
			return ParameterId.PID_METATRAFFIC_MULTICAST_IPADDRESS; 
		case 0x0046:
			return ParameterId.PID_METATRAFFIC_MULTICAST_PORT; 
		case 0x0043:
			return ParameterId.PID_EXPECTS_INLINE_QOS; 
		case 0x0034:
			return ParameterId.PID_PARTICIPANT_MANUAL_LIVELINESS_COUNT; 
		case 0x0044:
			return ParameterId.PID_PARTICIPANT_BUILTIN_ENDPOINTS; 
		case 0x0002:
			return ParameterId.PID_PARTICIPANT_LEASE_DURATION; 
		case 0x0035:
			return ParameterId.PID_CONTENT_FILTER_PROPERTY; 
		case 0x0050:
			return ParameterId.PID_PARTICIPANT_GUID; 
		case 0x0051:
			return ParameterId.PID_PARTICIPANT_ENTITYID; 
		case 0x0052:
			return ParameterId.PID_GROUP_GUID; 
		case 0x0053:
			return ParameterId.PID_GROUP_ENTITYID; 
		case 0x0058:
			return ParameterId.PID_BUILTIN_ENDPOINT_SET; 
		case 0x0059:
			return ParameterId.PID_PROPERTY_LIST; 
		case 0x0060:
			return ParameterId.PID_TYPE_MAX_SIZE_SERIALIZED; 
		case 0x0062:
			return ParameterId.PID_ENTITY_NAME; 
		case 0x0070:
			return ParameterId.PID_KEY_HASH; 
		case 0x0071:
			return ParameterId.PID_STATUS_INFO; 
		case 0x005a:
			return ParameterId.PID_ENDPOINT_GUID;
		default:
			return ParameterId.PID_SENTINEL;
		}
	}

}
