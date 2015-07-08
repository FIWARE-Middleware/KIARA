package org.fiware.kiara.ps.qos;

import java.io.IOException;
import java.util.List;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.messages.elements.EntityId;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.ps.rtps.messages.elements.ParameterList;
import org.fiware.kiara.ps.rtps.messages.elements.ProtocolVersion;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;
import org.fiware.kiara.ps.rtps.messages.elements.VendorId;
import org.fiware.kiara.ps.rtps.messages.elements.parameters.ParameterBuilder;
import org.fiware.kiara.ps.rtps.messages.elements.parameters.ParameterString;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;

public class QosList {
    
    private ParameterList m_allQos;
    
    private ParameterList m_inlineQos;
    
    public QosList() {
        this.m_allQos = new ParameterList();
        this.m_inlineQos = new ParameterList();
    }
    
    public boolean addQos(ParameterId pid, String string) {
        if (string.length() == 0) {
            return false;
        }
        if (pid == ParameterId.PID_TOPIC_NAME || pid == ParameterId.PID_TYPE_NAME || pid == ParameterId.PID_ENTITY_NAME) {
            ParameterString param = (ParameterString) ParameterBuilder.createParameter(pid, (short) 0);
            param.setContent(string);
            this.m_allQos.addParameter(param);
            this.m_allQos.setHasChanged(true);
            if (pid == ParameterId.PID_TOPIC_NAME) {
                this.m_inlineQos.addParameter(param);
                this.m_inlineQos.setHasChanged(true);
            }
            return true;
        }
        return false;
    }
    
    public boolean addQos(ParameterId pid, Locator loc) {
        if (pid == ParameterId.PID_UNICAST_LOCATOR || pid == ParameterId.PID_MULTICAST_LOCATOR ||
                pid == ParameterId.PID_DEFAULT_UNICAST_LOCATOR || pid == ParameterId.PID_DEFAULT_MULTICAST_LOCATOR ||
                pid == ParameterId.PID_METATRAFFIC_UNICAST_LOCATOR || pid == ParameterId.PID_METATRAFFIC_MULTICAST_LOCATOR)
        {
            /*ParameterLocator param = ParameterBuilder.createParameter(pid, (short) 0);
            p->Pid = pid;
            p->locator = loc;
            p->length = PARAMETER_LOCATOR_LENGTH;
            qos->allQos.m_parameters.push_back((Parameter_t*)p);
            qos->allQos.m_hasChanged = true;*/
            return true;
        }

        return false;
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
