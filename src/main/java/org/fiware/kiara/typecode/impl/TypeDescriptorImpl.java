package org.fiware.kiara.typecode.impl;

import java.util.ArrayList;

import org.fiware.kiara.typecode.TypeDescriptor;
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.impl.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.impl.data.ExceptionTypeDescriptor;
import org.fiware.kiara.typecode.impl.services.FunctionTypeDescriptor;

public class TypeDescriptorImpl implements TypeDescriptor {

    protected TypeKind m_kind = null;
    protected String m_name;
    
    /*
     * Public Methods
     */
    
    protected TypeDescriptorImpl(TypeKind kind, String name) {
        this.m_kind = kind;
        this.m_name = name;
    }
    
    @Override
    public boolean isData() {
        return false;
    }
    
    @Override
    public TypeKind getKind() {
        return this.m_kind;
    }
    
    @Override
    public String getName() {
        return this.m_name;
    }
    
    @Override
    public boolean isPrimitive() {
        return false;
    }
    
    @Override
    public boolean isString() {
        return false;
    }
    
    @Override
    public boolean isConstructed() {
        return false;
    }
    
    @Override
    public boolean isContainer() {
        return false;
    }
    
    @Override
    public boolean isArray() {
        return false;
    }
    
    @Override
    public boolean isSequence() {
        return false;
    }
    
    @Override
    public boolean isMap() {
        return false;
    }
    
    @Override
    public boolean isSet() {
        return false;
    }
    
    @Override
    public void setMaxSize(int size) {
        
    }
    
    @Override
    public int getMaxSize() {
        return 1;
    }
    
    @Override
    public boolean isMembered() {
        return false;
    }
    
    @Override
    public boolean isEnum() {
        return false;
    }
    
    @Override
    public boolean isUnion() {
        return false;
    }
    
    @Override
    public boolean isStruct() {
        return false;
    }
    
    @Override
    public boolean isException() {
        return false;
    }
    
    // ---------------------------- Services ----------------------------
    
    @Override
    public boolean isService() {
        return false;
    }
    
    @Override
    public ArrayList<FunctionTypeDescriptor> getFunctions() {
        return null;
    }
    
    // --------------------------- Functions ----------------------------
    
    @Override
    public boolean isFunction() {
        return false;
    }
    
    @Override
    public DataTypeDescriptor getReturnType() {
        return null;
    }
    
    @Override
    public ArrayList<DataTypeDescriptor> getParameters() {
        return null;
    }
    
    @Override
    public ArrayList<ExceptionTypeDescriptor> getExceptions() {
        return null;
    }
    
    // -----------------------------------------------------------------
    
    @Override
    public void setMaxFixedLength(int length) {}
    
    @Override
    public int getMaxFixedLength(){return -1;}
    
    @Override
    public void addMembers(DataTypeDescriptor... members){}
    
    @Override
    public ArrayList<DataTypeDescriptor> getMembers(){return null;}
    
    @Override
    public boolean setContentType(DataTypeDescriptor contentType){return false;}
    
    @Override
    public DataTypeDescriptor getContentType(){return null;}
    
    @Override
    public boolean setKeyTypeDescriptor(DataTypeDescriptor keyDescriptor) {return false;}
    
    @Override
    public boolean setValueTypeDescriptor(DataTypeDescriptor valueDescriptor) {return false;}
    
}
