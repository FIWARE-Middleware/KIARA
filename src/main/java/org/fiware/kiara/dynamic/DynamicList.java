package org.fiware.kiara.dynamic;

public interface DynamicList extends DynamicContainer {
    
    public DynamicData getElementAt(int index);
    
    public boolean setElementAt(DynamicData value, int index);

}
