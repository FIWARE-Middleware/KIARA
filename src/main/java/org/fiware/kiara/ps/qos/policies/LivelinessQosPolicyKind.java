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
package org.fiware.kiara.ps.qos.policies;

/**
 * Enum LivelinessQosPolicyKind, different kinds of liveliness for
 * LivelinessQosPolicy
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public enum LivelinessQosPolicyKind {

    /**
     * Automatic Liveliness, default value.
     */
    AUTOMATIC_LIVELINESS_QOS((byte) 0),
    /**
     * MANUAL_BY_PARTICIPANT_LIVELINESS_QOS
     */
    MANUAL_BY_PARTICIPANT_LIVELINESS_QOS((byte) 1),
    /**
     * MANUAL_BY_TOPIC_LIVELINESS_QOS
     */
    MANUAL_BY_TOPIC_LIVELINESS_QOS((byte) 2);

    /**
     * Enumeration value
     */
    private final byte m_value;

    /**
     * Private {@link LivelinessQosPolicyKind} constructor
     * 
     * @param value The enumeration value
     */
    private LivelinessQosPolicyKind(byte value) {
        this.m_value = value;
    }

    /**
     * Get the enumeration value
     * 
     * @return The enumeration value
     */
    public byte getValue() {
        return this.m_value;
    }

    /**
     * Creates a new {@link LivelinessQosPolicyKind} from its value
     * 
     * @param value The enumeration value
     * @return A new {@link LivelinessQosPolicyKind} object with the specified value set
     */
    public static LivelinessQosPolicyKind fromValue(byte value) {
        switch (value) {
            case 0:
                return AUTOMATIC_LIVELINESS_QOS;
            case 1:
                return MANUAL_BY_PARTICIPANT_LIVELINESS_QOS;
            case 2:
                return MANUAL_BY_TOPIC_LIVELINESS_QOS;
            default:
                return null;
        }
    }
}
