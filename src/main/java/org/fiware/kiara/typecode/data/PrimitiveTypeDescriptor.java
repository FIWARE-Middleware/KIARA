package org.fiware.kiara.typecode.data;

public interface PrimitiveTypeDescriptor extends DataTypeDescriptor {
    
    public boolean isString();
    
    public void setMaxFixedLength(int length);
    
    public int getMaxFixedLength();

}
