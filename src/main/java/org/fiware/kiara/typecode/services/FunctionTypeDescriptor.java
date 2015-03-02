package org.fiware.kiara.typecode.services;

import java.util.ArrayList;

import org.fiware.kiara.typecode.TypeDescriptor;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.impl.data.DataTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.ExceptionTypeDescriptorImpl;

public interface FunctionTypeDescriptor extends TypeDescriptor {
    
    public DataTypeDescriptor getReturnType();
    
    public ArrayList<DataTypeDescriptor> getParameters();
    
    public ArrayList<ExceptionTypeDescriptorImpl> getExceptions();

}
