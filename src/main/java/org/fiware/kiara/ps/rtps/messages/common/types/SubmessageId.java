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

/**
 * Enumeration that represents the submessage identifier
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public enum SubmessageId {

    /**
     * RTPS Pad message
     */
    PAD((byte) 0x01), 
    /**
     * RTPS AckNack message
     */
    ACKNACK((byte) 0x06), 
    /**
     * RTPS Heartbeat message
     */
    HEARTBEAT((byte) 0x07), 
    /**
     * RTPS Gap message
     */
    GAP((byte) 0x08), 
    /**
     * RTPS InfoTimestamp message
     */
    INFO_TS((byte) 0x09),
    /**
     * RTPS InfoSource message
     */
    INFO_SRC((byte) 0x0c),
    /**
     * RTPS InfoReplyIp4 message
     */
    INFO_REPLY_IP4((byte) 0x0d), 
    /**
     * RTPS InfoDestination message
     */
    INFO_DST((byte) 0x0e), 
    /**
     * RTPS InfoReply message
     */
    INFO_REPLY((byte) 0x0f), 
    /**
     * RTPS NackFrag message
     */
    NACK_FRAG((byte) 0x12), 
    /**
     * RTPS HeartbeatFrag message
     */
    HEARTBEAT_FRAG((byte) 0x13),
    /**
     * RTPS Data message
     */
    DATA((byte) 0x15),
    /**
     * RTPS DataFrag message
     */
    DATA_FRAG((byte) 0x016); 

    /**
     * Enumeration value
     */
    private final byte m_value;

    /**
     * Private {@link SubmessageId} constructor
     * 
     * @param value The enumeration value
     */
    private SubmessageId(byte value) {
        this.m_value = value;
    }

    /**
     * Creates a new SubmessageId according to the received value
     * 
     * @param value byte to create the SubmessageId value from
     * @return The new created SubmessageId
     */
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

    /**
     * Get the SubmessageId value
     * 
     * @return The SubmessageId value
     */
    public byte getValue() {
        return this.m_value;
    }

}
