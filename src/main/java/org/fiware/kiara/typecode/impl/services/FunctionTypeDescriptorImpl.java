package org.fiware.kiara.typecode.impl.services;

import java.util.ArrayList;

import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.data.ExceptionTypeDescriptor;
import org.fiware.kiara.typecode.impl.TypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.DataTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.ExceptionTypeDescriptorImpl;
import org.fiware.kiara.typecode.services.FunctionTypeDescriptor;

public class FunctionTypeDescriptorImpl extends TypeDescriptorImpl implements FunctionTypeDescriptor {
    
    private DataTypeDescriptor m_returnDescriptor;
    private ArrayList<DataTypeDescriptor> m_parametersDescriptors;
    private ArrayList<ExceptionTypeDescriptor> m_exceptionsDescriptors;
    private String m_name;

    public FunctionTypeDescriptorImpl(String name) {
        super(TypeKind.FUNCTION_TYPE);
        this.m_name = name;
    }
    
    public FunctionTypeDescriptorImpl(
            String name,
            DataTypeDescriptor returnDescriptor, 
            ArrayList<DataTypeDescriptor> parametersDescriptors, 
            ArrayList<ExceptionTypeDescriptor> exceptionsDescriptors) {
        super(TypeKind.FUNCTION_TYPE);
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
        this.m_returnDescriptor = returnType;
    }

    @Override
    public void addParameter(DataTypeDescriptor parameter, String name) {
        this.m_parametersDescriptors.add(parameter);
    }

    @Override
    public void addException(ExceptionTypeDescriptor exception) {
        this.m_exceptionsDescriptors.add(exception);
    }

}
