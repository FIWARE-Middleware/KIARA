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

import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;

public class ParameterKey extends Parameter {
	
	private InstanceHandle m_instanceHandle;

	public ParameterKey(InstanceHandle instanceHandle) {
		super(ParameterId.PID_KEY_HASH, instanceHandle.getSerializedSize());
		this.m_instanceHandle = instanceHandle;
	}
	
	@Override
	public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
		super.serialize(impl, message, name);
		this.m_instanceHandle.serialize(impl, message, name);
	}
	
	@Override
	public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
		super.deserialize(impl, message, name);
		this.m_instanceHandle.deserialize(impl, message, name);
	}
	
	@Override
	public void deserializeContent(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
		this.m_instanceHandle.deserialize(impl, message, name);
	}
	
	@Override
	public short getSerializedSize() {
		return (short) (super.getSerializedSize() + this.m_length);
	}

	 

}
