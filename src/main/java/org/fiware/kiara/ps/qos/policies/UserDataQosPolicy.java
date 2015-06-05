package org.fiware.kiara.ps.qos.policies;

import java.io.IOException;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class UserDataQosPolicy extends Parameter {

    // TODO
    
    public QosPolicy parent;
    
    private byte[] dataBuf;
    
    public UserDataQosPolicy() {
        super(ParameterId.PID_USER_DATA, (short) 0);
        this.parent = new QosPolicy(false);
    }
    
    public byte[] getDataBuf() {
        return this.dataBuf;
    }
    
    public void setDataBuf(byte[] buf) {
        this.dataBuf = buf;
    }

    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        // Do nothing
    }

}
