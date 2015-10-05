package org.fiware.kiara.ps.rtps.messages.elements.parameters;

import java.io.IOException;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class ParameterBuiltinEndpointSet extends Parameter {
    
    private int m_builtinEndpointSet;

    public ParameterBuiltinEndpointSet() {
        super(ParameterId.PID_BUILTIN_ENDPOINT_SET, Parameter.PARAMETER_BUILTINENDPOINTSET_LENGTH);
    }
    
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        super.serialize(impl, message, name);
        impl.serializeUI32(message, name, this.m_builtinEndpointSet);
    }
    
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        super.deserialize(impl, message, name);
        this.m_builtinEndpointSet = impl.deserializeUI32(message, name);
    }
    
    public void setBuiltinEndpointSet(int builtinEndpointSet) {
        this.m_builtinEndpointSet = builtinEndpointSet;
    }

    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.m_builtinEndpointSet = impl.deserializeUI32(message, name);
    }

    public int getEndpointSet() {
        return this.m_builtinEndpointSet;
    }

}
