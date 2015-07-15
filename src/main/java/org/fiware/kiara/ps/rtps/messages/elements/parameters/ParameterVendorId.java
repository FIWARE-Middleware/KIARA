package org.fiware.kiara.ps.rtps.messages.elements.parameters;

import java.io.IOException;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.ps.rtps.messages.elements.VendorId;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class ParameterVendorId extends Parameter {
    
    private final VendorId m_vendorId;

    public ParameterVendorId() {
        super(ParameterId.PID_VENDORID, Parameter.PARAMETER_VENDOR_LENGTH);
        m_vendorId = new VendorId();
        m_vendorId.setVendoreProsima();
    }

    /**
     * Constructor using a parameter PID and the parameter length
     * @param pid Pid of the parameter
     * @param length Its associated length
     */
    public ParameterVendorId(ParameterId pid, short length) {
        super(pid, length);
        m_vendorId = new VendorId();
        m_vendorId.setVendoreProsima();
    }

    public void setVendorId(VendorId vendorId) {
        this.m_vendorId.copy(vendorId);
    }

    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        super.serialize(impl, message, name);
        this.m_vendorId.serialize(impl, message, name);
    }

    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        super.deserialize(impl, message, name);
        this.m_vendorId.deserialize(impl, message, name);
    }

     @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
         this.m_vendorId.deserialize(impl, message, name);
    }

}
