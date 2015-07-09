package org.fiware.kiara.ps.rtps.messages.elements.parameters;

import java.io.IOException;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.ps.rtps.messages.elements.VendorId;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class ParameterVendorId extends Parameter {
    
    private VendorId m_vendorId;

    public ParameterVendorId() {
        super(ParameterId.PID_VENDORID, Parameter.PARAMETER_VENDOR_LENGTH);
    }
    
    public void setVendorId(VendorId vendorId) {
        this.m_vendorId = vendorId;
    }

    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        if (this.m_vendorId != null) {
            super.serialize(impl, message, name);
            this.m_vendorId.serialize(impl, message, name);
        }
    }

    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        if (this.m_vendorId == null) {
            this.m_vendorId = new VendorId();
        }
        super.deserialize(impl, message, name);
        this.m_vendorId.deserialize(impl, message, name);
    }

     @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        // Do nothing
    }

}
