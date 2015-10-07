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
 * Class QosPolicy is a base class for all the different QoS defined to the
 * Writers and Readers.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class QosPolicy {

    /**
     * Indicates whether to always send the QoS policy or not
     */
    protected boolean m_sendAlways;

    /**
     * Indicates if the {@link QosPolicy} has changed
     */
    public boolean hasChanged;

    /**
     * Default {@link QosPolicy} constructor
     */
    public QosPolicy() {
        this.hasChanged = false;
        this.m_sendAlways = false;
    }

    /**
     * Get the sendAlways attribute
     * 
     * @return The sendAlways attribute
     */
    public boolean getSendAlways() {
        return this.m_sendAlways;
    }

    /**
     * {@link QosPolicy} constructor
     * 
     * @param sendAlways Boolean indicating whether to always send the QoS or not
     */
    public QosPolicy(boolean sendAlways) {
        this.hasChanged = false;
        this.m_sendAlways = sendAlways;
    }

    /**
     * This method copies two instnces of {@link QosPolicy}
     * @param value The {@link QosPolicy} to be copied
     */
    public void copy(QosPolicy value) {
        hasChanged = value.hasChanged;
        m_sendAlways = value.m_sendAlways;
    }

}
