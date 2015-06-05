package org.fiware.kiara.ps.rtps.messages.elements;

import java.io.IOException;

import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.RTPSSubmessageElement;

public abstract class Parameter extends RTPSSubmessageElement {
    
    public static short PARAMETER_KIND_LENGTH = 4;
    public static short PARAMETER_TIME_LENGTH = 8;
    public static short PARAMETER_PRESENTATION_LENGTH = 8;

    protected ParameterId m_parameterId;
    protected short m_length;

    public Parameter(ParameterId pid, short length) {
        this.m_parameterId = pid;
        this.m_length = length;
    }

    @Override
    public short getSize() {
        return 4;
    }

    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        //this.m_parameterId.serialize(impl, message, name);
        impl.serializeI16(message, name, this.m_parameterId.getValue());
        impl.serializeI16(message, "", this.m_length);
        // Serialize parameter content in child class
    }

    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        //this.m_parameterId.deserialize(impl, message, name);
        this.m_parameterId = ParameterId.createFromValue(impl.deserializeI16(message, name));
        this.m_length = impl.deserializeI16(message, "");
        // Deserialize parameter content in child class
    }

    public abstract void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException;

    public ParameterId getParameterId() {
        return this.m_parameterId;
    }

}
