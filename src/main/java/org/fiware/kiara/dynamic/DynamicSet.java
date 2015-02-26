package org.fiware.kiara.dynamic;

public interface DynamicSet extends DynamicContainer {
    
    public DynamicData getElementAt(int index);
    
    public boolean setElementAt(DynamicData value, int index);

}
