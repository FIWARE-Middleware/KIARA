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
