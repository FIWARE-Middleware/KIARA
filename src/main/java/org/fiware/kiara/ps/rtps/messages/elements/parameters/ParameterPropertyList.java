package org.fiware.kiara.ps.rtps.messages.elements.parameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.util.Pair;

public class ParameterPropertyList extends Parameter {
    
    private List<Pair<String, String>> m_properties;

    public ParameterPropertyList() {
        super(ParameterId.PID_PROPERTY_LIST, (short) 0);
        this.m_properties = new ArrayList<Pair<String, String>>();
    }
    
    public void addProperty(Pair<String, String> property) {
        this.m_properties.add(property);
    }
    
    public List<Pair<String, String>> getProperties() {
        return this.m_properties;
    }

    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        for (Pair<String, String> pair : this.m_properties) {
            this.m_length += (4 + pair.getFirst().length());
        }
        super.serialize(impl, message, name);
        for (Pair<String, String> pair : this.m_properties) {
            impl.serializeString(message, name, pair.getFirst());
            impl.serializeString(message, name, pair.getSecond());
        }
    }

    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        super.deserialize(impl, message, name);
        if (this.m_length > 0) {
            int readBytes = 0;
            while (readBytes < this.m_length) {
                String first = impl.deserializeString(message, name);
                String second = impl.deserializeString(message, name);
                readBytes += (4 + first.length());
                readBytes += (4 + second.length());
                this.m_properties.add(new Pair<String, String>(first, second));
            }
        }
    }
    
    public void copy(ParameterPropertyList other) {
        this.m_properties.clear();
        this.m_properties.addAll(other.m_properties);
    }

    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        // Do nothing
    }

    public ParameterPropertyList getPropertyList() {
        ParameterPropertyList pProp = new ParameterPropertyList();
        for (Pair<String, String> pair : this.m_properties) {
            pProp.addProperty(pair);
        }
        return null;
    }

}
