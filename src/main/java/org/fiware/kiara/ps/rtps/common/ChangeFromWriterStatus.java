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
package org.fiware.kiara.ps.rtps.common;

/**
 * num ChangeFromWriterStatus, possible states for 
 * a CacheChange in a WriterProxy.
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public enum ChangeFromWriterStatus {
    
    /**
     * Change is UNKNOWN (received and not processed)
     */
    UNKNOWN(0),
    /**
     * Change is MISSING (not received, can be requested)
     */
    MISSING(1),
    /**
     * Change is RECEIVED (received and processed)
     */
    RECEIVED(2),
    /**
     * Change is LOST (not received)
     */
    LOST(3);
    
    /**
     * Enumeration value
     */
    private int m_value;
    
    /**
     * Private default {@link ChangeFromWriterStatus} constructor
     * 
     * @param value The enumeration value
     */
    private ChangeFromWriterStatus(int value) {
        this.m_value = value;
    }

    /**
     * Get the enumeration value
     * 
     * @return The enumeration value
     */
    public int getValue() {
        return m_value;
    }

}
