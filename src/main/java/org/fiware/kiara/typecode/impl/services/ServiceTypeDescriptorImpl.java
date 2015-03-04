package org.fiware.kiara.typecode.impl.services;

import java.util.ArrayList;

import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.impl.TypeDescriptorImpl;
import org.fiware.kiara.typecode.services.FunctionTypeDescriptor;
import org.fiware.kiara.typecode.services.ServiceTypeDescriptor;

public class ServiceTypeDescriptorImpl extends TypeDescriptorImpl implements ServiceTypeDescriptor {
    
    private ArrayList<FunctionTypeDescriptor> m_functionsDescriptors;
    
    private String m_name;

    protected ServiceTypeDescriptorImpl(String name) {
        super(TypeKind.SERVICE_TYPE);
        this.m_name = name;
    }
    
    protected ServiceTypeDescriptorImpl(ArrayList<FunctionTypeDescriptor> functionsDescriptors) {
        super(TypeKind.SERVICE_TYPE);
        this.m_functionsDescriptors = functionsDescriptors;
    }
    
    @Override
    public boolean isService() {
        return false;
    }
    
    @Override
    public ArrayList<FunctionTypeDescriptor> getFunctions() {
        return (ArrayList<FunctionTypeDescriptor>) this.m_functionsDescriptors;
    }

    @Override
    public void addFunction(FunctionTypeDescriptor funcionTypeDescriptor) {
        this.m_functionsDescriptors.add(funcionTypeDescriptor);
    }

}
