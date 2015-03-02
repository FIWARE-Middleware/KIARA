package org.fiware.kiara.typecode.impl.services;

import java.util.ArrayList;

import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.impl.TypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.DataTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.ExceptionTypeDescriptorImpl;
import org.fiware.kiara.typecode.services.FunctionTypeDescriptor;

public class FunctionTypeDescriptorImpl extends TypeDescriptorImpl implements FunctionTypeDescriptor {
    
    private DataTypeDescriptor m_returnDescriptor;
    private ArrayList<DataTypeDescriptor> m_parametersDescriptors;
    private ArrayList<ExceptionTypeDescriptorImpl> m_exceptionsDescriptors;

    protected FunctionTypeDescriptorImpl() {
        super(TypeKind.FUNCTION_TYPE, "function");
    }
    
    protected FunctionTypeDescriptorImpl(
            DataTypeDescriptor returnDescriptor, 
            ArrayList<DataTypeDescriptor> parametersDescriptors, 
            ArrayList<ExceptionTypeDescriptorImpl> exceptionsDescriptors) {
        super(TypeKind.FUNCTION_TYPE, "function");
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
    public ArrayList<ExceptionTypeDescriptorImpl> getExceptions() {
        return this.m_exceptionsDescriptors;
    }

}
