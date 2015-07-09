package org.fiware.kiara.ps.rtps.messages.elements.parameters;

import java.io.IOException;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.ps.rtps.messages.elements.ProtocolVersion;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class ParameterProtocolVersion extends Parameter {
    
    private ProtocolVersion m_protocolVersion;

    public ParameterProtocolVersion(ParameterId pid, short length) {
        super(pid, length);
    }
    
    public void setProtocolVersion(ProtocolVersion protocolVersion) {
        this.m_protocolVersion = protocolVersion;
    }

    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        if (this.m_protocolVersion != null) {
            super.serialize(impl, message, name);
            this.m_protocolVersion.serialize(impl, message, name);
        }
    }

    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        if (this.m_protocolVersion == null) {
            this.m_protocolVersion = new ProtocolVersion();
        }
        super.deserialize(impl, message, name);
        this.m_protocolVersion.deserialize(impl, message, name);
    }

    @Override
    public void deserializeContent(SerializerImpl impl,
            BinaryInputStream message, String name) throws IOException {
        // TODO Auto-generated method stub

    }

}
