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

public enum DurabilityQosPolicyKind {
    
    VOLATILE_DURABILITY_QOS((byte) 0), //!< Volatile Durability (default for Subscribers).
    TRANSIENT_LOCAL_DURABILITY_QOS((byte) 1), //!< Transient Local Durability (default for Publishers).
    TRANSIENT_DURABILITY_QOS((byte) 2), //!< NOT IMPLEMENTED.
    PERSISTENT_DURABILITY_QOS((byte) 3); //!< NOT IMPLEMENTED.
    
    private byte m_value;
    
    private DurabilityQosPolicyKind(byte value) {
        this.m_value = value;
    }
    
    public byte getValue() {
        return this.m_value;
    }

}
