package org.fiware.kiara.ps.rtps.messages.elements.parameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.util.Pair;

public class ParameterPropertyList extends Parameter {
    
    private List<Pair<String, String>> m_properties;
    
    private final Lock m_mutex = new ReentrantLock(true);

    public ParameterPropertyList() {
        super(ParameterId.PID_PROPERTY_LIST, (short) 0);
        this.m_properties = new ArrayList<Pair<String, String>>();
    }
    
    public void addProperty(Pair<String, String> property) {
        this.m_mutex.lock();
        try {
            this.m_properties.add(property);
        } finally {
            this.m_mutex.unlock();
        }
    }
    
    public List<Pair<String, String>> getProperties() {
        return this.m_properties;
    }

    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        this.m_mutex.lock();
        try {
            this.m_length = 0;
            for (Pair<String, String> pair : this.m_properties) {
                this.m_length += (4 + pair.getFirst().length() + 4 + pair.getSecond().length());
            }
            int offset = (this.m_length % 4 == 0) ? (0) : (4 - this.m_length % 4);
            this.m_length += offset + 4;
            super.serialize(impl, message, name);
            impl.serializeUI32(message, name, this.m_properties.size());
            for (Pair<String, String> pair : this.m_properties) {
                impl.serializeString(message, name, pair.getFirst());
                impl.serializeString(message, name, pair.getSecond());
            }
        } finally {
            this.m_mutex.unlock();
        }
    }

    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.m_mutex.lock();
        try {
            super.deserialize(impl, message, name);
            int length = impl.deserializeUI32(message, name);
            if (this.m_length > 0) {
                for (int i=0; i < length; ++i) {
                    String first = impl.deserializeString(message, name);
                    String second = impl.deserializeString(message, name);
                    this.m_properties.add(new Pair<String, String>(first, second));
                }
           }
        } finally {
            this.m_mutex.unlock();
        }
    }
    
    @Override
    public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        this.m_mutex.lock();
        try {
            int length = impl.deserializeUI32(message, name);
            for (int i=0; i < length; ++i) {
                String first = impl.deserializeString(message, name);
                String second = impl.deserializeString(message, name);
                this.m_properties.add(new Pair<String, String>(first, second));
            }
        } finally {
            this.m_mutex.unlock();
        }
    }
    
    public void copy(ParameterPropertyList other) {
        this.m_mutex.lock();
        try {
            this.m_properties.clear();
            this.m_properties.addAll(other.m_properties);
        } finally {
            this.m_mutex.unlock();
        }
    }

    public ParameterPropertyList getPropertyList() {
        ParameterPropertyList pProp = new ParameterPropertyList();
        this.m_mutex.lock();
        try {
            for (Pair<String, String> pair : this.m_properties) {
                pProp.addProperty(pair);
            }
        } finally {
            this.m_mutex.unlock();
        }
        return pProp;
    }
    
    public Lock getMutex() {
        return this.m_mutex;
    }
    


}
