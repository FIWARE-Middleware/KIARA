package org.fiware.kiara.dynamic.data;


public interface DynamicMap extends DynamicContainer {
    
    public boolean put(DynamicData key, DynamicData value);
    
    public boolean containsKey(DynamicData key);
    
    public boolean containsValue(DynamicData value);
    
    public DynamicData get(DynamicData key);
    

}
