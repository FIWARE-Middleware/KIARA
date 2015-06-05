package org.fiware.kiara.ps.qos.policies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class PartitionQosPolicy extends Parameter {
    
    // TODO

    public QosPolicy parent;
    
    private List<String> m_names;
    
    public PartitionQosPolicy() {
        super(ParameterId.PID_PARTITION, (short) 0);
        this.parent = new QosPolicy(false);
        this.m_names = new ArrayList<String>();
    }
    
    public void pushBack(String name) {
        this.m_names.add(name);
        this.parent.hasChanged = true;
    }
    
    public void clear() {
        this.m_names.clear();
    }
    
    public List<String> getNames() {
        return this.m_names;
    }
    
    public void setNames(List<String> names) {
        this.m_names = names;
    }

    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        // Do nothing
        
    }

}
