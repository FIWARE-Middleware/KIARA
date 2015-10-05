package org.fiware.kiara.ps.rtps.messages.elements.parameters;

import java.io.IOException;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class ParameterTime extends Parameter {
    
    private Timestamp m_timestamp;

    public ParameterTime() {
        super(ParameterId.PID_PARTICIPANT_LEASE_DURATION, Parameter.PARAMETER_TIME_LENGTH);
        this.m_timestamp = new Timestamp();
    }
    
    public void setTimestamp(Timestamp timestamp) {
        this.m_timestamp = timestamp;
    }

    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        super.serialize(impl, message, name);
        this.m_timestamp.serialize(impl, message, name);
    }

    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        super.deserialize(impl, message, name);
        this.m_timestamp.deserialize(impl, message, name);
    }

    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.m_timestamp.deserialize(impl, message, name);
    }

    public Timestamp getTime() {
        return this.m_timestamp;
    }

}
