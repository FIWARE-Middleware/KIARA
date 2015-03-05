package org.fiware.kiara.dynamic.data;

public interface DynamicList extends DynamicContainer {
    
    public boolean add(DynamicData element);
    
    public void add(int index, DynamicData element);
    
    public DynamicData get(int index);
    
    public boolean isEmpty();

}
