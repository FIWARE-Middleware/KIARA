package org.fiware.kiara.dynamic.data;

public interface DynamicSet extends DynamicContainer {
    
    /*public DynamicData getElementAt(int index);
    
    public boolean setElementAt(DynamicData value, int index);*/
    
    public boolean add(DynamicData element);
    
    public void add(int index, DynamicData element);
    
    public DynamicData get(int index);
    
    public boolean isEmpty();

}
