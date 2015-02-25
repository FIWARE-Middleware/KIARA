package org.fiware.kiara.dynamic.impl.data;

import java.util.ArrayList;

import org.fiware.kiara.dynamic.DynamicData;
import org.fiware.kiara.dynamic.impl.DynamicTypeImpl;
import org.fiware.kiara.exceptions.DynamicTypeException;
import org.fiware.kiara.typecode.impl.data.DataTypeDescriptor;

public class DynamicContainerImpl extends DynamicConstructedImpl {
    
    protected DynamicDataImpl m_contentType;
    protected ArrayList<DynamicData> m_members;
    
    public DynamicContainerImpl(DataTypeDescriptor dataDescriptor, String className) {
        super(dataDescriptor, className);
    }
    
    public DynamicTypeImpl getContentType() {
        return this.m_contentType;
    }
    
    public void setContentType(DynamicDataImpl contentType) {
        this.m_contentType = contentType;
    }
    
    

}
