package org.fiware.kiara.ps.attributes;

import org.fiware.kiara.ps.qos.policies.HistoryQosPolicy;
import org.fiware.kiara.ps.qos.policies.HistoryQosPolicyKind;
import org.fiware.kiara.ps.qos.policies.ResourceLimitsQosPolicy;
import org.fiware.kiara.ps.rtps.common.TopicKind;

public class TopicAttributes {
    
    public TopicKind topicKind;
    
    public String topicName;
    
    public String topicDataType;
    
    public HistoryQosPolicy historyQos;
    
    public ResourceLimitsQosPolicy resourceLimitQos;
    
    public TopicAttributes() {
        this.topicKind = TopicKind.NO_KEY;
        this.topicName = "UNDEF";
        this.topicDataType = "UNDEF";
        this.historyQos = new HistoryQosPolicy();
        this.resourceLimitQos = new ResourceLimitsQosPolicy();
    }
    
    public TopicAttributes(String name, String dataType, TopicKind kind) {
        this.topicKind = kind;
        this.topicName = name;
        this.topicDataType = dataType;
        this.historyQos = new HistoryQosPolicy();
        this.resourceLimitQos = new ResourceLimitsQosPolicy();
    }
    
    public boolean checkQos() {
        if (resourceLimitQos.maxSamplesPerInstance > resourceLimitQos.maxSamples && this.topicKind == TopicKind.WITH_KEY) {
            System.out.println("INCORRECT TOPIC QOS: Max samples per instance must be less or equal than max samples"); // TODO Log this
            return false;
        }
        
        if (resourceLimitQos.maxSamplesPerInstance * resourceLimitQos.maxInstances > resourceLimitQos.maxSamples && topicKind == TopicKind.WITH_KEY) {
            System.out.println("TOPIC QOS: maxSamples < maxSamplesPerInstance * maxInstances"); // Log this (W)
        }
        
        if (historyQos.kind == HistoryQosPolicyKind.KEEP_LAST_HISTORY_QOS) {
            if (historyQos.depth > resourceLimitQos.maxSamples) {
                System.out.println("INCORRECT TOPIC QOS: depth must be <= max_samples"); // Log this
                return false;
            }
            if (historyQos.depth > resourceLimitQos.maxSamplesPerInstance && topicKind == TopicKind.WITH_KEY) {
                System.out.println("INCORRECT TOPIC QOS: depth must be <= max_samples_per_instance"); // Log this
                return false;
            }
            if (historyQos.depth <= 0) {
                System.out.println("INCORRECT TOPIC QOS: depth must be > 0"); // Log this
                return false;
            }
        }
        
        return true;
        
    }
    
    @Override
    public boolean equals(Object other) {
        if (other instanceof TopicAttributes) {
            TopicAttributes t2 = (TopicAttributes) other;
            if (this.topicKind != t2.topicKind) {
                return false;
            }
            if (!this.topicName.equals(t2.topicName)) {
                return false;
            }
            if (!this.topicDataType.equals(t2.topicDataType)) {
                return false;
            }
            if (this.historyQos.kind != t2.historyQos.kind) {
                return false;
            }
            if (this.historyQos.kind == HistoryQosPolicyKind.KEEP_LAST_HISTORY_QOS && this.historyQos.depth != t2.historyQos.depth) {
                return false;
            }
            return true;
        }
        return false;
    }

}
