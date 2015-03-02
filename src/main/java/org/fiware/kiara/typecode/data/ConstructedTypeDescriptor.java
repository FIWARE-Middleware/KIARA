package org.fiware.kiara.typecode.data;

public interface ConstructedTypeDescriptor extends DataTypeDescriptor {
    
    //public boolean setDimensions(int... dimensions);
    
    public void setMaxSize(int length);
    
    public int getMaxSize();
    
    

}
