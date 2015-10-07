/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2015 Proyectos y Sistemas de Mantenimiento S.L. (eProsima)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
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

/**
 * Property List RTPS DATA parameter
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public class ParameterPropertyList extends Parameter {
    
    /**
     * {@link Parameter} value
     */
    private List<Pair<String, String>> m_properties;
    
    /**
     * Mutex
     */
    private final Lock m_mutex = new ReentrantLock(true);

    /**
     * Default {@link ParameterPropertyList} constructor
     */
    public ParameterPropertyList() {
        super(ParameterId.PID_PROPERTY_LIST, (short) 0);
        this.m_properties = new ArrayList<Pair<String, String>>();
    }
    
    /**
     * Adds a new property to the {@link ParameterPropertyList}
     * 
     * @param property The new property to be added
     */
    public void addProperty(Pair<String, String> property) {
        this.m_mutex.lock();
        try {
            this.m_properties.add(property);
        } finally {
            this.m_mutex.unlock();
        }
    }
    
    /**
     * Get the properties list
     * 
     * @return The list of properties of the {@link ParameterPropertyList}
     */
    public List<Pair<String, String>> getProperties() {
        return this.m_properties;
    }

    /**
     * Serializes a {@link ParameterPropertyList} object and its inherited attributes
     */
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

    /**
     * Deserializes a {@link ParameterPropertyList} object and its inherited attributes
     */
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
    
    /**
     * Deserializes a {@link ParameterPropertyList} object and not its inherited attributes
     */
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
    
    /**
     * This methos copies a {@link ParameterPropertyList} into another
     * 
     * @param other The {@link ParameterPropertyList} to be copied
     */
    public void copy(ParameterPropertyList other) {
        this.m_mutex.lock();
        try {
            this.m_properties.clear();
            this.m_properties.addAll(other.m_properties);
        } finally {
            this.m_mutex.unlock();
        }
    }

    /**
     * Gets a new {@link ParameterPropertyList} with the properties already added
     * @return The {@link ParameterPropertyList}
     */
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
    
    /**
     * Get the Mutex
     * 
     * @return {@link Lock} mutex
     */
    public Lock getMutex() {
        return this.m_mutex;
    }
    


}
