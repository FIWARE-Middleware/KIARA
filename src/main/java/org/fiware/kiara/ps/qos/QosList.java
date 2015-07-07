package org.fiware.kiara.ps.qos;

import java.util.List;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.messages.elements.EntityId;
import org.fiware.kiara.ps.rtps.messages.elements.ParameterList;
import org.fiware.kiara.ps.rtps.messages.elements.ProtocolVersion;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;
import org.fiware.kiara.ps.rtps.messages.elements.VendorId;

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
    
    public boolean addQos(ParameterId pid, Locator loc) {
        // TODO Implement
        return true;
    }
    
    public boolean addQos(ParameterId pid, int uint) {
        // TODO Implement
        return true;
    }
    
    public boolean addQos(ParameterId pid, ProtocolVersion protocol) {
        // TODO Implement
        return true;
    }
    
    public boolean addQos(ParameterId pid, VendorId vendor) {
        // TODO Implement
        return true;
    }
    
    public boolean addQos(ParameterId pid, byte o1, byte o2, byte o3, byte o4) {
        // TODO Implement
        return true;
    }
    
    public boolean addQos(ParameterId pid, EntityId entityId) {
        // TODO Implement
        return true;
    }
    
    public boolean addQos(ParameterId pid, Timestamp timestamp) {
        // TODO Implement
        return true;
    }
    
    public boolean addQos(ParameterId pid, boolean inBool) {
        // TODO Implement
        return true;
    }
    
    public boolean addQos(ParameterId pid, String str1, String str2) {
        // TODO Implement
        return true;
    }
    
    public boolean addQos(ParameterId pid, List<Byte> ocVec) {
        // TODO Implement
        return true;
    }
    
    public boolean addQos(ParameterId pid, ParameterPropertyList list) {
        // TODO Implement
        return true;
    }

    public ParameterList getAllQos() {
        return this.m_allQos;
    }

    public ParameterList getInlineQos() {
        return this.m_inlineQos;
    }

}
