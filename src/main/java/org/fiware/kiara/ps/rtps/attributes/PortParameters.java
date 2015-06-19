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

public class PortParameters {
    
    public short portBase; // Default 7400
    
    public short domainIDGain; // Default 250
    
    public short participantIDGain; // Default 2
    
    public short offsetd0; // Defailt value 0
    public short offsetd1; // Defailt value 10
    public short offsetd2; // Defailt value 1
    public short offsetd3; // Defailt value 11
    
    public PortParameters() {
        this.portBase = 7400;
        this.participantIDGain = 2;
        this.domainIDGain = 250;
        this.offsetd0 = 0;
        this.offsetd1 = 10;
        this.offsetd2 = 1;
        this.offsetd3 = 11;
    }
    
    public int getMulticastPort(int domainID) {
        return portBase + (domainIDGain * domainID) + offsetd0;
    }
    
    public int getUnicastPort(int domainID, int participantID) {
        return portBase + (domainIDGain * domainID) + offsetd1 + (participantIDGain * participantID);
    }

}
