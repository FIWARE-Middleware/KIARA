/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2015 German Research Center for Artificial Intelligence (DFKI)
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
 * Enum ChangeForReaderStatus, possible states for a CacheChange in a
 * ReaderProxy.
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public enum ChangeForReaderStatus {

    /**
     * Change is UNSENT (not sent yet)
     */
    UNSENT(0),
    /**
     * Change is UNACKNOWLEDGED (sent but not aknowledged)
     */
    UNACKNOWLEDGED(1),
    /**
     * Change is REQUESTED (sent and not received by reader)
     */
    REQUESTED(2),
    /**
     * Change is ACKNOWLEDGED (sent and received by reader)
     */
    ACKNOWLEDGED(3),
    /**
     * Change is UNDERWAY (in process to be sent)
     */
    UNDERWAY(4);

    /**
     * Enumeration value
     */
    private final int m_value;

    /**
     * Private default {@link ChangeForReaderStatus} constructor
     * 
     * @param value The enumeration value
     */
    private ChangeForReaderStatus(int value) {
        this.m_value = value;
    }

    /**
     * Creates a new ChangeForReaderStatus object from its integer value.
     * 
     * @param value The ordinal value of the enumeration
     * @return ChangeForReaderStatus
     */
    public static ChangeForReaderStatus createFromValue(int value) {
        switch (value) {
        case 0:
            return ChangeForReaderStatus.UNSENT;
        case 1:
            return ChangeForReaderStatus.UNACKNOWLEDGED;
        case 2:
            return ChangeForReaderStatus.REQUESTED;
        case 3:
            return ChangeForReaderStatus.ACKNOWLEDGED;
        case 4:
            return ChangeForReaderStatus.UNDERWAY;
        default:
            return ChangeForReaderStatus.UNSENT;        
        }
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
