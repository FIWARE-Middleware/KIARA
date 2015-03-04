package org.fiware.kiara.typecode.services;

import java.util.ArrayList;

import org.fiware.kiara.typecode.TypeDescriptor;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.data.ExceptionTypeDescriptor;
import org.fiware.kiara.typecode.impl.data.DataTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.ExceptionTypeDescriptorImpl;

public interface FunctionTypeDescriptor extends TypeDescriptor {
    
    public DataTypeDescriptor getReturnType();
    
    public void setReturnType(DataTypeDescriptor returnType);
    
    public ArrayList<DataTypeDescriptor> getParameters();
    
    public void addParameter(DataTypeDescriptor parameter, String name);
    
    public ArrayList<ExceptionTypeDescriptor> getExceptions();
    
    public void addException(ExceptionTypeDescriptor exception);

}
