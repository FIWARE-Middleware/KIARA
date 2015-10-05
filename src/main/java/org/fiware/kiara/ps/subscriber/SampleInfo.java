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
package org.fiware.kiara.ps.subscriber;

import org.fiware.kiara.ps.rtps.messages.common.types.ChangeKind;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;

/**
 * Class that represents the sample information of the data.
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public class SampleInfo {
    
    /**
     * {@link ChangeKind} of the SampleInfo
     */
    public ChangeKind sampleKind;
    
    /**
     * Writer {@link GUID}
     */
    public GUID writerGUID;
    
    /**
     * Strenght of the ownership
     */
    public short ownershipStrength;
    
    /**
     * {@link Timestamp} associated to the sample
     */
    public Timestamp sourceTimestamp;
    
    /**
     * InstanceHandle of the SampleInfo
     */
    public InstanceHandle handle;
    
    /**
     * Default constructor
     */
    public SampleInfo() {
        this.sampleKind = ChangeKind.ALIVE;
        this.writerGUID = new GUID();
        this.ownershipStrength = 0;
        this.sourceTimestamp = new Timestamp();
        this.handle = new InstanceHandle();
    }

}
