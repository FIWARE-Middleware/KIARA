package org.fiware.kiara.dynamic.data;

public interface DynamicUnion extends DynamicMembered {
    
    public void _d(Object value);
    
    public Object _d();
    
    public DynamicData getMember(String name);
    
    public void setMember(String name, DynamicData data);

}
