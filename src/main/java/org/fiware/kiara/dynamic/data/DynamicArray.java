package org.fiware.kiara.dynamic.data;

public interface DynamicArray extends DynamicContainer {
    
    public DynamicData getElementAt(int... position);
    
    public boolean setElementAt(DynamicData value, int... position);

}
