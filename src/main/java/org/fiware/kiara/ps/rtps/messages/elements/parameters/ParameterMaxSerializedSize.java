package org.fiware.kiara.ps.rtps.messages.elements.parameters;

import java.io.IOException;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.Count;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class ParameterMaxSerializedSize extends Parameter {
    
    private int maxSerializedSize;

    public ParameterMaxSerializedSize() {
        super(ParameterId.PID_TYPE_MAX_SIZE_SERIALIZED, Parameter.PARAMETER_TYPE_MAX_SIZE_SERIALIZED_LENGTH);
    }

    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        super.serialize(impl, message, name);
        impl.serializeUI32(message, name, this.maxSerializedSize);
    }

    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        super.deserialize(impl, message, name);
        this.maxSerializedSize = impl.deserializeUI32(message, name);
    }

    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.maxSerializedSize = impl.deserializeUI32(message, name);
    }

    public int getMaxSerializedSize() {
        return maxSerializedSize;
    }

    public void setMaxSerializedSize(int maxSerializedSize) {
        this.maxSerializedSize = maxSerializedSize;
    }

}
