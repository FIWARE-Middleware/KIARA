package org.fiware.kiara.ps.rtps.messages.elements.parameters;

import java.io.IOException;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.Count;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class ParameterCount extends Parameter {

    private Count m_count;

    public ParameterCount() {
        super(ParameterId.PID_PARTICIPANT_MANUAL_LIVELINESS_COUNT, Parameter.PARAMETER_COUNT_LENGTH);
    }

    public void setCount(Count count) {
        this.m_count = count;
    }

    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        if (this.m_count != null) {
            super.serialize(impl, message, name);
            this.m_count.serialize(impl, message, name);
        }
    }

    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        if (this.m_count == null) {
            this.m_count = new Count(0);
        }
        super.deserialize(impl, message, name);
        this.m_count.deserialize(impl, message, name);
    }

    @Override
    public void deserializeContent(SerializerImpl impl,BinaryInputStream message, String name) throws IOException {
        // Do nothing

    }



}
