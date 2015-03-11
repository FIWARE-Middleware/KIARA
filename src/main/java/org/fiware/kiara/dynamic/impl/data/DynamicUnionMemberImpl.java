package org.fiware.kiara.dynamic.impl.data;

import java.util.ArrayList;

import org.fiware.kiara.dynamic.data.DynamicData;
import org.fiware.kiara.dynamic.data.DynamicMember;

public class DynamicUnionMemberImpl<T> extends DynamicMemberImpl implements DynamicMember {

    private ArrayList<T> m_labels;
    
    private boolean m_isDefault;
    
    public DynamicUnionMemberImpl(DynamicData dynamicData, String name, boolean isDefault, ArrayList<T> labels) {
        super(dynamicData, name);
        this.m_labels = labels;
        this.m_isDefault = isDefault;
    }
    
    public ArrayList<T> getLabels() {
        return this.m_labels;
    }
    
    public boolean isDefault() {
        return this.m_isDefault;
    }
    
    

}
