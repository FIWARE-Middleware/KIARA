package org.fiware.kiara.ps.qos;

import java.io.IOException;
import java.util.List;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.RTPSMessage;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

import com.eprosima.idl.util.Pair;

public class ParameterPropertyList extends Parameter {
    
    public List<Pair<String, String>> m_properties;
    
    public ParameterPropertyList() {
        super(ParameterId.PID_PROPERTY_LIST, (short) 0);
    }
    
    public ParameterPropertyList(ParameterId pid, short inLength) {
        super(pid, inLength);
    }
    
    public boolean addToRTPSMessage(RTPSMessage msg) {
        return true;
    }

    @Override
    public void deserializeContent(SerializerImpl impl,
            BinaryInputStream message, String name) throws IOException {
        // TODO Auto-generated method stub
        
    }

}
