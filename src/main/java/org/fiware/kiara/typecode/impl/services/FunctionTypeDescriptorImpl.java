/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2014 Proyectos y Sistemas de Mantenimiento S.L. (eProsima)
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
package org.fiware.kiara.typecode.impl.services;

import java.util.ArrayList;

import org.fiware.kiara.exceptions.TypeDescriptorException;
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.data.ExceptionTypeDescriptor;
import org.fiware.kiara.typecode.impl.TypeDescriptorImpl;
import org.fiware.kiara.typecode.services.FunctionTypeDescriptor;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*
*/

public class FunctionTypeDescriptorImpl extends TypeDescriptorImpl implements FunctionTypeDescriptor {
    
    private DataTypeDescriptor m_returnDescriptor;
    private ArrayList<DataTypeDescriptor> m_parametersDescriptors;
    private ArrayList<ExceptionTypeDescriptor> m_exceptionsDescriptors;
    @SuppressWarnings("unused")
    private String m_name;

    public FunctionTypeDescriptorImpl(String name) {
        super(TypeKind.FUNCTION_TYPE);
        if (name == null) {
            throw new TypeDescriptorException("FunctionTypeDescriptorImpl - Trying to specify a null value as function name.");
        } else if (name.length() == 0) {
            throw new TypeDescriptorException("FunctionTypeDescriptorImpl - Trying to specify an empty value as function name.");
        }
        this.m_name = name;
        this.m_parametersDescriptors = new ArrayList<DataTypeDescriptor>();
        this.m_exceptionsDescriptors = new ArrayList<ExceptionTypeDescriptor>();
    }
    
    public FunctionTypeDescriptorImpl(
            String name,
            DataTypeDescriptor returnDescriptor, 
            ArrayList<DataTypeDescriptor> parametersDescriptors, 
            ArrayList<ExceptionTypeDescriptor> exceptionsDescriptors) {
        super(TypeKind.FUNCTION_TYPE);
        if (name == null) {
            throw new TypeDescriptorException("FunctionTypeDescriptorImpl - Trying to specify a null value as function name.");
        } else if (name.length() == 0) {
            throw new TypeDescriptorException("FunctionTypeDescriptorImpl - Trying to specify an empty value as function name.");
        }
        this.m_name = name;
        this.m_returnDescriptor = returnDescriptor;
        this.m_parametersDescriptors = parametersDescriptors;
        this.m_exceptionsDescriptors = exceptionsDescriptors;
    }
    
    @Override
    public boolean isFunction() {
        return true;
    }
    
    @Override
    public DataTypeDescriptor getReturnType() {
        return this.m_returnDescriptor;
    }
    
    @Override
    public ArrayList<DataTypeDescriptor> getParameters() {
        return this.m_parametersDescriptors;
    }
    
    @Override
    public ArrayList<ExceptionTypeDescriptor> getExceptions() {
        return this.m_exceptionsDescriptors;
    }

    @Override
    public void setReturnType(DataTypeDescriptor returnType) {
        if (this.m_returnDescriptor == null) {
            this.m_returnDescriptor = returnType;
        } else {
            throw new TypeDescriptorException("FunctionTypeDescriptorImpl - A return type has already been defined for this function.");
        }
    }

    @Override
    public void addParameter(DataTypeDescriptor parameter, String name) {
        if (parameter == null) {
            throw new TypeDescriptorException("FunctionTypeDescriptorImpl - Trying to add a null value as a parameter for the function.");
        } else if (name == null) {
            throw new TypeDescriptorException("FunctionTypeDescriptorImpl - Trying to specify a null value as function name.");
        } else if (name.length() == 0) {
            throw new TypeDescriptorException("FunctionTypeDescriptorImpl - Trying to specify an empty value as function name.");
        }
        this.m_parametersDescriptors.add(parameter);
    }

    @Override
    public void addException(ExceptionTypeDescriptor exception) {
        if (exception == null) {
            throw new TypeDescriptorException("FunctionTypeDescriptorImpl - Trying to add a null value as a throwable exception.");
        }
        this.m_exceptionsDescriptors.add(exception);
    }

}
