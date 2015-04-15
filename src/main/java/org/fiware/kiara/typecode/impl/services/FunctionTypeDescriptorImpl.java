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
import java.util.List;

import org.fiware.kiara.exceptions.TypeDescriptorException;
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.data.ExceptionTypeDescriptor;
import org.fiware.kiara.typecode.data.Member;
import org.fiware.kiara.typecode.impl.FunctionTypeDescriptor;
import org.fiware.kiara.typecode.impl.TypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.MemberImpl;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*
*/

public class FunctionTypeDescriptorImpl extends TypeDescriptorImpl implements FunctionTypeDescriptor {
    
    private DataTypeDescriptor m_returnDescriptor;
    private ArrayList<Member> m_parametersDescriptors;
    //private ArrayList<String> m_parametersNames;
    private ArrayList<ExceptionTypeDescriptor> m_exceptionsDescriptors;
    private String m_name;
    private String m_ServiceName;

    public FunctionTypeDescriptorImpl(String name) {
        super(TypeKind.FUNCTION_TYPE);
        if (name == null) {
            throw new TypeDescriptorException("FunctionTypeDescriptorImpl - Trying to specify a null value as function name.");
        } else if (name.length() == 0) {
            throw new TypeDescriptorException("FunctionTypeDescriptorImpl - Trying to specify an empty value as function name.");
        }
        this.m_name = name;
        this.m_parametersDescriptors = new ArrayList<Member>();
        //this.m_parametersNames = new ArrayList<String>();
        this.m_exceptionsDescriptors = new ArrayList<ExceptionTypeDescriptor>();
    }
    
    /*public FunctionTypeDescriptorImpl(String name, String serviceName) {
        super(TypeKind.FUNCTION_TYPE);
        if (name == null) {
            throw new TypeDescriptorException("FunctionTypeDescriptorImpl - Trying to specify a null value as function name.");
        } else if (name.length() == 0) {
            throw new TypeDescriptorException("FunctionTypeDescriptorImpl - Trying to specify an empty value as function name.");
        }
        this.m_name = name;
        this.m_ServiceName = serviceName;
        this.m_parametersDescriptors = new ArrayList<Member>();
        //this.m_parametersNames = new ArrayList<String>();
        this.m_exceptionsDescriptors = new ArrayList<ExceptionTypeDescriptor>();
    }*/
    
    /*public FunctionTypeDescriptorImpl(
            String name,
            DataTypeDescriptor returnDescriptor, 
            ArrayList<Member> parametersDescriptors, 
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
    }*/
    
    @Override
    public boolean isFunction() {
        return true;
    }
    
    @Override
    public DataTypeDescriptor getReturnType() {
        return this.m_returnDescriptor;
    }
    
    @Override
    public DataTypeDescriptor getParameter(String name) {
        for (Member m : this.m_parametersDescriptors) {
            if (m.getName().equals(name)) {
                return m.getTypeDescriptor();
            }
        }
        return null;
    }

    @Override
    public ExceptionTypeDescriptor getException(String name) {
        for (ExceptionTypeDescriptor ex : this.m_exceptionsDescriptors) {
            if (ex.getName().equals(name)) {
                return ex;
            }
        }
        return null;
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
        if (exists(name)) {
            throw new TypeDescriptorException("FunctionTypeDescriptorImpl - Trying to add a duplicated parameter name.");
        } 
        this.m_parametersDescriptors.add(new MemberImpl(parameter, name));
        //this.m_parametersNames.add(name);
    }
    
    private boolean exists(String parameterName) {
        for (Member m : this.m_parametersDescriptors) {
            if (parameterName.equals(m.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addException(ExceptionTypeDescriptor exception) {
        if (exception == null) {
            throw new TypeDescriptorException("FunctionTypeDescriptorImpl - Trying to add a null value as a throwable exception.");
        }
        this.m_exceptionsDescriptors.add(exception);
    }
    
    @Override
    public String getName() {
        return this.m_name;
    }
    
    @Override
    public String getServiceName() {
       return this.m_ServiceName;
    }

    @Override
    public FunctionTypeDescriptor setServiceName(String serviceName) {
        this.m_ServiceName = serviceName;
        return this;
    }
    
    public ArrayList<ExceptionTypeDescriptor> getExceptions() {
        return this.m_exceptionsDescriptors;
    }

    public List<Member> getParameters() {
        /*ArrayList<DataTypeDescriptor> parameters = new ArrayList<DataTypeDescriptor>();
        for(Member m : this.m_parametersDescriptors) {
            parameters.add(m.getTypeDescriptor());
        }*/
        return this.m_parametersDescriptors;
    }


    

}
