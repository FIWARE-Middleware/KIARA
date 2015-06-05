package org.fiware.kiara.ps.qos.policies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class TopicDataQosPolicy extends Parameter {

    // TODO

    public QosPolicy parent;
    
    private List<Byte> m_value;
    
    public TopicDataQosPolicy() {
        super(ParameterId.PID_TOPIC_DATA, (short) 0);
        this.parent = new QosPolicy(false);
        this.m_value = new ArrayList<Byte>();
    }
    
    public void pushBack(byte b) {
        this.m_value.add(b);
    }
    
    public void clear() {
        this.m_value.clear();
    }
    
    public void setValue(List<Byte> value) {
        this.m_value = value;
    }
    
    public List<Byte> getValue() {
        return this.m_value;
    }

    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        // Do nothing
        
    }
    

}
