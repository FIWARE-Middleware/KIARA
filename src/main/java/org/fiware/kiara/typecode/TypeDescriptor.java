package org.fiware.kiara.typecode;

import java.util.ArrayList;

import org.fiware.kiara.typecode.impl.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.impl.data.ExceptionTypeDescriptor;
import org.fiware.kiara.typecode.impl.services.FunctionTypeDescriptor;

public interface TypeDescriptor {
    
    /*
     * Public Methods
     */
    
    public TypeKind getKind();
    
    public String getName();
    
    public boolean isData();
    
    public boolean isPrimitive();
    
    public boolean isString();
    
    // ---------------------- Constructed -----------------------
    
    public boolean isConstructed();
    
    public boolean isContainer();
    
    public boolean isArray();
    
    public boolean isSequence();
    
    public boolean isMap();
    
    public boolean isSet();
    
    public void setMaxSize(int length);
    
    public int getMaxSize();
    
    // ----------------------- Membered -----------------------
    
    public boolean isMembered();
    
    public boolean isEnum();
    
    public boolean isUnion();
    
    public boolean isStruct();
    
    public boolean isException();
    
    // --------------------- Service ------------------------------
    
    public boolean isService();
    
    public ArrayList<FunctionTypeDescriptor> getFunctions();
    
    // --------------------- Function ------------------------------
    
    public boolean isFunction();
    
    public DataTypeDescriptor getReturnType();
    
    public ArrayList<DataTypeDescriptor> getParameters();
    
    public ArrayList<ExceptionTypeDescriptor> getExceptions();
    
    // ------------------------------------------------------------
    
    public void setMaxFixedLength(int length);
    
    public int getMaxFixedLength();
    
    public void addMembers(DataTypeDescriptor... members);
    
    public ArrayList<DataTypeDescriptor> getMembers();
    
    public boolean setContentType(DataTypeDescriptor contentType);
    
    public DataTypeDescriptor getContentType();
    
    public boolean setKeyTypeDescriptor(DataTypeDescriptor keyDescriptor);
    
    public boolean setValueTypeDescriptor(DataTypeDescriptor valueDescriptor);

}
