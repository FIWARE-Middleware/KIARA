package org.fiware.kiara.ps.rtps.messages.elements.parameters;

import java.io.IOException;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.common.LocatorKind;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class ParameterLocator extends Parameter {

    private final Locator m_loc;

    public ParameterLocator(ParameterId pid) {
        super(pid, Parameter.PARAMETER_LOCATOR_LENGTH);
        m_loc = new Locator();
    }

    /**
     * Constructor using a parameter PID and the parameter length
     * @param pid Pid of the parameter
     * @param length Its associated length
     */
    public ParameterLocator(ParameterId pid, short length) {
        super(pid, length);
        m_loc = new Locator();
    }

    public ParameterLocator(ParameterId pid, short length, Locator loc) {
        super(pid, length);
        m_loc = new Locator(loc);
    }

    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        super.serialize(impl, message, name);
        impl.serializeI32(message, name, this.m_loc.getKind().getValue());
        impl.serializeUI32(message, name, this.m_loc.getPort());
        for (int i=0; i < this.m_loc.getAddress().length; ++i) {
            impl.serializeByte(message, name, this.m_loc.getAddress()[i]);
        }
    }

    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        super.deserialize(impl, message, name);
        this.m_loc.setKind(LocatorKind.values()[impl.deserializeI32(message, name)]);
        this.m_loc.setPort(impl.deserializeUI32(message, name));
        byte[] addr = new byte[16];
        for (int i=0; i < addr.length; ++i) {
            addr[i] = impl.deserializeByte(message, name);
        }
        this.m_loc.setAddress(addr);
    }
    
    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        // Do nothing
    }

    public void setLocator(Locator loc) {
        this.m_loc.copy(loc);
    }

}
