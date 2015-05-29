package org.fiware.kiara.ps.rtps.messages.elements;

import java.io.IOException;
import java.util.ArrayList;

import org.fiware.kiara.serialization.impl.BinaryInputStream;
import org.fiware.kiara.serialization.impl.BinaryOutputStream;
import org.fiware.kiara.serialization.impl.SerializerImpl;
import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.rtps.messages.RTPSSubmessageElement;
import org.fiware.kiara.ps.rtps.messages.elements.parameters.ParameterBuilder;

public class ParameterList extends RTPSSubmessageElement {
	
	private ArrayList<Parameter> m_parameters;
	private boolean m_hasChanged;
	private int m_totalBytes;
	
	public ParameterList() {
		this.m_hasChanged = true;
		this.m_parameters = new ArrayList<Parameter>();
	}
	
	/*public ParameterList(int totalBytes) {
		this.m_hasChanged = true;
		this.m_parameters = new ArrayList<Parameter>();
		this.m_totalBytes = totalBytes;
	}*/
	
	public boolean getHasChanged() {
		return this.m_hasChanged;
	}
	
	public int getListSize() {
		return this.m_totalBytes;
	}
	
	public void addParameter(Parameter parameter) {
		this.m_parameters.add(parameter);
	}

	/*@Override
	public void serialize(CDRSerializer ser, BinaryOutputStream bos) {
		
	}*/

	@Override
	public short getSize() {
		short retVal = 0;
		for (Parameter p : this.m_parameters) {
			retVal = (short) (retVal + p.getSize());
		}
		return retVal;
	}

	@Override
	public void serialize(SerializerImpl impl, BinaryOutputStream message, String name) throws IOException {
		for (Parameter p : this.m_parameters) {
			p.serialize(impl, message, name);
		}
	}

	@Override
	public void deserialize(SerializerImpl impl, BinaryInputStream message, String name) throws IOException {
		//int paramlistByteSize = 0;
		boolean isSentinel = false;
		//boolean valid = true;
		
		while (!isSentinel) {
		
			ParameterId pid = ParameterId.createFromValue(impl.deserializeI16(message, name));
			//pid.deserialize(impl, message, name);
			short length = impl.deserializeI16(message, name);
			
			Parameter param = ParameterBuilder.createParameter(pid, length);
			param.deserializeContent(impl, message, name);
			
			if (param.getParameterId() == ParameterId.PID_SENTINEL) {
				isSentinel = true;
			}
			
			this.m_parameters.add(param);
			this.m_totalBytes += param.getSize();
			this.m_hasChanged = true;
			
			
		}
		
		
		
	}

}
