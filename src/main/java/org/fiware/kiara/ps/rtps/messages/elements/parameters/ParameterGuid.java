package org.fiware.kiara.ps.rtps.messages.elements.parameters;

import java.io.IOException;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class ParameterGuid extends Parameter {

    private GUID m_guid;

    public ParameterGuid(ParameterId pid) {
        super(pid, Parameter.PARAMETER_GUID_LENGTH);
    }

    public void setGUID(GUID guid) {
        this.m_guid = guid;
    }

    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        if (this.m_guid != null) {
            super.serialize(impl, message, name);
            this.m_guid.serialize(impl, message, name);
        }
    }

    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        if (this.m_guid == null) {
            this.m_guid = new GUID();
        }
        super.deserialize(impl, message, name);
        this.m_guid.deserialize(impl, message, name);
    }

    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        // Do nothing

    }

}
