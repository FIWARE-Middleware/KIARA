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
package org.fiware.kiara.ps.qos;

import org.fiware.kiara.ps.qos.policies.DeadLineQosPolicy;
import org.fiware.kiara.ps.qos.policies.DestinationOrderQosPolicy;
import org.fiware.kiara.ps.qos.policies.DurabilityQosPolicy;
import org.fiware.kiara.ps.qos.policies.DurabilityServiceQosPolicy;
import org.fiware.kiara.ps.qos.policies.GroupDataQosPolicy;
import org.fiware.kiara.ps.qos.policies.LatencyBudgetQosPolicy;
import org.fiware.kiara.ps.qos.policies.LifespanQosPolicy;
import org.fiware.kiara.ps.qos.policies.LivelinessQosPolicy;
import org.fiware.kiara.ps.qos.policies.OwnershipQosPolicy;
import org.fiware.kiara.ps.qos.policies.PartitionQosPolicy;
import org.fiware.kiara.ps.qos.policies.PresentationQosPolicy;
import org.fiware.kiara.ps.qos.policies.ReliabilityQosPolicy;
import org.fiware.kiara.ps.qos.policies.TimeBasedFilterQosPolicy;
import org.fiware.kiara.ps.qos.policies.TopicDataQosPolicy;
import org.fiware.kiara.ps.qos.policies.UserDataQosPolicy;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public class ReaderQos {
    
    public DurabilityQosPolicy durability;
    
    public DeadLineQosPolicy deadline;
    
    public LatencyBudgetQosPolicy latencyBudget;
    
    public LivelinessQosPolicy liveliness;
    
    public ReliabilityQosPolicy reliability;
    
    public OwnershipQosPolicy ownership;
    
    public DestinationOrderQosPolicy destinationOrder;
    
    public UserDataQosPolicy userData;
    
    public TimeBasedFilterQosPolicy timeBasedFilter;
    
    public PresentationQosPolicy presentation;
    
    public PartitionQosPolicy partition;
    
    public TopicDataQosPolicy topicData;
    
    public GroupDataQosPolicy groupData;
    
    public DurabilityServiceQosPolicy durabilityService;
    
    public LifespanQosPolicy lifespan;
    
    public ReaderQos() {
        this.durability = new DurabilityQosPolicy();
        this.deadline = new DeadLineQosPolicy();
        this.latencyBudget = new LatencyBudgetQosPolicy();
        this.liveliness = new LivelinessQosPolicy();
        this.reliability= new ReliabilityQosPolicy();
        this.ownership = new OwnershipQosPolicy();
        this.destinationOrder = new DestinationOrderQosPolicy();
        this.userData = new UserDataQosPolicy();
        this.timeBasedFilter = new TimeBasedFilterQosPolicy();
        this.presentation = new PresentationQosPolicy();
        this.partition = new PartitionQosPolicy();
        this.topicData = new TopicDataQosPolicy();
        this.groupData = new GroupDataQosPolicy();
        this.durabilityService = new DurabilityServiceQosPolicy();
        this.lifespan = new LifespanQosPolicy();
    }
    
    

}
