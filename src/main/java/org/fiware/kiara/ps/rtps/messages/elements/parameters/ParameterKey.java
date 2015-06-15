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
