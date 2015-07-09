package org.fiware.kiara.ps.rtps.messages.elements.parameters;

import java.io.IOException;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class ParameterBool extends Parameter {

    private boolean m_bool;

    public ParameterBool() {
        super(ParameterId.PID_EXPECTS_INLINE_QOS, Parameter.PARAMETER_BOOL_LENGTH);
    }

    /**
     * Constructor using a parameter PID and the parameter length
     * @param pid Pid of the parameter
     * @param length Its associated length
     */
    public ParameterBool(ParameterId pid, short length) {
        super(pid, length);
        m_bool = false;
    }

    public ParameterBool(ParameterId pid, short length, boolean inbool) {
        super(pid,length);
        m_bool = inbool;
    }

    public void setBool(boolean bool) {
        this.m_bool = bool;
    }

    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        super.serialize(impl, message, name);
        impl.serializeBoolean(message, name, this.m_bool);
    }

    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        super.deserialize(impl, message, name);
        this.m_bool = impl.deserializeBoolean(message, name);
    }

    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        // Do nothing
    }

}
