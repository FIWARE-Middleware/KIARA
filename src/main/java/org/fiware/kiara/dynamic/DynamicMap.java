package org.fiware.kiara.dynamic;


public interface DynamicMap extends DynamicConstructed {
    
    public boolean setElementAt(DynamicData key, DynamicData value, int index);
    
    public DynamicData getKeyAt(int index);
    
    public DynamicData getValueAt(int index);
    
    public DynamicData getValue(DynamicData key);

}
