package org.fiware.kiara.ps.rtps.messages.elements.parameters;

import java.io.IOException;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class ParameterGuid extends Parameter {

    private final GUID m_guid;

    public ParameterGuid(ParameterId pid) {
        super(pid, Parameter.PARAMETER_GUID_LENGTH);
        m_guid = new GUID();
    }

    /**
     * Constructor using a parameter PID and the parameter length
     * @param pid Pid of the parameter
     * @param length Its associated length
     */
    public ParameterGuid(ParameterId pid, short length) {
        super(pid, length);
        m_guid = new GUID();
    }

    public ParameterGuid(ParameterId pid, short length, GUID guid) {
        super(pid, length);
        m_guid = new GUID();
        m_guid.copy(guid);
    }

    public ParameterGuid(ParameterId pid, short length, InstanceHandle iH) {
        super(pid, length);
        m_guid = new GUID();
            for(int i =0; i<16; ++i) {
                    if(i<12)
                            m_guid.getGUIDPrefix().setValue(i, iH.getValue(i));
                    else
                            m_guid.getEntityId().setValue(i-12, iH.getValue(i));
            }
    };

    public void setGUID(GUID guid) {
        this.m_guid.copy(guid);
    }

    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        super.serialize(impl, message, name);
        this.m_guid.serialize(impl, message, name);
    }

    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        super.deserialize(impl, message, name);
        this.m_guid.deserialize(impl, message, name);
    }

    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        // Do nothing

    }

}
