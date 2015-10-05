package org.fiware.kiara.ps.rtps.messages.elements.parameters;

import java.io.IOException;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class ParameterIPv4Address extends Parameter {
    
    private byte[] m_address;

    public ParameterIPv4Address(ParameterId pid) {
        super(pid, Parameter.PARAMETER_IP4_LENGTH);
        this.m_address = new byte[4];
        this.setIpV4Address((byte) 0, (byte) 0, (byte) 0, (byte) 0);
    }
    
    public void setIpV4Address(byte o1, byte o2, byte o3, byte o4) {
        this.m_address[0] = o1;
        this.m_address[1] = o2;
        this.m_address[2] = o3;
        this.m_address[3] = o4;
    }

    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        super.serialize(impl, message, name);
        impl.serializeByte(message, name, this.m_address[0]);
        impl.serializeByte(message, name, this.m_address[1]);
        impl.serializeByte(message, name, this.m_address[2]);
        impl.serializeByte(message, name, this.m_address[3]);
    }

    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        super.deserialize(impl, message, name);
        this.m_address[0] = impl.deserializeByte(message, name);
        this.m_address[1] = impl.deserializeByte(message, name);
        this.m_address[2] = impl.deserializeByte(message, name);
        this.m_address[3] = impl.deserializeByte(message, name);
    }

    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        // Do nothing
    }

}
