package org.fiware.kiara.dynamic.impl.services;

import java.util.ArrayList;

//import org.fiware.kiara.dynamic.impl.DynamicTypeImpl;
import org.fiware.kiara.dynamic.impl.data.DynamicDataImpl;
import org.fiware.kiara.typecode.impl.data.DataTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.data.ExceptionTypeDescriptorImpl;
import org.fiware.kiara.typecode.impl.services.FunctionTypeDescriptorImpl;


public class DynamicFunction /*extends DynamicTypeImpl*/ {
    
    public DynamicFunction(FunctionTypeDescriptorImpl functionTypeDescriptor) {
        //super(functionTypeDescriptor);
        
        /*DataTypeDescriptor returnType = functionTypeDescriptor.getReturnType();
        this.m_expectedKeys.add(returnType.getName());
        
        ArrayList<DataTypeDescriptor> parameters = functionTypeDescriptor.getParameters();
        for (DataTypeDescriptor desc : parameters) {
            this.m_expectedKeys.add(desc.getName());
        }
        
        ArrayList<ExceptionTypeDescriptor> exceptions = functionTypeDescriptor.getExceptions();
        for (ExceptionTypeDescriptor desc : exceptions) {
            this.m_expectedKeys.add(desc.getName());
        }*/
    }

    /*
    @Override
    public boolean set(Object value) {
        if (!this.m_expectedKeys.contains(name)) {
            if (!this.m_values.containsKey(name)) {
                this.m_values.put(name, value);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean set(DynamicDataType value) {
        return this.set(value);
    }*/

}
