package org.fiware.kiara.ps.qos.policies;

import java.io.IOException;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.Serializable;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class ResourceLimitsQosPolicy extends Parameter implements Serializable {
    
    public QosPolicy parent;
    
    public ResourceLimitsQosPolicy() {
        super(ParameterId.PID_RESOURCE_LIMITS, (short) (Parameter.PARAMETER_KIND_LENGTH + 4 + 4));
        this.parent = new QosPolicy(false);
        this.maxSamples = 5000;
        this.maxInstances = 10;
        this.maxSamplesPerInstance = 400;
        this.allocatedSamples = 3000;
    }

    public int maxSamples;
    
    public int maxInstances;
    
    public int maxSamplesPerInstance;
    
    public int allocatedSamples;
    
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        super.serialize(impl, message, name);
        impl.serializeI32(message, name, this.maxSamples);
        impl.serializeI32(message, name, this.maxInstances);
        impl.serializeI32(message, name, this.maxSamplesPerInstance);
    }

    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        super.deserialize(impl, message, name);
        this.maxSamples = impl.deserializeI32(message, name);
        this.maxInstances = impl.deserializeI32(message, name);
        this.maxSamplesPerInstance = impl.deserializeI32(message, name);
    }

    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        // Do nothing
    }

}
