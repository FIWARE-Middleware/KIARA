package org.fiware.kiara.dynamic.data;

public interface DynamicPrimitive extends DynamicData {
    
    public boolean set(Object value);
    
    public Object get();
    
    public boolean set(DynamicData value);

}
