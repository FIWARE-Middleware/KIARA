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
package org.fiware.kiara.ps.rtps.messages.elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.RTPSSubmessageElement;
import org.fiware.kiara.ps.rtps.messages.elements.parameters.ParameterBuilder;
import org.fiware.kiara.ps.rtps.messages.elements.parameters.ParameterSentinel;

/**
 * This class contains a list of Parameter references.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class ParameterList extends RTPSSubmessageElement {

    /**
     * The {@link Parameter} list
     */
    private List<Parameter> m_parameters;
    
    /**
     * boolean value indicating if the list has changed
     */
    private boolean m_hasChanged;
    
    /**
     * Total size of all the Parameter references
     */
    private int m_totalBytes;

    /**
     * Default constructor
     */
    public ParameterList() {
        this.m_hasChanged = true;
        this.m_parameters = new ArrayList<Parameter>();
    }
    
    /**
     * Get the change status of the ParameterList
     * 
     * @return true if the ParameterList has changed; false otherwise
     */
    public boolean getHasChanged() {
        return this.m_hasChanged;
    }

    /**
     * Set the change status of the ParameterList
     * 
     * @param hasChanged The value to be set
     */
    public void setHasChanged(boolean hasChanged) {
        this.m_hasChanged = hasChanged;
    }

    /**
     * Get the ParameterList size
     * 
     * @return The ParameterList size
     */
    public int getListSize() {
        return this.m_totalBytes;
    }

    /**
     * Get the number of Parameter references in the ParameterList
     * 
     * @return The number of references in the ParameterList
     */
    public int getListLength() {
        return this.m_parameters.size();
    }

    /**
     * Adds a new Parameter into the ParameterList
     * 
     * @param parameter The Parameter to be added
     */
    public void addParameter(Parameter parameter) {
        this.m_parameters.add(parameter);
    }

    /**
     * Get the ParameterList
     * 
     * @return The ParameterList
     */
    public List<Parameter> getParameters() {
        return this.m_parameters;
    }

    /**
     * Adds the last Parameter of the ParameterList (a Sentinel)
     * 
     * @return true if the Sentinel has been added; false otherwise
     */
    public boolean addSentinel() {
        this.m_parameters.add(new ParameterSentinel());
        this.m_hasChanged = false;
        return true;
    }

    /**
     * Get the ParameterList serialized size
     */
    @Override
    public short getSerializedSize() {
        short retVal = 0;
        for (Parameter p : this.m_parameters) {
            retVal = (short) (retVal + p.getSerializedSize());
        }
        return retVal;
    }

    /**
     * Serializes a ParameterList object
     */
    @Override
    public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
        for (Parameter p : this.m_parameters) {
            if (p!= null) {
                int prevPos = message.getPosition();
                p.serialize(impl, message, name);
                int lastPos = message.getPosition();
                int serializedSize = lastPos - prevPos - 4;
                int bytesToSkip = (serializedSize % 4 == 0) ? 0 : 4 - (serializedSize % 4);

                for (int i=0; i < bytesToSkip; ++i) {
                    impl.serializeByte(message, name, (byte) 0);
                }
            }
        }
    }

    /**
     * Deserializes a ParameterList object
     */
    @Override
    public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
        boolean isSentinel = false;
        while (!isSentinel) {
            ParameterId pid = ParameterId.createFromValue(impl.deserializeI16(message, name));
            short length = impl.deserializeI16(message, name);
            Parameter param = ParameterBuilder.createParameter(pid, length);
            if (param != null) {
                int initialPos = message.getPosition();
                param.deserializeContent(impl, message, name);
                int finalPos = message.getPosition();
                int deserializedBytes = finalPos - initialPos;
                int bytesToSkip = (deserializedBytes % 4 == 0) ? 0 : 4 - (deserializedBytes % 4);/*4 - (message.getPosition() % 4);*/
                message.skipBytes(bytesToSkip);
                if (param.getParameterId() == ParameterId.PID_SENTINEL) {
                    isSentinel = true;
                }
                this.m_parameters.add(param);
                this.m_totalBytes += param.getSerializedSize();
                this.m_hasChanged = true;
            }
        }
    }

    /**
     * Removes all the Parameter references in the list
     */
    public void deleteParams() {
        resetList();
    }

    /**
     * Resets the list
     */
    public void resetList() {
        this.m_parameters.clear();
        this.m_hasChanged = true;
    }

}
