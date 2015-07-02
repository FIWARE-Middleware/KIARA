package org.fiware.kiara.ps.qos;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.ParameterList;

public class QosList {
    
    private ParameterList m_allQos;
    
    private ParameterList m_inlineQos;
    
    public QosList() {
        this.m_allQos = new ParameterList();
        this.m_inlineQos = new ParameterList();
    }
    
    public boolean addQos(ParameterId pid, String string) {
        // TODO Implement
        return true;
    }

    public ParameterList getAllQos() {
        return this.m_allQos;
    }

}
