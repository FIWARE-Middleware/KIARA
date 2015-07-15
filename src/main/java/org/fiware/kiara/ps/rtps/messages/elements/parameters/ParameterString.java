package org.fiware.kiara.ps.rtps.messages.elements.parameters;

import java.io.IOException;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class ParameterString extends Parameter {
    
    private String m_content; 

    public ParameterString(ParameterId pid) {
        super(pid, (short) 4);
    }

    /**
     * Constructor using a parameter PID and the parameter length
     * @param pid Pid of the parameter
     * @param length Its associated length
     */
    public ParameterString(ParameterId pid, short length) {
        super(pid, length);
    }

    public ParameterString(ParameterId pid, short length, String str) {
        super(pid, length);
        m_content = str;
    }

    public void setContent(String content) {
        this.m_content = content;
        int size = (4 + this.m_content.length());
        int pad = (4 - (size % 4));
        super.m_length = (short) (size + pad);
    }

    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        super.serialize(impl, message, name);
        if (this.m_content != null) {
            impl.serializeString(message, name, this.m_content);
            int size = (4 + this.m_content.length());
            int pad = (4 - (size % 4));
            for (byte i=0; i < pad; ++i) {
                impl.serializeByte(message, name, (byte) 0);
            }
        }
    }
    
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        super.deserialize(impl, message, name);
        this.m_content = impl.deserializeString(message, name);
        int size = (4 + this.m_content.length());
        int pad = (4 - (size % 4));
        message.skipBytes(pad);
    }
    
    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        // Do nothing
    }

}
