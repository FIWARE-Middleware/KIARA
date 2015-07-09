package org.fiware.kiara.ps.rtps.messages.elements.parameters;

import java.io.IOException;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.ps.rtps.messages.elements.ProtocolVersion;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class ParameterProtocolVersion extends Parameter {

    private final ProtocolVersion m_protocolVersion;

    public ParameterProtocolVersion() {
        super(ParameterId.PID_PROTOCOL_VERSION, Parameter.PARAMETER_PROTOCOL_LENGTH);
        m_protocolVersion = new ProtocolVersion();
    }

    /**
     * Constructor using a parameter PID and the parameter length
     * @param pid Pid of the parameter
     * @param length Its associated length
     */
    public ParameterProtocolVersion(ParameterId pid, short length) {
        super(pid, length);
        m_protocolVersion = new ProtocolVersion();
    }

    public void setProtocolVersion(ProtocolVersion protocolVersion) {
        this.m_protocolVersion.copy(protocolVersion);
    }

    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        super.serialize(impl, message, name);
        this.m_protocolVersion.serialize(impl, message, name);
    }

    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        super.deserialize(impl, message, name);
        this.m_protocolVersion.deserialize(impl, message, name);
    }

    @Override
    public void deserializeContent(SerializerImpl impl,
            BinaryInputStream message, String name) throws IOException {
        // TODO Auto-generated method stub

    }

}
