package org.fiware.kiara.typecode.impl.services;

import java.util.ArrayList;

import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.impl.TypeDescriptorImpl;

public class ServiceTypeDescriptor extends TypeDescriptorImpl {
    
    private ArrayList<FunctionTypeDescriptor> m_functionsDescriptors;

    protected ServiceTypeDescriptor() {
        super(TypeKind.SERVICE_TYPE, "service");
    }
    
    protected ServiceTypeDescriptor(ArrayList<FunctionTypeDescriptor> functionsDescriptors) {
        super(TypeKind.SERVICE_TYPE, "service");
        this.m_functionsDescriptors = functionsDescriptors;
    }
    
    @Override
    public boolean isService() {
        return false;
    }
    
    @Override
    public ArrayList<FunctionTypeDescriptor> getFunctions() {
        return this.m_functionsDescriptors;
    }

}
