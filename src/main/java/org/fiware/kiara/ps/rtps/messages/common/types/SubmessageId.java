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
package org.fiware.kiara.ps.rtps.messages.common.types;

public enum SubmessageId {
	
	PAD((byte) 0x01), /* Pad */
	ACKNACK((byte) 0x06), /* AckNack */
	HEARTBEAT((byte) 0x07), /* Heartbeat */
	GAP((byte) 0x08), /* Gap */
	INFO_TS((byte) 0x09), /* InfoTimestamp */
	INFO_SRC((byte) 0x0c), /* InfoSource */
	INFO_REPLY_IP4((byte) 0x0d), /* InfoReplyIp4 */
	INFO_DST((byte) 0x0e), /* InfoDestination */
	INFO_REPLY((byte) 0x0f), /* InfoReply */
	NACK_FRAG((byte) 0x12), /* NackFrag */
	HEARTBEAT_FRAG((byte) 0x13), /* HeartbeatFrag */
	DATA((byte) 0x15), /* Data */
	DATA_FRAG((byte) 0x016); /* DataFrag */
	
	private final byte m_value;
	
	private SubmessageId(byte value) {
		this.m_value = value;
	}
	
	public static SubmessageId createFromValue(byte value) {
		switch(value) {
		case 0x01:
			return SubmessageId.PAD;
		case 0x06:
			return SubmessageId.ACKNACK;
		case 0x07:
			return SubmessageId.HEARTBEAT;
		case 0x09:
			return SubmessageId.INFO_TS;
		case 0x0c:
			return SubmessageId.INFO_SRC;
		case 0x0d:
			return SubmessageId.INFO_REPLY_IP4;
		case 0x0e:
			return SubmessageId.INFO_DST;
		case 0x0f:
			return SubmessageId.INFO_REPLY;
		case 0x12:
			return SubmessageId.NACK_FRAG;
		case 0x13:
			return SubmessageId.HEARTBEAT_FRAG;
		case 0x15:
			return SubmessageId.DATA;
		case 0x016:
			return SubmessageId.DATA_FRAG;
		default:
			return SubmessageId.GAP;
		}
	}
	
	public byte getValue() {
		return this.m_value;
	}

}
