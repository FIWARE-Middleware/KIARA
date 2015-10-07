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
package org.fiware.kiara.ps.rtps.attributes;

/**
 * Class SimpleEDPAttributes, to define the attributes of the Simple Endpoint
 * Discovery Protocol.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class SimpleEDPAttributes {

    /**
     * Commands the EDP to use the Publication Writer and Subscription Reader (default value true).
     */
    public boolean usePulicationWriterAndSubscriptionReader;

    /**
     * Commands the EDP to use the Publication Reader and Subscription Writer (default value true).
     */
    public boolean usePulicationReaderAndSubscriptionWriter;

    /**
     * Default {@link SimpleEDPAttributes} constructor
     */
    public SimpleEDPAttributes() {
        this.usePulicationReaderAndSubscriptionWriter = true;
        this.usePulicationWriterAndSubscriptionReader = true;
    }

    /**
     * This method copies an instance of {@link SimpleEDPAttributes} into another
     * 
     * @param other The {@link SimpleEDPAttributes} to be copied
     */
    public void copy(SimpleEDPAttributes other) {
        usePulicationWriterAndSubscriptionReader = other.usePulicationWriterAndSubscriptionReader;
        usePulicationReaderAndSubscriptionWriter = other.usePulicationReaderAndSubscriptionWriter;
    }

}
