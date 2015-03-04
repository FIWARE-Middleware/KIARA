package org.fiware.kiara.typecode.services;

import java.util.ArrayList;

import org.fiware.kiara.typecode.TypeDescriptor;

public interface ServiceTypeDescriptor extends TypeDescriptor {

    public ArrayList<FunctionTypeDescriptor> getFunctions();
    
    public void addFunction(FunctionTypeDescriptor funcionTypeDescriptor);
    
}
