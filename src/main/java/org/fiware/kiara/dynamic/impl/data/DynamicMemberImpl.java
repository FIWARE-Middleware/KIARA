package org.fiware.kiara.dynamic.impl.data;

import org.fiware.kiara.dynamic.data.DynamicData;
import org.fiware.kiara.dynamic.data.DynamicMember;

public class DynamicMemberImpl implements DynamicMember { 
    
    private String m_name; 
    private DynamicData m_dynamicData;
    
    public DynamicMemberImpl(DynamicData dynamicData, String name) {
        this.m_name = name;
        this.m_dynamicData = dynamicData;
    }

    @Override
    public String getName() {
        return m_name;
    }

    @Override
    public DynamicData getDynamicData() {
        return m_dynamicData;
    }
    
    

}
