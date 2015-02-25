package org.fiware.kiara.typecode.impl.services;

import java.util.ArrayList;

import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.impl.TypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.impl.data.ExceptionTypeDescriptor;

public class FunctionTypeDescriptor extends TypeDescriptorImpl {
    
    private DataTypeDescriptor m_returnDescriptor;
    private ArrayList<DataTypeDescriptor> m_parametersDescriptors;
    private ArrayList<ExceptionTypeDescriptor> m_exceptionsDescriptors;

    protected FunctionTypeDescriptor() {
        super(TypeKind.FUNCTION_TYPE, "function");
    }
    
    protected FunctionTypeDescriptor(
            DataTypeDescriptor returnDescriptor, 
            ArrayList<DataTypeDescriptor> parametersDescriptors, 
            ArrayList<ExceptionTypeDescriptor> exceptionsDescriptors) {
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
    public ArrayList<ExceptionTypeDescriptor> getExceptions() {
        return this.m_exceptionsDescriptors;
    }

}
