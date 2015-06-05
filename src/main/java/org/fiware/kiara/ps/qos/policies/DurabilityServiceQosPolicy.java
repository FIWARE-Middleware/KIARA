package org.fiware.kiara.ps.qos.policies;

import java.io.IOException;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class DurabilityServiceQosPolicy extends Parameter {

    public QosPolicy parent;
    
    public Timestamp serviceCleanupDelay;
    
    public HistoryQosPolicyKind kind;
    
    public int historyDepth;
    
    public int maxSamples;
    
    public int maxInstances;
    
    public int maxSamplesPerInstance;
    
    public DurabilityServiceQosPolicy() {
        super(ParameterId.PID_DURABILITY_SERVICE, (short) (Parameter.PARAMETER_KIND_LENGTH + Parameter.PARAMETER_KIND_LENGTH + 4 + 4 + 4 + 4));
        this.parent = new QosPolicy(false);
        this.serviceCleanupDelay = new Timestamp().timeZero();
        this.kind = HistoryQosPolicyKind.KEEP_LAST_HISTORY_QOS;
        this.historyDepth = 1;
        this.maxSamples = -1;
        this.maxInstances = -1;
        this.maxSamplesPerInstance = -1;
    }
    
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        super.serialize(impl, message, name);
        
        this.serviceCleanupDelay.serialize(impl, message, name);
        
        impl.serializeByte(message, name, this.kind.getValue());
        impl.serializeByte(message, name, (byte) 0);
        impl.serializeByte(message, name, (byte) 0);
        impl.serializeByte(message, name, (byte) 0);
        
        impl.serializeI32(message, name, this.historyDepth);
        impl.serializeI32(message, name, this.maxSamples);
        impl.serializeI32(message, name, this.maxInstances);
        impl.serializeI32(message, name, this.maxSamplesPerInstance);
        
    }

    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        super.deserialize(impl, message, name);
        
        this.serviceCleanupDelay.deserialize(impl, message, name);
        
        this.kind = HistoryQosPolicyKind.values()[impl.deserializeByte(message, name)];
        message.skipBytes(3);
        
        this.historyDepth = impl.deserializeI32(message, name);
        this.maxSamples = impl.deserializeI32(message, name);
        this.maxInstances = impl.deserializeI32(message, name);
        this.maxSamplesPerInstance = impl.deserializeI32(message, name);
    }

    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        // Do nothing
    }

    
    

}
