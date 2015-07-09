package org.fiware.kiara.ps.rtps.messages.elements.parameters;

import java.io.IOException;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class ParameterPort extends Parameter {
    
    private int m_port;

    public ParameterPort(ParameterId pid) {
        super(pid, Parameter.PARAMETER_PORT_LENGTH);
    }

    /**
     * Constructor using a parameter PID and the parameter length
     * @param pid Pid of the parameter
     * @param length Its associated length
     */
    public ParameterPort(ParameterId pid, short length) {
        super(pid, length);
        m_port = 0;
    }

    public ParameterPort(ParameterId pid, short length, int po) {
        super(pid, length);
        m_port = po;
    }

    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        super.serialize(impl, message, name);
        impl.serializeUI32(message, name, this.m_port);
    }

    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        super.deserialize(impl, message, name);
        this.m_port = impl.deserializeUI32(message, name);
    }

    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        // TODO Do nothing
    }
    
    public void setPort(int port) {
        this.m_port = port;
    }

}
